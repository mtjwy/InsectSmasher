package com.lamaryw.InsectSmasher;

import android.content.Context;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MainGameView extends SurfaceView implements SurfaceHolder.Callback {
	private SurfaceHolder holder = null;
	private Context context;
	private MainThread t = null;

	public MainGameView(Context context) {
		super(context);

		this.context = context;
		Assets.state = Assets.GameState.GettingReady;
		Assets.livesLeft = 3;

		holder = getHolder();
		holder.addCallback(this);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x, y;
		int action = event.getAction();
		x = event.getX();
		y = event.getY();

		if (action == MotionEvent.ACTION_DOWN) {
			t.setXY((int) x, (int) y);
		}

		return true; // to indicate we have handled this event
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (t == null) {
			t = new MainThread(holder, context);
			t.setRunning(true);
			t.start();
			setFocusable(true); // make sure we get events

		} else if (t.getState() == Thread.State.TERMINATED) {
			t = new MainThread(holder, context);
			t.setRunning(true);
			t.start();
			setFocusable(true); // make sure we get events
		}

	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {

		pause();

	}

	public void pause() {
		if (null != t)
			t.interrupt();
		if (t != null) {
			t.setRunning(false);
			while (true) {
				try {
					t.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				break;
			}
			t = null;
		}
	}
}

















