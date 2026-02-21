package com.livajq.arcanetweaks.handlers;

import com.livajq.arcanetweaks.ArcaneTweaks;
import com.livajq.arcanetweaks.bossbehavior.BossBehavior;
import com.livajq.arcanetweaks.bossbehavior.BossBehaviorRegistry;
import com.livajq.arcanetweaks.bossbehavior.HurtResult;
import com.livajq.arcanetweaks.common.capability.ArcaneCapabilities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = ArcaneTweaks.MODID)
public class BossBehaviorHandler {
    
    @SuppressWarnings("unchecked")
    private static <T extends LivingEntity> void dispatch(BossBehavior<?> behavior, LivingEntity boss, Callback<T> cb) {
        cb.call((BossBehavior<T>) behavior, (T) boss);
    }
    
    @SuppressWarnings("unchecked")
    private static <T extends LivingEntity, R> R dispatchReturn(BossBehavior<?> behavior, LivingEntity boss, ReturnCallback<T, R> cb) {
        return cb.call((BossBehavior<T>) behavior, (T) boss);
    }
    
    @FunctionalInterface
    private interface Callback<T extends LivingEntity> {
        void call(BossBehavior<T> behavior, T boss);
    }
    
    @FunctionalInterface
    private interface ReturnCallback<T extends LivingEntity, R> {
        R call(BossBehavior<T> behavior, T boss);
    }

    @SubscribeEvent
    public static void onBossTick(LivingEvent.LivingTickEvent event) {
        LivingEntity boss = event.getEntity();
        if (boss.level().isClientSide()) return;
        
        BossBehavior<?> behavior = BossBehaviorRegistry.get(boss);
        if (behavior == null) return;
        
        double hpRatio = boss.getHealth() / boss.getMaxHealth();
        
        int newPhase = 1;
        for (int i = 0; i < behavior.thresholds.length; i++) {
            if (hpRatio <= behavior.thresholds[i]) newPhase = i + 2;
        }
        
        CompoundTag tag = boss.getPersistentData();
        
        int currentPhase = tag.contains("Arcane_BossPhase") ? tag.getInt("Arcane_BossPhase") : 1;
        int visitedMask = tag.getInt("Arcane_BossVisitedMask");
        
        if (newPhase != currentPhase) {
            boolean firstTime = (visitedMask & (1 << (newPhase - 1))) == 0;
            
            visitedMask |= (1 << (newPhase - 1));
            
            tag.putInt("Arcane_BossPhase", newPhase);
            tag.putInt("Arcane_BossVisitedMask", visitedMask);
            
            if (!boss.isDeadOrDying()) {
                int finalNewPhase = newPhase;
                dispatch(behavior, boss,
                        (b, e) -> b.onPhaseChange(e, finalNewPhase, currentPhase, firstTime));
            }
        }
        
        dispatch(behavior, boss,
                (b, e) -> b.onPhaseTick(e, currentPhase));
    }
    
    @SubscribeEvent
    public static void onBossHurt(LivingHurtEvent event) {
        LivingEntity boss = event.getEntity();
        BossBehavior<?> behavior = BossBehaviorRegistry.get(boss);
        if (behavior == null) return;
        
        HurtResult result = dispatchReturn(behavior, boss,
                (b, e) -> b.onHurt(e, event.getSource(), event.getAmount()));
        
        if (result.cancel) {
            event.setCanceled(true);
            return;
        }
        if (result.modify) event.setAmount(result.newAmount);
    }
    
    @SubscribeEvent
    public static void onBossJoin(EntityJoinLevelEvent event) {
        if (!(event.getEntity() instanceof LivingEntity boss)) return;
        if (boss.level().isClientSide()) return;
        
        BossBehavior<?> behavior = BossBehaviorRegistry.get(boss);
        if (behavior == null) return;
        
        dispatch(behavior, boss,
                (b, e) -> b.reconcileMinions(e));
    }
    
    @SubscribeEvent
    public static void onBossDeath(LivingDeathEvent event) {
        LivingEntity boss = event.getEntity();
        if (boss.level().isClientSide()) return;
        
        BossBehavior<?> behavior = BossBehaviorRegistry.get(boss);
        if (behavior == null) return;
        
        dispatch(behavior, boss,
                (b, e) -> b.onBossDied(e));
    }
    
    @SubscribeEvent
    public static void onMinionDeath(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof Mob mob)) return;
        
        mob.getCapability(ArcaneCapabilities.BOSS_MINION).ifPresent(cap -> {
            LivingEntity boss = cap.getBoss();
            if (boss == null) return;
            
            BossBehavior<?> behavior = BossBehaviorRegistry.get(boss);
            if (behavior == null) return;
            
            Set<UUID> set = BossBehavior.MINIONS.get(boss);
            if (set != null) set.remove(mob.getUUID());
            
            dispatch(behavior, boss,
                    (b, e) -> b.onMinionDied(e, mob));
        });
    }
    
    @SubscribeEvent
    public static void onMinionJoin(EntityJoinLevelEvent event) {
        if (!(event.getEntity() instanceof Mob mob)) return;
        
        mob.getCapability(ArcaneCapabilities.BOSS_MINION).ifPresent(cap -> {
            LivingEntity boss = cap.getBoss();
            if (boss == null) return;
            
            BossBehavior<?> behavior = BossBehaviorRegistry.get(boss);
            if (behavior == null) return;
            
            Set<UUID> set = BossBehavior.MINIONS.computeIfAbsent(boss, b -> new HashSet<>());
            
            if (set.add(mob.getUUID())) {
                dispatch(behavior, boss,
                        (b, e) -> b.onMinionAdded(e, mob));
            }
        });
    }
}
