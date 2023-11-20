package net.zhuruoling.tnca.util;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Difficulty;
import net.minecraft.world.LightType;
import net.minecraft.world.ServerWorldAccess;
import net.zhuruoling.tnca.settings.CarpetAdditionSetting;
import net.zhuruoling.tnca.spawn.SpawnRestrictionManager;

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

    public static SpawnCheckResult checkCanSpawnByBrightness(EntityType<? extends HostileEntity> type, ServerWorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random){
        if (CarpetAdditionSetting.commandMobSpawn.equals("false"))
            return SpawnCheckResult.IGNORE;
        Identifier id = Util.getIdFromEntityType(type);
        IntRange range = SpawnRestrictionManager.INSTANCE.getBrightness(id);
        if (!SpawnRestrictionManager.INSTANCE.contains(id) || range == null)
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
