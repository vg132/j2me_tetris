package com.vgsoftware.j2metetris;

import java.util.Random;

public class Field
{
	public static int BLOCK_COLOR_DARKPINK=1;
	public static int BLOCK_COLOR_BROWN=2;
	public static int BLOCK_COLOR_PINK=3;
	public static int BLOCK_COLOR_YELLOW=4;
	public static int BLOCK_COLOR_RED=5;
	public static int BLOCK_COLOR_GREEN=6;
	public static int BLOCK_COLOR_BLUE=7;
	public static int BLOCK_COLOR_GRAY=8;
	public static int BLOCK_COLOR_NONE=9;

	public static int BLOCK_SHAPE_L=20;
	public static int BLOCK_SHAPE_RL=21;
	public static int BLOCK_SHAPE_Z=22;
	public static int BLOCK_SHAPE_RZ=23;
	public static int BLOCK_SHAPE_I=24;
	public static int BLOCK_SHAPE_T=25;
	public static int BLOCK_SHAPE_O=26;

	public static int MOVE_DOWN=60;
	public static int MOVE_UP=61;
	public static int MOVE_LEFT=62;
	public static int MOVE_RIGHT=63;
	public static int ROTATE_LEFT=64;
	public static int ROTATE_RIGHT=65;

	public static int STATUS_REMOVE_LINE=87;
	public static int STATUS_NORMAL=88;

	public static int BLOCK_REMOVE=160;
	public static int BLOCK_SOLID=161;
	
	public static int WIDTH=10;
	public static int HEIGHT=24;
	public static int VISIBLE_HEIGHT=20;
	
	private int[][] currentBlock=new int[4][4];
	private int[][] nextBlock=new int[4][4];
	private int[][] field=new int[WIDTH][HEIGHT];
	
	private Random rand=new Random();
	private int angle=0;
	private int blockType=0;
	private int blockX=0;
	private int blockY=0;
	private int next=-1;
	private int status=0;
	private int flashCounter=0;
	private int resets=0;
	private int nextColor=0;
	private int color=0;
	private int startHeight=0;
	
	public int getColor(int color)
	{
		switch(color)
		{
			case 1:
				return(0x00d000d0);
			case 2:
				return(0x00804818);
			case 3:
				return(0x00e68b00);
			case 4:
				return(0x00d0d000);
			case 5:
				return(0x007a0000);
			case 6:
				return(0x00009c00);
			case 7:
				return(0x00000094);
			case 8:
				return(0x00b9b9b9);
		}
		return(0);
	}
	
	private void clearBlock(int[][] block)
	{
		for(int x=0;x<4;x++)
		{
			for(int y=0;y<4;y++)
			{
				block[x][y]=BLOCK_COLOR_NONE;
			}
		}
	}
	
	private void removeFullRow(int row)
	{
		for(int y=row-1;y>=0;y--)
		{
			for(int x=0;x<10;x++)
				field[x][y+1]=field[x][y];
		}
	}
	
	private void setRemoveFullRow(int row)
	{
		for(int x=0;x<10;x++)
			field[x][row]=MAKELONG(field[x][row],BLOCK_REMOVE);
	}
	
	private int LOWORD(int i)
	{
		return(i & 0xFFFF);
	}
	
	private int HIWORD(int i)
	{
		return(i >> 16);
	}
	
	private int MAKELONG(int low, int high)
	{
		return((low & 0xFFFF) | ((high & 0xFFFF) << 16));
	}
	
	public void flip()
	{
		int found=-1;
		int block=4;
		int x=0;
		int y=0;
		clearPos();
		while((found==-1)&&(y<HEIGHT))
		{
			while((found==-1)&&(x<WIDTH))
			{
				if(field[x][y]!=LOWORD(BLOCK_COLOR_NONE))
				{
					found=y;
				}
				x++;
			};
			x=0;
			y++;
		};

		for(y=0;y<4;y++)
		{
			for(x=0;x<4;x++)
			{
				if(currentBlock[x][y]!=BLOCK_COLOR_NONE)
					block=y+1;
			}
		}
		if((found!=-1)&&(found!=(HEIGHT-1))&&(found>block+blockY))
		{
			int tmp=0;
			int bottom=HEIGHT-1;
			int stop=(HEIGHT-found)/2+found;
			for(;found<stop;found++)
			{
				for(x=0;x<WIDTH;x++)
				{
					tmp=field[x][found];
					field[x][found]=field[x][bottom];
					field[x][bottom]=tmp;
				}
				bottom--;
			}
		}
		put(); //Paint current block
	}

	public void reset()
	{
		for(int y=0;y<WIDTH;y++)
		{
			for(int x=0;x<HEIGHT;x++)
			{
				field[y][x]=MAKELONG(BLOCK_COLOR_NONE,0);
			}
		}
		next=-1;
		resets=0;
		startHeight=0;
		status=STATUS_NORMAL;
		newBlock();
	}

