package alpvax.classmodcore.api.classes;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import alpvax.classmodcore.api.powers.PowerEntry;


/**
 * @author Alpvax
 *
 */
public interface IPlayerClass
{
	/**
	 * @return a unique id to reference this class. Uses the form <group>.<id>
	 */
	public String getClassID();

	public String getDisplayName();

	public List<PowerEntry> getPowers();

	/**
	 * Set player attributes etc.
	 */
	public void setup(EntityPlayer player);

	/**
	 * Restore player attributes to without a class etc.
	 */
	public void reset(EntityPlayer player);

}
