package com.merfemor.vkwallwatcher.telegram

import com.merfemor.vkwallwatcher.telegram.command.CallbackQueryProcessorHolder
import com.merfemor.vkwallwatcher.telegram.filter.PermittedUsersMessagesFilterImpl
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot
import org.telegram.telegrambots.meta.api.objects.CallbackQuery
import org.telegram.telegrambots.meta.api.objects.Update

@Component
internal class Bot(
    private val properties: TelegramProperties,
    private val sendHelper: SendHelper,
    private val nonCommandMessagesProcessor: NonCommandMessagesProcessorHolder,
    private val callbackQueryProcessor: CallbackQueryProcessorHolder,
    private val messagesFilter: PermittedUsersMessagesFilterImpl
) : TelegramLongPollingCommandBot() {

    override fun getBotToken(): String = properties.botToken

    override fun getBotUsername(): String = properties.botUsername

    override fun processNonCommandUpdate(update: Update) {
        if (update.callbackQuery != null) {
            processCallbackQuery(update.callbackQuery)
            return
        }
        val message = update.message
        if (message == null) {
            log.info("Skip non-message and non callback update {}", message)
            return
        }
        if (!messagesFilter.test(message.from)) {
            messagesFilter.responseMessageForReject?.let {
                sendHelper.sendTextMessage(message.chatId, this, it)
            }
            return
        }
        val processor = nonCommandMessagesProcessor.getProcessorForChat(message.chatId)
        processor.process(update, this)
    }

    private fun processCallbackQuery(callbackQuery: CallbackQuery) {
        if (!messagesFilter.test(callbackQuery.from)) {
            return
        }
        callbackQueryProcessor.process(callbackQuery, this)
    }

    private companion object {
        private val log = LoggerFactory.getLogger(Bot::class.java)
    }
}
