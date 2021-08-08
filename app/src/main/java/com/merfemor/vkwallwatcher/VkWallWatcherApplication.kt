package com.merfemor.vkwallwatcher

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class VkWallWatcherApplication

fun main(args: Array<String>) {
    runApplication<VkWallWatcherApplication>(*args)
}
