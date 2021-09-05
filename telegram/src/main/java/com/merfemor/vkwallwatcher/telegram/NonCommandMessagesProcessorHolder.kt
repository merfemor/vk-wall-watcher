package com.merfemor.vkwallwatcher.telegram

import org.springframework.stereotype.Component

@Component
internal class NonCommandMessagesProcessorHolder(
    private val emptyProcessor: EmptyNonCommandMessageProcessor
) {
    private val chatIdToProcessor = mutableMapOf<Long, NonCommandMessagesProcessor>()

    fun setProcessor(chatId: Long, processor: NonCommandMessagesProcessor?) {
        if (processor == null) {
            chatIdToProcessor.remove(chatId)
        } else {
            chatIdToProcessor[chatId] = processor
        }
    }

    fun getProcessorForChat(chatId: Long): NonCommandMessagesProcessor {
        return chatIdToProcessor.getOrDefault(chatId, emptyProcessor)
    }
}