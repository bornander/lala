package com.bornander.lala.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.bornander.lala.Assets;
import com.bornander.lala.GameScreen;
import com.bornander.lala.Level;
import com.bornander.lala.Material;
import com.bornander.lala.TiledBackground;
import com.bornander.lala.persisted.ChapterInfo;
import com.bornander.lala.persisted.LevelInfo;
import com.bornander.lala.persisted.PlanetInfo;
import com.bornander.libgdx.Collections;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.alpha;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.parallel;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.rotateTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.scaleTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

public class AlienSelectScreen extends GameScreen {
	
	private final static boolean DEBUG_ALL_UNLOCKED = false;
	
	private final static int NUMBER_OF_LEVELS_PER_CHAPTER = 12;
	private final static int NUMBER_OF_LEVELS_PER_ROW = 6;
	
	private final static Random RND = new Random();
	
	protected final float vHeight = 1024;
	protected final float vWidth = vHeight / ((float)Gdx.graphics.getHeight() / (float)Gdx.graphics.getWidth());

	protected final Stage stage;
	
	private final List<Button> buttons = new ArrayList<Button>(5);

	private final TiledBackground background;
	private final List<Button> levelButtons = new ArrayList<Button>(32);
	private final Map<Button, Image> levelImages = new HashMap<Button, Image>(32);

	private final Label savedCount;

	private final PlanetInfo planetInfo = PlanetInfo.load();
	
	private LevelInfo justCompleted;
	private Set<LevelInfo> justUnlockedLevels = new HashSet<LevelInfo>();
	private Set<ChapterInfo> justUnlockedChapters = new HashSet<ChapterInfo>();
	
	
	final float[] yPos = new float[] {
			vHeight * 0.3f,
			vHeight * 0.1f
	};
	
	final float width = vWidth * 0.8f;
	final float bw = 128;
	final float spacing = (width - bw) / (NUMBER_OF_LEVELS_PER_ROW - 1);
	final float margin = (vWidth - width) / 2.0f;	
	
	
	private ChapterInfo currentChapter = null;
	
