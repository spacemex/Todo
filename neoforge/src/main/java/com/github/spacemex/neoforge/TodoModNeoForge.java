package com.github.spacemex.neoforge;

import com.github.spacemex.TodoMod;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.github.spacemex.client.TodoKeys.OPEN_TODO;

@Mod(TodoMod.MOD_ID)
public final class TodoModNeoForge {
    public static final Logger LOGGER = LoggerFactory.getLogger(TodoMod.MOD_ID);
    public TodoModNeoForge(IEventBus bus) {
        if (FMLEnvironment.getDist() == Dist.CLIENT) {
            bus.addListener(this::initClient);
            bus.addListener(this::onRegisterKeyMappings);
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void initClient(FMLClientSetupEvent event){
        TodoMod.init();
    }

    public void onRegisterKeyMappings(RegisterKeyMappingsEvent event){
        event.register(OPEN_TODO);
    }
}
