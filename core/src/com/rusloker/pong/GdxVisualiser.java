package com.rusloker.pong;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.rusloker.pong.engine.GameProcessor;

import java.util.Collection;

public class GdxVisualiser extends ApplicationAdapter {
	SpriteBatch batch;


	@Override
	public void create () {
		batch = new SpriteBatch();
		GameRepository.initialize();
		Gdx.graphics.setVSync(true);
		GameProcessor.createGame();
	}

	@Override
	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Collection<Sprite> drawables = GameRepository.getDrawables();
		batch.begin();
		for (Sprite i : drawables) {
			i.draw(batch);
		}
		batch.end();
	}

	@Override
	public void dispose () {
		batch.dispose();
	}
}