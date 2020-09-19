package com.bornander.lala.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.bornander.lala.Assets;
import com.bornander.lala.DancingAlien;
import com.bornander.lala.DropOffShip;
import com.bornander.lala.GameScreen;
import com.bornander.lala.MapUtils;
import com.bornander.lala.persisted.PlanetInfo;
import com.bornander.lala.utils.Hud;
import com.bornander.libgdx.Log;
import com.bornander.libgdx.Temp;

import java.util.ArrayList;
import java.util.List;

public class HomePlanetScreen extends GameScreen {
	
	private enum State {
		LANDING,
		WALKING, 
		DANCING
	}
	
	protected final float vHeight = 240 * 3f;
	protected final float vWidth = vHeight / ((float)Gdx.graphics.getHeight() / (float)Gdx.graphics.getWidth());
	
	private final ShapeRenderer shapeRenderer = new ShapeRenderer();
	
	private final ParticleEffect[] background_effects;
	
	private final AlienSelectScreen ownerScreen;
	
	private final SpriteBatch spriteBatch = new SpriteBatch();
	private final PlanetInfo planetInfo;
	private final TiledMap map; 
	private final OrthographicCamera camera;
	private final OrthogonalTiledMapRenderer mapRenderer;
	private final Stage stage;
	private final float width;
	private final float height;
	private final Button home;
	private final GlyphLayout dialogGlyphLayout = new GlyphLayout();
	
	private boolean dialogEnabled = true;
	
	private State state;
	private DropOffShip ship = new DropOffShip();
	private List<DancingAlien> dancingAliens = new ArrayList<DancingAlien>();
	
	

	
	
	public HomePlanetScreen(Game game, AlienSelectScreen ownerScreen, PlanetInfo planetInfo) {
		super(game);
		this.ownerScreen = ownerScreen;
		this.planetInfo = planetInfo;
		
		map = Assets.instance.levels.load("homeworld.tmx");
		mapRenderer = new OrthogonalTiledMapRenderer(map, 1.0f / 70.0f);
		MapProperties properties = map.getProperties();
		
		background_effects = MapUtils.getEffects(map, "background_effects");

		
		float aspectRatio = (float)Gdx.graphics.getHeight() / (float)Gdx.graphics.getWidth();
		height = (float)properties.get("height", Integer.class);
		width = (float)properties.get("width", Integer.class);
		camera = new OrthographicCamera(height / aspectRatio, height);
		float halfRows = height / 2.0f;
		camera.position.set(halfRows / aspectRatio, halfRows, 1.0f); 
		camera.update();

		
		for(String landed : planetInfo.getLanded()) {
			Vector2 targetPosition = MapUtils.getRectanglePosition(map, "entities", "alien_target", landed, Temp.vector2.obtain());
			dancingAliens.add(new DancingAlien(targetPosition, Assets.instance.characters.getAlien(PlanetInfo.getPrefix(landed))));
			Temp.vector2.free(targetPosition);
		}		
		
		buildLander(planetInfo, height);

		
		ScreenViewport viewport = new ScreenViewport();
		viewport.setUnitsPerPixel(vHeight / Gdx.graphics.getHeight());
		
		
		stage = new Stage(viewport) {
			@Override
			public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        		boolean isHudTouch = super.touchDown(screenX, screenY, pointer, button);
        		if (!isHudTouch && pointer == 0) 
        			HomePlanetScreen.this.touchDown(screenX, screenY);
        		
				Vector2 p = Temp.vector2.obtain().set(Temp.unproject(camera, screenX, screenY));
				for(DancingAlien dancingAlien : dancingAliens) {
					if (dancingAlien.contains(p)) {
						dancingAlien.sing();
					}
				}
				


        		return isHudTouch;
			}
			
			@Override
			public boolean touchDragged(int screenX, int screenY, int pointer) {
				boolean isHudTouchDragged = super.touchDragged(screenX, screenY, pointer);
				if (!isHudTouchDragged && pointer == 0) 
					HomePlanetScreen.this.touchDragged(screenX, screenY);
				
				return isHudTouchDragged;
			}
			
			@Override
			public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        		boolean isHudTouch = super.touchUp(screenX, screenY, pointer, button);
        		if (!isHudTouch && pointer == 0)
        			HomePlanetScreen.this.touchUp(screenX, screenY);

        		return isHudTouch;
			}
			
