package com.vgsoftware.j2metetris;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.GameCanvas;

public class TetrisCanvas extends GameCanvas implements Runnable, CommandListener
{
	private Game game=null;
	private Draw draw=null;
	private Control control=null;

	private boolean playing=false;
	private long delay=0;
	private int currentX=0;
	private int currentY=0;
	private int width=0;
	private int height=0;
	private Tetris tetris=null;
	private Command btnPause=new Command("Pause",Command.OK,1);

	public TetrisCanvas(Tetris tetris)
	{
		super(true);
		this.tetris=tetris;
		width=getWidth();
		height=getHeight();
		currentX=width/2;
		currentY=height/2;
		delay=20;

		game=new Game();
		control=new Control();
		draw=new Draw(width,height);
		draw.setGame(game);
		game.addPlayer(new Player());
		game.setStatus(Game.STATUS_MAIN_MENU);
		this.setCommandListener(this);
	}

	public void start()
	{
		playing=true;
		new Thread(this).start();
	}

	public void run()
	{
		long frmctr=0;
		Graphics g=getGraphics();
		while(game.getStatus()!=Game.STATUS_EXIT)
		{
			frmctr++;
			control.Handle(game,getKeyStates(),frmctr);
			if(game.getStatus()==Game.STATUS_RUNNING)
			{
				this.addCommand(btnPause);
				if((game.getPlayer().getField().getStatus()==Field.STATUS_NORMAL)&&
					((game.getPlayer().getLastMove()+game.getPlayer().getMoveSize())<frmctr))
				{
					if(game.getPlayer().getField().move(Field.MOVE_DOWN)==false)
					{
						int rows=game.getPlayer().getField().checkFullRow();
						if(rows==0)
						{
							game.getPlayer().getField().newBlock();
							game.getPlayer().getField().clearPos();
							if(!game.getPlayer().getField().canMove(Field.MOVE_DOWN))
							{
								game.getPlayer().getField().put();
								game.setStatus(Game.STATUS_GAME_OVER);
							}
							game.getPlayer().getField().put();
						}
						else
						{
							switch(rows)
							{
								case 1:
									game.getPlayer().addPoints(100*((game.getPlayer().getLevel()/10)+1));
									break;
								case 2:
									game.getPlayer().addPoints(300*((game.getPlayer().getLevel()/10)+1));
									break;
								case 3:
									game.getPlayer().addPoints(1200*((game.getPlayer().getLevel()/10)+1));
									break;
								case 4:
									game.getPlayer().addPoints(5100*((game.getPlayer().getLevel()/10)+1));
									break;
							}
							game.getPlayer().setRows(rows);
							if(game.getPlayer().getLines(4)>=((game.getPlayer().getLevel()+1)*10))
								game.getPlayer().setLevel(game.getPlayer().getLevel()+1);
						}
					}
					game.getPlayer().setLastMove(frmctr);
				}
			}

			draw.drawScene(g);
			flushGraphics();
			try
			{
				Thread.sleep(delay);
			}
			catch(InterruptedException ie)
			{
			}
		}
		tetris.exit();
	}

	public void commandAction(Command command, Displayable disp)
	{
		if(command==btnPause)
		{
			game.setPauseMenuPos(0);
			game.setStatus(Game.STATUS_PAUSED);
		}
	}
	
	
}
