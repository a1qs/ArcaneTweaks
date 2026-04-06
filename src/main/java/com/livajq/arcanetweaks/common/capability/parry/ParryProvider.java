package com.livajq.arcanetweaks.common.capability.parry;

import com.livajq.arcanetweaks.common.capability.ArcaneCapabilities;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

public class ParryProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    
    private final ParryImp backend;
    private final LazyOptional<ParryData> optional;
    
    public ParryProvider(Player player) {
        backend = new ParryImp(player);
        optional = LazyOptional.of(() -> backend);
    }
    
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return cap == ArcaneCapabilities.PARRY ? optional.cast() : LazyOptional.empty();
    }
    
    @Override
    public CompoundTag serializeNBT() {
        return backend.serializeNBT();
    }
    
    @Override
    public void deserializeNBT(CompoundTag nbt) {
        backend.deserializeNBT(nbt);
    }
    
    public ParryImp getBackend() {
        return backend;
    }
}