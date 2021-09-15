package com.merfemor.vkwallwatcher.vk;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @implNote validation not works if implemented in Kotlin
 */
@ConfigurationProperties(prefix = "vk")
@ConstructorBinding
@Validated
public class VkProperties {
    @NotNull
    public final Integer appId;
    @NotNull
    @NotEmpty
    public final String clientSecret;
    @NotNull
    @NotEmpty
    public final String accessToken;
    @NotNull
    @NotEmpty
    public final String checkScheduleCron;

    private VkProperties(Integer appId, String clientSecret, String accessToken, String checkScheduleCron) {
        this.appId = appId;
        this.clientSecret = clientSecret;
        this.accessToken = accessToken;
        this.checkScheduleCron = checkScheduleCron;
    }
}
