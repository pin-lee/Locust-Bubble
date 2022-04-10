package com.braydenl.locustbubble;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Game extends ApplicationAdapter {
	private BitmapFont font;
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
	private Texture gold;
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
	private int timer = 60;
	private int timerFrameCounter = 0;
	private final int goldCounter = 0;
	private int isWonFrameCount;

	public enum State{
		Running, Paused
	}

	private State state = State.Running;

	Player playerCharacter;
	private final boolean isWon = false;
	private int[][] villagers = {
			{4, 346},
			{192, 305},
			{115, 301},
			{122, 362},
			{67, 348},
			{36, 404},
			{179, 386},
			{170, 417},
			{221, 419},
			{271, 412},
	};

	
	@Override
	public void create () {
		font = new BitmapFont();
		font.getData().setScale(5.0f);
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
		gold = new Texture(Gdx.files.internal("gold.png"));
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
		timer = 60;
		timerFrameCounter = 0;
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
			//System.out.println("X: " + (1024 - Gdx.input.getX()) + " | " + "Y: " + (768 - Gdx.input.getY()));
			System.out.println("{" + (Gdx.input.getX()) + ", " + (768 - Gdx.input.getY()) + "},");
		}
	}


	// https://stackoverflow.com/questions/22550259/libgdx-play-sound-or-music-best-practice
	// https://stackoverflow.com/questions/22296997/about-libgdx-addlistener

	private void keystrokeCheck() {
		if (playerCharacter.y > (750)) 	playerCharacter.y = 750;
		if (playerCharacter.x < 10) 		playerCharacter.x = 10;
		if (playerCharacter.x > (1010)) playerCharacter.x = 1010;
		if (playerCharacter.y < 10) playerCharacter.y = 10;

		// W is UP
		if (Gdx.input.isKeyPressed(Input.Keys.W)) {
			playerCharacter.y += 2 * playerCharacter.speedModifier;
		}
		// S is DOWN
		if (Gdx.input.isKeyPressed(Input.Keys.S)) {
			playerCharacter.y -= 2 * playerCharacter.speedModifier;
		}
		// A is LEFT
		if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			playerCharacter.x -= 2 * playerCharacter.speedModifier;
		}
		// D is RIGHT
		if (Gdx.input.isKeyPressed(Input.Keys.D)) {
			playerCharacter.x += 2 * playerCharacter.speedModifier;
		}
		// SPACE is USE / INTERACT
		if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
			// Check for intractable hit-boxes / coordinate ranges.
		}
	}

	@Override
	public void render() {
		//ScreenUtils.clear(1, 0, 0, 1);
		batch.begin();
		checkLoss();
		switch (state) {	// Pause implementation from here:
			case Running:	// https://stackoverflow.com/questions/21576181/pause-resume-a-simple-libgdx-game-for-android
				//updateBubble();
				backgroundSprite.draw(batch);
				batch.draw(wizard, 336, 350);

				// TIMER STUFF
				font.draw(batch, String.valueOf(timer), 470, 760);
				timerFrameCounter++;
				if (timerFrameCounter == 60) {
					timer--;
					timerFrameCounter = 0;
				}
				if (timer == 0) state = State.Paused;

				// COIN STUFF
				font.draw(batch, String.valueOf(goldCounter), 700, 760);
				batch.draw(gold, 745, 695);

				// BARLEY STUFF
				font.draw(batch, String.valueOf(barleyCounter), 45, 760);
				batch.draw(barley, 90, 695);

				// LOCUST STUFF
				font.draw(batch, String.valueOf(locustCount), 270, 760);
				batch.draw(locust, 315, 695);

				if (isWon) animateVillagers();
				renderVillagers();
				batch.draw(bubble, bubbleX, 0);
				coordinateClickListen();

				keystrokeCheck();
				batch.draw(player, playerCharacter.x, playerCharacter.y);

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
