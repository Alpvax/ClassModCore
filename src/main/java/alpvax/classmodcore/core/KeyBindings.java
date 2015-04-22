package alpvax.classmodcore.core;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

import org.lwjgl.input.Keyboard;


public class KeyBindings
{
	public static List<KeyBinding> powerBindings = new ArrayList<KeyBinding>();

	public static void init()
	{
		powerBindings.add(new KeyBinding("key.classmod.power1", Keyboard.KEY_R, "key.classmod.powers"));
		powerBindings.add(new KeyBinding("key.classmod.power2", Keyboard.KEY_F, "key.classmod.powers"));

		// Register all KeyBindings to the ClientRegistry
		for(KeyBinding kb : powerBindings)
		{
			ClientRegistry.registerKeyBinding(kb);
		}
	}
}
