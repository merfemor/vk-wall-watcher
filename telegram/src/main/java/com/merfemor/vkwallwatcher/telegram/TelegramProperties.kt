package com.merfemor.vkwallwatcher.telegram

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.NotEmpty

@ConstructorBinding
@ConfigurationProperties(prefix = "telegram")
@Validated
class TelegramProperties private constructor(
        @NotEmpty val botUsername: String,
        @NotEmpty val botToken: String,
        val permittedUsernames: Array<String>?,
        val adminUsernames: Array<String>?)
