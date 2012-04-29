package com.devcampnyc.spheromynd;

import java.util.Stack;


public class Translator {
	
	public final class SpheroSettings
	{
		private float _heading;
		private float _speed;
		private int _change_cnt = 0;
		
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
		
		public void setHeading(float heading)
		{
			_heading = heading;
			_change_cnt += 1;
		}
		
		public void adjustHeading(float heading_chg)
		{
			float h = _heading + heading_chg;
			setHeading((h > 360)? 360 - h: h);
		}
		
		public void setSpeed(float speed)
		{
			_speed = speed;
		}
		
	}
	
	private SpheroSettings _settings;
	
	private Stack<Integer> _meditation = new Stack<Integer>();
	private Stack<Integer> _attention = new Stack<Integer>();
	
	public int base_blink = 50;
	public float base_speed = .5f;
	public float base_heading_chg = 120.0f;
	
	public Translator()
	{
		_settings = new SpheroSettings(0.0f, 0.0f);
	}
	
	public SpheroSettings updateSpheroSettings (int meditation, int attention, 
			int blink)
	{
		
		_meditation.push(meditation);
		_attention.push(attention);
		
		int avg_med = 0;
		int avg_att = 0;
		int cnt = 0;
		final int moving_avg = 3;
		
		for(int i = _meditation.size() - 1; i >= 0; i--)
		{
			avg_med = _meditation.get(i);
			cnt += 1;
			if(cnt == moving_avg){ break; }
		}
		
		avg_med = avg_med / cnt;
		cnt = 0;
		
		for(int i = _attention.size() - 1; i >= 0; i--)
		{
			avg_att = _attention.get(i);
			cnt += 1;
			if(cnt == moving_avg){ break; }
		}
		
		avg_att = avg_att / cnt;
		
		_settings.adjustHeading(calcHeadingChg(avg_med, avg_att));
		
		/*if (blinked(blink))
		{
			_settings.setSpeed (!(_settings.getSpeed() > 0)? base_speed: 0);
		}*/
		
		return _settings;
		
	}
	
	protected boolean blinked (int blink)
	{
		return (blink > base_blink);
	}
	
	private float calcHeadingChg(int meditation, int attention)
	{
		float heading_adj = 10.0f;
		
		float madj = (meditation / 100.0f) * (heading_adj / 2.0f);
		float aadj = (attention / 100.0f) * (heading_adj / 2.0f);
		
		return base_heading_chg - (heading_adj - (madj + aadj));
	}
	
}
