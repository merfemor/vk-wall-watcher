package com.merfemor.vkwallwatcher.vk

import com.merfemor.vkwallwatcher.data.VkWallWatchSubscription
import com.merfemor.vkwallwatcher.data.VkWallWatchSubscriptionRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import java.util.Date
import java.util.concurrent.Executor

@Component
internal class VkSubscriptionsCheckJob(
        private val subscriptionRepository: VkWallWatchSubscriptionRepository,
        private val vkApi: VkApi,
        private val postNotificationSender: PostNotificationSender,
        @Qualifier(VkCheckerConfiguration.EXECUTOR) private val executor: Executor
) : Runnable {

    private fun processSubscription(subscription: VkWallWatchSubscription) {
        logger.info("Check subscription ${subscription.id} for chat ${subscription.chatId}, previous check")
        val checkStartDate = Date()
        val minDate: Date = subscription.lastCheckedDate ?: subscription.createdDate
        val posts = vkApi.searchGroupAllWallPosts(subscription.communityId, subscription.query, minDate, checkStartDate)
        postNotificationSender.notifyAboutNewPosts(subscription, posts)
        subscription.lastCheckedDate = checkStartDate
        subscriptionRepository.save(subscription)
    }

    private fun checkSubscriptions() {
        logger.info("Start subscriptions check")
        val subscriptions = subscriptionRepository.findAll()
        for (subscription in subscriptions) {
            val task = Runnable {
                processSubscription(subscription)
            }.named("SubscriptionCheck-${subscription.id}")
            executor.execute(task)
        }
    }

    override fun run() = checkSubscriptions()

    private companion object {
        private val logger = LoggerFactory.getLogger(VkSubscriptionsCheckJob::class.java)
    }
}