package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.ColorSensorV3;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj2.command.*;
import edu.wpi.first.wpilibj2.command.button.Trigger;

import static frc.robot.Constants.IntakeConstants;

public class Intake extends SubsystemBase {
  private int ballCount;
  private final CANSparkMax frontMotor = new CANSparkMax(IntakeConstants.INTAKE_MOTOR_ID, CANSparkMaxLowLevel.MotorType.kBrushless);
  private final CANSparkMax backMotor = new CANSparkMax(IntakeConstants.UPPER_MOTOR_ID, CANSparkMaxLowLevel.MotorType.kBrushless);
  private final DoubleSolenoid piston = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, IntakeConstants.FWD_CHANNEL, IntakeConstants.REV_CHANNEL);
  private final ColorSensorV3 colorSensor = new ColorSensorV3(I2C.Port.kMXP);
  private final Ultrasonic ultrasonic = new Ultrasonic(IntakeConstants.UPPER_PING, IntakeConstants.UPPER_ECHO);

  public Intake() {
    frontMotor.setIdleMode(CANSparkMax.IdleMode.kBrake);
    backMotor.setIdleMode(CANSparkMax.IdleMode.kBrake);

    frontMotor.setInverted(true);
    backMotor.setInverted(true);

    ultrasonic.setEnabled(true);
    ballCount = 1;
  }

  private Command incrementBallCount(int inc){
    return new InstantCommand(()-> this.ballCount = ballCount + inc);
  }

  public Command pullIntoIntake() {
    return new FunctionalCommand(
            ()-> piston.set(DoubleSolenoid.Value.kForward),
            ()-> frontMotor.set(IntakeConstants.INTAKE_SPEED),
            (__)-> frontMotor.set(0),
            ()-> colorSensor.getProximity() < IntakeConstants.COLOR_LIMIT)
            .andThen(incrementBallCount(1));
  }

  public Command pullIntoUpper(){
    return new FunctionalCommand(
            () -> {},
            ()-> backMotor.set(IntakeConstants.TRANSPORT_SPEED),
            (__)-> backMotor.set(0),
            ()-> ultrasonic.getRangeMM() < IntakeConstants.SONIC_LIMIT);
  }

  public Command pullIntoShooter(Trigger ballShotTrigger){
    return new FunctionalCommand(
            ()-> {},
            ()-> backMotor.set(IntakeConstants.TRANSPORT_SPEED),
            (__) -> backMotor.set(0),
            ballShotTrigger)
            .andThen(incrementBallCount(-1));
  }
}
