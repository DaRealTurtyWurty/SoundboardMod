package dev.turtywurty.soundboardmod.client;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.turtywurty.soundboardmod.SoundboardMod;
import net.minecraft.client.Minecraft;
import net.minecraft.util.GsonHelper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SoundInfo {
    public static final List<SoundInfo> SOUNDS = new ArrayList<>();

    private String name;
    private String path;
    private String imagePath;
    private String category;
    private String description;

    public SoundInfo(String name, String path, String imagePath, String category, String description) {
        this.name = name;
        this.path = path;
        this.imagePath = imagePath;
        this.category = category;
        this.description = description;
    }

    public String getName() {
        return this.name;
    }

    public String getPath() {
        return this.path;
    }

    public String getImagePath() {
        return this.imagePath;
    }

    public String getCategory() {
        return this.category;
    }

    public String getDescription() {
        return this.description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        var soundInfo = (SoundInfo) obj;
        return Objects.equals(name, soundInfo.name) && Objects.equals(path, soundInfo.path) && Objects.equals(imagePath,
                soundInfo.imagePath) && Objects.equals(category, soundInfo.category) && Objects.equals(description,
                soundInfo.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, path, imagePath, category, description);
    }

    public static List<SoundInfo> loadSounds() {
        try {
            SoundboardMod.LOGGER.debug("Loading soundboard...");
            var soundboardFile = new File(Minecraft.getInstance().gameDirectory, "soundboard.json");
            if (!soundboardFile.exists()) {
                SoundboardMod.LOGGER.debug("soundboard.json file not found. Creating new file...");
                Files.write(soundboardFile.toPath(), GsonHelper.toStableString(new JsonObject()).getBytes(),
                        StandardOpenOption.CREATE_NEW);
            }
            SoundboardMod.LOGGER.debug("soundboard.json file found. Loading file...");

            SoundboardMod.LOGGER.debug("Loading soundboard.json file...");
            JsonArray soundboardJson = SoundboardMod.GSON.fromJson(Files.readString(soundboardFile.toPath()),
                    JsonArray.class);
            List<SoundInfo> sounds = new ArrayList<>();
            for (JsonElement jsonElement : soundboardJson) {
                sounds.add(SoundboardMod.GSON.fromJson(jsonElement, SoundInfo.class));
            }
            SoundboardMod.LOGGER.debug("soundboard.json file loaded successfully!");


            SoundboardMod.LOGGER.debug("Loading soundboard sounds...");
            SoundInfo.SOUNDS.clear();
            SoundInfo.SOUNDS.addAll(sounds);
            SoundboardMod.LOGGER.debug("Soundboard sounds loaded successfully!");
        } catch (IOException exception) {
            SoundboardMod.LOGGER.error("Failed to read/write to soundboard.json file", exception);
            System.exit(1);
        }

        return List.copyOf(SoundInfo.SOUNDS);
    }

    public static void saveSounds() {
        try {
            SoundboardMod.LOGGER.debug("Saving soundboard...");
            var soundboardFile = new File(Minecraft.getInstance().gameDirectory, "soundboard.json");
            if (!soundboardFile.exists()) {
                SoundboardMod.LOGGER.debug("soundboard.json file not found. Creating new file...");
                Files.write(soundboardFile.toPath(), GsonHelper.toStableString(new JsonObject()).getBytes(),
                        StandardOpenOption.CREATE_NEW);
            }
            SoundboardMod.LOGGER.debug("soundboard.json file found. Saving file...");

            SoundboardMod.LOGGER.debug("Saving soundboard.json file...");
            JsonArray soundboardJson = new JsonArray();
            for (SoundInfo sound : SoundInfo.SOUNDS) {
                soundboardJson.add(SoundboardMod.GSON.toJsonTree(sound));
            }
            Files.write(soundboardFile.toPath(), GsonHelper.toStableString(soundboardJson).getBytes(),
                    StandardOpenOption.TRUNCATE_EXISTING);
            SoundboardMod.LOGGER.debug("soundboard.json file saved successfully!");
        } catch (IOException exception) {
            SoundboardMod.LOGGER.error("Failed to read/write to soundboard.json file", exception);
            System.exit(1);
        }
    }
}
