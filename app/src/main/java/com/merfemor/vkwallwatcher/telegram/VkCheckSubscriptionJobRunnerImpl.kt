package com.merfemor.vkwallwatcher.telegram

import com.merfemor.vkwallwatcher.telegram.command.VkCheckSubscriptionJobRunner
import com.merfemor.vkwallwatcher.vk.JobScheduler
import org.springframework.stereotype.Component

@Component
internal class VkCheckSubscriptionJobRunnerImpl(
        private val jobScheduler: JobScheduler
) : VkCheckSubscriptionJobRunner {
    override fun run() = jobScheduler.checkVkSubscriptions()
}
