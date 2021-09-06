package com.merfemor.vkwallwatcher.data;

import edu.umd.cs.findbugs.annotations.NonNull;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface VkWallWatchSubscriptionRepository extends MongoRepository<VkWallWatchSubscription, String> {
    @NonNull
    List<VkWallWatchSubscription> findByChatId(long chatId);
}
