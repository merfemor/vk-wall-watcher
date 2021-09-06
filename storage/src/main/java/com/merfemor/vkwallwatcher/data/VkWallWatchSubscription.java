package com.merfemor.vkwallwatcher.data;

import edu.umd.cs.findbugs.annotations.NonNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

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
    public final String query;
    @CreatedDate
    public final Date createdDate;

    public VkWallWatchSubscription(long chatId, int communityId, @NonNull String query) {
        this("", chatId, communityId, query, new Date());
    }

    @PersistenceConstructor
    private VkWallWatchSubscription(@NonNull String id, long chatId, int communityId,
                                    @NonNull String query, @NonNull Date createdDate) {
        this.id = id;
        this.chatId = chatId;
        this.communityId = communityId;
        this.query = query;
        this.createdDate = createdDate;
    }
}
