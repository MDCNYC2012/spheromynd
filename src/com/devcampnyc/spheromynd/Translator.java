package com.devcampnyc.spheromynd;

import android.os.AsyncTask;

public class Translator {
	
	public final class SpheroSettings
	{
		private float _heading;
		private float _speed;
		
		public SpheroSettings (float heading, float speed) {
			_heading = heading;
			_speed = speed;	
		}
		
		public float getHeading()
		{
			return _heading;
		}
		
		public float getSpeed()
		{
			return _speed;
		}
	}
	
	public SpheroSettings getSpheroSettings (int meditation, int attention, 
			int blink)
	{
		float heading = 0;
		float speed = 0;
		
		int base_meditation = 25;
		int base_attention = 25;
		int base_blink = 50;
		
		if (meditation > base_meditation || attention > base_attention)
		{
			speed = (meditation / 200) + (attention / 200);
		}
		
		if (blink > base_blink)
		{
			heading = heading + 90;
		}
		
		return new SpheroSettings(heading, speed);
		
	}
	
	/*
	private void spheroTalk(String command)
	{
		
	}
	
	public void spheroRead(String data)
	{
		
	}
	
	private void recordSpheroData(String data)
	{
				
	}
	*/
}
