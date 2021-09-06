package com.merfemor.vkwallwatcher.vk

import com.merfemor.vkwallwatcher.data.VkWallWatchSubscription
import com.merfemor.vkwallwatcher.data.VkWallWatchSubscriptionRepository
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.Date

@Component
internal class ScheduledVkSubscriptionsChecker(
        private val subscriptionRepository: VkWallWatchSubscriptionRepository,
        private val vkApi: VkApi,
        private val postNotificationSender: PostNotificationSender
) {

    private fun processSubscription(subscription: VkWallWatchSubscription) {
        val checkStartDate = Date()
        val minDate: Date = subscription.lastCheckedDate ?: subscription.createdDate
        val posts = vkApi.searchGroupAllWallPosts(subscription.communityId, subscription.query, minDate, checkStartDate)
        postNotificationSender.notifyAboutNewPosts(subscription, posts)
        subscription.lastCheckedDate = checkStartDate
        subscriptionRepository.save(subscription)
    }

    @Scheduled(cron = "\${vk.check_schedule_cron}")
    private fun checkSubscriptions() {
        logger.info("Start scheduled subscriptions check")
        val subscriptions = subscriptionRepository.findAll()
        for (subscription in subscriptions) {
            processSubscription(subscription)
        }
    }

    private companion object {
        private val logger = LoggerFactory.getLogger(ScheduledVkSubscriptionsChecker::class.java)
    }
}