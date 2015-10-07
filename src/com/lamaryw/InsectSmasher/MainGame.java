package com.lamaryw.InsectSmasher;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Window;
import android.view.WindowManager;

public class MainGame extends Activity {
	private MainGameView v;
	private AudioManager audioManager;

	private float actVolume, maxVolume;
	private SharedPreferences prefs;
	private boolean load_level_enabled;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Disable the title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// Make full screen
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		// Start the view
		v = new MainGameView(this);
		setContentView(v);

		// AudioManager audio settings for adjusting the volume
		audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
		actVolume = (float) audioManager
				.getStreamVolume(AudioManager.STREAM_MUSIC);
		maxVolume = (float) audioManager
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		Assets.volume = actVolume / maxVolume;

		// Hardware buttons setting to adjust the media sound
		this.setVolumeControlStream(AudioManager.STREAM_MUSIC);

		Assets.mediaPlayer = MediaPlayer.create(this, R.raw.background);
		Assets.mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		Assets.mediaPlayer.setLooping(true);
		Assets.mediaPlayer.setVolume(1, 1);

		// Load the sound effects
		Assets.soundPool = new SoundPool(15, AudioManager.STREAM_MUSIC, 0);

		Assets.sound_getready = Assets.soundPool.load(this, R.raw.getready, 1);
		Assets.sound_squish1 = Assets.soundPool.load(this, R.raw.squish1, 1);
		Assets.sound_thump = Assets.soundPool.load(this, R.raw.thump, 1);
		Assets.sound_bugdead2 = Assets.soundPool.load(this, R.raw.bugdead2, 1);
		Assets.sound_bugdead3 = Assets.soundPool.load(this, R.raw.bugdead3, 1);
		
		Assets.sound_highscore = Assets.soundPool
				.load(this, R.raw.highscore, 1);
		Assets.sound_ladybug_touched = Assets.soundPool.load(this,
				R.raw.ladybug_touched, 1);
		Assets.sound_superbug_touched = Assets.soundPool.load(this,
				R.raw.superbug_touched, 1);
		Assets.sound_superbug_dead = Assets.soundPool.load(this,
				R.raw.superbug_dead, 1);
		Assets.sound_leaf_touched = Assets.soundPool.load(this,
				R.raw.leaf_touched, 1);

		prefs = PreferenceManager.getDefaultSharedPreferences(this);

		Assets.entertainment_mode_enabled = prefs.getBoolean(
				"prefs_entertainment_mode_enabled", false);

		if (Assets.entertainment_mode_enabled == true) {
			// get the current high score out of the preference
			Assets.highestScore = prefs.getInt("prefs_highscore_entertainment",
					0);
			Assets.highest_level = prefs.getInt(
					"prefs_highest_level_entertainment", 0);
		} else {
			// get the current high score out of the preference
			Assets.highestScore = prefs.getInt("prefs_highscore", 0);
			Assets.highest_level = prefs.getInt("prefs_highest_level", 0);
		}

		

		load_level_enabled = prefs
				.getBoolean("prefs_load_level_enabled", false);

		if (load_level_enabled) {
			Assets.game_level = Assets.highest_level;
			// set the score
			Assets.gamescore = Assets.game_level * 100 + 1;
			Assets.bonusScore = 0;

			Assets.speed_seed = 1.0f + Assets.game_level / 10.0f;

			if (Assets.game_level >= 0 && Assets.game_level <= 3) {
				// initialize the bug count for bug factory
				Assets.LaydyBugCount = 1 + Assets.game_level;
				Assets.bugCount = 5 + Assets.game_level;
				Assets.redAntCount = 2 + Assets.game_level;
				Assets.superbugCount = 1 + Assets.game_level;
			} else {
				// initialize the bug count for bug factory
				Assets.LaydyBugCount = 1 + 3;
				Assets.bugCount = 5 + 3;
				Assets.redAntCount = 2 + 3;
				Assets.superbugCount = 1 + 3;
			}

			if (Assets.game_level >= 10 && Assets.game_level < 20) {
				Assets.superbugCount = 1 + 3 + 1;
			}

			if (Assets.game_level >= 20) {
				Assets.superbugCount = 1 + 3 + 1 + 1;
			}

		} else {
			// set the score
			Assets.gamescore = 0;
			Assets.bonusScore = 0;
			Assets.game_level = 0;

			Assets.speed_seed = 1.0f;

			// initialize the bug count for bug factory
			Assets.LaydyBugCount = 1;
			Assets.bugCount = 5;
			Assets.redAntCount = 2;
			Assets.superbugCount = 1;
		}