	public void setStartHeight(int startHeight)
	{
		int block=0;
		for(int y=HEIGHT-1;y>(HEIGHT-(startHeight+1));y--)
		{
			for(int x=0;x<WIDTH;x++)
			{
				block=rand.nextInt(30);
				if(block<7)
					field[x][y]=MAKELONG(block,0);
			}
		}
	}

	public int getStartHeight()
	{
		return(startHeight);
	}

	public void setFlashCounter(int flashCounter)
	{
		this.flashCounter=flashCounter;
	}

	public int getFlashCounter()
	{
		return(flashCounter);
	}

	public void resetFlashCounter()
	{
		flashCounter=0;
		resets++;
		if(resets>2)
		{
			resets=0;
			checkFullRow();
			setStatus(STATUS_NORMAL);
			newBlock();
			clearPos();
			put();
		}
	}

	public void setStatus(int status)
	{
		this.status=status;
	}

	public int getStatus()
	{
		return(status);
	}
	
	public void put()
	{
		for(int x=0;x<4;x++)
		{
			for(int y=0;y<4;y++)
			{
				if(currentBlock[x][y]!=BLOCK_COLOR_NONE)
					field[blockX+x][blockY+y]=MAKELONG(currentBlock[x][y],0);
			}
		}
	}

	public void clearPos()
	{
		for(int x=0;x<4;x++)
		{
			for(int y=0;y<4;y++)
			{
				if(currentBlock[x][y]!=BLOCK_COLOR_NONE)
					field[blockX+x][blockY+y]=MAKELONG(BLOCK_COLOR_NONE,0);
			}
		}
	}
	
	public boolean canMove(int direction)
	{
		clearPos();
		for(int y=0;y<4;y++)
		{
			for(int x=0;x<4;x++)
			{
				if(currentBlock[x][y]!=BLOCK_COLOR_NONE)
				{
					if((direction==MOVE_DOWN)&&(((blockY+y)>=(HEIGHT-1))||(LOWORD(field[blockX+x][blockY+y+1])!=BLOCK_COLOR_NONE)))
						return(false);
					else if((direction==MOVE_LEFT)&&(((x+blockX)<=0)||(LOWORD(field[blockX+x-1][blockY+y])!=BLOCK_COLOR_NONE)))
						return(false);
					else if((direction==MOVE_RIGHT)&&(((x+blockX)>=(WIDTH-1))||(LOWORD(field[blockX+x+1][blockY+y])!=BLOCK_COLOR_NONE)))
						return(false);
				}
			}
		}
		return(true);
	}

	public int getBlock(int x, int y)
	{
		return(field[x][y]);
	}
	
	public int getNextBlock(int x, int y)
	{
		return(nextBlock[x][y]);
	}
	
	public boolean move(int direction)
	{
		if(status!=STATUS_NORMAL)
			return(false);
		boolean retVal=true;
		clearPos();
		if(canMove(direction))
		{
			if(direction==MOVE_DOWN)
				blockY++;
			else if(direction==MOVE_LEFT)
				blockX--;
			else if(direction==MOVE_RIGHT)
				blockX++;
			else if(direction==MOVE_UP)
				blockY--;
		}
		else
		{
			retVal=false;
		}
		put();
		return(retVal);
	}
	
