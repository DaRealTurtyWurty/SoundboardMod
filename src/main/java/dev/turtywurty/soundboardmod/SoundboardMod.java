package dev.turtywurty.soundboardmod;

import com.google.gson.Gson;
import com.mojang.logging.LogUtils;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

@Mod(SoundboardMod.MODID)
public class SoundboardMod {
    public static final String MODID = "soundboard";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final Gson GSON = new Gson();

    public SoundboardMod() {
    }
}
