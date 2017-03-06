package ru.den_abr.glownpc.event;

import org.bukkit.event.Cancellable;

public class FCancellable extends FEvent implements Cancellable {
    private boolean cancelled = false;

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

}
