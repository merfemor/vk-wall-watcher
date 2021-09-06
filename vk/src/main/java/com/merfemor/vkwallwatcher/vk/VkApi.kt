package com.merfemor.vkwallwatcher.vk

import com.vk.api.sdk.client.VkApiClient
import com.vk.api.sdk.client.actors.ServiceActor
import com.vk.api.sdk.httpclient.HttpTransportClient
import com.vk.api.sdk.objects.groups.responses.GetByIdLegacyResponse
import com.vk.api.sdk.objects.wall.responses.GetResponse
import org.springframework.stereotype.Component

@Component
class VkApi internal constructor(
        properties: VkProperties
) {
    private val client = VkApiClient(HttpTransportClient())
    private val actor = ServiceActor(properties.appId, properties.clientSecret, properties.accessToken)

    private fun getGroupInfoByNameId(nameId: String): GetByIdLegacyResponse {
        return client.groups().getByIdLegacy(actor).groupId(nameId).execute()[0]
    }

    fun getGroupIdByNameId(nameId: String): Int {
        return getGroupInfoByNameId(nameId).id
    }

    fun searchGroupWallPosts(groupId: Int, query: String, count: Int = 100, offset: Int = 0): GetResponse {
        val id = -groupId // group ids with minuses
        return client.wall().get(actor).ownerId(id)
                .count(count)
                .offset(offset)
                .execute()
    }
}