package org.usfirst.frc.team4537.robot;

import edu.wpi.first.wpilibj.*;

public class Climber 
{
	private Robot robot;
	private final int CLIMBER = 0;
	private CANTalon winchTalon;
	private double speed = 1;
	
	private DoubleSolenoid climberSolenoid;
	
	private long phhhtDelay = 1500;
	private long lastPhhht = 0;
	private long phhtLength = 250;
	private long targetTime = 0;
	private boolean phhhting = false;
	
	public Climber(Robot robot) 
	{
		this.robot = robot;
		winchTalon = new CANTalon(CLIMBER);
		climberSolenoid = new DoubleSolenoid(0, 1);
		this.retractClimber();
	}
	
	public void operatorControl() 
	{
		if (robot.getController().triggerClimber())
		{
			this.extendClimber();
			this.robot.getRioduino().send(Rioduino.CLIMBER_TRIGGERED);
		}
		
		else if(robot.getController().detriggerClimber()) 
		{
			retractClimber();
			this.robot.getRioduino().send(Rioduino.CLIMBER_DETRIGGERED);
		}
		
		else if(robot.getController().raiseClimber()) 
		{
			long currentTime = System.currentTimeMillis();

			this.windWinch();

			if (lastPhhht == 0) 
			{ 
				lastPhhht = currentTime; 
				targetTime = currentTime + phhhtDelay;
				this.robot.getRioduino().send(Rioduino.CLIMBER_WINDING);
			}
			
			if (currentTime > targetTime)
			{
				if (this.phhhting)
				{
					this.stopClimber();
					this.phhhting = false;
					targetTime = currentTime + phhhtDelay;
					this.robot.getRioduino().send(Rioduino.CLIMBER_WINDING);
				}
				else
				{
					this.retractClimber();
					this.phhhting = true;
					targetTime = currentTime + phhtLength;
					this.robot.getRioduino().send(Rioduino.PHHHT);
				}
			}
		}
		
		else if(robot.getController().lowerClimber()) 
		{
			this.unwindWinch();
			this.robot.getRioduino().send(Rioduino.CLIMBER_UNWINDING);
		}
		
		else 
		{
			this.stopClimber();
			this.stopWinch();
		}
	}
	
	public void extendClimber()
	{
		this.climberSolenoid.set(DoubleSolenoid.Value.kReverse);
	}
	
	public void retractClimber()
	{
		this.climberSolenoid.set(DoubleSolenoid.Value.kForward);
	}
	
	public void stopClimber()
	{
		this.climberSolenoid.set(DoubleSolenoid.Value.kOff);
	}
	
	public void windWinch()
	{
		this.winchTalon.set(-speed);
	}
	
	public void unwindWinch()
	{
		this.winchTalon.set(speed);
	}
	
	public void stopWinch()
	{
		this.winchTalon.set(0);
		this.robot.getRioduino().send(Rioduino.NO_ACTION);
	}
}
