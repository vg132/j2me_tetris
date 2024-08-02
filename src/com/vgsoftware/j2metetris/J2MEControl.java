package com.vgsoftware.j2metetris;

import javax.microedition.lcdui.game.GameCanvas;

public class J2MEControl
{
	public long up=0;
	public long down=0;
	public long left=0;
	public long right=0;
	public long fire=0;
	public long currentNr=0;
	public int keyState=0;

	public boolean Left(int delay)
	{
		if(((keyState & GameCanvas.LEFT_PRESSED)!=0)&&((left+delay)<currentNr))
		{
			left=currentNr;
			return(true);
		}
		else if((keyState & GameCanvas.LEFT_PRESSED)==0)
		{
			left=0;
		}
		return(false);
	}

	public boolean Right(int delay)
	{
		if(((keyState & GameCanvas.RIGHT_PRESSED)!=0)&&((right+delay)<currentNr))
		{
			right=currentNr;
			return(true);
		}
		else if((keyState & GameCanvas.RIGHT_PRESSED)==0)
		{
			right=0;
		}
		return(false);
	}

	public boolean Up(int delay)
	{
		if(((keyState & GameCanvas.UP_PRESSED)!=0)&&((up+delay)<currentNr))
		{
			up=currentNr;
			return(true);
		}
		else if((keyState & GameCanvas.UP_PRESSED)==0)
		{
			up=0;
		}
		return(false);
	}

	public boolean Down(int delay)
	{
		if(((keyState & GameCanvas.DOWN_PRESSED)!=0)&&((down+delay)<currentNr))
		{
			down=currentNr;
			return(true);
		}
		else if((keyState & GameCanvas.DOWN_PRESSED)==0)
		{
			down=0;
		}
		return(false);
	}

	public boolean Fire(int delay)
	{
		if(((keyState & GameCanvas.FIRE_PRESSED)!=0)&&((fire+delay)<currentNr))
		{
			fire=currentNr;
			return(true);
		}
		else if((keyState & GameCanvas.FIRE_PRESSED)==0)
		{
			fire=0;
		}
		return(false);
	}

	public void init(long currentNr, int keyState)
	{
		this.currentNr=currentNr;
		this.keyState=keyState;
	}
}
