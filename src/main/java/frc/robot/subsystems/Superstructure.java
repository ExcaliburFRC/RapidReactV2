package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.*;
import frc.robot.Constants;
import lib.RepeatingCommand;

public class Superstructure {
  private final Intakee intake = new Intakee();
  private final Shooter shooter = new Shooter();

  public Command intakeBallsCommand() {
    return new InstantCommand(
          () -> intake.openClosePiston(true))
          .andThen(
                new RepeatingCommand(
                new ConditionalCommand(
                      // deciding by whether the ball is our color
                      new ConditionalCommand(
                            // whether the ultrasonic detects a ball
                            intake.closeIntake(),
                            intake.pullToUltrasonic(),
                            intake::isUltraDist
                      ),
                      new ConditionalCommand(
                            // whether we want to eject from intake or from shooter
                            intake.ejectFromColor()
                            ,
                            ,
                            intake::isUltraDist
                      ),
                      intake::isOurColor
                ));

  }
}
