package com.merfemor.vkwallwatcher.telegram

import org.springframework.stereotype.Component

@Component
class TelegramMessageApi internal constructor(
        private val bot: Bot,
        private val sendHelper: SendHelper
) {
    fun sendTextMessage(chatId: Long, messageText: String,
                        enableHtml: Boolean = false,
                        enabledWebPagePreview: Boolean = true) {
        sendHelper.sendTextMessage(chatId, bot, messageText, enableHtml, enabledWebPagePreview)
    }
}