package org.usfirst.frc.team4537.robot.Controllers;

import edu.wpi.first.wpilibj.*;

public class ArcadeTwoDrivers extends DriverController{
	private Joystick driverStick;
	private Joystick operatorStick;
	
	public double getSlider() {
		return driverStick.getRawAxis(3);
	}
	
	public ArcadeTwoDrivers() {
		driverStick = new Joystick(0);
		operatorStick = new Joystick(1);
	}
	
	@Override
	public int getDriveMode() {
		return ARCADE;
	}

	@Override
	public Joystick getDefaultJoystick() {
		return driverStick;
	}
	
	@Override
	public boolean canChangeSpeed() {
		return false;
	}

	@Override
	public Joystick getJoystick(int id)	{
		if (id == 0) return driverStick;
		else return operatorStick;
	}

	@Override
	public boolean isShooterSpinning() {
		return operatorStick.getRawButton(1);
	}

	@Override
	public boolean isShooting() {
		return operatorStick.getRawButton(0);
	}

	@Override
	public boolean canSwapDirection() {
		return false;
	}

	@Override
	public double getSpeed () {
		return Math.abs(driverStick.getRawAxis(1));
	}

	@Override
	public boolean getCameraSwap() {
		return false;
	}

	@Override
	public boolean isBraking() {
		if(driverStick.getRawAxis(1) == 0) {
			return true;
		}
		else return false;
	}

	@Override
	public double getAngle() {
		return 0;
	}



	@Override
	public int getBallGrabberDirection() {
		return STOPPED;
	}

	@Override
	public boolean raiseClimber() {
		return driverStick.getRawButton(1);
	}

	@Override
	public int getWinchDirection() {
		if(driverStick.getRawButton(1) == true) {
			return FORWARD;
		}
		else return REVERSE;
	}

	@Override
	public boolean isTowerUp() {
		return false;
	}

	@Override
	public double getCameraX() {
		return 1;
	}

	@Override
	public double getCameraY() {
		return 1;
	}
}
