package alpvax.classmodcore.api.powers;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;


//TODO:Modify method:
/**{@link net.minecraft.entity.ai.EntityAITarget#isSuitableTarget(EntityLivingBase, boolean)}<br>
 * <pre>ASM: (line 209: "return true")
 * {@code
 * mv.visitLineNumber(209, l5);
 * mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
 * mv.visitInsn(ICONST_1);
 * mv.visitInsn(IRETURN);
 * Label l14 = new Label();
 * mv.visitLabel(l14);
 * mv.visitLocalVariable("this", "Lnet/minecraft/entity/ai/EntityAITarget;", null, l0, l14, 0);
 * mv.visitLocalVariable("p_75296_1_", "Lnet/minecraft/entity/EntityLivingBase;", null, l0, l14, 1);
 * mv.visitLocalVariable("p_75296_2_", "Z", null, l0, l14, 2);
 * mv.visitMaxs(4, 3);
 * mv.visitEnd();}</pre>
 *
 */
public abstract class PowerOblivious extends DummyPower
{
	public PowerOblivious(String entity)
	{
		super("Oblivious to ", entity);
	}

	public abstract boolean canEntityTargetPlayer(EntityLiving entity, EntityPlayer player);
}
