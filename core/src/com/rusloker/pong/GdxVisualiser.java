package com.rusloker.pong;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.rusloker.pong.engine.GameProcessor;

import java.util.Collection;

public class GdxVisualiser extends ApplicationAdapter {
	SpriteBatch batch;
	ShapeRenderer shapeRenderer;


	@Override
	public void create () {
		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		GameRepository.initialize();
		Gdx.graphics.setVSync(true);
		GameProcessor.createGame();
	}

	@Override
	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Collection<Sprite> drawables = GameRepository.getDrawables();
		final float height = Gdx.graphics.getHeight();
		final float width = Gdx.graphics.getWidth();
		batch.begin();
		for (Sprite i : drawables) {
			i.draw(batch);
		}
		batch.end();
		drawDottedLine(shapeRenderer, 0, width, height/2);
	}

	@Override
	public void dispose () {
		batch.dispose();
		shapeRenderer.dispose();
	}

	private void drawDottedLine(ShapeRenderer shapeRenderer, float x, float x1, float y) {
		final float dotLength = 30;
		final float step = 30;
		final float dotHeight = 10;

		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		shapeRenderer.setColor(1, 1, 1, 1);
		for (float i = x + step / 2; i < x1; i += step + dotLength) {
			shapeRenderer.rect(i, y, dotLength, dotHeight);
		}
		shapeRenderer.end();
	}
}