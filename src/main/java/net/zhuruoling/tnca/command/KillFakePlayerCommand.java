package net.zhuruoling.tnca.command;

import carpet.CarpetSettings;
import carpet.patches.EntityPlayerMPFake;
import carpet.settings.SettingsManager;
import carpet.utils.Messenger;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.zhuruoling.tnca.command.arguments.RegexArgumentType;
import net.zhuruoling.tnca.settings.CarpetAdditionSetting;

import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class KillFakePlayerCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("killFakePlayer").
                requires(src -> CommandUtil.canUseCommand(src, CarpetSettings.commandPlayer) && CarpetAdditionSetting.commandKillFakePlayer).then(
                        literal("contains").
                                then(argument("string", StringArgumentType.string()).executes(context -> {
                                            acceptString(context.getSource(), context.getArgument("string", String.class));
                                            return 0;
                                        })
                                )
                ).then(
                        //#if MC > 11940
//$$                        literal("matches").then(argument("pattern", StringArgumentType.greedyString()).executes(context -> {
//$$                                    try {
//$$                                        Pattern pattern = Pattern.compile(context.getArgument("pattern", String.class));
//$$                                        acceptPattern(context.getSource(), pattern);
//$$                                    } catch (java.util.regex.PatternSyntaxException e) {
//$$                                        for (String s : e.getMessage().split(System.lineSeparator())) {
//$$                                            Messenger.m(context.getSource(), "r " + s);
//$$                                        }
//$$                                    }
//$$                                    return 0;
//$$                                })
//$$                        )
                        //#else
                        literal("matches").
                                then(argument("pattern", new RegexArgumentType()).executes(context -> {
                                            acceptPattern(context.getSource(), context.getArgument("pattern", Pattern.class));
                                            return 0;
                                        })
                                )
                        //#endif
                )
        );
    }

    private static void acceptString(ServerCommandSource source, String string) {
        killPlayers(source, filterPlayers(source, p -> p.getGameProfile().getName().contains(string)));
    }

    private static void acceptPattern(ServerCommandSource source, Pattern pattern) {
        killPlayers(source, filterPlayers(source, p -> pattern.matcher(p.getGameProfile().getName()).matches()));
    }

    private static void killPlayers(ServerCommandSource source, List<ServerPlayerEntity> playerEntities) {
        var players = playerEntities.stream().filter(serverPlayerEntity -> {
            boolean bl = serverPlayerEntity instanceof EntityPlayerMPFake;
            if (!bl){
                Messenger.m(source, "y Player %s is not a fake player.".formatted(serverPlayerEntity.getGameProfile().getName()));
            }
            return bl;
        }).toList();
        for (ServerPlayerEntity player : players) {
            player.kill();
        }
        Messenger.m(source, "l Killed %d fake players.".formatted(players.size()));
    }

    private static List<ServerPlayerEntity> filterPlayers(ServerCommandSource source, Predicate<PlayerEntity> filter) {
        return source.getServer().getPlayerManager().getPlayerList().stream().filter(filter).toList();
    }
}
