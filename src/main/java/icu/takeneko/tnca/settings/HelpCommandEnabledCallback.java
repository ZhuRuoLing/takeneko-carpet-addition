package icu.takeneko.tnca.settings;

import net.minecraft.server.command.HelpCommand;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

public class HelpCommandEnabledCallback implements SettingCallback{
    @Override
    public void accept(ServerCommandSource source) {
        var dispatcher = source.getServer().getCommandManager().getDispatcher();
        HelpCommand.register(dispatcher);
        for (ServerPlayerEntity player : source.getServer().getPlayerManager().getPlayerList()) {
            source.getServer().getCommandManager().sendCommandTree(player);
        }
    }
}
