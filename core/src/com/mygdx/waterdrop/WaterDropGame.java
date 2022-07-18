package com.mygdx.waterdrop;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class WaterDropGame extends Game {

    public SpriteBatch batch; // used to render objects on screen
    public BitmapFont font; // renders text on screen
    WaterDrop waterDropScreen;

    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont(); // default = Arial font
        this.setScreen(new MainMenuScreen(this));

    }

    public void setGameScreen(WaterDrop waterDropScreen) {
        this.waterDropScreen = waterDropScreen;
    }

    public void render() {
        super.render(); // important! w/o this, create() will not render if you override
    }

    public void dispose() {
        batch.dispose();
        font.dispose();
        waterDropScreen.dispose();

    }
}
