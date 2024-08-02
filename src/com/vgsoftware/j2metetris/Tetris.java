package com.vgsoftware.j2metetris;

import javax.microedition.lcdui.Display;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

public class Tetris extends MIDlet
{
	private Display display=null;

	public Tetris()
	{
	}

	protected void startApp()
	throws MIDletStateChangeException
	{
		display=Display.getDisplay(this);
		TetrisCanvas tetrisCanvas=new TetrisCanvas(this);
		tetrisCanvas.start();
		display.setCurrent(tetrisCanvas);
	}

	protected void destroyApp(boolean unconditional)
	throws MIDletStateChangeException
	{
	}

	protected void pauseApp()
	{
	}
	
	public Display getDisplay()
	{
		return(display);
	}
	
	public void exit()
	{
		try
		{
			System.gc();
			destroyApp(true);
			notifyDestroyed();
		}
		catch(MIDletStateChangeException msce)
		{
			msce.printStackTrace();
		}
	}
}
