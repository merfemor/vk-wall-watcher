package com.merfemor.vkwallwatcher.telegram.command

import org.telegram.telegrambots.meta.api.objects.CallbackQuery
import org.telegram.telegrambots.meta.bots.AbsSender

interface CallbackQueryProcessor {
    fun process(callbackQuery: CallbackQuery, absSender: AbsSender)
}