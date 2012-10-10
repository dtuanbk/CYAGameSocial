package com.cyagamesocial.sprite;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class Player extends AnimatedSprite{

	public Player(float pX, float pY,ITiledTextureRegion pTiledTextureRegion,
			VertexBufferObjectManager vertexBufferObjectManager) {
		super(pX, pY, pTiledTextureRegion,
				vertexBufferObjectManager);
		// TODO Auto-generated constructor stub
	}

}
