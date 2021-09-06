package com.merfemor.vkwallwatcher.telegram.command

import com.merfemor.vkwallwatcher.telegram.MessageFormatter
import com.merfemor.vkwallwatcher.telegram.SendHelper
import org.springframework.stereotype.Component
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender

@Component
internal class CancelCommand(
    private val sendHelper: SendHelper,
    private val messageFormatter: MessageFormatter
) : BotCommand("cancel", "Cancel interactive input") {
    private val chatIdToProcessor = mutableMapOf<Long, Runnable>()

    fun setProcessorForChat(chatId: Long, processor: Runnable?) {
        if (processor == null) {
            chatIdToProcessor.remove(chatId)
        } else {
            chatIdToProcessor[chatId] = processor
        }
    }

    override fun execute(absSender: AbsSender, user: User, chat: Chat, arguments: Array<out String>) {
        val processor = chatIdToProcessor[chat.id]
        if (processor != null) {
            processor.run()
            return
        }
        sendHelper.sendTextMessage(chat.id, absSender, messageFormatter.formatError("Nothing to cancel"))
    }
}