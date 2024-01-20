package net.zhuruoling.tnca.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Difficulty;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.zhuruoling.tnca.settings.CarpetAdditionSetting;
import net.zhuruoling.tnca.spawn.SpawnRestrictionManager;
import net.zhuruoling.tnca.spawn.SpawnRestrictionModification;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SpawnUtil {

    /* TOO MUCH MIXIN
        AxolotlEntity::canSpawn
        WaterCreatureEntity::canSpawn
        DrownedEntity::canSpawn
        GuardianEntity::canSpawn
        TropicalFishEntity::canTropicalFishSpawn
        BatEntity::canSpawn
        HostileEntity::canSpawnIgnoreLightLevel
        HostileEntity::canSpawnInDark
        AnimalEntity::isValidNaturalSpawn
        EndermiteEntity::canSpawn
        MobEntity::canMobSpawn
        FrogEntity::canSpawn
        GhastEntity::canSpawn
        GlowSquidEntity::canSpawn
        GoatEntity::canSpawn
        HuskEntity::canSpawn
        MagmaCubeEntity::canMagmaCubeSpawn
        MooshroomEntity::canSpawn
        OcelotEntity::canSpawn
        ParrotEntity::canSpawn
        HoglinEntity::canSpawn
        PiglinEntity::canSpawn
        PatrolEntity::canSpawn
        PolarBearEntity::canSpawn
        RabbitEntity::canSpawn
        SilverfishEntity::canSpawn
        SlimeEntity::canSpawn
        StrayEntity::canSpawn
        StriderEntity::canSpawn
        TurtleEntity::canSpawn
        WolfEntity::canSpawn
        ZombifiedPiglinEntity::canSpawn
        FoxEntity::canSpawn
     */

    /*
    public enum SpawnReason {
*	NATURAL,
*	CHUNK_GENERATION,
*	SPAWNER,
*	STRUCTURE,
	BREEDING,
	MOB_SUMMONED,
	JOCKEY,
	EVENT,
	CONVERSION,
	REINFORCEMENT,
	TRIGGERED,
	BUCKET,
	SPAWN_EGG,
	COMMAND,
	DISPENSER,
	PATROL;
}

     */

    public static SpawnCheckResult checkCanSpawnIgnoreLight(EntityType<? extends Entity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        if (CarpetAdditionSetting.commandMobSpawn.equals("false"))
            return SpawnCheckResult.IGNORE;
        if (spawnReason == SpawnReason.NATURAL
                || spawnReason == SpawnReason.CHUNK_GENERATION
                || spawnReason == SpawnReason.STRUCTURE
                || spawnReason == SpawnReason.PATROL) {
            Identifier id = Util.getIdFromEntityType(type);
            if (!SpawnRestrictionManager.INSTANCE.contains(id)) return SpawnCheckResult.IGNORE;
            List<SpawnCheckResult> results = new ArrayList<>();
            doCheck(results, type, world, spawnReason, pos, random, id, true, false);
            if (results.contains(SpawnCheckResult.CANNOT_SPAWN)) return SpawnCheckResult.CANNOT_SPAWN;
            if (results.contains(SpawnCheckResult.CAN_SPAWN)) return SpawnCheckResult.CAN_SPAWN;
            return SpawnCheckResult.IGNORE;
        }
        return SpawnCheckResult.IGNORE;
    }

    public static SpawnCheckResult checkCanSpawn(EntityType<? extends Entity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        if (CarpetAdditionSetting.commandMobSpawn.equals("false"))
            return SpawnCheckResult.IGNORE;
        if (spawnReason == SpawnReason.NATURAL
                || spawnReason == SpawnReason.CHUNK_GENERATION
                || spawnReason == SpawnReason.STRUCTURE
                || spawnReason == SpawnReason.PATROL) {
            Identifier id = Util.getIdFromEntityType(type);
            if (!SpawnRestrictionManager.INSTANCE.contains(id)) return SpawnCheckResult.IGNORE;
            List<SpawnCheckResult> results = new ArrayList<>();
            //brightness
            doCheck(results, type, world, spawnReason, pos, random, id, false, false);
            if (results.contains(SpawnCheckResult.CANNOT_SPAWN)) return SpawnCheckResult.CANNOT_SPAWN;
            if (results.contains(SpawnCheckResult.CAN_SPAWN)) return SpawnCheckResult.CAN_SPAWN;
            return SpawnCheckResult.IGNORE;
        }
        return SpawnCheckResult.IGNORE;
    }

    private static void doCheck(
            List<SpawnCheckResult> results,
            EntityType<? extends Entity> type,
            WorldAccess world,
            SpawnReason spawnReason,
            BlockPos pos,
            Random random,
            Identifier id,
            boolean ignoreLight,
            boolean ignoreHeight
    ) {
        if(!ignoreLight) results.add(checkCanSpawnByBrightness(world, pos, random, id));
        if(!ignoreHeight)results.add(checkCanSpawnByHeight(pos, id));
    }

    private static SpawnCheckResult checkCanSpawnByHeight(BlockPos pos, Identifier id) {
        IntRange range = SpawnRestrictionManager.INSTANCE.getHeight(id);
        if (range == null) return SpawnCheckResult.IGNORE;
        return range.from <= pos.getY() && range.to >= pos.getY() ? SpawnCheckResult.CAN_SPAWN : SpawnCheckResult.CANNOT_SPAWN;
    }

    @NotNull
    private static SpawnCheckResult checkCanSpawnByBrightness(WorldAccess world, BlockPos pos, Random random, Identifier id) {
        IntRange range = SpawnRestrictionManager.INSTANCE.getBrightness(id);
        if (range == null)
            return SpawnCheckResult.IGNORE;
        if (world.getDifficulty() == Difficulty.PEACEFUL)
            return SpawnCheckResult.IGNORE;
        if (world.getLightLevel(LightType.SKY, pos) > random.nextInt(32)) {
            return SpawnCheckResult.CANNOT_SPAWN;
        }
        int level = world.getLightLevel(LightType.BLOCK, pos);
        boolean match = level >= range.from() && level <= range.to();
        return match ? SpawnCheckResult.CAN_SPAWN : SpawnCheckResult.CANNOT_SPAWN;
    }
}
