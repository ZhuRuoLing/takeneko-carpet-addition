package icu.takeneko.tnca.mixin;

import icu.takeneko.tnca.spawn.SpawnRestrictionManager;
import icu.takeneko.tnca.spawn.SpawnUtil;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import icu.takeneko.tnca.settings.CarpetAdditionSetting;
import icu.takeneko.tnca.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SpawnHelper.class)
public class SpawnHelperMixin {
    @Inject(
            method = "canSpawn(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/SpawnGroup;Lnet/minecraft/world/gen/StructureAccessor;Lnet/minecraft/world/gen/chunk/ChunkGenerator;Lnet/minecraft/world/biome/SpawnSettings$SpawnEntry;Lnet/minecraft/util/math/BlockPos$Mutable;D)Z",
            at = @At("RETURN"),
            cancellable = true
    )
    private static void mixinCanSpawn(ServerWorld world, SpawnGroup group, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, SpawnSettings.SpawnEntry spawnEntry, BlockPos.Mutable pos, double squaredDistance, CallbackInfoReturnable<Boolean> cir) {
        if (CarpetAdditionSetting.commandMobSpawn.equals("false"))return;
        var entityIdentifier = Util.getIdFromEntityType(spawnEntry.type);
        if (!SpawnRestrictionManager.INSTANCE.canSpawn(entityIdentifier)){
            cir.setReturnValue(false);
            cir.cancel();
        }
        SpawnUtil.logCanSpawn(cir.getReturnValue(), group, entityIdentifier, pos, world, world.getServer());
    }
}
