package net.marblock.keystrokes.gui;

import net.marblock.keystrokes.ModernKeystrokesClient;
import net.marblock.keystrokes.config.KeystrokesConfig;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class SettingsScreen extends Screen {

    public SettingsScreen() {
        super(Component.literal("Modern Keystrokes"));
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;

        int btnW = 100;
        int btnH = 20;
        int gap = 4;

        int currentY = 40;

        this.addRenderableWidget(Button.builder(Component.literal("Mod: " + getStatus(KeystrokesConfig.isEnabled)), b -> {
            KeystrokesConfig.isEnabled = !KeystrokesConfig.isEnabled;
            b.setMessage(Component.literal("Mod: " + getStatus(KeystrokesConfig.isEnabled)));
        }).bounds(centerX - btnW - gap, currentY, btnW, btnH).build());

        this.addRenderableWidget(Button.builder(Component.literal("FPS: " + getStatus(KeystrokesConfig.showFPS)), b -> {
            KeystrokesConfig.showFPS = !KeystrokesConfig.showFPS;
            b.setMessage(Component.literal("FPS: " + getStatus(KeystrokesConfig.showFPS)));
        }).bounds(centerX + gap, currentY, btnW, btnH).build());

        currentY += btnH + gap;

        this.addRenderableWidget(Button.builder(Component.literal("CPS: " + getStatus(KeystrokesConfig.showCPS)), b -> {
            KeystrokesConfig.showCPS = !KeystrokesConfig.showCPS;
            b.setMessage(Component.literal("CPS: " + getStatus(KeystrokesConfig.showCPS)));
        }).bounds(centerX - btnW - gap, currentY, btnW, btnH).build());

        this.addRenderableWidget(Button.builder(Component.literal("Keys: " + getStatus(KeystrokesConfig.showKeys)), b -> {
            KeystrokesConfig.showKeys = !KeystrokesConfig.showKeys;
            b.setMessage(Component.literal("Keys: " + getStatus(KeystrokesConfig.showKeys)));
        }).bounds(centerX + gap, currentY, btnW, btnH).build());

        currentY += btnH + gap;

        this.addRenderableWidget(Button.builder(Component.literal("Spacebar: " + getStatus(KeystrokesConfig.showSpace)), b -> {
            KeystrokesConfig.showSpace = !KeystrokesConfig.showSpace;
            b.setMessage(Component.literal("Spacebar: " + getStatus(KeystrokesConfig.showSpace)));
        }).bounds(centerX - 75, currentY, 150, btnH).build());

        currentY += btnH + 15;
        int sliderW = 200;

        double currentXVal = Math.max(0.0, Math.min(1.0, (double) KeystrokesConfig.x / Math.max(1, this.width - 65)));
        this.addRenderableWidget(new PositionSlider(centerX - sliderW / 2, currentY, sliderW, btnH, currentXVal, false));

        currentY += btnH + gap;

        double currentYVal = Math.max(0.0, Math.min(1.0, (double) KeystrokesConfig.y / Math.max(1, this.height - 85)));
        this.addRenderableWidget(new PositionSlider(centerX - sliderW / 2, currentY, sliderW, btnH, currentYVal, true));
        int bottomY = this.height - 28;

        this.addRenderableWidget(Button.builder(Component.literal("Reset Pos"), b -> {
            KeystrokesConfig.x = 25;
            KeystrokesConfig.y = 22;
            this.rebuildWidgets();
        }).bounds(centerX - btnW - gap, bottomY, btnW, btnH).build());

        this.addRenderableWidget(Button.builder(Component.literal("Save & Exit"), b -> {
            KeystrokesConfig.save();
            this.onClose();
        }).bounds(centerX + gap, bottomY, btnW, btnH).build());
    }

    private String getStatus(boolean b) {
        return b ? "ON" : "OFF";
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 15, 0xFFFFFF);

        if (ModernKeystrokesClient.renderer != null) {
            net.marblock.keystrokes.ModernKeystrokesClient.renderer.render(guiGraphics, partialTick);
        }
    }

    @Override
    public void onClose() {
        KeystrokesConfig.save();
        super.onClose();
    }

    private class PositionSlider extends AbstractSliderButton {
        private final boolean isYAxis;

        public PositionSlider(int x, int y, int width, int height, double initialValue, boolean isYAxis) {
            super(x, y, width, height, Component.empty(), initialValue);
            this.isYAxis = isYAxis;
            this.updateMessage();
        }

        @Override
        protected void updateMessage() {
            int max = isYAxis ? Math.max(1, SettingsScreen.this.height - 85) : Math.max(1, SettingsScreen.this.width - 65);
            int currentValue = (int) (this.value * max);
            String label = isYAxis ? "Y: " : "X: ";
            this.setMessage(Component.literal(label + currentValue));
        }

        @Override
        protected void applyValue() {
            int max = isYAxis ? Math.max(1, SettingsScreen.this.height - 85) : Math.max(1, SettingsScreen.this.width - 65);
            int pixelValue = (int) (this.value * max);

            if (isYAxis) {
                KeystrokesConfig.y = pixelValue;
            } else {
                KeystrokesConfig.x = pixelValue;
            }
        }
    }
}