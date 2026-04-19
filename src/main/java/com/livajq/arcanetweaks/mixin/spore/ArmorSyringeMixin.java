package com.livajq.arcanetweaks.mixin.spore;

import com.Harbinger.Spore.Sitems.Agents.AbstractSyringe;
import com.Harbinger.Spore.Sitems.Agents.ArmorSyringe;
import com.Harbinger.Spore.Sitems.BaseWeapons.SporeArmorMutations;
import com.livajq.arcanetweaks.Config;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArmorSyringe.class)
public abstract class ArmorSyringeMixin extends AbstractSyringe {
    
    @Shadow
    @Final
    private SporeArmorMutations mutations;
    
    @Inject(method = "useSyringe", at = @At("HEAD"), cancellable = true, remap = false)
    private void applyAlternativeEffects(ItemStack stack, LivingEntity living, CallbackInfo ci) {
        
        Config.SporeMutationEffect mutation = switch (mutations)
        {
            case REINFORCED -> Config.sporeMutationEffectReinforced;
            case SKELETAL -> Config.sporeMutationEffectSkeletal;
            case DROWNED -> Config.sporeMutationEffectDrowned;
            case CHARRED -> Config.sporeMutationEffectCharred;
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
