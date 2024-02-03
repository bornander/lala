package com.bornander.lala.huds;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.bornander.lala.Assets;
import com.bornander.lala.Block;
import com.bornander.lala.BlockRenderer;
import com.bornander.lala.BlockVibrator;
import com.bornander.lala.GameHud;
import com.bornander.lala.Level;
import com.bornander.lala.screens.AlienSelectScreen;
import com.bornander.lala.screens.GameplayScreen;
import com.bornander.lala.screens.GameplayScreen.State;
import com.bornander.lala.utils.Grid;
import com.bornander.libgdx.FloatConsumer;
import com.bornander.libgdx.Lerper;
import com.bornander.libgdx.Temp;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.parallel;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.repeat;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.rotateTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.scaleTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import static com.bornander.lala.utils.Hud.dragTouchDown;
import static com.bornander.lala.utils.Hud.dragTouchDrag;

public class TransformingHud extends GameHud {
	
	public static class StateData {
		public final Block block;
		public final Vector2 position;
		
		public StateData(Block block, Vector2 position) {
			this.block = block;
			this.position = position;
		}
		
		public StateData(Block block) {
			this(block, null);
		}
		
		public boolean hasPosition() {
			return position != null;
		}
	}
	
	private final float DURATION = 0.25f;
	
	private final FloatConsumer angleConsumer = new FloatConsumer() {
		@Override
		public void updateValue(float value) {
			float fixed = value % 360;
			if (fixed < 0)
				fixed += 360;

			if (TransformingHud.this.picked != null) {
				TransformingHud.this.picked.transform.angle = fixed;
			}
		}
	};
	
	private final BlockRenderer blockRenderer = new BlockRenderer();
	
	private final GameplayScreen game;
	private final Button rotateLeft;
	private final Button rotateRight;
	private final Button mirror;
	private final SpriteBatch spriteBatch = new SpriteBatch();

	private Block picked;
	
	private Lerper lerper = null;
	private BlockVibrator blockVibrator = null;
	private boolean renderSnapped = true;
	private boolean dragGrabbed = false;
	private boolean isTransforming = false;
	
	private boolean isDualTouch = false;
	private Vector2 pointer2 = new Vector2();
	
	private boolean playBlockSound = false;
	
	
	protected final Stage tutorialStage;


	
	
	public TransformingHud(AlienSelectScreen homeScreen, float screenVerticalBase, GameplayScreen game, String musicTrack) {
		super(screenVerticalBase, homeScreen, game.game, musicTrack);
		this.game = game;
		tutorialStage = new Stage(viewport);

		rotateLeft = Assets.instance.hud.getRotateLeft();
		rotateRight = Assets.instance.hud.getRotateRight();
		mirror = Assets.instance.hud.getMirror();
		layout();
		addButtonListeners();
		

		
	}
	
	public void runTutorial1() {
		float scale = 1.5f;
		float duration = 0.5f;

		play.setZIndex(100);
		play.setOrigin(0, 0);
		play.addAction(repeat(2, sequence(scaleTo(scale, scale, duration, Interpolation.sine),scaleTo(1, 1, duration, Interpolation.sine))));
		
		
		Image cursor = Assets.instance.hud.getCursor();
		cursor.setPosition(vWidth/2.0f, vHeight/2.0f);
		tutorialStage.addActor(cursor);		
		
		cursor.addAction(sequence(
				moveTo(play.getX() + play.getWidth() / 2.0f, play.getY() - play.getHeight() / 2.0f, 2.0f, Interpolation.sineOut),
				fadeOut(0.25f, Interpolation.sineOut)
				));
		
		Image click = Assets.instance.hud.getClick();
		click.setPosition(play.getX(), play.getY());
		click.addAction(sequence(
				scaleTo(0, 0),
				delay(2.0f),
				parallel(
						scaleTo(4, 4, 2.0f),
						fadeIn(1.0f),
						delay(1.0f, fadeOut(1.0f))
						)
				));
		click.act(0);
		tutorialStage.addActor(click);			
		
	}
	
