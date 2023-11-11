package net.zhuruoling.tnca.command;

import carpet.utils.Messenger;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.datafixers.util.Function4;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Identifier;
import net.zhuruoling.tnca.settings.CarpetAdditionSetting;
import net.zhuruoling.tnca.spawn.SpawnRestricionManager;

import java.util.function.Supplier;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class MobSpawnCommand {
    private static final LiteralArgumentBuilder<ServerCommandSource> canSpawn =
            buildMobSpawnRuleCommand("canSpawn", BoolArgumentType::bool, MobSpawnCommand::acceptCanSpawnModification);


    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess access) {
        dispatcher.register(literal("mobSpawn").requires(src -> CarpetAdditionSetting.commandMobSpawn && src.hasPermissionLevel(2)).
                then(
                        //#if MC > 11900
                        //$$ argument("mobType", net.minecraft.command.argument.RegistryEntryArgumentType.registryEntry(access, net.minecraft.registry.RegistryKeys.ENTITY_TYPE))
                        //#else
                        argument("mobType", net.minecraft.command.argument.EntitySummonArgumentType.entitySummon())
                        //#endif
                                .then(canSpawn)
                ));
    }

    private static int acceptCanSpawnModification(ServerCommandSource src, boolean modify, Identifier mobIdentifier, boolean canSpawn) {
        if (!modify) {
            if (!SpawnRestricionManager.INSTANCE.contains(mobIdentifier)) {
                Messenger.m(src, "Entity %s has no modified spawn conditions.");
                return 1;
            }
            Messenger.m(src,
                    "%s Entity %s %s".formatted(
                            SpawnRestricionManager.INSTANCE.canSpawn(mobIdentifier) ? "l" : "y",
                            mobIdentifier.toString(),
                            SpawnRestricionManager.INSTANCE.canSpawn(mobIdentifier) ? "is allowed to spawn." : "cannot spawn."
                    )
            );
            return 1;
        }
        SpawnRestricionManager.INSTANCE.setCanSpawn(mobIdentifier, canSpawn);
        if (!canSpawn) {
            Messenger.m(src, "rb Disallowed", "w %s from spawn.".formatted(mobIdentifier.toString()));
            return 0;
        }
        Messenger.m(src, "lb Allowed", "w %s to spawn.".formatted(mobIdentifier.toString()));
        return 0;
    }

    private static Identifier getEntityArgument(CommandContext<ServerCommandSource> context) {
        return context.getArgument("mobType", Identifier.class);
    }


    private static <T> LiteralArgumentBuilder<ServerCommandSource> buildMobSpawnRuleCommand(
            String rule,
            Supplier<ArgumentType<T>> argumentTypeSupplier,
            Function4<ServerCommandSource, Boolean, Identifier, T, Integer> handler
    ) {
        return literal(rule).then(literal("get").
                executes(ctx -> {
                    handler.apply(ctx.getSource(), false, getEntityArgument(ctx), (T) ctx.getArgument("value", Object.class));
                    return 1;
                })
        ).then(literal("set").
                then(argument("value", argumentTypeSupplier.get()).
                        executes(ctx -> {
                            handler.apply(ctx.getSource(),
                                    true,
                                    getEntityArgument(ctx),
                                    (T) ctx.getArgument("value", Object.class));
                            return 1;
                        })
                )
        );
    }


}
