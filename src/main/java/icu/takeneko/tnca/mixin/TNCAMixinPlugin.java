package icu.takeneko.tnca.mixin;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class TNCAMixinPlugin implements IMixinConfigPlugin {

    private final static List<String> MIXIN_1_16_5_IGNORE = List.of("FrogEntityMixin_canSpawn",
            "GlowSquidEntityMixin_canSpawn",
            "GoatEntityMixin_canSpawn",
            "WaterCreatureEntityMixin_canSpawn",
            "WolfEntityMixin_canSpawn",
            "AxolotlEntityMixin_canSpawn");

    private final static List<String> MIXIN_1_21_APPEND = List.of("AmadilloMixin_canSpawn");

    @Override
    public void onLoad(String mixinPackage) {

    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (Objects.equals(mixinClassName, "icu.takeneko.tnca.mixin.ServerPlayNetworkHandlerMixin")){
            return !Boolean.getBoolean("doNotBypassMessageOrderCheck");
        }
        if (MIXIN_1_16_5_IGNORE.stream().anyMatch(it -> mixinClassName.endsWith(it))){
            //#if MC < 11800
            //$$ return false;
            //#else
            return true;
            //#endif
        }
        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    @Override
    public List<String> getMixins() {
        //#if MC >= 12100
        //$$return MIXIN_1_21_APPEND;
        //#else
        return null;
        //#endif

    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }
}
