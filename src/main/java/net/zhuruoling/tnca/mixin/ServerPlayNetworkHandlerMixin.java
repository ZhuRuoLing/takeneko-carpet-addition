package net.zhuruoling.tnca.mixin;


import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.zhuruoling.tnca.settings.CarpetAdditionSetting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.time.Instant;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin {
    //#if MC >= 11900
    @Redirect(method = "validateMessage", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayNetworkHandler;isInProperOrder(Ljava/time/Instant;)Z"))
    public boolean redirect(ServerPlayNetworkHandler instance, Instant instant) throws Throwable {
        if (CarpetAdditionSetting.bypassMessageOrderCheck) {
            return true;
        } else {
            //INVOKEVIRTUAL net/minecraft/server/network/ServerPlayNetworkHandler.isInProperOrder (Ljava/time/Instant;)Z
            return (boolean) MethodHandles.lookup().findVirtual(ServerPlayNetworkHandler.class,
                    "isInProperOrder",
                    MethodType.methodType(boolean.class, Instant.class)
                    ).invoke(instance, instant);
        }
    }
    //#endif
}
