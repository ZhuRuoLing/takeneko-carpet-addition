package net.zhuruoling.tnca.settings;

import net.minecraft.command.CommandSource;
import net.minecraft.server.command.ServerCommandSource;

public interface SettingCallback {
    void accept(ServerCommandSource source);
}
