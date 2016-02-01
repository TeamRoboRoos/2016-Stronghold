package org.usfirst.frc.team4537.robot.Controllers;

import edu.wpi.first.wpilibj.*;

public class TankDrive extends DriverController {
	
	private Joystick leftStick;
	private Joystick rightStick; 

	public TankDrive() {
		leftStick = new Joystick(0);
		rightStick = new Joystick(1);
	}

	@Override
	public int getDriveMode() {
		return TANK;
	}

	@Override
	public Joystick getDefaultJoystick() {
		return leftStick;
	}

	@Override
	public Joystick getJoystick(int id) {
		if (id == 0) return leftStick;
		else return rightStick;
	}

	@Override
	public boolean isShooterSpinning() {
		return leftStick.getRawButton(0);
	}

	@Override
	public boolean isShooting() {
		return rightStick.getRawButton(0);
	}

	@Override
	public boolean raiseClimber() {
		return false;
	}
	
	@Override
	public boolean canChangeSpeed() {
		return false;
	}

	@Override
	public boolean isBallGrabberChangingPosition() {
		return false;
	}

	@Override
	public boolean canSwapDirection() {
		return false;
	}

	@Override
	public boolean isTowerUp() {
		return false;
	}

	@Override
	public boolean getCameraSwap() {
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
	public double getCameraX() {
		return 0;
	}

	@Override
	public double getCameraY() {
		return 0;
	}

	@Override
	public int getBallGrabberDirection() {
		return STOPPED;
	}

	@Override
	public int getWinchDirection() {
		return STOPPED;
	}

	@Override
	public boolean isBraking() {
		return false;
	}
}
