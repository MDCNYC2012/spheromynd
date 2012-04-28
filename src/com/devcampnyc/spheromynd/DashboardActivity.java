package com.devcampnyc.spheromynd;

import orbotix.robot.app.StartupActivity;
import orbotix.robot.base.Robot;
import orbotix.robot.base.RobotProvider;
import orbotix.robot.base.RollCommand;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import com.neurosky.thinkgear.TGDevice;

public class DashboardActivity extends Activity {

  public static final int STARTUP_ACTIVITY = 1;

  protected static final String TAG = "MindSphero";

  private SeekBar mHeadingControl;
  private SeekBar mSpeedControl;
  private Button mStopButton;
  
  TGDevice tgDevice;
  BluetoothAdapter bluetoothAdapter;
  final boolean rawEnabled = false;
  
  private Robot mRobot;
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    
    mHeadingControl = (SeekBar) findViewById(R.id.heading_control);
    mSpeedControl = (SeekBar) findViewById(R.id.speed_control);
    mStopButton = (Button) findViewById(R.id.stop_button);
    
    bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    
    if (bluetoothAdapter == null) {
      // Alert user that Bluetooth is not available
      Toast.makeText(this, "Bluetooth not available", Toast.LENGTH_LONG).show();
      finish();
      return;
    } else {
      /* create the TGDevice */
      tgDevice = new TGDevice(bluetoothAdapter, handler);
    }
    
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
  

  private final Handler handler = new Handler() {
    @Override public void handleMessage(Message msg) {
      switch (msg.what) {
        case TGDevice.MSG_STATE_CHANGE :

          switch (msg.arg1) {
            case TGDevice.STATE_IDLE :
              break;
            case TGDevice.STATE_CONNECTING :
              Log.d(TAG, "Connecting...\n");
              break;
            case TGDevice.STATE_CONNECTED :
              Log.d(TAG, "Connected.\n");
              tgDevice.start();
              break;
            case TGDevice.STATE_NOT_FOUND :
              Log.d(TAG, "Can't find\n");
              break;
            case TGDevice.STATE_NOT_PAIRED :
              Log.d(TAG, "not paired\n");
              break;
            case TGDevice.STATE_DISCONNECTED :
              Log.d(TAG, "Disconnected mang\n");
          }

          break;
        case TGDevice.MSG_POOR_SIGNAL :
          Log.d(TAG, "PoorSignal: " + msg.arg1 + "\n");
          break;
        case TGDevice.MSG_RAW_DATA :
          break;
        case TGDevice.MSG_HEART_RATE :
          Log.d(TAG, "Heart rate: " + msg.arg1 + "\n");
          break;
        case TGDevice.MSG_ATTENTION :
          mSpeedControl.setProgress(msg.arg1);
          Log.d(TAG, "Attention: " + msg.arg1 + "\n");
          break;
        case TGDevice.MSG_MEDITATION :
          Log.d(TAG, "Meditation: " + msg.arg1 + "\n");

          break;
        case TGDevice.MSG_BLINK :
          Log.d(TAG, "Blink: " + msg.arg1 + "\n");
          break;
        case TGDevice.MSG_RAW_COUNT :
          break;
        case TGDevice.MSG_LOW_BATTERY :
          Toast.makeText(getApplicationContext(), "Low battery!", Toast.LENGTH_SHORT).show();
          break;
        case TGDevice.MSG_RAW_MULTI :
        default :
          break;
      }
    }
  };


  public void connectMindwave(View view) {
    if (tgDevice.getState() != TGDevice.STATE_CONNECTING && tgDevice.getState() != TGDevice.STATE_CONNECTED)
      tgDevice.connect(rawEnabled);
  }
  
}