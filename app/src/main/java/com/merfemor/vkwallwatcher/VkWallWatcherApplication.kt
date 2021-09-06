package com.merfemor.vkwallwatcher

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@ConfigurationPropertiesScan
@SpringBootApplication
@EnableScheduling
open class VkWallWatcherApplication

fun main(args: Array<String>) {
    runApplication<VkWallWatcherApplication>(*args)
}
