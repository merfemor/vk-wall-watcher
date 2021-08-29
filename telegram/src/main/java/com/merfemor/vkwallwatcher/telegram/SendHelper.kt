package com.merfemor.vkwallwatcher.telegram

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.bots.AbsSender
import org.telegram.telegrambots.meta.exceptions.TelegramApiException

@Component
internal class SendHelper {
    fun sendTextMessageResponse(message: Message, sender: AbsSender, responseText: String) {
        val response = SendMessage().apply {
            text = responseText
            chatId = message.chatId.toString()
        }
        try {
            sender.execute(response)
        } catch (e: TelegramApiException) {
            logger.error("Failed to send message", e)
        }
    }

    private companion object {
        private val logger = LoggerFactory.getLogger(SendHelper::class.java)
    }
}