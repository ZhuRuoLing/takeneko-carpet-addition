package net.zhuruoling.tnca.spawn;

import net.minecraft.util.Identifier;
import net.zhuruoling.tnca.util.IntRange;

public class SpawnRestrictionModification {

    private final Identifier identifier;
    private boolean canSpawn = true;
    private IntRange brightness = null;
    private IntRange height = null;

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

    public IntRange getHeight() {
        return height;
    }

    public void setHeight(IntRange height) {
        this.height = height;
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
