
package org.usfirst.frc.team4537.robot;

import org.usfirst.frc.team4537.robot.AutonomousModes.*;
import org.usfirst.frc.team4537.robot.Controllers.*;
import org.usfirst.frc.team4537.robot.Watcher;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
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
    private PortCullisLift portcullisLift;
    
	private Watcher watcher;
	private Thread watcherThread;
	
	private Rioduino rioduino;
	
	private DriverStation driverStation;
	
	private Sensors sensors;
	
	private Victor motorFans;
	
	private AnalogInput pneumaticPressure;
	

	/**
	 * Constructor. Creates the robot and the main components.
	 */
    public Robot() {
    	// 1. Set the default controller
        controller = new ArcadeTwoDriversPS4();
        
        this.sensors = new Sensors();
        
        // Set the drive base. Note that the drive base may reference
        // the controller, so it needs to be the last step.
    	driveBase = new DriveBase(this);

        ballGrabber = new BallGrabber(this);
    	
        shooter = new Shooter(this);
        
        climber = new Climber(this);
        
        this.rioduino = new Rioduino(this);

        defaultAutonomous = new DriveToDefence(this);
    	
        portcullisLift = new PortCullisLift();
        
        pneumaticPressure = new AnalogInput(3);
        // 100 psi = 2.528075933456421v
        
        try
        {
        	camera = new RobotCamera();
        }
        catch (com.ni.vision.VisionException e)
        {
        }
        
        // Set the default autonomous mode
        defaultAutonomous = new DriveToShootLeftSide(this);
        
        driverStation = DriverStation.getInstance();
    	
        this.watcher = new Watcher(this);
        watcherThread = new Thread(this.watcher);
        watcherThread.start();
        
        this.motorFans = new Victor(3);
        
        //thing = new DoubleSolenoid(0,1,2);
        
    }
    
	/**
	 * Initialise the robot	 
	 */
    public void robotInit() {
        SmartDashboard.putString("DB/String 0", "Stop/Breach/Shoot");
        SmartDashboard.putString("DB/String 1", "Left/Right");
        SmartDashboard.putString("DB/String 2", "Defense Type");
        SmartDashboard.putString("DB/String 4", "Drive Mode");
        SmartDashboard.putString("DB/String 8", "Normal");
        
		SmartDashboard.putNumber("DB/Slider 0", 0.1); 
		SmartDashboard.putNumber("DB/Slider 1", 1); 
		SmartDashboard.putNumber("DB/Slider 2", 0.1); 
		SmartDashboard.putNumber("DB/Slider 3", 1);
    }

	/**
	 * Autonomous mode	 
	 */
    public void autonomous() {
    	this.rioduino.send(Rioduino.AUTONOMOUS);
    	Autonomous autonomousMode = null;
    	
    	String action = SmartDashboard.getString("DB/String 5").toLowerCase();
    	String side = SmartDashboard.getString("DB/String 6").toLowerCase();
    	String defense = SmartDashboard.getString("DB/String 7").toLowerCase();
    	
    	// Drive and stop
    	
    	if (action.equals("stop"))
    	{
    		if (defense.equals("drawbridge") || defense.equals("portcullis ") || defense.equals("sally port"))
    		{
    			autonomousMode = new DriveToStopOnSensor(this);
    		}
    		else
    		{
    			autonomousMode = new DriveToStopOnDistance(this);
    		}
    	}
    	
    	if (autonomousMode != null)
    	{
    		
    		while(isAutonomous() && isEnabled())
    		{
    			autonomousMode.update();
        		Timer.delay(0.005);
    		}
    		this.getDriveBase().stop();
    		/*
    		do
    		{
    			this.getDriveBase().driveForwardToRange(0.15, 25);
        		Timer.delay(0.005);
    		} while (isAutonomous() && isEnabled() && driveBase.isDriving());
    		*/
    	}
    	
    	this.driveBase.stop();
    }

    /**
     * Teleop mode
     */
    public void operatorControl() {
    	this.rioduino.send(Rioduino.OPERATOR_CONTROLLED);
    	this.motorFans.set(1);
    	while(isOperatorControl() && isEnabled()){
    		Timer.delay(0.005);
    		
    		System.out.println("pn " + pneumaticPressure.getVoltage());
    		
        	this.driveBase.operatorControl();
        	this.ballGrabber.operatorControl();
        	this.climber.operatorControl();
    	}
    	this.motorFans.set(0);
    }

    /**
     * Runs during test mode
     */
    public void test() {
    }
    
    public void disabled() {
    	this.rioduino.send(Rioduino.DISABLED);
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
    
    public PortCullisLift getPortCullisLift()
    {
    	return portcullisLift;
    }
    
    public Shooter getShooter() {
    	return shooter;
    }
    
    public Climber getClimber() {
    	return climber;
    }
    
    public DriverStation getDriverStation() {
    	return driverStation;
    }
    
    public Rioduino getRioduino()
    {
    	return this.rioduino;
    }
    
    public Sensors getSensors()
    {
    	return this.sensors;
    }
}
