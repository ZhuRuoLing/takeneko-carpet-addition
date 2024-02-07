package icu.takeneko.tnca.settings;

public class SettingCallbacks {
    public static SettingCallback COMMAND_OPTION_CHANGED = new CommandOptionChangedCallback();

    public static SettingCallback HELP_COMMAND_ENABLED = new HelpCommandEnabledCallback();
    public static SettingCallback HELP_COMMAND_DISABLED = new HelpCommandDisabledCallback();
}
