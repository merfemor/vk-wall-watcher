package com.merfemor.vkwallwatcher.data

import org.springframework.data.mongodb.repository.MongoRepository

interface VkWallWatchSubscriptionRepository : MongoRepository<VkWallWatchSubscription, String> {
    fun findByChatId(chatId: Long): List<VkWallWatchSubscription>
}