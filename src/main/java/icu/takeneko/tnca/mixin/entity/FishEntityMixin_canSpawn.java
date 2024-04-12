package icu.takeneko.tnca.mixin.entity;
//#if MC < 11800
//import icu.takeneko.tnca.spawn.SpawnUtil;
//import net.minecraft.entity.Entity;
//import net.minecraft.entity.EntityType;
//import net.minecraft.entity.SpawnReason;
//import net.minecraft.entity.passive.FishEntity;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.world.ServerWorldAccess;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//
//import java.util.Random;
//
//@Mixin(FishEntity.class)
//public class FishEntityMixin_canSpawn {
//
//    @Inject(method = "canSpawn(Lnet/minecraft/entity/EntityType;Lnet/minecraft/world/WorldAccess;Lnet/minecraft/entity/SpawnReason;Lnet/minecraft/util/math/BlockPos;Ljava/util/Random;)Z", at = @At("RETURN"), cancellable = true)
//    private static void inj(EntityType<? extends Entity> type, ServerWorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random, CallbackInfoReturnable<Boolean> cir) {
//        switch (SpawnUtil.checkCanSpawn(type, world, spawnReason, pos, random)){
//            case IGNORE -> {
//            }
//            case CAN_SPAWN -> {
//                cir.setReturnValue(true);
//                cir.cancel();
//            }
//            case CANNOT_SPAWN -> {
//                cir.setReturnValue(false);
//                cir.cancel();
//            }
//        }
//    }
//
//}
//#endif