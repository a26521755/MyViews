package com.qing.testmyview;

import java.util.Random;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends Activity {
	
	private CircleProgressBar cpb;
	private CusButton cb;
	int i = 0;
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what <= cpb.getMax()){
				cpb.setProgress(msg.what);
				handler.sendEmptyMessageDelayed(i++, 50);
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
//		cpb = (CircleProgressBar) findViewById(R.id.cpb);
//		cb = (CusButton) findViewById(R.id.cb);
//		cb.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				showText(v);
//			}
//		});
//		handler.sendEmptyMessageDelayed(i++, 50);
	}
	boolean flag = false;
	
	public void showText(View v){
		Log.i("tata", "button is clicked");
		if(i >= cpb.getMax()){
			i = 0;
			handler.sendEmptyMessageDelayed(i++, 50);
		}
		Random r= new Random();
		cpb.setColor(Color.rgb(r.nextInt(255), r.nextInt(255), r.nextInt(255)));
	}
	
}
