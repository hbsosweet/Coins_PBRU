package hblifang.coin.thai;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

public class MyGdxGame extends ApplicationAdapter {
	//Explicit
	private SpriteBatch batch;
	private Texture wallpaperTexture, cloudTexture, pigTexture, coinsTexture, rainTexture;
	private OrthographicCamera objOrthographicCamera;
	private BitmapFont nameBitmapFont, scoreBitmapFont, showScoreBitmapFont;
	int xCloudAnInt, yCloudAnInt = 600;
	private boolean cloudABoolean = true, finishABoolean= false;
	private Rectangle picRectangle, coinsRectangle, rainRectangle;
	private Vector3 objVector3;
	private Sound pigSound;
	private Array<Rectangle> coinsArray, rainArray;
	private long lastDropCoins, lastDropRain;
	private Iterator<Rectangle> coinsIterator, rainIterator; // ===>Java.util
	private Sound waterDropSound, coinDropSound;
	private int scoreAnInt, falseAnInt = 0, finalScoreAnInt;
	private Music rainMusic, backgroundMusic;


	@Override
	public void create () {
		batch = new SpriteBatch();

		//คือการกำหนดขนาดของจอที่ต้องการ
		objOrthographicCamera = new OrthographicCamera();
		objOrthographicCamera.setToOrtho(false, 1200, 800);

		//Setup Wallpaper
		wallpaperTexture = new Texture("wallpapers_b_05.png");

		//Setup BitmapFont
		nameBitmapFont = new BitmapFont();
		nameBitmapFont.setColor(Color.RED); //ใส่สี
		nameBitmapFont.setScale(4); //scale font

		//Setup Cloud
		cloudTexture = new Texture("cloud.png");

		//Setup Pig
		pigTexture = new Texture("lk.png");

		//Setup Rectangle pig
		picRectangle = new Rectangle();
		picRectangle.x = 560;
		picRectangle.y = 100;
		picRectangle.width = 80;
		picRectangle.height = 56;

		//Setup Pig Sound
		pigSound = Gdx.audio.newSound(Gdx.files.internal("mariojump.wav"));

		//Setup Coins
		coinsTexture = new Texture("coins.png");

		//Create coinsArray
		coinsArray = new Array<Rectangle>();
		coinsRandomDrop();

		//Setup WaterDrop
		waterDropSound = Gdx.audio.newSound(Gdx.files.internal("water_drop.wav"));

		//Setup CoinDrop
		coinDropSound = Gdx.audio.newSound(Gdx.files.internal("coins_drop.wav"));

		//Setup scoreBitMapFont
		scoreBitmapFont = new BitmapFont();
		scoreBitmapFont.setColor(Color.YELLOW);
		scoreBitmapFont.setScale(4);

		//Setup rainTexture
		rainTexture = new Texture("droplet.png");

		//Create rainArray
		rainArray = new Array<Rectangle>();
		rainRandomDrop();

		//Setup rainMusic
		rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));

		//Setup BackgroundMusic
		backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("numalee.mp3"));

		//Setup ShowScore
		showScoreBitmapFont = new BitmapFont();
		showScoreBitmapFont.setColor(230, 28, 223, 255);
		showScoreBitmapFont.setScale(5);

	} //create เอาไว้กำหนดค่า

	private void rainRandomDrop() {

		rainRectangle = new Rectangle();
		rainRectangle.x = MathUtils.random(0, 1136);
		rainRectangle.y = 800;
		rainRectangle.width = 64;
		rainRectangle.height = 64;

		rainArray.add(rainRectangle);
		lastDropRain = TimeUtils.nanoTime();

	}

	private void coinsRandomDrop() {

		coinsRectangle = new Rectangle();
		coinsRectangle.x = MathUtils.random(0, 1136); // เอา pixel เหรียญลบกับขนาดจอ 1200
		coinsRectangle.y = 800; // เป็น 800 เพราะเริ่มที่ตำแหน่งความสูงของจอ
		coinsRectangle.width = 64; //ความกว้างของภาพเหรียญ
		coinsRectangle.height = 64; //ความสูงของภาพเหรียญ
		coinsArray.add(coinsRectangle);
		lastDropCoins = TimeUtils.nanoTime();

	} //coinRandomDrop

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		//Setup Screen
		objOrthographicCamera.update();
		batch.setProjectionMatrix(objOrthographicCamera.combined);

		//เอาไว้วาด Object
		batch.begin();

		//Draw Wallpaper
		batch.draw(wallpaperTexture, 0, 0);

		//Drawable Cloud
		batch.draw(cloudTexture, xCloudAnInt, yCloudAnInt);

		//Drawable BitmapFont
		nameBitmapFont.draw(batch, "Little K Coins Collector", 50, 750);

		//Drawable Pig วาดรูปตัวเก็บเหรียญ (หมูหมี)
		batch.draw(pigTexture, picRectangle.x, picRectangle.y);

		//Drawable Coins วาดรูปเหรียญ
		for (Rectangle forCoins : coinsArray) {
			batch.draw(coinsTexture, forCoins.x, forCoins.y);
		}

		//Drawable Score วาดโชว์คะแนน
		scoreBitmapFont.draw(batch, "Score = " + Integer.toString(scoreAnInt), 900, 750);

		//Drawable Rain วาดฝน
		for (Rectangle forRain : rainArray) {
			batch.draw(rainTexture, forRain.x, forRain.y);
		}

		if (finishABoolean) {

			batch.draw(wallpaperTexture, 0, 0);
			showScoreBitmapFont.draw(batch, "Your Score ==> " + Integer.toString(finalScoreAnInt), 500, 750);

		} //if

		batch.end();

		//Move Cloud
		moveCloud();
		
		//Active When Touch Screen
		activeTouchScreen();

		//Random Drop Coins ทำให้เหรียญตกลงมา
		randomDropCoins();
		
		//Random Drop Rain
		randomDropRain();

		//Play Music
		rainMusic.play();

		//Play backgroundMusic
		backgroundMusic.play();


	} //render ตัวนี้คือ loop

	private void randomDropRain() {
		if (TimeUtils.nanoTime() - lastDropRain > 1E9) {
			rainRandomDrop();
		} //if

		rainIterator = rainArray.iterator();
		while (rainIterator.hasNext()) {

			Rectangle myRainRectangle = rainIterator.next();
			myRainRectangle.y -= 50 * Gdx.graphics.getDeltaTime();

			//When Rain drop into Floor
			if (myRainRectangle.y + 64 < 0) {
				waterDropSound.play();
				rainIterator.remove();
			} //if

			//When Rain Overlap Pig
			if (myRainRectangle.overlaps(picRectangle)) {
				scoreAnInt -= 1;
				waterDropSound.play();
				rainIterator.remove();
			} //if

		} //while

	}

	private void randomDropCoins() {

		if (TimeUtils.nanoTime() - lastDropCoins > 1E9) //1E9 คือ 10^9 หมายถึงให้ทำการดรอปเหรียญ
		{
			coinsRandomDrop();
		}

		coinsIterator = coinsArray.iterator();
		while (coinsIterator.hasNext()) //hasNext หมายถึง มีค่าต่อไปเรื่อยๆ
		{
			Rectangle myCoinsRectangle = coinsIterator.next();
			myCoinsRectangle.y -= 50 * Gdx.graphics.getDeltaTime(); //เป็น 50 เพราะตัวละครที่รับเหรียญต้องวิ่งไวกว่าเหรียญ

			//When Coin into Floor เมื่อเหรียญถึงพื้นจะให้ทำการล้างหน่วยความจำ
			if (myCoinsRectangle.y + 64 < 0) //เหรียญอยู่ตรงขอบจอด้านล่าง
			{
				falseAnInt += 1;
				waterDropSound.play();
				coinsIterator.remove(); //ให้ทำการลบค่าออกไป
				checkFalse();
			} //if

			//When Coins OverLap Pig
			if (myCoinsRectangle.overlaps(picRectangle)) {
				scoreAnInt += 1;
				coinDropSound.play();
				coinsIterator.remove();
			} //if

		} //while Loop

	}		// นี่คือ randomDropCoins

	private void checkFalse() {
		if (falseAnInt > 20) {
			dispose();

			if (!finishABoolean) {
				finalScoreAnInt = scoreAnInt;
			}

			finishABoolean = true;

		} //if
	} //checkFalse

	@Override
	public void dispose() {
		super.dispose();

		backgroundMusic.dispose();
		rainMusic.dispose();
		pigSound.dispose();
		waterDropSound.dispose();
		coinDropSound.dispose();

	} //dispose

	private void activeTouchScreen() {
		if (Gdx.input.isTouched()) {

			//Sound Effect Pig
			pigSound.play();

			objVector3 = new Vector3(); //ตัวทำหน้าที่ในการเก็บค่าเมื่อนิ้วไปโดน
			objVector3.set(Gdx.input.getX(), Gdx.input.getY(), 0);

			if (objVector3.x < Gdx.graphics.getWidth()/2) {
				if (picRectangle.x < 0) {
					picRectangle.x = 0;
				} else {
					picRectangle.x -= 10;
				}
			} else {
				if (picRectangle.x > 1100) {
					picRectangle.x = 1100;
				} else {
					picRectangle.x += 10;
				}
			}
		} //if
	}

	private void moveCloud() {
		if (cloudABoolean) {
			if (xCloudAnInt < 937) {
				xCloudAnInt += 100 * Gdx.graphics.getDeltaTime();
			} else {
				cloudABoolean = !cloudABoolean;
			}
		} else {
			if (xCloudAnInt > 0) {
				xCloudAnInt -= 100 * Gdx.graphics.getDeltaTime();
			} else {
				cloudABoolean = !cloudABoolean;
			}
		}
	}
} //Main Class
