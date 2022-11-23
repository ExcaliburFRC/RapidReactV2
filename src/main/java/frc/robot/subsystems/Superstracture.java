package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.*;
import frc.robot.Constants;

public class Superstracture {
  private final Intake intake = new Intake();
  private final Shooter shooter = new Shooter();

  public Command intakeBallsCommand(){
    return new FunctionalCommand(
          ()-> intake.OpenClosePiston(true),
          ()-> {
            intake.setFrontMotorSpeed(0.3);
          },
          (__)-> {},
          ()-> intake.getColorDis() < Constants.IntakeConstants.COLOR_LIMIT
    ).andThen();
  }
}
