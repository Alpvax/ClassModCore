package alpvax.classmodcore.api.powers;

import java.util.List;
import java.util.Map;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Vec3;
import net.minecraftforge.event.entity.living.LivingEvent;

import com.google.common.base.Predicate;


/**
 * @author Alpvax
 *
 */
public interface IPower
{
	/**
	 * Only required if the power will show on GUI
	 */
	public String getDisplayName();

	/**
	 * An IPower which does something each tick
	 */
	public interface ITickingPower extends IPower
	{
		public void onTick(EntityPlayer player, int ticksActive);
	}

	/**
	 * An IPower which does something when triggered (Either automatically or by the player)
	 */
	public interface ITriggeredPower extends IPower
	{
		/**
		 * @param player the player this power is acting from
		 * @param instanceData and additional data
		 * @return true if the power should be triggered automatically
		 */
		public boolean shouldTrigger(EntityPlayer player);

		/**
		 * Called whenever the power is triggered (either automatically or manually)<br>
		 * Also called to start continuous powers (i.e. for setting attributes)
		 *
		 * @return true if the trigger was successful, cooldowns and durations will be adjusted
		 */
		public boolean triggerPower(EntityPlayer player);
	}

	/**
	 * An ITriggeredPower which also requires resetting (Either automatically or by the player)
	 */
	public interface IToggledPower extends ITriggeredPower
	{
		/**
		 * Opposite of {@link #shouldTrigger(EntityPlayer)}<br>
		 * Needs an implementation if it is automatically disabled, but not timed.
		 * @return true if the power should be reset automatically
		 */
		public boolean shouldReset(EntityPlayer player);

		/**
		 * Called whenever the power is reset (either automatically or manually)<br>
		 * Needs an implementation if it is automatically disabled, or disabled after a timer.
		 */
		public void resetPower(EntityPlayer player, int ticksActive);
	}


	/**
	 * An extended version of IPower which affects multiple entities in an area
	 * @param targetEntity the entity the target is centred on (i.e. the player for self-target, the entity hit by the MOP or null if point target)
	 */
	public interface IMultiTargetPower extends IPower
	{
		public List<Entity> getTargetEntities(Entity e, Vec3 target, Map<String, Object> instanceData);
	}

	/**
	 * An extended version of IPower which has additional entity targeting options
	 */
	public interface ITargetedPower extends IPower
	{
		public Predicate<Entity> getEntityFilter(Map<String, Object> instanceData);

		/**
		 * @return the radius to search for a nearby target. 0 means no homing, negative numbers indicate no limit
		 */
		public double homingRadius(Map<String, Object> instanceData);
	}

	/**
	 * An extended version of IPower which is triggered by events instead of the normal triggering system
	 */
	public interface IPowerEventListener<T extends LivingEvent> extends IPower
	{
		public void listenToEvent(T e, EntityPlayer player);

		/**
		 * Needs to return the Event Class to listen to
		 */
		public Class<T> getEventClass();
	}

	/**
	 * An extended version of IPower which saves additional data
	 */
	public interface IExtendedPower extends IPower
	{
		public void readFromNBT(NBTTagCompound nbt);

		public void writeToNBT(NBTTagCompound nbt);
	}
}
