package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class robot {
    public drivetrain drive = new drivetrain();
    public initOperator operator = new initOperator();

    public void init(HardwareMap hardwareMap){
        drive.initDrive(hardwareMap);
        operator.initOperator(hardwareMap);
    }
}
