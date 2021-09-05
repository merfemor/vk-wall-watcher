package com.merfemor.vkwallwatcher.telegram

import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.bots.AbsSender

internal fun IBotCommand.withFilter(filter: MessagesFilter): IBotCommand =
    CommandWithFilterDecorator(filter, this)

internal class CommandWithFilterDecorator(
    private val filter: MessagesFilter,
    private val command: IBotCommand
) : IBotCommand {

    private val sendHelper = SendHelper()

    override fun getCommandIdentifier(): String = command.commandIdentifier

    override fun getDescription(): String = command.description

    override fun processMessage(absSender: AbsSender, message: Message, arguments: Array<out String>) {
        if (filter.test(message.from)) {
            command.processMessage(absSender, message, arguments)
        } else {
            filter.responseMessageForReject?.let {
                sendHelper.sendTextMessageResponse(message.chatId, absSender, it)
            }
        }
    }
}