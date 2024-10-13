package me.restonic4.thenormalsmp.networking.Packets.ServerToClient;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;

public class Hardcore {
    public static boolean isHardcore = true;

    public static void receive(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buf, PacketSender responseSender)
    {
        isHardcore = buf.readBoolean();
    }
}
