package com.merfemor.vkwallwatcher.vk

import com.merfemor.vkwallwatcher.data.VkWallWatchSubscription
import com.merfemor.vkwallwatcher.telegram.TelegramMessageApi
import com.vk.api.sdk.objects.groups.responses.GetByIdLegacyResponse
import com.vk.api.sdk.objects.wall.WallpostFull
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

private val NOTIFICATION_MESSAGE_FMT = """
    <b>New %s posts in community "%s" with query "%s"</b>
    
    %s
""".trimIndent()

private val POST_ROW_FMT = """
    %d. %s
    <a href="%s">🔗 Link</a>
""".trimIndent()

@Component
internal class PostNotificationSender(
        private val vkApi: VkApi,
        private val telegramMessageApi: TelegramMessageApi
) {
    private fun constructPostString(postIndex: Int, communityInfo: GetByIdLegacyResponse, post: WallpostFull): String {
        val ellipsizedText = post.text.ellipsize(MAX_TEXT_LENGTH)
        val linkToPost = vkApi.getLinkForPost(communityInfo.screenName, communityInfo.id, post.id)
        return POST_ROW_FMT.format(postIndex + 1, ellipsizedText, linkToPost)
    }

    private fun constructMessages(communityId: Int, query: String,
                                  posts: Collection<WallpostFull>): Sequence<String> = sequence {
        val limitedPosts = posts.asSequence().take(MAX_POSTS_IN_MESSAGE)
        val postsNumberString = if (posts.size > MAX_POSTS_IN_MESSAGE) {
            "${posts.size} (showing only $MAX_POSTS_IN_MESSAGE)"
        } else {
            "${posts.size}"
        }

        val communityInfo = vkApi.getGroupInfoById(communityId)

        val postStrings = limitedPosts.mapIndexed { index, post ->
            constructPostString(index, communityInfo, post)
        }
        val sb = StringBuilder()
        val maxLengthForPostsString = TELEGRAM_MAX_MESSAGE_SIZE - NOTIFICATION_MESSAGE_FMT.length

        fun constructFullMessageString(): String {
            val postsString = sb.toString()
            return NOTIFICATION_MESSAGE_FMT.format(postsNumberString, communityInfo.name, query, postsString)
        }

        val postsSeparator = "\n\n"

        for (postString in postStrings) {
            if (sb.length + postString.length + postsSeparator.length > maxLengthForPostsString) {
                yield(constructFullMessageString())
                sb.clear()
            }
            if (sb.isNotEmpty()) {
                sb.append(postsSeparator)
            }
            sb.append(postString)
        }
        yield(constructFullMessageString())
    }

    fun notifyAboutNewPosts(subscription: VkWallWatchSubscription, posts: Collection<WallpostFull>) {
        if (posts.isEmpty()) {
            logger.info("No posts to notify in subscription ${subscription.id}")
            return
        }
        logger.info("Notifying about ${posts.size} posts in subscription ${subscription.id}")
        for (messageTextPart in constructMessages(subscription.communityId, subscription.query, posts)) {
            telegramMessageApi.sendTextMessage(subscription.chatId, messageTextPart,
                    enableHtml = true, enabledWebPagePreview = false)
        }
    }

    private companion object {
        private const val TELEGRAM_MAX_MESSAGE_SIZE = 4096
        private const val MAX_TEXT_LENGTH = 100
        private const val MAX_POSTS_IN_MESSAGE = 30

        private fun String.ellipsize(maxLength: Int): String {
            if (length <= maxLength) {
                return this
            }
            return take(maxLength - 1) + Typography.ellipsis
        }

        private val logger = LoggerFactory.getLogger(PostNotificationSender::class.java)
    }
}