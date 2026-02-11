package net.marblock.keystrokes.config;

import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class KeystrokesConfig {
    public static int x = 25;
    public static int y = 22;
    public static boolean showFPS = true;
    public static boolean showCPS = true;
    public static boolean showSpace = true;
    public static boolean showKeys = true;
    public static boolean isEnabled = true;

    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("modern-keystrokes.properties");

    public static void save() {
        Properties props = new Properties();
        props.setProperty("x", String.valueOf(x));
        props.setProperty("y", String.valueOf(y));
        props.setProperty("showFPS", String.valueOf(showFPS));
        props.setProperty("showCPS", String.valueOf(showCPS));
        props.setProperty("showSpace", String.valueOf(showSpace));
        props.setProperty("showKeys", String.valueOf(showKeys));
        props.setProperty("isEnabled", String.valueOf(isEnabled));

        try (OutputStream out = Files.newOutputStream(CONFIG_PATH)) {
            props.store(out, "Modern Keystrokes Configuration");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void load() {
        if (!Files.exists(CONFIG_PATH)) return;

        Properties props = new Properties();
        try (InputStream in = Files.newInputStream(CONFIG_PATH)) {
            props.load(in);
            x = Integer.parseInt(props.getProperty("x", "25"));
            y = Integer.parseInt(props.getProperty("y", "22"));
            showFPS = Boolean.parseBoolean(props.getProperty("showFPS", "true"));
            showCPS = Boolean.parseBoolean(props.getProperty("showCPS", "true"));
            showSpace = Boolean.parseBoolean(props.getProperty("showSpace", "true"));
            showKeys = Boolean.parseBoolean(props.getProperty("showKeys", "true"));
            isEnabled = Boolean.parseBoolean(props.getProperty("isEnabled", "true"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
