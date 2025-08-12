package com.github.spacemex.fabric.client;

import com.github.spacemex.TodoMod;
import net.fabricmc.api.ClientModInitializer;

public final class TodoModFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        TodoMod.init();
    }
}
