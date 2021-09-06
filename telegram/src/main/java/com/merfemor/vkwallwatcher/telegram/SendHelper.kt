package com.merfemor.vkwallwatcher.telegram

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.bots.AbsSender
import org.telegram.telegrambots.meta.exceptions.TelegramApiException

@Component
internal class SendHelper {
    fun sendTextMessage(chatIdLong: Long, sender: AbsSender, plainText: String,
                        enabledHtml: Boolean = false,
                        enabledPreview: Boolean = true) {
        sendTextMessage(chatIdLong, sender) {
            text = plainText
            enableHtml(enabledHtml)
            if (enabledPreview) {
                enableWebPagePreview()
            } else {
                disableWebPagePreview()
            }
        }
    }

    private fun sendTextMessage(chatIdLong: Long, sender: AbsSender, modifier: SendMessage.() -> Unit) {
        val response = SendMessage().apply {
            chatId = chatIdLong.toString()
            modifier()
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