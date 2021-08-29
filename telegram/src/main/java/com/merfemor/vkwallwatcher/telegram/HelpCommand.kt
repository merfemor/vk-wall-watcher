package com.merfemor.vkwallwatcher.telegram

import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.User

internal class HelpCommand : BaseCommand("help", DESCRIPTION) {

    override fun fillMessage(message: SendMessage, user: User, chat: Chat,
                             arguments: Array<out String>) {
        message.text = "This is VK communities wall watcher. Development is in progress."
    }

    private companion object {
        private const val DESCRIPTION = "prints help info"
    }
}