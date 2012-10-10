package com.cyagamesocial;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.ButtonSprite.OnClickListener;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.SimpleBaseGameActivity;

import com.cyagamesocial.activity.MainGameActivity;
import com.cyagamesocial.activity.TMXIsometricExampleActivity;
import com.cyagamesocial.unity.MyCamera;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Editable;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TableLayout;
import android.widget.Toast;

public class LoginActivity extends SimpleBaseGameActivity {
	private Camera mCamera;

	// Khai bao cac resource
	private BitmapTextureAtlas mBitmapTextureAtlas;
	private ITextureRegion mTR_bg;
	private ITextureRegion mTR_LoginForm;
	private ITextureRegion mTR_Button_LetGo;
	private ITextureRegion mTR_Button_Homepage;
	private ITextureRegion mTR_Button_Register;
	private ITextureRegion mTR_Button_Quit;

	private TiledTextureRegion mInputBoxTexture;

	private TiledTextureRegion mTR_CheckboxButton;

	// Khai bao cac sprite
	private Scene mScene;
	private Sprite mSprite_bg;
	private Sprite mSprite_LoginForm;
	private ButtonSprite mSprite_Button_LetGo;
	private ButtonSprite mSprite_Button_Register;
	private ButtonSprite mSprite_Button_Homepage;
	private ButtonSprite mSprite_Button_Quit;
	private TiledSprite mSprite_Tile_CheckBox;

	private ITextureRegion mTR_InputText;
	// private TiledTextureRegion mInputBoxTexture;

	private TableLayout tableLayout;

	// Font
	private Font mFont;

	// Edittext
	private EditText username;
	private EditText password;
	
	
	//Remember
	private Boolean isRemembe=true;

	@Override
	public EngineOptions onCreateEngineOptions() {
		mCamera = new Camera(0, 0, MyCamera.CAMERA_WIDTH, MyCamera.CAMERA_HEIGHT);
		EngineOptions engineOptions = new EngineOptions(true,
				ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(
						MyCamera.CAMERA_WIDTH, MyCamera.CAMERA_HEIGHT), mCamera);
		return engineOptions;

	}

