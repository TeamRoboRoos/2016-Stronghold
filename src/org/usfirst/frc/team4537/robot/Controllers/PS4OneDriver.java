package org.usfirst.frc.team4537.robot.Controllers;
import edu.wpi.first.wpilibj.*;

public class PS4OneDriver extends DriverController {

	private Joystick stick;
	
	private final int circle = 2;				//Circle button PS4 Controller.
	private final int triangle = 4;				//Triangle button PS4 Controller.
	//private final int leftShoulder = 5;			//R1 button PS4 Controller.
	//private final int rightShoulder = 6;		//R2 button PS4 Controller.

	public PS4OneDriver(){
		stick = new Joystick(0);
	}
	
	@Override
	public int getDriveMode()
	{
		return PS4;
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
		return stick.getRawButton(triangle);
	}

	@Override
	public boolean canChangeSpeed() {
		return stick.getRawButton(circle);
	}

	@Override
	public double getSpeed() {
		//Gets rid of the motor problem with holding down L2 and R2.
		double speed = 	stick.getRawAxis(2) - stick.getRawAxis(3); 
		return speed;
	}

	@Override
	public double getAngle() {
		return stick.getRawAxis(0) * -1;
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
