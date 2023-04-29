package dev.turtywurty.soundboardmod.client;

import com.mojang.blaze3d.audio.Library;
import dev.turtywurty.soundboardmod.SoundboardMod;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.ChannelAccess;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class Utils {
    public static boolean isPosInArea(double x, double y, double width, double height, double posX, double posY) {
        return posX >= x && posX <= x + width && posY >= y && posY <= y + height;
    }

    public static double getMouseX() {
        return Minecraft.getInstance().mouseHandler.xpos() * Minecraft.getInstance().getWindow().getGuiScaledWidth()
                / Minecraft.getInstance().getWindow().getScreenWidth();
    }

    public static double getMouseY() {
        return Minecraft.getInstance().mouseHandler.ypos() * Minecraft.getInstance().getWindow().getGuiScaledHeight()
                / Minecraft.getInstance().getWindow().getScreenHeight();
    }

    public static boolean isSoundFile(Path path) {
        if(!Files.isRegularFile(path)) return false;

        String fileName = path.getFileName().toString();
        return fileName.endsWith(".ogg");
    }

    public static Optional<Path> findFirstSoundFile(List<Path> paths) {
        return paths.stream().filter(Utils::isSoundFile).findFirst();
    }

    public static boolean isImageFile(Path path) {
        if(!Files.isRegularFile(path)) return false;

        String fileName = path.getFileName().toString();
        return fileName.endsWith(".png");
    }

    public static Optional<Path> findFirstImageFile(List<Path> paths) {
        return paths.stream().filter(Utils::isImageFile).findFirst();
    }
}
