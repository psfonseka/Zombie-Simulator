import java.awt.Color;

import java.awt.Graphics;
import java.awt.Graphics2D;


public class DeadZombie extends GameToken
{

   //Fields
   
   //Constructors
   public DeadZombie(int gridX, int gridY)
   {
      super(gridX,gridY);
      setLocation(gridX*CityPanel.gridSize,gridY*CityPanel.gridSize);
      setSize(CityPanel.gridSize, CityPanel.gridSize);
      // TODO set location and size
   }
   

   //Methods
   protected void paintComponent(Graphics g)
   {
      super.paintComponent(g);
      Graphics2D g2d = (Graphics2D) g;
      // Draw grid
      g2d.setColor(Color.BLACK);
      //ToDo: Draw a black triangle depending on direction 
      if (getDirection() == 0)
      {
      int[] x = {CityPanel.gridSize/2,0,CityPanel.gridSize};
      int[] y = {0,CityPanel.gridSize,CityPanel.gridSize};
      g2d.fillPolygon(x, y, 3);
      }
      
      else if (getDirection() == 1)
      {
      int[] x = {0,0,CityPanel.gridSize};
      int[] y = {0,CityPanel.gridSize,CityPanel.gridSize/2};
      g2d.fillPolygon(x, y, 3);
      }
      
      else if (getDirection() == 2)
      {
      int[] x = {0,CityPanel.gridSize/2,CityPanel.gridSize};
      int[] y = {0,CityPanel.gridSize,0};
      g2d.fillPolygon(x, y, 3);
      }
      
      else if (getDirection() == 3)
      {
      int[] x = {0,CityPanel.gridSize,CityPanel.gridSize};
      int[] y = {CityPanel.gridSize/2,0,CityPanel.gridSize};
      g2d.fillPolygon(x, y, 3);
      }
      
      else
      {
    	  System.out.println("Error - incorrect direction ");
      }

   }

}