package net.zhuruoling.tnca.mixin;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.zhuruoling.tnca.spawn.SpawnRestrictionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin{

    @Shadow private PlayerManager playerManager;

    @Inject(method = "save", at = @At("HEAD"))
    void inj(boolean suppressLogs, boolean flush, boolean force, CallbackInfoReturnable<Boolean> cir){
        SpawnRestrictionManager.INSTANCE.save(this.playerManager.getServer());
    }

    @Inject(method = "loadWorld", at = @At("HEAD"))
    void inj(CallbackInfo ci){
        SpawnRestrictionManager.INSTANCE.load(this.playerManager.getServer());
    }
}
