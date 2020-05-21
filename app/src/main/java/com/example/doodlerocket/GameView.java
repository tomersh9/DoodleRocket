package com.example.doodlerocket;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.View;

public class GameView extends View {

    //player properties - where it located on screen
    private int rocketX, rocketY; //rocketSpeed;
    private int score;
    private int lifeCount;
    private boolean isMoving = false;

    //enemy spawns
    private int yellowX,yellowY, yellowSpeed = 15;
    private Paint yellowPaint = new Paint();
    private int greenX,greenY, greenSpeed = 25;
    private Paint greenPaint = new Paint();
    private int redX,redY, redSpeed = 20;
    private Paint redPaint = new Paint();

    //canvas properties
    private int canvasW, canvasH;

    //drawables
    private Bitmap rocketFigures[] = new Bitmap[2]; //rockets
    private Bitmap life[] = new Bitmap[2]; //hearts
    private Bitmap background;

    //paint
    private Paint scorePaint = new Paint();


    public GameView(Context context) { //context when calling it
        super(context);

        //decodes bitmap to objects of Bitmap
        rocketFigures[0] = BitmapFactory.decodeResource(getResources(), R.drawable.rocket);
        rocketFigures[1] = BitmapFactory.decodeResource(getResources(), R.drawable.rocket_red_70);
        background = BitmapFactory.decodeResource(getResources(), R.drawable.background2);

        //balls spawn on screen
        yellowPaint.setColor(Color.YELLOW);
        yellowPaint.setAntiAlias(false);
        greenPaint.setColor(Color.GREEN);
        greenPaint.setAntiAlias(false);
        redPaint.setColor(Color.RED);
        redPaint.setAntiAlias(false);

        //initial spawn for balls
        yellowX = 250;
        greenX = 500;
        redX = 1000;

        //setting default paint properties
        scorePaint.setColor(Color.BLACK);
        scorePaint.setTextSize(100);
        scorePaint.setTypeface(Typeface.DEFAULT);
        scorePaint.setAntiAlias(true);

        //life display of fish (have or not)
        life[0] = BitmapFactory.decodeResource(getResources(),R.drawable.heart_red);
        life[1] = BitmapFactory.decodeResource(getResources(),R.drawable.heart_grey);

        //first position when starting app (bottom middle screen)
        //and current positions that we update all the time
        rocketY = 2000;
        rocketX = 450;

        //initialize score & health
        score = 0;
        lifeCount = 3;
    }

    @Override
    protected void onDraw(Canvas canvas) { //order matters!!
        super.onDraw(canvas);

        //getting canvas measures
        canvasW = canvas.getWidth();
        canvasH = canvas.getHeight();

        //draw background
        //fix gap in right side of screen!!!!!!!
        canvas.drawBitmap(background, 0, 0, null);

        //setting up min & max values for the rocket to travel on X Axis
        int minRocketX = 50; //left side of screen
        int maxRocketX = canvas.getWidth() - rocketFigures[0].getWidth(); //right side of screen

        //retains rocket in screen (Horizontal)
        if(rocketX < minRocketX) rocketX = minRocketX;
        if(rocketX > maxRocketX) rocketX = maxRocketX;

        // *** CONTROLLER ***
        //moving rocket on X Axis
        canvas.drawBitmap(rocketFigures[0], rocketX, rocketY,null); //draw normal rocket
        if(isMoving) {
            isMoving = false;
        }

        //moving balls vertical Top-Down
        yellowY += yellowSpeed;
        greenY += greenSpeed;
        redY += redSpeed;

        if(hitBallCheck(greenX,greenY)) { //if fish hit ball
            score += 10;
            greenY = canvasH + 50; //sending ball back up to "relaunch"
        }

        if(greenY > canvasH) { //ball appears on random position after colliding with them
            greenY =  0; //reappear at top screen
            greenX = (int) Math.floor(Math.random() * (maxRocketX - minRocketX) + minRocketX);
        }
        canvas.drawCircle(greenX,greenY,35,greenPaint); //draw green circle

        if(hitBallCheck(yellowX,yellowY)) { //if fish hit ball
            score++;
            yellowY = canvasH + 50; //sending ball back up to "relaunch"
        }

        if(yellowY > canvasH) { //ball appears on random position after colliding with them
            yellowY = 0; //reappear at top screen
            yellowX = (int) Math.floor(Math.random() * (maxRocketX - minRocketX) + minRocketX);
        }
        canvas.drawCircle(yellowX,yellowY,20,yellowPaint); //draw yellow circle

        if(hitBallCheck(redX,redY)) { //red ball dec life

            lifeCount--;
            canvas.drawBitmap(rocketFigures[1], rocketX, rocketY, null); //hit rocket
            redY = canvasH +50; //moving ball so it can be created randomly again

            if(lifeCount == 0) { //GAME OVER INTENT MOVE

                Intent gameOverIntent = new Intent(getContext(),GameOverActivity.class);
                gameOverIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                gameOverIntent.putExtra("score", score);
                getContext().startActivity(gameOverIntent);
            }

        }

        if(redY > canvasH) { //ball appears on random position after colliding with them
            redY = 0; //reappears on top
            redX = (int) Math.floor(Math.random() * (maxRocketX - minRocketX) + minRocketX);
        }
        canvas.drawCircle(redX,redY,50,redPaint); //draw green circle

        canvas.drawText("Score: " + score, 60,100,scorePaint);

        //decrease life bitmaps
        for(int i=0; i<3; i++) {

            //20x - first heart position
            //width of heart
            // *1.5 * i space between them
            int x = (int) (20 + life[0].getWidth() * i);
            int y = 65; //margin top

            if(i<lifeCount) { //full heart
                canvas.drawBitmap(life[0],x,y,null);
            }
            else { //grey heart
                canvas.drawBitmap(life[1],x,y,null);
            }
        }
    }

    //rocket collides with ball
    public boolean hitBallCheck(int x, int y) {
        if(rocketX < x && x < (rocketX + rocketFigures[0].getWidth())
                && rocketY < y && y < (rocketY + rocketFigures[0].getHeight())) {
            return true;
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        //move rocket left and right
        if(event.getAction() == MotionEvent.ACTION_MOVE) {
            rocketX = (int) event.getX();
            isMoving = true;
        }
        return true;
    }
}
