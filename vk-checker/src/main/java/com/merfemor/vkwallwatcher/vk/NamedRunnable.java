package com.merfemor.vkwallwatcher.vk;

public abstract class NamedRunnable implements Runnable {
    private final String threadNamePrefix;

    public NamedRunnable(String threadNamePrefix) {
        this.threadNamePrefix = threadNamePrefix;
    }

    @Override
    public final void run() {
        Thread thread = Thread.currentThread();
        String oldName = thread.getName();
        thread.setName(threadNamePrefix + "_" + oldName);
        execute();
        thread.setName(oldName);
    }

    protected abstract void execute();
}
