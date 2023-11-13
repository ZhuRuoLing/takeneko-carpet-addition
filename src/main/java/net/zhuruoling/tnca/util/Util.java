package net.zhuruoling.tnca.util;

import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;

public class Util {
    public static Identifier getIdFromEntityType(EntityType<?> entityType){
        //#if MC > 11900
        return net.minecraft.registry.Registries.ENTITY_TYPE.getId(entityType);
        //#else
//$$    return net.minecraft.util.registry.Registry.ENTITY_TYPE.getId(entityType);
        //#endif
    }
}
