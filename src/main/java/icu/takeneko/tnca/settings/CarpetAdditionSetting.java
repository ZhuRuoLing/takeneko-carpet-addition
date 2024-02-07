package icu.takeneko.tnca.settings;
//#if MC >=11900
import carpet.api.settings.Rule;
//#else
//$$import carpet.settings.Rule;
//#endif



public class CarpetAdditionSetting {

    @Rule(
            //#if MC >=11900
            categories
            //#else
//$$            desc = "Use regular expressions or string inclusion rules to remove fake players.",
//$$            category
            //#endif
                    = {"TNCA", "command"},
            options = {"false", "true", "0","1","2","3","4","ops"})
    public static String commandKillFakePlayer = "ops";

    @Rule(
            //#if MC >=11900
            categories
            //#else
//$$            desc = "Modify mob spawn conditions",
//$$            category
                    //#endif
                    = {"TNCA", "command"},
            options = {"false", "true", "0","1","2","3","4","ops"})
    public static String commandMobSpawn = "ops";
    //spawnFakePlayers Player{range 1..3 }%3{random 1..100}%2

    //#if MC >=11900
    @Rule(categories = {"TNCA","fix"})
    public static boolean bypassMessageOrderCheck = false;
    //#endif


}
