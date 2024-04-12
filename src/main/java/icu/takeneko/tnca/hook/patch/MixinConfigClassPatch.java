package icu.takeneko.tnca.hook.patch;

import icu.takeneko.tnca.hook.MixinHook;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import java.util.ListIterator;

public class MixinConfigClassPatch implements ClassPatch {
    @Override
    public String getClassName() {
        return "org.spongepowered.asm.mixin.transformer.MixinConfig";
    }

    @Override
    public boolean patchClass(ClassNode classNode) {
//        if (classNode.name.contentEquals(getClassName().replace(".", "/"))) {
//            return false;
//        }
        for (MethodNode method : classNode.methods) {
            //prepareMixins(Ljava/lang/String;Ljava/util/List;ZLorg/spongepowered/asm/mixin/transformer/ext/Extensions;)V
            if (method.name.equals("prepareMixins")
                    && method.desc.equals("(Ljava/lang/String;Ljava/util/List;ZLorg/spongepowered/asm/mixin/transformer/ext/Extensions;)V")) {
                ListIterator<AbstractInsnNode> iter = method.instructions.iterator();
                iter.add(new VarInsnNode(Opcodes.ALOAD, 0));
                //GETFIELD org/spongepowered/asm/mixin/transformer/MixinConfig.mixinPackage : Ljava/lang/String;
                iter.add(new FieldInsnNode(
                        Opcodes.GETFIELD,
                        "org/spongepowered/asm/mixin/transformer/MixinConfig",
                        "mixinPackage",
                        "Ljava/lang/String;"
                ));
                iter.add(new VarInsnNode(Opcodes.ALOAD, 2));
                iter.add(new MethodInsnNode(
                        Opcodes.INVOKESTATIC,
                        MixinHook.INTERNAL_NAME,
                        "fixMixinClasses",
                        "(Ljava/lang/String;Ljava/util/List;)Ljava/util/List;"
                ));
                iter.add(new VarInsnNode(Opcodes.ASTORE, 2));
                return true;
            }
        }
        return false;
    }
}
