package com.lamaryw.InsectSmasher;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Handler;
import android.view.SurfaceHolder;

public class MainThread extends Thread {
	private SurfaceHolder holder;
	private Handler handler; // required for running code in the UI thread
	private boolean isRunning = false;

	private Context context;

	private int touchx, touchy; // x,y of touch event
	private boolean touched; // true if touch happened
	private static final Object lock = new Object();

	// set in method where need
	private float spacing;

	private String scoreString;
	private String congratulationString;
	private String gotNewHighestScoreString;
	private String retryLevelString;
	private String s3;
	private String s4;

	public MainThread(SurfaceHolder surfaceHolder, Context context) {
		holder = surfaceHolder;
		this.context = context;
		handler = new Handler();
		touched = false;

		Canvas canvas = holder.lockCanvas();
		spacing = canvas.getWidth() * 0.01f;
		holder.unlockCanvasAndPost(canvas);

		Assets.spacing = spacing;

		scoreString = "Score";
		congratulationString = "CONGRATULATIONS!";
		gotNewHighestScoreString = "Got new highest score: ";
		retryLevelString = "Retry level ";
		s3 = "You current score is: ";
		s4 = "The highest score is: ";

	}

	public void setRunning(boolean b) {
		isRunning = b; // no need to synchronize this since this is the only
						// line of code to writes this variable
	}

	// Set the touch event x,y location and flag indicating a touch has happened
	public void setXY(int x, int y) {
		synchronized (lock) {
			touchx = x;
			touchy = y;
			this.touched = true;
		}
	}

	@Override
	public void run() {
		while (!Thread.currentThread().isInterrupted() && isRunning) {

			// Lock the canvas before drawing
			Canvas canvas = holder.lockCanvas();
			if (canvas != null) {
				// Perform drawing operations on the canvas
				render(canvas);
				// After drawing, unlock the canvas and display it
				holder.unlockCanvasAndPost(canvas);
			}
		}
	}

	// Loads graphics, etc. used in game
	private void loadData(Canvas canvas) {
		Bitmap bmp;

		// Load food bar
		bmp = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.foodbar);
		// Compute size of bitmap needed (suppose want height = 10% of screen
		// height)
		int newHeight = (int) (canvas.getHeight() * 0.1f);
		// Scale it to a new size
		Assets.foodbar = Bitmap.createScaledBitmap(bmp, canvas.getWidth(),
				newHeight, false);
		// Delete the original
		bmp = null;

		Assets.getready = loadBackground(canvas, R.drawable.getready);
		Assets.wood = loadBackground(canvas, R.drawable.wood);
		Assets.level_failed = loadBackground(canvas, R.drawable.level_failed);
		Assets.level1 = loadBackground(canvas, R.drawable.next_level);
		Assets.next_level_background = loadBackground(canvas,
				R.drawable.next_level_background);
		Assets.bees_coming = loadBackground(canvas, R.drawable.bees_coming);
		Assets.superbugs_coming = loadBackground(canvas,
				R.drawable.superbugs_coming);

		Assets.pausebutton = loadIcon(canvas, R.drawable.pausebutton, 0.1f);
		Assets.continuebutton = loadIcon(canvas, R.drawable.continuebutton,
				0.1f);
		Assets.music_on_icon = loadIcon(canvas, R.drawable.music_on_icon, 0.1f);
		Assets.music_pause_icon = loadIcon(canvas, R.drawable.music_pause_icon,
				0.1f);
		Assets.soundpool_on_icon = loadIcon(canvas,
				R.drawable.soundpool_on_icon, 0.1f);
		Assets.soundpool_off_icon = loadIcon(canvas,
				R.drawable.soundpool_off_icon, 0.1f);
		Assets.lifeicon = loadIcon(canvas, R.drawable.lifeicon, 0.1f);

		// Assets.leaf = loadIcon(canvas, R.drawable.leaf, 0.25f);
		Assets.leaf = loadIcon(canvas, R.drawable.lifeicon, 0.15f);

