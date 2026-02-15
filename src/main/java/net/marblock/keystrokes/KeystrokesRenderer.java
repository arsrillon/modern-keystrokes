package net.marblock.keystrokes;

import net.marblock.keystrokes.config.KeystrokesConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import java.awt.Color;
import java.util.Deque;
import java.util.LinkedList;

public class KeystrokesRenderer {
    private final Deque<Long> leftClickTimestamps = new LinkedList<>();
    private final Deque<Long> rightClickTimestamps = new LinkedList<>();

    private static final int KEY_SIZE = 18;
    private static final int KEY_SPACING = 5;
    private static final int LMB_RMB_Y_OFFSET = (KEY_SPACING + KEY_SIZE) - 3;
    private static final int SPACEBAR_Y_OFFSET = 2 * KEY_SPACING;

    private static final int INDICATOR_WIDTH = 45;
    private static final int INDICATOR_HEIGHT = 11;
    private static final float TEXT_SCALE = 0.75f;
    private static final float CPS_TEXT_SCALE = 0.65f;

    public void render(GuiGraphics guiGraphics, float partialTick) {
        if (!KeystrokesConfig.isEnabled) return;

        Minecraft mc = Minecraft.getInstance();
        Font font = mc.font;
        long currentTime = System.currentTimeMillis();

        while (!leftClickTimestamps.isEmpty() && currentTime - leftClickTimestamps.getFirst() > 1000) {
            leftClickTimestamps.pollFirst();
        }
        while (!rightClickTimestamps.isEmpty() && currentTime - rightClickTimestamps.getFirst() > 1000) {
            rightClickTimestamps.pollFirst();
        }

        float hue = (currentTime % 3000L) / 3000.0f;
        int rgbColor = Color.getHSBColor(hue, 1, 1).getRGB();

        int fpsBoxTop = KeystrokesConfig.y - INDICATOR_HEIGHT - KEY_SPACING; // Исправлено позиционирование FPS

        if (KeystrokesConfig.showFPS) {
            renderFPS(guiGraphics, font, "FPS: " + mc.getFps(), KeystrokesConfig.x + 4 - KEY_SIZE - KEY_SPACING, fpsBoxTop, rgbColor);
        }
        if (KeystrokesConfig.showKeys) {
            renderKeys(guiGraphics, font, mc, rgbColor);
        }
        if (KeystrokesConfig.showSpace) {
            renderSpaceBar(guiGraphics, mc, rgbColor);
        }
        if (KeystrokesConfig.showCPS) {
            boolean leftTransition = !leftClickTimestamps.isEmpty() && (currentTime - leftClickTimestamps.getLast() < 50);
            boolean rightTransition = !rightClickTimestamps.isEmpty() && (currentTime - rightClickTimestamps.getLast() < 50);

            renderCPS(guiGraphics, font, "LMB", leftClickTimestamps.size() + " CPS", KeystrokesConfig.x - KEY_SIZE - KEY_SPACING, KeystrokesConfig.y + 2 * KEY_SIZE + LMB_RMB_Y_OFFSET, rgbColor, leftTransition);
            renderCPS(guiGraphics, font, "RMB", rightClickTimestamps.size() + " CPS", KeystrokesConfig.x + KEY_SIZE + KEY_SPACING, KeystrokesConfig.y + 2 * KEY_SIZE + LMB_RMB_Y_OFFSET, rgbColor, rightTransition);
        }
    }

    private void renderKeys(GuiGraphics guiGraphics, Font font, Minecraft mc, int rgbColor) {
        int textColor = 0xFFFFFFFF;
        renderKeyBar(guiGraphics, font, "W", KeystrokesConfig.x, KeystrokesConfig.y, KEY_SIZE, KEY_SIZE, mc.options.keyUp.isDown(), rgbColor, textColor);
        renderKeyBar(guiGraphics, font, "A", KeystrokesConfig.x - KEY_SIZE - KEY_SPACING, KeystrokesConfig.y + KEY_SIZE + KEY_SPACING, KEY_SIZE, KEY_SIZE, mc.options.keyLeft.isDown(), rgbColor, textColor);
        renderKeyBar(guiGraphics, font, "S", KeystrokesConfig.x, KeystrokesConfig.y + KEY_SIZE + KEY_SPACING, KEY_SIZE, KEY_SIZE, mc.options.keyDown.isDown(), rgbColor, textColor);
        renderKeyBar(guiGraphics, font, "D", KeystrokesConfig.x + KEY_SIZE + KEY_SPACING, KeystrokesConfig.y + KEY_SIZE + KEY_SPACING, KEY_SIZE, KEY_SIZE, mc.options.keyRight.isDown(), rgbColor, textColor);
    }

