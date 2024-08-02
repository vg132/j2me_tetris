package com.vgsoftware.j2metetris;

import javax.microedition.rms.RecordStore;

public class Game
{
	public static int STATUS_MAIN_MENU=10;
	public static int STATUS_RUNNING=11;
	public static int STATUS_PAUSED=12;
	public static int STATUS_GAME_OVER=13;
	public static int STATUS_EXIT=14;
	public static int STATUS_SAVE=15;

	private Player player=null;
	private int status=0;
	private int menuPos=0;
	private int pauseMenuPos=0;

	public Game()
	{
	}

	public void addPlayer(Player player)
	{
		this.player=player;
	}

	public Player getPlayer()
	{
		return(player);
	}

	public void setStatus(int status)
	{
		this.status=status;
	}

	public int getStatus()
	{
		return(status);
	}

	public int getMenuPos()
	{
		return menuPos;
	}

	public void setMenuPos(int menuPos)
	{
		this.menuPos=menuPos;
	}

	public int getPauseMenuPos()
	{
		return pauseMenuPos;
	}

	public void setPauseMenuPos(int pauseMenuPos)
	{
		this.pauseMenuPos=pauseMenuPos;
	}
	
	public void resumeGame()
	{
		try
		{
			RecordStore rs=RecordStore.openRecordStore("j2me_tetris",false);
			byte[] data=rs.getRecord(1);
			if(getPlayer().restoreState(new String(data)))
			{
				data=rs.getRecord(2);
				Field f=new Field();
				f.restoreState(new String(data));
				getPlayer().setField(f);
				setStatus(Game.STATUS_PAUSED);
				return;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		setStatus(Game.STATUS_MAIN_MENU);
	}

	public void saveGame()
	{
		try
		{
			RecordStore rs=RecordStore.openRecordStore("j2me_tetris",true);
			byte[] data=getPlayer().getState().getBytes();
			if(rs.getNumRecords()>0)
				rs.setRecord(1,data,0,data.length);
			else
				rs.addRecord(data,0,data.length);
			data=getPlayer().getField().getState().getBytes();
			if(rs.getNumRecords()>1)
				rs.setRecord(2,data,0,data.length);
			else
				rs.addRecord(data,0,data.length);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
