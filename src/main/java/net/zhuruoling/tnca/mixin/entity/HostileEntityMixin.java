package net.zhuruoling.tnca.mixin.entity;

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
import net.zhuruoling.tnca.util.IntRange;
import net.zhuruoling.tnca.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HostileEntity.class)
public class HostileEntityMixin {
    @Inject(method = "canSpawnInDark", at = @At("HEAD"), cancellable = true)
    private static void inj(EntityType<? extends HostileEntity> type, ServerWorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random, CallbackInfoReturnable<Boolean> cir) {
        if (CarpetAdditionSetting.commandMobSpawn.equals("false"))
            return;
        Identifier id = Util.getIdFromEntityType(type);
        IntRange range = SpawnRestrictionManager.INSTANCE.getBrightness(id);
        if (!SpawnRestrictionManager.INSTANCE.contains(id) || range == null)
            return;
        if (world.getDifficulty() == Difficulty.PEACEFUL)
            return;
        if (world.getLightLevel(LightType.SKY, pos) > random.nextInt(32)) {
            cir.setReturnValue(false);
            cir.cancel();
            return;
        }
        int level = world.getLightLevel(LightType.BLOCK, pos);
        boolean match = level >= range.from() && level <= range.to();
        cir.setReturnValue(match);
        cir.cancel();
    }
}
