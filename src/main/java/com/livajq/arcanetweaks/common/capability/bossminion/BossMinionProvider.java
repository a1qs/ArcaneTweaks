package com.livajq.arcanetweaks.common.capability.bossminion;

import com.livajq.arcanetweaks.common.capability.ArcaneCapabilities;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

public class BossMinionProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    
    private final BossMinionImp backend = new BossMinionImp();
    private final LazyOptional<BossMinionData> optional = LazyOptional.of(() -> backend);
    
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return cap == ArcaneCapabilities.BOSS_MINION ? optional.cast() : LazyOptional.empty();
    }
    
    @Override
    public CompoundTag serializeNBT() {
        return backend.serializeNBT();
    }
    
    @Override
    public void deserializeNBT(CompoundTag nbt) {
        backend.deserializeNBT(nbt);
    }
    
    public BossMinionImp getBackend() {
        return backend;
    }
}