		Assets.hasLeaf = false;

	}

	@Override
	protected void onResume() {
		super.onResume();

		if (Assets.mediaPlayer != null) {

			if (Assets.music_on) {
				Assets.mediaPlayer.start();
			}

		}

	}

	@Override
	protected void onPause() {
		super.onPause();

		if (Assets.mediaPlayer != null) {
			Assets.mediaPlayer.pause();
			
		}
		

		if (Assets.state == Assets.GameState.GameOver
				|| Assets.state == Assets.GameState.GameEnding) {
			Assets.state = Assets.GameState.GameOver;
		} else if (Assets.state == Assets.GameState.GettingReady
				|| Assets.state == Assets.GameState.Starting) {
			finish();
		} else if (Assets.state == Assets.GameState.Level1
				|| Assets.state == Assets.GameState.Trans) {
			Assets.state = Assets.GameState.Level1;
		} else if (Assets.state == Assets.GameState.Bonus
				|| Assets.state == Assets.GameState.Running) {
			Assets.state = Assets.GameState.Paused;
		}

		// update prefs_highestscore

		SharedPreferences.Editor editor = prefs.edit();

		if (Assets.entertainment_mode_enabled == false) {
			if (Assets.gamescore >= Assets.highestScore) {
				editor.putInt("prefs_highscore", Assets.gamescore);
				editor.commit();
			} else {
				editor.putInt("prefs_highscore", Assets.highestScore);
				editor.commit();
			}

			// update prefs_highest_level
			if (Assets.game_level >= Assets.highest_level) {
				editor.putInt("prefs_highest_level", Assets.game_level);
				editor.commit();
			} else {
				editor.putInt("prefs_highest_level", Assets.highest_level);
				editor.commit();
			}
		} else {
			if (Assets.gamescore >= Assets.highestScore) {
				editor.putInt("prefs_highscore_entertainment", Assets.gamescore);
				editor.commit();
			} else {
				editor.putInt("prefs_highscore_entertainment",
						Assets.highestScore);
				editor.commit();
			}

			// update prefs_highest_level
			if (Assets.game_level >= Assets.highest_level) {
				editor.putInt("prefs_highest_level_entertainment",
						Assets.game_level);
				editor.commit();
			} else {
				editor.putInt("prefs_highest_level_entertainment",
						Assets.highest_level);
				editor.commit();
			}
		}
	}

	@Override
	public void onBackPressed() {
		if (Assets.state == Assets.GameState.GameOver) {
			finish();
		} else {

			if (Assets.mediaPlayer != null) {
				Assets.mediaPlayer.pause();
				Assets.music_on = false;
			}

			if (Assets.state == Assets.GameState.Level1) {

			} else {
				Assets.state = Assets.GameState.Paused;
			}

			// Display a dialog quit confirmation
			new AlertDialog.Builder(this)
					.setTitle("Confirm Quit")
					.setMessage("Quit Current Game?")
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface da,
										int which) {
									Assets.music_on = true;
									Assets.soundpool_on = true;

									finish();
								}
							})
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface da,
										int which) {
									if (Assets.mediaPlayer != null) {
										if (Assets.music_on) {
											Assets.mediaPlayer.start();
											Assets.music_on = true;
										}
									}
								}
							}).show();
		}

	}

}
