package com.livajq.arcanetweaks.util;

import com.livajq.arcanetweaks.compat.alexscaves.BiomeConfig;
import com.livajq.arcanetweaks.compat.alexscaves.BiomeConfigLoader;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;

import java.util.function.Function;

public class AlexsCavesUtils {
    
    public static <T> T getBiomeOverride(Holder<Biome> biome, Function<BiomeConfig, T> getter) {
        var key = biome.unwrapKey().orElse(null);
        if (key == null) return null;
        
        BiomeConfig cfg = BiomeConfigLoader.lookupOverrides.get(key);
        return cfg == null ? null : getter.apply(cfg);
    }
    
    public static <T> T getBiomeOverride(ResourceKey<Biome> biomeKey, Function<BiomeConfig, T> getter) {
        BiomeConfig cfg = BiomeConfigLoader.lookupOverrides.get(biomeKey);
        return cfg == null ? null : getter.apply(cfg);
    }
}
