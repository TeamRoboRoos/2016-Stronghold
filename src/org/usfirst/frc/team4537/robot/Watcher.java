package org.usfirst.frc.team4537.robot;

//import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Watcher implements Runnable
{
	private Robot robot;
	
	private boolean stopThread = false;
	
	private byte lastBatteryCharge;
	
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
	        	 
	        	 byte currentBatteryCharge = (byte)(Math.round(this.robot.getDriverStation().getBatteryVoltage() * 10) * -1);
	        	 
	        	 if (currentBatteryCharge != lastBatteryCharge)
	        	 {
	        		 this.robot.getRioduino().send(currentBatteryCharge);
	        		 lastBatteryCharge = currentBatteryCharge;
	        	 }
	        	 
	        	 System.out.println(currentBatteryCharge);
	        	 //this.robot.getRioduino().send(b);
	         }
	     } 
		catch (InterruptedException e) 
		{
			
		}
    	SmartDashboard.putBoolean("DB/LED 0", false);
	}
}