	@Override
	protected void onCreateResources() {
		FontFactory.setAssetBasePath("font/");
		this.mFont = FontFactory.createFromAsset(this.getFontManager(),
				this.getTextureManager(), 512, 512, TextureOptions.BILINEAR,
				this.getAssets(), "Vnidisneynornal.ttf", 32, true,
				Color.rgb(30, 159, 255));
		this.mFont.load();

		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(),
				1024, 1024, TextureOptions.DEFAULT);
		mTR_bg = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				mBitmapTextureAtlas, this, "bg_login.png", 0, 0);

		// mTR_LoginForm=BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBitmapTextureAtlas,
		// this, "login_form.png",0,(int)this.mTR_bg.getHeight()+1);
		mTR_Button_LetGo = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mBitmapTextureAtlas, this,
						"login_form_button_letgo.png", 0,
						(int) (this.mTR_bg.getHeight() + 1));
		mTR_Button_Register = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(
						this.mBitmapTextureAtlas,
						this,
						"login_form_button_register.png",
						0,
						(int) (this.mTR_bg.getHeight()
								+ this.mTR_Button_LetGo.getHeight() + 1));
		mTR_Button_Homepage = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(
						this.mBitmapTextureAtlas,
						this,
						"login_form_button_homepage.png",
						0,
						(int) (this.mTR_bg.getHeight()
								+ this.mTR_Button_LetGo.getHeight()
								+ this.mTR_Button_Register.getHeight() + 1));
		mTR_Button_Quit = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(
						this.mBitmapTextureAtlas,
						this,
						"login_form_button_quit.png",
						0,
						(int) (this.mTR_bg.getHeight()
								+ this.mTR_Button_LetGo.getHeight()
								+ this.mTR_Button_Register.getHeight()
								+ this.mTR_Button_Homepage.getHeight() + 1));

		mTR_CheckboxButton = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(
						this.mBitmapTextureAtlas,
						this,
						"login_form_checkbox.png",
						0,
						(int) (this.mTR_bg.getHeight()
								+ this.mTR_Button_LetGo.getHeight()
								+ this.mTR_Button_Register.getHeight()
								+ this.mTR_Button_Homepage.getHeight()
								+ this.mTR_Button_Quit.getHeight() + 1), 2, 1);

		mBitmapTextureAtlas.load();
	}

	@Override
	protected Scene onCreateScene() {
		// TODO Auto-generated method stub
		this.mEngine.registerUpdateHandler(new FPSLogger());
		mScene = new Scene();
		VertexBufferObjectManager vertexBufferObjectManager = this
				.getVertexBufferObjectManager();
		// Backgorund
		mSprite_bg = new Sprite(0, 0, this.mTR_bg, vertexBufferObjectManager);
		mScene.attachChild(mSprite_bg);

		mSprite_Button_LetGo = new ButtonSprite(321, 309,
				this.mTR_Button_LetGo, vertexBufferObjectManager);
		mScene.attachChild(mSprite_Button_LetGo);

		mSprite_Button_LetGo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				// TODO Auto-generated method stub

				Editable edusername=username.getText();
				Editable edpassword=password.getText();
				
				final String name=edusername.toString();
				final String pass=edpassword.toString();
				LoginActivity.this.runOnUiThread(new Runnable() {
					
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
//						showToast("Let's Go");
//						showToast("Name:"+name+" Pass:"+pass);
						Intent intent=new Intent(getApplicationContext(), TMXIsometricExampleActivity.class);
						startActivity(intent);
					}
				});

			}
		});
		mScene.registerTouchArea(mSprite_Button_LetGo);

		// //Button Register
		mSprite_Button_Register = new ButtonSprite(244, 386,
				this.mTR_Button_Register, vertexBufferObjectManager);
		mScene.attachChild(mSprite_Button_Register);

		mSprite_Button_Register.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				// TODO Auto-generated method stub
				LoginActivity.this.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						showToast("Register");
					}
				});
			}
		});
		mScene.registerTouchArea(mSprite_Button_Register);

		// //Button Homepage
		mSprite_Button_Homepage = new ButtonSprite(352, 386,
				this.mTR_Button_Homepage, vertexBufferObjectManager);
		mScene.attachChild(mSprite_Button_Homepage);

		mSprite_Button_Homepage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				// TODO Auto-generated method stub
				LoginActivity.this.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						showToast("Homepage");
					}
				});

			}
		});
		mScene.registerTouchArea(mSprite_Button_Homepage);

		// //Button Quit
		mSprite_Button_Quit = new ButtonSprite(463, 386, this.mTR_Button_Quit,
				vertexBufferObjectManager);
		mScene.attachChild(mSprite_Button_Quit);

		mSprite_Button_Quit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				// TODO Auto-generated method stub
				LoginActivity.this.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						showToast("Quit");
						LoginActivity.this.finish();
					}
				});
			}
		});
		
		mScene.registerTouchArea(mSprite_Button_Quit);
		
		mSprite_Tile_CheckBox = new TiledSprite(260, 266,
				this.mTR_CheckboxButton, vertexBufferObjectManager){
			
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if(pSceneTouchEvent.isActionDown()) {
					if(!isRemembe){
						mSprite_Tile_CheckBox.setCurrentTileIndex(0);
						isRemembe=true;
					}else{
						mSprite_Tile_CheckBox.setCurrentTileIndex(1);
						isRemembe=false;
					}
				}
				return true;
			}
		};
		mScene.attachChild(mSprite_Tile_CheckBox);
		mScene.registerTouchArea(mSprite_Tile_CheckBox);
		
		
		
		return mScene;

	}

	@Override
	protected synchronized void onResume() {
		// TODO Auto-generated method stub
		username = createEditText(true, 1, "User name", "", 60,
				Color.TRANSPARENT);
		tableLayout = createTableLayout(253, 129, 270,
				MyCamera.CAMERA_HEIGHT - 129 - 60, username);
		this.addContentView(tableLayout, new ViewGroup.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		password = createEditText(true, 1, "Password", "", 60,
				Color.TRANSPARENT);
		tableLayout = createTableLayout(253, 195, 270,
				MyCamera.CAMERA_HEIGHT - 191 - 60, password);
		this.addContentView(tableLayout, new ViewGroup.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		super.onResume();
	}

	public void showToast(String msg) {
		Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
	}

	private TableLayout createTableLayout(int left, int top, int right,
			int bottom, View view) {
		TableLayout tableLayout = new TableLayout(this);
		tableLayout.setVerticalGravity(Gravity.CENTER_VERTICAL);
		tableLayout.setLayoutParams(new FrameLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		tableLayout.setPadding(left, top, right, bottom);
		tableLayout.addView(view);
		return tableLayout;
	}

	private EditText createEditText(boolean isSingleLine, int id, String hint,
			String text, int height, int color) {
		EditText editText = new EditText(this);
		editText.setSingleLine(isSingleLine);
		editText.setId(id);
		editText.setHint(hint);
		Typeface mTypeface = Typeface.createFromAsset(getAssets(),
				"font/Vnidisneynornal.ttf");
		editText.setTextSize(22f);
		editText.setTypeface(mTypeface);
		editText.setHeight(height);
		editText.setBackgroundColor(color);
		editText.setPadding(16, 15, 0, 0);
		editText.setTextColor(Color.rgb(30, 159, 255));
		editText.setHintTextColor(Color.rgb(30, 159, 255));
		return editText;
	}

	@Override
	public synchronized void onPauseGame() {
		// TODO Auto-generated method stub
		LoginActivity.this.finish();
		super.onPauseGame();
	}
	
}