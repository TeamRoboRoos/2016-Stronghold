
package org.usfirst.frc.team4537.robot;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Ultrasonic;

public class Sensors {

	//Add sensors here when needed
	
	private Ultrasonic leftUltrasonic;
    private MaxbotixUltrasonic frontUltrasonic;
	
	private ADXRS450_Gyro gyro;
    
    public Sensors()
    {
        this.gyro = new ADXRS450_Gyro();
        this.gyro.calibrate();
        
        //leftUltrasonic = new Ultrasonic(1,0);
        //leftUltrasonic.setEnabled(true);
        //leftUltrasonic.setAutomaticMode(true);
        
        frontUltrasonic = new MaxbotixUltrasonic(0);
    	
    }

    /*
    WARNING Back and Right distances have been commented out
    as the sensors have not been connected. Once connected delete the commenting.
    */
    
    public double getDistanceFront()
    {
    	return frontUltrasonic.getRangeInMM() / 10; //has been converted to CM
    }
    
    /*
    public double getDistanceBack()
    {
    	return backUltrasonic.RangeInMM() / 10; //has been converted to CM
    }
    */
    
    public double getDistanceLeft()
    {
    	return leftUltrasonic.getRangeMM() / 10; //has been converted to CM
    }
   
    /*
    public double getDistanceRight()
    {
    	return rightUltrasonic.getRangeMM() / 10; //has been converted to CM
    }
    */
    
    
}
