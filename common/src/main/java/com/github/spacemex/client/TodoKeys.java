package com.github.spacemex.client;

import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.option.KeyBinding;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class TodoKeys {
    public static final KeyBinding OPEN_TODO = new KeyBinding(
            "key.todo.open",
            GLFW.GLFW_KEY_G,
            "key.categories.todo"
    );

    public static void register(){
        KeyMappingRegistry.register(OPEN_TODO);
    }
}
