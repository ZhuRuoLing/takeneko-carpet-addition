package icu.takeneko.tnca.hook.patch;

import org.objectweb.asm.tree.ClassNode;

public interface ClassPatch {

    String getClassName();

    boolean patchClass(ClassNode classNode);
}
