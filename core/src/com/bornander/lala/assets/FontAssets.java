package com.bornander.lala.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.utils.Disposable;

public class FontAssets implements Disposable {
	public final BitmapFont dialog;
	public final BitmapFont menu_title;
	public final BitmapFont tag_line;

	
	public FontAssets() {
		dialog = buildFont("fonts/Chewy.ttf", 48, 4, Color.BLACK, "ABCDEFGHIJKLMNOPQRSTUVWXYZÅÄÖ1234567890\"!`?'.,;:()[]{}<>|/@\\^$-%+=#_&~*");
		menu_title = buildFont("fonts/Chewy.ttf", 128, 12, Color.BLACK, "ABCDEFGHIJKLMNOPQRSTUVWXYZÅÄÖ1234567890\"!`?'.,;:()[]{}<>|/@\\^$-%+=#_&~*");
		tag_line = buildFont("fonts/Chewy.ttf", 36, 2, Color.BLACK, "ABCDEFGHIJKLMNOPQRSTUVWXYZÅÄÖ1234567890\"!`?'.,;:()[]{}<>|/@\\^$-%+=#_&~*");
	}
	
	private static BitmapFont buildFont(String filename, int size, float borderWidth, Color borderColor, String characters) {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(filename));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = size;
		parameter.borderColor = borderColor;
		parameter.borderWidth = borderWidth;
		parameter.characters = characters;
		parameter.kerning = true;
		parameter.magFilter = TextureFilter.Linear;
		parameter.minFilter = TextureFilter.Linear;
		BitmapFont font = generator.generateFont(parameter);
		font.getData().markupEnabled = true;
		generator.dispose();
		return font;
	}

	@Override
	public void dispose() {
		dialog.dispose();
	}
}
