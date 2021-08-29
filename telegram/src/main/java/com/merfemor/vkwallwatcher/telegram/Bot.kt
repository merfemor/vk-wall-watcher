package com.merfemor.vkwallwatcher.telegram

import org.springframework.stereotype.Component
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot
import org.telegram.telegrambots.meta.api.objects.Update

@Component
internal class Bot(
    private val properties: TelegramProperties,
    private val nonCommandUpdateProcessor: NonCommandUpdateProcessor,
    messagesFilter: MessagesFilter
) : TelegramLongPollingCommandBot() {

    init {
        register(HelpCommand().withFilter(messagesFilter))
    }

    override fun getBotToken(): String = properties.botToken

    override fun getBotUsername(): String = properties.botUsername

    override fun processNonCommandUpdate(update: Update) =
        nonCommandUpdateProcessor.process(update, this)
}
