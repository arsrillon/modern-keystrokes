package net.marblock.keystrokes.command;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.marblock.keystrokes.gui.SettingsScreen;

public class KeystrokesCommand {
    private static boolean openScreen = false;

    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("keystrokes")
                    .executes(context -> {
                        openScreen = true;
                        return 1;
                    }));
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (openScreen) {
                client.setScreen(new SettingsScreen());
                openScreen = false;
            }
        });
    }
}