package net.marblock.keystrokes.command;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.marblock.keystrokes.gui.SettingsScreen;

public class KeystrokesCommand {
    private static boolean openSettingsNextTick = false;

    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("keystrokes")
                    .executes(context -> {
                        openSettingsNextTick = true;
                        return 1;
                    }));
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (openSettingsNextTick) {
                client.setScreen(new SettingsScreen());
                openSettingsNextTick = false;
            }
        });
    }
}