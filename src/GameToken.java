import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.util.Random;

import javax.swing.JComponent;

public abstract class GameToken extends JComponent
{

   //Fields
   private int direction;
   private int gridX;
   private int gridY; 
   
   //Constructors
   public GameToken(int gridX, int gridY)
   {
      super();
      //Set location and size
      this.gridX = gridX;
      this.gridY = gridY;
      setLocation(gridX*CityPanel.gridSize,gridY*CityPanel.gridSize);
      setSize(CityPanel.gridSize, CityPanel.gridSize);
   }
   
   //Methods
   public int getGridX()
   {
      return gridX;
   }

   public void setGridX(int x)
   {
      if (gridX < 0 || x > CityPanel.numGrids-1)
      {
         System.err.println("Bad Grid X in GameToken setGridX() x = " + gridX);
      }
      else
      {
         this.gridX = x;
         setLocation(gridX*CityPanel.gridSize, gridY*CityPanel.gridSize);
      }
   }

   public int getGridY()
   {
      return gridY;
   }

   public void setGridY(int y)
   {
      if (gridY < 0 || gridY > CityPanel.numGrids-1)
      {
         System.err.println("Bad gridY in GameToken setGridY() y = " + gridY);
      }
      else
      {
         this.gridY = y;
         setLocation(gridX*CityPanel.gridSize, gridY*CityPanel.gridSize);
      }
   }
   
   public int getDirection()
   {
	   return direction;
   }
   
   public void setDirection(String d) //Take compass directions and turn them into digits from 0 to 3
   {
	   if (d.equals("NORTH"))
	   {
		   direction = 0;
	   }
	   
	   else if (d.equals("EAST"))
	   {
		   direction = 1;
	   }
	   
	   else if (d.equals("SOUTH"))
	   {
		   direction = 2;
	   }
	   
	   else if (d.equals("WEST"))
	   {
		   direction = 3;
	   }
   }
   
   public void setDirection(int d)
   {
	   if (d == 0)
	   {
		   direction = 0;
	   }
	   
	   else if (d == 1)
	   {
		   direction = 1;
	   }
	   
	   else if (d == 2)
	   {
		   direction = 2;
	   }
	   
	   else if (d == 3)
	   {
		   direction = 3;
	   }
   }
   
   public void turnLeft() //Turn token 90 degrees left
   {
	   if (getDirection() == 0)
	   {
		   setDirection("WEST");
	   }
	   else if (getDirection() == 1)
	   {
		   setDirection("NORTH");
	   }
	   else if (getDirection() == 2)
	   {
		   setDirection("EAST");
	   }
	   else 
	   {
		   setDirection("SOUTH");
	   }
   }
   
   public void turnRight() //Turn token 90 degrees right
   {
	   if (getDirection() == 0)
	   {
		   setDirection("EAST");
	   }
	   else if (getDirection() == 1)
	   {
		   setDirection("SOUTH");
	   }
	   else if (getDirection() == 2)
	   {
		   setDirection("WEST");
	   }
	   else 
	   {
		   setDirection("NORTH");
	   }
   }
   
   public void turnAround() //Turn token 180 degrees
   {
	   if (getDirection() == 0)
	   {
		   setDirection("SOUTH");
	   }
	   else if (getDirection() == 1)
	   {
		   setDirection("WEST");
	   }
	   else if (getDirection() == 2)
	   {
		   setDirection("NORTH");
	   }
	   else 
	   {
		   setDirection("EAST");
	   }
   }
   
   public void turn()//Turn left(40%), right(40%), or around(20%) if on edge or in front a building
   {
	   int rnd;
	   Random random = new Random();
       rnd = random.nextInt(100);
       if (rnd < 40)
       {
       	turnLeft();
       }
       else if (rnd < 80)
       {
       	turnRight();
       }
       else
       {
       	turnAround();
       }
   }
   
   public void move()
   {
	   if (getDirection() == 0) //Taking the step
       {
          if (gridY != 0)
          {
             setGridY(gridY - 1);
          }
          else
          {
       	   turn();
          }
       }
       else if (getDirection() == 1)
       {
          if (gridX != CityPanel.numGrids-1)
          {
             setGridX(gridX + 1);
          }        
          else
          {
       	   turn();
          }
       }
       else if (getDirection() == 2)
       {
          if (gridY != CityPanel.numGrids-1)
          {
             setGridY(gridY + 1);
          }
          else
          {
       	   turn();
          }
       }
       else if (getDirection() == 3)
       {
          if (gridX != 0)
          {
             setGridX(gridX - 1);
          }   
          else
          {
       	   turn();
          }
       }       
   }


}
