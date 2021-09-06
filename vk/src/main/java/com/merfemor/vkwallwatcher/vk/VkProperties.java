package com.merfemor.vkwallwatcher.vk;

import edu.umd.cs.findbugs.annotations.NonNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties(prefix = "vk")
@ConstructorBinding
class VkProperties {
    final int appId;
    @NonNull
    final String clientSecret;
    @NonNull
    final String accessToken;
    @NonNull
    final String checkScheduleCron;

    VkProperties(int appId, @NonNull String clientSecret, @NonNull String accessToken,
                 @NonNull String checkScheduleCron) {
        this.appId = appId;
        this.clientSecret = clientSecret;
        this.accessToken = accessToken;
        this.checkScheduleCron = checkScheduleCron;
    }
}