	public AlienSelectScreen(Game game) {
		super(game);
		
		ScreenViewport viewport = new ScreenViewport();
		viewport.setUnitsPerPixel(vHeight / Gdx.graphics.getHeight());

		stage = new Stage(viewport);	
		background = new TiledBackground(vWidth, Assets.instance.backgrounds.coloredDesert, stage);


		Button homeworldButton = Assets.instance.menu.getByMaterial(Material.PLANET);
		homeworldButton.setPosition(vWidth - homeworldButton.getWidth() * 1.1f, vHeight - homeworldButton.getHeight() * 1.1f);
		stage.addActor(homeworldButton);
		homeworldButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				AlienSelectScreen.this.game.setScreen(new HomePlanetScreen(AlienSelectScreen.this.game, AlienSelectScreen.this, planetInfo));
			}
		});
		
		savedCount = new Label("" + planetInfo.getSavedCount(), new LabelStyle(Assets.instance.fonts.dialog, Color.WHITE));
		savedCount.setAlignment(Align.center);
		savedCount.setPosition(
				homeworldButton.getX() + homeworldButton.getWidth() / 2 - savedCount.getWidth() / 2,
				homeworldButton.getY() + homeworldButton.getHeight() / 2 - savedCount.getHeight() / 2
				);
		
		savedCount.setTouchable(Touchable.disabled);
		stage.addActor(savedCount);
		
		
		float bindex = 0;
		for(final ChapterInfo chapter : ChapterInfo.load()) {
			
			final Button chapterButton = Assets.instance.menu.getByAlien(chapter.id);
			float cv = vHeight * 0.6f;
			float cbw = chapterButton.getWidth();
			float cspacing = (width - cbw) / (5 - 1);
			float cmargin = (vWidth - width) / 2.0f;

			chapterButton.setUserObject(chapter);
			chapterButton.setTransform(true);
			chapterButton.setPosition(cmargin + bindex *  cspacing, cv);
			stage.addActor(chapterButton);
			bindex++;
			if (!chapter.unlocked) {
				if (!DEBUG_ALL_UNLOCKED) {
					chapterButton.setTouchable(Touchable.disabled);
					chapterButton.setDisabled(true);
				}
			}
			else {
				if (areAllCompleted(chapter)) {
					Image image = new Image(Assets.instance.menu.starGold);
					

					image.setPosition(chapterButton.getX(), chapterButton.getY());
					image.setTouchable(Touchable.disabled);
					AlienSelectScreen.this.stage.addActor(image);

				}
			}
			
			chapterButton.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					super.clicked(event, x, y);
					swapChapter(chapter);
				}
			});
				
			buttons.add(chapterButton);
		}
		
		LabelStyle labelStyle = new LabelStyle(Assets.instance.fonts.menu_title, Color.WHITE);
		Label label = new Label("LALA", labelStyle);
		
		stage.addActor(label);
		
		label.setPosition((vWidth - label.getWidth()) / 2.0f, vHeight - label.getHeight());
		
		
		
		Pixmap pm = new Pixmap(Gdx.files.internal("graphics/cursor.png"));
		//Gdx.input.setc .setCursorImage(pm, 0, 0);
		pm.dispose();		
		
		
		//layout();
	}
	
	private boolean areAllCompleted(final ChapterInfo chapter) {
		boolean allCompleted = true;
		for(int row = 0; row < 2; ++row) {
			for(int column = 0; column < NUMBER_OF_LEVELS_PER_ROW; ++column) {
				allCompleted &= isLevelCompleted(chapter, row, column);
			}		
		}
		return allCompleted;
	}
	
	public boolean isLevelCompleted(final ChapterInfo chapter, int row, int column) {
		LevelInfo levelInfo = chapter.levels[row * NUMBER_OF_LEVELS_PER_ROW + column];
		return levelInfo.completed;
	}
	
	private boolean createLevelButtons(final ChapterInfo chapter, float[] yPos, float spacing, float margin) {
		boolean allCompleted = true;
		for(int row = 0; row < 2; ++row) {
			for(int column = 0; column < NUMBER_OF_LEVELS_PER_ROW; ++column) {
				allCompleted &= createLevelButton(chapter, yPos, spacing, margin, row, column);
			}		
		}
		return allCompleted;
	}

	private boolean createLevelButton(final ChapterInfo chapter, float[] yPos, float spacing, float margin, int row, int column) {
		final LevelInfo levelInfo = chapter.levels[row * NUMBER_OF_LEVELS_PER_ROW + column];
		Button button = Assets.instance.menu.getByMaterial(levelInfo.type);
		button.setUserObject(levelInfo);
		
		Image image = levelInfo.completed ? new Image(Assets.instance.menu.starGold) : null;
		button.setTransform(true);

		if (row == 0 && column == 0)
			button.setDisabled(false);
		else {
			if (!DEBUG_ALL_UNLOCKED) {
				button.setDisabled(!levelInfo.unlocked);
				button.setTouchable(levelInfo.unlocked ? Touchable.enabled : Touchable.disabled);
			}
		}
		
		float xPos = margin + column * spacing;
		button.setPosition(xPos, vHeight +  row * button.getHeight());
		if (levelInfo.completed) {
			image.setPosition(-image.getWidth(), 0);
			image.setTouchable(Touchable.disabled);
		}
		
		float timing = MathUtils.random(0.8f, 1.0f);
		button.addAction(moveTo(xPos, yPos[row], timing, Interpolation.sine));
		if (levelInfo.completed) {
			image.addAction(moveTo(xPos, yPos[row], timing, Interpolation.sine));
		}
		
		AlienSelectScreen.this.stage.addActor(button);
		levelButtons.add(button);
		
		if (levelInfo.completed) {
			AlienSelectScreen.this.stage.addActor(image);
			levelImages.put(button, image);
		}
		
		button.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				Level level = new Level(levelInfo); 				
				// TODO: Change the constructor of GameplayScreen to only take AlientSelectScreen and level?
				//AlienSelectScreen.this.game.setScreen(new GameplayScreen(AlienSelectScreen.this.game, AlienSelectScreen.this, level));
				AlienSelectScreen.this.game.setScreen(
						new FadeScreen(AlienSelectScreen.this.game, AlienSelectScreen.this, new GameplayScreen(AlienSelectScreen.this.game, AlienSelectScreen.this, level), levelInfo.toString()));
			}
		});
		
		return levelInfo.completed;
	}	
	

	public void setJustCompleted(LevelInfo justCompleted) {
		this.justCompleted = justCompleted;
	}
	
	public void addUnlocked(LevelInfo level) {
		justUnlockedLevels.add(level);
	}
	
	public void addUnlocked(ChapterInfo chapter) {
		justUnlockedChapters.add(chapter);
	}
	
	
	@Override
	public void show() {
		super.show();
		Gdx.input.setInputProcessor(stage);
		Assets.instance.music.play("subdued_theme");
		
		savedCount.setText("" + planetInfo.getSavedCount());
		
		if (justCompleted != null) {
			Assets.instance.sounds.playLevelCompletedFirstTime();
			planetInfo.setAsPending(justCompleted);
			planetInfo.save();
			for(Actor actor : stage.getActors()) {
				if (actor instanceof Button && justCompleted.equals(actor.getUserObject())) {
					Image image = new Image(Assets.instance.menu.starGold);
					levelImages.put((Button)actor, image);

					stage.addActor(image);					
					image.setPosition(actor.getX(), actor.getY());
					image.setColor(image.getColor().r, image.getColor().g, image.getColor().b, 0.0f);
					image.addAction(
							parallel(
									alpha(1, 1, Interpolation.sine),
									sequence(scaleTo(2, 2, 1, Interpolation.sine), scaleTo(1, 1, 1, Interpolation.sine)))
							);
					break;
				}
			}
			justCompleted = null;
		}
		
		for(LevelInfo unlocked : justUnlockedLevels) {
			for(Actor actor : stage.getActors()) {
				if (actor instanceof Button && unlocked.equals(actor.getUserObject())) {
					((Button)actor).setTouchable(Touchable.enabled);
					((Button)actor).setDisabled(false);
					Image image = new Image(Assets.instance.menu.locked);
					levelImages.put((Button)actor, image);

					stage.addActor(image);					
					image.setPosition(actor.getX(), actor.getY());
					float duration = 1.0f + RND.nextFloat()*0.5f;
					image.addAction(
							parallel(
									moveTo(actor.getX(), -image.getHeight()*2, duration, Interpolation.swingIn),
									rotateTo(180.0f * (RND.nextFloat() - 0.5f), duration, Interpolation.sineIn)
									)
							);
					break;
				}
			}			
		}
		
		for(ChapterInfo unlocked : justUnlockedChapters) {
			for(Actor actor : stage.getActors()) {
				if (actor instanceof Button && unlocked.equals(actor.getUserObject())) {
					Button button = (Button)actor;
					
					button.setTouchable(Touchable.enabled);
					button.setDisabled(false);
					Image image = new Image(Assets.instance.menu.locked);
					levelImages.put(button, image);

					stage.addActor(image);					
					image.setPosition(button.getX(), button.getY());
					float duration = 2.0f + RND.nextFloat()*0.5f;
					image.addAction(
							parallel(
									moveTo(button.getX(), - image.getHeight()*2, duration, Interpolation.swingIn),
									rotateTo(180.0f * (RND.nextFloat() - 0.5f), duration, Interpolation.sineIn)
									)
							);
					
					button.setOrigin(button.getWidth()/2.0f,button.getHeight()/2.0f);
					button.setRotation(0);
					button.addAction(parallel(
							rotateTo(360, duration),
							sequence(
									scaleTo(1.5f, 1.5f, duration/2.0f, Interpolation.sine),
									scaleTo(1.0f, 1.0f, duration/2.0f, Interpolation.sine)
									)
							));
					
					if (currentChapter != null && areAllCompleted(currentChapter))
						swapChapter(unlocked);
					
					break;
				}
			}			
		}
		
		
		justUnlockedLevels.clear();
		justUnlockedChapters.clear();
	}
	
	private void layout() {
		float cv = vHeight * 0.6f;
		float width = vWidth * 0.8f;
		float bw = buttons.get(0).getWidth();
		float spacing = (width - bw) / (buttons.size() - 1);
		float margin = (vWidth - width) / 2.0f;
		
		for(int i = 0; i < buttons.size(); ++i) {
			Button button = buttons.get(i);
			button.setTransform(true);
			button.setPosition(margin + i *  spacing, cv);
			stage.addActor(button);
		}
	}
	
	@Override
	public void update(float delta) {
		//background.update(delta);
		stage.act(delta);
	}
	
	@Override
	public void render() {
		//Gdx.gl.glClearColor(0,0,0, 1);
		//Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.draw();
	}
	
	@Override
	public void dispose() {
		super.dispose();
		background.dispose();
	}

	private void swapChapter(final ChapterInfo chapter) {
		for(Button button : levelButtons) {
			button.setTouchable(Touchable.disabled);
			float d = MathUtils.random(0.8f, 1.0f);
			button.addAction(sequence(moveTo(button.getX(), -button.getWidth(), d, Interpolation.pow4), Actions.removeActor()));
			
			Image image = Collections.getOrDefault(levelImages, button, null);
			if (image != null) {
				image.addAction(sequence(moveTo(image.getX(), -image.getWidth(), d, Interpolation.pow4), Actions.removeActor()));							
			}
		}
		
		levelButtons.clear();
		
		createLevelButtons(chapter, yPos, spacing, margin);
		currentChapter = chapter;
	}
}