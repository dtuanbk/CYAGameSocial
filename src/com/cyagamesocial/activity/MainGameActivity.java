package com.cyagamesocial.activity;

import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.IOnAreaTouchListener;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.extension.tmx.TMXLayer;
import org.andengine.extension.tmx.TMXLoader;
import org.andengine.extension.tmx.TMXLoader.ITMXTilePropertiesListener;
import org.andengine.extension.tmx.TMXProperties;
import org.andengine.extension.tmx.TMXTile;
import org.andengine.extension.tmx.TMXTileProperty;
import org.andengine.extension.tmx.TMXTiledMap;
import org.andengine.extension.tmx.util.exception.TMXLoadException;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.debug.Debug;


import com.cyagamesocial.sprite.Player;
import com.cyagamesocial.unity.MyCamera;

public class MainGameActivity extends SimpleBaseGameActivity{
	
	private BoundCamera mBoundChaseCamera;

	private TMXTiledMap mTMXTiledMap;
	
	private BitmapTextureAtlas mBitmapTextureAtlas2;
	private ITextureRegion mFaceTextureRegion;
	
	private BitmapTextureAtlas mBitmapTextureAtlas;
	private TiledTextureRegion mPlayerTextureRegion;
	
	private Scene scene;
	
	private Sprite face;
	
	private AnimatedSprite playerSprite;
	
	private boolean isPlayerMoving=false;
	
	private float newPlayerPositionX,newPlayerPositionY;
	private float oldPlayerPositionX,oldPlayerPositionY;
	
	
	
