package com.merfemor.vkwallwatcher.telegram.command

import com.merfemor.vkwallwatcher.data.VkWallWatchSubscription
import com.merfemor.vkwallwatcher.data.VkWallWatchSubscriptionRepository
import com.merfemor.vkwallwatcher.telegram.NonCommandMessagesProcessor
import com.merfemor.vkwallwatcher.telegram.NonCommandMessagesProcessorHolder
import com.merfemor.vkwallwatcher.telegram.SendHelper
import com.merfemor.vkwallwatcher.vk.VkApi
import org.slf4j.LoggerFactory
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
    private val cancelCommand: CancelCommand,
    private val vkWallWatchSubscriptionRepository: VkWallWatchSubscriptionRepository,
    private val vkApi: VkApi
) : BotCommand("newwatch", "Creates new VK wall watcher") {

    override fun execute(absSender: AbsSender, user: User, chat: Chat, arguments: Array<out String>) {
        setupCancelCommand(chat.id, absSender)
        val inputCommunityId = InputCommunityIdProcessor(chat.id, absSender) { communityId ->
            val inputQueryProcessor = InputQueryProcessor(chat.id, absSender) { query ->
                onSuccessEnter(chat.id, absSender, communityId, query)
            }
            nonCommandUpdateProcessorHolder.setProcessor(chat.id, inputQueryProcessor)
        }
        nonCommandUpdateProcessorHolder.setProcessor(chat.id, inputCommunityId)
    }

    private fun getCommunityIdByNameOptional(nameId: String): Int? {
        return try {
            vkApi.getGroupIdByNameId(nameId)
        } catch (th: Throwable) {
            logger.error("Failed to get community id by name=$nameId", th)
            null
        }
    }

    private fun onSuccessEnter(chatId: Long, absSender: AbsSender, communityId: Int, query: String) {
        nonCommandUpdateProcessorHolder.setProcessor(chatId, null)
        val subscription = VkWallWatchSubscription(chatId, communityId, query)
        try {
            vkWallWatchSubscriptionRepository.save(subscription)
            sendHelper.sendTextMessageResponse(chatId, absSender, "Successfully subscribed")
        } catch (e: Throwable) {
            sendHelper.sendTextMessageResponse(chatId, absSender, "Failed to create subscription")
            throw e
        }
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
                                                  onSuccess: (communityId: Int) -> Unit)
        : BaseCancellableInputStateMessagesProcessor<Int>(sendHelper, "Input community id", chatId,
        sender, onSuccess, "Community with such id is not exists or internal error"
    ) {
        override fun transformInput(input: String): Int? {
            return getCommunityIdByNameOptional(input)
        }
    }

    private inner class InputQueryProcessor(chatId: Long, sender: AbsSender,
                                            onSuccess: (keywords: String) -> Unit)
        : BaseCancellableInputStateMessagesProcessor<String>(sendHelper, "Input query for filter", chatId,
        sender, onSuccess) {

        override fun transformInput(input: String): String? {
            if (input.isEmpty()) {
                return null
            }
            return input
        }
    }

    private abstract class BaseCancellableInputStateMessagesProcessor<T>(
        private val sendHelper: SendHelper,
        private val askForInputText: String,
        private val chatId: Long,
        private val sender: AbsSender,
        private val onSuccess: (T) -> Unit,
        private val inputNotValidText: String = WRONG_INPUT_REPEAT_TEXT
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
            sendHelper.sendTextMessageResponse(chatId, sender, inputNotValidText)
        }

        override fun process(update: Update, sender: AbsSender) {
            val input = update.message.text
            val transformedInput = transformInput(input)
            if (transformedInput != null) {
                onSuccess.invoke(transformedInput)
                return
            }
            sayIncorrectInput()
            askForInput()
        }

        abstract fun transformInput(input: String): T?

        private companion object {
            private const val INFORM_OPTION_TO_CANCEL_TEXT = "You can cancel command with /cancel"
            private const val WRONG_INPUT_REPEAT_TEXT = "Incorrect input"
        }
    }

    private companion object {
        private val logger = LoggerFactory.getLogger(NewWatchCommand::class.java)
    }
}