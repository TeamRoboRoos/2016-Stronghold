package org.usfirst.frc.team4537.robot;

import com.ni.vision.NIVision.*;
import com.ni.vision.*;
import edu.wpi.first.wpilibj.*;

public class RobotCamera {
	
	private int[] cameras;			// All of the available cameras
	private String[] cameraNames;	// Camera names to display on the pictures
	
	private int currentCamera;		// The currently selected camera
	
	private int shooterCamera;		// The camera for the shooter may need sights, so this is its ID.
	
	private Image frame;  			// The frame to be displayed
	
	public RobotCamera()
	{
		frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
		
		// This needs to be updated when adding cameras or it will not compiled.
		int cameraCount = 1;
		
		// Create the lists of cameras and camera names.
		this.cameras = new int[cameraCount];
		this.cameraNames = new String[cameraCount];
		
		// Add each camera here. We need to count from 0, so if there is one camera,
		// it will be sessions[0], and if two there will be sessions[0] and sessions[1].
		// To get the name of the cameras, check http://roborio-4537-frc.local in
		// FireFox or Explorer.
		// Each camera should have a name to display.
		
		this.cameraNames[0] = "Shooter";
		this.cameras[0] = NIVision.IMAQdxOpenCamera("cam0", NIVision.IMAQdxCameraControlMode.CameraControlModeController);

		// Change this if the shooter camera is not 0.
		shooterCamera = 0;
		
		// Shouldn't need to touch these. They start the first camera, and set the first
		// one to be camera 0.
		this.currentCamera = 0;
		NIVision.IMAQdxConfigureGrab(this.cameras[this.currentCamera]); 
	}
	
	public void swapCameras()
	{
		// Record the index of the old camera - if we change cameras, we may need this.
		int oldCamera = this.currentCamera;
		
		// Change to the next camera
		this.currentCamera = this.currentCamera + 1;
		
		// But what if there is no next camera? In that case, change back to the first.
		if (this.currentCamera >= this.cameras.length)
		{
			this.currentCamera = 1;
		}
		
		// Did we swap cameras?
		if (oldCamera != this.currentCamera)
		{
			// Yes! We did. So stop capturing the old stream, and start grabbing the new.
			NIVision.IMAQdxStopAcquisition(this.cameras[oldCamera]);
			NIVision.IMAQdxConfigureGrab(this.cameras[this.currentCamera]);
		}
	}

	// Sends the frame to the drive station
	public void pushFrame()
	{
		// Grab the frame.
		NIVision.IMAQdxGrab(this.cameras[this.currentCamera], frame, 1);
		
		// If the current camera is the shooter, we may want to add sights.
		if (currentCamera == shooterCamera) {
			/*
			DrawTextOptions textOptions = new NIVision.DrawTextOptions(String fontName,
                    int fontSize,
                    int bold,
                    int italic,
                    int underline,
                    int strikeout,
                    NIVision.TextAlignment textAlignment,
                    NIVision.FontColor fontColor)
                    */
			//NIVision.imaqDrawTextOnImage(frame, frame, new NIVision.Point(20,20), "hello", new NIVision.DrawTextOptions());
			
			//NIVision.imaqOverlayText(frame, new NIVision.Point(100,100), "Hello World", new NIVision.RGBValue(255,255,255, 1), new NIVision.OverlayTextOptions(), "a");
			
			NIVision.Rect rect = new NIVision.Rect(150, 150, 100, 460); 
			NIVision.imaqDrawShapeOnImage(frame, frame, rect, DrawMode.DRAW_VALUE, ShapeMode.SHAPE_RECT, 255.0f);
			
		}
		
		// Send the frame to the drive station.
		CameraServer.getInstance().setImage(frame);
	}
}
