package dev.turtywurty.soundboardmod.client;

import com.mojang.blaze3d.platform.InputConstants;
import dev.turtywurty.soundboardmod.SoundboardMod;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT, modid = SoundboardMod.MODID)
public class ClientModEvents {
    @SubscribeEvent
    public static void registerKeybinds(RegisterKeyMappingsEvent event) {
        var openSoundboardKeybind = new KeyMapping("key.soundboard.open", KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, InputConstants.KEY_F7, KeyMapping.CATEGORY_INTERFACE);
        Keybindings.INSTANCE.setOpenSoundboardGui(openSoundboardKeybind);
        event.register(openSoundboardKeybind);
    }

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(SoundInfo::loadSounds);
    }
}
