package icu.takeneko.tnca.mixin;

import com.mojang.brigadier.arguments.ArgumentType;
import icu.takeneko.tnca.command.arguments.RegexArgumentType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.command.argument.ArgumentTypes;
import net.minecraft.command.argument.serialize.ArgumentSerializer;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;
import net.minecraft.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ArgumentTypes.class)
public abstract class ArgumentTypesMixin {
    //#if MC > 11900
    @Shadow
    private static <A extends ArgumentType<?>, T extends ArgumentSerializer.ArgumentTypeProperties<A>> ArgumentSerializer<A, T> register(Registry<ArgumentSerializer<?, ?>> registry, String id, Class<? extends A> clazz, ArgumentSerializer<A, T> serializer) {
        return null;
    }

    @Environment(EnvType.SERVER)
    @Inject(method = "register(Lnet/minecraft/registry/Registry;)Lnet/minecraft/command/argument/serialize/ArgumentSerializer;", at = @At("HEAD"))
    private static void register(Registry<ArgumentSerializer<?, ?>> registry, CallbackInfoReturnable<ArgumentSerializer<?, ?>> cir) {
        register(registry, "regex", RegexArgumentType.class, ConstantArgumentSerializer.of(RegexArgumentType::new));
        //register(registry, "brightness", IntRangeArgumentType.class, ConstantArgumentSerializer.of(IntRangeArgumentType::brightnessRange));
    }


    @Environment(EnvType.CLIENT)
    @Inject(method = "get", at = @At("HEAD"), cancellable = true)
    private static <A extends ArgumentType<?>> void handleGet(A argumentType, CallbackInfoReturnable<ArgumentSerializer<A, ?>> cir) {
        if (argumentType instanceof RegexArgumentType){
            cir.setReturnValue((ArgumentSerializer<A, ?>) ConstantArgumentSerializer.of(RegexArgumentType::new));
            cir.cancel();
        }
    }

    //#endif
}