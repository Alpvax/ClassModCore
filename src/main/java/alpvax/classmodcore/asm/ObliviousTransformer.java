package alpvax.classmodcore.asm;

import java.util.Iterator;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FrameNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import alpvax.classmodcore.api.classes.PlayerClassHelper;
import alpvax.common.asm.AlpTransformer;


public class ObliviousTransformer extends AlpTransformer
{
	@Override
	/*OLD:
	mv.visitInsn(ICONST_0);
	mv.visitInsn(IRETURN);
	mv.visitLabel(l7);****************
	mv.visitLineNumber(144, l7);**************

	 *NEW:
	mv.visitInsn(ICONST_0);
	mv.visitInsn(IRETURN);
	mv.visitLabel(l7);**********************
	mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
	mv.visitVarInsn(ALOAD, 1);
	mv.visitTypeInsn(INSTANCEOF, "net/minecraft/entity/player/EntityPlayer");
	Label cpCont = new Label();
	mv.visitJumpInsn(IFEQ, cpCont);
	mv.visitVarInsn(ALOAD, 1);
	mv.visitTypeInsn(CHECKCAST, "net/minecraft/entity/player/EntityPlayer");
	mv.visitVarInsn(ALOAD, 0);
	mv.visitMethodInsn(INVOKESTATIC, "alpvax/classmodcore/api/classes/PlayerClassHelper", "isOblivious", "(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/entity/EntityLivingBase;)Z", false);
	mv.visitJumpInsn(IFEQ, cpCont);
	Label pcRet = new Label();
	mv.visitLabel(pcRet);
	mv.visitInsn(ICONST_0);
	mv.visitInsn(IRETURN);
	mv.visitLabel(pcCont);
	mv.visitLineNumber(144, l7);************************
	 */
	protected byte[] do_transform(String name, byte[] basicClass, boolean obfuscated)
	{
		String methodName = obfuscated ? "a" : "isSuitableTarget"; //func_179445_a
		String methodDesc = obfuscated ? "(Lxn;Lxm;ZZ)Z" : "(Lnet/minecraft/entity/EntityLiving;Lnet/minecraft/entity/EntityLivingBase;ZZ)Z";
		String entityPlayerClass = obfuscated ? "ahd" : "net/minecraft/entity/player/EntityPlayer";
		String targetMethodNodeName = obfuscated ? "bN" : "getTeam";

		System.err.printf("Transforming %s.%s%s%n", name, methodName, methodDesc);//XXX
		ClassNode classNode = new ClassNode();
		ClassReader classReader = new ClassReader(basicClass);
		classReader.accept(classNode, 0);
		Iterator<MethodNode> methods = classNode.methods.iterator();
		while(methods.hasNext())
		{
			MethodNode m = methods.next();
			if(m.name.equals(methodName) && m.desc.equals(methodDesc))
			{

				Iterator<AbstractInsnNode> iter = m.instructions.iterator();
				//Loop over the instruction set and find:
				/*OLD:
				mv.visitInsn(ICONST_0);
				mv.visitInsn(IRETURN);
				mv.visitLabel(l7);************************INSERT HERE
				mv.visitLineNumber(144, l7);*************************/

				while(iter.hasNext())
				{
					AbstractInsnNode node = iter.next();
					if(node instanceof MethodInsnNode)
					{
						MethodInsnNode mnode = (MethodInsnNode)node;
						System.err.printf("this: %s %s%n", mnode.name, mnode.desc);//XXX
						if(mnode.name.equals(targetMethodNodeName) && mnode.desc.equals("()L" + (obfuscated ? "" : "net/minecraft/scoreboard/Team") + ";"))
						{
							AbstractInsnNode target = node.getPrevious();
							for(int i = 0; i < 4; i++)
							{
								target = target.getPrevious();
								if(target instanceof LabelNode)
								{
									break;
								}
							}

							/*NEW:
							mv.visitInsn(ICONST_0);
							mv.visitInsn(IRETURN);
							mv.visitLabel(l7);************************INSERT HERE
							mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
							mv.visitVarInsn(ALOAD, 1);
							mv.visitTypeInsn(INSTANCEOF, "net/minecraft/entity/player/EntityPlayer");
							Label l9 = new Label();
							mv.visitJumpInsn(IFEQ, l9);
							mv.visitVarInsn(ALOAD, 1);
							mv.visitTypeInsn(CHECKCAST, "net/minecraft/entity/player/EntityPlayer");
							mv.visitVarInsn(ALOAD, 0);
							mv.visitMethodInsn(INVOKESTATIC, "alpvax/classmodcore/api/classes/PlayerClassHelper", "isOblivious", "(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/entity/EntityLivingBase;)Z", false);
							mv.visitJumpInsn(IFEQ, l9);
							mv.visitInsn(ICONST_0);
							mv.visitInsn(IRETURN);
							mv.visitLabel(l9);
							mv.visitLineNumber(144, l7);*************************/

							InsnList list = new InsnList();
							list.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
							list.add(new VarInsnNode(Opcodes.ALOAD, 1));
							list.add(new TypeInsnNode(Opcodes.INSTANCEOF, entityPlayerClass));
							LabelNode cont = new LabelNode();
							list.add(new JumpInsnNode(Opcodes.IFEQ, cont));
							list.add(new VarInsnNode(Opcodes.ALOAD, 1));
							list.add(new TypeInsnNode(Opcodes.CHECKCAST, entityPlayerClass));
							list.add(new VarInsnNode(Opcodes.ALOAD, 0));
							list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, PlayerClassHelper.class.getName(), "isOblivious", "(L" + entityPlayerClass + (obfuscated ? ";Lxm;)Z" : ";Lnet.minecraft.entity.EntityLivingBase;)Z"), false));
							list.add(new JumpInsnNode(Opcodes.IFEQ, cont));
							list.add(new InsnNode(Opcodes.ICONST_0));
							list.add(new InsnNode(Opcodes.IRETURN));
							list.add(cont);
							m.instructions.insert(target, list);
							break;
						}
					}
				}

				iter = m.instructions.iterator();//XXX
				while(iter.hasNext())
				{//XXX
					AbstractInsnNode node = iter.next();//XXX
					System.err.printf("%s; Opcode=%s; %s;%n", node.getClass().getSimpleName(), node.getOpcode(), node instanceof MethodInsnNode ? ((MethodInsnNode)node).name + ((MethodInsnNode)node).desc : "");//XXX
				}//XXX
			}
		}

		//ASM specific for cleaning up and returning the final bytes for JVM processing.
		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
		classNode.accept(writer);
		return writer.toByteArray();
	}

	@Override
	protected String getDeobfName()
	{
		return "net.minecraft.entity.ai.EntityAITarget";
	}

	@Override
	protected String getObfName()
	{
		return "aaw";
	}
}
