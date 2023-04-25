package dev.turtywurty.soundboardmod.client;

import net.minecraft.client.KeyMapping;

public class Keybindings {
    public static final Keybindings INSTANCE = new Keybindings();

    private KeyMapping openSoundboardGui;

    public KeyMapping getOpenSoundboardGui() {
        return this.openSoundboardGui;
    }

    public void setOpenSoundboardGui(KeyMapping openSoundboardGui) {
        if(this.openSoundboardGui != null) {
            throw new IllegalStateException("Open Soundboard GUI Keybinding already set!");
        }

        if(openSoundboardGui == null) {
            throw new IllegalArgumentException("Open Soundboard GUI Keybinding cannot be null!");
        }

        this.openSoundboardGui = openSoundboardGui;
    }
}
