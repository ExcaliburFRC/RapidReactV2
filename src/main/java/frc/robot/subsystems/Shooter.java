package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Shooter extends SubsystemBase {
  private final CANSparkMax shooter =
        new CANSparkMax(Constants.ShooterConstants.LEADER_ID, CANSparkMaxLowLevel.MotorType.kBrushless);

  public Shooter() {
    shooter.setInverted(true);
    shooter.setSmartCurrentLimit(50);
  }

  private Command stopMotor() {
    return new InstantCommand(()-> shooter.set(0));
  }

  public Command toggleShooter() {
    return new RunCommand(()-> shooter.set(0.5));
  }
}
