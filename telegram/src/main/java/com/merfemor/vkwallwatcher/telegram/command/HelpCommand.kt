package com.merfemor.vkwallwatcher.telegram.command

import com.merfemor.vkwallwatcher.telegram.SendHelper
import org.springframework.stereotype.Component
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender

private val MSG = """
    Hello!
    I am VK wall watcher bot. I can subscribe you on posts of some VK community filtered by the query that you specify.
    After that I will periodically check community for new posts and send them to you here. Of course you can also delete subscription.
     
    Here is the list of available commands:
    /help - to print this help message
    /newwatch - create new subscription
    /allwatch - show all active subscriptions
    /deletewatch - delete subscriptions
    
    You can start with /newwatch
""".trimIndent()

@Component
internal class HelpCommand(
        private val sendHelper: SendHelper
) : BotCommand("help", "prints help message") {
    override fun execute(absSender: AbsSender, user: User, chat: Chat, arguments: Array<String>) {
        sendHelper.sendTextMessage(chat.id, absSender, MSG, enabledHtml = true)
    }
}