package alpvax.classmodcore.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

import org.lwjgl.opengl.GL11;

import alpvax.classmodcore.api.classes.IPlayerClass;
import alpvax.classmodcore.api.classes.PlayerClassRegistry;
import alpvax.classmodcore.core.ClassMod;
import alpvax.classmodcore.network.packets.ClassChangePacket;


public class GuiClassSelect extends GuiScreen
{
	private static int minPadX = 10;
	private static int minPadY = 10;

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

	private int maxRows;
	private int maxColumns;

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void initGui()
	{
		int i = (int)(width / 3F);
		int j = height / 2 + 60;
		String s = I18n.format("classmodcore.gui.select");
		buttonList.add(new GuiButton(0, (width - (fontRendererObj.getStringWidth(s) + 6)) / 2, j, fontRendererObj.getStringWidth(s) + 12, 20, s));
		buttonList.add(new GuiButton(1, i - 20, j, 20, 20, I18n.format("classmodcore.gui.prev")));
		buttonList.add(new GuiButton(2, width - i, j, 20, 20, I18n.format("classmodcore.gui.next")));

		pageAreaWidth = 2 * (int)(width * 0.4F);// Ensures even spacing both sides (realistically about 80% of screen width)
		pageAreaX = (width - pageAreaWidth) / 2;
		pageAreaY = height / 10;
		pageAreaHeight = j - pageAreaY - 2 * minPadY;

		maxRows = (pageAreaHeight - minPadY) / (GuiClassButton.btnHeight + minPadY);
		maxColumns = (pageAreaWidth - minPadX) / (GuiClassButton.btnWidth + minPadX);

		List<IPlayerClass> classes = PlayerClassRegistry.availableClassesForGUI(mc.thePlayer);
		i = maxRows * maxColumns;
		j = 0;
		while(j < classes.size())
		{
			pages.add(new Page(classes.subList(j, Math.min(j + i, classes.size()))));
			j = j + i;
		}
		changePage(currentPage);
	}

	private void changePage(int pageNo)
	{
		if(pageNo < 0 || pageNo > pages.size() - 1)
		{
			return;
		}
		// Clear all ClassButtons, leave the rest
		//buttonList.subList(3, buttonList.size()).clear();

		// Enable/disable the "prev" button
		((GuiButton)buttonList.get(1)).enabled = pageNo > 0;
		// Enable/disable the "next" button
		((GuiButton)buttonList.get(2)).enabled = pageNo < pages.size() - 1;

		currentPage = pageNo;
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		drawDefaultBackground();
		for(int i = 0; i < buttonList.size(); ++i)
		{
			((GuiButton)buttonList.get(i)).drawButton(mc, mouseX, mouseY);
		}
		GL11.glPushMatrix();
		drawCenteredString(fontRendererObj, I18n.format("classmodcore.gui.selectclass"), width / 2, pageAreaY - minPadY, 16777215);
		GL11.glPopMatrix();
		pages.get(currentPage).draw(mouseX, mouseY);
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		super.mouseClicked(mouseX, mouseY, mouseButton);
		for(GuiClassButton btn : pages.get(currentPage).buttons)
		{
			if(mouseButton == 0 && btn.mousePressed(mc, mouseX, mouseY))
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
			case 0:// Done, select class
				mc.setIngameFocus();
				break;
			case 1:// Prev
				changePage(currentPage - 1);
				break;
			case 2:// Next
				changePage(currentPage + 1);
				break;
		}
	}

	/*
	 * private int getNumForPage() { return Math.min(PlayerClassRegistry.allowedClasses.size() - page * selectGUIMaxC * selectGUIMaxR, selectGUIMaxC * selectGUIMaxR); }
	 * 
	 * private int getNumRows() { return (int)Math.ceil((float)getNumForPage() / (float)selectGUIMaxC); }
	 * 
	 * private int getNumForRow(int row) { return Math.min(getNumForPage() - row * selectGUIMaxC, selectGUIMaxC); }
	 */

	/*
	 * private void makeClassButton(int currentPage, int row, int column, int startX, int startY) { int i = startX + (62 + xPadding) * column; int j = startY + (96 + yPadding) * row; IPlayerClass playerclass = PlayerClassRegistry.getPlayerClass(PlayerClassRegistry.allowedClasses.get(currentPage * selectGUIMaxC * selectGUIMaxR + row * selectGUIMaxC + column)); buttonList.add(new GuiClassButton(i, j, playerclass)); }
	 */

	private class Page
	{
		private final List<IPlayerClass> pageClasses;
		private List<GuiClassButton> buttons = new ArrayList<GuiClassButton>();

		private Page(List<IPlayerClass> classes)
		{
			System.err.printf("Total screen size: %dx%d; Page area: %d+%dx%d+%d;%n", width, height, pageAreaWidth, pageAreaX, pageAreaHeight, pageAreaY);
			pageClasses = classes;
			int numR = (maxColumns + pageClasses.size() - 1) / maxColumns;
			int gapY = (pageAreaHeight - numR * GuiClassButton.btnHeight) / (numR + 1);
			int i = 0;
			for(int rowNum = 0; rowNum < numR; rowNum++)
			{//For each row
				int numC = numForRow(rowNum);
				int gapX = (pageAreaWidth - numC * GuiClassButton.btnWidth) / (numC + 1);
				for(int colNum = 0; colNum < numC; colNum++)
				{//For each button in row
					addButton(i++, colNum, rowNum, gapX, gapY);//Use index before incrementing
				}
			}
		}

		private void addButton(int id, int colNum, int rowNum, int xGap, int yGap)
		{
			buttons.add(new GuiClassButton(id, pageAreaX + xGap + colNum * (GuiClassButton.btnWidth + xGap), pageAreaY + yGap + rowNum * (GuiClassButton.btnHeight + yGap), pageClasses.get(id)));
			System.err.printf("Adding button (%1$s) at: %2$d,%3$d {%4$d + %5$d + %6$d(%7$d + %5$d), %8$d + %9$d + %10$d(%11$d + %9$d)};%n", pageClasses.get(id).getDisplayName(), buttons.get(id).xPosition, buttons.get(id).yPosition, pageAreaX, xGap, colNum, GuiClassButton.btnWidth, pageAreaY, yGap, rowNum, GuiClassButton.btnHeight);//XXX
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
			return Math.min(maxColumns, pageClasses.size() - row * maxRows);
		}
	}
}
