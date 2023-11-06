package net.zhuruoling.tnca.command;

import net.minecraft.command.CommandSource;

public class CommandUtil {
    public static boolean canUseCommand(CommandSource source, Object commandLevel) {
        if (commandLevel instanceof Boolean) return (Boolean) commandLevel;
        String commandLevelString = commandLevel.toString();
        return switch (commandLevelString) {
            case "true" -> true;
            case "false" -> false;
            case "ops" -> source.hasPermissionLevel(2); // typical for other cheaty commands
            case "0", "1", "2", "3", "4" -> source.hasPermissionLevel(Integer.parseInt(commandLevelString));
            default -> false;
        };
    }
}
