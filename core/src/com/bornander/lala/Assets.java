package com.bornander.lala;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.Disposable;
import com.bornander.lala.assets.AliensAssets;
import com.bornander.lala.assets.BackgroundAssets;
import com.bornander.lala.assets.BlockAssets;
import com.bornander.lala.assets.EffectAssets;
import com.bornander.lala.assets.FontAssets;
import com.bornander.lala.assets.HudAssets;
import com.bornander.lala.assets.LevelAssets;
import com.bornander.lala.assets.MenuAssets;
import com.bornander.lala.assets.MusicAssets;
import com.bornander.lala.assets.NpcAssets;
import com.bornander.lala.assets.ShipsAssets;
import com.bornander.lala.assets.SoundAssets;
import com.bornander.lala.assets.TextAssets;
import com.bornander.libgdx.Log;

public class Assets implements Disposable {

	private static final AssetDescriptor<TextureAtlas> ATLAS_HUD = new AssetDescriptor<TextureAtlas>("graphics/hud.atlas", TextureAtlas.class);
	private static final AssetDescriptor<TextureAtlas> ATLAS_BLOCKS = new AssetDescriptor<TextureAtlas>("graphics/blocks.atlas", TextureAtlas.class);
	// TODO: Rename this to something covering aliens and ships
	private static final AssetDescriptor<TextureAtlas> ATLAS_CHARACTERS = new AssetDescriptor<TextureAtlas>("graphics/characters.atlas", TextureAtlas.class);
	private static final AssetDescriptor<TextureAtlas> ATLAS_TILES = new AssetDescriptor<TextureAtlas>("graphics/tiles.atlas", TextureAtlas.class);
	private static final AssetDescriptor<TextureAtlas> ATLAS_EFFECTS = new AssetDescriptor<TextureAtlas>("graphics/effects.atlas", TextureAtlas.class);
	private static final AssetDescriptor<TextureAtlas> ATLAS_MENU = new AssetDescriptor<TextureAtlas>("graphics/menu.atlas", TextureAtlas.class);
	
	private AssetManager assetManager;
	
	public final static Assets instance = new Assets();
	
	public HudAssets hud;
	public BlockAssets blocks;
	public AliensAssets characters;
	public ShipsAssets ships;
	public LevelAssets levels;
	public EffectAssets effects;
	public NpcAssets npcs;
	public FontAssets fonts;
	public MenuAssets menu;
	public SoundAssets sounds;
	public MusicAssets music;
	public BackgroundAssets backgrounds;
	public TextAssets texts;
	
	private Assets() {
	}
	
	public void initialize(AssetManager assetManager) {
		Log.info("Initializing assets");
		this.assetManager = assetManager;
		assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
		assetManager.load(ATLAS_HUD);
		assetManager.load(ATLAS_BLOCKS);
		assetManager.load(ATLAS_CHARACTERS);
		assetManager.load(ATLAS_TILES);
		assetManager.load(ATLAS_EFFECTS);
		assetManager.load(ATLAS_MENU);
		assetManager.finishLoading();
		hud = new HudAssets(assetManager.get(ATLAS_HUD));
		blocks = new BlockAssets(assetManager.get(ATLAS_BLOCKS));
		characters = new AliensAssets(assetManager.get(ATLAS_CHARACTERS));
		ships = new ShipsAssets(assetManager.get(ATLAS_CHARACTERS));
		levels = new LevelAssets();
		effects = new EffectAssets(assetManager.get(ATLAS_EFFECTS));
		npcs = new NpcAssets(assetManager.get(ATLAS_CHARACTERS));
		fonts = new FontAssets();
		menu = new MenuAssets(assetManager.get(ATLAS_MENU));
		sounds = new SoundAssets();
		if (music != null)
			music.dispose();
		music = new MusicAssets();
		backgrounds = new BackgroundAssets();
		texts = new TextAssets(LalaGame.TextResolver);
		assetManager.finishLoading();
		Log.info("Assets initialized");
	}
	
	public Object load(String path, Class<?> clazz) {
		assetManager.load(path, clazz);
		assetManager.finishLoading();
		return assetManager.get(path);
	}
	
	@Override
	public void dispose() {
		Log.info("Disposing assets");
		assetManager.dispose();
		fonts.dispose();
		sounds.dispose();
		music.dispose();
		backgrounds.dispose();
		effects.dispose();
		Log.info("Assets have been disposed");
	}

	public void unload(String fileName) {
		assetManager.unload(fileName);
	}
}
