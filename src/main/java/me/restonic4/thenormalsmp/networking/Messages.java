package me.restonic4.thenormalsmp.networking;

import me.restonic4.thenormalsmp.TheNormalSMP;
import me.restonic4.thenormalsmp.networking.Packets.ServerToClient.Hardcore;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.resources.ResourceLocation;

public class Messages  {
    public static final ResourceLocation HARDCORE = new ResourceLocation(TheNormalSMP.ID, "hardcore");

    public static void registerServerToClientPackets() {
        ClientPlayNetworking.registerGlobalReceiver(HARDCORE, Hardcore::receive);
    }
}
