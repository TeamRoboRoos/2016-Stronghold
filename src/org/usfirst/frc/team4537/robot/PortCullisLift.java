package org.usfirst.frc.team4537.robot;
import org.usfirst.frc.team4537.robot.Controllers.DriverController;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Relay.Direction;


public class PortCullisLift {

	private Robot robot;
	private final int BALL_GRABBER_LEFT= 1;
	private final int BALL_GRABBER_RIGHT= 2;
	private Talon leftMotor;
	private Talon rightMotor;
	
	private double grabberRaiseVariant = 0.4;
	private double grabberLowerVariant = 0.25;
	
	private int currentState = 0;
	
	public PortCullisLift(Robot robot){
		
		this.robot = robot;
		
		leftMotor = new Talon(BALL_GRABBER_LEFT);
		rightMotor = new Talon(BALL_GRABBER_RIGHT);
	}
	
	
	public void operatorControl (){
	
		double grabberDirection = robot.getController().getBallGrabberMovement();
		
		if (grabberDirection != 0)
		{
			if (currentState == 0)
			{
				currentState = 1;
			}
			else if (currentState == 1)
			{
				// Lower ballGrabber down
				currentState = 2;
			}
			else if (currentState == 2)
			{
				// Drive toward port cullis
				currentState = 3;
			}
			else if (currentState == 3)
			{
				grabberDirection = grabberDirection * grabberRaiseVariant;
				robot.getDriveBase().driveForwardForTime(0.5, 2);
				currentState = 4;
			}
			else if (currentState == 4){
				if (!robot.getDriveBase().isDriving())
				{
					currentState = 5;
				}
			}
		}
		else
		{
			currentState = 0;
		}
		
		/*
		if (grabberDirection > 0) { 
			grabberDirection = grabberDirection * grabberLowerVariant; 
			robot.getDriveBase().driveForwardForTime(0.2, 0.5);
			grabberDirection = grabberDirection * grabberRaiseVariant;
			robot.getDriveBase().driveForwardForTime(0.5, 2);
		}
		
		else if(grabberDirection < 0) {
			grabberDirection = grabberDirection * grabberRaiseVariant;
			robot.getDriveBase().driveForwardForTime(0.5, 2);
		}

		leftMotor.set(grabberDirection);
		rightMotor.set(grabberDirection);
		*/
	}
}
