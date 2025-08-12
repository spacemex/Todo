package com.github.spacemex.forge;

import com.github.spacemex.TodoMod;
import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(TodoMod.MOD_ID)
public final class TodoModForge {
    public static final Logger LOGGER = LoggerFactory.getLogger(TodoMod.MOD_ID);
    public TodoModForge() {
        EventBuses.registerModEventBus(TodoMod.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> ()->{
            FMLJavaModLoadingContext.get().getModEventBus().addListener(this::initClient);
        });
    }

    @OnlyIn(Dist.CLIENT)
    private void initClient(FMLClientSetupEvent event){
        TodoMod.init();
    }
}
