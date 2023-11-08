package net.zhuruoling.tnca.spawn;

import net.minecraft.util.Identifier;

public class SpawnRestrictionModification {

    Identifier identifier;
    boolean canSpawn = true;

    public SpawnRestrictionModification(Identifier identifier) {
        this.identifier = identifier;
    }

    public boolean canSpawn() {
        return canSpawn;
    }

    public void setCanSpawn(boolean canSpawn) {
        this.canSpawn = canSpawn;
    }
}
