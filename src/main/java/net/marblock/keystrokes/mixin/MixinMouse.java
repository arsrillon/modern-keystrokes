package net.marblock.keystrokes.mixin;

import net.marblock.keystrokes.ModernKeystrokesClient;
import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public class MixinMouse {
    @Inject(method = "onPress", at = @At("HEAD"))
    private void onMouseClick(long window, int button, int action, int modifiers, CallbackInfo ci) {
        if (action == 1) {
            if (ModernKeystrokesClient.renderer != null) {
                if (button == 0) {
                    ModernKeystrokesClient.renderer.incrementLeftClicks();
                } else if (button == 1) {
                    ModernKeystrokesClient.renderer.incrementRightClicks();
                }
            }
        }
    }
}