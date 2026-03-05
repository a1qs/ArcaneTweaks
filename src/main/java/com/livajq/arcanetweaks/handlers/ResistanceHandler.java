package com.livajq.arcanetweaks.handlers;

import com.livajq.arcanetweaks.ArcaneTweaks;
import com.livajq.arcanetweaks.Config;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = ArcaneTweaks.MODID)
public class ResistanceHandler {
    private static final UUID FIRE_MAGIC_RES_UUID = UUID.fromString("d4e8f2d0-6c4a-4b6e-9f1a-2b7a6e3c1a11");
    
    @SubscribeEvent
    public static void customResistanceCalc(LivingDamageEvent event) {
        DamageSource source = event.getSource();
        if (source.is(DamageTypeTags.BYPASSES_RESISTANCE) || source.is(DamageTypeTags.BYPASSES_EFFECTS)) return;
        LivingEntity target = event.getEntity();
        MobEffectInstance resistance = target.getEffect(MobEffects.DAMAGE_RESISTANCE);
        if (resistance == null) return;
        
        float resAmount = (float) ((resistance.getAmplifier() + 1) * Config.resistanceAmount);
        event.setAmount(event.getAmount() * Math.max(1 - resAmount, 0));
    }
    
    @SubscribeEvent(priority = EventPriority.LOW)
    public static void customFireResistanceCalc(LivingDamageEvent event) {
        DamageSource source = event.getSource();
        LivingEntity target = event.getEntity();
        
        if (!source.is(DamageTypeTags.IS_FIRE) || !target.hasEffect(MobEffects.FIRE_RESISTANCE)) return;
        MobEffectInstance fireResistance = target.getEffect(MobEffects.FIRE_RESISTANCE);
        if (fireResistance == null) return;
        
        float fireResAmount = (float) ((fireResistance.getAmplifier() + 1) * Config.fireResistanceAmount);
        if (fireResAmount >= 1.0F) target.setRemainingFireTicks(0);
        event.setAmount(event.getAmount() * Math.max(1 - fireResAmount, 0));
    }
    
    @SubscribeEvent
    public static void addFireMagicResBonus(MobEffectEvent.Added event) {
        LivingEntity entity = event.getEntity();
        MobEffectInstance instance = event.getEffectInstance();
        if (instance == null || entity == null) return;
        
        if (instance.getEffect() != MobEffects.FIRE_RESISTANCE) return;
        
        AttributeInstance attr = entity.getAttribute(AttributeRegistry.FIRE_MAGIC_RESIST.get());
        
        if (attr == null) return;
        
        double value = (instance.getAmplifier() + 1) * Config.fireResistanceAmount;
        
        attr.removeModifier(FIRE_MAGIC_RES_UUID);
        
        attr.addPermanentModifier(
                new AttributeModifier(FIRE_MAGIC_RES_UUID, "Fire magic res bonus", value, AttributeModifier.Operation.MULTIPLY_TOTAL)
        );
    }
    
    @SubscribeEvent
    public static void removeFireMagicResBonus(MobEffectEvent.Remove event) {
        LivingEntity entity = event.getEntity();
        if (entity == null) return;
        if (event.getEffect() != MobEffects.FIRE_RESISTANCE) return;
        
        AttributeInstance attr = entity.getAttribute(AttributeRegistry.FIRE_MAGIC_RESIST.get());
        if (attr != null) attr.removeModifier(FIRE_MAGIC_RES_UUID);
        
    }
}
