package com.merfemor.vkwallwatcher.telegram

import org.springframework.stereotype.Component

@Component
class MessageFormatter {
    fun formatError(message: String) = "❌ $message"

    fun formatSuccess(message: String) = "✅ $message"
}