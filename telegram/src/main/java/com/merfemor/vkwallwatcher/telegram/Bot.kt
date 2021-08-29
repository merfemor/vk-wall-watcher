package com.merfemor.vkwallwatcher.telegram

import org.springframework.stereotype.Component
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot
import org.telegram.telegrambots.meta.api.objects.Update

@Component
internal class Bot(
    private val properties: TelegramProperties
) : TelegramLongPollingCommandBot() {

    override fun getBotToken(): String = properties.botToken

    override fun getBotUsername(): String = properties.botUsername

    override fun processNonCommandUpdate(update: Update?) {
        TODO("Not yet implemented")
    }
}
