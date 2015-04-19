package alpvax.mod.classmodcore.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import alpvax.mod.classmodcore.classes.IPlayerClass;
import alpvax.mod.classmodcore.classes.PlayerClassRegistry;

public class GuiClassButton extends GuiButton
{
	/** Button width in pixels */
	public static final int btnWidth = 64;

	/** Button height in pixels */
	public static final int btnHeight = 100;
	
	
	/** The IPlayerClass of this button. */
	public IPlayerClass playerclass;

	public GuiClassButton(int id, int x, int y, IPlayerClass playerClass)
	{
		super(id, x, y, btnWidth, btnHeight, null);
		playerclass = playerClass;
	}

	public void draw(Minecraft mc, int mouseX, int mouseY)
	{
		mc.getTextureManager().bindTexture(PlayerClassRegistry.getClassImage(playerclass.getClassID()));
		drawTexturedModalRect(xPosition, yPosition, 0, 0, width, height);
		/*
		 * TODO: textures drawTexturedModalRect(xPosition + 8, yPosition + 12, 0, 0, 46, 73);//Class image mc.getTextureManager().bindTexture(ClassUtil.classGUIMain); drawTexturedModalRect(xPosition, yPosition, 0, 0, 62, 96);//Border if(isMouseOver()) { drawTexturedModalRect(xPosition - 10, yPosition - 10, 62, 0, 86, 118);//SelectionBox }
		 */
		FontRenderer fr = mc.fontRendererObj;
		String s = playerclass.getDisplayName();
		fr.drawStringWithShadow(s, xPosition + width / 2 - fr.getStringWidth(s) / 2, yPosition + 85, 16777215);
	}
}
