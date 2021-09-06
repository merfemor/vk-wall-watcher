package com.merfemor.vkwallwatcher.data;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.NonNull;

import java.util.Date;

/**
 * @implNote mongo fails to create mapper if this class written in Kotlin
 */
@Document("subscription")
public final class VkWallWatchSubscription {
    @Id
    @NonNull
    public final String id;
    public final long chatId;
    public final int communityId;
    @NonNull
    public final String keywords;
    @CreatedDate
    @NonNull
    public final Date createdDate;

    public VkWallWatchSubscription(long chatId, int communityId, @NonNull String keywords) {
        this("", chatId, communityId, keywords, new Date());
    }

    @PersistenceConstructor
    private VkWallWatchSubscription(@NonNull String id, long chatId, int communityId,
                                    @NonNull String keywords, @NonNull Date createdDate) {
        this.id = id;
        this.chatId = chatId;
        this.communityId = communityId;
        this.keywords = keywords;
        this.createdDate = createdDate;
    }
}
