package net.zhuruoling.tnca;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import carpet.settings.SettingsManager;
import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.zhuruoling.tnca.lang.LanguageProvider;
import net.zhuruoling.tnca.settings.CarpetAdditionSetting;
import net.zhuruoling.tnca.settings.SettingCallbacks;

import java.util.Map;

public class CarpetAdditionMain implements CarpetExtension, ModInitializer {
    @Override
    public void onInitialize() {
        CarpetServer.manageExtension(new CarpetAdditionMain());
        CarpetServer.settingsManager.parseSettingsClass(CarpetAdditionSetting.class);
        SettingsManager.addGlobalRuleObserver(((src, parsedRule, s) -> {
            if (parsedRule.type != CarpetAdditionSetting.class) return;
            switch (parsedRule.name) {
                case "commandKillPlayerMPFake" -> SettingCallbacks.KILL_PLAYER_MPFAKE.accept(src);

                default -> {

                }
            }

        }));
        LanguageProvider.INSTANCE.init();
    }

    @Override
    public void onServerLoadedWorlds(MinecraftServer server) {
        CarpetExtension.super.onServerLoadedWorlds(server);
    }

    //#if MC >= 11900
    //$$@Override
    //$$    public void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher, net.minecraft.command.CommandRegistryAccess commandBuildContext) {
    //$$
    //$$    }
    //#else
    @Override
    public void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher) {

    }
    //#endif

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
