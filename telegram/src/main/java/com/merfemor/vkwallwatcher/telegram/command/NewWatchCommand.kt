package com.merfemor.vkwallwatcher.telegram.command

import com.merfemor.vkwallwatcher.telegram.NonCommandMessagesProcessor
import com.merfemor.vkwallwatcher.telegram.NonCommandMessagesProcessorHolder
import com.merfemor.vkwallwatcher.telegram.SendHelper
import org.springframework.stereotype.Component
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender


@Component
internal class NewWatchCommand(
    private val sendHelper: SendHelper,
    private val nonCommandUpdateProcessorHolder: NonCommandMessagesProcessorHolder,
    private val cancelCommand: CancelCommand
) : BotCommand("newwatch", "Creates new VK wall watcher") {

    override fun execute(absSender: AbsSender, user: User, chat: Chat, arguments: Array<out String>) {
        setupCancelCommand(chat.id, absSender)
        val inputCommunityId = InputCommunityIdProcessor(chat.id, absSender) { communityId ->
            val inputKeywordsProcessor = InputKeywordsProcessor(chat.id, absSender) { keywords ->
                onSuccessEnter(chat.id, absSender, communityId, keywords)
            }
            nonCommandUpdateProcessorHolder.setProcessor(chat.id, inputKeywordsProcessor)
        }
        nonCommandUpdateProcessorHolder.setProcessor(chat.id, inputCommunityId)
    }

    private fun onSuccessEnter(chatId: Long, absSender: AbsSender, communityId: String, keywords: String) {
        sendHelper.sendTextMessageResponse(chatId, absSender, "You entered \"$communityId\" \"$keywords\"")
        // TODO: implement store into DB
    }

    private fun removeCancelCommandProcessor(chatId: Long) {
        cancelCommand.setProcessorForChat(chatId, null)
    }

    private fun setupCancelCommand(chatId: Long, absSender: AbsSender) {
        cancelCommand.setProcessorForChat(chatId, Runnable {
            removeCancelCommandProcessor(chatId)
            nonCommandUpdateProcessorHolder.setProcessor(chatId, null)
            sendHelper.sendTextMessageResponse(chatId, absSender, "Canceled")
        })
    }

    private inner class InputCommunityIdProcessor(chatId: Long, sender: AbsSender,
                                                  onSuccess: (communityId: String) -> Unit)
        : BaseCancellableInputStateMessagesProcessor(sendHelper, "Input community id", chatId,
        sender, onSuccess
    ) {
        override fun isInputValid(input: String): Boolean = input.isNotEmpty()
    }

    private inner class InputKeywordsProcessor(chatId: Long, sender: AbsSender,
                                               onSuccess: (keywords: String) -> Unit)
        : BaseCancellableInputStateMessagesProcessor(sendHelper, "Input keywords for filter", chatId,
        sender, onSuccess) {
        override fun isInputValid(input: String): Boolean = input.isNotEmpty()
    }

    private abstract class BaseCancellableInputStateMessagesProcessor(
        private val sendHelper: SendHelper,
        private val askForInputText: String,
        private val chatId: Long,
        private val sender: AbsSender,
        private val onSuccess: (String) -> Unit
    ) : NonCommandMessagesProcessor {

        init {
            askForInput()
        }

        private fun askForInput() {
            sendHelper.sendTextMessageResponse(chatId, sender, askForInputText +
                    "\n" + INFORM_OPTION_TO_CANCEL_TEXT
            )
        }

        private fun sayIncorrectInput() {
            sendHelper.sendTextMessageResponse(chatId, sender, WRONG_INPUT_REPEAT_TEXT)
        }

        override fun process(update: Update, sender: AbsSender) {
            val input = update.message.text
            if (isInputValid(input)) {
                onSuccess.invoke(input)
                return
            }
            sayIncorrectInput()
            askForInput()
        }

        abstract fun isInputValid(input: String) : Boolean

        private companion object {
            private const val INFORM_OPTION_TO_CANCEL_TEXT = "You can cancel command with /cancel"
            private const val WRONG_INPUT_REPEAT_TEXT = "Incorrect input"
        }
    }
}