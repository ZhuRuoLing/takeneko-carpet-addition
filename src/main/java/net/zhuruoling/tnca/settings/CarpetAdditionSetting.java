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

}
