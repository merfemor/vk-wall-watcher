package com.merfemor.vkwallwatcher.vk

import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
internal class ScheduledVkSubscriptionsChecker {
    @Scheduled(cron = "\${vk.checkScheduleCron}")
    private fun checkSubscriptions() {
        logger.info("Check subscriptions...")
        // TODO: implement check subscriptions
    }

    private companion object {
        private val logger = LoggerFactory.getLogger(ScheduledVkSubscriptionsChecker::class.java)
    }
}