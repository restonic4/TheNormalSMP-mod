package me.restonic4.thenormalsmp;

import me.restonic4.thenormalsmp.networking.Messages;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class TheNormalSMP implements ModInitializer {
    public static final String ID = "thenormalsmp";

    @Override
    public void onInitialize() {
        ModGameRules.register();

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerPlayer player = handler.getPlayer();

            FriendlyByteBuf friendlyByteBuf = PacketByteBufs.create();
            friendlyByteBuf.writeBoolean(player.level().getGameRules().getBoolean(ModGameRules.HARDCODE));

            ServerPlayNetworking.send(player, Messages.HARDCORE, friendlyByteBuf);

            if (player.level().getGameRules().getBoolean(ModGameRules.HARDCODE)) {
                BannedPeopleHelper bannedPeopleHelper = BannedPeopleHelper.get();
                for (BannedPeopleHelper.PlayerData playerData : bannedPeopleHelper.getPlayerList()) {
                    if (player.getName().getString().equals(playerData.getName())) {
                        player.connection.disconnect(Component.literal("El modo lore esta activo. Esta canonicamente muerto en esta fase."));
                    }
                }
            }
        });

        ServerLivingEntityEvents.AFTER_DEATH.register((livingEntity, source) -> {
            if (livingEntity instanceof ServerPlayer player) {
                if (player.level().getGameRules().getBoolean(ModGameRules.HARDCODE)) {
                    BannedPeopleHelper.get().addPlayer(player);
                    BannedPeopleHelper.get().saveToJson();
                    player.connection.disconnect(Component.literal("Has muerto..."));
                }
            }
        });
    }
}
