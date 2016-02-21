package org.usfirst.frc.team4537.robot.Controllers;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.Relay.Value;

public abstract class DriverController {

	//For Ball Pickup 
	public static final int FORWARD = 1;
	public static final int REVERSE = -1;
	
	public static final int STOPPED = 0;
	
	public static final int UP = 1;
	public static final int DOWN = -1;
	
	public static final int IN = 1;
	public static final int OUT = -1;
	
	public static final int TANK = 1;
	public static final int ARCADE = 2;
	public static final int PS4 = 3;
	
	public abstract int getDriveMode();
	
	//drive
	public abstract boolean canSwapDirection();
	public abstract double getSpeed();
	public abstract double getAngle();
	public abstract boolean isBraking();
	
	public abstract boolean canChangeSpeed();
	
	public double getMaxSpeed() { return -1; }
	
	//ball pickup
	public double getBallGrabberMovement(){
		return 0.0;
	};
	public abstract int getBallGrabberDirection();
	
	//ball shooter
	public abstract boolean isShooterSpinning();
	public abstract boolean isShooting();
	
	//climber
	public abstract boolean raiseClimber();
	public abstract boolean lowerClimber();
	public abstract boolean triggerClimber();
	public abstract boolean detriggerClimber();
	
	//cameras and sensors
	public abstract double getCameraX();
	public abstract double getCameraY();
	public abstract boolean getCameraSwap();
	
	public abstract boolean isTowerUp();
	
	//joysticks
	public abstract Joystick getDefaultJoystick();
	public abstract Joystick getJoystick(int id);
	
	public Value getRollerDirection(){
		return Relay.Value.kOff;
	};
	
}
