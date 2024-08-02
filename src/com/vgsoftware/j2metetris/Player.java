package com.vgsoftware.j2metetris;

public class Player
{
	private int points=0;
	private int level=0;
	private boolean showNext=true;
	private boolean paused=false;
	private Field field=null;
	private int moveSize=0;
	private long lastMove=0;
	private int[] lines=new int[4];
	private J2MEControl control=new J2MEControl();
	
	public Player()
	{
		points=0;
		level=0;
		for(int i=0;i<4;i++)
			lines[i]=0;
	  moveSize=50;
	  showNext=true;
		field=null;
	}

	public Field getField()
	{
		return(field);
	}

	public void setField(Field field)
	{
		this.field=field;
	}

	public long getLastMove()
	{
		return(lastMove);
	}

	public void setLastMove(long lastMove)
	{
		this.lastMove=lastMove;
	}

	public int getLevel()
	{
		return(level);
	}

	public void setLines(int[] lines)
	{
		this.lines=lines;
	}

	public int getMoveSize()
	{
		return(moveSize);
	}

	public void setMoveSize(int moveSize)
	{
		this.moveSize=moveSize;
	}

	public boolean isPaused()
	{
		return(paused);
	}

	public void setPaused(boolean paused)
	{
		this.paused=paused;
	}

	public int getPoints()
	{
		return(points);
	}

	public void setPoints(int points)
	{
		this.points=points;
	}

	public boolean isShowNext()
	{
		return(showNext);
	}

	public void setShowNext(boolean showNext)
	{
		this.showNext=showNext;
	}
	
	public void reset()
	{
		field=new Field();
	  points=0;
	  level=0;
		lastMove=0;
		for(int i=0;i<4;i++)
			lines[i]=0;
	}

	public void deleteField()
	{
		field=null;
	}
	
	public void setRows(int rows)
	{
		lines[rows-1]++;
	}
	
	public void addPoints(int points)
	{
		this.points+=points;
	}
	
	public void removePoints(int points)
	{
		this.points-=points;
	}
	
	public int getLines(int index)
	{
		if(index<4)
			return(lines[index]);
		else
			return(lines[0]+(lines[1]*2)+(lines[2]*3)+(lines[3]*4));
	}
	
	void setLevel(int level)
	{
	  this.level=level;
	  if(level==0)
			moveSize=50;
	  else if(level==1)
			moveSize=40;
	  else if(level==2)
			moveSize=35;
	  else if(level==3)
			moveSize=30;
	  else if(level==4)
			moveSize=25;
	  else if(level==5)
			moveSize=20;
	  else if(level==6)
			moveSize=17;
	  else if(level==7)
			moveSize=15;
	  else if(level==8)
			moveSize=12;
	  else if(level==9)
			moveSize=10;
		else if(level>9)
			moveSize=6;
	}

	public J2MEControl getControl()
	{
		return control;
	}

	public void setControl(J2MEControl control)
	{
		this.control=control;
	}

	public String getState()
	{
		StringBuffer sb=new StringBuffer();
		sb.append(level);
		sb.append("|");
		sb.append(points);
		sb.append("|");
		sb.append(lines[0]);
		sb.append("|");
		sb.append(lines[1]);
		sb.append("|");
		sb.append(lines[2]);
		sb.append("|");
		sb.append(lines[3]);
		sb.append("|");
		sb.append(moveSize);
		return(sb.toString());
	}

	public boolean restoreState(String state)
	{
		try
		{
			level=Integer.parseInt(state.substring(0,state.indexOf("|")));
			state=state.substring(state.indexOf("|")+1);
			points=Integer.parseInt(state.substring(0,state.indexOf("|")));
			state=state.substring(state.indexOf("|")+1);
			lines[0]=Integer.parseInt(state.substring(0,state.indexOf("|")));
			state=state.substring(state.indexOf("|")+1);
			lines[1]=Integer.parseInt(state.substring(0,state.indexOf("|")));
			state=state.substring(state.indexOf("|")+1);
			lines[2]=Integer.parseInt(state.substring(0,state.indexOf("|")));
			state=state.substring(state.indexOf("|")+1);
			lines[3]=Integer.parseInt(state.substring(0,state.indexOf("|")));
			state=state.substring(state.indexOf("|")+1);
			moveSize=Integer.parseInt(state);
			return(true);
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return(false);
	}
}
