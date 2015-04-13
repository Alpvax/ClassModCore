package alpvax.mod.classmodcore.client;

import static alpvax.mod.classmodcore.core.ClassMod.selectGUIMaxC;
import static alpvax.mod.classmodcore.core.ClassMod.selectGUIMaxR;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import org.lwjgl.opengl.GL11;

import alpvax.mod.classmodcore.classes.PlayerClassRegistry;
import alpvax.mod.classmodcore.core.ClassUtil;
import alpvax.mod.classmodcore.network.packet.ClassSelectPacket;
import alpvax.mod.classmodcore.playerclass.PlayerClass;

public class GuiClassSelect extends GuiScreen
{
	private static int xPadding = 10;
	private static int yPadding = 10;
	private int page;
	private int maxPages = 1;
	
	/**
     * Adds the buttons (and other controls) to the screen in question.
     */
	@Override
    public void initGui()
    {
        this.buttonList.clear();
        int i = PlayerClassRegistry.allowedClasses.size();
        if (i > selectGUIMaxC * selectGUIMaxR)
        {
            buttonList.add(new GuiButton(0, width / 2 - (62 + xPadding), height / 2 + 58, 20, 20, "<"));
            buttonList.add(new GuiButton(1, width / 2 + (62 + xPadding), height / 2 + 58, 20, 20, ">"));
            maxPages = (int)Math.floor(i / selectGUIMaxC * selectGUIMaxR);
        }
        for(int i1 = 0; i1 < maxPages; i1++)
        {
	        int j = getNumRows();
	    	int startY = height / 2 - (j * 96 + (j - 1) * yPadding) / 2;
	        for(int j1 = 0; j1 < j; j1++)
	        {
	        	int k = getNumForRow(j1);
	        	int startX = width / 2 - (k * 62 + (k - 1) * xPadding) / 2;
	        	for(int k1 = 0; k1 < k; k1++)
	        	{
	        		makeClassButton(i1, j1, k1, startX, startY);
	        	}
	        }
        }
    }
	
	/**
     * Draws the screen and all the components in it.
     */
	@Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
		this.drawDefaultBackground();
        for (int var4 = 0; var4 < buttonList.size(); ++var4)
        {
        	if(this.buttonList.get(var4) instanceof GuiButton)
        	{
	            GuiButton var5 = (GuiButton)this.buttonList.get(var4);
	            var5.drawButton(this.mc, mouseX, mouseY);
        	}
        	else
        	{
        		GuiClassButton var5 = (GuiClassButton)this.buttonList.get(var4);
                var5.undraw();
        	}
        }
        int i = getNumRows();
    	int startY = height / 2 - (i * 96 + (i - 1) * yPadding) / 2;
        GL11.glPushMatrix();
        this.drawCenteredString(this.fontRendererObj, "Select your class", this.width / 2, startY - yPadding, 16777215);
        GL11.glPopMatrix();
        for(int i1 = 0; i1 < i; i1++)
        {
        	int j = getNumForRow(i1);
        	int startX = width / 2 - (j * 62 + (j - 1) * xPadding) / 2;
        	for(int j1 = 0; j1 < j; j1++)
        	{
                GuiClassButton var5 = (GuiClassButton)this.buttonList.get(page * selectGUIMaxC * selectGUIMaxR + i1 * selectGUIMaxC + j1);// + maxPages > 1 ? 2 : 0);
                var5.draw(this.mc, mouseX, mouseY);
        	}
        }
    }

	/**
     * Called when the mouse is clicked.
     */
	@Override
    protected void mouseClicked(int par1, int par2, int par3)
    {
        if (par3 == 0)
        {
            for (int var4 = 0; var4 < this.buttonList.size(); ++var4)
            {
            	if(buttonList.get(var4) instanceof GuiButton)
            	{
	                GuiButton var5 = (GuiButton)this.buttonList.get(var4);
	
	                if (var5.mousePressed(this.mc, par1, par2))
	                {
	                    this.mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);
	                    this.actionPerformed(var5);
	                }
            	}
            	else
            	{
            		GuiClassButton var5 = (GuiClassButton)this.buttonList.get(var4);

	                if(var5.mousePressed(this.mc, par1, par2))
	                {
	                	PacketDispatcher.sendPacketToServer(new ClassSelectPacket(mc.thePlayer, var5.playerclass).makePacket());
	                    //PlayerClass.setPlayerClass(mc.thePlayer, var5.playerclass);
	                    System.out.println(var5.playerclass);
	                    mc.setIngameFocus();
	        	        //PlayerClass.startPowerDelay();
	                }
            	}
            }
        }
    }
	
	/**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
	@Override
    protected void actionPerformed(GuiButton par1GuiButton)
    {
        switch (par1GuiButton.id)
        {
            case 0:
                page = Math.max(page - 1, 0);
                break;
            case 1:
                page = Math.min(page + 1, maxPages);
                break;
        }
        initGui();
    }
	
	private int getNumForPage()
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
	}
	
	private void makeClassButton(int currentPage, int row, int column, int startX, int startY)
	{
		int i = startX + (62 + xPadding) * column;
		int j = startY + (96 + yPadding) * row;
		PlayerClass playerclass = PlayerClassRegistry.getPlayerClass(PlayerClassRegistry.allowedClasses.get(currentPage * selectGUIMaxC * selectGUIMaxR + row * selectGUIMaxC + column));
		buttonList.add(new GuiClassButton(i, j, playerclass));
	}
	
	public class GuiClassButton extends Gui
	{
		/** Button width in pixels */
	    protected int width = 62;

	    /** Button height in pixels */
	    protected int height = 96;

	    /** The x position of this control. */
	    public int xPosition;

	    /** The y position of this control. */
	    public int yPosition;
	    
	    /** Is not drawn */
	    private boolean hidden = true;
	    
	    /** The y position of this control. */
	    public PlayerClass playerclass;

		public GuiClassButton(int par1, int par2, PlayerClass par3PlayerClass)
		{
			xPosition = par1;
			yPosition = par2;
			playerclass = par3PlayerClass;
		}

	    /**
	     * Returns 0 if the button is disabled, 1 if the mouse is NOT hovering over this button and 2 if it IS hovering over
	     * this button.
	     */
	    protected int getHoverState(boolean par1)
	    {
	        return par1 ? 2 : 1;
	    }
	    
	    /**
	     * Returns true if the mouse has been pressed on this control. Equivalent of MouseListener.mousePressed(MouseEvent
	     * e).
	     */
	    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
	    {
	        return !hidden && mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
	    }
	    
	    public void draw(Minecraft mc, int mouseX, int mouseY)
	    {
	    	hidden = false;
	        mc.getTextureManager().bindTexture(playerclass.getIcon());
            drawTexturedModalRect(xPosition + 8, yPosition + 12, 0, 0, 46, 73);
	    	mc.getTextureManager().bindTexture(ClassUtil.classGUIMain);
	        drawTexturedModalRect(xPosition, yPosition, 0, 0, 62, 96);
            boolean flag = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
	        if(flag)
	        {
	            drawTexturedModalRect(xPosition - 10, yPosition - 10, 62, 0, 86, 118);
	        }
	        FontRenderer fr = mc.fontRendererObj;
	        String s = playerclass.getDisplayName();
	        fr.drawStringWithShadow(s, xPosition + width / 2 - fr.getStringWidth(s) / 2, yPosition + 85, 16777215);
	    }
	    
	    public void undraw()
	    {
	    	hidden = true;
	    }
	}
}
