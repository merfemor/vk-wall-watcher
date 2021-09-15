package com.merfemor.vkwallwatcher.vk

fun Runnable.named(threadNamePrefix: String): Runnable {
    val runnable = this
    return object : NamedRunnable(threadNamePrefix) {
        override fun execute() {
            runnable.run()
        }
    }
}

abstract class NamedRunnable(private val threadNamePrefix: String) : Runnable {
    override fun run() {
        val thread = Thread.currentThread()
        val oldName = thread.name
        thread.name = threadNamePrefix + "_" + oldName
        execute()
        thread.name = oldName
    }

    protected abstract fun execute()
}
