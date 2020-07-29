
import java.awt.Color;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;

public class Building extends GameToken
{

   public Building(int gridX, int gridY)
   {
      super(gridX, gridY);
      setLocation(gridX*CityPanel.gridSize,gridY*CityPanel.gridSize);
      setSize(CityPanel.gridSize, CityPanel.gridSize);
      // TODO set location and size
   }
   
   
   protected void paintComponent(Graphics g)
   {
      super.paintComponent(g);
      Graphics2D g2d = (Graphics2D) g;
      // Draw grid
      g2d.setColor(Color.BLACK);
      //ToDo: Draw a black box 
      Rectangle2D.Double r = new Rectangle2D.Double(0,0,CityPanel.gridSize,CityPanel.gridSize);
      g2d.fill(r);
   }

}
