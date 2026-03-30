package frc.robot.subsystem;

import com.ctre.phoenix6.Orchestra;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.controls.PositionDutyCycle;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;


//this will spin a single motor
public class SingleMotorSubsystem extends SubsystemBase {

    // This is the only motor we are concerned about spinning in this example.
    TalonFX motor = null;

    // We will use this to play music so we know where things are at within our
    // code.
    Orchestra orch = null;

    // DutyCycle values we can set the motor to to spin forward
    private final DutyCycleOut spinOutputForward = new DutyCycleOut(0.05); // 50% power

    // DutyCycle we can set the motor to stop
    private final DutyCycleOut stopOutput = new DutyCycleOut(0.0); // Stop motor

    // DutyCycle we can set the motor to go backwards
    private final DutyCycleOut spinOutputBackward = new DutyCycleOut(-0.05); // 50% power

    // Move to 4 rotations forward
    PositionDutyCycle rotate4 = new PositionDutyCycle(4.0); // 4 rotations

    String[] _songs = new String[] {
            "CantinaBand.chrp", "song1.chrp",
            "StarWarsMainSong.chrp" };

    /* track which song is selected for play */
    int _songSelection = 0;

    /* overlapped actions */
    int _timeToPlayLoops = 0;

    /** Creates the SingleMotorSubsystem which takes in a TalonFX motor object.. */
    public SingleMotorSubsystem(TalonFX motor) {
        this.motor = motor;

        //This will ensure the motor breaks
        motor.setNeutralMode(NeutralModeValue.Brake);

        //Here we load the Orchestra object and add the motor object and load the the music
        orch = new Orchestra();
        orch.addInstrument(motor);
        LoadMusicSelection(0);

    }


       //* Method that returns a command to spin the motor forward */
       public Command spinArmCommandForward() {
        System.out.println("spinArmCommandForward");

        return run(() -> motor.setControl(spinOutputForward))
            .finallyDo(() -> motor.setControl(stopOutput)); // stop when finished
      } 



         /** Method that returns a command to stop the motor */
        public Command stopArmCommand() {
            System.out.println("stopArmCommand");                
            return run(() -> motor.setControl(stopOutput)).finallyDo(() -> motor.setControl(stopOutput)); // stop .gwhen finished
         }


        /** Method that returns a command to spin the motor backward */
        public Command spinArmCommandBackward() {
            System.out.println("spinArmCommandBack");

            return run(() -> motor.setControl(spinOutputBackward))
                        .finallyDo(() -> motor.setControl(stopOutput)); // stop when finished
        }


    /** This method will goForward for a number of rotations that are inputted */
    public Command goForward(int rotations) {
        System.out.println("go forward");
        return this.startEnd(() -> motor.setControl(spinOutputForward), () -> motor.setControl(stopOutput));
    }

    /** This method will goBackwards for a number of roations that are inputted */
    public Command goBackward(int rotations) {
        System.out.println("go Backward");
        return this.startEnd(() -> motor.setControl(spinOutputBackward), () -> motor.setControl(stopOutput));
    }


    /** This method will top the motor. */
    public Command stopMotor() {
        System.out.println("Stop Motor");

        return runOnce(
                () -> {
                    motor.setControl(stopOutput);
                });
    }


    /** goForwardForTme is a method that you can put a time increment in which 
     * will have the motor spin forwards for a time duration in seconds.
     */
    public Command goForwardForTime(double time) {

        System.out.println("go foward for time");
        return this.startEnd(() -> motor.setControl(spinOutputForward), () -> motor.setControl(stopOutput))
                .withTimeout(time);

    }

    /** goBackwardForTime is a method that you can put a time increment in which 
     * will hae the motor spin backwards for a time duration in seconds.
     */
    public Command goBackwardForTime(double time) {

        System.out.println("go foward for time");
        return this.startEnd(() -> motor.setControl(spinOutputBackward), () -> motor.setControl(stopOutput))
                .withTimeout(time);

    }

    
    /** LoadMusicSelection takes in a number to load the file to play */
    public void LoadMusicSelection(int offset) {

        /* increment song selection */
        _songSelection += offset;


        /* wrap song index in case it exceeds boundary */
        if (_songSelection >= _songs.length) {
            _songSelection = 0;
        }
        if (_songSelection < 0) {
            _songSelection = _songs.length - 1;
        }
        /* load the music files */
        orch.loadMusic(_songs[_songSelection]);

        /* print to console which file we are playing */
        System.out.println("Song selected is: " + _songs[_songSelection] + ".  Press left/right on d-pad to change.");

        /*
         * schedule a play request, after a delay.
         * This gives the Orchestra service time to parse the music file.
         * If play() is called immedietely after, you may get an invalid action error
         * code.
         */
        _timeToPlayLoops = 10;
    }

}
