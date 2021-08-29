package com.merfemor.vkwallwatcher.telegram

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
internal class TelegramService(bot: Bot) {
    init {
        logger.info("Started bot ${bot.botUsername}")
    }

    private companion object {
        private val logger = LoggerFactory.getLogger(TelegramService::class.java)
    }
}