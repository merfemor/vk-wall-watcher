package com.merfemor.vkwallwatcher.vk

import com.merfemor.vkwallwatcher.data.VkWallWatchSubscription
import com.merfemor.vkwallwatcher.data.VkWallWatchSubscriptionRepository
import com.vk.api.sdk.objects.wall.WallpostFull
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.Date

@Component
internal class ScheduledVkSubscriptionsChecker(
        private val subscriptionRepository: VkWallWatchSubscriptionRepository,
        private val vkApi: VkApi
) {
    private fun notifyAboutNewPosts(subscription: VkWallWatchSubscription, posts: Collection<WallpostFull>) {
        if (posts.isEmpty()) {
            return
        }
        logger.info("Notifying chat ${subscription.chatId} about ${posts.size} new posts...")
        // TODO: notify about new posts through telegram
    }

    private fun processSubscription(subscription: VkWallWatchSubscription) {
        val checkStartDate = Date()
        val minDate: Date = subscription.lastCheckedDate ?: subscription.createdDate
        val posts = vkApi.searchGroupAllWallPosts(subscription.communityId, subscription.query, minDate, checkStartDate)
        notifyAboutNewPosts(subscription, posts)
        subscription.lastCheckedDate = checkStartDate
        subscriptionRepository.save(subscription)
    }

    @Scheduled(cron = "\${vk.check_schedule_cron}")
    private fun checkSubscriptions() {
        val subscriptions = subscriptionRepository.findAll()
        for (subscription in subscriptions) {
            processSubscription(subscription)
        }
    }

    private companion object {
        private val logger = LoggerFactory.getLogger(ScheduledVkSubscriptionsChecker::class.java)
    }
}