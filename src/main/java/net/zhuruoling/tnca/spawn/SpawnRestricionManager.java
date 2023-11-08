package net.zhuruoling.tnca.spawn;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

import java.util.HashMap;
import java.util.Map;

public class SpawnRestricionManager {
    public final static SpawnRestricionManager INSTANCE = new SpawnRestricionManager();

    HashMap<Identifier, SpawnRestrictionModification> map = new HashMap<>();

    public void addEmpty(Identifier identifier){
        map.put(identifier, new SpawnRestrictionModification(identifier));
    }

    public boolean contains(Identifier identifier){
        return map.containsKey(identifier);
    }

    public SpawnRestrictionModification getModification(Identifier identifier){
        return map.get(identifier);
    }

    public boolean canSpawn(Identifier identifier){
        return !contains(identifier) || getModification(identifier).canSpawn();
    }

    public void setCanSpawn(Identifier identifier, boolean canSpawn){
        if (!contains(identifier))addEmpty(identifier);
        getModification(identifier).setCanSpawn(canSpawn);
    }

}
