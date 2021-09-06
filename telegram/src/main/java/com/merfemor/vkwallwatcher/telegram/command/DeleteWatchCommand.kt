package com.merfemor.vkwallwatcher.telegram.command

import com.merfemor.vkwallwatcher.data.VkWallWatchSubscriptionRepository
import com.merfemor.vkwallwatcher.telegram.MessageFormatter
import com.merfemor.vkwallwatcher.telegram.SendHelper
import com.merfemor.vkwallwatcher.vk.VkApi
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


private val SUBSCRIPTION_ITEM_FMT = """
    %d. %s
    Query: <i>%s</i>
""".trimIndent()


@Component
internal class DeleteWatchCommand(
        private val sendHelper: SendHelper,
        private val subscriptionRepository: VkWallWatchSubscriptionRepository,
        private val vkApi: VkApi,
        private val callbackQueryProcessorHolder: CallbackQueryProcessorHolder,
        private val messageFormatter: MessageFormatter
) : BotCommand("deletewatch", "Delete vk wall watch") {

    private class SubscriptionInfo(val id: String, val communityName: String, val query: String)

    private fun getSubscriptionsList(chatId: Long): List<SubscriptionInfo> {
        val subscriptions = subscriptionRepository.findByChatId(chatId)
        val groupIds = subscriptions.map { it.communityId }
        val groupNames = vkApi.getGroupInfosById(groupIds).associate { it.id to it.name }
        return subscriptions.map {
            SubscriptionInfo(it.id, groupNames[it.communityId]!!, it.query)
        }
    }

    private fun buildKeyboard(subs: Collection<SubscriptionInfo>): ReplyKeyboard {
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
        val subs = getSubscriptionsList(chat.id)
        val keyboard = buildKeyboard(subs)

        val processor = WatchSelectKeyboardCallbackProcessor(subs)
        if (callbackQueryProcessorHolder.hasProcessorForChat(chat.id)) {
            callbackQueryProcessorHolder.unregisterProcessorForChat(chat.id)
        }
        callbackQueryProcessorHolder.registerProcessorForChat(chat.id, processor)

        val textBuilder = StringBuilder("Choose one of the subscriptions or cancel:\n")
        for ((index, sub) in subs.withIndex()) {
            textBuilder.append("\n\n")
                    .append(SUBSCRIPTION_ITEM_FMT.format(index + 1, sub.communityName, sub.query))
        }

        val msg = SendMessage.builder()
                .chatId(chat.id.toString())
                .text(textBuilder.toString())
                .parseMode(ParseMode.HTML)
                .replyMarkup(keyboard)
                .build()

        sendHelper.sendMessageCatchingErrors(absSender, msg)
    }

    private inner class WatchSelectKeyboardCallbackProcessor(
            private val subscriptions: List<SubscriptionInfo>
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