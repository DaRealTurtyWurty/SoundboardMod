package dev.turtywurty.soundboardmod.client;

import dev.turtywurty.soundboardmod.SoundboardMod;
import dev.turtywurty.soundboardmod.client.screen.SoundboardScreen;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT, modid = SoundboardMod.MODID)
public class ClientForgeEvents {
    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        if(event.getKey() == Keybindings.INSTANCE.getOpenSoundboardGui().getKey().getValue()) {
            Minecraft.getInstance().setScreen(new SoundboardScreen());
        }
    }
}
