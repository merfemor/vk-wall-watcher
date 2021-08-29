package com.merfemor.vkwallwatcher.telegram

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession

@Service
internal class TelegramService(bot: Bot) {
    init {
        val api = TelegramBotsApi(DefaultBotSession::class.java)
        api.registerBot(bot)
        logger.info("Started bot session")
    }

    private companion object {
        private val logger = LoggerFactory.getLogger(TelegramService::class.java)
    }
}