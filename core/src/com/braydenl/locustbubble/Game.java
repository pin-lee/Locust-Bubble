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

import java.util.concurrent.ThreadLocalRandom;


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
	private Sprite winScreen;
	private Sprite shopScreen;
	Music gameplayMusic;

	private int bubbleCounter = 0;
	private int bubbleX = 1024;
	private int locustCount = 0;
	private int bubbleSlowdown = locustCount + 2;
	private int barleyCounter = 0;
	private int timer = 60;
	private int timerFrameCounter = 0;
	private int goldCounter = 0;
	private int isWonFrameCount = 0;	// Count to end villager animation
	private int bubbleOnScreen = 0;

	public enum State{
		Running, Lost, Shopping, Won
	}

	private State state = State.Running;

	Player playerCharacter;
	private boolean isWon = false;
	private final int[][] villagerLocations = {
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
	private final int[][] villagers = villagerLocations;
	private final int[][] goldLocations = {
			{33, 39},
			{800, 645},
			{19, 679},
	};
	private int[][] goldSpawns = goldLocations;
	private final int[][] barleyLocations = {
			{853, 627},
			{596, 570},
			{285, 543},
			{202, 543},
			{140, 543},
			{33, 564},
			{58, 148},
			{233, 110},
			{640, 126},
			{642, 214},
			{916, 278},
			{822, 212},
	};
	private int[][] barleySpawns = barleyLocations;

	
	@Override
	public void create () {
		font = new BitmapFont();
		font.getData().setScale(5.0f);
		batch = new SpriteBatch();
		// load the images for the droplet and the bucket, 64x64 pixels each
		background = new Texture((Gdx.files.internal("background.png")));
		backgroundSprite = new Sprite(background);
		Texture win = new Texture(Gdx.files.internal("win.png"));
		Texture shop = new Texture(Gdx.files.internal("shop.png"));
		shopScreen = new Sprite(shop);
		winScreen = new Sprite (win);
		locust = new Texture(Gdx.files.internal("locust.png"));
		barley = new Texture(Gdx.files.internal("barley.png"));
		locustQueen = new Texture(Gdx.files.internal("locustQueen.png"));
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
		barleySpawns = barleyLocations;
		goldSpawns = goldLocations;
		bubbleOnScreen = 0;
	}

	void hardReset() {
		reset();
		isWonFrameCount = 0;
		barleyCounter = 0;
		playerCharacter.speedModifier = 1;
		playerCharacter.hasBarleySave = false;
		playerCharacter.hasLocustSummon = false;
		playerCharacter.x = 371;
		playerCharacter.y = 223;
	}

	private void renderVillagers() {
		for (int[] i : villagers) {
			batch.draw(villager, i[0], i[1]);
		}
	}

	private void animateVillagers() {
			for (int[] i : villagers) {
				i[0]--;
				if (isWonFrameCount == 450) {
					state = State.Won;
					break;
				}
		}
	}

	private void updateBubble() {
		if (bubbleCounter == bubbleSlowdown) {	// Moves the bubble a single pixel if the slowdown (amount of frames)
			bubbleX--;							// has been met. This changes the rate of movement of the bubble.
			bubbleOnScreen++;
			bubbleCounter = 0;
		} else {
			bubbleCounter++;
		}
	}

	private void checkLoss() {
		if (bubbleX == 0) {
			state = State.Lost;
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

	private void runningKeystrokeCheck() {
		if (playerCharacter.y > (750)) 	playerCharacter.y = 750;	// "Pad" the edges of the window with clipping.
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
			/* Wizard Hit-box:
					{333, 358},
					{400, 358},
					{337, 413},
					{399, 410},

					X RANGE = [ 333 TO 400 ]
					Y RANGE = [ 358 TO 412 ]
			*/
			// Checks for Wizard hit-box
			if (
					(playerCharacter.x >= 323)
					&& (playerCharacter.x <= 410)
					&& (playerCharacter.y >= 340)
					&& (playerCharacter.y <= 422)
			) {
				state = State.Shopping;
			}

		}
	}

	private void shoppingClickCheck() {
		if (playerCharacter.y > (750)) 	playerCharacter.y = 750;	// "Pad" the edges of the window with clipping.
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
		if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
			if (((playerCharacter.x >= 605) && (playerCharacter.x <= 675) && (playerCharacter.y >= 436)) && (Gdx.input.getY() <= 506)) {
				if (goldCounter >= 3) {
					upgradeSound.play();
					goldCounter -= 3;
					playerCharacter.speedModifier += 0.5;
				}
			}
			if (((playerCharacter.x >= 819) && (playerCharacter.x <= 885) && (playerCharacter.y >= 334)) && (Gdx.input.getY() <= 403)) {
				if (goldCounter >= 5) {
					upgradeSound.play();
					goldCounter -= 5;
					playerCharacter.hasBarleySave = true;
				}
			}
			if (((playerCharacter.x >= 811) && (playerCharacter.x <= 883) && (playerCharacter.y >= 239)) && (Gdx.input.getY() <= 306)) {
				if (goldCounter >= 5) {
					upgradeSound.play();
					goldCounter -= 5;
					playerCharacter.hasLocustSummon = true;
				}
			}
			if (((playerCharacter.x >= 811) && (playerCharacter.x <= 887) && (playerCharacter.y >= 58)) && (Gdx.input.getY() <= 110)) {
				if (barleyCounter >= 1) {
					locustCount += barleyCounter;
					bubbleSlowdown = locustCount + 2;
					barleyCounter = 0;
					locustSound.play();
				}
			}
		}
	}

	@Override
	public void render() {
		batch.begin();
		checkLoss();
		switch (state) {	// Pause implementation from here:
			case Running:	// https://stackoverflow.com/questions/21576181/pause-resume-a-simple-libgdx-game-for-android
				runningKeystrokeCheck();
				backgroundSprite.draw(batch);
				batch.draw(wizard, 336, 350);

				// TIMER STUFF
				font.draw(batch, String.valueOf(timer), 470, 760);
				timerFrameCounter++;
				if (timerFrameCounter == 60) {
					timer--;
					timerFrameCounter = 0;
				}
				if (timer == 0 && bubbleX != 0) {
					isWon = true;
					state = State.Won;
				}

				// COIN STUFF
				font.draw(batch, String.valueOf(goldCounter), 700, 760);
				batch.draw(gold, 745, 695);

				// BARLEY HUD STUFF
				font.draw(batch, String.valueOf(barleyCounter), 45, 760);
				batch.draw(barley, 90, 695);

				// LOCUST STUFF
				font.draw(batch, String.valueOf(locustCount), 270, 760);
				batch.draw(locust, 320, 695);

				if (isWon) animateVillagers();	// Handles win sequence

				renderVillagers();

				batch.draw(bubble, bubbleX, 0);

				coordinateClickListen();

				// Render barley spawns
				for (int[] i : barleySpawns) {
					batch.draw(barley, i[0], i[1]);
					if ((Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) && (playerCharacter.x >= (i[0] - 32)) && (playerCharacter.x <= i[0] + 32) && (playerCharacter.y >= i[1] - 32) && (playerCharacter.y <= i[1] + 32)) {
						harvestSound.play();
						i[0] = 2000;
						i[1] = 2000;
						barleyCounter++;
						if (playerCharacter.hasLocustSummon) {
							locustCount += barleyCounter;
							barleyCounter = 0;
							locustSound.play();
						}
					}
				}
				for (int[] i : goldSpawns) {
					batch.draw(gold, i[0], i[1]);
					if ((Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) && (playerCharacter.x >= (i[0] - 32)) && (playerCharacter.x <= i[0] + 32) && (playerCharacter.y >= i[1] - 32) && (playerCharacter.y <= i[1] + 32)) {
						upgradeSound.play();
						i[0] = 2000;
						i[1] = 2000;
						goldCounter++;
					}
				}

				batch.draw(player, playerCharacter.x, playerCharacter.y);

				for (int i = 0; i < locustCount; i++) {
					int x = ThreadLocalRandom.current().nextInt(bubbleX, bubbleX + bubbleOnScreen + 1);
					int y = ThreadLocalRandom.current().nextInt(0, 768 + 1);
					batch.draw(locust, x, y);
				}

				updateBubble();
				break;

			case Lost:
				lossScreen.draw(batch);
				reset();
				if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
					state = State.Running;
					break;
				}

			case Won:
				winScreen.draw(batch);

				hardReset();
				if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
					state = State.Running;
					break;
				}

			case Shopping:
				shopScreen.draw(batch);	// Background
				// HUD stuff
				font.draw(batch, String.valueOf(timer), 470, 760);
				font.draw(batch, String.valueOf(goldCounter), 700, 760);
				batch.draw(gold, 745, 695);
				font.draw(batch, String.valueOf(barleyCounter), 45, 760);
				batch.draw(barley, 90, 695);
				font.draw(batch, String.valueOf(locustCount), 270, 760);
				batch.draw(locust, 315, 695);
				// Upgrade stuff
				batch.draw(speedUpgrade, 610, 438);
				if (!playerCharacter.hasBarleySave) batch.draw(barleySaveUpgrade, 820, 336);
				if (!playerCharacter.hasLocustSummon) batch.draw(locustSummonUpgrade, 816, 236);

				// Summon
				batch.draw(locustQueen, 814, 49);

				batch.draw(player, playerCharacter.x, playerCharacter.y);
				shoppingClickCheck();
				coordinateClickListen();
				if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
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
