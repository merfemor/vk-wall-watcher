package com.merfemor.vkwallwatcher.telegram.command

import org.slf4j.LoggerFactory
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender
import org.telegram.telegrambots.meta.exceptions.TelegramApiException


internal abstract class BaseCommand(
    commandIdentifier: String,
    description: String
) : BotCommand(commandIdentifier, description) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun execute(absSender: AbsSender, user: User, chat: Chat, arguments: Array<out String>) {
        val message = SendMessage()
        message.chatId = chat.id.toString()
        fillMessage(message, user, chat, arguments)
        try {
            absSender.execute(message)
        } catch (e: TelegramApiException) {
            processError(user, chat, arguments, e)
        }
    }

    abstract fun fillMessage(message: SendMessage, user: User, chat: Chat,
                             arguments: Array<out String>)

    protected open fun processError(user: User, chat: Chat, arguments: Array<out String>,
                                    e: TelegramApiException) {
        logger.error("Failed to execute command")
    }
}