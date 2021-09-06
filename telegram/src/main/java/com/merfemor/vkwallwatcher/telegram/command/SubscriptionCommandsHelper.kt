package com.merfemor.vkwallwatcher.telegram.command

import com.merfemor.vkwallwatcher.data.VkWallWatchSubscriptionRepository
import com.merfemor.vkwallwatcher.vk.VkApi
import org.springframework.stereotype.Component

private val SUBSCRIPTION_ITEM_FMT = """
    %d. %s
    Query: <i>%s</i>
""".trimIndent()

@Component
internal class SubscriptionCommandsHelper(
        private val subscriptionRepository: VkWallWatchSubscriptionRepository,
        private val vkApi: VkApi
) {

    class SubscriptionShortInfo(val id: String, val communityName: String, val query: String)

    fun getSubscriptionsShortInfoList(chatId: Long): List<SubscriptionShortInfo> {
        val subscriptions = subscriptionRepository.findByChatId(chatId)
        if (subscriptions.isEmpty()) {
            return emptyList()
        }
        val groupIds = subscriptions.map { it.communityId }
        val groupNames = vkApi.getGroupInfosById(groupIds).associate { it.id to it.name }
        return subscriptions.map {
            SubscriptionShortInfo(it.id, groupNames[it.communityId]!!, it.query)
        }
    }

    fun createSubscriptionsListText(subs: Collection<SubscriptionShortInfo>): String {
        if (subs.isEmpty()) {
            return "No active subscriptions.\nCreate one with /newwatch command"
        }
        return subs.withIndex().joinToString(separator = "\n\n") { (index, sub) ->
            SUBSCRIPTION_ITEM_FMT.format(index + 1, sub.communityName, sub.query)
        }
    }
}