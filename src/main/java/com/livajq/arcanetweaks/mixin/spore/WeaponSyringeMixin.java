package com.livajq.arcanetweaks.mixin.spore;

import com.Harbinger.Spore.Sitems.Agents.AbstractSyringe;
import com.Harbinger.Spore.Sitems.Agents.WeaponSyringe;
import com.Harbinger.Spore.Sitems.BaseWeapons.SporeToolsMutations;
import com.livajq.arcanetweaks.Config;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WeaponSyringe.class)
public abstract class WeaponSyringeMixin extends AbstractSyringe {
    
    @Shadow
    @Final
    private SporeToolsMutations mutations;
    
    @Inject(method = "useSyringe", at = @At("HEAD"), cancellable = true, remap = false)
    private void applyAlternativeEffects(ItemStack stack, LivingEntity living, CallbackInfo ci) {
        
        Config.SporeMutationEffect mutation = switch (mutations) {
            case VAMPIRIC -> Config.sporeMutationEffectVampiric;
            case CALCIFIED -> Config.sporeMutationEffectCalcified;
            case BEZERK -> Config.sporeMutationEffectBezerk;
            case TOXIC -> Config.sporeMutationEffectToxic;
            case ROTTEN -> Config.sporeMutationEffectRotten;
            default -> null;
        };
        
        if (mutation == null) return;
        MobEffect effect = ForgeRegistries.MOB_EFFECTS.getValue(mutation.id());
        if (effect == null) return;
        
        living.addEffect(new MobEffectInstance(effect, mutation.duration(), mutation.amplifier() * 2 + 1, false, true));
        this.addMycelium(living);
        stack.shrink(1);
        
        ci.cancel();
    }
}
