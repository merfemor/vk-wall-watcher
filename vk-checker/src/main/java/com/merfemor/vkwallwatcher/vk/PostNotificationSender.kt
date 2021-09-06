package com.merfemor.vkwallwatcher.vk

import com.merfemor.vkwallwatcher.data.VkWallWatchSubscription
import com.merfemor.vkwallwatcher.telegram.TelegramMessageApi
import com.vk.api.sdk.objects.wall.WallpostFull
import org.springframework.stereotype.Component

private val NOTIFICATION_MESSAGE_FMT = """
    New %s posts in community %s with query "%s":
    
    %s
""".trimIndent()

private val POST_ROW_FMT = """
    %d. %s
    %s
""".trimIndent()

@Component
internal class PostNotificationSender(
        private val vkApi: VkApi,
        private val telegramMessageApi: TelegramMessageApi
) {
    private fun constructMessage(communityId: Int, query: String, posts: Collection<WallpostFull>): String {
        val limitedPosts = posts.take(MAX_POSTS_IN_MESSAGE) // TODO: handle telegram max message size instead of dropping posts

        val communityInfo = vkApi.getGroupInfoById(communityId)
        val postListStr = limitedPosts.mapIndexed { index, post ->
            val ellipsizedText = post.text.ellipsize(MAX_TEXT_LENGTH)
            val linkToPost = vkApi.getLinkForPost(communityInfo.screenName, communityInfo.id, post.id)
            POST_ROW_FMT.format(index + 1, ellipsizedText, linkToPost)
        }.joinToString(separator = "\n\n")

        val postsNumberString = if (limitedPosts.size < posts.size) {
            "${posts.size} (showing only ${limitedPosts.size})"
        } else {
            "${posts.size}"
        }
        return NOTIFICATION_MESSAGE_FMT.format(postsNumberString, communityInfo.name, query, postListStr)
    }

    fun notifyAboutNewPosts(subscription: VkWallWatchSubscription, posts: Collection<WallpostFull>) {
        if (posts.isEmpty()) {
            return
        }
        val messageText = constructMessage(subscription.communityId, subscription.query, posts)
        telegramMessageApi.sendPlainTextMessage(subscription.chatId, messageText)
    }

    private companion object {
        private const val TELEGRAM_MAX_MESSAGE_SIZE = 4096
        private const val MAX_TEXT_LENGTH = 300
        private const val MAX_POSTS_IN_MESSAGE = 5

        private fun String.ellipsize(maxLength: Int): String {
            if (length <= maxLength) {
                return this
            }
            return take(maxLength - 1) + Typography.ellipsis
        }
    }
}