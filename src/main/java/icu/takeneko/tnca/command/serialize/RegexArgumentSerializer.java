package icu.takeneko.tnca.command.serialize;

import com.google.gson.JsonObject;
import icu.takeneko.tnca.command.arguments.RegexArgumentType;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.serialize.ArgumentSerializer;
import net.minecraft.network.PacketByteBuf;

public class RegexArgumentSerializer implements ArgumentSerializer<RegexArgumentType, RegexArgumentSerializer.Properties> {

    @Override
    public void writePacket(Properties properties, PacketByteBuf buf) {

    }

    @Override
    public Properties fromPacket(PacketByteBuf buf) {
        return null;
    }

    @Override
    public void writeJson(Properties properties, JsonObject json) {

    }

    @Override
    public Properties getArgumentTypeProperties(RegexArgumentType argumentType) {
        return null;
    }

    public static class Properties implements ArgumentSerializer.ArgumentTypeProperties<RegexArgumentType>{

        @Override
        public RegexArgumentType createType(CommandRegistryAccess commandRegistryAccess) {
            return null;
        }

        @Override
        public ArgumentSerializer<RegexArgumentType, ?> getSerializer() {
            return null;
        }

    }
}
