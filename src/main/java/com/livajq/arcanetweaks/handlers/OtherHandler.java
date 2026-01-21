package com.livajq.arcanetweaks.handlers;

import com.livajq.arcanetweaks.ArcaneTweaks;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ArcaneTweaks.MODID)
public class OtherHandler {
    
    @SubscribeEvent
    public static void onTagsUpdated(TagsUpdatedEvent event) {
        //rip
    }
}