	public void rotate(int direction)
	{
		if(status!=STATUS_NORMAL)
			return;
		clearPos();
		boolean rotated=false;
		if(direction==ROTATE_LEFT)
			angle--;
		else if(direction==ROTATE_RIGHT)
			angle++;
		if(angle>3)
			angle=0;
		else if(angle<0)
			angle=3;
		if(blockType==BLOCK_SHAPE_I)
		{
			/*	#
					#
					#
					#
			*/
			if((angle==0)||(angle==2))
			{
				if(
					(blockY<=20)
					&&(LOWORD(field[blockX+3][blockY+2])==BLOCK_COLOR_NONE)
					&&(LOWORD(field[blockX+2][blockY+2])==BLOCK_COLOR_NONE)
					&&(LOWORD(field[blockX+1][blockY+2])==BLOCK_COLOR_NONE)
					&&(LOWORD(field[blockX+1][blockY+3])==BLOCK_COLOR_NONE)
					&&(LOWORD(field[blockX+1][blockY])==BLOCK_COLOR_NONE)
					)
				{
					rotated=true;
					clearBlock(currentBlock);
					currentBlock[1][0]=color;
					currentBlock[1][1]=color;
					currentBlock[1][2]=color;
					currentBlock[1][3]=color;
				}
			}
			/*####*/
			else if((angle==1)||(angle==3))
			{
				if(
						(blockX>=0)&&(blockX<=6)
						&&(LOWORD(field[blockX][blockY+1])==BLOCK_COLOR_NONE)
						&&(LOWORD(field[blockX+2][blockY+1])==BLOCK_COLOR_NONE)
						&&(LOWORD(field[blockX+3][blockY+1])==BLOCK_COLOR_NONE)
						&&(LOWORD(field[blockX+2][blockY+2])==BLOCK_COLOR_NONE)
						&&(LOWORD(field[blockX+3][blockY+2])==BLOCK_COLOR_NONE)
					)
				{
					rotated=true;
					clearBlock(currentBlock);
					currentBlock[0][1]=color;
					currentBlock[1][1]=color;
					currentBlock[2][1]=color;
					currentBlock[3][1]=color;
				}
			}
		}
		else if(blockType==BLOCK_SHAPE_T)
		{
			/**
				#[#]#
				  #
			*/
			if(angle==0)
			{
				if(((direction==ROTATE_RIGHT)
						&&(blockX>=0)&&(blockX<=7)&&(blockY<=21)
						&&(LOWORD(field[blockX][blockY])==BLOCK_COLOR_NONE)
						&&(LOWORD(field[blockX][blockY+2])==BLOCK_COLOR_NONE)
						&&(LOWORD(field[blockX+2][blockY+1])==BLOCK_COLOR_NONE)
						&&(LOWORD(field[blockX+2][blockY+2])==BLOCK_COLOR_NONE))
					||((direction==ROTATE_LEFT)
						&&(blockX>=0)&&(blockX<=7)&&(blockY<=21)
						&&(LOWORD(field[blockX+2][blockY])==BLOCK_COLOR_NONE)
						&&(LOWORD(field[blockX+2][blockY+2])==BLOCK_COLOR_NONE)
						&&(LOWORD(field[blockX][blockY+1])==BLOCK_COLOR_NONE)
						&&(LOWORD(field[blockX][blockY+2])==BLOCK_COLOR_NONE)))
				{
					rotated=true;
					clearBlock(currentBlock);
					currentBlock[0][1]=color;
					currentBlock[1][1]=color;
					currentBlock[2][1]=color;
					currentBlock[1][2]=color;
				}
			}
			/**
				 #
				[#]#
				 #
			*/
			else if(angle==1)
			{
				if(((direction==ROTATE_RIGHT)
						&&(blockX>=0)&&(blockX<=7)&&(blockY<=21)
						&&(LOWORD(field[blockX][blockY+2])==BLOCK_COLOR_NONE)
						&&(LOWORD(field[blockX+1][blockY])==BLOCK_COLOR_NONE)
						&&(LOWORD(field[blockX+2][blockY])==BLOCK_COLOR_NONE)
						&&(LOWORD(field[blockX+2][blockY+2])==BLOCK_COLOR_NONE))
					||((direction==ROTATE_LEFT)
						&&(blockX>=0)&&(blockX<=7)&&(blockY<=21)
						&&(LOWORD(field[blockX][blockY])==BLOCK_COLOR_NONE)
						&&(LOWORD(field[blockX+2][blockY])==BLOCK_COLOR_NONE)
						&&(LOWORD(field[blockX+1][blockY+2])==BLOCK_COLOR_NONE)
						&&(LOWORD(field[blockX+2][blockY+2])==BLOCK_COLOR_NONE)))
				{
					rotated=true;
					clearBlock(currentBlock);
					currentBlock[1][0]=color;
					currentBlock[1][1]=color;
					currentBlock[2][1]=color;
					currentBlock[1][2]=color;
				}
			}
			/**
				  #
				#[#]#
			*/
			else if(angle==2)
			{
				if(((direction==ROTATE_RIGHT)
						&&(blockX>=0)&&(blockX<=7)&&(blockY<=21)
						&&(LOWORD(field[blockX][blockY])==BLOCK_COLOR_NONE)
						&&(LOWORD(field[blockX][blockY+1])==BLOCK_COLOR_NONE)
						&&(LOWORD(field[blockX+2][blockY])==BLOCK_COLOR_NONE)
						&&(LOWORD(field[blockX+2][blockY+2])==BLOCK_COLOR_NONE))
					||((direction==ROTATE_LEFT)
						&&(blockX>=0)&&(blockX<=7)&&(blockY<=21)
						&&(LOWORD(field[blockX][blockY])==BLOCK_COLOR_NONE)
						&&(LOWORD(field[blockX][blockY+2])==BLOCK_COLOR_NONE)
						&&(LOWORD(field[blockX+2][blockY])==BLOCK_COLOR_NONE)
						&&(LOWORD(field[blockX+2][blockY+1])==BLOCK_COLOR_NONE)))
				{
					rotated=true;
					clearBlock(currentBlock);
					currentBlock[1][0]=color;
					currentBlock[1][1]=color;
					currentBlock[2][1]=color;
					currentBlock[0][1]=color;
				}
			}
			/**
				  #
				#[#]
				  #
			*/
			else if(angle==3)
			{
				if(
					(
						(direction==ROTATE_RIGHT)
						&&(blockX>=0)&&(blockX<=7)&&(blockY<=21)
						&&(LOWORD(field[blockX][blockY])==BLOCK_COLOR_NONE)
						&&(LOWORD(field[blockX+2][blockY])==BLOCK_COLOR_NONE)
						&&(LOWORD(field[blockX][blockY+2])==BLOCK_COLOR_NONE)
						&&(LOWORD(field[blockX+1][blockY+2])==BLOCK_COLOR_NONE)
					)
					||
					(
						(direction==ROTATE_LEFT)
						&&(blockX>=0)&&(blockX<=7)&&(blockY<=21)
						&&(LOWORD(field[blockX][blockY])==BLOCK_COLOR_NONE)
						&&(LOWORD(field[blockX+1][blockY])==BLOCK_COLOR_NONE)
						&&(LOWORD(field[blockX][blockY+2])==BLOCK_COLOR_NONE)
						&&(LOWORD(field[blockX+2][blockY+2])==BLOCK_COLOR_NONE)
					)
					)
				{
					rotated=true;
					clearBlock(currentBlock);
					currentBlock[1][0]=color;
					currentBlock[1][1]=color;
					currentBlock[1][2]=color;
					currentBlock[0][1]=color;
				}
			}
		}
		else if(blockType==BLOCK_SHAPE_RL)
		{
			/**
				  #
				 [#]
				 ##
			*/
			if(angle==0)
			{
				if(((direction==ROTATE_RIGHT)
						&&(blockX>=0)&&(blockX<=7)&&(blockY<=21)
						&&(LOWORD(field[blockX][blockY+2])==BLOCK_COLOR_NONE)
						&&(LOWORD(field[blockX+1][blockY+2])==BLOCK_COLOR_NONE)
						&&(LOWORD(field[blockX+1][blockY])==BLOCK_COLOR_NONE)
						&&(LOWORD(field[blockX+2][blockY])==BLOCK_COLOR_NONE))
					||((direction==ROTATE_LEFT)
						&&(blockX>=0)&&(blockX<=7)&&(blockY<=21)
						&&(LOWORD(field[blockX][blockY+2])==BLOCK_COLOR_NONE)
						&&(LOWORD(field[blockX+1][blockY+2])==BLOCK_COLOR_NONE)
						&&(LOWORD(field[blockX][blockY])==BLOCK_COLOR_NONE)
						&&(LOWORD(field[blockX+1][blockY])==BLOCK_COLOR_NONE)))
				{
					rotated=true;
					clearBlock(currentBlock);
					currentBlock[1][0]=color;
					currentBlock[1][1]=color;
					currentBlock[1][2]=color;
					currentBlock[0][2]=color;
				}
			}
			/**
				#[#]#
				    #
			*/
			else if(angle==1)
			{
				if(((direction==ROTATE_RIGHT)
						&&(blockX>=0)&&(blockX<=7)&&(blockY<=21)
						&&(LOWORD(field[blockX][blockY])==BLOCK_COLOR_NONE)
						&&(LOWORD(field[blockX][blockY+1])==BLOCK_COLOR_NONE)
						&&(LOWORD(field[blockX+2][blockY+1])==BLOCK_COLOR_NONE)
						&&(LOWORD(field[blockX+2][blockY+2])==BLOCK_COLOR_NONE))
					||((direction==ROTATE_LEFT)
						&&(blockX>=0)&&(blockX<=7)&&(blockY<=21)
						&&(LOWORD(field[blockX][blockY+1])==BLOCK_COLOR_NONE)
						&&(LOWORD(field[blockX][blockY+2])==BLOCK_COLOR_NONE)
						&&(LOWORD(field[blockX+2][blockY+1])==BLOCK_COLOR_NONE)
						&&(LOWORD(field[blockX+2][blockY+2])==BLOCK_COLOR_NONE)))
				{
					rotated=true;
					clearBlock(currentBlock);
					currentBlock[0][1]=color;
					currentBlock[1][1]=color;
					currentBlock[2][1]=color;
					currentBlock[2][2]=color;
				}
			}
			/**
				 ##
				[#]
				 #
			*/
			else if(angle==2)
			{
				if(((direction==ROTATE_RIGHT)
						&&(blockX>=0)&&(blockX<=7)&&(blockY<=21)
						&&(LOWORD(field[blockX][blockY+2])==BLOCK_COLOR_NONE)
						&&(LOWORD(field[blockX+1][blockY+2])==BLOCK_COLOR_NONE)
						&&(LOWORD(field[blockX+1][blockY])==BLOCK_COLOR_NONE)
						&&(LOWORD(field[blockX+2][blockY])==BLOCK_COLOR_NONE))
					||((direction==ROTATE_LEFT)
						&&(blockX>=0)&&(blockX<=7)&&(blockY<=21)
						&&(LOWORD(field[blockX+1][blockY])==BLOCK_COLOR_NONE)
						&&(LOWORD(field[blockX+2][blockY])==BLOCK_COLOR_NONE)
						&&(LOWORD(field[blockX+1][blockY+2])==BLOCK_COLOR_NONE)
						&&(LOWORD(field[blockX+2][blockY+2])==BLOCK_COLOR_NONE)))
				{
					rotated=true;
					clearBlock(currentBlock);
					currentBlock[1][0]=color;
					currentBlock[1][1]=color;
					currentBlock[1][2]=color;
					currentBlock[2][0]=color;
				}
			}
			/**
				#
				#[#]#
			*/
			else if(angle==3)
			{
				if(((direction==ROTATE_RIGHT)
						&&(blockX>=0)&&(blockX<=7)&&(blockY<=21)
						&&(LOWORD(field[blockX][blockY])==BLOCK_COLOR_NONE)
						&&(LOWORD(field[blockX][blockY+1])==BLOCK_COLOR_NONE)
						&&(LOWORD(field[blockX+2][blockY+2])==BLOCK_COLOR_NONE)
						&&(LOWORD(field[blockX+2][blockY+1])==BLOCK_COLOR_NONE))
					||((direction==ROTATE_LEFT)
						&&(blockX>=0)&&(blockX<=7)&&(blockY<=21)
						&&(LOWORD(field[blockX][blockY])==BLOCK_COLOR_NONE)
						&&(LOWORD(field[blockX][blockY+1])==BLOCK_COLOR_NONE)
						&&(LOWORD(field[blockX+2][blockY])==BLOCK_COLOR_NONE)
						&&(LOWORD(field[blockX+2][blockY+1])==BLOCK_COLOR_NONE)))
				{
					rotated=true;
					clearBlock(currentBlock);
					currentBlock[0][1]=color;
					currentBlock[1][1]=color;
					currentBlock[2][1]=color;
					currentBlock[0][0]=color;
				}
			}
		}
		else if(blockType==BLOCK_SHAPE_L)
		{
			/**
				 #
				[#]
				 ##
			*/
			if(angle==0)
			{
				if(((direction==ROTATE_RIGHT)
						&&(blockX>=0)&&(blockX<=7)
						&&(LOWORD(field[blockX+1][blockY+2])==BLOCK_COLOR_NONE)
						&&(LOWORD(field[blockX+2][blockY+2])==BLOCK_COLOR_NONE)
						&&(LOWORD(field[blockX+1][blockY])==BLOCK_COLOR_NONE)
						&&(LOWORD(field[blockX+2][blockY])==BLOCK_COLOR_NONE))
					||((direction==ROTATE_LEFT)
						&&(blockX>=0)&&(blockX<=7)&&(blockY<=21)
						&&(LOWORD(field[blockX+1][blockY+2])==BLOCK_COLOR_NONE)
						&&(LOWORD(field[blockX+2][blockY+2])==BLOCK_COLOR_NONE)
						&&(LOWORD(field[blockX][blockY+1])==BLOCK_COLOR_NONE)
						&&(LOWORD(field[blockX+1][blockY])==BLOCK_COLOR_NONE)))
				{
					rotated=true;
					clearBlock(currentBlock);
					currentBlock[1][0]=color;
					currentBlock[1][1]=color;
					currentBlock[1][2]=color;
					currentBlock[2][2]=color;
				}
			}
			/**
				    #
				#[#]#
			*/
			else if(angle==1)
			{
				if(((direction==ROTATE_RIGHT)
							&&(blockX>=0)&&(blockX<=7)
							&&(LOWORD(field[blockX][blockY+1])==BLOCK_COLOR_NONE)
							&&(LOWORD(field[blockX+2][blockY])==BLOCK_COLOR_NONE)
							&&(LOWORD(field[blockX+2][blockY+1])==BLOCK_COLOR_NONE)
							&&(LOWORD(field[blockX][blockY])==BLOCK_COLOR_NONE))
						||((direction==ROTATE_LEFT)
							&&(blockX<=7)&&(blockX>=0)
							&&(LOWORD(field[blockX][blockY+1])==BLOCK_COLOR_NONE)
							&&(LOWORD(field[blockX+2][blockY])==BLOCK_COLOR_NONE)
							&&(LOWORD(field[blockX+2][blockY+1])==BLOCK_COLOR_NONE)
							&&(LOWORD(field[blockX][blockY+2])==BLOCK_COLOR_NONE)))
				{
					rotated=true;
					clearBlock(currentBlock);
					currentBlock[0][1]=color;
					currentBlock[1][1]=color;
					currentBlock[2][1]=color;
					currentBlock[2][0]=color;
				}
			}
			/**
				 ##
				 [#]
				  #
			*/
			else if(angle==2)
			{
				if(((direction==ROTATE_RIGHT)
							&&(blockX>=0)&&(blockX<=7)&&(blockY<=21)
							&&(LOWORD(field[blockX][blockY])==BLOCK_COLOR_NONE)
							&&(LOWORD(field[blockX+1][blockY])==BLOCK_COLOR_NONE)
							&&(LOWORD(field[blockX+1][blockY+2])==BLOCK_COLOR_NONE)
							&&(LOWORD(field[blockX][blockY+2])==BLOCK_COLOR_NONE))
						||((direction==ROTATE_LEFT)
							&&(blockX>=0)&&(blockX<=7)
							&&(LOWORD(field[blockX+1][blockY+2])==BLOCK_COLOR_NONE)
							&&(LOWORD(field[blockX+2][blockY+2])==BLOCK_COLOR_NONE)
							&&(LOWORD(field[blockX][blockY])==BLOCK_COLOR_NONE)
							&&(LOWORD(field[blockX+1][blockY])==BLOCK_COLOR_NONE)))
				{
					rotated=true;
					clearBlock(currentBlock);
					currentBlock[1][0]=color;
					currentBlock[1][1]=color;
					currentBlock[1][2]=color;
					currentBlock[0][0]=color;
				}
			}
			/**
				#[#]#
				#
			*/
			else if(angle==3)
			{
				if(((direction==ROTATE_RIGHT)
						&&(blockX>=0)&&(blockX<=7)
						&&(LOWORD(field[blockX][blockY+1])==BLOCK_COLOR_NONE)
						&&(LOWORD(field[blockX][blockY+2])==BLOCK_COLOR_NONE)
						&&(LOWORD(field[blockX+2][blockY+1])==BLOCK_COLOR_NONE)
						&&(LOWORD(field[blockX+2][blockY+2])==BLOCK_COLOR_NONE))
					||((direction==ROTATE_LEFT)
						&&(blockX>=0)&&(blockX<=7)
						&&(LOWORD(field[blockX+2][blockY])==BLOCK_COLOR_NONE)
						&&(LOWORD(field[blockX+2][blockY+1])==BLOCK_COLOR_NONE)
						&&(LOWORD(field[blockX][blockY+1])==BLOCK_COLOR_NONE)
						&&(LOWORD(field[blockX][blockY+2])==BLOCK_COLOR_NONE)))
				{
					rotated=true;
					clearBlock(currentBlock);
					currentBlock[0][1]=color;
					currentBlock[1][1]=color;
					currentBlock[2][1]=color;
					currentBlock[0][2]=color;
				}
			}
		}
		else if(blockType==BLOCK_SHAPE_Z)
		{
			/**
				   #
				[#]#
				 #
			*/
			if((angle==0)||(angle==2))
			{
				if((blockX>=0)&&(blockX<=7)&&(blockY<=21)
						&&(LOWORD(field[blockX][blockY+1])==BLOCK_COLOR_NONE)
						&&(LOWORD(field[blockX][blockY+2])==BLOCK_COLOR_NONE)
						&&(LOWORD(field[blockX+1][blockY+2])==BLOCK_COLOR_NONE))
				{
					rotated=true;
					clearBlock(currentBlock);
					currentBlock[2][0]=color;
					currentBlock[1][1]=color;
					currentBlock[2][1]=color;
					currentBlock[1][2]=color;
				}
			}
			/**
				 ##
				 [#]#
			*/
			else if((angle==1)||(angle==3))
			{
				if((blockX>=0)&&(blockX<=7)&&(blockY<=21)
					&&(LOWORD(field[blockX][blockY])==BLOCK_COLOR_NONE)
					&&(LOWORD(field[blockX+1][blockY+2])==BLOCK_COLOR_NONE)
					&&(LOWORD(field[blockX+2][blockY+1])==BLOCK_COLOR_NONE)
					&&(LOWORD(field[blockX+2][blockY+2])==BLOCK_COLOR_NONE))
				{
					rotated=true;
					clearBlock(currentBlock);
					currentBlock[0][0]=color;
					currentBlock[1][1]=color;
					currentBlock[1][0]=color;
					currentBlock[2][1]=color;
				}
			}
		}
		else if(blockType==BLOCK_SHAPE_RZ)
		{
			/**
				 #
				[#]#
				   #
			*/
			if((angle==0)||(angle==2))
			{
				if((blockX>=0)&&(blockX<=7)&&(blockY<=21)
						&&(LOWORD(field[blockX+1][blockY])==BLOCK_COLOR_NONE)
						&&(LOWORD(field[blockX+2][blockY])==BLOCK_COLOR_NONE)
						&&(LOWORD(field[blockX+2][blockY+2])==BLOCK_COLOR_NONE))
				{
					rotated=true;
					clearBlock(currentBlock);
					currentBlock[1][1]=color;
					currentBlock[2][1]=color;
					currentBlock[1][0]=color;
					currentBlock[2][2]=color;
				}
			}
			/**
				 [#]#
				 ##
			*/
			else if((angle==1)||(angle==3))
			{
				if((blockX>=0)&&(blockX<=7)&&(blockY<=21)
						&&(LOWORD(field[blockX+2][blockY])==BLOCK_COLOR_NONE)
						&&(LOWORD(field[blockX+2][blockY+1])==BLOCK_COLOR_NONE)
						&&(LOWORD(field[blockX][blockY+2])==BLOCK_COLOR_NONE)
						&&(LOWORD(field[blockX+1][blockY+2])==BLOCK_COLOR_NONE))
				{
					rotated=true;
					clearBlock(currentBlock);
					currentBlock[1][1]=color;
					currentBlock[2][1]=color;
					currentBlock[0][2]=color;
					currentBlock[1][2]=color;
				}
			}
		}
		if(rotated==false)
		{
			if(direction==ROTATE_LEFT)
				angle++;
			else if(direction==ROTATE_RIGHT)
				angle--;
			if(angle>3)
				angle=0;
			else if(angle<0)
				angle=3;
		}
		put();
	}
	
