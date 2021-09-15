package com.merfemor.vkwallwatcher.telegram;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import javax.annotation.CheckForNull;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @implNote validation not works if implemented in Kotlin
 */
@ConstructorBinding
@ConfigurationProperties(prefix = "telegram")
@Validated
public class TelegramProperties {
    @NotNull
    @NotEmpty
    public final String botUsername;
    @NotNull
    @NotEmpty
    public final String botToken;
    @CheckForNull
    public final String[] permittedUsernames;
    @CheckForNull
    public final String[] adminUsernames;

    private TelegramProperties(String botUsername, String botToken,
                               @CheckForNull String[] permittedUsernames,
                               @CheckForNull String[] adminUsernames) {
        this.botUsername = botUsername;
        this.botToken = botToken;
        this.permittedUsernames = permittedUsernames;
        this.adminUsernames = adminUsernames;
    }
}
