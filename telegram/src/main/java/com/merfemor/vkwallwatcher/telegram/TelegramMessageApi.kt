package com.merfemor.vkwallwatcher.telegram

import org.springframework.stereotype.Component

@Component
class TelegramMessageApi internal constructor(
        private val bot: Bot,
        private val sendHelper: SendHelper
) {
    fun sendPlainTextMessage(chatId: Long, messageText: String) {
        sendHelper.sendTextMessageResponse(chatId, bot, messageText)
    }
}