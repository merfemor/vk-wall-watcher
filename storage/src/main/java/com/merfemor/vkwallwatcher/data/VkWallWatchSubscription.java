package com.merfemor.vkwallwatcher.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.NonNull;

/**
 * @implNote mongo fails to create mapper if this class written in Kotlin
 */
@Document("subscription")
public final class VkWallWatchSubscription {
    @Id
    @NonNull
    public final String id;
    public final long chatId;
    @NonNull
    public final String communityId;
    @NonNull
    public final String keywords;

    public VkWallWatchSubscription(long chatId, @NonNull String communityId, @NonNull String keywords) {
        this("", chatId, communityId, keywords);
    }

    private VkWallWatchSubscription(@NonNull String id, long chatId, @NonNull String communityId,
                                   @NonNull String keywords) {
        this.id = id;
        this.chatId = chatId;
        this.communityId = communityId;
        this.keywords = keywords;
    }
}
