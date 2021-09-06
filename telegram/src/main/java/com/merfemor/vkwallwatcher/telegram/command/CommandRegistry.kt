package com.merfemor.vkwallwatcher.telegram.command

import org.springframework.stereotype.Component
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand


@Component
internal class CommandRegistry(
    cancelCommand: CancelCommand,
    newWatchCommand: NewWatchCommand,
    deleteWatchCommand: DeleteWatchCommand
) {
    private val commands = arrayOf<IBotCommand>(
        cancelCommand,
        newWatchCommand,
        deleteWatchCommand
    )

    fun forEachCommand(action: (IBotCommand) -> Unit) = commands.forEach(action)
}