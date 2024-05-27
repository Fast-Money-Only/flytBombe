package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;

public class MainMenuScreen implements Screen {

    final FlytBombe game;
    Texture backgroundImg;

    OrthographicCamera camera;

    public MainMenuScreen(final FlytBombe game) {
        this.game = game;

        backgroundImg = new Texture(Gdx.files.internal("welcomeBG.jpg"));

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
        game.batch.draw(backgroundImg, 0, 0);
        game.font.getData().setScale(3, 3);
        game.font.draw(game.batch, "Du skal nu flytte bomben sikkert fra ammunitionsbunkeren til basen ", 100, 150);
        game.font.draw(game.batch, "Klik på skærmen for at starte", 100, 100);
        game.batch.end();

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {

            try {
                game.setScreen(new GameScreen(game));
                dispose();
            } catch (Exception e){
                System.out.println(e.getMessage());
            }

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
