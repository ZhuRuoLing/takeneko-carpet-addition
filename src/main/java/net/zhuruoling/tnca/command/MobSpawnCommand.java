package net.zhuruoling.tnca.command;

import carpet.utils.Messenger;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.argument.EntitySummonArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Identifier;
import net.zhuruoling.tnca.settings.CarpetAdditionSetting;
import net.zhuruoling.tnca.spawn.SpawnRestricionManager;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class MobSpawnCommand {
    private static final LiteralArgumentBuilder<ServerCommandSource> canSpawn =
            literal("canSpawn").then(literal("get").
                    executes(ctx -> {
                        acceptCanSpawnModification(ctx.getSource(), false, EntitySummonArgumentType.getEntitySummon(ctx, "mobType"), true);
                        return 1;
                    })
            ).then(literal("set").
                    then(argument("value", BoolArgumentType.bool()).
                            executes(ctx -> {
                                acceptCanSpawnModification(ctx.getSource(),
                                        true,
                                        EntitySummonArgumentType.getEntitySummon(ctx, "mobType"),
                                        BoolArgumentType.getBool(ctx, "value"));
                                return 1;
                            })
                    )
            );

    private static final LiteralArgumentBuilder<ServerCommandSource> command =
            literal("mobSpawn").requires(src -> CarpetAdditionSetting.commandMobSpawn && src.hasPermissionLevel(2)).
                    then(
                            argument("mobType", EntitySummonArgumentType.entitySummon()).
                                    then(canSpawn)
                    );


    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(command);
    }

    private static void acceptCanSpawnModification(ServerCommandSource src, boolean modify, Identifier mobIdentifier, boolean canSpawn) {
        if (!modify) {
            if (!SpawnRestricionManager.INSTANCE.contains(mobIdentifier)) {
                Messenger.m(src, "Entity %s has no modified spawn conditions.");
                return;
            }
            Messenger.m(src,
                    "%s Entity %s %s".formatted(
                            SpawnRestricionManager.INSTANCE.canSpawn(mobIdentifier) ? "l" : "y",
                            mobIdentifier.toString(),
                            SpawnRestricionManager.INSTANCE.canSpawn(mobIdentifier) ? "is allowed to spawn." : "cannot spawn."
                    )
            );
            return;
        }
        SpawnRestricionManager.INSTANCE.setCanSpawn(mobIdentifier, canSpawn);
        if (!canSpawn){
            Messenger.m(src, "rb Disallowed", "w %s from spawn.".formatted(mobIdentifier.toString()));
            return;
        }
        Messenger.m(src, "lb Allowed", "w %s to spawn.".formatted(mobIdentifier.toString()));
    }
}
