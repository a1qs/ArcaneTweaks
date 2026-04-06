package com.livajq.arcanetweaks.common.capability;

import com.livajq.arcanetweaks.common.capability.bossminion.BossMinionData;
import com.livajq.arcanetweaks.common.capability.foodbonuses.FoodBonusesData;
import com.livajq.arcanetweaks.common.capability.parry.ParryData;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

public class ArcaneCapabilities {
    public static final Capability<BossMinionData> BOSS_MINION =
            CapabilityManager.get(new CapabilityToken<>() {});
    
    public static final Capability<FoodBonusesData> FOOD_BONUSES =
            CapabilityManager.get(new CapabilityToken<>() {});
    
    public static final Capability<ParryData> PARRY =
            CapabilityManager.get(new CapabilityToken<>() {});
}