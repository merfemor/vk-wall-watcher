package com.merfemor.vkwallwatcher.telegram

import org.telegram.telegrambots.meta.api.objects.User

internal interface MessagesFilter {

    val responseMessageForReject: String?

    /**
     * @return true if message should be accepted, false if skipped
     */
    fun test(user: User): Boolean
}