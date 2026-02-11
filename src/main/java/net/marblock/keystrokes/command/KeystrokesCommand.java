package net.marblock.keystrokes.command;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.marblock.keystrokes.gui.SettingsScreen;
import net.minecraft.client.Minecraft;

public class KeystrokesCommand {

    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("keystrokes")
                    .executes(context -> {
                        Minecraft.getInstance().execute(() -> {
                            Minecraft.getInstance().setScreen(new SettingsScreen());
                        });
                        return 1;
                    }));
        });
    }
}