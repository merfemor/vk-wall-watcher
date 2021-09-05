package com.merfemor.vkwallwatcher.telegram

import org.springframework.stereotype.Component
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand


@Component
internal class CommandRegistry(
    helpCommand: HelpCommand,
    cancelCommand: CancelCommand,
    newWatchCommand: NewWatchCommand
) {
    private val commands = arrayOf<IBotCommand>(
        helpCommand,
        cancelCommand,
        newWatchCommand
    )

    fun forEachCommand(action: (IBotCommand) -> Unit) = commands.forEach(action)
}