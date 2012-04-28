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

import com.devcampnyc.spheromynd.Translator.SpheroSettings;
import com.neurosky.thinkgear.TGDevice;

public class DashboardActivity extends Activity {

  public static final int STARTUP_ACTIVITY = 1;

  protected static final String TAG = "MindSphero";

  private SeekBar mMeditationControl;
  private SeekBar mAttentionControl;
  private Button mStopButton;
  
  TGDevice tgDevice;
  BluetoothAdapter bluetoothAdapter;
  final boolean rawEnabled = false;
  
  private Robot mRobot;
  private ServerClient mServerClient;
  private MindwaveState mState;
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    
    mMeditationControl = (SeekBar) findViewById(R.id.meditation_control);
    mAttentionControl = (SeekBar) findViewById(R.id.attention_control);
    mStopButton = (Button) findViewById(R.id.stop_button);
    
    mServerClient = new ServerClient();
    mState = new MindwaveState();
    
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
    mMeditationControl.setOnSeekBarChangeListener(seekBarControlChanged);
    mAttentionControl.setOnSeekBarChangeListener(seekBarControlChanged);

    mStopButton.setOnClickListener(new View.OnClickListener() {
      
      @Override
      public void onClick(View v) {
          RollCommand.sendStop(mRobot);
      }
    });
  }
  
  private void updateRobotWithControlValues() {
    if (mState.isSetUp()) {
      SpheroSettings settings = new Translator().getSpheroSettings(
          mState.meditation, mState.attention, mState.blink);
      

      RollCommand.sendCommand(mRobot, settings.getHeading(), settings.getSpeed());
      
      mServerClient.sendMindwaveState(mState);
      
      // reset state
      mState = new MindwaveState();
    }
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
          mAttentionControl.setProgress(msg.arg1);
          mState.attention = msg.arg1;
          updateRobotWithControlValues();

          Log.d(TAG, "Attention: " + msg.arg1 + "\n"); 
          break;
        case TGDevice.MSG_MEDITATION :
          mMeditationControl.setProgress(msg.arg1);
          mState.meditation = msg.arg1;
          updateRobotWithControlValues();

          Log.d(TAG, "Meditation: " + msg.arg1 + "\n");
          break;
        case TGDevice.MSG_BLINK :
          mState.blink = msg.arg1;
          updateRobotWithControlValues();
          
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

  class MindwaveState {
    
    int meditation = -1;
    int attention = -1;
    int blink = -1;
    
    boolean isSetUp() {
      return meditation != -1 &&
          attention!= -1 &&
          blink != -1;
    }

  }
  
}