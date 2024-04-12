package icu.takeneko.tnca;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import com.mojang.brigadier.CommandDispatcher;
import icu.takeneko.tnca.command.KillFakePlayerCommand;
import icu.takeneko.tnca.command.MobSpawnCommand;
import icu.takeneko.tnca.compat.log.Log;
import icu.takeneko.tnca.compat.log.SimpleLogService;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import icu.takeneko.tnca.lang.LanguageProvider;
import icu.takeneko.tnca.settings.CarpetAdditionSetting;
import icu.takeneko.tnca.settings.SettingCallbacks;

import java.util.Map;

public class CarpetAdditionMain implements CarpetExtension, ModInitializer {

    public static final SimpleLogService logger = Log.getLogService();

    @Override
    public void onInitialize() {
        CarpetServer.manageExtension(new CarpetAdditionMain());
        //#if MC >= 11900
        carpet.api.settings.SettingsManager.registerGlobalRuleObserver(((src, parsedRule, s) -> {
            if (parsedRule.type() != CarpetAdditionSetting.class) return;
            onRulesChanged(parsedRule.name(), src, s);
        }));
        //#else
//$$        carpet.settings.SettingsManager.addGlobalRuleObserver(((src, parsedRule, s) -> {
//$$            if (parsedRule.type != CarpetAdditionSetting.class) return;
//$$            onRulesChanged(parsedRule.name, src, s);
//$$        }));
        //#endif
        LanguageProvider.INSTANCE.init();
        logger.info("[TNCA] Hello World!");
    }

    private void onRulesChanged(String name, ServerCommandSource src, String value) {
        switch (name) {
            case "commandKillFakePlayer", "commandMobSpawn" -> SettingCallbacks.COMMAND_OPTION_CHANGED.accept(src);
            case "commandHelp" -> {
                boolean enable = Boolean.parseBoolean(value);
                if (enable){
                    SettingCallbacks.HELP_COMMAND_ENABLED.accept(src);
                }else {
                    SettingCallbacks.HELP_COMMAND_DISABLED.accept(src);
                }
            }
            default -> {

            }
        }
    }

    @Override
    public void onGameStarted() {
        CarpetServer.settingsManager.parseSettingsClass(CarpetAdditionSetting.class);
    }


    //#if MC >= 11900
    @Override
    public void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher, net.minecraft.command.CommandRegistryAccess access) {
        this.registerCommand(dispatcher, access);
    }
    //#else
//$$    @Override
//$$    public void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher) {
//$$        this.registerCommand(dispatcher, null);
//$$    }
    //#endif

    private void registerCommand(CommandDispatcher<ServerCommandSource> dispatcher, net.minecraft.command.CommandRegistryAccess access) {
        KillFakePlayerCommand.register(dispatcher);
        MobSpawnCommand.register(dispatcher, access);
    }

    @Override
    public void onServerClosed(MinecraftServer server) {

    }

    @Override
    public void onReload(MinecraftServer server) {

    }

    @Override
    public String version() {
        return FabricLoader.getInstance().getModContainer("takeneko-carpet-addition").get().getMetadata().getVersion().getFriendlyString();
    }


    @Override
    public Map<String, String> canHasTranslations(String lang) {
        return LanguageProvider.INSTANCE.getTranslationsForLang(lang);
    }
}
