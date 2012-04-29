package com.devcampnyc.spheromynd;

import java.util.Stack;

public class Translator {
	
	public final class SpheroSettings
	{
		private float _heading;
		private float _heading_skip;
		private float _speed;
		private int _change_cnt = 0;
		public final float circle = 360.0f;
		private boolean _need_skip;
		
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
		
		public int getChangeCnt()
		{
			return _change_cnt;
		}
		
		public void setHeading(float heading)
		{
			_heading = heading;
			_change_cnt += 1;
		}
		
		public void adjustHeading(float heading_chg)
		{
			float h = _heading + heading_chg; // + getChangeFactor());
			
			if(h > circle){
				setHeading(0);
				_heading_skip = Math.abs(circle - h);
				_need_skip = true;
			} else {
				setHeading(h);
				_heading_skip = 0.0f;
				_need_skip = false;
			}
		}
		
		private float getChangeFactor()
		{
			float cfc = _change_cnt / circle;
			int cf = 0;
			
			if(cfc <= .1)
			{
				cf = _change_cnt;
			} else if(cfc < 1) {
				cf = (int)circle / 10;
			}
			
			return cf;
		}
		
		public float getHeadingSkip()
		{
			return _heading_skip;
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
	
	public void setSpeed(float i) {
	  _settings.setSpeed(i);
	}
	
	
	protected boolean blinked (int blink)
	{
		return (blink > base_blink);
	}
	
	private float calcHeadingChg(int meditation, int attention)
	{
		float heading_adj = 10.0f;
		
		//float madj = (meditation / 100.0f) * (heading_adj / 2.0f);
		float aadj = (attention / 100.0f) * (heading_adj); // / 2.0f);
		
		return base_heading_chg - (heading_adj - aadj); //(madj + aadj));
	}
}
