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
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class MyGdxGame extends ApplicationAdapter {
	//Explicit
	private SpriteBatch batch;
	private Texture wallpaperTexture, cloudTexture, pigTexture;
	private OrthographicCamera objOrthographicCamera;
	private BitmapFont nameBitmapFont;
	int xCloudAnInt, yCloudAnInt = 600;
	private boolean cloudABoolean = true;
	private Rectangle picRectangle;
	private Vector3 objVector3;
	private Sound pigSound;

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

	} //create เอาไว้กำหนดค่า

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
			if (objVector3.x < 600) {
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
