package com.merfemor.vkwallwatcher.telegram

import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.bots.AbsSender

@Component
internal class EmptyNonCommandMessageProcessor(
    private val sendHelper: SendHelper
) : NonCommandMessagesProcessor {
    override fun process(update: Update, sender: AbsSender) {
        sendHelper.sendTextMessage(update.message.chatId, sender, "I don't understand you")
    }
}