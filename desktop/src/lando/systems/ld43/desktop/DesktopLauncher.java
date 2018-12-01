package lando.systems.ld43.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import lando.systems.ld43.LudumDare43;
import lando.systems.ld43.utils.Config;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = Config.title;
		config.width = Config.window_width;
		config.height = Config.window_height;
		config.resizable = Config.resizable;
		config.samples = 4;
		new LwjglApplication(new LudumDare43(), config);
	}
}
