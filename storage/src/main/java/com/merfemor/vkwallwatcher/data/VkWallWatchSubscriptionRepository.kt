package com.merfemor.vkwallwatcher.data

import org.springframework.data.mongodb.repository.MongoRepository

interface VkWallWatchSubscriptionRepository : MongoRepository<VkWallWatchSubscription, String>