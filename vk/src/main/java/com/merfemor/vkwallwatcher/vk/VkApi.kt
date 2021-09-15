package com.merfemor.vkwallwatcher.vk

import com.vk.api.sdk.client.VkApiClient
import com.vk.api.sdk.client.actors.ServiceActor
import com.vk.api.sdk.httpclient.HttpTransportClient
import com.vk.api.sdk.objects.groups.responses.GetByIdObjectLegacyResponse
import com.vk.api.sdk.objects.wall.WallpostFull
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.Date
import java.util.concurrent.TimeUnit

@Component
class VkApi internal constructor(
        properties: VkProperties
) {
    private val client = VkApiClient(HttpTransportClient())
    private val actor = ServiceActor(properties.appId, properties.clientSecret, properties.accessToken)

    private fun getGroupInfoByNameId(nameId: String): GetByIdObjectLegacyResponse {
        return client.groups().getByIdObjectLegacy(actor).groupId(nameId).execute()[0]
    }

    fun getGroupInfosById(groupIds: Collection<Int>): List<GetByIdObjectLegacyResponse> {
        logger.debug("get group infos by id: groupIds=$groupIds")
        val ids = groupIds.map { it.toString() }
        return client.groups().getByIdObjectLegacy(actor).groupIds(ids).execute()
    }

    fun getGroupInfoById(groupId: Int): GetByIdObjectLegacyResponse {
        return getGroupInfosById(listOf(groupId))[0]
    }

    fun getGroupIdByNameId(nameId: String): Int {
        return getGroupInfoByNameId(nameId).id
    }

    fun searchGroupAllWallPosts(groupId: Int, query: String,
                                minDate: Date? = null, maxDate: Date? = null): Collection<WallpostFull> {
        logger.debug("search group wall posts: groupId=$groupId, minDate=$minDate, maxDate=$maxDate")

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
        return result.filter { unixSecondsToDate(it.date).before(maxDate) }
    }

    private fun searchGroupWallPostsWithCount(groupId: Int, query: String, minDate: Date? = null,
                                              count: Int, offset: Int): Collection<WallpostFull> {
        assert(count in 1..100) { "Count must be between 0 and 100, actual $count" }
        assert(offset >= 0) { "Offset must be non-negative, actual $offset" }

        logger.debug("search group wall posts: groupId=$groupId, query=$query, minDate=$minDate" +
                ", count=$count, offset=$offset")

        val id = -groupId // group ids with minuses
        val result = client.wall().search(actor).ownerId(id)
                .count(count)
                .query(query)
                .offset(offset)
                .execute()
                .items

        logger.debug("search group wall posts: result size ${result.size}")
        if (minDate == null) {
            return result
        }
        val filteredResults = result.filter { unixSecondsToDate(it.date).after(minDate) }
        logger.debug("search group wall posts: result size after filtering ${filteredResults.size}")
        return filteredResults
    }

    fun getLinkForPost(communityNameId: String, communityId: Int, postId: Int): String {
        return "vk.com/$communityNameId?w=wall-${communityId}_$postId"
    }

    private companion object {
        private const val MAX_COUNT = 100
        private val logger = LoggerFactory.getLogger(VkApi::class.java)

        private fun unixSecondsToDate(unixSeconds: Int): Date {
            val millis = TimeUnit.SECONDS.toMillis(unixSeconds.toLong())
            return Date(millis)
        }
    }
}