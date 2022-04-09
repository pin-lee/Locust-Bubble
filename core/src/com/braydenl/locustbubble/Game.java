package com.braydenl.locustbubble;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public class Game extends ApplicationAdapter {
	private Texture background;
	public static Sprite backgroundSprite;
	private Texture locust;
	private Texture barley;
	private Texture locustQueen;
	private Texture villager;
	private Texture wizard;
	private Texture dirt;
	private Texture house;
	private Texture player;
	private Texture portal;
	private Texture speedUpgrade;
	private Texture locustSummonUpgrade;
	private Texture barleySaveUpgrade;
	private Texture bubble;
	private Sound portalSound;
	private Sound harvestSound;
	private Sound locustSound;
	private Sound upgradeSound;
	private Sound runSound;
	private Sound summonSound;
	private Music gameplayMusic;
	//private Music victoryMusic;
	SpriteBatch batch;
	Texture img;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		// load the images for the droplet and the bucket, 64x64 pixels each
		background = new Texture((Gdx.files.internal("background.png")));
		backgroundSprite = new Sprite(background);
		locust = new Texture(Gdx.files.internal("locust.png"));
		barley = new Texture(Gdx.files.internal("barley.png"));
		locustQueen = new Texture(Gdx.files.internal("bubble.png"));
		villager = new Texture(Gdx.files.internal("dirt.png"));
		wizard = new Texture(Gdx.files.internal("wizard.png"));
		dirt = new Texture(Gdx.files.internal("dirt.png"));
		player = new Texture(Gdx.files.internal("player.png"));
		portal = new Texture(Gdx.files.internal("portal.png"));
		speedUpgrade = new Texture(Gdx.files.internal("speedUpgrade.png"));
		locustSummonUpgrade = new Texture(Gdx.files.internal("locustSummonUpgrade.png"));
		barleySaveUpgrade = new Texture(Gdx.files.internal("barleySaveUpgrade.png"));
		bubble = new Texture(Gdx.files.internal("bubble.png"));
		// load the sound effects and the background music
		/*
		portalSound = Gdx.audio.newSound(Gdx.files.internal(".wav"));
		harvestSound = Gdx.audio.newSound(Gdx.files.internal(".wav"));
		locustSound = Gdx.audio.newSound(Gdx.files.internal(".wav"));
		upgradeSound = Gdx.audio.newSound(Gdx.files.internal(".wav"));
		runSound = Gdx.audio.newSound(Gdx.files.internal(".wav"));
		summonSound = Gdx.audio.newSound(Gdx.files.internal(".wav"));
		*/
		gameplayMusic = Gdx.audio.newMusic(Gdx.files.internal("gameplayMusic.mp3"));
		//victoryMusic = Gdx.audio.newMusic(Gdx.files.internal(".mp3"));


		// start the playback of the background music immediately
		gameplayMusic.setLooping(true);
		gameplayMusic.play();
	}

	public void renderBackground() {
		backgroundSprite.draw(batch);
	}

	@Override
	public void render () {
		ScreenUtils.clear(1, 0, 0, 1);
		batch.begin();
		renderBackground();
		//batch.draw(img, 0, 0);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
