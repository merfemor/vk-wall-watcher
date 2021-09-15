package com.merfemor.vkwallwatcher.vk

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class JobScheduler internal constructor(
        private val vkSubscriptionsCheckJob: VkSubscriptionsCheckJob
) {
    @Scheduled(cron = "\${vk.check_schedule_cron}")
    fun checkVkSubscriptions() {
        vkSubscriptionsCheckJob.run()
    }
}