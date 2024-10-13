package me.restonic4.thenormalsmp;

import me.restonic4.thenormalsmp.networking.Messages;
import net.fabricmc.api.ClientModInitializer;

public class Client implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        Messages.registerServerToClientPackets();
    }
}
