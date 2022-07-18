package com.mygdx.waterdrop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;

public class MainMenuScreen implements Screen {
    final WaterDropGame game;

    OrthographicCamera camera;

    public MainMenuScreen(final WaterDropGame game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.font.draw(game.batch, "Welcome to my WaterDrop Game!", 200, 250);
        game.font.draw(game.batch, "Drag the bucket with mouse or use left/right arrow keys", 200, 200);
        game.font.draw(game.batch, "Tap anywhere to begin!", 200, 150);

        game.batch.end();

        if(Gdx.input.isTouched()) {
            game.setScreen(new WaterDrop(game));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
    }
}
