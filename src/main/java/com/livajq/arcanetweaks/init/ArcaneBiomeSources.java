package com.livajq.arcanetweaks.init;

import com.livajq.arcanetweaks.ArcaneTweaks;
import com.livajq.arcanetweaks.world.district.DistrictBiomeSource;
import com.mojang.serialization.Codec;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraftforge.registries.RegisterEvent;

public class ArcaneBiomeSources {
    public static final ResourceKey<Codec<? extends BiomeSource>> DISTRICT =
            ResourceKey.create(Registries.BIOME_SOURCE, new ResourceLocation(ArcaneTweaks.MODID, "district"));
    
    public static void register(RegisterEvent.RegisterHelper<Codec<? extends BiomeSource>> helper) {
        helper.register(DISTRICT.location(), DistrictBiomeSource.CODEC);
    }
}