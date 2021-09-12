package com.merfemor.vkwallwatcher.vk

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.Executor
import java.util.concurrent.Executors

@Configuration
class VkCheckerConfiguration {
    @Bean(name = [EXECUTOR])
    fun vkCheckerExecutor(): Executor {
        return Executors.newSingleThreadExecutor()
    }

    companion object {
        const val EXECUTOR = "vkCheckerExecutor"
    }
}