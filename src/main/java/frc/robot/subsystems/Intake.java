package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.ColorSensorV3;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj2.command.*;
import frc.robot.Constants;

public class Intake extends SubsystemBase {
    private int ballsQuantity;
    private final CANSparkMax frontMotor = new CANSparkMax(
            Constants.IntakeConstants.INTAKE_MOTOR_ID,
            CANSparkMaxLowLevel.MotorType.kBrushless);
    private final CANSparkMax backMotor = new CANSparkMax(
            Constants.IntakeConstants.UPPER_MOTOR_ID,
            CANSparkMaxLowLevel.MotorType.kBrushless);
    private final DoubleSolenoid piston = new DoubleSolenoid(
            PneumaticsModuleType.CTREPCM,
            Constants.IntakeConstants.FWD_CHANNEL,
            Constants.IntakeConstants.REV_CHANNEL);
    private final ColorSensorV3 colorSensor = new ColorSensorV3(I2C.Port.kMXP);
    private final Ultrasonic ultrasonic = new Ultrasonic(
            Constants.IntakeConstants.UPPER_PING,
            Constants.IntakeConstants.UPPER_ECHO);

    public Intake() {
        frontMotor.setIdleMode(CANSparkMax.IdleMode.kBrake);
        backMotor.setIdleMode(CANSparkMax.IdleMode.kBrake);

        frontMotor.setInverted(true);
        backMotor.setInverted(true);

        ultrasonic.setEnabled(true);
        ballsQuantity = 1;
    }

    public void openClosePiston(boolean needToOpen) {
        if (needToOpen) piston.set(DoubleSolenoid.Value.kForward);
        else piston.set(DoubleSolenoid.Value.kReverse);
    }

    public void setFrontMotorSpeed(double speed) {
        frontMotor.set(speed);
    }

    public void setBackMotorSpeed(double speed) {
        backMotor.set(speed);
    }

    private double getColorDis() {
        return colorSensor.getProximity();
    }

    public boolean isColorDist() {
        return getColorDis() > Constants.IntakeConstants.COLOR_LIMIT;
    }

    private double getUltrasonicDis() {
        return ultrasonic.getRangeMM();
    }

    public boolean isUltraDist() {
        return this.ultrasonic.getRangeMM() < Constants.IntakeConstants.SONIC_LIMIT;
    }

    public Command closeIntake() {
        return new InstantCommand(
                () -> openClosePiston(false), this)
                .andThen(stopFrontMotor());
    }

    public Command pullToUltrasonic() {
        return new RunCommand(() -> setFrontMotorSpeed(0.5), this)
                .until(this::isUltraDist)
                .andThen(stopFrontMotor());
    }

    public boolean isOurColor() {
        switch (DriverStation.getAlliance()) {
            case Red:
                return colorSensor.getRed() > colorSensor.getBlue();
            case Blue:
                return colorSensor.getBlue() > colorSensor.getRed();
            default:
                DriverStation.reportError("No alliance has been found", false);
                return false;
        }
    }

    public FunctionalCommand ejectFromColorCommand() {
        return new FunctionalCommand(
                () -> {},
                () -> this.setFrontMotorSpeed(-0.3),
                (__) -> new WaitCommand(0.5).andThen(stopFrontMotor()),
                ()-> !isColorDist());
    }

    public boolean intakeFull() {
        return isColorDist() && isUltraDist() && isOurColor();
    }

    public Command stopFrontMotor() {
        return new InstantCommand(() -> frontMotor.set(0));
    }

    public Command stopBackMotor() {
        return new InstantCommand(() -> backMotor.set(0));
    }
}
