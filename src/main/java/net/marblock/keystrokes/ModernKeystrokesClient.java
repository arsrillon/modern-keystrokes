package net.marblock.keystrokes;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.marblock.keystrokes.command.KeystrokesCommand;
import net.marblock.keystrokes.config.KeystrokesConfig;
import net.minecraft.resources.Identifier;

public class ModernKeystrokesClient implements ClientModInitializer {
    public static KeystrokesRenderer renderer;

    @Override
    public void onInitializeClient() {
        KeystrokesConfig.load();

        renderer = new KeystrokesRenderer();

        HudElementRegistry.addLast(Identifier.fromNamespaceAndPath("modern-keystrokes", "overlay"), (guiGraphics, deltaTracker) -> {
            float partialTick = deltaTracker.getGameTimeDeltaPartialTick(true);
            renderer.render(guiGraphics, partialTick);
        });

        KeystrokesCommand.register();
    }
}