package me.restonic4.thenormalsmp;

import me.restonic4.thenormalsmp.networking.Messages;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameRules;

public class ModGameRules {
    public static final GameRules.Key<GameRules.BooleanValue> HARDCODE = GameRuleRegistry.register("modo_lore", GameRules.Category.PLAYER, GameRuleFactory.createBooleanRule(true));
    private static boolean lastHardcoreValue = false;

    public static void register() {
        System.out.println("GameRules!");

        registerTickHandler();
    }

    public static void registerTickHandler() {
        ServerTickEvents.START_WORLD_TICK.register(world -> {
            if (world instanceof ServerLevel) {
                checkGameRuleChange((ServerLevel) world);
            }
        });
    }

    private static void checkGameRuleChange(ServerLevel level) {
        boolean currentHardcoreValue = level.getGameRules().getBoolean(ModGameRules.HARDCODE);

        if (currentHardcoreValue != lastHardcoreValue) {
            System.out.println("GameRule 'modo_lore' changed: " + currentHardcoreValue);

            FriendlyByteBuf friendlyByteBuf = PacketByteBufs.create();
            friendlyByteBuf.writeBoolean(currentHardcoreValue);

            for (ServerPlayer player : level.players()) {
                ServerPlayNetworking.send(player, Messages.HARDCORE, friendlyByteBuf);

                if (player.level().getGameRules().getBoolean(ModGameRules.HARDCODE)) {
                    BannedPeopleHelper bannedPeopleHelper = BannedPeopleHelper.get();
                    for (BannedPeopleHelper.PlayerData playerData : bannedPeopleHelper.getPlayerList()) {
                        if (player.getName().getString().equals(playerData.getName())) {
                            player.connection.disconnect(Component.literal("El modo lore esta activo. Esta canonicamente muerto en esta fase."));
                        }
                    }
                }
            }

            lastHardcoreValue = currentHardcoreValue;
        }
    }
}
