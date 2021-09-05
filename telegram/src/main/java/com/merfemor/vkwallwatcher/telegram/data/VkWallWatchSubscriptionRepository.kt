package com.merfemor.vkwallwatcher.telegram.data

import org.springframework.data.mongodb.repository.MongoRepository

interface VkWallWatchSubscriptionRepository : MongoRepository<VkWallWatchSubscription, String>