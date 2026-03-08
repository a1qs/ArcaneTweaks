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
    
    public static final RegistryObject<SoundEvent> DRAGON_NUKE_CHARGE =
            SOUNDS.register("dragon_nuke_charge",
                    () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ArcaneTweaks.MODID, "dragon_nuke_charge")));
    
    public static final RegistryObject<SoundEvent> DRAGON_NUKE_EXPLODE =
            SOUNDS.register("dragon_nuke_explode",
                    () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ArcaneTweaks.MODID, "dragon_nuke_explode")));
}