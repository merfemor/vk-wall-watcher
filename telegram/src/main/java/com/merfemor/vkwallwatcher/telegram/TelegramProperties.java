package com.merfemor.vkwallwatcher.telegram;

import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

/**
 * @implNote not works if written in Kotlin
 */
@ConstructorBinding
@ConfigurationProperties(prefix = "telegram")
class TelegramProperties {
    @NonNull
    final String botUsername;
    @NonNull
    final String botToken;
    @Nullable
    final String[] permittedUsernames;

    TelegramProperties(@NonNull String botUsername, @NonNull String botToken,
                       @Nullable String[] permittedUsernames) {
        this.botUsername = botUsername;
        this.botToken = botToken;
        this.permittedUsernames = permittedUsernames;
    }
}
