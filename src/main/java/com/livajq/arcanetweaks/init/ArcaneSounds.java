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
    
    public static final RegistryObject<SoundEvent> DRAGON_NUKE_CHARGE_FIRE =
            SOUNDS.register("dragon_nuke_charge_fire",
                    () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ArcaneTweaks.MODID, "dragon_nuke_charge_fire")));
    
    public static final RegistryObject<SoundEvent> DRAGON_NUKE_CHARGE_ICE =
            SOUNDS.register("dragon_nuke_charge_ice",
                    () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ArcaneTweaks.MODID, "dragon_nuke_charge_ice")));
    
    public static final RegistryObject<SoundEvent> DRAGON_NUKE_CHARGE_LIGHTNING =
            SOUNDS.register("dragon_nuke_charge_lightning",
                    () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ArcaneTweaks.MODID, "dragon_nuke_charge_lightning")));
    
    public static final RegistryObject<SoundEvent> DRAGON_NUKE_EXPLODE =
            SOUNDS.register("dragon_nuke_explode",
                    () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ArcaneTweaks.MODID, "dragon_nuke_explode")));
    
    public static final RegistryObject<SoundEvent> MRKRABS_SADGE =
            SOUNDS.register("mrkrabs_sadge",
                    () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ArcaneTweaks.MODID, "mrkrabs_sadge")));
}