    private void renderSpaceBar(GuiGraphics guiGraphics, Minecraft mc, int rgbColor) {
        int bgColor = mc.options.keyJump.isDown() ? rgbColor : 0x80000000;
        int spaceBarWidth = 3 * KEY_SIZE + 2 * KEY_SPACING;
        int spaceBarHeight = KEY_SIZE / 4;
        int spaceBarX = KeystrokesConfig.x + 1 - KEY_SIZE - KEY_SPACING;
        int spaceBarY = KeystrokesConfig.y + 2 * KEY_SIZE + SPACEBAR_Y_OFFSET;

        guiGraphics.fill(spaceBarX, spaceBarY, spaceBarX + spaceBarWidth, spaceBarY + spaceBarHeight, bgColor);
        drawBorder(guiGraphics, spaceBarX, spaceBarY, spaceBarX + spaceBarWidth, spaceBarY + spaceBarHeight, rgbColor);
    }

    private void renderFPS(GuiGraphics guiGraphics, Font font, String fpsText, int x, int y, int borderColor) {
        int adjustedX = x + 5;
        guiGraphics.fill(adjustedX, y, adjustedX + INDICATOR_WIDTH, y + INDICATOR_HEIGHT, 0x80000000);
        drawBorder(guiGraphics, adjustedX, y, adjustedX + INDICATOR_WIDTH, y + INDICATOR_HEIGHT, borderColor);

        guiGraphics.pose().pushMatrix();
        guiGraphics.pose().scale(TEXT_SCALE, TEXT_SCALE);
        int textColor = (borderColor == Color.WHITE.getRGB()) ? 0xFF000000 : 0xFFFFFFFF;
        int centeredX = (int) ((adjustedX + (INDICATOR_WIDTH / 2f)) / TEXT_SCALE);
        int centeredY = (int) ((y + (INDICATOR_HEIGHT / 2f) - (font.lineHeight * TEXT_SCALE / 2f)) / TEXT_SCALE);
        guiGraphics.drawString(font, fpsText, centeredX - font.width(fpsText) / 2, centeredY, textColor, false);
        guiGraphics.pose().popMatrix();
    }

    private void renderCPS(GuiGraphics guiGraphics, Font font, String label, String cps, int x, int y, int borderColor, boolean transition) {
        int bgColor = transition ? Color.WHITE.getRGB() : 0x80000000;
        int textColor = transition ? 0xFF000000 : 0xFFFFFFFF;

        guiGraphics.fill(x, y, x + INDICATOR_WIDTH, y + INDICATOR_HEIGHT * 2, bgColor);
        drawBorder(guiGraphics, x, y, x + INDICATOR_WIDTH, y + INDICATOR_HEIGHT * 2, borderColor);

        guiGraphics.pose().pushMatrix();
        guiGraphics.pose().scale(CPS_TEXT_SCALE, CPS_TEXT_SCALE);
        guiGraphics.drawCenteredString(font, label, (int) ((x + INDICATOR_WIDTH / 2f) / CPS_TEXT_SCALE), (int) ((y + 3) / CPS_TEXT_SCALE), textColor);
        guiGraphics.drawCenteredString(font, cps, (int) ((x + INDICATOR_WIDTH / 2f) / CPS_TEXT_SCALE), (int) ((y + INDICATOR_HEIGHT + 2) / CPS_TEXT_SCALE), textColor);
        guiGraphics.pose().popMatrix();
    }

    private void renderKeyBar(GuiGraphics guiGraphics, Font font, String key, int x, int y, int width, int height, boolean isPressed, int borderColor, int textColor) {
        int bgColor = isPressed ? 0xFFFFFFFF : 0x80000000;
        guiGraphics.fill(x, y, x + width, y + height, bgColor);
        if (bgColor == 0xFFFFFFFF) textColor = 0xFF000000;
        drawBorder(guiGraphics, x, y, x + width, y + height, borderColor);
        guiGraphics.drawCenteredString(font, key, x + width / 2, y + (height / 2) - 4, textColor);
    }

    private void drawBorder(GuiGraphics guiGraphics, int x1, int y1, int x2, int y2, int color) {
        guiGraphics.fill(x1 - 1, y1 - 1, x2 + 1, y1, color);
        guiGraphics.fill(x1 - 1, y2, x2 + 1, y2 + 1, color);
        guiGraphics.fill(x1 - 1, y1, x1, y2, color);
        guiGraphics.fill(x2, y1, x2 + 1, y2, color);
    }

    public void incrementLeftClicks() { leftClickTimestamps.addLast(System.currentTimeMillis()); }
    public void incrementRightClicks() { rightClickTimestamps.addLast(System.currentTimeMillis()); }
}