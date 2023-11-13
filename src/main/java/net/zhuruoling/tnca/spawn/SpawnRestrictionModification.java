package net.zhuruoling.tnca.spawn;

import net.minecraft.util.Identifier;
import net.zhuruoling.tnca.util.IntRange;

public class SpawnRestrictionModification {

    private Identifier identifier;
    private boolean canSpawn = true;

    private IntRange brightness = null;

    public SpawnRestrictionModification(Identifier identifier) {
        this.identifier = identifier;
    }

    public boolean canSpawn() {
        return canSpawn;
    }

    public void setCanSpawn(boolean canSpawn) {
        this.canSpawn = canSpawn;
    }

    public IntRange getBrightness() {
        return brightness;
    }

    public void setBrightness(IntRange brightness) {
        this.brightness = brightness;
    }

    @Override
    public String toString() {
        return "SpawnRestrictionModification{" +
                "identifier=" + identifier +
                ", canSpawn=" + canSpawn +
                ", brightness=" + brightness +
                '}';
    }
}
