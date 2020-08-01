package com.rusloker.pong.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.rusloker.pong.GameRepository;
import com.rusloker.pong.GdxVisualiser;
import com.rusloker.pong.ai.PongBot;
import com.rusloker.pong.ai.TestBot;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new GdxVisualiser(), config);
		GameRepository.createGame();
		GameRepository.startGame();
		new PongBot().start();
		new TestBot().start();
	}
}
