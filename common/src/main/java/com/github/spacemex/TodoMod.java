package com.github.spacemex;

import com.github.spacemex.client.TodoClientEvents;
import com.github.spacemex.client.TodoKeys;
import dev.architectury.event.events.client.ClientTickEvent;

public final class TodoMod {
    public static final String MOD_ID = "todo";


    public static void init() {
        TodoKeys.register();
        ClientTickEvent.CLIENT_POST.register(TodoClientEvents::clientTick);
    }

}
