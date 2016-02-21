package org.usfirst.frc.team4537.robot.Controllers;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.Relay.Value;

public class ArcadeTwoDriversPS4 extends DriverController{
	private Joystick driverStick;
	private Joystick operatorPS4;
	
	public double getSlider() {
		return driverStick.getRawAxis(3);
	}
	
	public ArcadeTwoDriversPS4() {
		driverStick = new Joystick(0);
		operatorPS4 = new Joystick(1);
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
		else return operatorPS4;
	}

	@Override
	public boolean isShooterSpinning() {
		return operatorPS4.getRawButton(1);
	}

	@Override
	public boolean isShooting() {
		return operatorPS4.getRawButton(0);
	}

	@Override
	public boolean canSwapDirection() {
		return driverStick.getRawButton(3);
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
		return (operatorPS4.getRawButton(4) && operatorPS4.getRawButton(2));
	}
	
	@Override
	public boolean lowerClimber() {
		return operatorPS4.getRawButton(2);
	}
	
	/*
	@Override
	public int getWinchDirection() {
		if(operatorPS4.getRawButton(2) == true) {
			return FORWARD;
		}
		else return REVERSE;
	}
	
	*/
	
	@Override
	public boolean triggerClimber() {
		return (operatorPS4.getRawButton(5) && operatorPS4.getRawButton(6));
	}
	
	@Override
	public boolean detriggerClimber() {
		return (operatorPS4.getRawButton(9) && operatorPS4.getRawButton(10));
	}
	
	@Override
	public double getBallGrabberMovement() {
		double direction = 0;

		if(operatorPS4.getPOV() == 0)
		{
			direction = -1;
		}
		else if(operatorPS4.getPOV() == 180)
		{
			direction = .6;
		}
		
		return direction;
	}
	
	@Override
	public Value getRollerDirection(){
		Value direction = Relay.Value.kOff;//set the inital value of direction to Off
		
		if(operatorPS4.getRawButton(1))
		{
			direction = Relay.Value.kForward; // if POV is forward (up) set direction to Forward
		}
		else if(operatorPS4.getRawButton(3))
		{
			direction = Relay.Value.kReverse;// if POV is backwards (down) set direction to Reverse
		}
		
		return direction;
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
