package com.merfemor.vkwallwatcher.vk

import com.merfemor.vkwallwatcher.data.VkWallWatchSubscription
import com.merfemor.vkwallwatcher.data.VkWallWatchSubscriptionRepository
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
internal class ScheduledVkSubscriptionsChecker(
        private val subscriptionRepository: VkWallWatchSubscriptionRepository
) {
    private fun processSubscription(subscription: VkWallWatchSubscription) {
        logger.info("Check subscription ${subscription.communityId}...")
        // TODO: implement check subscription with count/offset, support filter by date
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