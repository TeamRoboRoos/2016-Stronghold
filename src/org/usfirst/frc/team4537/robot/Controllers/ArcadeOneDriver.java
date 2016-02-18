package org.usfirst.frc.team4537.robot.Controllers;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.Relay.Value;

public class ArcadeOneDriver extends DriverController {

	private Joystick stick;
	
	public ArcadeOneDriver(){
		stick = new Joystick(0);
	}
	
	//Used for relay roller
	@Override
	public Value getRollerDirection(){
		Value direction = Relay.Value.kOff;//set the inital value of direction to Off
		
		if(stick.getRawButton(1))
		{
			direction = Relay.Value.kForward; // if POV is forward (up) set direction to Forward
		}
		else if(stick.getRawButton(2))
		{
			direction = Relay.Value.kReverse;// if POV is backwards (down) set direction to Reverse
		}
		
		return direction;
	}
	
	@Override
	public int getDriveMode()
	{
		return ARCADE;
	}
	
	@Override
	public Joystick getDefaultJoystick() {
		return stick;
	}

	@Override
	public Joystick getJoystick(int id)
	{
		return stick;
	}
	
	@Override
	public boolean canChangeSpeed() {
		return false;
	}

	@Override
	public boolean isShooterSpinning() {
		return stick.getRawButton(1);
	}

	@Override
	public boolean isShooting() {
		return stick.getRawButton(0);
	}

	@Override
	public double getBallGrabberMovement() {
		double direction = 0;

		if(stick.getPOV() == 0)
		{
			direction = -1;
		}
		else if(stick.getPOV() == 180)
		{
			direction = 1;
		}
		
		return direction;
	}

	@Override
	public double getCameraX() {
		return 0;
	}

	@Override
	public double getCameraY() {
		return 0;
	}

	@Override
	public boolean isTowerUp() {
		return stick.getRawButton(5);
	}

	@Override
	public boolean canSwapDirection() {
		if(stick.getRawButton(3)){
			System.out.println("SWAPDIRECTION");
			return true;
		}else{
			return false;
		}
	}

	@Override
	public double getSpeed() {
		return 0;
	}

	@Override
	public double getAngle() {
		return 0;
	}

	@Override
	public boolean isBraking() {
		return false;
	}

	@Override
	public boolean getCameraSwap(){
		return false;
	}

	@Override
	public int getBallGrabberDirection(){
		return 0;
	}

	@Override
	public int getWinchDirection() {
		return 0;
	}

	@Override
	public boolean raiseClimber() {
		return false;
	}
}
