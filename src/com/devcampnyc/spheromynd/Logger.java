package com.devcampnyc.spheromynd;

import android.os.AsyncTask;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Logger {
	
	private String logFileName = "applog.txt";
	private AsyncLogger aLogger;
	
	private int _meditation;
	private int _attention;
	private int _blink;
	
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
	
	public Logger()
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
	
	public void setMeditation(int meditation)
	{
		_meditation = meditation;
	}
	
	public void setAttention(int attention)
	{
		_attention = attention;
	}
	
	public void setBlink(int blink)
	{
		_blink = blink;
		logAsync();
	}
	
	private void logAsync()
	{
		aLogger.execute(this._meditation, this._attention, this._blink);
	}
	
}
