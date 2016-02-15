package org.usfirst.frc.team4537.robot;

import org.usfirst.frc.team4537.robot.Controllers.DriverController;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Relay.Direction;

public class BallGrabber {
	private Robot robot;
	private final int ROLLER = 0;
	private final int BALL_GRABBER_LEFT= 1;
	private Talon leftMotor;
	private Talon rightMotor;
	private final int BALL_GRABBER_RIGHT=2;
	private Relay rollerRelay;
	private int[] encoderPins = {5,6}; // encoder pins {a,b}
	private Encoder encoder;
	
	private double grabberRaiseVariant = 0.4;
	private double grabberLowerVariant = 0.25;
	
	
	private int[] angles = {0, 90};
	
	public BallGrabber(Robot robot)
	{
		this.robot = robot;
		
		rollerRelay = new Relay(ROLLER,Direction.kBoth);
		
		leftMotor = new Talon(BALL_GRABBER_LEFT);
		rightMotor = new Talon(BALL_GRABBER_RIGHT);
		
		encoder = new Encoder(encoderPins[0],encoderPins[1]);
	}
	
	public void operatorControl() {
		Relay.Value rollerDirection = robot.getController().getRollerDirection();
		rollerRelay.set(rollerDirection);
		
		double grabberDirection = robot.getController().getBallGrabberMovement();
		
		if (grabberDirection > 0) { grabberDirection = grabberDirection * grabberLowerVariant; }
		if (grabberDirection < 0) { grabberDirection = grabberDirection * grabberRaiseVariant; }
		
		System.out.println(grabberDirection);
		
		leftMotor.set(grabberDirection);
		rightMotor.set(grabberDirection);
	}
	
}
