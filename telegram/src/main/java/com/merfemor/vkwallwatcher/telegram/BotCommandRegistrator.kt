package com.merfemor.vkwallwatcher.telegram

import com.merfemor.vkwallwatcher.telegram.command.CommandRegistry
import com.merfemor.vkwallwatcher.telegram.command.withFilter
import com.merfemor.vkwallwatcher.telegram.filter.PermittedUsersMessagesFilterImpl
import org.springframework.stereotype.Component

@Component
internal class BotCommandRegistrator(
        bot: Bot,
        commandRegistry: CommandRegistry,
        messagesFilter: PermittedUsersMessagesFilterImpl
) {
    init {
        commandRegistry.forEachCommand { bot.register(it.withFilter(messagesFilter)) }
    }
}