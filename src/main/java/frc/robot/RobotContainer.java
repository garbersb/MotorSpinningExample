// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.SelectCommand;
import edu.wpi.first.wpilibj2.command.button.CommandGenericHID;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsystem.SingleMotorSubsystem;

import java.util.Map;

import com.ctre.phoenix6.hardware.TalonFX;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {


  // The enum used as keys for selecting the command to run.
  /** AUTO GENERATED */
  private enum CommandSelector {
    ONE,
    TWO,
    THREE
  }

  // An example selector method for the selectcommand.  Returns the selector that will select
  // which command to run.  Can base this choice on logical conditions evaluated at runtime.
    /** AUTO GENERATED */
  private CommandSelector select() {
    return CommandSelector.ONE;
  }

  // An example selectcommand.  Will select from the three commands based on the value returned
  // by the selector method at runtime.  Note that selectcommand works on Object(), so the
  // selector does not have to be an enum; it could be any desired type (string, integer,
  // boolean, double...)
    /** AUTO GENERATED */
  private final Command m_exampleSelectCommand =
      new SelectCommand<>(
          // Maps selector values to commands
          Map.ofEntries(
              Map.entry(CommandSelector.ONE, new PrintCommand("Command one was selected!")),
              Map.entry(CommandSelector.TWO, new PrintCommand("Command two was selected!")),
              Map.entry(CommandSelector.THREE, new PrintCommand("Command three was selected!"))),
          this::select);


  // The robot's subsystems 
  private SingleMotorSubsystem singleMotorSubsystem = null;


  // XBoxController Object that points to port 1 of the USB device.  You can use any of the buttons on the XBoxController
  private final CommandXboxController m_driverController =  new CommandXboxController(1);

   // Generic object that points to our custom button box which is plugged into Port 0
   // Note that you can see this within the Driver Station Controller
  private CommandGenericHID comm = new CommandGenericHID(0);

  //Talon Motor object we will look to manipulate.
 private TalonFX motor = null;


  /** RobotContainer that calls a method that configures the buttons */
  public RobotContainer() {

     //We will create our motor object that will be used to spin.
     motor = new TalonFX(1);

     //SingleMotorSubsystem Object that will be used to turn the motor based on button presses
     singleMotorSubsystem = new SingleMotorSubsystem(motor);

    // Configure the button bindings
    configureButtonBindings();
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then calling passing it to a
   * {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    System.out.println("configureBindings");

    //The Y Button when pressed will spin the motor forward
    m_driverController.y().onTrue(singleMotorSubsystem.spinArmCommandForward());

    //The Y command will stop when it is false
    m_driverController.y().onFalse(singleMotorSubsystem.stopArmCommand());

    //The A button will spin the motor backward
    m_driverController.a().onTrue(singleMotorSubsystem.spinArmCommandBackward());

    //The B button will stop the motor
    m_driverController.b().onTrue(singleMotorSubsystem.stopArmCommand());

    //We waned to test out our generic controller.  If the first button is presseed we will spin the motor forward
    //and when it is released it will stop.
    comm.button(1).onTrue(singleMotorSubsystem.spinArmCommandForward());
    comm.button(1).onFalse(singleMotorSubsystem.stopArmCommand());
    
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    return m_exampleSelectCommand;
  }
}
