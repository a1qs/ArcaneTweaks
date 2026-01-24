package com.livajq.arcanetweaks.mixin.vanilla;

import com.livajq.arcanetweaks.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class BlockStateBaseMixin {
    
    @Inject(method = "canSurvive", at = @At("HEAD"), cancellable = true)
    private void arcane$extraPlantSurfaces(LevelReader level, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        
        BlockState state = (BlockState)(Object)this;
        Block plant = state.getBlock();
        
        ResourceLocation plantId = ForgeRegistries.BLOCKS.getKey(plant);
        if (plantId == null) return;
        
        Set<ResourceLocation> allowed = Config.extraPlantSurfaces.get(plantId);
        if (allowed == null) return;
        
        BlockState ground = level.getBlockState(pos.below());
        ResourceLocation groundId = ForgeRegistries.BLOCKS.getKey(ground.getBlock());
        if (groundId == null) return;
        
        if (allowed.contains(groundId)) {
            cir.setReturnValue(true);
        }
    }
}