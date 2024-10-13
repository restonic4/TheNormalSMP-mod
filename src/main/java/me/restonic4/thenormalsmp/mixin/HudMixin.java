package me.restonic4.thenormalsmp.mixin;

import me.restonic4.thenormalsmp.ModGameRules;
import me.restonic4.thenormalsmp.TheNormalSMP;
import me.restonic4.thenormalsmp.networking.Packets.ServerToClient.Hardcore;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(Gui.class)
public abstract class HudMixin {
    @Inject(method = "renderHeart", at = @At("HEAD"), cancellable = true)
    private void onPlace(GuiGraphics guiGraphics, Gui.HeartType heartType, int x, int y, int k, boolean blinking, boolean halfHeart, CallbackInfo ci) {
        if (Minecraft.getInstance().cameraEntity instanceof Player player) {
            if (!blinking) {
                if (Hardcore.isHardcore) {
                    guiGraphics.blit(Gui.GUI_ICONS_LOCATION, x, y, heartType.getX(halfHeart, blinking), k + 45, 9, 9);

                    ci.cancel();
                }
            }
        }
    }
}
