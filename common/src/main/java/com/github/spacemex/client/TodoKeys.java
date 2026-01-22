package com.github.spacemex.client;

import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

import static com.github.spacemex.TodoMod.MOD_ID;

@Environment(EnvType.CLIENT)
public class TodoKeys {
    public static final KeyBinding OPEN_TODO = new KeyBinding(
            "key.todo.open",
            InputUtil.Type.KEYSYM,
            InputUtil.GLFW_KEY_G,
            KeyBinding.Category.MISC
    );

    public static void register(){
        KeyMappingRegistry.register(OPEN_TODO);
    }
}
