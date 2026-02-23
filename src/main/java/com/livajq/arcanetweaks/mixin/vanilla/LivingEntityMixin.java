package com.livajq.arcanetweaks.mixin.vanilla;

import com.livajq.arcanetweaks.interfaces.TickSpeedAccessor;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(LivingEntity.class)
public class LivingEntityMixin implements TickSpeedAccessor {
    
    @Unique
    private double arcanetweaks$tickSpeedAccumulator = 0;
    
    @Override
    public double arcanetweaks$getTickSpeedAccumulator() {
        return arcanetweaks$tickSpeedAccumulator;
    }
    
    @Override
    public void arcanetweaks$setTickSpeedAccumulator(double value) {
        arcanetweaks$tickSpeedAccumulator = value;
    }
}