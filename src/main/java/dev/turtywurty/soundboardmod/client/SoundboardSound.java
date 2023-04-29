package dev.turtywurty.soundboardmod.client;

import com.mojang.blaze3d.audio.OggAudioStream;
import dev.turtywurty.soundboardmod.SoundboardMod;
import net.minecraft.client.resources.sounds.AbstractSoundInstance;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.AudioStream;
import net.minecraft.client.sounds.SoundBufferLibrary;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.Vec3;

import javax.sound.sampled.AudioFormat;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

public class SoundboardSound extends AbstractSoundInstance {
    public static final ResourceLocation SOUND_LOC = new ResourceLocation(SoundboardMod.MODID, "soundboard_sound");
    private final Path soundPath;

    public SoundboardSound(Path soundPath, Vec3 position, float volume, float pitch) {
        super(SOUND_LOC, SoundSource.MASTER, SoundInstance.createUnseededRandom());

        this.soundPath = soundPath;

        this.x = position.x;
        this.y = position.y;
        this.z = position.z;

        this.volume = volume;
        this.pitch = pitch;
    }

    @Override
    public CompletableFuture<AudioStream> getStream(SoundBufferLibrary soundBuffers, Sound sound, boolean looping) {
        try {
            return CompletableFuture.completedFuture(new OggAudioStream(Files.newInputStream(this.soundPath)));
        } catch (IOException exception) {
            return CompletableFuture.failedFuture(exception);
        }
    }
}
