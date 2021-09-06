package com.merfemor.vkwallwatcher.vk;

import edu.umd.cs.findbugs.annotations.NonNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties(prefix = "vk")
@ConstructorBinding
public class VkProperties {
    public final int appId;
    @NonNull
    public final String clientSecret;
    @NonNull
    public final String accessToken;
    @NonNull
    public final String checkScheduleCron;

    private VkProperties(int appId, @NonNull String clientSecret, @NonNull String accessToken,
                 @NonNull String checkScheduleCron) {
        this.appId = appId;
        this.clientSecret = clientSecret;
        this.accessToken = accessToken;
        this.checkScheduleCron = checkScheduleCron;
    }
}
