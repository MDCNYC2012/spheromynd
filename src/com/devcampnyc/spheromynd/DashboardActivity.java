package com.devcampnyc.spheromynd;

import orbotix.robot.app.StartupActivity;
import orbotix.robot.base.Robot;
import orbotix.robot.base.RobotProvider;
import orbotix.robot.base.RollCommand;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

public class DashboardActivity extends Activity {

  public static final int STARTUP_ACTIVITY = 1;

  private SeekBar mHeadingControl;
  private SeekBar mSpeedControl;
  private Button mStopButton;
  
  private Robot mRobot;
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    
    mHeadingControl = (SeekBar) findViewById(R.id.heading_control);
    mSpeedControl = (SeekBar) findViewById(R.id.speed_control);
    mStopButton = (Button) findViewById(R.id.stop_button);
  }
  
  @Override
  protected void onStart() {
    super.onStart();
    
    Intent intent = new Intent(this, StartupActivity.class);  
    startActivityForResult(intent, STARTUP_ACTIVITY);  
  }

  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == STARTUP_ACTIVITY) {
      if (resultCode == RESULT_OK) {
        String robotId = data.getStringExtra(StartupActivity.EXTRA_ROBOT_ID);
        mRobot = RobotProvider.getDefaultProvider().findRobot(robotId);
        
        enableControls();
      }
      else {
        // could not connect to any Sphero
      }
    }
  }
  
  private void enableControls() {
    mHeadingControl.setOnSeekBarChangeListener(seekBarControlChanged);
    mSpeedControl.setOnSeekBarChangeListener(seekBarControlChanged);

    mStopButton.setOnClickListener(new View.OnClickListener() {
      
      @Override
      public void onClick(View v) {
          RollCommand.sendStop(mRobot);
      }
    });
  }
  
  private void updateRobotWithControlValues() {
    float heading = (mHeadingControl.getProgress() / 100.0f) * 360; // heading is float between 0 and 360
    float speed = mSpeedControl.getProgress() / 100.0f; // speed is float between 0 and 1  
    
    RollCommand.sendCommand(mRobot, heading, speed);
  }
  
  private SeekBar.OnSeekBarChangeListener seekBarControlChanged = new SeekBar.OnSeekBarChangeListener() {
    
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {}
    
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {}
    
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
      updateRobotWithControlValues();
    }
  };
  
}