	public int getWidth()
	{
		return(WIDTH);
	}
	
	public int getHeight()
	{
		return(HEIGHT);
	}
	
	public int getVisibleHeight()
	{
		return(VISIBLE_HEIGHT);
	}
	
	public void newBlock()
	{
		if(next==-1)
		{
			next=(rand.nextInt(7000)/1000)+20;
			newBlock(nextBlock,next,true);
		}
		newBlock(currentBlock,next,false);
		next=(rand.nextInt(7000)/1000)+20;
		newBlock(nextBlock,next,true);
	}
	
	public void newBlock(int[][] block, int type, boolean next)
	{
		if(next)
			nextColor=type-20;
		clearBlock(block);
		if(type==BLOCK_SHAPE_L)
		{
			block[0][1]=BLOCK_COLOR_DARKPINK;
			block[1][1]=BLOCK_COLOR_DARKPINK;
			block[2][1]=BLOCK_COLOR_DARKPINK;
			block[2][0]=BLOCK_COLOR_DARKPINK;
			if(!next)
			{
				angle=1;
				blockType=BLOCK_SHAPE_L;
				color=BLOCK_COLOR_DARKPINK;
				blockX=3;
				blockY=3;
			}
		}
		else if(type==BLOCK_SHAPE_RL)
		{
			block[0][1]=BLOCK_COLOR_BROWN;
			block[1][1]=BLOCK_COLOR_BROWN;
			block[2][1]=BLOCK_COLOR_BROWN;
			block[0][0]=BLOCK_COLOR_BROWN;
			if(!next)
			{
				angle=3;
				blockType=BLOCK_SHAPE_RL;
				color=BLOCK_COLOR_BROWN;
				blockX=3;
				blockY=3;
			}
		}
		else if(type==BLOCK_SHAPE_Z)
		{
			block[0][0]=BLOCK_COLOR_PINK;
			block[1][1]=BLOCK_COLOR_PINK;
			block[1][0]=BLOCK_COLOR_PINK;
			block[2][1]=BLOCK_COLOR_PINK;
			if(!next)
			{
				angle=1;
				blockType=BLOCK_SHAPE_Z;
				color=BLOCK_COLOR_PINK;
				blockX=3;
				blockY=3;
			}
		}
		else if(type==BLOCK_SHAPE_RZ)
		{
			block[0][2]=BLOCK_COLOR_YELLOW;
			block[1][2]=BLOCK_COLOR_YELLOW;
			block[1][1]=BLOCK_COLOR_YELLOW;
			block[2][1]=BLOCK_COLOR_YELLOW;
			if(!next)
			{
				angle=1;
				blockType=BLOCK_SHAPE_RZ;
				color=BLOCK_COLOR_YELLOW;
				blockX=3;
				blockY=2;
			}
		}
		else if(type==BLOCK_SHAPE_I)
		{
			block[0][1]=BLOCK_COLOR_RED;
			block[1][1]=BLOCK_COLOR_RED;
			block[2][1]=BLOCK_COLOR_RED;
			block[3][1]=BLOCK_COLOR_RED;
			if(!next)
			{
				angle=1;
				blockType=BLOCK_SHAPE_I;
				color=BLOCK_COLOR_RED;
				blockX=3;
				blockY=3;
			}
		}
		else if(type==BLOCK_SHAPE_T)
		{
			block[1][0]=BLOCK_COLOR_GREEN;
			block[1][1]=BLOCK_COLOR_GREEN;
			block[2][1]=BLOCK_COLOR_GREEN;
			block[0][1]=BLOCK_COLOR_GREEN;
			if(!next)
			{
				angle=2;
				blockType=BLOCK_SHAPE_T;
				color=BLOCK_COLOR_GREEN;
				blockX=3;
				blockY=3;
			}
		}
		else if(type==BLOCK_SHAPE_O)
		{
			block[0][0]=BLOCK_COLOR_BLUE;
			block[0][1]=BLOCK_COLOR_BLUE;
			block[1][0]=BLOCK_COLOR_BLUE;
			block[1][1]=BLOCK_COLOR_BLUE;
			if(!next)
			{
				angle=0;
				blockType=BLOCK_SHAPE_O;
				color=BLOCK_COLOR_BLUE;
				blockX=3;
				blockY=3;
			}
		}
		if(!next)
			put();
	}
	
