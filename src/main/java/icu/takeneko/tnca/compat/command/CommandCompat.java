package icu.takeneko.tnca.compat.command;

import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.util.Identifier;

public class CommandCompat {
    public static ArgumentType<Identifier> getEntityArgumentType(){
        //#if MC >= 11800
            //#if MC > 11900
            //$$ return null;
            //#else
            //$$ return net.minecraft.command.argument.EntitySummonArgumentType.entitySummon();
            //#endif
        //#else
            //#if MC < 11800
            //$$ return net.minecraft.command.argument.EntitySummonArgumentType.entitySummon();
            //#else
            return null;
            //#endif
        //#endif
    }
}
