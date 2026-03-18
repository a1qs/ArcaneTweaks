package com.livajq.arcanetweaks.handlers;

import com.github.alexmodguy.alexscaves.server.entity.living.ForsakenEntity;
import com.livajq.arcanetweaks.ArcaneTweaks;
import com.livajq.arcanetweaks.Config;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ArcaneTweaks.MODID)
public class SporeHandler {
    private static final TagKey<EntityType<?>> FUNGUS_ENTITIES = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation("spore", "fungus_entities"));
    
    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        LivingEntity victim = event.getEntity();
        Entity attacker = event.getSource().getEntity();
        
        boolean involvesSpore = attacker != null && victim != null && (isSporeMob(victim) || isSporeMob(attacker));
        if (!involvesSpore) return;
        
        if (attacker instanceof ForsakenEntity) event.setAmount((float) (event.getAmount() * Config.forsakenSporeDamageDealt));
        else if (victim instanceof ForsakenEntity) event.setAmount((float) (event.getAmount() * Config.forsakenSporeDamageTaken));
    }
    
    private static boolean isSporeMob(Entity entity) {
        return entity.getType().is(FUNGUS_ENTITIES);
    }
}