/**
 * CSCI 2113 - Project 2 - Zombie Apocalypse Simulator

 * 
 * @author [Pravin Fonseka]
 *
 */
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public class SimFrame extends JFrame
{
	//Fields
   private CityPanel cityPanel;
   private JPanel buttonPanel;
   private JButton startButton;
   private JButton stopButton;
   private JButton stepButton;
   private JButton restartButton;
   private RandomButtonListener randomButtonListener;
   private ButtonListener buttonListener;
   private Random rng;
   private Timer timer;
   private ArrayList<GameToken> buildings;
   private ArrayList<GameToken> humans;
   private ArrayList<GameToken> zombies;
   private ArrayList<GameToken> deadHumans;
   private ArrayList<GameToken> deadZombies;

   //Constructors
   public SimFrame()
   {
      setTitle("Zombie Apocalypse Simulator");
      cityPanel = new CityPanel(); 
    
      setDefaultCloseOperation(EXIT_ON_CLOSE);
      getContentPane().add(cityPanel, BorderLayout.CENTER);
      buildings = new ArrayList<GameToken>();
      humans = new ArrayList<GameToken>();
      zombies = new ArrayList<GameToken>();
      deadHumans = new ArrayList<GameToken>();
      deadZombies = new ArrayList<GameToken>();
      tokenLoader("City.map");

      
      buttonPanel = new JPanel(new FlowLayout()); //For buttons 
      startButton = new JButton("Start");
      stopButton = new JButton("Stop");
      stepButton = new JButton("Step");
      restartButton = new JButton("Restart");
      buttonPanel.add(startButton);
      buttonPanel.add(stopButton);
      buttonPanel.add(stepButton);
      buttonPanel.add(restartButton);
      rng = new Random();

      //Setting up all necessary action listeners
      timer = new Timer(200, new RandomButtonListener()); //Timer is set to 200 milliseconds 
      buttonListener = new ButtonListener();
      randomButtonListener = new RandomButtonListener();
      startButton.addActionListener(buttonListener);
      stopButton.addActionListener(buttonListener);
      stepButton.addActionListener(randomButtonListener);
      restartButton.addActionListener(buttonListener);

      getContentPane().add(buttonPanel, BorderLayout.SOUTH);

      pack();
      setVisible(true);  
      setSize(1000,1000);

   }
   
   //Methods
   
   public void tokenLoader(String filename)//Loads everything into the map from the City.map file
   {
	    BufferedReader frdr;
	    String file = "";
	    String line = "";
	    
	      try
	      {

	        frdr = new BufferedReader(new FileReader(filename));
	        
	        line = frdr.readLine();
	        
	        while (line != null) //Cycle through each line
	        {
	        	String[] words = line.split(" "); //Get the object type and coordinates separated
	        	String object = words[0];
	        	int x = Integer.parseInt(words[1]);
	        	int y = Integer.parseInt(words[2]);
	        	
	        	if (words[0].equals("Building"))
	        	{
	        		Building b = new Building(x,y);
	        		buildings.add(b);
	        	    cityPanel.add(b);
	        	}
	        	else if (words[0].equals("Human"))
	        	{
	        		Human h = new Human(x,y);
	        		h.setDirection(words[3]);
	        		humans.add(h);
	        	    cityPanel.add(h);
	        	}
	        	else if (words[0].equals("Zombie"))
	        	{
	        		Zombie z = new Zombie(x,y);
	        		z.setDirection(words[3]);
	        		zombies.add(z);
	        	    cityPanel.add(z);
	        	}

	        	line = frdr.readLine();
	        }


	        frdr.close();
	      }
	      catch (IOException e)
	      {
	         System.err.println(e);
	      }
   }
   
   public void killZombie(GameToken z) //Replaces a zombie with a dead zombie
   {
	   	int dir = z.getDirection();
		int x = z.getGridX();
		int y = z.getGridY();
		DeadZombie d = new DeadZombie(x,y);
		d.setDirection(dir);
		deadZombies.add(d);
		cityPanel.add(d);
		zombies.remove(z);
		cityPanel.remove(z);
   }
   
   public void killHuman(GameToken h) //Replaces a human with a dead human
   {
	   int dir = h.getDirection();
	   int hx = h.getGridX();
	   int hy = h.getGridY();
	   humans.remove(h);
	   DeadHuman d = new DeadHuman(hx,hy);
	   d.setDirection(dir);
	   deadHumans.add(d);
	   cityPanel.add(d);
	   cityPanel.remove(h);
   }
   
   public void fightOrFlight(GameToken g, GameToken z)//25% change human will fight zombie, then it's 50/50 whether it kills the zombie
   {
	   int rnd;
	   Random random = new Random();
       rnd = random.nextInt(100);
       if (rnd < 25)
       {
       		rnd = random.nextInt(100);
       		{
       			if (rnd < 50)
       			{
       				killZombie(z);
       			}
       		}
       }
       else
       {
       	g.turnAround();
       }
   }
   
   public boolean buildingInTheWay(GameToken g, GameToken z) //check if there is a building blocking the view for gametoken g
   {
	   int x = g.getGridX();
	   int y = g.getGridY();
	   int dir = g.getDirection();
	   int zx = z.getGridX();
	   int zy = z.getGridY();
	   for (GameToken b : buildings)
	   {
		   int bx = b.getGridX();
		   int by = b.getGridY();
		   if (dir == 0)
		   {
			   if (bx == x && by < y && by > zy)
			   {
				   return true;
			   }
		   }
		   else if (dir == 1)
		   {
			   if (by == y && bx > x && bx < zx )
			   {
				   return true;
			   }
		   }
		   else if (dir == 2)
		   {
			   if (bx == x && by > y && bx < zy )
			   {
				   return true;
			   }
		   }
		   else if (dir == 3)
		   {
			   if (by == y && bx < x && bx > zx )
			   {
				   return true;
			   }
		   }
	   }
	   
	   return false;
   }
   
   public boolean zombieInView(GameToken g) //Check if there is a zombie in front more than 1 space away without a building in the way
   {
	   int x = g.getGridX();
	   int y = g.getGridY();
	   int dir = g.getDirection();
	   for (GameToken z : zombies)
	   {
		   if (dir == 0)
		   {
			   if (z.getGridX() == x && z.getGridY() > y)
			   {
				   if (!buildingInTheWay(g,z))
				   {
					   return true;
				   }
			   }
		   }
		   else if (dir == 1)
		   {
			   if (z.getGridX() > x && z.getGridY() == y)
			   {
				   if (!buildingInTheWay(g,z))
				   {
					   return true;
				   }
			   }
		   }
		   else if (dir == 2)
		   {
			   if (z.getGridX() == x && z.getGridY() > y)
			   {
				   if (!buildingInTheWay(g,z))
				   {
					   return true;
				   };
			   }
		   }
		   else if (dir == 3)
		   {
			   if (z.getGridX() < x && z.getGridY() == y)
			   {
				   if (!buildingInTheWay(g,z))
				   {
					   return true;
				   }
			   }
		   }
		   else
		   {
			   System.out.println("Something went wrong - tokenInFront method zombieinview");
		   }
	   }
	   return false;
   }
   
   public GameToken zombieInFront(GameToken g)//checks to see if the token has a zombie directly in front of it and returns that zombie
   {
	   int x = g.getGridX();
	   int y = g.getGridY();
	   int dir = g.getDirection();
	   for (GameToken z : zombies)
	   {
		   if (dir == 0)
		   {
			   if (z.getGridX() == x && z.getGridY() == y-1)
			   {
				   return z;
			   }
		   }
		   else if (dir == 1)
		   {
			   if (z.getGridX() == x+1 && z.getGridY() == y)
			   {
				   return z;
			   }
		   }
		   else if (dir == 2)
		   {
			   if (z.getGridX() == x && z.getGridY() == y+1)
			   {
				   return z;
			   }
		   }
		   else if (dir == 3)
		   {
			   if (z.getGridX() == x-1 && z.getGridY() == y)
			   {
				   return z;
			   }
		   }
		   else
		   {
			   System.out.println("Something went wrong - tokenInFront method zombieinfront");
		   }
	   }
	   return null;
   }
   
   public boolean buildingInFront(GameToken g)//Method to check if the currently moving token has something block it's way
   {
	   boolean front = false;
	   int x = g.getGridX();
	   int y = g.getGridY();
	   int z = g.getDirection();
	   for (GameToken b : buildings)
	   {
		   if (z == 0)
		   {
			   if (b.getGridX() == x && b.getGridY() == y-1)
			   {
				   front = true;
			   }
		   }
		   else if (z == 1)
		   {
			   if (b.getGridX() == x+1 && b.getGridY() == y)
			   {
				   front = true;
			   }
		   }
		   else if (z == 2)
		   {
			   if (b.getGridX() == x && b.getGridY() == y+1)
			   {
				   front = true;
			   }
		   }
		   else if (z == 3)
		   {
			   if (b.getGridX() == x-1 && b.getGridY() == y)
			   {
				   front = true;
			   }
		   }
		   else
		   {
			   System.out.println("Something went wrong - tokenInFront method buildinginfront");
		   }
	   }
	   return front;
   }
   
   public boolean counterAttack(GameToken z, GameToken h) //Gives humans a chance for counter attack depending on how much zombies outnumber them
   {
	   
	   int rnd;
	   Random random = new Random();
       rnd = random.nextInt(100);
       if (((Human) h).killedThisTurn() == true)
       {
    	   killHuman(h);
    	   return false;
       }
       if (zombies.size() > humans.size()*5)
       {
    	   if (rnd < 80)
    	   {
    		   killZombie(z);
    		   return true;
    	   }
    	   else 
    	   {
    		   killHuman(h);
    		   return false;
    	   }
       }
       else if (zombies.size() > humans.size()*2)
       {
    	   if (rnd < 40)
    	   {
    		   killZombie(z);
    		   return true;
    	   }
    	   else 
    	   {
    		   killHuman(h);
    		   return false;
    	   }
       }
       else if (zombies.size() > humans.size())
       {
    	   if (rnd < 20)
    	   {
    		   killZombie(z); 
    		   return true;
    	   }
    	   else 
    	   {
    		   killHuman(h);
    		   return false;
    	   }
       }
       else 
	   {
		   killHuman(h);
		   return false;
	   }
   }
   public boolean checkForHumans(GameToken g)//Method to check if if there are humans to kill
   {                                                          //If there is, the human is replaced by a dead human.

	   int x = g.getGridX();
	   int y = g.getGridY();
	   int z = g.getDirection();
	   for (GameToken h : humans) //Priority on humans on the same spot
	   {
		   if (h.getGridX() == x && h.getGridY() == y)
		   {
			   if (counterAttack(g,h) == true)
			   {
				   ((Human) h).setKilled(true);
				   return false;
			   }
			   else
			   {
				   return true;
			   }
					   
		   }
	   }
	   
	   for (GameToken h : humans) //Then check for humans in front
	   {
		   if (z == 0)
		   {
			   if (h.getGridX() == x && h.getGridY() == y-1)
			   {
				   if (counterAttack(g,h) == true)
				   {
					   ((Human) h).setKilled(true);
					   return false;
				   }
				   else
				   {
					   return true;
				   }
			   }
		   }
		   else if (z == 1)
		   {
			   if (h.getGridX() == x+1 && h.getGridY() == y)
			   {
				   if (counterAttack(g,h) == true)
				   {
					   ((Human) h).setKilled(true);
					   return false;
				   }
				   else
				   {
					   return true;
				   }
			   }
		   }
		   else if (z == 2)
		   {
			   if (h.getGridX() == x && h.getGridY() == y+1)
			   {
				   if (counterAttack(g,h) == true)
				   {
					   ((Human) h).setKilled(true);
					   return false;
				   }
				   else
				   {
					   return true;
				   }
			   }
		   }
		   else if (z == 3)
		   {
			   if (h.getGridX() == x-1 && h.getGridY() == y)
			   {
				   if (counterAttack(g,h) == true)
				   {
					   ((Human) h).setKilled(true);
					   return false;
				   }
				   else
				   {
					   return true;
				   }
			   }
		   }
		   else
		   {
			   System.out.println("Something went wrong - tokenInFront method checkforhumans");
			   System.out.println(z);
		   }
	   }
   
	   return true;
   } 

   class RandomButtonListener implements ActionListener
   {
      Random random;

      public RandomButtonListener()
      {
         super();
         random = new Random();
      }

      @Override
      public void actionPerformed(ActionEvent event)
      {
         int rnd;

         Object obj = event.getSource();

         if (obj == stepButton  || obj == timer)
         {
        
             for (int i = 0; i < deadHumans.size(); i++) //Moves the death timer for dead humans and converts to zombies if the death timer is at 0.
             {
            	DeadHuman d = (DeadHuman) deadHumans.get(i);
             	int timer = d.getTimer();
             	d.setTimer(timer-1);
             	if (d.getTimer() == 0)
             	{
             		int dir = d.getDirection();
             		int x = d.getGridX();
             		int y = d.getGridY();
             		
             		Zombie z = new Zombie(x,y);
             		zombies.add(z);
             		z.setDirection(dir);
             		deadHumans.remove(d);
             		cityPanel.remove(d);
             		cityPanel.add(z);
             	}
             }
             

        	for (int i = 0; i < 2; i++)//Humans move twice as fast as zombies
        	{
        		for (GameToken h : humans)
                {
        			((Human) h).setKilled(false);
                    rnd = random.nextInt(100);
                    int gridX = h.getGridX();
                    int gridY = h.getGridY();
                    if (zombieInFront(h) != null)
                    {
                    	fightOrFlight(h, zombieInFront(h));
                    }
                    else if (zombieInView(h))
                    {
                    	h.turnAround();
                    	h.move();
                    }
                    else
                    {
                    	   if (rnd < 20) //20% chance of changing direction
                           {
                               rnd = random.nextInt(100);
                               if (rnd < 25)
                               {
                               	if (h.getDirection() == 0)
                               	{
                                   	h.setDirection("SOUTH");
                               	}
                               	else
                               	{
                               	h.setDirection("NORTH");
                               	}
                               }
                               else if (rnd < 50)
                               {
                                 	if (h.getDirection() == 1)
                               	{
                                   	h.setDirection("WEST");
                               	}
                               	else
                               	{
                               	h.setDirection("EAST");
                               	}
                               }
                               else if (rnd < 75)
                               {
                                 	if (h.getDirection() == 2)
                               	{
                                   	h.setDirection("NORTH");
                               	}
                               	else
                               	{
                               	h.setDirection("SOUTH");
                               	}
                               }
                               else
                               {
                                 	if (h.getDirection() == 3)
                               	{
                                   	h.setDirection("EAST");
                               	}
                               	else
                               	{
                               	h.setDirection("WEST");
                               	}
                               }                             
                           }
                           
                           while (buildingInFront(h) == true) //Make sure the token does not go into a building
                           {
                               h.turn();
                           }
                         
                           	h.move();   
                           
                       }
                    }
            	}
                           
            
            
	            for (int i = 0; i < zombies.size(); i++)
	            {
	            	Zombie z = (Zombie) zombies.get(i);
	                rnd = random.nextInt(100);
	                int gridX = z.getGridX();
	                int gridY = z.getGridY();
	                if (checkForHumans(z) == true)
	                {
	                	if (rnd < 25) //25% chance of changing direction
	                    {
	                        rnd = random.nextInt(100);
	                        if (rnd < 25)
	                        {
	                        	if (z.getDirection() == 0)
	                        	{
	                            	z.setDirection("SOUTH");
	                        	}
	                        	else
	                        	{
	                        		z.setDirection("NORTH");
	                        	}
	                        }
	                        else if (rnd < 50)
	                        {
	                          	if (z.getDirection() == 1)
	                        	{
	                            	z.setDirection("WEST");
	                        	}
	                        	else
	                        	{
	                        		z.setDirection("EAST");
	                        	}
	                        }
	                        else if (rnd < 75)
	                        {
	                          	if (z.getDirection() == 2)
	                        	{
	                            	z.setDirection("NORTH");
	                        	}
	                        	else
	                        	{
	                        		z.setDirection("SOUTH");
	                        	}
	                        }
	                        else
	                        {
	                          	if (z.getDirection() == 3)
	                        	{
	                            	z.setDirection("EAST");
	                        	}
	                        	else
	                        	{
	                        		z.setDirection("WEST");
	                        	}
	                        }                             
	                    }
	                }
                                
                while (buildingInFront(z) == true) //Make sure the token does not go into a building
                {
                    z.turn();
                }
              
                	z.move();  
                
                }
         }
            
            SimFrame.this.repaint();
       	 	if (humans.size() == 0)//Check if zombies have won
       	 	{
       	 		timer.stop();
       	 		startButton.setEnabled(false);
       	 		stopButton.setEnabled(false);
       	 		stepButton.setEnabled(false);
       	 		JOptionPane.showMessageDialog(null, "All humans are dead and the simulation is over.");
       	 	}
       	 	if (zombies.size() == 0 && deadHumans.size() == 0)//Check if zombies have won
       	 	{
       	 		timer.stop();
       	 		startButton.setEnabled(false);
       	 		stopButton.setEnabled(false);
       	 		stepButton.setEnabled(false);
       	 		JOptionPane.showMessageDialog(null, "All Zombies are dead and the simulation is over.");
       	 	}
         }
      }
   //listener for button actions
   class ButtonListener implements ActionListener
   {
      public void actionPerformed(ActionEvent event)
      {
         Object obj = event.getSource();
         //Gives function to the start and stop buttons 
         if (obj == startButton)
         {
        	 timer.restart();
        	 startButton.setEnabled(false);
        	 stopButton.setEnabled(true);
       

         }
         else if (obj == stopButton)
         {
        	 timer.stop();
        	 startButton.setEnabled(true);
        	 stopButton.setEnabled(false);
        	 
         }
         else if (obj == restartButton)
         {
        	 timer.stop();
        	 buildings.clear();
        	 zombies.clear();
        	 humans.clear();
        	 deadHumans.clear();
        	 deadZombies.clear();
        	 cityPanel.removeAll();
             tokenLoader("City.map");
        	 startButton.setEnabled(true);
        	 stopButton.setEnabled(false);
        	 stepButton.setEnabled(true);
             SimFrame.this.repaint();
         }
         else
         {
            System.out.println("Not sure what happened: " + event);
         }
      }
   }
   
   }

   