		Assets.drink1 = loadIcon(canvas, R.drawable.drink1, 0.2f);
		Assets.drink2 = loadIcon(canvas, R.drawable.drink2, 0.2f);
		// Assets.fish = loadIcon(canvas, R.drawable.fish, 0.15f);
		Assets.honey = loadIcon(canvas, R.drawable.honey, 0.2f);
		Assets.humburger = loadIcon(canvas, R.drawable.humburger, 0.2f);
		Assets.chichen = loadIcon(canvas, R.drawable.chichen, 0.2f);
		Assets.chip = loadIcon(canvas, R.drawable.chip, 0.2f);

		Assets.ant1 = loadIcon(canvas, R.drawable.ant1, 0.2f);
		Assets.ant2 = loadIcon(canvas, R.drawable.ant2, 0.2f);
		// Assets.ant3 = loadIcon(canvas, R.drawable.ant3, 0.15f);
		Assets.dead_ant = loadIcon(canvas, R.drawable.dead_ant, 0.2f);

		Assets.red_ant1 = loadIcon(canvas, R.drawable.red_ant1, 0.2f);
		Assets.red_ant2 = loadIcon(canvas, R.drawable.red_ant2, 0.2f);
		// Assets.red_ant3 = loadIcon(canvas, R.drawable.red_ant3, 0.15f);
		Assets.dead_red_ant = loadIcon(canvas, R.drawable.dead_red_ant, 0.2f);

		Assets.superbug1 = loadIcon(canvas, R.drawable.superbug1, 0.2f);
		Assets.superbug2 = loadIcon(canvas, R.drawable.superbug2, 0.2f);
		Assets.superbug3 = loadIcon(canvas, R.drawable.superbug3, 0.2f);
		Assets.deadsuperbug = loadIcon(canvas, R.drawable.deadsuperbug, 0.2f);

		Assets.lady_bug = loadIcon(canvas, R.drawable.lady_bug, 0.2f);
		Assets.lady_bug1 = loadIcon(canvas, R.drawable.lady_bug1, 0.2f);
		Assets.dead_lady_bug = loadIcon(canvas, R.drawable.dead_lady_bug, 0.2f);

		Assets.grasshopper1 = loadIcon(canvas, R.drawable.grasshopper1, 0.2f);
		Assets.grasshopper2 = loadIcon(canvas, R.drawable.grasshopper2, 0.2f);
		Assets.dead_grasshopper = loadIcon(canvas, R.drawable.dead_grasshopper,
				0.2f);

		Assets.spider1 = loadIcon(canvas, R.drawable.spider1, 0.2f);
		Assets.spider2 = loadIcon(canvas, R.drawable.spider2, 0.2f);
		Assets.dead_spider = loadIcon(canvas, R.drawable.dead_spider, 0.2f);

		Assets.baby_butterfly1 = loadIcon(canvas, R.drawable.baby_butterfly1,
				0.2f);
		Assets.baby_butterfly2 = loadIcon(canvas, R.drawable.baby_butterfly2,
				0.2f);
		Assets.dead_baby_butterfly = loadIcon(canvas,
				R.drawable.dead_baby_butterfly, 0.2f);

	
		Assets.retry = loadIcon(canvas, R.drawable.retry, 0.5f);

		// Create a bugFactory
		Assets.bugs = new BugFactory(Assets.LaydyBugCount, Assets.bugCount,
				Assets.redAntCount, Assets.superbugCount, Assets.speed_seed);

		// Create a paint object for drawing vector graphics
		Assets.paint = new Paint();

		// set paint
		Typeface tf = Typeface.createFromAsset(context.getAssets(),
				"DroidSerif-BoldItalic.ttf");
		Assets.paint.setTypeface(tf);
		// Assets.paint.setColor(Color.argb(220, 65, 105, 225));
		Assets.paint.setColor(Color.argb(255, 65, 105, 225));
		
		Assets.paint.setTextSize(Assets.music_on_icon.getHeight() / 2.0f);

