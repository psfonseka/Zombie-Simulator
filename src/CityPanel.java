/**
 * CSCI 2113 - Project 2 - Zombie Apocalypse Simulator
 * 
 * @author [Pravin Fonseka]
 *
 */
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class CityPanel extends JPanel
{
   static int gridSize = 30;
   static int numGrids = 30;
   static int panelSize = 900;
   
   public CityPanel()
   {
      setBorder(BorderFactory.createLineBorder(Color.black));
      setBackground(new Color(197, 2, 81, 1));
      setLayout(null);
      setSize(900, 900);
   }

   @Override
   public Dimension getPreferredSize()
   {
      return new Dimension(900, 900);
   }
   
   public int getGridSize()
   {
	   return gridSize;
   }

   @Override
   protected void paintComponent(Graphics g)
   {
      super.paintComponent(g);
      Graphics2D g2d = (Graphics2D) g;

      // Draw grid
      g2d.setColor(Color.BLACK);
      for (int i = 0; i <= numGrids; i++)
      {
         g2d.drawLine(i * gridSize, 0, i * gridSize, panelSize);
         g2d.drawLine(0, i * gridSize, panelSize, i * gridSize);
      }
   }

}
