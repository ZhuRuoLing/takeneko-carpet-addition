package icu.takeneko.tnca.settings;

import net.minecraft.command.CommandSource;
import net.minecraft.server.command.ServerCommandSource;

@FunctionalInterface
public interface SettingCallback {
    void accept(ServerCommandSource source);
}
