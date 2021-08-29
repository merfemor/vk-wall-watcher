package com.merfemor.vkwallwatcher.telegram;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @implNote not works if written in Kotlin
 */
@ConstructorBinding
@ConfigurationProperties(prefix = "telegram")
class TelegramProperties {
    @Nonnull
    final String botUsername;
    @Nonnull
    final String botToken;
    @Nullable
    final String[] permittedUsernames;

    TelegramProperties(@Nonnull String botUsername, @Nonnull String botToken,
                       @Nullable String[] permittedUsernames) {
        this.botUsername = botUsername;
        this.botToken = botToken;
        this.permittedUsernames = permittedUsernames;
    }
}
