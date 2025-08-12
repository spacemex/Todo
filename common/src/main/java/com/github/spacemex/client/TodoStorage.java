package com.github.spacemex.client;

import com.github.spacemex.Helper;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import dev.architectury.platform.Platform;
import net.minecraft.client.MinecraftClient;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class TodoStorage {
    private static final Gson GSON = new Gson();
    private static final Type LIST_TYPE = new TypeToken<List<String>>(){}.getType();

    private static File getSaveFile(){
        MinecraftClient mc = MinecraftClient.getInstance();
        String identifier;
        if (mc.isInSingleplayer() || mc.isIntegratedServerRunning()) {
            identifier = mc.getServer().getSaveProperties().getLevelName();
            return Platform.getGameFolder().resolve("saves/" + identifier + "/todo.json").toFile();
        } else if (mc.getCurrentServerEntry() != null) {
            identifier = mc.getCurrentServerEntry().address.replace(":", "_");
            File dir = Platform.getGameFolder().resolve("todo/servers/" + identifier).toFile();
            dir.mkdirs();
            return new File(dir, "todo.json");
        }
        return null;
    }

    public static List<String> loadTodos(){
        try {
            File f = getSaveFile();
            if (f != null && f.exists()){
                try(FileReader reader = new FileReader(f)){
                    return GSON.fromJson(reader, LIST_TYPE);
                }
            }
        }catch (Exception e){
            Helper.getLogger().error("Failed to LoadTodos,");
            Helper.getLogger().warn("Creating One!");
        }
        return new ArrayList<>();
    }

    public static void saveTodos(List<String> todos){
        try {
            File f = getSaveFile();
            if (f != null){
                try(FileWriter writer = new FileWriter(f)){
                    GSON.toJson(todos, LIST_TYPE, writer);
                }
            }
        }catch (Exception e){
            Helper.getLogger().error("Failed to SaveTodos, {}", e.getMessage());
        }
    }
}
