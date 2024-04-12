package icu.takeneko.tnca.command;

import carpet.CarpetSettings;
import carpet.patches.EntityPlayerMPFake;
import carpet.utils.Messenger;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import icu.takeneko.tnca.command.arguments.RegexArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import icu.takeneko.tnca.settings.CarpetAdditionSetting;
import net.minecraft.text.Text;

import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class KillFakePlayerCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("killFakePlayer").
                requires(src -> CommandUtil.canUseCommand(src, CarpetSettings.commandPlayer) && CommandUtil.canUseCommand(src, CarpetAdditionSetting.commandKillFakePlayer)).then(
                        literal("contains").
                                then(argument("string", StringArgumentType.string()).executes(context -> {
                                            acceptString(context.getSource(), context.getArgument("string", String.class));
                                            return 0;
                                        })
                                )
                ).then(
                        literal("matches").
                                then(argument("pattern", StringArgumentType.greedyString())
                                        .suggests(new RegexSuggestionProvider<>())
                                        .executes(context -> {
                                            try {
                                                acceptPattern(context.getSource(), Pattern.compile(context.getArgument("pattern", String.class)));
                                            }catch (PatternSyntaxException e){
                                                int index = e.getIndex();
                                                String errorMessage = String.format("w Invalid Regex pattern: %s%s: %s <--[HERE]",
                                                        e.getDescription(),
                                                        index >= 0 ? String.format(" near index %d ",e.getIndex()) : "",
                                                        e.getPattern());
                                                context.getSource().sendError(Messenger.s(errorMessage));
                                            }
                                            //acceptPattern(context.getSource(), Pattern.compile(context.getArgument("pattern", String.class)));
                                            return 0;
                                        })
                                )

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
            if (!bl) {
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
