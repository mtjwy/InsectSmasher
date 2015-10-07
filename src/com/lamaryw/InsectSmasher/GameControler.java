package com.lamaryw.InsectSmasher;

import android.graphics.Canvas;

public class GameControler {

	public boolean pauseTouched(Canvas canvas, int touchx, int touchy) {
		boolean touched = false;
		float x = canvas.getWidth() / 2.0f;
		float y = 8 + Assets.pausebutton.getHeight() / 2.0f;
		// Compute distance between touch and center of pause
		float dis = (float) (Math.sqrt((touchx - x) * (touchx - x)
				+ (touchy - y) * (touchy - y)));
		// Is this close enough for a pause?
		if (dis <= Assets.pausebutton.getWidth() * 0.75f) {
			Assets.state = Assets.GameState.Paused;

			touched = true;
		}

		return touched;
	}

	public boolean playTouched(Canvas canvas, int touchx, int touchy) {
		boolean touched = false;
		float x = canvas.getWidth() / 2.0f;
		float y = 8 + Assets.continuebutton.getHeight() / 2.0f;
		// Compute distance between touch and center of pause
		float dis = (float) (Math.sqrt((touchx - x) * (touchx - x)
				+ (touchy - y) * (touchy - y)));
		// Is this close enough for a pause?
		if (dis <= Assets.continuebutton.getWidth() * 0.75f) {

			if (Assets.bonusScore != 0) {
				Assets.state = Assets.GameState.Bonus;
			} else {
				Assets.state = Assets.GameState.Running;
			}
			touched = true;
		}

		return touched;
	}

	// int spacing = 8;
	public boolean musicIconTouched(Canvas canvas, int touchx, int touchy) {
		boolean touched = false;
		float x = canvas.getWidth() / 2.0f + Assets.pausebutton.getWidth()
				/ 2.0f + Assets.spacing + Assets.music_on_icon.getWidth()
				/ 2.0f;
		float y = Assets.spacing + Assets.pausebutton.getHeight() / 2.0f;
		// Compute distance between touch and center of pause
		float dis = (float) (Math.sqrt((touchx - x) * (touchx - x)
				+ (touchy - y) * (touchy - y)));
		// Is this close enough for a pause?
		if (dis <= Assets.pausebutton.getWidth() * 0.75f) {
			if (Assets.music_on == true) {
				if (Assets.mediaPlayer != null) {
					Assets.mediaPlayer.pause();
				}
				Assets.music_on = false;
				// Assets.music_icon = Assets.music_pause_icon;
			} else {
				if (Assets.mediaPlayer != null) {
					Assets.mediaPlayer.start();
				}
				Assets.music_on = true;
				// Assets.music_icon = Assets.music_on_icon;
			}
			touched = true;
		}

		return touched;
	}

	public boolean soundpoolIconTouched(Canvas canvas, int touchx, int touchy) {
		boolean touched = false;
		float x = canvas.getWidth() / 2.0f - Assets.pausebutton.getWidth()
				/ 2.0f - Assets.spacing - Assets.soundpool_on_icon.getWidth()
				/ 2.0f;
		float y = Assets.spacing + Assets.soundpool_on_icon.getHeight() / 2.0f;
		// Compute distance between touch and center of pause
		float dis = (float) (Math.sqrt((touchx - x) * (touchx - x)
				+ (touchy - y) * (touchy - y)));
		// Is this close enough for a pause?
		if (dis <= Assets.soundpool_on_icon.getWidth() * 0.75f) {
			if (Assets.soundpool_on == true) {
				Assets.soundpool_on = false;
				// Assets.soundpool_icon = Assets.soundpool_off_icon;
			} else {
				Assets.soundpool_on = true;
				// Assets.soundpool_icon = Assets.soundpool_on_icon;
			}
			touched = true;
		}

		return touched;
	}

	public boolean restartGameTouched(Canvas canvas, int touchx, int touchy) {
		boolean touched = false;
		float x = canvas.getWidth() / 2.0f;
		float y = canvas.getHeight() / 2.0f + Assets.retry.getHeight();
		// Compute distance between touch and center of pause
		float dis = (float) (Math.sqrt((touchx - x) * (touchx - x)
				+ (touchy - y) * (touchy - y)));
		// Is this close enough for a restart?
		if (dis <= Assets.retry.getHeight() * 1.0f) {
			touched = true;
		}
		return touched;
	}

	public boolean nextLevelTouched(Canvas canvas, int touchx, int touchy) {
		boolean touched = false;
		float x = canvas.getWidth() / 2.0f;
		float y = canvas.getHeight() * 17.0f / 29.0f;
		// Compute distance between touch and center of pause
		float dis = (float) (Math.sqrt((touchx - x) * (touchx - x)
				+ (touchy - y) * (touchy - y)));
		// Is this close enough for a restart?
		if (dis <= Assets.retry.getHeight() * 1.0f) {
			touched = true;
		}
		return touched;
	}

}
