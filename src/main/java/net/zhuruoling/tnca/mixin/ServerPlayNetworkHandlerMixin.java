package net.zhuruoling.tnca.mixin;


import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.impl.launch.FabricLauncher;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.zhuruoling.tnca.settings.CarpetAdditionSetting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.time.Instant;
import java.util.Objects;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin {
    @Unique
    private final static String methodName = FabricLoader.getInstance().getMappingResolver().mapMethodName("intermediary", "net.minecraft.class_3244", "method_44160","(Ljava/time/Instant;)Z");

    //#if MC >= 11900
    @Redirect(method = "validateMessage", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayNetworkHandler;isInProperOrder(Ljava/time/Instant;)Z"))
    public boolean redirect(ServerPlayNetworkHandler instance, Instant instant) throws Throwable {
        if (CarpetAdditionSetting.bypassMessageOrderCheck) {
            return true;
        } else {
            return (boolean) MethodHandles.lookup().findVirtual(ServerPlayNetworkHandler.class,
                    methodName,
                    MethodType.methodType(boolean.class, Instant.class)
            ).invoke(instance, instant);
        }
    }
    //#endif
}
