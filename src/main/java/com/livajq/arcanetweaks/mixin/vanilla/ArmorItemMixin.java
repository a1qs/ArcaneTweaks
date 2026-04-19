package com.livajq.arcanetweaks.mixin.vanilla;

import com.Harbinger.Spore.Sitems.BaseWeapons.SporeArmorData;
import net.minecraft.world.item.ArmorItem;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ArmorItem.class)
public abstract class ArmorItemMixin implements SporeArmorData {}