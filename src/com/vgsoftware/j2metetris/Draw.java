package com.vgsoftware.j2metetris;

import java.io.IOException;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class Draw
{
	private Game game=null;
	private int screenWidth=0;
	private int screenHeight=0;
	private Image logo=null;
	private Image background=null;

	private void drawField(Graphics g, Field field)
	{
		int x;
		int y;
		int type;
		int diff=field.getHeight()-field.getVisibleHeight();
		for(y=diff;y<field.getHeight();y++)
		{
			for(x=0;x<field.getWidth();x++)
			{
				type=field.getBlock(x,y);
				if((LOWORD(type)!=Field.BLOCK_COLOR_NONE)&&(HIWORD(type)!=Field.BLOCK_REMOVE))
				{
					drawBlock(g,x,y-diff,field.getColor(LOWORD(type)),field);
				}
				else if((field.getStatus()==Field.STATUS_REMOVE_LINE)&&(HIWORD(type)==Field.BLOCK_REMOVE))
				{
					if(field.getFlashCounter()>10)
						drawBlock(g,x,y-diff,field.getColor(LOWORD(type)),field);
				}
			}
		}
		if(field.getStatus()==Field.STATUS_REMOVE_LINE)
		{
			field.setFlashCounter(field.getFlashCounter()+1);
			if(field.getFlashCounter()>=20)
				field.resetFlashCounter();
		}
	}

	private void drawInfo(Graphics g)
	{
		g.setColor(0x00000000);
		g.drawString("Lines",2,10,Graphics.TOP|Graphics.LEFT);
		g.drawString(""+(game.getPlayer().getLines(4)),2,25,Graphics.TOP|Graphics.LEFT);
		g.drawString("Level",2,40,Graphics.TOP|Graphics.LEFT);
		g.drawString(""+(game.getPlayer().getLevel()+1),2,55,Graphics.TOP|Graphics.LEFT);
		g.drawString("Points",2,70,Graphics.TOP|Graphics.LEFT);
		g.drawString(""+game.getPlayer().getPoints(),2,85,Graphics.TOP|Graphics.LEFT);
	}

	private void drawTitle(Graphics g)
	{
		g.setColor(0x00000000);
		g.drawImage(logo,0,0,Graphics.TOP|Graphics.LEFT);
		if(game.getMenuPos()==0)
			writeCenter(g,">Start<",screenHeight/2-10);
		else
			writeCenter(g,"Start",screenHeight/2-10);
		if(game.getMenuPos()==1)
			writeCenter(g,">Resume<",screenHeight/2+5);
		else
			writeCenter(g,"Resume",screenHeight/2+5);
		if(game.getMenuPos()==2)
			writeCenter(g,">Exit<",screenHeight/2+20);
		else
			writeCenter(g,"Exit",screenHeight/2+20);

		writeCenter(g,"(C) VG Software 2005",135);
		writeCenter(g,"http://www.vgsoftware.com/",150);
	}

	private void writeCenter(Graphics g, String text, int y)
	{
		g.drawString(text,screenWidth/2-(g.getFont().stringWidth(text)/2),y,Graphics.TOP|Graphics.LEFT);
	}
	
	private void drawPause(Graphics g)
	{
		g.drawImage(background,0,0,Graphics.TOP|Graphics.LEFT);
		drawInfo(g);
		drawNext(g,142,2);
		g.setColor(0x00000000);
		g.drawRect((screenWidth/2)-((game.getPlayer().getField().getWidth()*8)/2),(screenHeight/2)-((game.getPlayer().getField().getVisibleHeight()*8)/2),game.getPlayer().getField().getWidth()*8,game.getPlayer().getField().getVisibleHeight()*8);
		writeCenter(g,"Paused",screenHeight/2-20);
		if(game.getPauseMenuPos()==0)
			writeCenter(g,">Resume<",screenHeight/2-5);
		else
			writeCenter(g,"Resume",screenHeight/2-5);
		if(game.getPauseMenuPos()==1)
			writeCenter(g,">Quit<",screenHeight/2+10);
		else
			writeCenter(g,"Quit",screenHeight/2+10);
	}

	private void drawSave(Graphics g)
	{
		g.drawImage(background,0,0,Graphics.TOP|Graphics.LEFT);
		drawInfo(g);
		drawNext(g,142,2);
		g.setColor(0x00000000);
		g.drawRect((screenWidth/2)-((game.getPlayer().getField().getWidth()*8)/2),(screenHeight/2)-((game.getPlayer().getField().getVisibleHeight()*8)/2),game.getPlayer().getField().getWidth()*8,game.getPlayer().getField().getVisibleHeight()*8);
		writeCenter(g,"Save state",screenHeight/2-20);
		if(game.getPauseMenuPos()==0)
			writeCenter(g,">Yes<",screenHeight/2-5);
		else
			writeCenter(g,"Yes",screenHeight/2-5);
		if(game.getPauseMenuPos()==1)
			writeCenter(g,">No<",screenHeight/2+10);
		else
			writeCenter(g,"No",screenHeight/2+10);		
	}

	private void drawGameOver(Graphics g)
	{
		g.drawImage(background,0,0,Graphics.TOP|Graphics.LEFT);
		drawInfo(g);
		g.setColor(0x00000000);
		writeCenter(g,"Game Over",screenHeight/2);
	}

	private void drawGame(Graphics g)
	{
		g.drawImage(background,0,0,Graphics.TOP|Graphics.LEFT);
		drawInfo(g);
		drawNext(g,142,2);
		g.setColor(0x00000000);
		g.drawRect((screenWidth/2)-((game.getPlayer().getField().getWidth()*8)/2),(screenHeight/2)-((game.getPlayer().getField().getVisibleHeight()*8)/2),game.getPlayer().getField().getWidth()*8,game.getPlayer().getField().getVisibleHeight()*8);
		drawField(g,game.getPlayer().getField());
	}

	private void drawNext(Graphics g, int baseX, int baseY)
	{
		g.setColor(0x00000000);
		g.drawString("Next",baseX,baseY,Graphics.TOP|Graphics.LEFT);
		baseY+=15;
		int type=0;
		for(int y=0;y<4;y++)
		{
			for(int x=0;x<4;x++)
			{
				type=game.getPlayer().getField().getNextBlock(x,y);
				if(type!=Field.BLOCK_COLOR_NONE)
				{
					g.setColor(0x00000000);
					g.fillRect((x*8)+baseX,(y*8)+baseY,8,8);
					g.setColor(game.getPlayer().getField().getColor(type));
					g.fillRect((x*8)+baseX+1,(y*8)+baseY+1,6,6);
				}
			}
		}
	}

	private void drawBlock(Graphics g, int x, int y, int color, Field field)
	{
		x=(x*8)+(screenWidth/2)-((field.getWidth()*8)/2);
		y=(y*8)+(screenHeight/2)-((field.getVisibleHeight()*8)/2);
		g.setColor(0x00000000);
		g.fillRect(x,y,8,8);
		g.setColor(color);
		g.fillRect(x+1,y+1,6,6);
	}

	private int LOWORD(int i)
	{
		return(i & 0xFFFF);
	}

	private int HIWORD(int i)
	{
		return(i >> 16);
	}

	public Draw(int height, int width)
	{
		game=null;

		screenWidth=width;
		screenHeight=height;

		try
		{
			background=Image.createImage("/graphics/background.png");
			logo=Image.createImage("/graphics/tetris.png");
		}
		catch(IOException io)
		{
			io.printStackTrace();
		}
	}

	public void setGame(Game game)
	{
		this.game=game;
	}

	public void drawScene(Graphics g)
	{
		if(game.getStatus()==Game.STATUS_MAIN_MENU)
			drawTitle(g);
		else if(game.getStatus()==Game.STATUS_RUNNING)
			drawGame(g);
		else if(game.getStatus()==Game.STATUS_GAME_OVER)
			drawGameOver(g);
		else if(game.getStatus()==Game.STATUS_PAUSED)
			drawPause(g);
		else if(game.getStatus()==Game.STATUS_SAVE)
			drawSave(g);
	}
}