	@Override
	public EngineOptions onCreateEngineOptions() {
		this.mBoundChaseCamera = new BoundCamera(0, 0, MyCamera.CAMERA_WIDTH, MyCamera.CAMERA_HEIGHT);

		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(MyCamera.CAMERA_WIDTH, MyCamera.CAMERA_HEIGHT), this.mBoundChaseCamera);

	}

	@Override
	protected void onCreateResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		this.mBitmapTextureAtlas2 = new BitmapTextureAtlas(this.getTextureManager(), 32, 32, TextureOptions.BILINEAR);
		this.mFaceTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTextureAtlas2, this, "face_box.png", 0, 0);
		this.mBitmapTextureAtlas2.load();
		
		//Load resoure player
		loadResourePlayer();
		
		//
	}

	@Override
	protected Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());
		// TODO Auto-generated method stub
		scene=new Scene();
		try {
			final TMXLoader tmxLoader = new TMXLoader(this.getAssets(), this.mEngine.getTextureManager(), TextureOptions.BILINEAR_PREMULTIPLYALPHA, this.getVertexBufferObjectManager(), new ITMXTilePropertiesListener() {
				@Override
				public void onTMXTileWithPropertiesCreated(final TMXTiledMap pTMXTiledMap, final TMXLayer pTMXLayer, final TMXTile pTMXTile, final TMXProperties<TMXTileProperty> pTMXTileProperties) {
					
				}
			});
			this.mTMXTiledMap = tmxLoader.loadFromAsset("tmx/map_pro.tmx");
			
		} catch (final TMXLoadException e) {
			Debug.e(e);
		}
		final TMXLayer tmxLayer = this.mTMXTiledMap.getTMXLayers().get(0);
		
		scene.attachChild(tmxLayer);
		
		/* Make the camera not exceed the bounds of the TMXEntity. */
		this.mBoundChaseCamera.setBounds(0, 0, tmxLayer.getHeight(), tmxLayer.getWidth());
		this.mBoundChaseCamera.setBoundsEnabled(true);
		
		final float centerX = (MyCamera.CAMERA_WIDTH - this.mFaceTextureRegion.getWidth()) / 2;
		final float centerY = (MyCamera.CAMERA_HEIGHT - this.mFaceTextureRegion.getHeight()) / 2;
		face = new Sprite(centerX, centerY, this.mFaceTextureRegion, this.getVertexBufferObjectManager());
		
		scene.attachChild(face);
		
		//Add playerSprite to screen
		initplayerSprite(scene,this.mPlayerTextureRegion);
		
		//Scene UpdateHandler
		
		scene.setTouchAreaBindingOnActionDownEnabled(true);
		scene.setOnAreaTouchListener(pOnAreaTouchListener);
		scene.registerUpdateHandler(pUpdateHandler);
		scene.registerTouchArea(tmxLayer);
		scene.setOnSceneTouchListener(pOnSceneTouchListener);
		return scene;
	}
	
	
	
	//pOnAreaTouchListener
	IOnAreaTouchListener pOnAreaTouchListener=new IOnAreaTouchListener() {
		
		@Override
		public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
				ITouchArea pTouchArea, float pTouchAreaLocalX,
				float pTouchAreaLocalY) {
				newPlayerPositionX=pSceneTouchEvent.getX();
				newPlayerPositionY=pSceneTouchEvent.getY();
				oldPlayerPositionX=playerSprite.getX();
				oldPlayerPositionY=playerSprite.getY();
				
				System.out.println("NewPosition:"+newPlayerPositionX+","+newPlayerPositionY);

			return true;
		}
	};
	
	
	//pOnSceneTouchListener
	IOnSceneTouchListener pOnSceneTouchListener=new IOnSceneTouchListener() {
		
		@Override
		public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
			// TODO Auto-generated method stub
			if(pSceneTouchEvent.isActionDown()){
				newPlayerPositionX=pSceneTouchEvent.getMotionEvent().getX();
				newPlayerPositionY=pSceneTouchEvent.getMotionEvent().getY();
				System.out.println("NewPosition:"+newPlayerPositionX+","+newPlayerPositionY);
				
				isPlayerMoving=true;
			}
			return true;
		}
	};
	
	
	
	//pUpdateHandler
	
	IUpdateHandler pUpdateHandler=new IUpdateHandler() {
		
		@Override
		public void reset() {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onUpdate(float pSecondsElapsed) {
			// TODO Auto-generated method stub
			if(isPlayerMoving){
				updatePlayerPosition(newPlayerPositionX, newPlayerPositionY);
			}
		}
	};
	
	
	
	//Player
	
	public void loadResourePlayer(){
		this.mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 72, 128, TextureOptions.DEFAULT);
		this.mPlayerTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "player.png", 0, 0, 3, 4);

		this.mBitmapTextureAtlas.load();
	}
	
	public void initplayerSprite(Scene mScene,TiledTextureRegion mTiledTextureRegion){
		playerSprite=new Player(100, 100, mTiledTextureRegion, this.getVertexBufferObjectManager());
//		mBoundChaseCamera.setChaseEntity(playerSprite);
		mScene.attachChild(playerSprite);
	}
	
	public void updatePlayerPosition(float newPlayerPositionX,float newPlayerPositionY){
//		playerSprite.setPosition(newPlayerPositionX, newPlayerPositionY);
		if(oldPlayerPositionX<newPlayerPositionX && oldPlayerPositionY<newPlayerPositionY){
			if(playerSprite.getX()<newPlayerPositionX){
				if(playerSprite.getY()<newPlayerPositionY){
					playerSprite.setPosition(playerSprite.getX()+2, playerSprite.getY()+2);
				}else if(playerSprite.getY()>newPlayerPositionY){
					playerSprite.setPosition(playerSprite.getX()+2, playerSprite.getY()-2);
				}else{
					isPlayerMoving=false;
				}
				
			}
			
			else if(playerSprite.getX()>newPlayerPositionX){
				if(playerSprite.getY()<newPlayerPositionY){
					playerSprite.setPosition(playerSprite.getX()-2,	playerSprite.getY()+2);
				}else if(playerSprite.getY()>newPlayerPositionY){
					playerSprite.setPosition(playerSprite.getX()-2, playerSprite.getY()-2);
				}else{
					isPlayerMoving=false;
				}
				
			}
		}
	}
	
	
}
