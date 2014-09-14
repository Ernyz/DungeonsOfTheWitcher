package com.ernyz.dotw.Model.Tiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Torch extends Tile {
	
	private Animation animation;
	private Texture tileSheet = new Texture("data/animatedTiles/torch.png");
	private int frameCols = tileSheet.getWidth() / 50;  //TODO: Remove hardcoding!
	private int frameRows = tileSheet.getHeight() / 50;
	private TextureRegion[] frames;
	private TextureRegion currentFrame;
	private float stateTime = 0;

	public Torch(Vector2 position, float rotation) {
		super(position, rotation);
		
		asciiSymbol = 'Y';
		//texture = new Texture(Gdx.files.internal("data/tiles/CaveGround.png"));
		this.setWalkable(true);
		this.setName("torch");
		
		frames = new TextureRegion[frameCols * frameRows];
		TextureRegion[][] tmp = TextureRegion.split(tileSheet, 50, 50);  //TODO: Remove hardcoding!
		int counter = 0;
		for(int i = 0; i < frameCols; i++) {
			for(int j = 0; j < frameRows; j++) {
				frames[counter++] = tmp[j][i];
			}
		}
		animation = new Animation(1/16f, frames);
	}
	
	@Override
	public void update(float delta) {
		stateTime += delta;
		currentFrame = animation.getKeyFrame(stateTime, true);
	}
	
	public TextureRegion getTextureRegion() {
		return currentFrame;
	}
	
}
