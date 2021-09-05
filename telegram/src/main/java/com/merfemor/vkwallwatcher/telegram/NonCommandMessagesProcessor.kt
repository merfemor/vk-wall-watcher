package com.merfemor.vkwallwatcher.telegram

import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.bots.AbsSender

interface NonCommandMessagesProcessor {
    fun process(update: Update, sender: AbsSender)
}