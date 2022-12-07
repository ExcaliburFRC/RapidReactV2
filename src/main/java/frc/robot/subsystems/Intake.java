package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.ColorSensorV3;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj2.command.*;
import edu.wpi.first.wpilibj2.command.button.Trigger;

import static frc.robot.Constants.IntakeConstants.*;

public class Intake extends SubsystemBase {
  private int ballsQuantity;
  private final CANSparkMax frontMotor = new CANSparkMax(
        INTAKE_MOTOR_ID,
        CANSparkMaxLowLevel.MotorType.kBrushless);
  private final CANSparkMax backMotor = new CANSparkMax(
        UPPER_MOTOR_ID,
        CANSparkMaxLowLevel.MotorType.kBrushless);
  private final DoubleSolenoid piston = new DoubleSolenoid(
        PneumaticsModuleType.CTREPCM,
        FWD_CHANNEL,
        REV_CHANNEL);
  private final ColorSensorV3 colorSensor = new ColorSensorV3(I2C.Port.kMXP);
  private final Ultrasonic ultrasonic = new Ultrasonic(
        UPPER_PING,
        UPPER_ECHO);

  private Trigger colorTrigger = new Trigger(() -> colorSensor.getProximity() > COLOR_LIMIT);
  private Trigger sonicTrigger = new Trigger(() -> ultrasonic.getRangeMM() < SONIC_LIMIT).debounce(0.2);

  public Intake() {
    frontMotor.setIdleMode(CANSparkMax.IdleMode.kBrake);
    backMotor.setIdleMode(CANSparkMax.IdleMode.kBrake);

    frontMotor.setInverted(true);
    backMotor.setInverted(true);

    ultrasonic.setEnabled(true);
    Ultrasonic.setAutomaticMode(true);
    ballsQuantity = 1;
  }

  private void openClosePiston(boolean needToOpen) {
    if (needToOpen) piston.set(DoubleSolenoid.Value.kForward);
    else piston.set(DoubleSolenoid.Value.kReverse);
  }

  public Command closeIntake() {
    return new InstantCommand(
          () -> openClosePiston(false), this)
          .andThen(stopFrontMotor());
  }

  public Command pullToColorCommand() {
    return new FunctionalCommand(
          () -> openClosePiston(true),
          () -> frontMotor.set(0.3),
          (__) -> frontMotor.set(0),
          colorTrigger.debounce(0.1));
  }

  public Command pullToUltrasonic() {
    return new StartEndCommand(
          ()-> {
            frontMotor.set(0.3);
            backMotor.set(0.1);
          },
          ()-> {
            frontMotor.set(0);
            backMotor.set(0);},
          this)
          .until(sonicTrigger);
  }

  public Command pullToShooter() {
    return new RunCommand(() -> backMotor.set(0.1))
          .until(sonicTrigger.negate());
  }

  public Command ejectFromColorCommand() {
    return new FunctionalCommand(
          () -> {
          },
          () -> frontMotor.set(-0.3),
          (__) -> frontMotor.set(0),
          colorTrigger.negate().debounce(0.75));
  }

  public Command ejectFromUltrasonicCommand() {
    return new FunctionalCommand(
          () -> {
          },
          () -> backMotor.set(-0.3),
          (__) -> backMotor.set(0),
          colorTrigger);
  }

  public Command ejectBallsCommand() {
    return new InstantCommand(()-> openClosePiston(true))
    .andThen(
          new ConditionalCommand(
          new ConditionalCommand(
                      ejectFromColorCommand(),
                new InstantCommand(()-> {}),
                colorTrigger)
                .andThen(ejectFromUltrasonicCommand().andThen(ejectFromColorCommand())),
          new InstantCommand(() -> {}),
          sonicTrigger), new InstantCommand(()-> openClosePiston(false)));
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

  @Override
  public void periodic() {
    System.out.println(CommandScheduler.getInstance().requiring(this));
  }

  @Override
  public void initSendable(SendableBuilder builder) {
    builder.addBooleanProperty("sonic trigger: ", ()-> sonicTrigger.get(), null);
    builder.addBooleanProperty("color trigger: ", ()-> colorTrigger.get(), null);
  }

  public boolean intakeFull() {
    return colorTrigger.and(sonicTrigger).get() && isOurColor();
  }

  private Command stopFrontMotor() {
    return new InstantCommand(() -> frontMotor.set(0));
  }

  private Command stopBackMotor() {
    return new InstantCommand(() -> backMotor.set(0));
  }
}
