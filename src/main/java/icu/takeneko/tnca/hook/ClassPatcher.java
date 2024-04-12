package icu.takeneko.tnca.hook;

import icu.takeneko.tnca.hook.patch.ClassPatch;
import icu.takeneko.tnca.hook.patch.MixinConfigClassPatch;
import net.bytebuddy.agent.ByteBuddyAgent;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.ProtectionDomain;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ClassPatcher {

    private static final Instrumentation instrumentation = ByteBuddyAgent.install();
    private static final List<ClassPatch> classPatches = new CopyOnWriteArrayList<>();
    private static final Path patcherDumps = Path.of("./.tnca_dumps");
    public static void init(){
        instrumentation.addTransformer(new ClassFileTransformer() {
            @Override
            public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
                ClassReader reader = new ClassReader(classfileBuffer);
                ClassNode classNode = new ClassNode();
                reader.accept(classNode, 0);
                String name = className.replace("/", ".");
                boolean transformed = classPatches.stream()
                        .filter(cp -> cp.getClassName().equals(name))
                        .anyMatch(cp -> cp.patchClass(classNode));
                if (transformed){
                    ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
                    classNode.accept(cw);
                    byte[] ba = cw.toByteArray();
                    try {
                        Path path = patcherDumps.resolve(className + ".class");
                        Files.createDirectories(path.getParent());
                        Files.write(path, ba);
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                    return ba;
                }
                return null;
            }
        }, true);
    }

    static {
        classPatches.add(new MixinConfigClassPatch());
    }

    public static void reTransformClass(Class<?> clazz) throws UnmodifiableClassException {
        instrumentation.retransformClasses(clazz);
    }

}
