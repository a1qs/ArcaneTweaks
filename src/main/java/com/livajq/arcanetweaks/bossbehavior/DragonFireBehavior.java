package com.livajq.arcanetweaks.bossbehavior;

import com.github.alexthe666.iceandfire.entity.EntityFireDragon;
import com.livajq.arcanetweaks.Config;

import java.awt.*;

public class DragonFireBehavior extends DragonBehavior<EntityFireDragon> {
    private static final Color FIRE_EXPLOSION_COLOR = new Color(Config.dragonNukeColorFire);
    
    
    public DragonFireBehavior() {
        super(2);
    }
    
    @Override
    public Color getExplosionColor() {
        return FIRE_EXPLOSION_COLOR;
    }
}
