
package org.usfirst.frc.team4537.robot;

import org.usfirst.frc.team4537.robot.AutonomousModes.*;
import org.usfirst.frc.team4537.robot.Controllers.*;
import org.usfirst.frc.team4537.robot.Watcher;

import edu.wpi.first.wpilibj.*;

/**
 * Basic robot.
 */
public class Robot extends SampleRobot {
	
	// -----------------------------------------------------------------------------------------------
    // Ok. Jack? Are you reading this? Cool if you are. Or Enzo. Hi Enzo! Maybe Scott? Yay Scott!
	// If you are reading this, (Scott, Enzo, Jack, or mysterious person X), and you are installing 
	// this on the board for the first time, some notes.
	//
	// a) This worked fine on the test robot. (Ignoring some changes I just made). Any bugs are 
	//    therefore not my fault. :)
	//
	// b) To make this run you will need to do three things:
	//    1) Install it. (Naturally).
	//    2) Test the motors individually and confirm their CAN IDs. You may be able to do this
	//       through the web interface to the RoboRIO (Enzo knows how) by turning on their LEDs.
	//       Otherwise, in autonomous, run each motor in turn and note (or change appropriately)
	//       the CAN ID for the left front and rear motors, and the right fornt and rear motors.
	//    3) Open DriveBase.java and set those IDs before running the robot. If each side's 
	//       wheels are running in opposite directions, you will need to uncomment the setInvertedMotor
	//       code in DriveBase.
	//
	// Give me a call if you have any problems - I should be able to explain what is going on.
	// -----------------------------------------------------------------------------------------------
	
	private DriverController controller; 	// User input
    private DriveBase driveBase;			// Robot base with wheels and motors and stuff
    private Autonomous defaultAutonomous;	// Stores the autonomous mode instructions
    private RobotCamera camera;
    private Shooter shooter;
    private Climber climber;
    private BallGrabber ballGrabber;
    
	private Watcher watcher;
	private Thread watcherThread;
	
	private ADXRS450_Gyro gyro;

	/**
	 * Constructor. Creates the robot and the main components.
	 */
    public Robot() {
    	// 1. Set the default controller
        controller = new ArcadeTwoDriversPS4();
        
        // Set the drive base. Note that the drive base may reference
        // the controller, so it needs to be the last step.
    	driveBase = new DriveBase(this);

        ballGrabber = new BallGrabber(this);
    	
        shooter = new Shooter(this);
        
        climber = new Climber(this);

        defaultAutonomous = new DriveToDefence(this);
    	
        try
        {
        	camera = new RobotCamera();
        }
        catch (com.ni.vision.VisionException e)
        {
        }
        
        // Set the default autonomous mode
        defaultAutonomous = new DriveToShootLeftSide(this);
        
        this.gyro = new ADXRS450_Gyro();
        this.gyro.calibrate();
    	
        this.watcher = new Watcher(this);
        watcherThread = new Thread(this.watcher);
        watcherThread.start();
    }
    
	/**
	 * Initialise the robot	 
	 */
    public void robotInit() {
    }

	/**
	 * Autonomous mode	 
	 */
    public void autonomous() {
    	driveBase.stopSafety();
    	while(isAutonomous() && isEnabled()){
    		defaultAutonomous.update();
    	}
    	
    	driveBase.startSafety();
    }

    /**
     * Teleop mode
     */
    public void operatorControl() {
    	while(isOperatorControl() && isEnabled()){
    		Timer.delay(0.005);
        	driveBase.operatorControl();
        	ballGrabber.operatorControl();
    	}
    }

    /**
     * Runs during test mode
     */
    public void test() {
    }
    
    /* -------------------------------------------------------------------
     * Get and set methods (mostly get, to be honest)
     * ------------------------------------------------------------------- */
    
    /**
     * Provides access to the drive base.
     * @return DriveBase
     */
    public DriveBase getDriveBase() {
    	return this.driveBase;
    }
    
    /*
     * Provides access to the user input
     * @return The current DriverController
     */
    public DriverController getController() {
    	return this.controller;
    }
    
    public RobotCamera getCamera() {
    	return camera;
    }
    
    public BallGrabber getBallGrabber() {
    	return ballGrabber;
    }
    
    public Shooter getShooter() {
    	return shooter;
    }
    
    public Climber getClimber() {
    	return climber;
    }
    
    public ADXRS450_Gyro getGyro() {
    	return gyro;
    }
}
