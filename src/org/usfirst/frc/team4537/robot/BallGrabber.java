package org.usfirst.frc.team4537.robot;

import org.usfirst.frc.team4537.robot.Controllers.DriverController;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Relay.Direction;

public class BallGrabber {
	private Robot robot;
	private final int ROLLER = 2;
	private final int BALL_GRABBER_TALON_ID = 1;
	private Talon ballGrabberRaiseLowerMotor;
	private Talon rollerMotor;
	private int[] encoderPins = {5,6}; // encoder pins {a,b}
	private Encoder encoder;
	
	private double grabberRaiseVariant = 0.4;
	private double grabberLowerVariant = 0.25;
	
	
	private int[] angles = {0, 90};
	
	public BallGrabber(Robot robot)
	{
		this.robot = robot;
		
		rollerMotor = new Talon(ROLLER);
		
		ballGrabberRaiseLowerMotor = new Talon(BALL_GRABBER_TALON_ID);
		
		encoder = new Encoder(encoderPins[0],encoderPins[1]);
	}
	
	public void operatorControl() {
		Relay.Value rollerDirection = robot.getController().getRollerDirection();
		if (rollerDirection == Relay.Value.kForward)
			rollerMotor.set(-1);
		else if (rollerDirection == Relay.Value.kReverse)
			rollerMotor.set(1);
		else
			rollerMotor.set(0);
		
		double grabberDirection = robot.getController().getBallGrabberMovement();
		
		if (grabberDirection > 0) { grabberDirection = grabberDirection * grabberLowerVariant; }
		if (grabberDirection < 0) { grabberDirection = grabberDirection * grabberRaiseVariant; }
		
		System.out.println(grabberDirection);
		
		ballGrabberRaiseLowerMotor.set(grabberDirection);
	}
	
	public void lowerBallGrabber()
	{
	}
	
	public void raiseBallGrabber()
	{
	}
}
