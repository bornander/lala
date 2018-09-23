package com.bornander.lala.huds;

import com.bornander.lala.GameHud;
import com.bornander.lala.persisted.ChapterInfo;
import com.bornander.lala.persisted.LevelInfo;
import com.bornander.lala.screens.AlienSelectScreen;
import com.bornander.lala.screens.GameplayScreen;
import com.bornander.lala.screens.GameplayScreen.State;
import com.bornander.libgdx.Timeout;

public class RunningHud extends GameHud {

	private final GameplayScreen game;
	private final Timeout completedTimeout = new Timeout(3.0f);
	
	public RunningHud(AlienSelectScreen homeScreen, float screenVerticalBase, GameplayScreen sourceGame, String musicTrack) {
		super(screenVerticalBase, homeScreen, sourceGame.game, musicTrack);
		this.game = sourceGame;
		initializeStatic();
	}
	
	private void initializeStatic() {
		setButtonsEnabled(false, play);
	}
	
	@Override
	protected void onStopPressed() {
		super.onStopPressed();
		game.level.restore();
		game.setState(State.TRANSFORMING);
	}
	
	@Override
	protected void onResetPressed() {
		game.level.reset();
		game.setState(State.TRANSFORMING);
	}
	
	@Override
	public void initialize(Object state) {
		super.initialize(state);
		initializeStatic();
		game.level.addPlaced();
	}
	
	@Override
	public boolean update(float elapsed) {
		super.update(elapsed);
		completedTimeout.update(elapsed);
		
		switch(game.level.update(elapsed, true, this)) {
		case GO_TO_TRANSFORM:	
			onStopPressed();
			break;
		case GO_TO_MAINMENU:
			completedTimeout.start();
			
			if (completedTimeout.getState() == Timeout.State.ELAPSED) {
				if (!game.level.levelInfo.completed)
					homeScreen.setJustCompleted(game.level.levelInfo);
				
				for(int unlockedId : game.level.levelData.unlocks_levels) {
					LevelInfo levelToUnlock = game.level.levelInfo.chapter.levels[unlockedId];
					if (!levelToUnlock.unlocked) {
						levelToUnlock.unlocked = true;
						homeScreen.addUnlocked(levelToUnlock);
						levelToUnlock.save();
					}
				}
				
				if (game.level.levelData.unlocks_next_chapter) {
					ChapterInfo chapter = game.level.levelInfo.chapter;
					chapter.save();
					if (chapter.hasNext()) {
						ChapterInfo next = chapter.next;
						if (!next.unlocked) {
							next.unlocked = true;
							next.save();
							homeScreen.addUnlocked(next);
						}
					}
				}

				game.level.levelInfo.setLevelCompleted();
				game.level.levelInfo.save();
				return true;
			}
			break;
		}
		
		return false;
	}
}
