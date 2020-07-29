/**
 * CSCI 2113 - Project 2 - Zombie Apocalypse Simulator

 * 
 * @author [Pravin Fonseka]
 *
 */

import javax.swing.SwingUtilities;

public class ZombieApocalypseSimulator
{
   private static ZombieApocalypseSimulator instance;

   public static ZombieApocalypseSimulator Instance()
   {
      return instance;
   }

   public ZombieApocalypseSimulator()
   {

   }
   
   private void createAndShowGUI()
   {
      SimFrame simFrame = new SimFrame();
   }
   
   /**
    * @param args
    */
   public static void main(String[] args)
   {
      instance = new ZombieApocalypseSimulator();
      
      SwingUtilities.invokeLater(new Runnable() {
         public void run() {
             instance.createAndShowGUI(); 

         }
      });

   }
}
