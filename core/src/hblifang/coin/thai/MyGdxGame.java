package hblifang.coin.thai;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
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
	private Texture wallpaperTexture, cloudTexture, pigTexture, coinsTexture;
	private OrthographicCamera objOrthographicCamera;
	private BitmapFont nameBitmapFont;
	int xCloudAnInt, yCloudAnInt = 600;
	private boolean cloudABoolean = true;
	private Rectangle picRectangle, coinsRectangle;
	private Vector3 objVector3;
	private Sound pigSound;
	private Array<Rectangle> coinsArray;
	private long lastDropCoins;
	private Iterator<Rectangle> coinsIterator; // ===>Java.util


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
		nameBitmapFont.setColor(Color.YELLOW); //ใส่สี
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
		pigSound = Gdx.audio.newSound(Gdx.files.internal("dog.wav"));

		//Setup Coins
		coinsTexture = new Texture("coins.png");

		//Create coinsArray
		coinsArray = new Array<Rectangle>();
		coinsRandomDrop();


	} //create เอาไว้กำหนดค่า

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

		//Drawable Pig
		batch.draw(pigTexture, picRectangle.x, picRectangle.y);

		batch.end();

		//Move Cloud
		moveCloud();
		
		//Active When Touch Screen
		activeTouchScreen();


	} //render ตัวนี้คือ loop

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
