package com.merfemor.vkwallwatcher.telegram;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import javax.annotation.CheckForNull;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @implNote not works if written in Kotlin
 */
@ConstructorBinding
@ConfigurationProperties(prefix = "telegram")
@Validated
class TelegramProperties {
    @NotNull
    @NotEmpty
    final String botUsername;
    @NotNull
    @NotEmpty
    final String botToken;
    @CheckForNull
    final String[] permittedUsernames;

    private TelegramProperties(String botUsername, String botToken,
                               @CheckForNull String[] permittedUsernames) {
        this.botUsername = botUsername;
        this.botToken = botToken;
        this.permittedUsernames = permittedUsernames;
    }
}
