package com.merfemor.vkwallwatcher.telegram.command

import com.merfemor.vkwallwatcher.telegram.MessageFormatter
import com.merfemor.vkwallwatcher.telegram.SendHelper
import com.merfemor.vkwallwatcher.telegram.filter.AdminUsersMessagesFilterImpl
import org.springframework.stereotype.Component
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender

private const val FORCE_CHECK_ACTION = "forcecheck"
private val HELP_MSG = """
    Command format: /admin <action>
    Available actions:
    - $FORCE_CHECK_ACTION - force subscription check job 
""".trimIndent()

@Component
internal class AdminCommand(
        private val vkCheckSubscriptionJobRunner: VkCheckSubscriptionJobRunner,
        private val messageFilter: AdminUsersMessagesFilterImpl,
        private val sendHelper: SendHelper,
        private val messageFormatter: MessageFormatter
) : BotCommand("admin", "Admin functions") {
    override fun execute(absSender: AbsSender, user: User, chat: Chat, arguments: Array<String>) {
        if (!messageFilter.test(user)) {
            val msg = messageFilter.responseMessageForReject
            val formattedMsg = messageFormatter.formatError(msg)
            sendHelper.sendTextMessage(chat.id, absSender, formattedMsg)
            return
        }
        if (arguments.isEmpty()) {
            printHelp(chat.id, absSender)
            return
        }
        if (arguments.size != 1 || arguments[0] != FORCE_CHECK_ACTION) {
            printHelp(chat.id, absSender)
            return
        }
        forceCheck(chat.id, absSender)
    }

    private fun forceCheck(chatId: Long, absSender: AbsSender) {
        vkCheckSubscriptionJobRunner.run()
        val msg = messageFormatter.formatSuccess("Force run check")
        sendHelper.sendTextMessage(chatId, absSender, msg)
    }

    private fun printHelp(chatId: Long, absSender: AbsSender) {
        sendHelper.sendTextMessage(chatId, absSender, HELP_MSG)
    }
}