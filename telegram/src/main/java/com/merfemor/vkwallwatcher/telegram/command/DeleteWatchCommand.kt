package com.merfemor.vkwallwatcher.telegram.command

import com.merfemor.vkwallwatcher.data.VkWallWatchSubscriptionRepository
import com.merfemor.vkwallwatcher.telegram.MessageFormatter
import com.merfemor.vkwallwatcher.telegram.SendHelper
import org.springframework.stereotype.Component
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand
import org.telegram.telegrambots.meta.api.methods.ParseMode
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.CallbackQuery
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import org.telegram.telegrambots.meta.bots.AbsSender
import kotlin.math.abs


@Component
internal class DeleteWatchCommand(
        private val sendHelper: SendHelper,
        private val subscriptionRepository: VkWallWatchSubscriptionRepository,
        private val subscriptionCommandsHelper: SubscriptionCommandsHelper,
        private val callbackQueryProcessorHolder: CallbackQueryProcessorHolder,
        private val messageFormatter: MessageFormatter
) : BotCommand("deletewatch", "Delete vk wall watch") {

    private fun buildKeyboard(subs: Collection<SubscriptionCommandsHelper.SubscriptionShortInfo>): ReplyKeyboard {
        return InlineKeyboardMarkup.builder().apply {
            for ((index, sub) in subs.withIndex()) {
                val button = InlineKeyboardButton.builder()
                        .text("${index + 1}. ${sub.communityName}")
                        .callbackData(index.toString())
                        .build()
                keyboardRow(listOf(button))
            }
            val cancelButton = InlineKeyboardButton.builder()
                    .text("Cancel")
                    .callbackData("-1")
                    .build()
            keyboardRow(listOf(cancelButton))
        }.build()
    }

    override fun execute(absSender: AbsSender, user: User, chat: Chat, arguments: Array<String>) {
        val subs = subscriptionCommandsHelper.getSubscriptionsShortInfoList(chat.id)
        if (subs.isEmpty()) {
            sendHelper.sendTextMessage(chat.id, absSender, "No active subscriptions")
            return
        }

        val keyboard = buildKeyboard(subs)

        val processor = WatchSelectKeyboardCallbackProcessor(subs)
        if (callbackQueryProcessorHolder.hasProcessorForChat(chat.id)) {
            callbackQueryProcessorHolder.unregisterProcessorForChat(chat.id)
        }
        callbackQueryProcessorHolder.registerProcessorForChat(chat.id, processor)

        val textBuilder = StringBuilder("Choose one of the subscriptions or cancel:\n\n")
        textBuilder.append(subscriptionCommandsHelper.createSubscriptionsListText(subs))

        val msg = SendMessage.builder()
                .chatId(chat.id.toString())
                .text(textBuilder.toString())
                .parseMode(ParseMode.HTML)
                .replyMarkup(keyboard)
                .build()

        sendHelper.sendMessageCatchingErrors(absSender, msg)
    }

    private inner class WatchSelectKeyboardCallbackProcessor(
            private val subscriptions: List<SubscriptionCommandsHelper.SubscriptionShortInfo>
    ) : CallbackQueryProcessor {

        override fun process(callbackQuery: CallbackQuery, absSender: AbsSender) {
            val chatId = callbackQuery.message.chatId
            callbackQueryProcessorHolder.unregisterProcessorForChat(chatId)

            val index = callbackQuery.data.toInt()
            if (index >= 0) {
                val sub = subscriptions[index]
                try {
                    subscriptionRepository.deleteById(sub.id)
                    val msgText = messageFormatter.formatSuccess(
                            "Deleted subscription for \"${sub.communityName}\" with query=\"${sub.query}\"")
                    sendHelper.sendTextMessage(chatId, absSender, msgText)
                } catch (th: Throwable) {
                    val msgText = messageFormatter.formatError("Failed to delete (internal error)")
                    sendHelper.sendTextMessage(chatId, absSender, msgText)
                    throw th
                }
            } else {
                sendHelper.sendTextMessage(chatId, absSender, "Canceled")
            }
        }
    }
}