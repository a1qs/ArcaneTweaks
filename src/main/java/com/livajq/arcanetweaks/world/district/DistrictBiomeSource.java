package com.livajq.arcanetweaks.world.district;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

//I have no idea what I'm doing
public class DistrictBiomeSource extends BiomeSource {
    
    public static final Codec<DistrictBiomeSource> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    BiomeSource.CODEC.fieldOf("delegate").forGetter(src -> src.delegate),
                    Codec.LONG.fieldOf("seed").forGetter(src -> src.seed)
            ).apply(instance, DistrictBiomeSource::new)
    );
    
    private final BiomeSource delegate;
    private final long seed;
    
    private final Map<TagKey<Biome>, List<Holder<Biome>>> tagCache = new HashMap<>();
    
    public DistrictBiomeSource(BiomeSource delegate, long seed) {
        this.delegate = delegate;
        this.seed = seed;
    }
    
    @Override
    protected Codec<? extends BiomeSource> codec() {
        return CODEC;
    }

    @Override
    protected Stream<Holder<Biome>> collectPossibleBiomes() {
        return delegate.possibleBiomes().stream();
    }
    
    @Override
    public Holder<Biome> getNoiseBiome(int quartX, int quartY, int quartZ, Climate.Sampler sampler) {
        
        int x = quartX << 2;
        int z = quartZ << 2;
        
        Holder<Biome> original = delegate.getNoiseBiome(quartX, quartY, quartZ, sampler);
        
        if (original == null) return fallbackBiome();
        
        DistrictBand band = DistrictBand.fromZ(z);
        
        if (!isControlledBiome(original)) return original;
        if (original.is(band.tag())) return original;
        
        return pickBiomeForBand(band, x, z, original);
    }
    
    private Holder<Biome> fallbackBiome() {
        return delegate.possibleBiomes().stream().findFirst().orElseThrow();
    }
    
    private boolean isControlledBiome(Holder<Biome> biome) {
        if (biome == null) return false;
        for (DistrictBand band : DistrictBand.values()) {
            if (biome.is(band.tag())) return true;
        }
        return false;
    }
    
    private Holder<Biome> pickBiomeForBand(DistrictBand band, int x, int z, Holder<Biome> fallback) {
        
        List<Holder<Biome>> list = tagCache.computeIfAbsent(
                band.tag(),
                tag -> delegate.possibleBiomes()
                        .stream()
                        .filter(holder -> holder.is(tag))
                        .toList()
        );
        
        if (list.isEmpty()) return fallback;
        
        long biomeSeed = Mth.getSeed(x, 0, z);
        RandomSource rand = RandomSource.create(seed ^ biomeSeed);
        
        return list.get(rand.nextInt(list.size()));
    }
}