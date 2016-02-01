package org.usfirst.frc.team4537.robot;

//import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Watcher implements Runnable
{
	private Robot robot;
	
	private boolean stopThread = false;
	
	public Watcher(Robot robot)
	{
		this.robot = robot;		
	}
	
	public void stopThread()
	{
		this.stopThread = true;
	}
	
	public void run() 
	{
	    System.out.println("Thread begun");
		try 
		{
	         while (true) 
	         {
	        	 if (this.stopThread == true) 
	        	 {
	        		 throw new InterruptedException("Stopped");
	        	 }
	        	 if (this.robot.getCamera() != null)
	        		 this.robot.getCamera().pushFrame();
	         }
	     } 
		catch (InterruptedException e) 
		{
			
		}
    	SmartDashboard.putBoolean("DB/LED 0", false);
	}
}
