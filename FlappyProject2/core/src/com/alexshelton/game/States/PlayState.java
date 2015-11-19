package com.alexshelton.game.States;

import com.alexshelton.game.FlappyBird;
import com.alexshelton.game.Sprites.Bird;
import com.alexshelton.game.Sprites.Tube;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Alex on 11/14/2015.
 */
public class PlayState extends State {
    private static final int TUBE_SPACING = 125;
    private static final int TUBE_COUNT = 4;
    private static final int GROUND_Y_OFFSET = -50;

    private BitmapFont font;
    private float testx = 120;
    private float y = 375 ;

    private Bird bird;
    private Texture bg;
    private Texture ground;
    private Vector2 groundPos1, groundPos2;
    private float test;

    private Array<Tube> tubes;


    public PlayState(GameStateManager gsm) {
        super(gsm);
        bird = new Bird(50, 100);
        cam.setToOrtho(false, FlappyBird.WIDTH / 2, FlappyBird.HEIGHT / 2);
        bg = new Texture("downtown.jpg");
        ground = new Texture("ground.png");
        groundPos1 = new Vector2(cam.position.x - cam.viewportWidth / 2, GROUND_Y_OFFSET);
        groundPos2 = new Vector2((cam.position.x - cam.viewportWidth / 2) + ground.getWidth(),GROUND_Y_OFFSET);

        font = new BitmapFont();










        tubes = new Array<Tube>();

        for(int i = 1; i <= TUBE_COUNT; i++){
            tubes.add(new Tube(i * (TUBE_SPACING + Tube.TUBE_WIDTH)));
        }
    }

    @Override
    protected void handleInput() {
        if(Gdx.input.justTouched())
            bird.jump();

    }

    @Override
    public void update(float dt) {
        handleInput();
        updateGround();
        updateScoreLocation();
        bird.update(dt);
        cam.position.x = bird.getPosition().x + 80;

        if(bird.getBounds().x >= 156)
            score = (bird.getBounds().x / 176) - 0.6f ;


        for(int i = 0; i < tubes.size; i++){

            Tube tube = tubes.get(i);

            if(cam.position.x - cam.viewportWidth / 2 > tube.getPosTopTube().x + tube.getTopTube().getWidth()){
                tube.reposition(tube.getPosTopTube().x + ((Tube.TUBE_WIDTH + TUBE_SPACING) * TUBE_COUNT));
            }

            




            if(tube.collides(bird.getBounds()))

                gsm.set(new GameOver(gsm));

            }

             if(bird.getPosition().y <= ground.getHeight() + GROUND_Y_OFFSET)

                gsm.set(new GameOver(gsm));


            cam.update();


    }

    @Override
    public void render(SpriteBatch sb) {

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        sb.setProjectionMatrix(cam.combined);
        sb.begin();








        sb.draw(bg, cam.position.x - (cam.viewportWidth / 2), 0);
        sb.draw(bird.getTexture(), bird.getPosition().x, bird.getPosition().y);

        for(Tube tube : tubes) {
            sb.draw(tube.getTopTube(), tube.getPosTopTube().x, tube.getPosTopTube().y);
            sb.draw(tube.getBottomTube(), tube.getPosBotTube().x, tube.getPosBotTube().y);
        }

        sb.draw(ground, groundPos1.x, groundPos1.y);
        sb.draw(ground, groundPos2.x, groundPos2.y);


        sb.end();

        sb.begin();

        font.setColor(Color.WHITE);
        font.draw(sb, String.format("%.0f", score) ,testx,y);
        finalScore = score;
        Gdx.app.log("testing",Float.toString(finalScore));

        sb.end();



    }

    @Override
    public void dispose() {
        bg.dispose();
        bird.dispose();

        ground.dispose();
        for(Tube tube : tubes)
            tube.dispose();

    }

    private void updateGround(){
        if(cam.position.x - (cam.viewportWidth /2) > groundPos1.x + ground.getWidth())
            groundPos1.add(ground.getWidth() * 2, 0);
        if(cam.position.x - (cam.viewportWidth /2) > groundPos2.x + ground.getWidth())
            groundPos2.add(ground.getWidth() * 2, 0);
    }

    private  void updateScoreLocation(){
       if(testx > 1)
           testx = testx + 1.67f;

    }




}
