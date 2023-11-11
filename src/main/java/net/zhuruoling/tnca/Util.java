package net.zhuruoling.tnca;

import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Util {
    public static Identifier getIdFromEntityType(EntityType<?> entityType){
        //#if MC > 11900
        //$$ return net.minecraft.registry.Registries.ENTITY_TYPE.getId(entityType);
        //#else
        return Registry.ENTITY_TYPE.getId(entityType);
        //#endif
    }
}
