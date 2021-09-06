package com.merfemor.vkwallwatcher.vk

import com.vk.api.sdk.client.VkApiClient
import com.vk.api.sdk.client.actors.ServiceActor
import com.vk.api.sdk.httpclient.HttpTransportClient
import com.vk.api.sdk.objects.groups.responses.GetByIdLegacyResponse
import com.vk.api.sdk.objects.wall.WallpostFull
import org.springframework.stereotype.Component
import java.util.Date

@Component
class VkApi internal constructor(
        properties: VkProperties
) {
    private val client = VkApiClient(HttpTransportClient())
    private val actor = ServiceActor(properties.appId, properties.clientSecret, properties.accessToken)

    private fun getGroupInfoByNameId(nameId: String): GetByIdLegacyResponse {
        return client.groups().getByIdLegacy(actor).groupId(nameId).execute()[0]
    }

    fun getGroupInfosById(groupIds: Collection<Int>): List<GetByIdLegacyResponse> {
        val ids = groupIds.map { it.toString() }
        return client.groups().getByIdLegacy(actor).groupIds(ids).execute()
    }

    fun getGroupInfoById(groupId: Int): GetByIdLegacyResponse {
        return getGroupInfosById(listOf(groupId))[0]
    }

    fun getGroupIdByNameId(nameId: String): Int {
        return getGroupInfoByNameId(nameId).id
    }

    fun searchGroupAllWallPosts(groupId: Int, query: String,
                                minDate: Date? = null, maxDate: Date? = null): Collection<WallpostFull> {
        var offset = 0
        val result = mutableListOf<WallpostFull>()
        while (true) {
            val items = searchGroupWallPostsWithCount(groupId, query, minDate, MAX_COUNT, offset)
            result.addAll(items)
            if (items.size < MAX_COUNT) {
                break
            }
            offset += MAX_COUNT
        }
        if (maxDate == null) {
            return result
        }
        return result.filter { Date(it.date.toLong()).before(maxDate) }
    }

    private fun searchGroupWallPostsWithCount(groupId: Int, query: String, minDate: Date? = null,
                                              count: Int, offset: Int): Collection<WallpostFull> {
        assert(count in 1..100) { "Count must be between 0 and 100, actual $count" }
        assert(offset >= 0) { "Offset must be non-negative, actual $offset" }

        val id = -groupId // group ids with minuses
        val result = client.wall().search(actor).ownerId(id)
                .count(count)
                .query(query)
                .offset(offset)
                .execute()
                .items
        if (minDate == null) {
            return result
        }
        return result.filter { Date(it.date.toLong()).after(minDate) }
    }

    fun getLinkForPost(communityNameId: String, communityId: Int, postId: Int): String {
        return "vk.com/$communityNameId?w=wall-${communityId}_$postId"
    }

    private companion object {
        private const val MAX_COUNT = 100
    }
}