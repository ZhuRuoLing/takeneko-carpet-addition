package net.zhuruoling.tnca.settings;
//#if MC >11900
//$$ import carpet.api.settings.Rule;
//#else

import carpet.settings.Rule;
//#endif

public class CarpetAdditionSetting {

    @Rule(
            //#if MC >11900
            //$$ categories
            //#else
            desc = "Use regular expressions or string inclusion rules to remove fake players.",
            category
                    //#endif
                    = {"TNCA", "command"})
    public static boolean commandKillFakePlayer = false;

    @Rule(
            //#if MC >11900
            //$$ categories
            //#else
            desc = "Use generate pattern to spawn carpet fake players in bulk.",
            category
                    //#endif
                    = {"TNCA", "command"})
    public static boolean commandSpawnFakePlayers = false;
    //spawnFakePlayers Player{range 1..3 }%3{random 1..100}%2

    @Rule(
            //#if MC >11900
            //$$ categories
            //#else
            desc = "Modify mob spawn conditions",
            category
                    //#endif
                    = {"TNCA", "command"})
    public static boolean commandMobSpawn = false;
    //spawnFakePlayers Player{range 1..3 }%3{random 1..100}%2

}
