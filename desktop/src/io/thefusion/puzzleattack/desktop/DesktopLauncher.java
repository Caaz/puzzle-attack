package io.thefusion.puzzleattack.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import io.thefusion.puzzleattack.pAttack;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Puzzle Attack!!";
		new LwjglApplication(new pAttack(), config);
	}
}
