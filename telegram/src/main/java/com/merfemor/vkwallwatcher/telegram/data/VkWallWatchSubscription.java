package com.merfemor.vkwallwatcher.telegram.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.annotation.Nonnull;

/**
 * @implNote mongo fails to create mapper if this class written in Kotlin
 */
@Document("subscription")
public final class VkWallWatchSubscription {
    @Id
    @Nonnull
    public final String id;
    public final long chatId;
    @Nonnull
    public final String communityId;
    @Nonnull
    public final String keywords;

    public VkWallWatchSubscription(long chatId, @Nonnull String communityId, @Nonnull String keywords) {
        this("", chatId, communityId, keywords);
    }

    private VkWallWatchSubscription(@Nonnull String id, long chatId, @Nonnull String communityId,
                                   @Nonnull String keywords) {
        this.id = id;
        this.chatId = chatId;
        this.communityId = communityId;
        this.keywords = keywords;
    }
}
