package com.merfemor.vkwallwatcher.vk

fun Runnable.named(threadNamePrefix: String): Runnable = object : NamedRunnable(threadNamePrefix) {
    override fun execute() {
        this.run()
    }
}