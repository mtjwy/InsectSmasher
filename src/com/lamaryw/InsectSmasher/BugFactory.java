package com.lamaryw.InsectSmasher;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class BugFactory {
	
	private int LaydyBugCount;
	private int bugCount;
	private int redAntCount;
	private int superbugCount;
	private Bug[] bugs;
	private RedAnt[] redAnts;
	private LadyBug[] ladyBugs;
	private SuperBug[] superbugs;
	private Bonus leaf = new Bonus();

	private int bonus2Count = 5;
	private Bonus2[] drink1;

	private int level1Score = 100;
	private int level2Score = 200;
	private int level3Score = 300;

	

	public int getLevel1Score() {
		return level1Score;
	}

	

	public int getLevel2Score() {
		return level2Score;
	}

	

	public int getLevel3Score() {
		return level3Score;
	}

	

	private Bitmap bug1;
	private Bitmap bug2;
	private Bitmap dead_bug;

	private float speed_seed;
	private GameControler gameControler = new GameControler();

	private void setBugBitmap() {
		int soundToPlay = (int) (Math.random() * 4);
		switch (soundToPlay) {

		case 0:
			bug1 = Assets.ant1;
			bug2 = Assets.ant2;
			dead_bug = Assets.dead_ant;
			break;

		case 1:
			bug1 = Assets.baby_butterfly1;
			bug2 = Assets.baby_butterfly2;
			dead_bug = Assets.dead_baby_butterfly;
			break;
		case 2:
			bug1 = Assets.spider1;
			bug2 = Assets.spider2;
			dead_bug = Assets.dead_spider;
			break;

		case 3:
			bug1 = Assets.grasshopper1;
			bug2 = Assets.grasshopper2;
			dead_bug = Assets.dead_grasshopper;
			break;
		}
	}

	public BugFactory(int LaydyBugCount, int bugCount, int redAntCount,
			int superbugCount, float speed_seed) {
		this.LaydyBugCount = LaydyBugCount;
		this.bugCount = bugCount;
		this.redAntCount = redAntCount;
		this.superbugCount = superbugCount;
		this.speed_seed = speed_seed;

		setBugBitmap();

		bugs = new Bug[bugCount];
		ladyBugs = new LadyBug[LaydyBugCount];
		// bugCount = new Random().nextInt(MAX_COUNT) + 1; // 1 ~ 10
		redAnts = new RedAnt[redAntCount];
		getBugs();
		getLadyBugs();
		getSuperBugs();

		getRedAnts();

		getBonus2();
	}

	private void getBugs() {
		for (int i = 0; i < bugCount; i++) {
			bugs[i] = new Bug(bug1, bug2, dead_bug, speed_seed);
		}
		// return bugs;
	}

	private void getRedAnts() {
		for (int i = 0; i < redAntCount; i++) {
			redAnts[i] = new RedAnt();
		}
		// return bugs;
	}

	private void getLadyBugs() {
		for (int i = 0; i < LaydyBugCount; i++) {
			ladyBugs[i] = new LadyBug();
		}
	}

	private void getSuperBugs() {
		superbugs = new SuperBug[superbugCount];
		for (int i = 0; i < superbugCount; i++) {
			superbugs[i] = new SuperBug();
		}
	}

	private void getBonus2() {
		drink1 = new Bonus2[bonus2Count];
		for (int i = 0; i < bonus2Count; i++) {
			drink1[i] = new Bonus2();
		}
	}

	public boolean touched(Canvas canvas, int touchx, int touchy) {
		boolean touched = false;

		for (int i = 0; i < bugCount; i++) {
			if (bugs[i].touched(canvas, touchx, touchy)) {
				touched = true;
			}
		}

		if (Assets.gamescore > level1Score) {
			for (int i = 0; i < redAntCount; i++) {
				if (redAnts[i].touched(canvas, touchx, touchy)) {
					touched = true;
				}
			}
		}

		if (Assets.gamescore > level2Score) {
			for (int i = 0; i < LaydyBugCount; i++) {
				if (ladyBugs[i].touched(canvas, touchx, touchy)) {
					touched = true;
				}
			}
		}

		if (Assets.gamescore > level3Score) {
			for (SuperBug superbug : superbugs) {
				if (superbug.touched(canvas, touchx, touchy)) {
					touched = true;
				}
			}
		}

		if (gameControler.pauseTouched(canvas, touchx, touchy)) {
			touched = true;
		}

		if (leaf.touched(canvas, touchx, touchy)) {
			touched = true;
		}

		if (gameControler.musicIconTouched(canvas, touchx, touchy)) {
			touched = true;
		}

		if (gameControler.soundpoolIconTouched(canvas, touchx, touchy)) {
			touched = true;
		}

		return touched;
	}

	public void processBugs(Canvas canvas) {
		for (int i = 0; i < bugCount; i++) {
			// Draw dead bugs on screen
			bugs[i].drawDead(canvas);
			// Move bugs on screen
			bugs[i].move(canvas);
			// Bring a dead bug to life?
			bugs[i].birth(canvas);
		}

		if (Assets.gamescore > level1Score) {
			for (RedAnt redAnt : redAnts) {
				redAnt.drawDead(canvas);
				redAnt.move(canvas);
				redAnt.birth(canvas);
			}
		}

		if (Assets.gamescore > level2Score) {

			for (LadyBug bug : ladyBugs) {
				bug.drawDead(canvas);
				bug.move(canvas);
				bug.birth(canvas);
			}
		}
		if (Assets.gamescore > level3Score) {

			for (SuperBug superbug : superbugs) {
				superbug.drawDead(canvas);
				superbug.move(canvas);
				superbug.birth(canvas);
			}
		}

		if (Assets.livesLeft < 3
				&& (Assets.gamescore - Assets.oldScoreForLeaf) > 10) {
			if (Assets.oldScoreForLeaf == 0) {
				Assets.oldScoreForLeaf = Assets.gamescore;
			} else {
				if (Assets.hasLeaf == false) {
					leaf.showUp();
				}
				Assets.oldScoreForLeaf = Assets.gamescore;
			}
		}
		leaf.birth(canvas);
		leaf.move(canvas);

		/*
		 * reset a level for the game
		 */
		if (Assets.gamescore == level1Score
				|| Assets.gamescore == level2Score
				|| Assets.gamescore == level3Score
				|| (Assets.gamescore != 0 && Assets.gamescore % level1Score == 0)) {

			Assets.state = Assets.GameState.Level1;
		}

		if (Assets.gamescore == 20
				|| Assets.gamescore == 60
				|| Assets.gamescore == 150
				|| (Assets.gamescore > 0 && Assets.gamescore % level1Score == 90)) {

			setBugsToDissapear();
			setBonusToDead();// let bonus begin to birth
			Assets.state = Assets.GameState.Bonus;
		}

	}

	public void processPaused(Canvas canvas) {

		for (int i = 0; i < bugCount; i++) {
			// Draw dead bugs on screen
			bugs[i].drawWhenPaused(canvas);

		}

		if (Assets.gamescore > level1Score) {
			for (RedAnt redAnt : redAnts) {
				redAnt.drawWhenPaused(canvas);
			}
		}

		if (Assets.gamescore > level2Score) {
			for (LadyBug bug : ladyBugs) {
				bug.drawWhenPaused(canvas);

			}
		}
		if (Assets.gamescore > level3Score) {
			for (SuperBug superbug : superbugs) {
				superbug.drawWhenPaused(canvas);
			}

		}
		leaf.drawWhenPaused(canvas);

		// if (Assets.gamescore > goToBonusScore) {
		for (Bonus2 b2 : drink1) {
			b2.drawWhenPaused(canvas);
		}

		// }
	}

	public void playTouched(Canvas canvas, int touchx, int touchy) {
		gameControler.playTouched(canvas, touchx, touchy);

	}

	public void musicIconTouched(Canvas canvas, int touchx, int touchy) {
		gameControler.musicIconTouched(canvas, touchx, touchy);
	}

	public void soundpoolIconTouched(Canvas canvas, int touchx, int touchy) {
		gameControler.soundpoolIconTouched(canvas, touchx, touchy);
	}

	public boolean restartGameTouched(Canvas canvas, int touchx, int touchy) {
		return gameControler.restartGameTouched(canvas, touchx, touchy);
	}

	public void resetGameState() {
		for (int i = 0; i < bugCount; i++) {
			// Draw dead bugs on screen
			bugs[i].state = Bug.BugState.Dead;
		}

		if (Assets.gamescore > level1Score) {
			for (RedAnt redAnt : redAnts) {
				redAnt.state = RedAnt.BugState.Dead;

			}
		}

		if (Assets.gamescore > level2Score) {
			for (LadyBug bug : ladyBugs) {
				bug.state = LadyBug.BugState.Dead;

			}
		}
		if (Assets.gamescore > level3Score) {
			for (SuperBug superbug : superbugs) {
				superbug.state = SuperBug.BugState.Dead;

			}
		}
	}

	/*********************************************************************
	 * code for Level 1
	 * 
	 *******************************************************************/

	public boolean nextLevelTouched(Canvas canvas, int touchx, int touchy) {
		return gameControler.nextLevelTouched(canvas, touchx, touchy);
	}

	public void processLevel1State(Canvas canvas) {
		setBugsToDissapear();
		processPaused(canvas);
	}

	public void setBugsToDissapear() {
		leaf.state = Bonus.BonusState.Disappear;
		for (int i = 0; i < bugCount; i++) {
			bugs[i].state = Bug.BugState.Disappear;
		}
		if (Assets.gamescore > level1Score) {
			for (RedAnt redAnt : redAnts) {

				redAnt.state = RedAnt.BugState.Disappear;
			}
		}

		if (Assets.gamescore > level2Score) {
			for (LadyBug bug : ladyBugs) {
				bug.state = LadyBug.BugState.Dissappear;
			}
		}

		if (Assets.gamescore > level3Score) {
			for (SuperBug superbug : superbugs) {

				superbug.state = SuperBug.BugState.Dissappear;
			}
		}
	}

	public void resumeFromLevel1State() {
		for (int i = 0; i < bugCount; i++) {
			bugs[i].state = Bug.BugState.Dead;
		}
		if (Assets.gamescore > level1Score) {
			for (RedAnt redAnt : redAnts) {

				redAnt.state = RedAnt.BugState.Dead;
			}
		}

		if (Assets.gamescore > level2Score) {
			for (LadyBug bug : ladyBugs) {
				bug.state = LadyBug.BugState.Dead;
			}
		}

		if (Assets.gamescore > level3Score) {
			for (SuperBug superbug : superbugs) {

				superbug.state = SuperBug.BugState.Dead;
			}
		}
	}

	/*
	 * *************************************************************************
	 * 
	 * Add bonus code
	 */
	public boolean bonusTouched(Canvas canvas, int touchx, int touchy) {
		boolean touched = false;

		for (Bonus2 b : drink1) {
			if (b.touched(canvas, touchx, touchy)) {
				touched = true;
			}
		}

		if (gameControler.pauseTouched(canvas, touchx, touchy)) {
			touched = true;
		}

		if (gameControler.musicIconTouched(canvas, touchx, touchy)) {
			touched = true;
		}

		if (gameControler.soundpoolIconTouched(canvas, touchx, touchy)) {
			touched = true;
		}

		return touched;
	}

	public void processBonus(Canvas canvas) {
		for (Bonus2 b : drink1) {
			b.move(canvas);
			b.birth(canvas);
		}

	}

	public void setBonusToDisappear() {
		for (Bonus2 b : drink1) {
			b.setToDisappear();

		}
	}

	public void setBonusToDead() {
		for (Bonus2 b : drink1) {
			b.setToDead();

		}
	}

}
