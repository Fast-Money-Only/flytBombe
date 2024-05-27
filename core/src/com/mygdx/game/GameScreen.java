package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

public class GameScreen implements Screen {

    final FlytBombe game;

    long diedTime;

    boolean hasDied = false;
    Texture stenImg;
    Texture bombeImg;
    Texture exploImg;
    Texture backgroundImg, startBGImg, midtBGImg, slutBGImg;
    Array<Sten> sten;
    Array<Background> baggrunde, fBaggrunde, lBaggrunde;

    Bombe bombe;

    int moveSpeed = 300;

    Background background, firstBackground, lastBackground;
    OrthographicCamera camera;

    int nextDropTime = 18000000;

    long lastDropTime, twoDodgedSpawnTime;

    int[] positions = {290, 896, 1515};
    int currentPositionIndex = 1;

    int dodgedStones = 0;
    String vindertekst = " ";



    public GameScreen(FlytBombe game) {
        this.game = game;

        stenImg = new Texture(Gdx.files.internal("rock.png"));
        bombeImg = new Texture(Gdx.files.internal("cart.png"));
        backgroundImg = new Texture(Gdx.files.internal("FlytBg.png"));
        startBGImg = new Texture(Gdx.files.internal("flytBGstart.png"));
        midtBGImg = new Texture(Gdx.files.internal("FlytBg.png"));
        slutBGImg = new Texture(Gdx.files.internal("flytBGgoal.png"));
        exploImg = new Texture(Gdx.files.internal("explosion.png"));

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1792, 1344);

        firstBackground = new Background(startBGImg,0, 0);
        background = new Background(backgroundImg,0,camera.viewportHeight);
        lastBackground = new Background(slutBGImg,0,camera.viewportHeight);

        background.width = (int) camera.viewportWidth;
        background.height = (int) camera.viewportHeight;

        bombe = new Bombe(bombeImg, positions[1]- (float) 72/2, 5 );
        bombe.width = 199;
        bombe.height = 256;


        baggrunde = new Array<>();
        baggrunde.add(background);

        fBaggrunde = new Array<>();
        fBaggrunde.add(firstBackground);

        lBaggrunde = new Array<>();
        lBaggrunde.add(lastBackground);

        addBackground();

        sten = new Array<>();
        spawnRock();
    }

    public void addBackground(){
        Background baggrund = new Background(backgroundImg,0, camera.viewportHeight);
        baggrunde.add(baggrund);

    }
    public void spawnRock(){
        Sten rock = new Sten(stenImg, positions[MathUtils.random(0,2)] - (float) 128 /2, camera.viewportHeight);
        sten.add(rock);
        lastDropTime = TimeUtils.nanoTime();
    }

    public void checkKeyspressed(){
        // Check for input to change bucket position
        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            // Move bucket left
            currentPositionIndex = (currentPositionIndex - 1 + positions.length) % positions.length;
            bombe.setX(positions[currentPositionIndex] - (float) bombe.width /2);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            // Move bucket right
            currentPositionIndex = (currentPositionIndex + 1) % positions.length;
            bombe.setX(positions[currentPositionIndex] - (float) bombe.width /2);
        }
    }


    public void moveRock(){
        Iterator<Sten> iter = sten.iterator();
        while (iter.hasNext()) {
            Sten rock = iter.next();
            rock.setY((float) (rock.getY() - moveSpeed * Gdx.graphics.getDeltaTime()));

            if (dodgedStones == 2 && rock.getY() + rock.height > ((double) backgroundImg.getHeight() / 2)
                    && rock.getY() < ((double) backgroundImg.getHeight() / 2)){
                if (TimeUtils.nanoTime() - twoDodgedSpawnTime > nextDropTime){
                    spawnRock();
                }
            }
            if (rock.getY() + rock.height < 0){
                iter.remove();
                spawnRock();
                dodgedStones++;
            }
            if (rock.overlaps(bombe)) {
                iter.remove();
                explosion();

            }
        }

        if (dodgedStones == 2){
            twoDodgedSpawnTime = TimeUtils.nanoTime();
        }

        if (dodgedStones == 5){
            moveSpeed = 400;
        }
        if (dodgedStones == 10){
            moveSpeed = 500;
        }
        if (dodgedStones == 15){
            moveSpeed = 600;
        }

        if (dodgedStones == 20){
            moveSpeed = 700;
        }
        if (dodgedStones == 50){
            moveSpeed = 200;
            vindertekst = "Du har ført bomben sikkert til basen!";
        }
    }

    public void moveBackground(){
        Iterator<Background> iter = baggrunde.iterator();

        while (iter.hasNext()) {

            Background baggrund = iter.next();
            baggrund.setY((float) (baggrund.getY() - moveSpeed * Gdx.graphics.getDeltaTime()));

            if (baggrund.getY() + baggrund.height < 0){
                if (dodgedStones == 50){
                    moveSpeed = 0;
                    backgroundImg = slutBGImg;
                }
                else {
                    iter.remove();
                    addBackground();
                }

            }
        }
    }

    public void moveFirstBackground(){
        Iterator<Background> iter = fBaggrunde.iterator();

        while (iter.hasNext()) {

            Background fBaggrund = iter.next();
            fBaggrund.setY((float) (fBaggrund.getY() - moveSpeed * Gdx.graphics.getDeltaTime()));

            if (fBaggrund.getY() + fBaggrund.height < 0){
                iter.remove();
                addBackground();
            }
        }

    }

    public void explosion(){
        //Animation af eksplosion efterfulgt af gameover
        bombeImg = exploImg;
        bombe.width = 200;
        bombe.height = 200;
        bombe.setX(positions[currentPositionIndex] - (float) bombe.width /2);

        moveSpeed = 10;
        hasDied = true;
        diedTime = TimeUtils.nanoTime(); //change
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float v) {
        ScreenUtils.clear(0, 0, 0.2f, 1);

        // tell the camera to update its matrices.
        camera.update();

        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        game.batch.setProjectionMatrix(camera.combined);



        game.batch.begin();
        game.batch.draw(startBGImg, (float) firstBackground.getX(), (float) firstBackground.getY());
        for (Background baggrund : baggrunde){
            game.batch.draw(backgroundImg, (float) baggrund.getX(), (float) baggrund.getY());
        }


        for (Sten rock : sten) {
            game.batch.draw(stenImg, (float) rock.getX(), (float) rock.getY());
        }

        game.batch.draw(bombeImg, (float) bombe.getX(), (float) bombe.getY(), bombe.width, bombe.height);

        game.font.getData().setScale(3, 3);
        game.font.draw(game.batch, "Sten undviget: " + dodgedStones, 50, 50);
        game.font.draw(game.batch, vindertekst , 290, 200);

        game.batch.end();

        moveFirstBackground();
        moveBackground();
        checkKeyspressed();
        moveRock();

        if (TimeUtils.nanoTime() - diedTime > 500000000 && hasDied){
            game.setScreen(new GameoverScreen(game));
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
        bombeImg.dispose();
        exploImg.dispose();
        backgroundImg.dispose();
        stenImg.dispose();
    }
}
