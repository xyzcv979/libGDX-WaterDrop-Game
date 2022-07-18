package com.mygdx.waterdrop;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import org.w3c.dom.css.Rect;

import java.awt.*;
import java.util.ArrayList;

public class WaterDrop extends ApplicationAdapter {
	private Texture waterDrop;
	private Texture bucketImage;
	private Sound dropSound;
	private Music rainMusic;
	private OrthographicCamera camera;
	private SpriteBatch batch; // used to draw 2D images
	private Rectangle bucket;
	private Vector3 touchPos; // for mouse click position
	private Array<Rectangle> rainDrops; // libGDX class used to minimize garbage
	private long lastDropTime; // tracks last time rain drop was spawned. Stored in nanoseconds

	private void spawnRainDrop() {
		Rectangle rainDrop = new Rectangle();
		rainDrop.x = MathUtils.random(0, 800-64);
		rainDrop.y = 480;
		rainDrop.width = 64;
		rainDrop.height = 64;
		rainDrops.add(rainDrop);
		lastDropTime = TimeUtils.nanoTime();
	}

	@Override
	public void create () {
		waterDrop = new Texture("rainDrop.png");
		bucketImage = new Texture("bucket.png");

		// Sound for <10 sec clips. Also stored in memory
		dropSound = Gdx.audio.newSound(Gdx.files.internal("water_drop_sound.wav"));
		// Music for >10 secs. Too big to be stored in memory completely
		rainMusic = Gdx.audio.newMusic(Gdx.files.internal("falling_rain.mp3"));
		rainMusic.setLooping(true);
		rainMusic.play();

		batch = new SpriteBatch();

		camera = new OrthographicCamera();
		//Camera always shows window of this size
		camera.setToOrtho(false, 800, 480);

		bucket = new Rectangle();
		bucket.x = 800 / 2 - 64 / 2; // center horizontally
		bucket.y = 20; // set 20 up
		bucket.width = 64;
		bucket.height = 64;

		touchPos = new Vector3();

		rainDrops = new Array<Rectangle>();
		spawnRainDrop();

	}

	@Override
	public void render () {
		// red blue green alpha within range [0,1]
		ScreenUtils.clear(0, 0, 0.2f, 1);

		camera.update();

		// use coordinates set by camera
		batch.setProjectionMatrix(camera.combined);
		// SpiritBatch records all drawing commands
		// between begin() and end(). Speeds up rendering
		batch.begin();
		batch.draw(bucketImage, bucket.x, bucket.y);
		for(Rectangle rainDrop : rainDrops) {
			batch.draw(waterDrop, rainDrop.x, rainDrop.y);
		}
		batch.end();

		//Moving bucket with mouse
		if(Gdx.input.isTouched()) {
			// Need to transform mouse coord to camera coord
			// because might be different

			// Setting to current touch/mouse pos
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			// Transform these coord to camera's coord system
			camera.unproject(touchPos);
			// Changing bucket to be center around mouse
			bucket.x = (int) (touchPos.x - 64 / 2);
		}

		// Moving bucket with keyboard

		// Move w/o acceleration at 200 pixels per sec
		// Time based movement, need to know time passed
		// between last and current rendering frame
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT))
			bucket.x -= 200 * Gdx.graphics.getDeltaTime();
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT))
			bucket.x += 200 * Gdx.graphics.getDeltaTime();

		// Making sure bucket stays within screen limits
		if(bucket.x < 0) bucket.x = 0;
		if(bucket.x > Gdx.graphics.getWidth() - 64) bucket.x = Gdx.graphics.getWidth() - 64;

		// Spawning raindrops
		if(TimeUtils.nanoTime() - lastDropTime > 1000000000) spawnRainDrop();

		// Making Raindrops move
		// If beneath screen, remove from array
		for(int i = rainDrops.size - 1; i >= 0; i--) {
			Rectangle rainDrop = rainDrops.get(i);
			rainDrop.y -= 200 * Gdx.graphics.getDeltaTime();
			if(rainDrop.y + 64 < 0) rainDrops.removeIndex(i);

			// Raindrop hitting bucket
			if(rainDrop.intersects(bucket)) {
				dropSound.play();
				rainDrops.removeIndex(i);
			}
		}

	}

	// Manually disposing, helping OS
	@Override
	public void dispose () {
		waterDrop.dispose();
		bucketImage.dispose();
		dropSound.dispose();
		waterDrop.dispose();
		batch.dispose();

	}
}