	public boolean addSolidBlocks(int nr)
	{
		int x=0;
		int y=0;
		blockY-=nr;
		if(blockY<0)
			blockY=0;
		//Check if there is room for more solid blocks
		for(y=0;y<nr;y++)
		{
			for(x=0;x<WIDTH;x++)
			{
				if(field[x][y]!=LOWORD(BLOCK_COLOR_NONE))
					return(false);
			}
		}

		//Move blocks
		for(y=nr;y<HEIGHT;y++)
		{
			for(x=0;x<WIDTH;x++)
			{
				field[x][y-nr]=field[x][y];
			}
		}
		//Add solid blocks
		for(y=HEIGHT-nr;y<HEIGHT;y++)
		{
			for(x=0;x<WIDTH;x++)
			{
				field[x][y]=MAKELONG(BLOCK_COLOR_GRAY,BLOCK_SOLID);
			}
		}
		return(true);
	}
	
	public int removeSolidBlocks(int nr)
	{
		int x=0;
		int y=0;
		clearPos(); //Remove current block
		//Check if there are any solid block rows.
		for(y=HEIGHT-nr;y<HEIGHT;y++)
		{
			for(x=0;x<WIDTH;x++)
			{
				if(field[x][y]!=MAKELONG(BLOCK_COLOR_GRAY,BLOCK_SOLID))
				{
					nr--;
					break;
				}
			}
		}

		//Move game field down
		for(y=HEIGHT-nr-1;y>=nr;y--)
		{
			for(x=0;x<WIDTH;x++)
				field[x][y+nr]=field[x][y];
		}
		put(); //Add current block
		return(nr);
	}
	
