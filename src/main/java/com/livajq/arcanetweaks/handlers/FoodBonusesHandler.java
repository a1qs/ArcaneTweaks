package com.livajq.arcanetweaks.handlers;

import com.livajq.arcanetweaks.ArcaneTweaks;
import com.livajq.arcanetweaks.Config;
import com.livajq.arcanetweaks.common.capability.ArcaneCapabilities;
import net.bandit.reskillable.common.commands.skills.SkillAttributeBonus;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = ArcaneTweaks.MODID)
public class FoodBonusesHandler {
    
    public static MobEffect TEMPERATURE_IMMUNITY;
    public static MobEffect HYDRATION_FILL;
    
    @SubscribeEvent
    public static void onFoodEaten(LivingEntityUseItemEvent.Finish event) {
        if (!(event.getEntity() instanceof Player player)) return;
        
        ItemStack stack = event.getItem();
        Item item = stack.getItem();

        ResourceLocation id = ForgeRegistries.ITEMS.getKey(item);
        if (id == null) return;
        
        String key = id.toString();

        if (Config.foodTemperatureImmunitySet.contains(key)) {
            player.getCapability(ArcaneCapabilities.FOOD_BONUSES).ifPresent(cap -> {
                cap.setNoTemperature(true);
            });
        }

        if (Config.foodThirstImmunitySet.contains(key)) {
            player.getCapability(ArcaneCapabilities.FOOD_BONUSES).ifPresent(cap -> {
                cap.setNoThirst(true);
            });
        }
    }
    
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        
        Player player = event.player;
        
        if (player.tickCount % 20 != 0) return;
        
        if (TEMPERATURE_IMMUNITY == null && !SyncTemperatureEffect()) return;
        if (HYDRATION_FILL == null && !SyncHydrationEffect()) return;
        
        player.getCapability(ArcaneCapabilities.FOOD_BONUSES).ifPresent(cap -> {
            
            if (cap.hasNoTemperature() && TEMPERATURE_IMMUNITY != null) {
                player.addEffect(new MobEffectInstance(TEMPERATURE_IMMUNITY, 210, 0, true, false));
            }
            
            if (cap.hasNoThirst() && HYDRATION_FILL != null) {
                player.addEffect(new MobEffectInstance(HYDRATION_FILL, 210, 0, true, false));
            }
        });
    }
    
    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (!event.isWasDeath()) return;
        
        Player oldPlayer = event.getOriginal();
        Player newPlayer = event.getEntity();
        
        oldPlayer.reviveCaps();
        
        oldPlayer.getCapability(ArcaneCapabilities.FOOD_BONUSES).ifPresent(oldCap -> {
            newPlayer.getCapability(ArcaneCapabilities.FOOD_BONUSES).ifPresent(newCap -> {
                newCap.setNoThirst(oldCap.hasNoThirst());
                newCap.setNoTemperature(oldCap.hasNoTemperature());
            });
        });
        
        oldPlayer.invalidateCaps();
    }
    
    private static boolean SyncTemperatureEffect() {
        TEMPERATURE_IMMUNITY = ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation("legendarysurvivaloverhaul", "temperature_immunity"));
        return  TEMPERATURE_IMMUNITY != null;
    }
    
    private static boolean SyncHydrationEffect() {
        HYDRATION_FILL = ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation("legendarysurvivaloverhaul", "hydration_fill"));
        return  HYDRATION_FILL != null;
    }
}