package net.zhuruoling.tnca.mixin.gen;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.GoatEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Difficulty;
import net.minecraft.world.LightType;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.WorldAccess;
import net.zhuruoling.tnca.settings.CarpetAdditionSetting;
import net.zhuruoling.tnca.spawn.SpawnRestrictionManager;
import net.zhuruoling.tnca.util.IntRange;
import net.zhuruoling.tnca.util.SpawnUtil;
import net.zhuruoling.tnca.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GoatEntity.class)
public class GoatEntityMixin_canSpawn {
    @Inject(method = "canSpawn", at = @At("HEAD"), cancellable = true)
    private static void inj(EntityType<? extends AnimalEntity> entityType, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random, CallbackInfoReturnable<Boolean> cir) {
        switch (SpawnUtil.checkCanSpawnByBrightness(entityType, world, spawnReason, pos, random)){
            case IGNORE -> {
            }
            case CAN_SPAWN -> {
                cir.setReturnValue(true);
                cir.cancel();
            }
            case CANNOT_SPAWN -> {
                cir.setReturnValue(false);
                cir.cancel();
            }
        }
    }
}
