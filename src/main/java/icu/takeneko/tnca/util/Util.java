package icu.takeneko.tnca.util;

import net.minecraft.entity.EntityType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class Util {
    public static Identifier getIdFromEntityType(EntityType<?> entityType) {
        //#if MC > 11900
        return net.minecraft.registry.Registries.ENTITY_TYPE.getId(entityType);
        //#else
//$$    return net.minecraft.util.registry.Registry.ENTITY_TYPE.getId(entityType);
        //#endif
    }

    public static Identifier getWorldIdFromServerWorld(ServerWorld world) {
        return world.getRegistryKey().getValue();
    }

    public static Text formatClickablePosition(BlockPos pos) {
        //#if MC > 11900
        return Text.literal(pos.getX() + ", " + pos.getY() + ", " + pos.getZ())
                .copy()
                .setStyle(Style.EMPTY.withColor(Formatting.AQUA).withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tp @s " + pos.getX() + " " + pos.getY() + " " + pos.getZ())).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.translatable("chat.coordinates.tooltip"))));
        //#else
        //$$ return new LiteralText(pos.getX() + ", " + pos.getY() + ", " + pos.getZ())
        //$$.setStyle(Style.EMPTY.withColor(Formatting.AQUA).withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tp @s " + pos.getX() + " " + pos.getY() + " " + pos.getZ())).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslatableText("chat.coordinates.tooltip"))));
        //#endif
    }

    public static Text getEntityDisplayName(Identifier identifier) {
        //#if MC > 11900
        return Text.translatable(net.minecraft.registry.Registries.ENTITY_TYPE.get(identifier).getTranslationKey())
                .copyContentOnly()
                .setStyle(Style.EMPTY.withColor(Formatting.GREEN));
        //#else
//$$    return new TranslatableText(net.minecraft.util.registry.Registry.ENTITY_TYPE.get(identifier).getTranslationKey()).setStyle(Style.EMPTY.withColor(Formatting.GREEN));
        //#endif

    }
}
