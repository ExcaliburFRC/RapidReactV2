package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.*;
import frc.robot.Constants;

public class Superstructure {
  private final Intakee intake = new Intakee();
  private final Shooter shooter = new Shooter();

  public Command intakeBallsCommand() {
    return new FunctionalCommand(
          () -> intake.openClosePiston(true),
          () -> {
            intake.setFrontMotorSpeed(0.3);
          },
          (__) -> {
          },
          () -> intake.getColorDis() < Constants.IntakeConstants.COLOR_LIMIT)
          .andThen(
                new ConditionalCommand(
                      // deciding by whether the ball is our color
                      new ConditionalCommand(
                            // whether the ultrasonic detects a ball
                            intake.closeIntake(),
                            intake.pullToUltrasonic(),
                            // TODO portal to A

                            intake::isUltraDist
                      ),
                      new ConditionalCommand(
                            // whether we want to eject from intake or from shooter
                            ,
                            ,
                            intake::isUltraDist
                      ),
                      intake::isOurColor
                ));

  }
}
