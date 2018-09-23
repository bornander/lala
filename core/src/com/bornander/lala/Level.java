package com.bornander.lala;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.bornander.lala.assets.TextAssets;
import com.bornander.lala.persisted.LevelInfo;
import com.bornander.lala.utils.Grid;
import com.bornander.libgdx.Box2DUtils;
import com.bornander.libgdx.FixedStepPhysicsUpdater;
import com.bornander.libgdx.Log;
import com.bornander.libgdx.Temp;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Level {

	public enum State {
		TRANSFORMING, WALKING, FLYING
	}

	
	public enum UpdateResult {
		NONE, GO_TO_TRANSFORM, GO_TO_MAINMENU
	}

	private final static Vector2 GRAVITY = new Vector2(0, -10);
	private final static float AVAILABLE_TRAY_HEIGHT = 1.25f;
	private final static Array<Body> bodies = new Array<Body>(256);
	private final Vector2 tempV2 = new Vector2();

	public final OrthographicCamera camera;
	public final OrthographicCamera hudCamera;

	public final float availableTrayScreenHeight;

	private final ShapeRenderer shapeRenderer = new ShapeRenderer();
	private final Box2DDebugRenderer box2dDebugRenderer = new Box2DDebugRenderer();
	private final BlockRenderer blockRenderer = new BlockRenderer();

	private final SpriteBatch spriteBatchWorld = new SpriteBatch();
	private final SpriteBatch spriteBatchHud = new SpriteBatch();
	private final ParticleEffect star_rain = Assets.instance.effects.star_rain.obtain();

	private final World world;
	private final FixedStepPhysicsUpdater physicsUpdater;

	public final LevelInfo levelInfo;
	public final LevelData levelData;
	private final TiledMap map;
	private final MapInfo mapInfo;
	private final OrthogonalTiledMapRenderer mapRenderer;

	private State state = State.TRANSFORMING;

	private Block[] available;
	private Set<Block> placed = new HashSet<Block>(64);
	public Alien alien;
	private Ship ship;
	public float availableTrayY;
	private float availableTrayTargetY;
	public DialogController dialogController;
	
	private AlienContactListener contactListener = new AlienContactListener();

	public Level(LevelInfo info) {
		levelInfo = info;
		levelData = info.getData();
		// TODO: Make this load asynchronous
		map = levelData.map;
		mapInfo = new MapInfo(map);

		world = buildWorld(contactListener);
		physicsUpdater = new FixedStepPhysicsUpdater(world);
		camera = buildCamera();

		hudCamera = new OrthographicCamera(levelData.viewportWidth, levelData.height);
		hudCamera.position.set(hudCamera.viewportWidth / 2, hudCamera.viewportHeight / 2.0f, 0.0f);
		hudCamera.update();
		availableTrayScreenHeight = hudCamera.project(new Vector3(0, AVAILABLE_TRAY_HEIGHT, 0)).y;

		mapRenderer = new OrthogonalTiledMapRenderer(map, 1.0f / 70.0f);

		reset();
	}

	private static World buildWorld(AlienContactListener contactListener) {
		World world = new World(GRAVITY, true);
		world.setContactListener(contactListener);
		return world;
	}

	private void buildTerrain() {

		for (Terrain terrain : levelData.terrain) {
			BodyDef bodyDefinition = new BodyDef();
			bodyDefinition.type = BodyType.StaticBody;

			PolygonShape shape = new PolygonShape();
			shape.set(terrain.polygon);

			FixtureDef fixtureDefinition = new FixtureDef();
			fixtureDefinition.shape = shape;
			fixtureDefinition.friction = 5.0f;
			fixtureDefinition.density = 10.0f;
			fixtureDefinition.restitution = 0.1f;

			Body body = world.createBody(bodyDefinition);
			body.setUserData(terrain.material);

			/* Fixture nodeFixture = */body.createFixture(fixtureDefinition);
			shape.dispose();
		}
	}

	private OrthographicCamera buildCamera() {

		float aspectRatio = (float) Gdx.graphics.getHeight() / (float) Gdx.graphics.getWidth();
		float viewportWidth = levelData.height / aspectRatio;

		OrthographicCamera camera = new OrthographicCamera(viewportWidth, levelData.height);
		float halfRows = levelData.height / 2.0f;
		camera.position.set(halfRows / aspectRatio, halfRows, 1.0f);
		camera.update();
		return camera;
	}

	private void buildAlien() {
		if (alien != null)
			alien.dispose();

		alien = new Alien(levelData);
		alien.getBody(world);

		for (Npc entity : levelData.entities)
			entity.setAlien(alien);

	}

	private void buildShip() {
		ship = new Ship(levelData);
	}

	public void reset() {
		contactListener.reset();

		placed.clear();
		Box2DUtils.destroyAllBodies(world);

		buildTerrain();

		available = levelData.getAvailable();

		buildAlien();
		buildShip();

		levelData.trackPosition(camera, alien.getTrackingTarget(), 1.0f);
		setState(State.TRANSFORMING);
	}

	public void restore() {
		contactListener.reset();
		Box2DUtils.destroyAllBodies(world);
		buildTerrain();

		List<Block> temp = new ArrayList<Block>(placed);
		placed.clear();
		for (Block block : temp) {
			place(block);
		}

		buildAlien();
		buildShip();

		levelData.trackPosition(camera, alien.getTrackingTarget(), 1.0f);
		setState(State.TRANSFORMING);

	}

	private void updateWalking(float delta, GameHud hud) {
		// Log.debug(delta);
		float pDelta = 0.017f;
		physicsUpdater.update(pDelta);
		physicsUpdater.update(pDelta);

		Alien.State alienState = alien.update(delta);
		levelData.trackPosition(camera, alien.getTrackingTarget(), 0.1f);

		switch (alienState) {
		case RETURNED:
			setState(State.TRANSFORMING);
			break;
		case CLIMBING_SHIP:
			hud.hideButtonsWhenCompleted();
			break;
		case IN_SHIP:
			dialogController.openDialog(Assets.instance.texts.getRandom(TextAssets.COMPLETED_IDS)); 
			float cx = camera.position.x;
			float cy = camera.position.y;
			star_rain.setPosition(cx, cy + camera.viewportHeight / 2);
			for (ParticleEmitter emitter : star_rain.getEmitters()) {
				emitter.getXOffsetValue().setLow(-camera.viewportWidth / 2, camera.viewportWidth / 2);
			}
			star_rain.start();
			ship.enterPilot();
			setState(State.FLYING);
			break;
		default:
			break;
		}
	}

	private void updateFlying(float delta) {
		alien.update(delta);
		ship.update(delta);
	}

	public UpdateResult update(float delta, boolean updateWorld, GameHud hud) {
		switch (state) {
		case TRANSFORMING:
			availableTrayTargetY = hud.getTrayTarget(0.0f, -1.5f);
			alien.update(delta);
			break;
		case WALKING:
			availableTrayTargetY = hud.getTrayTarget(0.0f, -1.5f);
			updateWalking(delta, hud);
			break;
		case FLYING:
			availableTrayTargetY = hud.getTrayTarget(0.0f, -1.5f);
			updateFlying(delta);
			break;
		}

		float trayDelta = availableTrayTargetY - availableTrayY;
		availableTrayY += trayDelta * 0.2f;

		star_rain.update(delta);

		for (ParticleEffect effect : levelData.background_effects)
			effect.update(delta);

		for (ParticleEffect effect : levelData.foreground_effects)
			effect.update(delta);
		
		for (ClickableEffect effect : levelData.clickable_effects) {
			effect.update(delta);
		}		

		for (Npc entity : levelData.entities)
			entity.update(delta);

		switch (state) {
		case TRANSFORMING:
			return UpdateResult.GO_TO_TRANSFORM;
		case FLYING:
			return UpdateResult.GO_TO_MAINMENU;
		default:
			return UpdateResult.NONE;
		}
	}

	private void debugRender() {
		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(Color.MAGENTA);

		// for (int row = 0; row <= levelData.height; ++row)
		// shapeRenderer.line(0, row, levelData.width, row);
		//
		// for (int column = 0; column <= levelData.width; ++column)
		// shapeRenderer.line(column, 0, column, levelData.height);

		shapeRenderer.end();

		box2dDebugRenderer.render(world, camera.combined);
	}

	private void renderBackground() {
		float x = camera.position.x;
		camera.position.x = camera.viewportWidth / 2.0f + camera.position.x * 0.5f;
		camera.update();
		mapRenderer.setView(camera);
		mapRenderer.render(mapInfo.background_images);
		mapRenderer.render(new int[] { 0 });
		camera.position.x = x;
		camera.update();
	}

	private Vector2 getCenterForAvailable(Block block, Vector2 target) {
		return target.set(block.id + 0.5f, availableTrayY + AVAILABLE_TRAY_HEIGHT / 2.0f);
	}

	public boolean isAboveTray(Vector2 point) {
		Vector2 p = Temp.vector2.obtain().set(point).sub(camera.position.x - camera.viewportWidth / 2.0f, 0.0f);

		float trayWidth = Math.max(available.length, 4);
		float trayOffset = (camera.viewportWidth - trayWidth) / 2.0f;
		boolean b = p.x > trayOffset && p.x < trayOffset + Math.max(available.length, 4) && p.y < AVAILABLE_TRAY_HEIGHT;

		Log.info("b=" + b + " x="+p.x + ", to=" + trayOffset + " ");
		return b;
	}

	public void renderAvailable() {
		spriteBatchHud.begin();
		hudCamera.update();
		spriteBatchHud.setProjectionMatrix(hudCamera.combined);

		float trayWidth = Math.max(available.length, 4);

		float blockOffset = (trayWidth - available.length) / 2.0f;
		float trayOffset = (camera.viewportWidth - trayWidth) / 2.0f;

		Assets.instance.hud.panel_metal_scaled.setColor(new Color(1, 1, 1, 0.85f));
		Assets.instance.hud.panel_metal_scaled.draw(spriteBatchHud, trayOffset, availableTrayY, trayWidth,
				AVAILABLE_TRAY_HEIGHT);

		for (Block available : available) {
			if (available != null) {
				blockRenderer.render(spriteBatchHud, available,
						getCenterForAvailable(available, tempV2).add(trayOffset + blockOffset, 0),
						levelData.oneOverMaxSize * 0.9f);
			}
		}
		spriteBatchHud.end();
	}

	public void render() {
		camera.update();
		spriteBatchWorld.setProjectionMatrix(camera.combined);
		Gdx.gl.glClearColor(0.1f, 0.2f, 0.8f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		renderBackground();

		spriteBatchWorld.begin();

		for (ParticleEffect effect : levelData.background_effects)
			effect.draw(spriteBatchWorld);
		ship.render(spriteBatchWorld);
		spriteBatchWorld.end();

		mapRenderer.setView(camera);
		mapRenderer.render(mapInfo.tiles_background);

		spriteBatchWorld.begin();

		world.getBodies(bodies);
		for (Body body : bodies) {
			Object userData = body.getUserData();
			if (userData instanceof Block) {
				Block block = (Block) userData;
				block.transform.set(body.getTransform().getPosition(), body.getAngle() * MathUtils.radiansToDegrees);
			}

			if (userData instanceof Alien) {
				Alien alien = (Alien) userData;
				alien.render(spriteBatchWorld);
			}
		}

		for (Block block : placed) {
			blockRenderer.render(spriteBatchWorld, block);
		}

		for (ParticleEffect effect : levelData.foreground_effects)
			effect.draw(spriteBatchWorld);
		
		for (ClickableEffect effect : levelData.clickable_effects) {
			effect.render(spriteBatchWorld);
		}

		spriteBatchWorld.end();
		mapRenderer.render(mapInfo.tiles_foreground);

		spriteBatchWorld.begin();
		for (Npc entity : levelData.entities)
			entity.render(spriteBatchWorld);

		star_rain.draw(spriteBatchWorld);

		spriteBatchWorld.end();

		renderAvailable();
		//debugRender();
	}

	public boolean isAvailable(Block picked) {
		return available[picked.id] != null;
	}

	public void grabBlock(Block block) {
		available[block.id] = null;
	}

	public void place(Block block) {
		Grid.snapToGrid(block, block.transform.position, block.transform.position);
		placed.add(block);
	}

	public boolean isSlotFree(Block block) {

		for (Block other : placed) {
			if (!other.equals(block) && other.collides(block)) {
				return false;
			}
		}

		for (Terrain terrainPart : levelData.terrain) {
			if (block.collides(terrainPart.polygon)) {
				return false;
			}
		}
		return true;
	}

	public Block getPlacedAt(Vector2 point) {
		Vector2 tv = Temp.vector2.obtain();
		Block closestBlock = null;
		for (Block block : placed) {
			tv.set(block.transform.position).sub(point);
			if (block.contains(point)) {
				closestBlock = block;
				break;
			}
			
		}
		Temp.vector2.free(tv);
		return closestBlock;
	}

	public void returnBlock(Block block) {
		block.transform.reset();
		placed.remove(block);
		available[block.id] = block;
	}

	public Block selectAvailable(int screenX, int screenY) {
		Block bestBlock = null;
		float bestDistance = Float.MAX_VALUE;
		Vector2 hudPoint = Temp.unproject(hudCamera, screenX, screenY);
		for (Block block : available) {
			if (block != null) {
				float trayWidth = Math.max(available.length, 4);
				float trayOffset = (camera.viewportWidth - trayWidth) / 2.0f;
				float blockOffset = (trayWidth - available.length) / 2.0f;

				float distance = getCenterForAvailable(block, tempV2).add(trayOffset + blockOffset, 0.0f).sub(hudPoint)
						.len();
				if (hudPoint.y < AVAILABLE_TRAY_HEIGHT && distance < bestDistance && distance < 1.0f) {
					bestDistance = distance;
					bestBlock = block;
				}
			}
		}

		return bestBlock;
	}

	public void setState(State newState) {
		Log.debug("Level state changing from %s to %s", state, newState);
		state = newState;
		if (state == State.WALKING)
			alien.startWalking();
	}

	public Block getFirstInBadPlace(Block checkFirst) {
		if (checkFirst != null && !isSlotFree(checkFirst))
			return checkFirst;

		for (Block block : placed) {
			if (!isSlotFree(block)) {
				return block;
			}
		}
		;

		return null;
	}

	public void addPlaced() {
		for (Block block : placed) {
			block.transform.push();
			block.getBody(world);
		}
	}

	public void restorePlaced() {
		for (Block block : placed) {
			block.transform.pop();
		}
	}

	public void checkClickables(Vector2 point) {
		for(ClickableEffect ce : levelData.clickable_effects) {
			ce.check(point);
		}
	}
}