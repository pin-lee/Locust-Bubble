package com.braydenl.locustbubble;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class Game extends ApplicationAdapter {
	private Texture background;
	private Texture locust;
	private Texture barley;
	private Texture locustQueen;
	private Texture villager;
	private Texture wizard;
	private Texture dirt;
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
	private SpriteBatch batch;
	private Sprite backgroundSprite;
	private Sprite lossScreen;
	Music gameplayMusic;

	private int bubbleCounter = 0;
	private int bubbleX = 1024;
	private int locustCount = 0;
	private int bubbleSlowdown = locustCount + 1;
	private int barleyCounter = 0;
	private final int goldCounter = 0;
	private int isWonFrameCount;

	public enum State{
		Running, Paused
	}

	private State state = State.Running;

	Player playerCharacter;
	private final boolean isWon = false;
	private int[][] villagers = {
			{5, 400},
			{56, 396},
			{100, 436},
			{170, 420},
			{159, 351},
			{77, 328},
			{180, 300},
			{147, 359},
			{211, 340},
			{220, 378},
			{249, 423}
	};

	
	@Override
	public void create () {
		batch = new SpriteBatch();
		// load the images for the droplet and the bucket, 64x64 pixels each
		background = new Texture((Gdx.files.internal("background.png")));
		backgroundSprite = new Sprite(background);
		locust = new Texture(Gdx.files.internal("locust.png"));
		barley = new Texture(Gdx.files.internal("barley.png"));
		locustQueen = new Texture(Gdx.files.internal("bubble.png"));
		villager = new Texture(Gdx.files.internal("villager.png"));
		wizard = new Texture(Gdx.files.internal("wizard.png"));
		dirt = new Texture(Gdx.files.internal("dirt.png"));
		player = new Texture(Gdx.files.internal("player.png"));
		portal = new Texture(Gdx.files.internal("portal.png"));
		lossScreen = new Sprite(portal);
		speedUpgrade = new Texture(Gdx.files.internal("speedUpgrade.png"));
		locustSummonUpgrade = new Texture(Gdx.files.internal("locustSummonUpgrade.png"));
		barleySaveUpgrade = new Texture(Gdx.files.internal("barleySaveUpgrade.png"));
		bubble = new Texture(Gdx.files.internal("bubble.png"));
		// load the sound effects and the background music
		runSound = Gdx.audio.newSound(Gdx.files.internal("runningSound.wav"));
		portalSound = Gdx.audio.newSound(Gdx.files.internal("portalSound.wav"));
		harvestSound = Gdx.audio.newSound(Gdx.files.internal("harvestSound.wav"));
		upgradeSound = Gdx.audio.newSound(Gdx.files.internal("upgradeSound.wav"));
		gameplayMusic = Gdx.audio.newMusic(Gdx.files.internal("gameplayMusic.mp3"));
		locustSound = Gdx.audio.newSound(Gdx.files.internal("locustSound.wav"));

		// start the playback of the background music immediately
		gameplayMusic.setLooping(true);
		gameplayMusic.play();

		playerCharacter = new Player();
	}

	private void reset() {
		bubbleSlowdown = 0;
		bubbleX = 1024;
		if (playerCharacter.hasBarleySave) barleyCounter = barleyCounter / 2;
		locustCount = 0;
	}

	private void renderVillagers() {
		for (int[] i : villagers) {
			batch.draw(villager, i[0], i[1]);
		}
	}

	private void animateVillagers() {
		for (int[] i : villagers) {
			i[0]--;
		}
	}

	private void updateBubble() {
		if (bubbleCounter == bubbleSlowdown) {	// Moves the bubble a single pixel if the slowdown (amount of frames)
			bubbleX--;							// has been met. This changes the rate of movement of the bubble.
			bubbleCounter = 0;
		} else {
			bubbleCounter++;
		}
	}

	private void checkLoss() {
		if (bubbleX == 0) {
			state = State.Paused;
		}
	}

	private void coordinateClickListen() {
		if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
			System.out.println("X: " + Gdx.input.getX() + "\n" + "Y: " + Gdx.input.getY());
		}
	}

	@Override
	public void render() {
		ScreenUtils.clear(1, 0, 0, 1);
		batch.begin();
		checkLoss();
		switch (state) {
			case Running:
				updateBubble();
				backgroundSprite.draw(batch);
				batch.draw(wizard, 336, 350);
				if (isWon) animateVillagers();
				renderVillagers();
				batch.draw(bubble, bubbleX, 0);
				coordinateClickListen();
				break;
			case Paused:
				lossScreen.draw(batch);
				reset();
				if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
					state = State.Running;
					break;
				}
		}
		batch.end();
	}


	@Override
	public void dispose () {
		batch.dispose();
		background.dispose();
		locust.dispose();
		barley.dispose();
		locustQueen.dispose();
		villager.dispose();
		wizard.dispose();
		dirt.dispose();
		player.dispose();
		portal.dispose();
		speedUpgrade.dispose();
		locustSummonUpgrade.dispose();
		barleySaveUpgrade.dispose();
		bubble.dispose();
		portalSound.dispose();
		harvestSound.dispose();
		locustSound.dispose();
		upgradeSound.dispose();
		runSound.dispose();
		portalSound.dispose();
		harvestSound.dispose();
		locustSound.dispose();
		upgradeSound.dispose();
		runSound.dispose();
	}
}
