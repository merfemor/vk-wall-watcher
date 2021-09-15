package com.merfemor.vkwallwatcher.telegram.filter

import com.merfemor.vkwallwatcher.telegram.TelegramProperties
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.User

@Component
class AdminUsersMessagesFilterImpl(
        private val telegramProperties: TelegramProperties
) : MessagesFilter {
    override val responseMessageForReject: String = "You're not admin"

    override fun test(user: User): Boolean {
        return user.userName in telegramProperties.adminUsernames.orEmpty()
    }
}