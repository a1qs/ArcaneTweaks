package com.livajq.arcanetweaks.bossbehavior;

import net.miauczel.legendary_monsters.entity.ModEntities;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;

import java.util.HashMap;
import java.util.Map;

public class BossBehaviorRegistry {
    
    private static final Map<EntityType<?>, BossBehavior<? extends LivingEntity>> BEHAVIORS = new HashMap<>();
    
    public static <T extends LivingEntity> void register(EntityType<T> type, BossBehavior<T> behavior) {
        BEHAVIORS.put(type, behavior);
    }
    
    @SuppressWarnings("unchecked")
    public static <T extends LivingEntity> BossBehavior<T> get(T entity) {
        return (BossBehavior<T>) BEHAVIORS.get(entity.getType());
    }
    
    public static void init() {
        register(ModEntities.THE_OBLITERATOR.get(), new ObliteratorBehavior());
        register(com.github.L_Ender.cataclysm.init.ModEntities.IGNIS.get(), new IgnisBehavior());
        register(com.github.L_Ender.cataclysm.init.ModEntities.SCYLLA.get(), new ScyllaBehavior());
        register(com.github.L_Ender.cataclysm.init.ModEntities.MALEDICTUS.get(), new MaledictusBehavior());
    }
}