			@Override
			public boolean keyDown(int keyCode) {
				if (keyCode == Keys.BACK || keyCode == Keys.BACKSPACE) {
					HomePlanetScreen.this.game.setScreen(new FadeScreen(HomePlanetScreen.this.game, HomePlanetScreen.this, HomePlanetScreen.this.ownerScreen, ""));
				}
				return true;
			}
		};
		
		home = Assets.instance.hud.getHome();

		layout();
		addButtonListeners();
		
		Gdx.input.setInputProcessor(stage);
		Gdx.input.setCatchBackKey(true);
		
		String message = Assets.instance.texts.get("homeplanet_0") + " " + planetInfo.getSavedCount() + " " + Assets.instance.texts.get("homeplanet_1");
		openDialog(message);
		
	}
	
	private void addButtonListeners() {
		home.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				if (event.getPointer() == 0) {
					game.setScreen(new FadeScreen(game, (GameScreen)game.getScreen(), ownerScreen, ""));
				}
			}
		});		
	}	
		
	
	private void layout() {
		float bs = 104;
		
		home.setTransform(true);
		
		home.setPosition(4, stage.getHeight() - bs);
		
		stage.addActor(home);

		home.setOrigin(Align.center);
	}	

	private void buildLander(PlanetInfo planetInfo, float height) {
		if (planetInfo.anyPending()) {
			state = State.LANDING;
			String landing = planetInfo.getNextToLand();
			Vector2 a = Temp.vector2.obtain();
			Vector2 b = Temp.vector2.obtain();
			Vector2 dropOffPosition = MapUtils.getRectanglePosition(map, "entities", "ship_dropoff", landing, a);
			Vector2 targetPosition = MapUtils.getRectanglePosition(map, "entities", "alien_target", landing, b);
			
			camera.position.set(targetPosition.x, camera.position.y, 1.0f); 
			camera.update();
			
			
			boolean shouldRecurse = false;
			if (dropOffPosition != null && targetPosition != null) {
				ship.initialize(height, dropOffPosition, targetPosition, Assets.instance.ships.get(PlanetInfo.getPrefix(landing)), Assets.instance.characters.getAlien(PlanetInfo.getPrefix(landing)));
			}
			else {
				Log.error("SUPER BAD FIX FIX FIX");
				shouldRecurse = true;
			}
			Temp.vector2.free(b);
			Temp.vector2.free(a);
			if (shouldRecurse)
				buildLander(planetInfo, height);
		}
		else {
			state = State.DANCING;
		}
	}
	
	public void openDialog(String text) {
		dialogEnabled = true;
		float dw = vWidth * 0.8f;
		dialogGlyphLayout.setText(Assets.instance.fonts.dialog, text, Color.WHITE, dw, Align.topLeft, true);		
	}
	
	public void closeDialog() {
		dialogEnabled = false;
	}	
	
	protected void touchDown(int screenX, int screenY) {
		Hud.dragTouchDown(screenX, screenY);
		dialogEnabled = false;
	}

	protected void touchDragged(int screenX, int screenY) {
		if (state == State.LANDING)
			return;
		
		Hud.dragTouchDrag(screenX, screenY, camera);
		
		float hw = camera.viewportWidth / 2.0f;
		float minX = hw;
		float maxX = width - hw;
		camera.position.x = Math.max(minX, Math.min(maxX, camera.position.x));
	}
	
	protected void touchUp(int screenX, int screenY) {
		//Log.debug("touchUp(%d, %d)", screenX, screenY);
	}	

	@Override
	public void update(float delta) {
		camera.update();
		for(ParticleEffect effect : background_effects)
			effect.update(delta);
		
		for(DancingAlien dancingAlien : dancingAliens)
			dancingAlien.update(delta);
		
		switch(state) {
		case LANDING:
			float x = camera.position.x;
			float dx = x - ship.alienTargetPosition.x;
			camera.position.x -= dx * 0.05f;
			if (ship.update(delta)) {
				dancingAliens.add(new DancingAlien(ship.alienTargetPosition, ship.alienAssets));
				buildLander(planetInfo, height);
			}
			break;
		}
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0,0,0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		shapeRenderer.begin(ShapeType.Filled);
		Color bottom = Color.GRAY;
		Color top = Color.WHITE;
		shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), top, top, bottom, bottom);
		shapeRenderer.end();	
		
		mapRenderer.setView(camera);

		mapRenderer.render();
		spriteBatch.setProjectionMatrix(camera.combined);
		spriteBatch.begin();

		
		for(ParticleEffect effect : background_effects)
			effect.draw(spriteBatch);
		
		for(DancingAlien dancingAlien : dancingAliens)
			dancingAlien.render(spriteBatch);


		
		
		spriteBatch.end();
		
//		shapeRenderer.setProjectionMatrix(camera.combined);
//		shapeRenderer.begin(ShapeType.Line);
//		for(DancingAlien dancingAlien : dancingAliens)
//			dancingAlien.renderBounds(shapeRenderer);
//		shapeRenderer.end();		
		
		if (dialogEnabled) {
			spriteBatch.setProjectionMatrix(stage.getCamera().combined);
			spriteBatch.begin();
			
			float tw = dialogGlyphLayout.width;
			float th = dialogGlyphLayout.height;
					
			float dw = tw + 0.05f * vWidth;
			float dh = th + 0.05f * vWidth;
			
			float bx = (vWidth - dw) / 2.0f;
			float by = vHeight - dh * 1.2f;
			
			Assets.instance.hud.panel_metal.draw(spriteBatch, bx, by, dw, dh);
			Assets.instance.fonts.dialog.draw(spriteBatch, dialogGlyphLayout, bx + (dw-tw) / 2.0f, by + dh - (dh-th) / 2.0f);
			
			spriteBatch.end();
		}
		
		spriteBatch.setProjectionMatrix(camera.combined);
		spriteBatch.begin();
		
		if (state == State.LANDING)
			ship.render(spriteBatch);
		
		spriteBatch.end();
		stage.draw();
		
	}

	
	@Override
	public void hide() {
		super.hide();
		planetInfo.save();
	}
	
	@Override
	public void pause() {
		super.pause();
		planetInfo.save();
	}
}
