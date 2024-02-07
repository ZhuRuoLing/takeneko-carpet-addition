package icu.takeneko.tnca.command;

import net.minecraft.command.CommandSource;

public class CommandUtil {
    public static boolean canUseCommand(CommandSource source, Object commandLevel) {
        if (commandLevel instanceof Boolean) return (Boolean) commandLevel;
        String commandLevelString = commandLevel.toString();
        return switch (commandLevelString) {
            case "true" -> true;
            case "ops" -> source.hasPermissionLevel(2);
            case "3", "1", "2", "4", "0" -> source.hasPermissionLevel(Integer.parseInt(commandLevelString));
            default -> false;
        };
    }
}
