package com.merfemor.vkwallwatcher.telegram.command

import com.merfemor.vkwallwatcher.telegram.SendHelper
import org.springframework.stereotype.Component
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand
import org.telegram.telegrambots.meta.api.methods.ParseMode
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender

@Component
internal class ListWatchCommand(
        private val subscriptionCommandsHelper: SubscriptionCommandsHelper,
        private val sendHelper: SendHelper
) : BotCommand("/allwatch", "List all active VK wall watch subscriptions") {

    override fun execute(absSender: AbsSender, user: User, chat: Chat, arguments: Array<String>) {
        val subs = subscriptionCommandsHelper.getSubscriptionsShortInfoList(chat.id)
        val text = subscriptionCommandsHelper.createSubscriptionsListText(subs)
        val msg = SendMessage.builder()
                .chatId(chat.id.toString())
                .text(text)
                .parseMode(ParseMode.HTML)
                .disableWebPagePreview(true)
                .build()
        sendHelper.sendMessageCatchingErrors(absSender, msg)
    }
}