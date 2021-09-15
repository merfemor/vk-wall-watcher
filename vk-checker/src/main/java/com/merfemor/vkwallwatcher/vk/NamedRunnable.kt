package com.merfemor.vkwallwatcher.vk

fun Runnable.named(threadNamePrefix: String): Runnable {
    val runnable = this
    return object : NamedRunnable(threadNamePrefix) {
        override fun execute() {
            runnable.run()
        }
    }
}
