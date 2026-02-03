package com.livajq.arcanetweaks.handlers;

import com.livajq.arcanetweaks.ArcaneTweaks;
import com.livajq.arcanetweaks.Config;
import com.livajq.arcanetweaks.mobs.MobStats;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ArcaneTweaks.MODID)
public class MobAttributeHandler {
    
    @SubscribeEvent
    public static void onJoinLevel(EntityJoinLevelEvent event) {
        if (!(event.getEntity() instanceof LivingEntity living)) return;
        
        MobStats stats = Config.mobAttributeModifiers.get(living.getType());
        if (stats == null) return;
        
        apply(living, Attributes.ATTACK_DAMAGE, stats.attack());
        apply(living, Attributes.ARMOR, stats.armor());
        apply(living, Attributes.MAX_HEALTH, stats.health());
        apply(living, Attributes.MOVEMENT_SPEED, stats.speed());
        apply(living, Attributes.FOLLOW_RANGE, stats.follow());
    }
    
    private static void apply(LivingEntity e, Attribute attr, double mult) {
        AttributeInstance inst = e.getAttribute(attr);
        if (inst == null) return;
        
        inst.setBaseValue(inst.getBaseValue() * mult);
        
        if (attr == Attributes.MAX_HEALTH) e.setHealth((float) inst.getBaseValue());
    }
    
}
