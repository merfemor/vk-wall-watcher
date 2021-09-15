package com.merfemor.vkwallwatcher.vk

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

@ConfigurationProperties(prefix = "vk")
@ConstructorBinding
@Validated
class VkProperties private constructor(
        @NotNull val appId: Int,
        @NotEmpty val clientSecret: String,
        @NotEmpty val accessToken: String,
        @NotEmpty val checkScheduleCron: String)