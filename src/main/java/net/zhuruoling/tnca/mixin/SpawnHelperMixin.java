package net.zhuruoling.tnca.mixin;

import net.minecraft.entity.SpawnGroup;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.zhuruoling.tnca.settings.CarpetAdditionSetting;
import net.zhuruoling.tnca.spawn.SpawnRestricionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SpawnHelper.class)
public class SpawnHelperMixin {
    @Inject(method = "canSpawn(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/SpawnGroup;Lnet/minecraft/world/gen/StructureAccessor;Lnet/minecraft/world/gen/chunk/ChunkGenerator;Lnet/minecraft/world/biome/SpawnSettings$SpawnEntry;Lnet/minecraft/util/math/BlockPos$Mutable;D)Z",
            at = @At("HEAD"),
            cancellable = true)
    static void mixinCanSpawn(ServerWorld world, SpawnGroup group, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, SpawnSettings.SpawnEntry spawnEntry, BlockPos.Mutable pos, double squaredDistance, CallbackInfoReturnable<Boolean> cir) {
        if (!CarpetAdditionSetting.commandMobSpawn)return;
        var entityIdentifier = Registry.ENTITY_TYPE.getId(spawnEntry.type);
        if (!SpawnRestricionManager.INSTANCE.canSpawn(entityIdentifier)){
            cir.setReturnValue(false);
            cir.cancel();
            return;
        }
    }
}
