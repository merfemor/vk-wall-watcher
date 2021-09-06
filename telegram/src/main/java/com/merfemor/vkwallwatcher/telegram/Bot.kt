package com.merfemor.vkwallwatcher.telegram

import com.merfemor.vkwallwatcher.telegram.command.CallbackQueryProcessorHolder
import com.merfemor.vkwallwatcher.telegram.command.CommandRegistry
import com.merfemor.vkwallwatcher.telegram.command.withFilter
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
    private val messagesFilter: MessagesFilter,
    commandRegistry: CommandRegistry
) : TelegramLongPollingCommandBot() {

    init {
        commandRegistry.forEachCommand { register(it.withFilter(messagesFilter)) }
    }

    override fun getBotToken(): String = properties.botToken

    override fun getBotUsername(): String = properties.botUsername

    override fun processNonCommandUpdate(update: Update) {
        if (update.callbackQuery != null) {
            processCallbackQuery(update.callbackQuery)
            return
        }
        val message = update.message
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
}
