package com.livajq.arcanetweaks.init;

import com.livajq.arcanetweaks.ArcaneTweaks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ArcaneSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, ArcaneTweaks.MODID);
    
    public static final RegistryObject<SoundEvent> SOUNDHEHE =
            SOUNDS.register("soundhehe",
                    () -> SoundEvent.createVariableRangeEvent(
                            new ResourceLocation(ArcaneTweaks.MODID, "soundhehe")
                    ));
}