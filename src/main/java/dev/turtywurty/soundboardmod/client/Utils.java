package dev.turtywurty.soundboardmod.client;

import net.minecraft.client.Minecraft;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

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
        return fileName.endsWith(".wav") || fileName.endsWith(".mp3") || fileName.endsWith(".ogg");
    }

    public static Optional<Path> findFirstSoundFile(List<Path> paths) {
        return paths.stream().filter(Utils::isSoundFile).findFirst();
    }

    public static boolean isImageFile(Path path) {
        if(!Files.isRegularFile(path)) return false;

        String fileName = path.getFileName().toString();
        return fileName.endsWith(".png") || fileName.endsWith(".jpg") || fileName.endsWith(".jpeg");
    }

    public static Optional<Path> findFirstImageFile(List<Path> paths) {
        return paths.stream().filter(Utils::isImageFile).findFirst();
    }

    public static boolean hasAlphaChannel(Path path) {
        if(!isImageFile(path)) return false;
        return path.getFileName().toString().endsWith(".png");
    }
}