	public int checkFullRow()
	{
		boolean fullrow=false;
		int rowCount=0;
		for(int y=(HEIGHT-1);y>=0;y--)
		{
			fullrow=true;
			for(int x=0;x<10;x++)
			{
				if((LOWORD(field[x][y])==BLOCK_COLOR_NONE)||(HIWORD(field[x][y])==BLOCK_SOLID))
					fullrow=false;
			}
			if(fullrow)
			{
				if(status==STATUS_REMOVE_LINE)
					removeFullRow(y++);
				else
					setRemoveFullRow(y);
				rowCount++;
			}
		}
		if(status==STATUS_REMOVE_LINE)
			status=STATUS_NORMAL;
		else if(rowCount>0)
		{
			status=STATUS_REMOVE_LINE;
			setFlashCounter(0);
		}
		return(rowCount);
	}
	
	public Field()
	{
		reset();
	}

	public String getState()
	{
		StringBuffer sb=new StringBuffer();
		sb.append(angle);
		sb.append("|");
		sb.append(blockType);
		sb.append("|");
		sb.append(blockX);
		sb.append("|");
		sb.append(blockY);
		sb.append("|");
		sb.append(next);
		sb.append("|");
		sb.append(status);
		sb.append("|");
		sb.append(flashCounter);
		sb.append("|");
		sb.append(resets);
		sb.append("|");
		sb.append(nextColor);
		sb.append("|");
		sb.append(color);
		sb.append("|");
		sb.append(startHeight);
		sb.append("|");
		int x=0;
		int y=0;
		for(y=0;y<4;y++)
		{
			for(x=0;x<4;x++)
			{
				sb.append(nextBlock[x][y]);
				sb.append("|");
			}
		}
		for(y=0;y<4;y++)
		{
			for(x=0;x<4;x++)
			{
				sb.append(currentBlock[x][y]);
				sb.append("|");
			}
		}		
		for(y=0;y<HEIGHT;y++)
		{
			for(x=0;x<WIDTH;x++)
			{
				sb.append(field[x][y]);
				sb.append("|");
			}
		}
		return(sb.toString());
	}
	
