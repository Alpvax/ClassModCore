package alpvax.mod.classmodcore.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

import org.lwjgl.opengl.GL11;

import alpvax.mod.classmodcore.classes.IPlayerClass;
import alpvax.mod.classmodcore.classes.PlayerClassRegistry;
import alpvax.mod.classmodcore.core.ClassMod;
import alpvax.mod.classmodcore.network.packets.ClassChangePacket;

public class GuiClassSelect extends GuiScreen
{
	private static int padX = 10;
	private static int padY = 10;
	
	/** Button width in pixels */
	protected static int btnWidth = 64;

	/** Button height in pixels */
	protected static int btnHeight = 100;
	
	/** Page area width in pixels */
	public int pageAreaWidth;
	/** Page area height in pixels */
	public int pageAreaHeight;
	/** The horizontal start point of the page area. */
	public int pageAreaX;
	/** The vertical start point of the page area. */
	public int pageAreaY;
	
	private List<Page> pages = new ArrayList<Page>();
	private int currentPage = 0;

	private int maxHorz;
	private int maxVert;

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	@Override
	public void initGui()
	{
		int i = (int)((float)width / 3F);
		int j = height / 2 + 60;
		buttonList.add(new GuiButton(1, i, j, 20, 20, I18n.format("classmodcore.gui.prev")));
		buttonList.add(new GuiButton(2, width - i, j, 20, 20, I18n.format("classmodcore.gui.next")));

		pageAreaWidth = 2 * (int)((float)width * 0.4F);//Ensures even spacing both sides (realistically about 80% of screen width)
		pageAreaY = height / 10;
		pageAreaHeight = j - pageAreaY - 2 * padY;
		pageAreaX = pageAreaWidth / 2;

		maxHorz = (pageAreaWidth + padX) / (btnWidth + padX);
		maxVert = (pageAreaHeight + padY) / (btnHeight + padY);
		
		if(pages.isEmpty())
		{
			List<IPlayerClass> classes = PlayerClassRegistry.availableClassesForGUI(this.mc.thePlayer);
			i = 0;
			j = maxVert * maxHorz;
			while(!classes.isEmpty())
			{
				int start = i * j++;//Increment i after using value
				pages.add(new Page(classes.subList(start, Math.min(start + i, classes.size()))));
			}
		}
		changePage(currentPage);
	}
	
	private void changePage(int pageNo)
	{
		if(pageNo != currentPage)
		{
			//Clear all ClassButtons, leave the rest
			buttonList.subList(3, buttonList.size()).clear();
			
			//Enable/disable the "prev" button
			((GuiButton)buttonList.get(1)).enabled = pageNo > 0;
			//Enable/disable the "next" button
			((GuiButton)buttonList.get(2)).enabled = pageNo < pages.size() - 1;
			
			currentPage = pageNo;
		}
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		this.drawDefaultBackground();
		for(int i = 0; i < buttonList.size(); ++i)
		{
			((GuiButton)buttonList.get(i)).drawButton(mc, mouseX, mouseY);
		}
		GL11.glPushMatrix();
		drawCenteredString(fontRendererObj, I18n.format("classmodcore.gui.selectclass"), width / 2, pageAreaY - padY, 16777215);
		GL11.glPopMatrix();
		pages.get(currentPage).draw(mouseX, mouseY);
	}
	
	@Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		for(GuiClassButton btn : pages.get(currentPage).buttons)
		{
			if(btn.mousePressed(mc, mouseX, mouseY))
			{
				ClassMod.packetHandler.sendToServer(new ClassChangePacket(mc.thePlayer, btn.playerclass));
			}
		}
    }

	/**
	 * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
	 */
	@Override
	protected void actionPerformed(GuiButton button)
	{
		switch(button.id)
		{
			case 0://Done, select class
				break;
			case 1://Prev
				changePage(currentPage - 1);
				break;
			case 2://Next
				changePage(currentPage + 1);
				break;
		}
	}

	/*private int getNumForPage()
	{
		return Math.min(PlayerClassRegistry.allowedClasses.size() - page * selectGUIMaxC * selectGUIMaxR, selectGUIMaxC * selectGUIMaxR);
	}

	private int getNumRows()
	{
		return (int)Math.ceil((float)getNumForPage() / (float)selectGUIMaxC);
	}

	private int getNumForRow(int row)
	{
		return Math.min(getNumForPage() - row * selectGUIMaxC, selectGUIMaxC);
	}*/

	/*private void makeClassButton(int currentPage, int row, int column, int startX, int startY)
	{
		int i = startX + (62 + xPadding) * column;
		int j = startY + (96 + yPadding) * row;
		IPlayerClass playerclass = PlayerClassRegistry.getPlayerClass(PlayerClassRegistry.allowedClasses.get(currentPage * selectGUIMaxC * selectGUIMaxR + row * selectGUIMaxC + column));
		buttonList.add(new GuiClassButton(i, j, playerclass));
	}*/
	
	private class Page
	{
		private final List<IPlayerClass> pageClasses;
		private List<GuiClassButton> buttons = new ArrayList<GuiClassButton>();
		
		private Page(List<IPlayerClass> classes)
		{
			pageClasses = classes;
			int i1 = 1 + (pageClasses.size() / maxHorz);
			
			int k = 0;
			for(int i = 0; i < i1; i++)
			{//For each row
				int j1 = numForRow(i);
				int gapY = (width - (j1 * btnWidth)) / j1;
				for(int j = 0; j < j1; j++)
				{
					int gapX = (height - (i1 * btnHeight)) / i1;
					buttons.add(new GuiClassButton(i * j1 + j, i * (btnWidth + gapX), j * (btnWidth + gapY), pageClasses.get(k++)));
				}
			}
		}
		
		public void draw(int mouseX, int mouseY)
		{
			for(GuiClassButton btn : buttons)
			{
				btn.draw(mc, mouseX, mouseY);
			}
		}
		
		public int numForRow(int row)
		{
			return pageClasses.size() - (row * maxHorz);
		}
	}

	public class GuiClassButton extends GuiButton
	{
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
			/*TODO: textures
			drawTexturedModalRect(xPosition + 8, yPosition + 12, 0, 0, 46, 73);//Class image
			mc.getTextureManager().bindTexture(ClassUtil.classGUIMain);
			drawTexturedModalRect(xPosition, yPosition, 0, 0, 62, 96);//Border
			if(isMouseOver())
			{
				drawTexturedModalRect(xPosition - 10, yPosition - 10, 62, 0, 86, 118);//SelectionBox
			}
			*/
			FontRenderer fr = mc.fontRendererObj;
			String s = playerclass.getDisplayName();
			fr.drawStringWithShadow(s, xPosition + width / 2 - fr.getStringWidth(s) / 2, yPosition + 85, 16777215);
		}
	}
}
