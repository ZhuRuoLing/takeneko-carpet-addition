package net.zhuruoling.tnca.command;

import carpet.utils.Messenger;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.datafixers.util.Function4;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.suggestion.SuggestionProviders;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Identifier;
import net.zhuruoling.tnca.command.arguments.IntRangeArgumentType;
import net.zhuruoling.tnca.settings.CarpetAdditionSetting;
import net.zhuruoling.tnca.spawn.SpawnRestrictionManager;
import net.zhuruoling.tnca.util.IntRange;

import java.util.function.Supplier;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class MobSpawnCommand {
    private static final LiteralArgumentBuilder<ServerCommandSource> canSpawn =
            buildMobSpawnRuleCommand("canSpawn", BoolArgumentType::bool, MobSpawnCommand::acceptCanSpawnModification, () -> true);

    private static final LiteralArgumentBuilder<ServerCommandSource> brightness =
            buildMobSpawnRuleCommand("brightness", IntRangeArgumentType::brightnessRange, MobSpawnCommand::acceptBrightnessModification, () -> new IntRange(0, 15));

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess access) {
        dispatcher.register(literal("mobSpawn").requires(src -> CommandUtil.canUseCommand(src, CarpetAdditionSetting.commandMobSpawn)).
                then(
                        //#if MC > 11900
                        argument("mobType", net.minecraft.command.argument.RegistryEntryArgumentType.registryEntry(access, net.minecraft.registry.RegistryKeys.ENTITY_TYPE))

                                //#else
                                //$$ argument("mobType", net.minecraft.command.argument.EntitySummonArgumentType.entitySummon())
                                //#endif
                                .suggests(SuggestionProviders.SUMMONABLE_ENTITIES)
                                .then(literal("reset").executes(src -> {
                                    var id = getEntityArgument(src);
                                    if (!SpawnRestrictionManager.INSTANCE.contains(id)) {
                                        Messenger.m(src.getSource(), "y %s has no modified spawn conditions.".formatted(id.toString()));
                                        return 1;
                                    }
                                    SpawnRestrictionManager.INSTANCE.clear(id);
                                    Messenger.m(src.getSource(), "w Removed all modified spawn conditions of %s.".formatted(id.toString()));
                                    return 0;
                                }))
                                .then(canSpawn)
                                .then(brightness)
                ));
    }

    private static int acceptBrightnessModification(ServerCommandSource src, boolean modify, Identifier id, IntRange range) {
        if (!modify) {
            if (!SpawnRestrictionManager.INSTANCE.contains(id) || SpawnRestrictionManager.INSTANCE.getBrightness(id) == null) {
                Messenger.m(src, "y %s has no modified brightness spawn conditions.".formatted(id.toString()));
                return 1;
            }
            var value = SpawnRestrictionManager.INSTANCE.getBrightness(id);
            Messenger.m(src, "w Set spawn brightness range of mob %s : ".formatted(id.toString()), "l " + value.toString());
            return 1;
        }
        SpawnRestrictionManager.INSTANCE.setBrightness(id, range);
        Messenger.m(src, "w Set spawn brightness range of mob %s : ".formatted(id.toString()), "l " + range.toString());
        return 0;
    }

    private static int acceptCanSpawnModification(ServerCommandSource src, boolean modify, Identifier mobIdentifier, boolean canSpawn) {
        if (!modify) {
            if (!SpawnRestrictionManager.INSTANCE.contains(mobIdentifier)) {
                Messenger.m(src, "y %s has no modified spawn conditions.".formatted(mobIdentifier.toString()));
                return 1;
            }
            Messenger.m(src,
                    "%s Entity %s %s".formatted(
                            SpawnRestrictionManager.INSTANCE.canSpawn(mobIdentifier) ? "l" : "y",
                            mobIdentifier.toString(),
                            SpawnRestrictionManager.INSTANCE.canSpawn(mobIdentifier) ? "is allowed to spawn." : "cannot spawn."
                    )
            );
            return 1;
        }
        SpawnRestrictionManager.INSTANCE.setCanSpawn(mobIdentifier, canSpawn);
        if (!canSpawn) {
            Messenger.m(src, "rb Disallowed ", "w %s from spawn.".formatted(mobIdentifier.toString()));
            return 0;
        }
        Messenger.m(src, "lb Allowed ", "w %s to spawn.".formatted(mobIdentifier.toString()));
        return 0;
    }

    private static Identifier getEntityArgument(CommandContext<ServerCommandSource> context) {
        //#if MC < 11900
        //$$ return context.getArgument("mobType", Identifier.class);
        //#else
        return context.getArgument("mobType", RegistryEntry.Reference.class).registryKey().getValue();
        //#endif
    }


    private static <T> LiteralArgumentBuilder<ServerCommandSource> buildMobSpawnRuleCommand(
            String rule,
            Supplier<ArgumentType<T>> argumentTypeSupplier,
            Function4<ServerCommandSource, Boolean, Identifier, T, Integer> handler,
            Supplier<T> defaultValueSupplier
    ) {
        return literal(rule).then(literal("get").
                executes(ctx -> {
                    try {
                        handler.apply(ctx.getSource(), false, getEntityArgument(ctx), defaultValueSupplier.get());
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
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
