package icu.takeneko.tnca.settings;

import net.minecraft.server.command.ServerCommandSource;

public class CommandOptionChangedCallback implements SettingCallback{
    @Override
    public void accept(ServerCommandSource source) {
        var server = source.getServer();
        var playerManager = server.getPlayerManager();
        playerManager.getPlayerList().forEach(playerManager::sendCommandTree);
    }
}
