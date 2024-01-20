package net.zhuruoling.tnca.spawn;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Identifier;
import net.minecraft.util.WorldSavePath;
import net.zhuruoling.tnca.util.IntRange;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;

public class SpawnRestrictionManager {
    public final static SpawnRestrictionManager INSTANCE = new SpawnRestrictionManager();
    private final static Gson GSON = new GsonBuilder().registerTypeAdapter(Identifier.class, new Identifier.Serializer()).setPrettyPrinting().serializeNulls().create();
    HashMap<Identifier, SpawnRestrictionModification> map = new HashMap<>();

    Set<Identifier> logEntities = new HashSet<>();

    public void addEmpty(Identifier identifier) {
        map.put(identifier, new SpawnRestrictionModification(identifier));
    }

    public boolean contains(Identifier identifier) {
        return map.containsKey(identifier);
    }

    public void save(MinecraftServer server) {
        try {
            var file = server.getSavePath(WorldSavePath.ROOT).resolve("mobSpawn.json").toFile();
            if (file.exists()) file.delete();
            if (!file.exists()) file.createNewFile();
            var writer = new FileWriter(file);
            GSON.toJson(new Storage(logEntities, map), writer);
            writer.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void load(MinecraftServer server) {
        try {
            var file = server.getSavePath(WorldSavePath.ROOT).resolve("mobSpawn.json").toFile();
            if (!file.exists()) {
                file.createNewFile();
                var writer = new FileWriter(file);
                writer.write("{}");
                writer.close();
                map = new HashMap<>();
                return;
            }
            var reader = new FileReader(file);
            var storage = GSON.fromJson(reader, Storage.class);
            map.clear();
            map.putAll(storage.map());
            if (storage.logEntities != null) {
                logEntities.clear();
                logEntities.addAll(storage.logEntities());
            }
            reader.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public SpawnRestrictionModification getModification(Identifier identifier) {
        return map.get(identifier);
    }

    public boolean canSpawn(Identifier identifier) {
        return !contains(identifier) || getModification(identifier).canSpawn();
    }

    public void setCanSpawn(Identifier identifier, boolean canSpawn) {
        if (!contains(identifier)) addEmpty(identifier);
        getModification(identifier).setCanSpawn(canSpawn);
    }

    public IntRange getBrightness(Identifier identifier) {
        var mod = getModification(identifier);
        if (mod == null)return null;
        return getModification(identifier).getBrightness();
    }

    public void setBrightness(Identifier identifier, IntRange range){
        if (!contains(identifier)) addEmpty(identifier);
        getModification(identifier).setBrightness(range);
    }

    public void setHeight(Identifier identifier, IntRange range){
        if (!contains(identifier)) addEmpty(identifier);
        getModification(identifier).setHeight(range);
    }

    public IntRange getHeight(Identifier identifier) {
        var mod = getModification(identifier);
        if (mod == null)return null;
        return getModification(identifier).getHeight();
    }

    public void clear(Identifier id) {
        map.remove(id);
    }

    public boolean logs(Identifier id){
        return logEntities.contains(id);
    }

    public boolean addSpawnLog(ServerCommandSource src, Identifier id) {
        return logEntities.add(id);
    }

    public boolean removeSpawnLog(ServerCommandSource src, Identifier id) {
        return logEntities.remove(id);
    }

    public static record Storage(Set<Identifier> logEntities, HashMap<Identifier, SpawnRestrictionModification> map){

    }
}
