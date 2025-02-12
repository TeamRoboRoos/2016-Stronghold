package org.usfirst.frc.team4537.robot.Controllers;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.Joystick.RumbleType;
public class PS4OneDriver extends DriverController {

	private Joystick stick;
	
	private final int circle = 2;				//Circle button PS4 Controller.
	private final int triangle = 4;				//Triangle button PS4 Controller.
	private final int leftShoulder = 5;			//R1 button PS4 Controller.
	private final int rightShoulder = 6;		//R2 button PS4 Controller.
	private final double leftButtonTurn = -0.5;
	private final double rightButtonTurn = 0.5;

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
		double speed = stick.getRawAxis(2) - stick.getRawAxis(3);		//Gets rid of the motor problem with holding down L2 and R2.
		return speed;
	}

	@Override
	public double getAngle() {
		double angle = 0;
		
		if(Math.abs(stick.getRawAxis(0)) < 0.03) { 		//if the left thumbstick isn't moving
			if (stick.getRawButton(leftShoulder)) {		//and if the left shoulder is being pressed
				angle += -0.3;							//turn anti-clockwise slowly
				System.out.println("leftShoulder");
			}
			
			if (stick.getRawButton(rightShoulder)) {	//and if the right shoulder is being pressed
				angle += 0.3;							//turn clockwise slowly
				System.out.println("rightShoulder");

				angle += leftButtonTurn;	
				System.out.println("Left " + angle);//turn anti-clockwise slowly
			}
			
			if (stick.getRawButton(rightShoulder)) {	//and if the right shoulder is being pressed
				angle += rightButtonTurn;	
				System.out.println("Right " + angle);//turn clockwise slowly

			}
		}
		else {											//if none of the buttons are being pressed and the thumbstick is moving
			angle = stick.getRawAxis(0);			//the angle is the thumbstick value
		}
		return angle;
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
	public boolean triggerClimber() {
		return false;
	}

	@Override
	public boolean detriggerClimber() {
		return false;
	}

	@Override
	public boolean raiseClimber() {
		return false;
	}

	@Override
	public boolean lowerClimber() {
		return false;
	}
}
