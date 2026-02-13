package com.livajq.arcanetweaks.world.district;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public enum DistrictBand {
    VERY_COLD("very_cold_district", -10000, -5000),
    COLD("cold_district", -5000, -1000),
    TEMPERATE("temperate_district", -1000, 1000),
    HOT("hot_district", 1000, 5000),
    VERY_HOT("very_hot_district", 5000, 10000),
    UNHOLY("unholy_district", 10000, 12000),
    WASTELAND("wasteland_district", 12000, 15000);
    
    private final TagKey<Biome> tag;
    private final int minZ;
    private final int maxZ;
    
    DistrictBand(String tagName, int minZ, int maxZ) {
        this.tag = TagKey.create(Registries.BIOME, new ResourceLocation("arcane", tagName));
        this.minZ = minZ;
        this.maxZ = maxZ;
    }
    
    public TagKey<Biome> tag() {
        return tag;
    }
    
    public boolean containsZ(int z) {
        return z >= minZ && z < maxZ;
    }
    
    public static DistrictBand fromZ(int z) {
        for (DistrictBand band : values()) {
            if (band.containsZ(z)) return band;
        }

        return TEMPERATE;
    }
}