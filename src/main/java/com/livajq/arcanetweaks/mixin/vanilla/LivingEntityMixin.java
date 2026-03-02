package com.livajq.arcanetweaks.mixin.vanilla;

import com.livajq.arcanetweaks.Config;
import com.livajq.arcanetweaks.interfaces.TickSpeedAccessor;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

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
    
    @Redirect(
            method = "getDamageAfterMagicAbsorb",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/lang/Math;max(FF)F"
            )
    )
    private float redirectResCalc(float a, float b, DamageSource src, float pDamageAmount) {
        LivingEntity self = (LivingEntity)(Object)this;
        if (!self.hasEffect(MobEffects.DAMAGE_RESISTANCE)) return pDamageAmount;
        
        final double resPerLvl = Config.resistanceAmount; //% reduction per resistance level
        pDamageAmount -= (float) (pDamageAmount * (self.getEffect(MobEffects.DAMAGE_RESISTANCE).getAmplifier() + 1) * resPerLvl);
        return Math.max(pDamageAmount, 0.0F);
    }
}