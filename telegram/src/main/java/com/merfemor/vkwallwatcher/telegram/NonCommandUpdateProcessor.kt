package com.merfemor.vkwallwatcher.telegram

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.bots.AbsSender
import org.telegram.telegrambots.meta.exceptions.TelegramApiException

@Component
internal class NonCommandUpdateProcessor(
    private val messagesFilter: MessagesFilter,
    private val sendHelper: SendHelper
) {

    fun process(update: Update, sender: AbsSender) {
        val message = update.message
        if (!messagesFilter.test(message.from)) {
            messagesFilter.responseMessageForReject?.let {
                sendHelper.sendTextMessageResponse(message, sender, it)
            }
            return
        }
        sendHelper.sendTextMessageResponse(message, sender, "Non command message not yet supported")
    }
}