		Assets.paint2 = new Paint();
		Assets.paint2.setTypeface(tf);
		Assets.paint2.setColor(Color.argb(255, 65, 105, 225));
		Assets.paint2.setTextSize(canvas.getWidth() / 15.0f);
	}

	// Load specific background screen
	private Bitmap loadBackground(Canvas canvas, int resId) {
		// Load background
		Bitmap bmp = BitmapFactory
				.decodeResource(context.getResources(), resId);
		// Scale it to fill entire canvas
		return Bitmap.createScaledBitmap(bmp, canvas.getWidth(),
				canvas.getHeight(), false);
	}

	private Bitmap loadIcon(Canvas canvas, int resourceId,
			float percentageCavansWidth) {
		// Load a icon
		Bitmap bmp = BitmapFactory.decodeResource(context.getResources(),
				resourceId);
		// Compute size of bitmap needed (suppose want width = 10% of screen
		// width)
		int newWidth = (int) (canvas.getWidth() * percentageCavansWidth);
		// What was the scaling factor to get to this?
		float scaleFactor = (float) newWidth / bmp.getWidth();
		// Compute the new height
		int newHeight = (int) (bmp.getHeight() * scaleFactor);
		// Scale it to a new size
		return Bitmap.createScaledBitmap(bmp, newWidth, newHeight, false);

	}

	private void render(Canvas canvas) {
		int i;
		float x, y;

		switch (Assets.state) {
		case GettingReady:

			// Load data and other graphics needed by game
			loadData(canvas);

			// Draw a special "getting ready screen"
			canvas.drawBitmap(Assets.next_level_background, 0, 0, null);
			canvas.drawBitmap(Assets.getready, 0, 0, null);
			Assets.paint2.setTextSize(canvas.getWidth() * 2.0f / 15.0f);
			canvas.drawText(String.valueOf(Assets.game_level),
					canvas.getWidth() * 9.0f / 20.0f,
					canvas.getHeight() * 9.0f / 29.0f, Assets.paint2);
			Assets.paint2.setTextSize(canvas.getWidth() / 15.0f);

			// Play a sound effect
			if (Assets.soundpool_on) {
				Assets.soundPool.play(Assets.sound_getready, 1, 1, 1, 0, 1);
			}

			// Start a timer
			Assets.gameTimer = System.nanoTime() / 1000000000f;
			// Go to next state
			Assets.state = Assets.GameState.Starting;
			break;
		case Starting:
			// Draw the background screen
			canvas.drawBitmap(Assets.next_level_background, 0, 0, null);
			canvas.drawBitmap(Assets.getready, 0, 0, null);

			Assets.paint2.setTextSize(canvas.getWidth() * 2.0f / 15.0f);
			canvas.drawText(String.valueOf(Assets.game_level),
					canvas.getWidth() * 9.0f / 20.0f,
					canvas.getHeight() * 9.0f / 29.0f, Assets.paint2);
			Assets.paint2.setTextSize(canvas.getWidth() / 15.0f);

			// Has 3 seconds elapsed?
			float currentTime = System.nanoTime() / 1000000000f;
			if (currentTime - Assets.gameTimer >= 3) {
				// Goto next state
				Assets.state = Assets.GameState.Running;
			}
			break;
		case Paused:

			// Draw the background screen
			canvas.drawBitmap(Assets.wood, 0, 0, null);

			// Draw the foodbar at bottom of screen
			canvas.drawBitmap(Assets.foodbar, 0, canvas.getHeight()
					- Assets.foodbar.getHeight(), null);

			// Draw continue_play button on the top center of screen
			canvas.drawBitmap(
					Assets.continuebutton,
					(canvas.getWidth() - Assets.continuebutton.getWidth()) / 2.0f,
					0 + spacing, null);

			// Draw music icon on the right of Pause button
			if (Assets.music_on) {
				canvas.drawBitmap(Assets.music_on_icon,
						(canvas.getWidth() - Assets.continuebutton.getWidth())
								/ 2.0f + Assets.continuebutton.getWidth()
								+ spacing, 0 + spacing, null);
			} else {
				canvas.drawBitmap(Assets.music_pause_icon,
						(canvas.getWidth() - Assets.continuebutton.getWidth())
								/ 2.0f + Assets.continuebutton.getWidth()
								+ spacing, 0 + spacing, null);
			}

			// Draw soundpool icon on the left of Pause button
			if (Assets.soundpool_on) {
				canvas.drawBitmap(
						Assets.soundpool_on_icon,
						(canvas.getWidth() - Assets.continuebutton.getWidth())
								/ 2.0f - spacing
								- Assets.soundpool_on_icon.getWidth(),
						0 + spacing, null);
			} else {
				canvas.drawBitmap(
						Assets.soundpool_off_icon,
						(canvas.getWidth() - Assets.continuebutton.getWidth())
								/ 2.0f - spacing
								- Assets.soundpool_off_icon.getWidth(),
						0 + spacing, null);
			}

			canvas.drawText(scoreString, spacing,
					Assets.music_on_icon.getHeight() / 2.0f, Assets.paint);

			canvas.drawText(String.valueOf(Assets.gamescore), spacing,
					Assets.music_on_icon.getHeight(), Assets.paint);

			// Draw life icon for each life at top right corner of screen
			x = canvas.getWidth() - spacing - Assets.lifeicon.getWidth(); // coordinates
																			// for
																			// rightmost
																			// circle
																			// to
																			// draw
			y = spacing;
			for (i = 0; i < Assets.livesLeft; i++) {
				canvas.drawBitmap(Assets.lifeicon, x, y, null);
				// Reposition to draw the next icon to the left
				x -= (Assets.lifeicon.getWidth() + spacing);
			}

			if (touched) {
				touched = false;
				Assets.bugs.playTouched(canvas, touchx, touchy);
				Assets.bugs.musicIconTouched(canvas, touchx, touchy);
				Assets.bugs.soundpoolIconTouched(canvas, touchx, touchy);

			}
			Assets.bugs.processPaused(canvas);

			break;
		case Running:
			// Draw the background screen
			canvas.drawBitmap(Assets.wood, 0, 0, null);

			// Draw the foodbar at bottom of screen
			canvas.drawBitmap(Assets.foodbar, 0, canvas.getHeight()
					- Assets.foodbar.getHeight(), null);

			// Draw one life icon for each life at top right corner of screen
			x = canvas.getWidth() - spacing - Assets.lifeicon.getWidth(); // coordinates
																			// for
																			// rightmost
																			// circle
																			// to
																			// draw
			y = spacing;
			for (i = 0; i < Assets.livesLeft; i++) {
				canvas.drawBitmap(Assets.lifeicon, x, y, null);
				// Reposition to draw the next icon to the left
				x -= (Assets.lifeicon.getWidth() + spacing);
			}

			canvas.drawText(scoreString, spacing,
					Assets.music_on_icon.getHeight() / 2.0f, Assets.paint);
			canvas.drawText(String.valueOf(Assets.gamescore), spacing,
					Assets.music_on_icon.getHeight(), Assets.paint);

			// Draw Pause button on the top center of screen
			canvas.drawBitmap(Assets.pausebutton,
					(canvas.getWidth() - Assets.pausebutton.getWidth()) / 2.0f,
					0 + spacing, null);

			// Draw music icon on the right of Pause button
			if (Assets.music_on) {
				canvas.drawBitmap(Assets.music_on_icon,
						(canvas.getWidth() - Assets.pausebutton.getWidth())
								/ 2.0f + Assets.pausebutton.getWidth()
								+ spacing, 0 + spacing, null);
			} else {
				canvas.drawBitmap(Assets.music_pause_icon,
						(canvas.getWidth() - Assets.pausebutton.getWidth())
								/ 2.0f + Assets.pausebutton.getWidth()
								+ spacing, 0 + spacing, null);
			}
			// Draw soundpool icon on the left of Pause button
			if (Assets.soundpool_on) {
				canvas.drawBitmap(
						Assets.soundpool_on_icon,
						(canvas.getWidth() - Assets.pausebutton.getWidth())
								/ 2.0f - spacing
								- Assets.soundpool_on_icon.getWidth(),
						0 + spacing, null);
			} else {
				canvas.drawBitmap(
						Assets.soundpool_off_icon,
						(canvas.getWidth() - Assets.pausebutton.getWidth())
								/ 2.0f - spacing
								- Assets.soundpool_off_icon.getWidth(),
						0 + spacing, null);
			}

			// Process a touch
			if (touched) {
				// Set touch flag to false since we are processing this touch
				// now
				touched = false;
				// See if this touch killed a bug
				// boolean bugKilled = Assets.bugs.touched(canvas, touchx,
				// touchy);
				Assets.bugs.touched(canvas, touchx, touchy);

			}

			/*
			 * // Draw dead bugs on screen Assets.bug.drawDead(canvas); // Move
			 * bugs on screen Assets.bug.move(canvas); // Bring a dead bug to
			 * life? Assets.bug.birth(canvas);
			 */
			Assets.bugs.processBugs(canvas);

			// Are no lives left?
			if (Assets.livesLeft <= 0)
				// Goto next state
				Assets.state = Assets.GameState.GameEnding;
			break;
		case GameEnding:

			handler.post(new Runnable() {
				public void run() {

					if (Assets.gamescore >= Assets.highestScore) {
						if (Assets.soundpool_on) {
							Assets.soundPool.play(Assets.sound_highscore, 1, 1,
									1, 0, 1);
						}
					}
				}
			});

			// Goto next state
			Assets.state = Assets.GameState.GameOver;
			break;
		case GameOver:

			canvas.drawBitmap(Assets.level_failed, 0, 0, null);

			// Draw restart_game icon in the center of screen
			canvas.drawBitmap(Assets.retry,
					(canvas.getWidth() - Assets.retry.getWidth()) / 2.0f,
					(canvas.getHeight() - Assets.retry.getHeight()) / 2.0f
							+ Assets.retry.getHeight(), null);
			canvas.drawText(retryLevelString,
					(canvas.getWidth() - Assets.retry.getWidth()) / 2.0f
							+ canvas.getWidth() / 50.0f,
					(canvas.getHeight() - Assets.retry.getHeight()) / 2.0f
							+ 1.6f * Assets.retry.getHeight(), Assets.paint2);
			canvas.drawText(String.valueOf(Assets.game_level),
					(canvas.getWidth() - Assets.retry.getWidth()) / 2.0f
							+ canvas.getWidth() * 4 / 10.0f,
					(canvas.getHeight() - Assets.retry.getHeight()) / 2.0f
							+ 1.6f * Assets.retry.getHeight(), Assets.paint2);

			if (Assets.highestScore <= Assets.gamescore) {

				canvas.drawText(
						congratulationString,
						canvas.getWidth() / 8.0f,
						(canvas.getHeight() / 3.0f + canvas.getHeight() / 9.0f),
						Assets.paint2);
				canvas.drawText(gotNewHighestScoreString,
						canvas.getWidth() / 15.0f, canvas.getHeight() / 2.0f,
						Assets.paint2);
				canvas.drawText(String.valueOf(Assets.highestScore),
						canvas.getWidth() * 5.0f / 6.0f,
						canvas.getHeight() / 2.0f, Assets.paint2);

				Assets.highestScore = Assets.gamescore;

			} else {
				canvas.drawText(
						s3,
						canvas.getWidth() / 8.0f,
						(canvas.getHeight() / 3.0f + canvas.getHeight() / 9.0f),
						Assets.paint2);
				canvas.drawText(
						String.valueOf(Assets.gamescore),
						canvas.getWidth() * 5.0f / 6.0f,
						(canvas.getHeight() / 3.0f + canvas.getHeight() / 9.0f),
						Assets.paint2);

				canvas.drawText(s4, canvas.getWidth() / 8.0f,
						canvas.getHeight() / 2.0f, Assets.paint2);
				canvas.drawText(String.valueOf(Assets.highestScore),
						canvas.getWidth() * 5.0f / 6.0f,
						canvas.getHeight() / 2.0f, Assets.paint2);

			}

			if (touched) {
				touched = false;

				if (Assets.bugs.restartGameTouched(canvas, touchx, touchy)) {

					Assets.bugs.resetGameState();

					// reset this level
					Assets.gamescore = Assets.game_level
							* Assets.bugs.getLevel1Score() + 1;

					Assets.livesLeft = 3;
					Assets.hasLeaf = false;
					Assets.state = Assets.GameState.Running;

				}

			}

			break;

		case Level1:

			if (Assets.gamescore == Assets.bugs.getLevel2Score()) {
				// Draw the background screen
				canvas.drawBitmap(Assets.wood, 0, 0, null);
				canvas.drawBitmap(Assets.bees_coming, 0, 0, null);
			} else if (Assets.gamescore == Assets.bugs.getLevel3Score()) {

				canvas.drawBitmap(Assets.wood, 0, 0, null);
				canvas.drawBitmap(Assets.superbugs_coming, 0, 0, null);
			} else {
				canvas.drawBitmap(Assets.next_level_background, 0, 0, null);
				canvas.drawBitmap(Assets.level1, 0, 0, null);

				Assets.paint2.setTextSize(canvas.getWidth() * 2.0f / 15.0f);
				canvas.drawText(String.valueOf(Assets.game_level + 1),
						canvas.getWidth() * 9.0f / 20.0f,
						canvas.getHeight() * 9.0f / 29.0f, Assets.paint2);
				Assets.paint2.setTextSize(canvas.getWidth() / 15.0f);
			}
			Assets.bugs.processLevel1State(canvas);
			if (touched) {
				touched = false;
				if (Assets.bugs.nextLevelTouched(canvas, touchx, touchy)) {
					Assets.gamescore += 1;
					Assets.livesLeft = 3;

					Assets.bugs.resumeFromLevel1State();
					Assets.state = Assets.GameState.Trans;
				}

			}

			break;

		case Trans:

			Assets.game_level++;
			// Draw the background screen
			canvas.drawBitmap(Assets.wood, 0, 0, null);

			// Draw the foodbar at bottom of screen
			canvas.drawBitmap(Assets.foodbar, 0, canvas.getHeight()
					- Assets.foodbar.getHeight(), null);

			// Draw one life icon for each life at top right corner of screen
			x = canvas.getWidth() - spacing - Assets.lifeicon.getWidth(); // coordinates
																			// for
																			// rightmost
																			// circle
																			// to
																			// draw
			y = spacing;
			for (i = 0; i < Assets.livesLeft; i++) {
				canvas.drawBitmap(Assets.lifeicon, x, y, null);
				// Reposition to draw the next icon to the left
				x -= (Assets.lifeicon.getWidth() + spacing);
			}

			canvas.drawText(scoreString, spacing,
					Assets.music_on_icon.getHeight() / 2.0f, Assets.paint);
			canvas.drawText(String.valueOf(Assets.gamescore), spacing,
					Assets.music_on_icon.getHeight(), Assets.paint);

			// Draw Pause button on the top center of screen
			canvas.drawBitmap(Assets.pausebutton,
					(canvas.getWidth() - Assets.pausebutton.getWidth()) / 2.0f,
					0 + spacing, null);

			// Draw music icon on the right of Pause button
			if (Assets.music_on) {
				canvas.drawBitmap(Assets.music_on_icon,
						(canvas.getWidth() - Assets.pausebutton.getWidth())
								/ 2.0f + Assets.pausebutton.getWidth()
								+ spacing, 0 + spacing, null);
			} else {
				canvas.drawBitmap(Assets.music_pause_icon,
						(canvas.getWidth() - Assets.pausebutton.getWidth())
								/ 2.0f + Assets.pausebutton.getWidth()
								+ spacing, 0 + spacing, null);
			}
			// Draw soundpool icon on the left of Pause button
			if (Assets.soundpool_on) {
				canvas.drawBitmap(
						Assets.soundpool_on_icon,
						(canvas.getWidth() - Assets.pausebutton.getWidth())
								/ 2.0f - spacing
								- Assets.soundpool_on_icon.getWidth(),
						0 + spacing, null);
			} else {
				canvas.drawBitmap(
						Assets.soundpool_off_icon,
						(canvas.getWidth() - Assets.pausebutton.getWidth())
								/ 2.0f - spacing
								- Assets.soundpool_off_icon.getWidth(),
						0 + spacing, null);
			}

			if (Assets.gamescore >= 0
					&& Assets.gamescore <= (Assets.bugs.getLevel3Score() + 1)) {
				Assets.LaydyBugCount += 1;
				Assets.bugCount += 1;
				Assets.redAntCount += 1;
				Assets.superbugCount += 1;
			}

			// raise the difficulty
			Assets.speed_seed = 1.0f + Assets.game_level / 10.0f;
			if (Assets.game_level == 10) {
				Assets.superbugCount = 1 + 3 + 1;
			}

			if (Assets.game_level == 20) {
				Assets.superbugCount = 1 + 3 + 1 + 1;
			}

			// Create a bugFactory
			Assets.bugs = new BugFactory(Assets.LaydyBugCount, Assets.bugCount,
					Assets.redAntCount, Assets.superbugCount, Assets.speed_seed);

			Assets.state = Assets.GameState.Running;

			break;

		case Bonus:

			// Draw the background screen
			canvas.drawBitmap(Assets.wood, 0, 0, null);

			// Draw the foodbar at bottom of screen
			canvas.drawBitmap(Assets.foodbar, 0, canvas.getHeight()
					- Assets.foodbar.getHeight(), null);

			// Draw one life icon for each life at top right corner of screen
			x = canvas.getWidth() - spacing - Assets.lifeicon.getWidth(); // coordinates
																			// for
																			// rightmost
																			// circle
																			// to
																			// draw
			y = spacing;
			for (i = 0; i < Assets.livesLeft; i++) {
				canvas.drawBitmap(Assets.lifeicon, x, y, null);
				// Reposition to draw the next icon to the left
				x -= (Assets.lifeicon.getWidth() + spacing);
			}

			canvas.drawText(scoreString, spacing,
					Assets.music_on_icon.getHeight() / 2.0f, Assets.paint);
			canvas.drawText(String.valueOf(Assets.gamescore), spacing,
					Assets.music_on_icon.getHeight(), Assets.paint);

			// Draw Pause button on the top center of screen
			canvas.drawBitmap(Assets.pausebutton,
					(canvas.getWidth() - Assets.pausebutton.getWidth()) / 2.0f,
					0 + spacing, null);

			// Draw music icon on the right of Pause button
			if (Assets.music_on) {
				canvas.drawBitmap(Assets.music_on_icon,
						(canvas.getWidth() - Assets.pausebutton.getWidth())
								/ 2.0f + Assets.pausebutton.getWidth()
								+ spacing, 0 + spacing, null);
			} else {
				canvas.drawBitmap(Assets.music_pause_icon,
						(canvas.getWidth() - Assets.pausebutton.getWidth())
								/ 2.0f + Assets.pausebutton.getWidth()
								+ spacing, 0 + spacing, null);
			}
			// Draw soundpool icon on the left of Pause button
			if (Assets.soundpool_on) {
				canvas.drawBitmap(
						Assets.soundpool_on_icon,
						(canvas.getWidth() - Assets.pausebutton.getWidth())
								/ 2.0f - spacing
								- Assets.soundpool_on_icon.getWidth(),
						0 + spacing, null);
			} else {
				canvas.drawBitmap(
						Assets.soundpool_off_icon,
						(canvas.getWidth() - Assets.pausebutton.getWidth())
								/ 2.0f - spacing
								- Assets.soundpool_off_icon.getWidth(),
						0 + spacing, null);
			}

			// Process a touch
			if (touched) {
				// Set touch flag to false since we are processing this touch
				// now
				touched = false;
				Assets.bugs.bonusTouched(canvas, touchx, touchy);
			}

			Assets.bugs.processBonus(canvas);

			/*
			 * reset a level for the game
			 */
			if (Assets.bonusScore >= 10) {
				Assets.bugs.setBonusToDisappear();
				Assets.bugs.resumeFromLevel1State();
				Assets.bonusScore = 0;
				Assets.state = Assets.GameState.Running;

			}

			break;
		}
	}

}
