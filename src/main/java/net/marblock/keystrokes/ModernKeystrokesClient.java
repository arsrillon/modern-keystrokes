package net.marblock.keystrokes;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.marblock.keystrokes.command.KeystrokesCommand;
import net.marblock.keystrokes.config.KeystrokesConfig;

public class ModernKeystrokesClient implements ClientModInitializer {
    public static KeystrokesRenderer renderer;

    @Override
    public void onInitializeClient() {
        KeystrokesConfig.load();

        renderer = new KeystrokesRenderer();

        HudRenderCallback.EVENT.register((guiGraphics, tickDelta) -> {
            float partialTick = tickDelta.getGameTimeDeltaPartialTick(true);
            renderer.render(guiGraphics, partialTick);
        });

        KeystrokesCommand.register();
    }
}
