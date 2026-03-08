package com.livajq.arcanetweaks.bossbehavior;

import com.github.alexthe666.iceandfire.entity.EntityIceDragon;
import com.livajq.arcanetweaks.Config;

import java.awt.*;

public class DragonIceBehavior extends DragonBehavior<EntityIceDragon> {
    private static final Color ICE_EXPLOSION_COLOR = new Color(Config.dragonNukeColorIce);
    
    public DragonIceBehavior() {
        super(2);
    }
    
    @Override
    public Color getExplosionColor() {
        return ICE_EXPLOSION_COLOR;
    }
}