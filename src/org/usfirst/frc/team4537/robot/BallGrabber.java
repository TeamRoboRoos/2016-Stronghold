package org.usfirst.frc.team4537.robot;

import org.usfirst.frc.team4537.robot.Controllers.DriverController;

import edu.wpi.first.wpilibj.DigitalInput;
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
	private int[] encoderPins = {0, 1}; // encoder pins {a,b}
	private Encoder encoder;
	
	private final int UPPER_LIMITER = 2;
	
	private double grabberRaiseVariant = 0.4;
	private double grabberLowerVariant = 0.25;
	
	public static final int FULLY_DOWN = 0;
	public static final int SHOOTING = 0;
	public static final int GRABBING = 0;
	public static final int FULLY_UP = 0;
	
	private boolean isMoving = false;
	private int targetPosition = 0;
	private DigitalInput topLimiter;
	
	public BallGrabber(Robot robot)
	{
		this.robot = robot;
		
		rollerMotor = new Talon(ROLLER);
		
		ballGrabberRaiseLowerMotor = new Talon(BALL_GRABBER_TALON_ID);
		
		this.topLimiter = new DigitalInput(UPPER_LIMITER);
		
		encoder = new Encoder(encoderPins[0],encoderPins[1]);
		encoder.reset();
	}
	
	public void operatorControl() {
		Relay.Value rollerDirection = robot.getController().getRollerDirection();
		if (rollerDirection == Relay.Value.kForward)
		{
			rollerMotor.set(1);
		}
		else if (rollerDirection == Relay.Value.kReverse)
		{
			rollerMotor.set(-1);
		}
		else
		{
			rollerMotor.set(0);
		}
		
		double grabberDirection = robot.getController().getBallGrabberMovement();

		if (grabberDirection > 0) { grabberDirection = grabberDirection * grabberLowerVariant; }
		if (grabberDirection < 0) { grabberDirection = grabberDirection * grabberRaiseVariant; }
		
		if (!this.topLimiter.get()) 
		{
			if (grabberDirection > 0) grabberDirection = 0;
			encoder.reset();
		}
		
		System.out.println("a:" + encoder.get());
		
		ballGrabberRaiseLowerMotor.set(grabberDirection);
		
	}
	
	public void moveToPosition(int position)
	{
		if (!this.isMoving)
		{
			this.isMoving = true;
			this.targetPosition = position;
		}
	}
	
	public boolean isMoving()
	{
		return this.isMoving;
	}
	
	public void reset()
	{
		if (!this.isMoving)
		{
			this.isMoving = true;
		}
	}
	
}
