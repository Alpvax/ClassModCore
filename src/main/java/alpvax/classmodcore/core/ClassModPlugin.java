package alpvax.classmodcore.core;

import java.util.Map;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import alpvax.classmodcore.asm.ObliviousTransformer;


public class ClassModPlugin implements IFMLLoadingPlugin
{

	@Override
	public String[] getASMTransformerClass()
	{
		return new String[]{ObliviousTransformer.class.getName()};
	}

	@Override
	public String getModContainerClass()
	{
		return null;// TODO Auto-generated method stub
	}

	@Override
	public String getSetupClass()
	{
		return null;// TODO Auto-generated method stub
	}

	@Override
	public void injectData(Map<String, Object> data)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public String getAccessTransformerClass()
	{
		return null;// TODO Auto-generated method stub
	}

}
