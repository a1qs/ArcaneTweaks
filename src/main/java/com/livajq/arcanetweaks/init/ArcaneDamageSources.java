package com.livajq.arcanetweaks.init;

import com.livajq.arcanetweaks.ArcaneTweaks;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.level.Level;

public final class ArcaneDamageSources {
    
    public static final ResourceKey<DamageType> VAPORIZED = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(ArcaneTweaks.MODID, "vaporized"));
    
    public static DamageSource vaporized(Level level) {
        Holder<DamageType> type = level.registryAccess()
                .registryOrThrow(Registries.DAMAGE_TYPE)
                .getHolderOrThrow(VAPORIZED);
        
        return new DamageSource(type);
    }
}