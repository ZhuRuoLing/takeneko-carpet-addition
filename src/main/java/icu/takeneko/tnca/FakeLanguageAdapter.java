package icu.takeneko.tnca;

import icu.takeneko.tnca.hook.ClassPatcher;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.fabricmc.loader.api.LanguageAdapter;
import net.fabricmc.loader.api.LanguageAdapterException;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.fabricmc.loader.impl.game.minecraft.MinecraftGameProvider;
import org.spongepowered.asm.service.MixinService;

import java.io.IOException;
import java.io.InputStream;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FakeLanguageAdapter implements LanguageAdapter {
    @Override
    public <T> T create(ModContainer mod, String value, Class<T> type) throws LanguageAdapterException {
        throw new UnsupportedOperationException("wtf");
    }

    static {
        try {
            var dumps = Path.of("./.tnca_dumps");
            if (dumps.toFile().exists()) {
                final Iterator<Path> iterator = Files.walk(dumps).sorted(Comparator.reverseOrder()).iterator();
                while (iterator.hasNext()) {
                    Files.delete(iterator.next());
                }
            }
            final ClassLoader classLoader = FakeLanguageAdapter.class.getClassLoader();
            final ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
            unlockLibraryOnKnot(contextClassLoader);
            try {
                final Instrumentation inst = ByteBuddyAgent.install();
                inst.redefineModule(
                        ModuleLayer.boot().findModule("java.base").get(),
                        Set.of(),
                        Map.of(),
                        Map.of("java.lang", Set.of(FakeLanguageAdapter.class.getModule())),
                        Set.of(),
                        Map.of()
                );
            } catch (Exception ignored) {
            }
            defineClass(MixinService.class.getClassLoader(), "icu.takeneko.tnca.hook.patch.ClassPatch");
            defineClass(MixinService.class.getClassLoader(), "icu.takeneko.tnca.hook.MixinHook");
            defineClass(MixinService.class.getClassLoader(), "icu.takeneko.tnca.hook.ClassPatcher");
            defineClass(MixinService.class.getClassLoader(), "icu.takeneko.tnca.hook.patch.MixinConfigClassPatch");

            ClassPatcher.init();
            ClassPatcher.reTransformClass(Class.forName("org.spongepowered.asm.mixin.transformer.MixinConfig"));
        }catch (Throwable e){
            throw new RuntimeException(e);
        }
    }


    public static Class<?> defineClass(ClassLoader classLoader, String name) throws IllegalAccessException, InvocationTargetException, IOException, NoSuchMethodException {
        final Method defineClass = ClassLoader.class.getDeclaredMethod("defineClass", String.class, byte[].class, int.class, int.class);
        defineClass.setAccessible(true);
        return (Class<?>) defineClass.invoke(
                classLoader,
                name,
                getClassFile(name),
                0,
                getClassFile(name).length
        );
    }

    private static byte[] getClassFile(String name) throws IOException {
        try (InputStream in = FakeLanguageAdapter.class.getClassLoader().getResourceAsStream(name.replace('.', '/') + ".class")) {
            return in.readAllBytes();
        }
    }

    //com.ishland.earlyloadingscreen.EarlyLaunch
    private static void unlockLibraryOnKnot(ClassLoader knotClassLoader) {
        try {
            final Method getDelegate = knotClassLoader.getClass().getDeclaredMethod("getDelegate");
            getDelegate.setAccessible(true);
            final Object knotClassLoaderDelegate = getDelegate.invoke(knotClassLoader);
            final Class<?> delegateClazz = Class.forName("net.fabricmc.loader.impl.launch.knot.KnotClassDelegate");
            final MinecraftGameProvider gameProvider = (MinecraftGameProvider) FabricLoaderImpl.INSTANCE.getGameProvider();
            final Field getMiscGameLibraries = MinecraftGameProvider.class.getDeclaredField("miscGameLibraries");
            getMiscGameLibraries.setAccessible(true);
            List<Path> miscGameLibraries = (List<Path>) getMiscGameLibraries.get(gameProvider);
            final Method setAllowedPrefixes = delegateClazz.getDeclaredMethod("setAllowedPrefixes", Path.class, String[].class);
            setAllowedPrefixes.setAccessible(true);
            final Method addCodeSource = delegateClazz.getDeclaredMethod("addCodeSource", Path.class);
            addCodeSource.setAccessible(true);
            for (Path library : miscGameLibraries) {
                setAllowedPrefixes.invoke(knotClassLoaderDelegate, library, new String[0]);
                addCodeSource.invoke(knotClassLoaderDelegate, library);
            }
        } catch (Throwable t) {
            throw new RuntimeException("Failed to unlock library on knot", t);
        }
    }
}
