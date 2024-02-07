package icu.takeneko.tnca.settings;

import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Map;

public class HelpCommandDisabledCallback implements SettingCallback{
    @Override
    public void accept(ServerCommandSource source) {
        try {
            var dispatcher = source.getServer().getCommandManager().getDispatcher();
            var clazz = dispatcher.getClass();
            var field = clazz.getDeclaredField("root");
            field.setAccessible(true);
            RootCommandNode<?> root = (RootCommandNode<?>) field.get(dispatcher);
            var childClazz = root.getClass();
            var nodeMapField = childClazz.getDeclaredField("children");
            nodeMapField.setAccessible(true);
            Map<String, CommandNode<?>> nodeMap = (Map<String, CommandNode<?>>) nodeMapField.get(root);
            nodeMap.remove("help");
            for (ServerPlayerEntity player : source.getServer().getPlayerManager().getPlayerList()) {
                source.getServer().getCommandManager().sendCommandTree(player);
            }
        }catch (Throwable e){
            throw new RuntimeException(e);
        }
    }
}
