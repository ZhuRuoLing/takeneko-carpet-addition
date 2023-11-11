package net.zhuruoling.tnca;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import carpet.settings.SettingsManager;
import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.zhuruoling.tnca.command.KillFakePlayerCommand;
import net.zhuruoling.tnca.command.MobSpawnCommand;
import net.zhuruoling.tnca.lang.LanguageProvider;
import net.zhuruoling.tnca.settings.CarpetAdditionSetting;
import net.zhuruoling.tnca.settings.SettingCallbacks;

import java.util.Map;

public class CarpetAdditionMain implements CarpetExtension, ModInitializer {
    @Override
    public void onInitialize() {
        CarpetServer.manageExtension(new CarpetAdditionMain());
        //#if MC >= 11900
        //$$SettingsManager.registerGlobalRuleObserver(((src, parsedRule, s) -> {
        //$$            if (parsedRule.type() != CarpetAdditionSetting.class) return;
        //$$            onRulesChanged(parsedRule.name(), src);
        //$$        }));
        //#else
        SettingsManager.addGlobalRuleObserver(((src, parsedRule, s) -> {
            if (parsedRule.type != CarpetAdditionSetting.class) return;
            onRulesChanged(parsedRule.name, src);
        }));
        //#endif
        LanguageProvider.INSTANCE.init();
        System.out.println("Hello World!");
    }

    private void onRulesChanged(String name, ServerCommandSource src) {
        switch (name) {
            case "commandKillPlayerMPFake", "commandMobSpawn" -> SettingCallbacks.COMMAND_OPTION_CHANGED.accept(src);
            default -> {

            }
        }
    }

    @Override
    public void onGameStarted() {
        CarpetServer.settingsManager.parseSettingsClass(CarpetAdditionSetting.class);
    }

    @Override
    public void onServerLoaded(MinecraftServer server) {
        CarpetServer.settingsManager.parseSettingsClass(CarpetAdditionSetting.class);
    }

    //#if MC >= 11900
    //$$@Override
    //$$    public void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher, net.minecraft.command.CommandRegistryAccess access) {
    //$$        this.registerCommand(dispatcher, access);
    //$$    }
    //#else
    @Override
    public void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher) {
        this.registerCommand(dispatcher, null);
    }
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
        return LanguageProvider.INSTANCE.canHasTranslations(lang);
    }
}
