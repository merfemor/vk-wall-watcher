package com.merfemor.vkwallwatcher.telegram

import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.Update

@Component
internal class NonCommandUpdateProcessor {
    fun process(update: Update) = Unit // not implemented
}