	public boolean restoreState(String state)
	{
		try
		{
			angle=Integer.parseInt(state.substring(0,state.indexOf("|")));
			state=state.substring(state.indexOf("|")+1);
			blockType=Integer.parseInt(state.substring(0,state.indexOf("|")));
			state=state.substring(state.indexOf("|")+1);
			blockX=Integer.parseInt(state.substring(0,state.indexOf("|")));
			state=state.substring(state.indexOf("|")+1);
			blockY=Integer.parseInt(state.substring(0,state.indexOf("|")));
			state=state.substring(state.indexOf("|")+1);
			next=Integer.parseInt(state.substring(0,state.indexOf("|")));
			state=state.substring(state.indexOf("|")+1);
			status=Integer.parseInt(state.substring(0,state.indexOf("|")));
			state=state.substring(state.indexOf("|")+1);
			flashCounter=Integer.parseInt(state.substring(0,state.indexOf("|")));
			state=state.substring(state.indexOf("|")+1);
			resets=Integer.parseInt(state.substring(0,state.indexOf("|")));
			state=state.substring(state.indexOf("|")+1);
			nextColor=Integer.parseInt(state.substring(0,state.indexOf("|")));
			state=state.substring(state.indexOf("|")+1);
			color=Integer.parseInt(state.substring(0,state.indexOf("|")));
			state=state.substring(state.indexOf("|")+1);
			startHeight=Integer.parseInt(state.substring(0,state.indexOf("|")));
			state=state.substring(state.indexOf("|")+1);
			int x=0;
			int y=0;
			for(y=0;y<4;y++)
			{
				for(x=0;x<4;x++)
				{
					nextBlock[x][y]=Integer.parseInt(state.substring(0,state.indexOf("|")));
					state=state.substring(state.indexOf("|")+1);
				}
			}
			for(y=0;y<4;y++)
			{
				for(x=0;x<4;x++)
				{
					currentBlock[x][y]=Integer.parseInt(state.substring(0,state.indexOf("|")));
					state=state.substring(state.indexOf("|")+1);
				}
			}			
			for(y=0;y<HEIGHT;y++)
			{
				for(x=0;x<WIDTH;x++)
				{
					field[x][y]=Integer.parseInt(state.substring(0,state.indexOf("|")));
					state=state.substring(state.indexOf("|")+1);
				}
			}
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
