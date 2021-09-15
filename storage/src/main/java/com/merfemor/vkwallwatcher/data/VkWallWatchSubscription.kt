package com.merfemor.vkwallwatcher.data

import org.springframework.data.annotation.PersistenceConstructor
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.Date

@Document("subscription")
class VkWallWatchSubscription @PersistenceConstructor private constructor(
        @field:Id val id: String,
        val chatId: Long,
        val communityId: Int,
        val query: String,
        @field:CreatedDate val createdDate: Date,
        var lastCheckedDate: Date?) {

    constructor(chatId: Long, communityId: Int, query: String) :
            this("", chatId, communityId, query, Date(), null)
}