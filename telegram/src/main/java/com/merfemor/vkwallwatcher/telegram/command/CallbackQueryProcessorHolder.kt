package com.merfemor.vkwallwatcher.telegram.command

import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.CallbackQuery
import org.telegram.telegrambots.meta.bots.AbsSender

@Component
internal class CallbackQueryProcessorHolder : CallbackQueryProcessor {
    private val chatIdToCallbackQueryProcessor = mutableMapOf<Long, CallbackQueryProcessor>()

    fun registerProcessorForChat(chatId: Long, processor: CallbackQueryProcessor) {
        if (chatIdToCallbackQueryProcessor.containsKey(chatId)) {
            throw IllegalStateException("Processor registered twice for $chatId")
        }
        chatIdToCallbackQueryProcessor[chatId] = processor
    }

    fun unregisterProcessorForChat(chatId: Long) {
        if (!chatIdToCallbackQueryProcessor.containsKey(chatId)) {
            throw IllegalStateException("Processor was not registered for chat $chatId")
        }
        chatIdToCallbackQueryProcessor.remove(chatId)
    }

    fun hasProcessorForChat(chatId: Long) = chatIdToCallbackQueryProcessor.containsKey(chatId)

    override fun process(callbackQuery: CallbackQuery, absSender: AbsSender) {
        val processor = chatIdToCallbackQueryProcessor[callbackQuery.message.chatId]
        processor?.process(callbackQuery, absSender)
    }
}