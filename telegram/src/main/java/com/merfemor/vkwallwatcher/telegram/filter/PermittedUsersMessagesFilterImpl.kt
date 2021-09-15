package com.merfemor.vkwallwatcher.telegram.filter

import com.merfemor.vkwallwatcher.telegram.TelegramProperties
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.User

@Component
internal class PermittedUsersMessagesFilterImpl(
    private val properties: TelegramProperties
) : MessagesFilter {

    override val responseMessageForReject = "You shall not pass!"

    override fun test(user: User): Boolean {
        val usernames = properties.permittedUsernames
        if (usernames.isNullOrEmpty()) {
            return true
        }
        return user.userName in usernames
    }
}