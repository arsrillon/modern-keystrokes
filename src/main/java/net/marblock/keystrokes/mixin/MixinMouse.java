package net.marblock.keystrokes.mixin;

import net.marblock.keystrokes.ModernKeystrokesClient;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.input.MouseButtonInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public class MixinMouse {
    @Inject(method = "onButton", at = @At("HEAD"))
    private void onMouseClick(long window, MouseButtonInfo mouseButtonInfo, int action, CallbackInfo ci) {
        if (action == 1) {
            if (ModernKeystrokesClient.renderer != null) {
                int button = mouseButtonInfo.button();

                if (button == 0) {
                    net.marblock.keystrokes.ModernKeystrokesClient.renderer.incrementLeftClicks();
                } else if (button == 1) {
                    net.marblock.keystrokes.ModernKeystrokesClient.renderer.incrementRightClicks();
                }
            }
        }
    }
}