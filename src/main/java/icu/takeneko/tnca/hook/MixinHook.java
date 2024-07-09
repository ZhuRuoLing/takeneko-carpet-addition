package icu.takeneko.tnca.hook;

import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings({"unused"})
public class MixinHook {
//    INVOKEINTERFACE java/util/Set.contains (Ljava/lang/Object;)Z (itf)
//    IFEQ L21
//    L22
//    LINENUMBER 846 L22
//    GOTO L17
    public final static String INTERNAL_NAME = MixinHook.class.getName().replace(".","/");
    private final static List<String> MIXIN_1_16_5_IGNORE = List.of("FrogEntityMixin_canSpawn",
            "GlowSquidEntityMixin_canSpawn",
            "GoatEntityMixin_canSpawn",
            "WaterCreatureEntityMixin_canSpawn",
            "WolfEntityMixin_canSpawn",
            "AxolotlEntityMixin_canSpawn");

    //for 1.16.5
    public static boolean shouldInitMixin(String mixinClass){
        return MIXIN_1_16_5_IGNORE.stream().noneMatch(mixinClass::endsWith);
    }

    public static List<String> fixMixinClasses(String mixinPackage, List<String> mixinClasses){
        if (mixinClasses == null)return null;
        //#if MC < 11800
//$$        if (mixinPackage.startsWith("icu.takeneko.tnca")){
//$$            mixinClasses.removeAll(mixinClasses.stream().filter(it -> MIXIN_1_16_5_IGNORE.stream().anyMatch(it::endsWith)).toList());
//$$        }
        //#endif
        return mixinClasses;
    }
}
