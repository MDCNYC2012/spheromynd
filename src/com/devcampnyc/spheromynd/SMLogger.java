package com.devcampnyc.spheromynd;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.os.AsyncTask;
import android.os.Environment;

public class SMLogger {
	
	private String logFileName = "applog.txt";
	private AsyncLogger aLogger;
	
	private int _meditation;
	private int _attention;
	private int _blink;
	private boolean _ready;
	
	private class AsyncLogger extends AsyncTask<Integer, Void, Void>
	{
		
		private FileOutputStream _log;
		
		public AsyncLogger(String logFileName)
		{
			try {
				File f = Environment.getExternalStorageDirectory();
				_log = new FileOutputStream(
						f.getPath() + "/SpheroMynd/" + logFileName);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		@Override
		protected void finalize()
		{
			try {
				_log.close();
				super.finalize();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		protected Void doInBackground(Integer... params) {		
			String logRecord;
			logRecord = System.currentTimeMillis() + "|" +
					params[0].toString() + "|" +
					params[1].toString() + "|" +
					params[2].toString() + "\n";
			
			try {
				_log.write(logRecord.getBytes());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return null;
		}
		
		
	}
	
	public SMLogger()
	{
		aLogger = new AsyncLogger(logFileName);
	}
	
	public int getLastMeditation()
	{
		return _meditation;
	}
	
	public int getLastAttention()
	{
		return _attention;
	}
	
	public int getLastBlink()
	{
		return _blink;
	}
	
	public boolean isReady()
	{
		return _ready;
	}
	
	public void setMeditation(int meditation)
	{
		_meditation = meditation;
		_ready = false;
	}
	
	public void setAttention(int attention)
	{
		_attention = attention;
		_ready = false;
	}
	
	public void setBlink(int blink)
	{
		_blink = blink;
		_ready = true;
		logAsync();
	}
	
	private void logAsync()
	{
		aLogger.execute(this._meditation, this._attention, this._blink);
	}
	
}