	public void runTutorial2() {
		float duration = 1;
		
		
		Image cursor = Assets.instance.hud.getCursor();
		cursor.setPosition(vWidth/2.0f, vHeight/2.0f);
		tutorialStage.addActor(cursor);		
		
		cursor.addAction(sequence(
				moveTo(vWidth / 2.0f, 0, 1.0f, Interpolation.sineOut),
				delay(1.0f),
				moveTo(vWidth / 2.0f, vHeight * 0.25f, 1.0f, Interpolation.sineOut),
				fadeOut(0.25f)
				));
		
		Image click = Assets.instance.hud.getClick();
		click.setPosition(vWidth / 2.0f, 0);
		click.addAction(sequence(
				fadeOut(0),
				moveTo(vWidth / 2.0f, 0.0f),
				scaleTo(0, 0),
				delay(1.0f),
				parallel(
						scaleTo(4, 4, 2.0f),
						fadeIn(1.0f),
						delay(1.0f, fadeOut(1.0f))
						),
				scaleTo(0, 0, 0.0f),
				moveTo(vWidth / 2.0f, vHeight * 0.25f),
				parallel(
						scaleTo(4, 4, 2.0f),
						fadeIn(1.0f),
						delay(1.0f, fadeOut(1.0f))
						)
				
				));
		click.act(0);
		tutorialStage.addActor(click);
		
		Image block = Assets.instance.hud.getBlock();
		block.setPosition((vWidth - block.getWidth()) / 2.0f, vHeight * 0.1f);
		block.addAction(sequence(
			fadeOut(0.0f),
			delay(2.0f),
			fadeIn(0.0f),
			moveTo((vWidth - block.getWidth()) / 2.0f, vHeight * 0.35f, 1.0f, Interpolation.sineOut),
			fadeOut(1.0f)
			));
		block.act(0);
		tutorialStage.addActor(block);
					
	}
	
	public void runTutorial3() {
		float duration = 1;
		
		
		Image cursor = Assets.instance.hud.getCursor();
		cursor.setPosition(vWidth/2.0f, vHeight/2.0f);
		tutorialStage.addActor(cursor);		
		
		cursor.addAction(sequence(
				moveTo(vWidth / 2.0f, 0, 1.0f, Interpolation.sineOut),
				delay(1.0f),
				moveTo(vWidth / 2.0f, vHeight * 0.25f, 1.0f, Interpolation.sineOut),
				delay(1.0f),
				moveTo(rotateRight.getX(), rotateRight.getY(), 1.0f, Interpolation.sineOut),
				fadeOut(0.5f)
				));
		
		Image click = Assets.instance.hud.getClick();
		click.setPosition(vWidth / 2.0f, 0);
		click.addAction(sequence(
				fadeOut(0),
				moveTo(vWidth / 2.0f, 0.0f),
				scaleTo(0, 0),
				delay(1.0f),
				parallel(
						scaleTo(4, 4, 2.0f),
						fadeIn(1.0f),
						delay(1.0f, fadeOut(1.0f))
						),
				scaleTo(0, 0, 0.0f),
				moveTo(vWidth / 2.0f, vHeight * 0.25f),
				parallel(
						scaleTo(4, 4, 2.0f),
						fadeIn(1.0f),
						delay(1.0f, fadeOut(1.0f))
						),
				scaleTo(0, 0, 0.0f),
				moveTo(rotateRight.getX(), rotateRight.getY()),
				parallel(
						scaleTo(4, 4, 2.0f),
						fadeIn(1.0f),
						delay(1.0f, fadeOut(1.0f))
						)				
				));
		click.act(0);
		tutorialStage.addActor(click);
		
		Image block = Assets.instance.hud.getTallBlock();
		block.setPosition((vWidth - block.getWidth()) / 2.0f, vHeight * 0.1f);
		block.addAction(sequence(
			fadeOut(0.0f),
			delay(2.0f),
			fadeIn(0.0f),
			moveTo((vWidth - block.getWidth()) / 2.0f, vHeight * 0.35f, 1.0f, Interpolation.sineOut),
			delay(2.4f),
			rotateTo(90, 1.0f),
			fadeOut(0.5f)
			));
		block.act(0);
		tutorialStage.addActor(block);
		
		rotateLeft.setZIndex(100);
		rotateRight.setZIndex(100);
		rotateLeft.setOrigin(mirror.getWidth(), 0);
		rotateRight.setOrigin(mirror.getWidth(), 0);

		rotateLeft.addAction(repeat(3, sequence(scaleTo(1.4f, 1.4f, duration, Interpolation.sine),scaleTo(1, 1, duration, Interpolation.sine))));		
		rotateRight.addAction(repeat(3, sequence(scaleTo(1.4f, 1.4f, duration, Interpolation.sine),scaleTo(1, 1, duration, Interpolation.sine))));			
					
	}	
	
	
	public void runTutorial4() {
		float duration = 1;
		
		
		Image cursor = Assets.instance.hud.getCursor();
		cursor.setPosition(vWidth/2.0f, vHeight/2.0f);
		tutorialStage.addActor(cursor);		
		
		cursor.addAction(sequence(
				moveTo(vWidth / 2.0f, 0, 1.0f, Interpolation.sineOut),
				delay(1.0f),
				moveTo(200+vWidth / 2.0f, vHeight * 0.25f, 1.0f, Interpolation.sineOut),
				delay(1.0f),
				moveTo(mirror.getX(), mirror.getY(), 1.0f, Interpolation.sineOut),
				fadeOut(0.5f)
				));
		
		Image click = Assets.instance.hud.getClick();
		click.setPosition(vWidth / 2.0f, 0);
		click.addAction(sequence(
				fadeOut(0),
				moveTo(vWidth / 2.0f, 0.0f),
				scaleTo(0, 0),
				delay(1.0f),
				parallel(
						scaleTo(4, 4, 2.0f),
						fadeIn(1.0f),
						delay(1.0f, fadeOut(1.0f))
						),
				scaleTo(0, 0, 0.0f),
				moveTo(200+vWidth / 2.0f, vHeight * 0.25f),
				parallel(
						scaleTo(4, 4, 2.0f),
						fadeIn(1.0f),
						delay(1.0f, fadeOut(1.0f))
						),
				scaleTo(0, 0, 0.0f),
				moveTo(mirror.getX(), mirror.getY()),
				parallel(
						scaleTo(4, 4, 2.0f),
						fadeIn(1.0f),
						delay(1.0f, fadeOut(1.0f))
						)				
				));
		click.act(0);
		tutorialStage.addActor(click);
		
		Image block1 = Assets.instance.hud.getTriangleBlock1();
		block1.setPosition((vWidth - block1.getWidth()) / 2.0f, vHeight * 0.1f);
		block1.addAction(sequence(
			fadeOut(0.0f),
			delay(2.0f),
			fadeIn(0.0f),
			moveTo(200+(vWidth - block1.getWidth()) / 2.0f, vHeight * 0.35f, 1.0f, Interpolation.sineOut),
			delay(2.0f),
			fadeOut(0.0f)
			));
		Image block2 = Assets.instance.hud.getTriangleBlock2();
		block2.setPosition(200+(vWidth - block2.getWidth()) / 2.0f, vHeight * 0.35f);
		block2.addAction(sequence(
			fadeOut(0.0f),
			delay(5f),
			fadeIn(0.0f),
			delay(1.0f),
			fadeOut(0.5f)
			));
		
		block1.act(0);
		tutorialStage.addActor(block1);
		block2.act(0);
		tutorialStage.addActor(block2);
		
		mirror.setZIndex(100);
		mirror.setOrigin(mirror.getWidth(), 0);

		mirror.addAction(repeat(3, sequence(scaleTo(1.4f, 1.4f, duration, Interpolation.sine),scaleTo(1, 1, duration, Interpolation.sine))));		
					
	}		
	
