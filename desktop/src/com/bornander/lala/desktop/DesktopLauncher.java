package com.bornander.lala.desktop;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.bornander.lala.LalaGame;
import com.bornander.libgdx.StringResolver;

import java.util.HashMap;
import java.util.Map;

public class DesktopLauncher {
	
	private static class DesktopStringResolver implements StringResolver {
		private final Map<String, String> txt = new HashMap<String, String>();
		
		public DesktopStringResolver() {
			//txt.put("instruction_0_0"), "[WHITE]PRESS [BLUE]PLAY[WHITE] TO MAKE THE [GREEN]ALIEN[WHITE] GO TO THE [YELLOW]SPACESHIP[WHITE].");
			
			txt.put("tagline", "SAVE ALL THE LALAS!");
			txt.put("sub_tagline_0", "SOUND EFFECTS BY EMMA");
			txt.put("sub_tagline_1", "SOUND EFFECTS BY OSKAR");
			txt.put("sub_tagline_2", "PHYSICS BY BOX2D");
			txt.put("sub_tagline_3", "BUILT USING LIBGDX");
			txt.put("sub_tagline_4", "MUSIC BY SKETCHYLOGIC");
			txt.put("sub_tagline_5", "GRAPHICS BY KENNEY");
			txt.put("sub_tagline_6", "PROGRAMMED BY FREDRIK BORNANDER");
			txt.put("instruction_0_0", "[WHITE]PRESS [BLUE]PLAY[WHITE] TO MAKE THE [GREEN]ALIEN[WHITE] GO TO THE [YELLOW]SPACESHIP[WHITE].");
			txt.put("instruction_0_1", "[WHITE]DRAG THE WOODEN BOX TO THE [BLUE]GAP[WHITE] TO LET THE [GREEN]ALIEN[WHITE] CROSS.");
			txt.put("instruction_0_3", "[WHITE]BLOCKS CAN BE [BLUE]STACKED[WHITE] ON TOP OF EACH OTHER.");
			txt.put("instruction_0_4", "[WHITE]SOMETIMES BLOCKS MUST BE [BLUE]ROTATED[WHITE].");
			txt.put("instruction_0_5", "[WHITE]BLOCKS CAN BE [BLUE]MIRRORED[WHITE].");
			txt.put("block_overlap", "[WHITE]BLOCKS MUST [RED]NOT[WHITE] OVERLAP!");
			txt.put("completed_0", "WELL DONE!");
			txt.put("completed_1", "GOOD JOB!");
			txt.put("completed_2", "AWESOME!");
			txt.put("completed_3", "GREAT!");
			txt.put("completed_4", "SUPERB!");
			txt.put("homeplanet_0", "THIS IS THE LALAS HOME PLANET.\r\nYOU HAVE SAVED ");
			txt.put("homeplanet_1", " LALAS");		
			

			
			
			
		    txt.put("name_0_0", "THE BEGINNING");   
		    txt.put("name_0_1", "EASY DOES IT");   
		    txt.put("name_0_2", "STRAIGHT FORWARD");   
		    txt.put("name_0_3", "THE FIRST STACK IS THE TALLEST");   
		    txt.put("name_0_4", "90 DEGREES IN THE SHADE");   
		    txt.put("name_0_5", "ON THE FLIPSIDE");
		    txt.put("name_0_6", "RAMP IT UP");
		    txt.put("name_0_7", "SUPERFLUOUS");
		    txt.put("name_0_8", "HIGH DESERT");
		    txt.put("name_0_9", "MUDDY");
		    txt.put("name_0_10", "STONE BRIDGE");
		    txt.put("name_0_11", "LONGER STONE BRIDGE");
		    
		    txt.put("name_1_0", "LIKE A JIGSAW PUZZLE");   
		    txt.put("name_1_1", "DOWN THEN UP");   
		    txt.put("name_1_2", "STONE CHASM");   
		    txt.put("name_1_3", "NOT LIKE A JIGSAW PUZZLE");   
		    txt.put("name_1_4", "THREE PILLARS");   
		    txt.put("name_1_5", "GRASSY HILL CLIMB");
		    txt.put("name_1_6", "DIFFERENT WEIGHTS");
		    txt.put("name_1_7", "MUSHROOM HOPPING");
		    txt.put("name_1_8", "ROCKY BUT EASY");
		    txt.put("name_1_9", "EASY PEASY");
		    txt.put("name_1_10", "DIFFERENT WEIGHTS II");
		    txt.put("name_1_11", "UP YOU GO");
		    
		    txt.put("name_2_0", "THIS AGAIN?");   
		    txt.put("name_2_1", "90 DEGREE SUPPORT");   
		    txt.put("name_2_2", "JUTTING OUT");   
		    txt.put("name_2_3", "SYMMETRICAL");   
		    txt.put("name_2_4", "A ROCKY WALK");   
		    txt.put("name_2_5", "FROM MUSHROOM TO SHIP");
		    txt.put("name_2_6", "WATCH THE WATER");
		    txt.put("name_2_7", "A DESERT FLY");
		    txt.put("name_2_8", "TWO BLOCK STEP");
		    txt.put("name_2_9", "A BIG MUSHROOM");
		    txt.put("name_2_10", "MANY WAYS");
		    txt.put("name_2_11", "A BIT WONKY");
		    
		    txt.put("name_3_0", "A STEEP CLIMB");   
		    txt.put("name_3_1", "DESERT WEDGES");   
		    txt.put("name_3_2", "A WALK IN THE SUN");   
		    txt.put("name_3_3", "A CAKE WALK");   
		    txt.put("name_3_4", "STAMPLER\'S LEVEL");   
		    txt.put("name_3_5", "EXTENDING WITH COUNTERWEIGHTS");
		    txt.put("name_3_6", "A BIT WEIRD ");
		    txt.put("name_3_7", "J0\'S LEVEL");
		    txt.put("name_3_8", "WINTER WONDER LAND");
		    txt.put("name_3_9", "QUITE A TREK");
		    txt.put("name_3_10", "TALL MUSHROOM");
		    txt.put("name_3_11", "A SNOWY CLIMB");      
		    
		    txt.put("name_4_0", "JUMP IN");   
		    txt.put("name_4_1", "LOTS OF BLOCKS");   
		    txt.put("name_4_2", "EXTENDING FAR");   
		    txt.put("name_4_3", "NO WAY");   
		    txt.put("name_4_4", "FIRST DOWN, THEN UP");   
		    txt.put("name_4_5", "BLOCK OVERLOAD");
		    txt.put("name_4_6", "FOUR OF EACH ");
		    txt.put("name_4_7", "EDVIN\'S EXTREME TRACK");
		    txt.put("name_4_8", "FLIES EVERYWHERE");
		    txt.put("name_4_9", "IMPOSSIBLE");
		    txt.put("name_4_10", "DON\'T FALL IN");
		    txt.put("name_4_11", "CAN\'T BE DONE");	
		
		}

		@Override
		public String resolveString(String id) {
			if (txt.containsKey(id)) {
				return txt.get(id);
			}
			else {
				return id;
			}
		}
		
	}
	
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		float f = 0.50f;
		
		float w = Lwjgl3ApplicationConfiguration.getDisplayMode().width;
		float h = Lwjgl3ApplicationConfiguration.getDisplayMode().height;
		config.setWindowIcon(FileType.Internal, "graphics/app_icon.png");
		config.setTitle("Lala");
		config.setWindowedMode((int)(w * f), (int)(h * f));
		config.setResizable(false);
		config.setForegroundFPS(60);
		config.useVsync(false);
		LalaGame.TextResolver = new DesktopStringResolver();

		new Lwjgl3Application(new LalaGame(), config);
	}
}
