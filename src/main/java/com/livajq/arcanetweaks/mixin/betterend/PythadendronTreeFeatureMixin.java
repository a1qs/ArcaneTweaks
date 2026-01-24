package com.livajq.arcanetweaks.mixin.betterend;

import com.livajq.arcanetweaks.Config;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import org.betterx.betterend.registry.EndBlocks;
import org.betterx.betterend.world.features.trees.PythadendronTreeFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Set;

@Mixin(PythadendronTreeFeature.class)
public abstract class PythadendronTreeFeatureMixin {
    
    @Redirect(
            method = "place",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/tags/TagKey;)Z"
            )
    )
    private boolean arcane$allowMoreGrounds(BlockState state, TagKey<Block> tag) {
        ResourceLocation plantId = ForgeRegistries.BLOCKS.getKey(EndBlocks.PYTHADENDRON_SAPLING);
        Set<ResourceLocation> allowed = Config.extraPlantSurfaces.get(plantId);
        
        if (allowed != null) {
            ResourceLocation blockId = ForgeRegistries.BLOCKS.getKey(state.getBlock());
            
            if (blockId != null && allowed.contains(blockId)) {
                return true;
            }
        }
        
        return state.is(tag);
    }
}