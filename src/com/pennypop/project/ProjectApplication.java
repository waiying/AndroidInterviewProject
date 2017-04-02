package com.pennypop.project;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * The {@link ApplicationListener} for this project, create(), resize() and
 * render() are the only methods that are relevant
 * 
 * @author Richard Taylor
 * */
public class ProjectApplication implements ApplicationListener {

	private static Screen screen;
	private static SpriteBatch sb;

	public static void main(String[] args) {
		new LwjglApplication(new ProjectApplication(), "PennyPop", 1280, 720,
				true);
	}

	@Override
	public void create() {
		sb = new SpriteBatch();
		screen = new MainScreen(sb);
		screen.show();
	}

	@Override
	public void dispose() {
		screen.hide();
		screen.dispose();
		sb.dispose();
	}

	@Override
	public void pause() {
		screen.pause();
	}

	@Override
	public void render() {
		clearWhite();
		screen.render(Gdx.graphics.getDeltaTime());
	}
	
	/** Switches screen to newScreen**/
	public static void updateScreen(Screen newScreen) {
		screen = newScreen;
	}

	/** Clears the screen with a white color */
	private void clearWhite() {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}

	@Override
	public void resize(int width, int height) {
		screen.resize(width, height);
	}

	@Override
	public void resume() {
		screen.resume();
	}
}
