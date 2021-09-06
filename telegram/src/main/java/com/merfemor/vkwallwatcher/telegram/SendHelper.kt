package com.merfemor.vkwallwatcher.telegram

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.ParseMode
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.bots.AbsSender
import org.telegram.telegrambots.meta.exceptions.TelegramApiException

@Component
internal class SendHelper {
    fun sendTextMessage(chatIdLong: Long, sender: AbsSender, plainText: String,
                        enabledHtml: Boolean = false,
                        enabledPreview: Boolean = true) {
        val message = SendMessage.builder()
                .chatId(chatIdLong.toString())
                .text(plainText)
                .disableWebPagePreview(!enabledPreview)
                .apply {
                    if (enabledHtml) {
                        parseMode(ParseMode.HTML)
                    }
                }
                .build()
        sendMessageCatchingErrors(sender, message)
    }

    fun sendMessageCatchingErrors(sender: AbsSender, sendMessage: SendMessage) {
        try {
            sender.execute(sendMessage)
        } catch (e: TelegramApiException) {
            logger.error("Failed to send message", e)
        }
    }

    private companion object {
        private val logger = LoggerFactory.getLogger(SendHelper::class.java)
    }
}