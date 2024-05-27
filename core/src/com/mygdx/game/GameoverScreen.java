package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;

import java.awt.*;

public class GameoverScreen implements Screen {

    final FlytBombe game;

    Texture backgroundImg;

    OrthographicCamera camera;

    public GameoverScreen(final FlytBombe game) {
        this.game = game;

        backgroundImg = new Texture(Gdx.files.internal("gameoverBG.jpg"));

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1792, 1344);
    }
    @Override
    public void show() {

    }

    @Override
    public void render(float v) {
        ScreenUtils.clear(0, 0, 0.2f, 1);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.font.getData().setScale(3, 3);
        game.batch.draw(backgroundImg, 0, 0);
        game.font.draw(game.batch, "GAMEOVER", camera.viewportWidth /2 - 100, camera.viewportHeight /2 + 40);
        game.font.draw(game.batch, "Du ramte en sten, og bomben sprang", camera.viewportWidth / 2 - 320, camera.viewportHeight /2 - 40);
        game.font.draw(game.batch, "Tryk for at gå tilbage til menuen!", camera.viewportWidth / 2, 200);
        game.batch.end();

        if (Gdx.input.isTouched()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            game.setScreen(new MainMenuScreen(game));
            dispose();
        }
    }

    @Override
    public void resize(int i, int i1) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
