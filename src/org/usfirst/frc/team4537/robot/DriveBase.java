package org.usfirst.frc.team4537.robot;

import org.usfirst.frc.team4537.robot.Controllers.DriverController;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.Joystick.AxisType;
import edu.wpi.first.wpilibj.Joystick.RumbleType;
import edu.wpi.first.wpilibj.RobotDrive.MotorType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveBase {

	// These are needed to set up the robot.
	private RobotDrive robotDrive; // The drive base for control
	private Robot robot; // A reference to the base robot class

	// ----------------------------------------------------------------------
	// The motors - each needs the correct CAN ID to be set

	private CANTalon leftFront;
	private CANTalon leftRear;
	private CANTalon rightFront;
	private CANTalon rightRear;

	// Can change for left motor, or left rear motor (if there are four)
	private final int LEFT_REAR_MOTOR = 11;

	// Can change for right motor, or right rear motor (if there are four)
	private final int RIGHT_REAR_MOTOR = 21;

	// Can change for left front motor (only if four motors are attached)
	private final int LEFT_FRONT_MOTOR = 10;

	// Can change for right front motor (only if four motors are attached)
	private final int RIGHT_FRONT_MOTOR = 20;

	// Set to -1 to reverse the default direction`
	private int motorDirection = 1;

	// Set to -1 to reverse the turn direction
	private int TURN_DIRECTION = -1;

	// Speed limited. Set to 1 to make Maddy happy. Set below 1 to make Maddy
	// sad.
	private final double MAX_SPEED = 1;

	// Limits the maxiumum turning speed. 1 is max turning speed.
	private final double MAX_TURN_SPEED = 0.65;

	// ----------------------------------------------------------------------
	// When we play with setting the maximum possible acceleration, we'll
	// need to adjust this. At the moment, it may be worth ignoring for a bit.
	private final double MAX_ACCELERATION = 0.03;

	// Modify this to test the acceleration limiters - probably
	// worth waiting until we confirm the code.
	private boolean limitMaxAcceleration = false;

	// --------------------------------------false--------------------------------
	// Various internal variables used to run the drivebase
	private boolean driving; // Is it currently moving in autonomous?
	private double finishTime; // When is it due to finish moving?
	private double targetDistance; // What range should it stop at?

	private boolean didChangeDirection; // Have we just swapped direction in
										// teleop?

	private double currentMaxSpeed; // What is the maximum speed available to
									// us?
	private boolean didChangeSpeed; // Did the driver recently change speed?

	private double previousSpeedLeft; // Used for acceleration limiter - what
										// was the last left motor speed?
	private double previousSpeedRight; // Used for acceleration limiter - what
										// was the last right motor speed?
	private double previousTurnSpeed; // Used for acceleration limiter - what
										// was the last turn speed?
	private boolean changedDirections = false;

	private boolean isActiveBraking = false;
	private int lastActiveBrakingCount = 0;
	private double activeBrakingSpeed = 0;
	private int lastActiveBrakingCountLeft = 0;
	private double activeBrakingSpeedLeft = 0;
	private int lastActiveBrakingCountRight = 0;
	private double activeBrakingSpeedRight = 0;
	
	private double voltageDraw;
	private double currentDraw;
	
	private int finishEncoderValue;
	private int startEncoderValue;
	private final double ENCODER_VALUE = -84.45;
	
	private boolean robCurve = false;

	// ----------------------------------------------------------------------
	// Constructors
	// ----------------------------------------------------------------------

	/**
	 * Default constructor - handles basic configuration
	 * 
	 * @param robot
	 *            a reference to the base robot class.
	 */
	public DriveBase(Robot robot) {
		// store the reference to robot.
		this.robot = robot;

		this.leftFront = new CANTalon(LEFT_FRONT_MOTOR);
		this.leftRear = new CANTalon(LEFT_REAR_MOTOR);
		this.rightFront = new CANTalon(RIGHT_FRONT_MOTOR);
		this.rightRear = new CANTalon(RIGHT_REAR_MOTOR);

		// Four motor drive. If the CAN IDs are properly set, you won't need to
		// do anything here.
		robotDrive = new RobotDrive(leftFront, leftRear, rightFront, rightRear);

		// robotDrive = new RobotDrive(new CANTalon(LEFT_FRONT_MOTOR), new
		// CANTalon(LEFT_REAR_MOTOR));

		// Inverts the motors. You will need to set this, in order to make the
		// wheels match direction. Commented out for now due to an overabundance
		// of caution, but change to get the robot running (or make electrical
		// handle this)

		// robotDrive.setInvertedMotor(MotorType.kRearLeft, true);
		// robotDrive.setInvertedMotor(MotorType.kFrontLeft, true);

		// Just sets what direction is forward by default.
		motorDirection = -1;

		// Set the current max speed to the default.
		currentMaxSpeed = MAX_SPEED;

		// Used to dected if we swapped speed from fast to slow, or slow to
		// fast. Slow mode halves
		// max speed, but also drops torque.
		didChangeSpeed = false;

		// Used to handled the acceleration limiter.
		previousSpeedLeft = 0;
		previousSpeedRight = 0;

		// Configure the encoders

	}

	// ----------------------------------------------------------------------
	// Driving methods for teleop. Not used in autonomous.
	// ----------------------------------------------------------------------

	// Teleop mode - the human is in control.
	public void operatorControl() {
		// First, grab the joysticks.
		Joystick joystick0 = robot.getController().getJoystick(0);
		Joystick joystick1 = robot.getController().getJoystick(1);

		// Is it in arcade mode?
		if (robot.getController().getDriveMode() == DriverController.ARCADE) {
			/*
			 * if(robot.getController().canSwapDirection() &&
			 * !changedDirections){ motorDirection = motorDirection * -1;
			 * changedDirections = true; }else
			 * if(!robot.getController().canSwapDirection()){ changedDirections
			 * = false; }
			 */
			// Grab the speed as the Y axis on the joystick and convert it (make
			// all the required adjustments)
			double speed = joystick0.getAxis(AxisType.kY);
			if (speed > 0.02 || speed < -0.02) {
				speed = this.convertSpeed(speed);
			} else {
				speed = 0;
			}

			double turnSpeed = joystick0.getThrottle();
			if (turnSpeed > 0.02 || turnSpeed < -0.02) {
				turnSpeed = this.convertSpeed(turnSpeed);
			} else {
				turnSpeed = 0;
			}

			// Finally, if the acceleration limiter is on, limit the max speed
			// permitted
			speed = limitMaxSpeedChange(speed);

			// Grab the turn speed as the X axis on the joystick and convert it
			// (make all the required adjustments)
			// System.out.print("Target Speed: " + turnSpeed);

			if (joystick0.getRawButton(1) == true) {
				speed = speed / 2;
				turnSpeed = turnSpeed / 1.5;
			}

			// System.out.println(" | Speed: " + turnSpeed);
			// Drive the robot in arcade mode.
			double speedSign = Math.signum(speed);
			double minDrivePower = 0.1;
			double minControlPower = 1;
			double maxControlPower = 0.1;
			double maddyPower = 1;
			double tempSpeed = Math.abs(speed);

			if (SmartDashboard.getNumber("DB/Slider 0") <= 1)
				minDrivePower = SmartDashboard.getNumber("DB/Slider 0");
			if (SmartDashboard.getNumber("DB/Slider 1") <= 1)
				minControlPower = SmartDashboard.getNumber("DB/Slider 1");
			if (SmartDashboard.getNumber("DB/Slider 2") <= 1)
				maxControlPower = SmartDashboard.getNumber("DB/Slider 2");
			if (SmartDashboard.getNumber("DB/Slider 3") <= 1)
				maddyPower = SmartDashboard.getNumber("DB/Slider 3");

			double robSpeed = speedSign * ((Math.pow((1 - tempSpeed), 3) * minDrivePower)
					+ (3 * Math.pow((1 - tempSpeed), 2) * tempSpeed * minControlPower)
					+ (3 * (1 - tempSpeed) * Math.pow(tempSpeed, 2) * maxControlPower)
					+ (Math.pow(tempSpeed, 3) * maddyPower));

			speedSign = Math.signum(turnSpeed);
			tempSpeed = Math.abs(turnSpeed);

			double robTurnSpeed = speedSign * ((Math.pow((1 - tempSpeed), 3) * minDrivePower)
					+ (3 * Math.pow((1 - tempSpeed), 2) * tempSpeed * minControlPower)
					+ (3 * (1 - tempSpeed) * Math.pow(tempSpeed, 2) * maxControlPower)
					+ (Math.pow(tempSpeed, 3) * maddyPower));

			if (speed == 0)
				robSpeed = speed;
			if (turnSpeed == 0)
				robTurnSpeed = turnSpeed;

			System.out.println("e:" + rightFront.getEncPosition());

			/*
			if (SmartDashboard.getBoolean("DB/Button 0") != this.robCurve)
			{
				this.robCurve = SmartDashboard.getBoolean("DB/Button 0");
				if (this.robCurve)
				{
			        SmartDashboard.putString("DB/String 8", "Rob curve");
				}
				else
				{
			        SmartDashboard.putString("DB/String 8", "Normal");
				}
			}
			*/
			
			if (this.robCurve)
			{
				this.robotDrive.arcadeDrive(robSpeed, robTurnSpeed * TURN_DIRECTION);
			}
			else
			{
				if (motorDirection == 1)
				{
					this.robotDrive.arcadeDrive(speed, turnSpeed * -1);
				}
				else
				{
					this.robotDrive.arcadeDrive(speed, turnSpeed * 1);
				}
			}

			// Active braking
			if (speed == 0 && turnSpeed == 0 && joystick0.getRawButton(7)) {
				this.activeBraking();
			} else if (this.isActiveBraking) {
				this.robotDrive.drive(0, 0);
				this.isActiveBraking = false;
			} else {
				this.isActiveBraking = false;
			}
			
			double newVoltageDraw = Math.round(leftFront.getOutputVoltage() + rightFront.getOutputVoltage() + leftRear.getOutputVoltage() +  + rightRear.getOutputVoltage() * 100) / 100;
			double newCurrentDraw = Math.round(leftFront.getOutputCurrent() + rightFront.getOutputCurrent() + leftRear.getOutputCurrent() +  + rightRear.getOutputCurrent() * 100) / 100;

			if (newVoltageDraw != voltageDraw || newCurrentDraw != currentDraw)
			{
		        SmartDashboard.putString("DB/String 4", newVoltageDraw + " volts " + newCurrentDraw + " amps");
		        voltageDraw = newVoltageDraw;
		        currentDraw = newCurrentDraw;
			}
		}

		/*
		 * // Is it in tank mode? else if (robot.getController().getDriveMode()
		 * == DriverController.TANK) { // Grab the left and right side motor
		 * speeds as the Y axis on each joystick. double leftSpeed =
		 * convertSpeed(joystick0.getAxis(AxisType.kY)); double rightSpeed =
		 * convertSpeed(joystick1.getAxis(AxisType.kY));
		 * 
		 * // Finally, if the acceleration limiter is on, limit the max speed
		 * permitted. leftSpeed = limitMaxSpeedLeftChange(leftSpeed); rightSpeed
		 * = limitMaxSpeedRightChange(rightSpeed); previousSpeedLeft =
		 * leftSpeed; previousSpeedRight = rightSpeed; // Drive the robot in
		 * tank mode. robotDrive.tankDrive(leftSpeed, rightSpeed, true); }
		 * 
		 * // Are we using Maddy's special PS4 controller mode? else if
		 * (robot.getController().getDriveMode() == DriverController.PS4) { //
		 * Grab the speed as the Y axis on the joystick and convert it (make all
		 * the required adjustments) double speed =
		 * convertSpeed(robot.getController().getSpeed()); // Finally, if the
		 * acceleration limiter is on, limit the max speed permitted. speed =
		 * limitMaxSpeedChange(speed); previousSpeedLeft = speed; // Grab the
		 * turn speed as the X axis on the joystick and convert it (make all the
		 * required adjustments) double turnSpeed =
		 * convertTurn(robot.getController().getAngle()); previousTurnSpeed =
		 * turnSpeed; // Drive the robot. robotDrive.drive(speed, turnSpeed);
		 * 
		 * //joystick0.setRumble(RumbleType.kLeftRumble, (float)
		 * Math.abs(speed)); //joystick0.setRumble(RumbleType.kRightRumble,
		 * (float) Math.abs(speed)); }
		 */

		// Check to see if the operator wants to adjust the maximum possible
		// speed.
		checkMaxSpeedAdjustment();

		// Check to see if the operator wants to drop the speed by half
		checkForHalfSpeed();

		// Check to see if the operator wants to invert the motors
		checkSwapDirection();
	}

	// Runs through most of the adjustments needed for the motor speed. As
	// the acceleration limiter is different for tank drive over other forms,
	// that
	// step has to be handled elsewhere.
	private double convertSpeed(double speed) {
		// Reverse the speed if the robot has been inverted (front is the
		// shooter, not the grabber)
		speed = speed * motorDirection;
		// Modify the speed by the current max speed value (for example, half
		// the speed if currentMaxSpeed is 0.5).
		speed = speed * currentMaxSpeed;

		return speed;
	}

	// Runs through the various adjustments needed for the turning speed.
	private double convertTurn(double turnSpeed) {
		// Reverse the turn speed if the robot has been inverted (front is the
		// shooter, not the grabber)
		turnSpeed = turnSpeed * motorDirection;
		// Reverse the turn speed if the turning has been inverted
		// turnSpeed = turnSpeed * TURN_DIRECTION;
		// Modify the turning speed by the current max speed value (for example,
		// half the speed if currentMaxSpeed is 0.5).
		turnSpeed = turnSpeed * currentMaxSpeed;
		// Modify the turning speed by the preset max turning speed value.
		turnSpeed = turnSpeed * MAX_TURN_SPEED;
		// Finally, if the acceleration limiter is on, limit the max speed
		// permitted.
		turnSpeed = limitMaxTurnSpeedChange(turnSpeed);
		previousTurnSpeed = turnSpeed;
		return turnSpeed;
	}

	private double convertTurnNoAccel(double turnSpeed) {
		// Reverse the turn speed if the robot has been inverted (front is the
		// shooter, not the grabber)
		turnSpeed = turnSpeed * motorDirection;
		// Reverse the turn speed if the turning has been inverted
		turnSpeed = turnSpeed * TURN_DIRECTION;
		// Modify the turning speed by the current max speed value (for example,
		// half the speed if currentMaxSpeed is 0.5).
		turnSpeed = turnSpeed * currentMaxSpeed;
		// Modify the turning speed by the preset max turning speed value.
		turnSpeed = turnSpeed * MAX_TURN_SPEED;
		// Finally, if the acceleration limiter is on, limit the max speed
		// permitted.

		return turnSpeed;
	}

	// Check to see if the maximum speed reported from the controller is
	// different
	// to what is currently recorded, and if it is not equal to -1 (the default
	// value,
	// indicating that there is no ability to set this on the current
	// controller).
	private void checkMaxSpeedAdjustment() {
		if (robot.getController().getMaxSpeed() != currentMaxSpeed && robot.getController().getMaxSpeed() != -1) {
			// Set the current max speed to be that which we pull from the
			// controller.
			currentMaxSpeed = robot.getController().getMaxSpeed();

			// If the controller is asking for something higher than the maximum
			// permitted, set it to
			// the maximum allowed.
			if (currentMaxSpeed > MAX_SPEED)
				currentMaxSpeed = MAX_SPEED;

			// We don;t want negative values, so if it is negative, set it to 0.
			if (currentMaxSpeed < 0)
				currentMaxSpeed = 0;
		}
	}

	// The operator might want to change to half speed. This is a toggle - press
	// for on, press again for off. If used, we need to make sure that
	// holding the button down doesn't constantly swap it back and forth.
	private void checkForHalfSpeed() {
		// Did they ask to change speed, and did that release the button since
		// the last time they asked?
		if (!didChangeSpeed && robot.getController().canChangeSpeed()) {
			// Record that a change was made.
			didChangeSpeed = true;

			// If it is currently going at max, change to half speed.
			if (currentMaxSpeed == MAX_SPEED) {
				currentMaxSpeed = MAX_SPEED * 0.66;
			}
			// Otherwise swap to max speed.
			else {
				currentMaxSpeed = MAX_SPEED;
			}
		}
		// If they didn't ask to change speed, record that they released the
		// button.
		else if (!robot.getController().canChangeSpeed()) {
			didChangeSpeed = false;
		}
	}

	// The operator might want to invert the controller. This handles it at the
	// drivebase level. We could also do it at the controller level, but then
	// we would have to write the code for every controller configuration.
	private void checkSwapDirection() {
		// Check to see if we want to swap directions
		if (robot.getController().canSwapDirection()) {
			// Make sure that the robot is stationary, to reduce strain on the
			// drivetrain.
			if (previousSpeedLeft <= 0.3 && previousSpeedRight <= 0.3 && previousSpeedLeft >= -0.3
					&& previousSpeedRight >= -0.3) {
				this.invertMotors();
			}
		}
		// If we weren't asked to change directions, record this.
		else {
			this.didChangeDirection = false;
		}
	}

	// Switch robot direction - front is now back, back is now front.
	private void invertMotors() {
		if (!didChangeDirection) {
			motorDirection = motorDirection * -1;
			TURN_DIRECTION = TURN_DIRECTION * -1;
			this.didChangeDirection = true;
		}
	}

	// ----------------------------------------------------------------------
	// Driving methods for autonomous. Not used in teleop.
	// ----------------------------------------------------------------------

	/**
	 * Drive forward in autonomous mode.
	 * 
	 * @param speed
	 *            value between -1 (reverse) and 1 (full speed)
	 */
	public void driveForward(double speed) {
		robotDrive.drive(speed, 0);
	}

	/**
	 * Drive forward for a set period of time. Only used in autonomous mode.
	 * 
	 * @param speed
	 *            value between -1 (reverse) and 1 (full speed)
	 * @param seconds
	 *            time to drive forward for. Will stop after the specified
	 *            number of seconds.
	 */
	public void driveForwardForTime(double speed, double seconds) {
		// Have we started driving yet? If not, let's start!
		if (!driving) {
			// Record that we are driving.
			driving = true;

			// Calculate the time that we will finish driving. (Add the number
			// of seconds
			// that we are driving for to the current time.
			this.finishTime = System.currentTimeMillis() + (seconds * 1000);
		}

		// Have we reached the target time?
		if (System.currentTimeMillis() < finishTime) {
			// No. Therefore keep driving at the specified speed.
			this.driveForward(speed);
		}

		else {
			// Yes. Time to stop driving.
			driving = false;
			this.stop();
		}
	}

	/**
	 * Drive forward for a set distance. 
	 * 
	 * @param speed
	 *            value between -1 (reverse) and 1 (full speed)
	 * @param meters
	 *            distance (in meters) to drive.
	 */
	public void driveForwardForDistance(double speed, double centimeters) {
		
		// Safety - can't risk driving fast
		if (speed > 0.5) { speed = 0.5; }		
		
		// Have we started driving yet? If not, let's start!
		if (!driving) {
			// Record that we are driving.
			driving = true;

			// Calculate the distance that we will finish driving. 
			//this.finishEncoderValue = centimeters;
			// Store the current encoder value
			this.startEncoderValue = rightFront.getEncPosition();
			this.finishEncoderValue = this.startEncoderValue + (int)(centimeters * this.ENCODER_VALUE);
		}
		
		// Have we reached the target pings?
		if (this.startEncoderValue > this.finishEncoderValue && rightFront.getEncPosition() > this.finishEncoderValue) {
			// No. Therefore keep driving at the specified speed.
			this.driveForward(speed);
		}
		else if (this.startEncoderValue < this.finishEncoderValue && rightFront.getEncPosition() < this.finishEncoderValue) {
			// No. Therefore keep driving at the specified speed.
			this.driveForward(-speed);
		}

		else {
			// Yes. Time to stop driving.
			driving = false;
			this.stop();
		}
	}

	/**
	 * Drive forward to a set distance from an object. 
	 * 
	 * @param speed		value between -1 (reverse) and 1 (full speed)
	 * @param range 	distance (in meters) to stop at. 
	 */
	public void driveForwardToRange(double speed, double range) {
		// Have we started driving yet? If not, let's start!
		if (!this.driving)
		{
			// Record that we are driving.
			this.driving = true;
			
			// Calculate the target distance that we will finish driving at. 
			targetDistance = range;
			if (targetDistance < 17) targetDistance = 17; // Smallest possible detectable distance
		}
		
		// Safety - can't risk driving fast
		if (speed > 0.5) { speed = 0.5; }
		
		// Grab the current distance
		// Note that this will change if the direction is inverted.
		double currentDistance = Math.round(this.robot.getSensors().getDistanceFront());
		
		double targetSpeed = 0;
		// Have we reached the target range?
		if (currentDistance > targetDistance)
		{
			targetSpeed = speed;
			if (currentDistance < targetDistance + 10)
			{
				targetSpeed = targetSpeed / 2;
			}
		}
		
		// Have we gone too far?
		if (currentDistance < targetDistance)
		{
			targetSpeed = -speed;
			if (currentDistance < targetDistance + 10)
			{
				targetSpeed = targetSpeed / 2;
			}
		}
		
		if (targetSpeed == 0)
		{
			// Yes. Time to stop driving.
			driving = false;
			this.stop();
		}
		else
		{
			this.driveForward(targetSpeed);
		}
	}

	/**
	 * Turn to a set angle using the gyro. Ryan? This is yours. :)
	 * 
	 * @param angle
	 *            specified in degrees
	 */
	public void turnToAngle(double angle) {

	}

	/**
	 * Stop the motors. Primarily for autonomous, but also posisbly good for
	 * teleop.
	 */
	public void stop() {
		driving = false;
		this.driveForward(0);
	}

	/**
	 * Disable the safety on the robot drive - probably not needed with the
	 * itterative model being used for autonomous.
	 * 
	 * The safety stops the motors if no signal is received for 0.01 seconds.
	 */
	public void stopSafety() {
		robotDrive.setSafetyEnabled(false);
	}

	/**
	 * Enable the safety on the robot drive - probably not needed with the
	 * itterative model being used for autonomous.
	 * 
	 * The safety stops the motors if no signal is received for 0.01 seconds.
	 */
	public void startSafety() {
		robotDrive.setSafetyEnabled(true);
	}

	/**
	 * Returns whether or not the robot is performing an action in automonous
	 * mode. For example, will return true if the robot has been told to drive
	 * for 3 seconds, and is yet to complete that task.
	 * 
	 * @return true if driving
	 */
	public boolean isDriving() {
		return driving;
	}

	private void activeBraking() {
		if (!this.isActiveBraking) {
			this.isActiveBraking = true;
			this.lastActiveBrakingCount = this.rightFront.getEncPosition();
			this.activeBrakingSpeed = 0;
		}

		int encoderDifference = this.rightFront.getEncPosition() - this.lastActiveBrakingCount;

		if (encoderDifference < 0) {
			this.lastActiveBrakingCount = this.rightFront.getEncPosition();
			this.activeBrakingSpeed -= encoderDifference * 0.0001;
		}

		else if (encoderDifference > 0) {
			this.lastActiveBrakingCount = this.rightFront.getEncPosition();
			this.activeBrakingSpeed += encoderDifference * 0.0001;
		}

		this.robotDrive.drive(activeBrakingSpeed, 0);
	}

	// ----------------------------------------------------------------------
	// Acceleration Limiter
	// ----------------------------------------------------------------------

	private double limitMaxSpeedChange(double speed) {
		double newSpeed = limitSpeed(speed, previousSpeedLeft);
		previousSpeedLeft = newSpeed;
		return newSpeed;
	}

	private double limitMaxSpeedLeftChange(double speed) {
		double newSpeed = limitSpeed(speed, previousSpeedLeft);
		previousSpeedLeft = newSpeed;
		return newSpeed;
	}

	private double limitMaxSpeedRightChange(double speed) {
		double newSpeed = limitSpeed(speed, previousSpeedRight);
		previousSpeedRight = newSpeed;
		return newSpeed;
	}

	private double limitMaxTurnSpeedChange(double turnSpeed) {
		double newSpeed = limitSpeed(turnSpeed, previousSpeedRight);
		previousTurnSpeed = newSpeed;
		return newSpeed;
	}

	private double limitSpeed(double targetSpeed, double oldSpeed) {
		double speed = 0;

		// If we aren't limiting max acceleration, don't do anything
		if (limitMaxAcceleration == false) {
			speed = targetSpeed;
		}

		// Stopping, and the motors are set to coast, let it stop without
		// needing to
		// deaccelerate
		else if (targetSpeed == 0) {
			if (oldSpeed > (MAX_ACCELERATION * 2)) {
				speed = oldSpeed - (MAX_ACCELERATION * 2);
			} else if (oldSpeed < (MAX_ACCELERATION * -2)) {
				speed = oldSpeed + (MAX_ACCELERATION * 2);
			} else {
				speed = 0;
			}
		}

		// No change in speed?
		else if (targetSpeed == oldSpeed) {
			speed = targetSpeed;
		}

		// Probably won't need, but per above. If set to coast, going from 1 to
		// .5 may not
		// put any pressure on the wheels, so this would allow that to happen
		// freely.
		// We can uncomment this later if needed.
		/*
		 * else if (targetSpeed > 0 && targetSpeed < oldSpeed) { speed =
		 * targetSpeed; } else if (targetSpeed < 0 && targetSpeed > oldSpeed) {
		 * speed = targetSpeed; }
		 */

		// If the difference is less than the max acceleration, who cares?
		else if (Math.abs(targetSpeed - oldSpeed) < MAX_ACCELERATION) {
			speed = targetSpeed;
		}

		// If old speed is less that the target speed, the
		// new speed is the old speed plus the max acceleration
		else if (oldSpeed < targetSpeed) {
			speed = oldSpeed + MAX_ACCELERATION;
		}
		// If old speed is greater that the target speed, the
		// new speed is the old speed minus the max acceleration
		else if (oldSpeed > targetSpeed) {
			speed = oldSpeed - MAX_ACCELERATION;
		}

		// Safety
		else {
			speed = 0;
		}

		// System.out.println("Speed: " + oldSpeed + " : " + speed);

		return speed;
	}
}