	private void layout() {
		float bs = 108;


		rotateLeft.setTransform(true);
		rotateRight.setTransform(true);
		mirror.setTransform(true);

		rotateLeft.setOrigin(Align.right);
		rotateRight.setOrigin(Align.right);
		mirror.setOrigin(Align.right);
		
		
		rotateLeft.setPosition(vWidth - bs, 0*verticalBase + bs * 0 + 4);
		rotateRight.setPosition(vWidth - bs, 0*verticalBase + bs * 1 + 4);
		mirror.setPosition(vWidth - bs, 0*verticalBase + bs * 2 + 4);
		
		stage.addActor(rotateLeft);
		stage.addActor(rotateRight);
		stage.addActor(mirror);
	}
	
	private void addButtonListeners() {
		rotateLeft.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				if (TransformingHud.this.picked == null)
					return;
				
				disableAll();
				renderSnapped = false;
				// TODO: Stop newing up here, add .set method on Lerper
				lerper = new Lerper(angleConsumer, DURATION, TransformingHud.this.picked.transform.angle, TransformingHud.this.picked.transform.angle + 90.0f);
			}
		});
		
		rotateRight.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				if (TransformingHud.this.picked == null)
					return;
				
				disableAll();
				renderSnapped = false;
				// TODO: Stop newing up here, add .set method on Lerper
				
				lerper = new Lerper(angleConsumer, DURATION, TransformingHud.this.picked.transform.angle, TransformingHud.this.picked.transform.angle - 90.0f);
			}
		});
		
		mirror.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				if (TransformingHud.this.picked == null)
					return;
				
				TransformingHud.this.picked.mirror();
			}
		});
	}
	
	private void confirmPickedBlock() {
		if (picked != null) {
			game.placeBlock(picked);
		}
	}
	
	private void disableAll() {
		setButtonsEnabled(false, play, stop, reset, rotateLeft, rotateRight, mirror);
		isTransforming = true;
	}

	private void enableAll() {
		setButtonsEnabled(true, play, reset, rotateLeft, rotateRight, mirror);
		isTransforming = false;
	}
	
	private int badBlockCounter = 0;
	private void vibrateBlock(Block block) {
		badBlockCounter++;
		if (badBlockCounter % 3 == 0) {
			this.openDialog(Assets.instance.texts.get("block_overlap"));
		}
		disableAll();
		renderSnapped = false;
		blockVibrator = new BlockVibrator(angleConsumer, 0.5f, block.transform.angle);
		
		if (!game.level.camera.frustum.pointInFrustum(block.transform.position.x, block.transform.position.y, 0)) {
			game.level.camera.position.set(block.transform.position.x, game.level.camera.position.y, 0);
			game.level.levelData.snapCamera(game.level.camera);
		}
	}
	
	@Override
	public void initialize(Object state) {
		super.initialize(state);
		
		game.level.restorePlaced();
		picked = null;
		enableAll();
		setButtonsEnabled(false, stop);
	}
	
	@Override
	protected void onPlayPressed() {
		super.onPlayPressed();

		if (picked != null) {
			game.level.place(picked);
		}
		
		Block badBlock = game.level.getFirstInBadPlace(picked);
		if (badBlock != null) {
			picked = badBlock;
			vibrateBlock(picked);
		}
		else {
			game.setState(State.RUNNING);
			game.level.setState(Level.State.WALKING);
		}		
	}
	
	@Override
	protected void onResetPressed() {
		super.onResetPressed();
		game.level.reset();
		game.setState(State.TRANSFORMING, null);
	}
	
	@Override
	public boolean update(float elapsed) {
		super.update(elapsed);
		tutorialStage.act(elapsed);
		if (lerper != null && lerper.update(elapsed)) {
			lerper = null;
			enableAll();
			renderSnapped = true;
		}
		
		if (blockVibrator != null && blockVibrator.update(elapsed)) {
			blockVibrator = null;
			enableAll();
			renderSnapped = true;
		}
		
		game.level.update(elapsed, false, this);
	
		return false;
	}
	
	@Override
	protected void touchDragged(int screenX, int screenY, int pointer) {
		super.touchDragged(screenX, screenY, pointer);
		if (!isTransforming) {
			Vector2 temp = Temp.vector2.obtain().set(Temp.unproject(game.level.camera, screenX, screenY));
			if (dragGrabbed && picked != null)
				picked.transform.position.set(temp);
			else {
				dragTouchDrag(screenX, screenY, game.level.camera);
				game.level.levelData.snapCamera(game.level.camera);
			}
			Temp.vector2.free(temp);
		}
	}
	
	@Override
	protected void touchDown(int screenX, int screenY, int pointer) {
		playBlockSound = false;
		super.touchDown(screenX, screenY, pointer);
		if (!isTransforming) {
			if (pointer == 0) {
				Vector2 temp = Temp.vector2.obtain().set(Temp.unproject(game.level.camera, screenX, screenY));
				dragTouchDown(screenX, screenY);
				// TODO: Should the distance here be the bounding radius?
				if (picked != null && picked.contains(temp)) {
					dragGrabbed = true;
					playBlockSound = true;
				}
				else {
					Block block = game.level.getPlacedAt(Temp.unproject(game.level.camera, screenX, screenY));
					if (block != null) {
						confirmPickedBlock();
						picked = block;
						dragGrabbed = true;
						playBlockSound = true;
					}	
					else {
						Block available = game.level.selectAvailable(screenX, screenY);
						if (available != null) {
							game.level.grabBlock(available);
							confirmPickedBlock();
							picked = available;
							dragGrabbed = true;
							picked.transform.position.set(temp);
							playBlockSound = true;
	
						}
						else {
							confirmPickedBlock();
							//picked = null;
							Vector2 temp2 = Temp.vector2.obtain().set(Temp.unproject(game.level.camera, screenX, screenY));
							Vector2 temp3 = Temp.vector2.obtain();
							game.level.alien.getPosition(temp3);
							if (temp3.sub(temp2).len()<0.5f) {
								game.level.alien.speak();
							}
							else {
								game.level.checkClickables(temp2);
							}
							Temp.vector2.free(temp3);
							Temp.vector2.free(temp2);
						}
					}
				}
				Temp.vector2.free(temp);
			}
			else {
				isDualTouch = true;
			}
		}
	}
	
	@Override
	protected void touchUp(int screenX, int screenY, int pointer) {
		if (pointer == 1) {
			isDualTouch = false;
		}
		dragGrabbed = false;
		if (!isTransforming) {
			if (picked != null) {
				if (game.level.isAboveTray(picked.transform.position)) {
					game.level.returnBlock(picked);
					picked = null;
				}
				else {
					Grid.snapToGrid(picked, picked.transform.position, picked.transform.position);
					if (playBlockSound)
						Assets.instance.sounds.playStep(picked.definition.material);
				}
			}
		}
	}
	
	@Override
	public void draw() {
		
		spriteBatch.begin();
		spriteBatch.setProjectionMatrix(game.level.camera.combined);
		
		if (picked != null) {
			boolean isBelowLimit = picked.isBelow(1.5f);
			Vector2 temp = new Vector2();
			if (renderSnapped && !isBelowLimit) {
				Grid.snapToGrid(picked, picked.transform.position, temp);
				spriteBatch.setColor(1.0f, 1.0f, 1.0f, 0.5f);
				blockRenderer.render(spriteBatch, picked, temp);
			}
	
			spriteBatch.setColor(Color.WHITE);
			blockRenderer.render(spriteBatch, picked);
			
			if (renderSnapped && !isBelowLimit) {
				if (!game.level.isSlotFree(picked)) {
					spriteBatch.setColor(1.0f, 0.0f, 0.0f, 0.5f);
					blockRenderer.render(spriteBatch, picked, temp);
				}
			}
		}
		
		if (picked != null) {
			spriteBatch.setColor(0.5f, 0.5f, 1.0f, 0.75f);
			blockRenderer.render(spriteBatch, picked, picked.transform.position);
		}

		spriteBatch.end();
		
		if (picked != null) {
			blockRenderer.renderShape(picked, Color.CYAN, game.level.camera);
		}
		
		super.draw();

	}
	
	@Override
	public void postDraw() {
		spriteBatch.begin();
		spriteBatch.setProjectionMatrix(game.level.camera.combined);
		if (picked != null) {
			boolean isBelowLimit = picked.isBelow(1.5f);
			Vector2 temp = new Vector2();
			if (renderSnapped && !isBelowLimit) {
				Grid.snapToGrid(picked, picked.transform.position, temp);
				spriteBatch.setColor(1.0f, 1.0f, 1.0f, 0.5f);
				blockRenderer.render(spriteBatch, picked, temp);
			}
	
			spriteBatch.setColor(Color.WHITE);
			blockRenderer.render(spriteBatch, picked);
			
			if (renderSnapped && !isBelowLimit) {
				if (!game.level.isSlotFree(picked /*, temp*/)) {
					spriteBatch.setColor(1.0f, 0.0f, 0.0f, 0.5f);
					blockRenderer.render(spriteBatch, picked, temp);
				}
			}
		}
		
		if (picked != null) {
			spriteBatch.setColor(0.5f, 0.5f, 1.0f, 0.75f);
			blockRenderer.render(spriteBatch, picked, picked.transform.position);
		}

		spriteBatch.end();
		
		if (picked != null) {
			blockRenderer.renderShape(picked, Color.CYAN, game.level.camera);
		}

		super.postDraw();
		tutorialStage.draw();
	}
	
	/*
	public void fireButtonHighlights(List<String> buttons) {
		super.fireButtonHighlights(buttons);
		float scale = 1.5f;
		float duration = 0.5f;
		if (buttons.contains("MIRROR")) {
			mirror.setZIndex(100);
			mirror.setOrigin(mirror.getWidth(), 0);
			mirror.addAction(repeat(8, sequence(scaleTo(scale, scale, duration, Interpolation.sine),scaleTo(1, 1, duration, Interpolation.sine))));
		}
		if (buttons.contains("ROTATE")) {
			rotateLeft.setZIndex(100);
			rotateRight.setZIndex(100);
			rotateLeft.setOrigin(mirror.getWidth(), 0);
			rotateRight.setOrigin(mirror.getWidth(), 0);

			rotateLeft.addAction(repeat(8, sequence(scaleTo(scale, scale, duration, Interpolation.sine),scaleTo(1, 1, duration, Interpolation.sine))));		
			rotateRight.addAction(repeat(8, sequence(scaleTo(scale, scale, duration, Interpolation.sine),scaleTo(1, 1, duration, Interpolation.sine))));		
		}
	}
	*/
	
	@Override
	public float getTrayTarget(float showing, float hidden) {
		if (dragGrabbed) {
			if (picked != null && picked.isBelow(0.5f)) {
				return showing;
			}
			else {
				return hidden;
			}
		}
		else {
			return showing;
		}
	}
	
}