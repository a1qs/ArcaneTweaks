package com.livajq.arcanetweaks.util;

import com.livajq.arcanetweaks.Config;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Set;

public class BetterEndUtils {
    
    public static boolean isProperSurfaceOrTag(Block plant, BlockState state, TagKey<Block> tag) {
        if (isProperSurface(plant, state)) return true;
        return state.is(tag);
    }
    
    public static boolean isProperSurfaceOrBlock(Block plant, BlockState state, Block block) {
        if (isProperSurface(plant, state)) return true;
        return state.is(block);
    }
    
    private static boolean isProperSurface(Block plant, BlockState state) {
        ResourceLocation plantId = ForgeRegistries.BLOCKS.getKey(plant);
        if (plantId == null) return false;
        
        Set<ResourceLocation> allowed = Config.extraPlantSurfaces.get(plantId);
        if (allowed == null) return false;
        
        ResourceLocation blockId = ForgeRegistries.BLOCKS.getKey(state.getBlock());
        if (blockId == null) return false;
        
        return allowed.contains(blockId);
    }
}

