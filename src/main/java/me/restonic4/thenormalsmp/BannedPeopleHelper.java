package me.restonic4.thenormalsmp;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.LevelResource;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class BannedPeopleHelper {
    private static BannedPeopleHelper instance;

    private List<PlayerData> playerList;
    private final File jsonFile;
    private final Gson gson;

    public BannedPeopleHelper(File file) {
        this.jsonFile = file;
        this.playerList = new ArrayList<>();
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public static BannedPeopleHelper get() {
        if (instance == null) {
            File jsonFile = new File(BannedPeopleHelper.getPath() + "/skill_issue_people.json");
            instance = new BannedPeopleHelper(jsonFile);
            instance.loadFromJson();
        }

        return instance;
    }

    private static MinecraftServer getServer() {
        return FabricLoader.getInstance().getGameInstance() instanceof MinecraftServer ?
                (MinecraftServer) FabricLoader.getInstance().getGameInstance() : null;
    }

    public static Path getPath() {
        Path path;

        if (FabricLoader.getInstance().getGameInstance() instanceof MinecraftServer) {
            path = FabricLoader.getInstance().getGameDir().resolve("world/data");
        }
        else {
            path = Minecraft.getInstance().getSingleplayerServer().getWorldPath(LevelResource.ROOT).resolve("data");
        }

        return path;
    }

    public void addPlayer(Player player) {
        playerList.add(new PlayerData(player.getName().getString()));
    }

    public void saveToJson() {
        try (Writer writer = new FileWriter(jsonFile)) {
            gson.toJson(playerList, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadFromJson() {
        try (Reader reader = new FileReader(jsonFile)) {
            Type type = new TypeToken<List<PlayerData>>() {}.getType();
            playerList = gson.fromJson(reader, type);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<PlayerData> getPlayerList() {
        return playerList;
    }

    public static class PlayerData {
        private String name;

        public PlayerData(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
