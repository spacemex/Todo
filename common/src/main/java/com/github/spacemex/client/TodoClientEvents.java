package com.github.spacemex.client;

import com.github.spacemex.screen.TodoScreen;
import net.minecraft.client.MinecraftClient;

public class TodoClientEvents {
    public static void clientTick(MinecraftClient client){
        while (TodoKeys.OPEN_TODO.wasPressed()){
            client.setScreen(new TodoScreen());
        }
    }
}
