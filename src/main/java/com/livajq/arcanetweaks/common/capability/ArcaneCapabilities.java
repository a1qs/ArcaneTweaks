package com.livajq.arcanetweaks.common.capability;

import com.livajq.arcanetweaks.common.capability.bossminion.BossMinionData;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

public class ArcaneCapabilities {
    public static final Capability<BossMinionData> BOSS_MINION =
            CapabilityManager.get(new CapabilityToken<>() {});
}
