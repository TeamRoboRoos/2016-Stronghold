package org.usfirst.frc.team4537.robot.Controllers;
import edu.wpi.first.wpilibj.*;

public class ArcadeOneDriver extends DriverController {

	private Joystick stick;
	
	public ArcadeOneDriver(){
		stick = new Joystick(0);
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
	public boolean isBallGrabberChangingPosition() {
		return stick.getRawButton(2);
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
		return false;
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
