package com.ntonsite.fish;

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
import android.widget.Toast;

public class FlyingFishView extends View {

    private Bitmap fish[] = new Bitmap[2];
    //Initializing fish position

    private int fishX = 10;
    private int fishY;
    private int fishSpeed;
    private int canvasWidth, canvasHeight;
    // yellow balls
    private int yellowX, yellowY, yellowSpeed = 16;
    private Paint yellowPaint = new Paint();

    //green Balls
    private int greenX, greenY, greenSpeed = 20;
    private Paint greenPaint =  new Paint();

    //Red Balls
    private int redX, redY, redSpeed = 25;
    private Paint redPaint =  new Paint();

    private int score, lifeCounterOffish;
    private boolean touch = false;

    private Bitmap backgroundImage;
    private Paint scorePaint = new Paint();
    private Bitmap Life[] = new Bitmap[2];
    public FlyingFishView(Context context) {
        super(context);

        fish[0] = BitmapFactory.decodeResource(getResources(), R.drawable.fish1);
        fish[1] = BitmapFactory.decodeResource(getResources(), R.drawable.fish2);

        backgroundImage = BitmapFactory.decodeResource(getResources(), R.drawable.background);

        //Blue Balls
        yellowPaint.setColor(Color.BLUE);
        yellowPaint.setAntiAlias(false);

        //Green Balls

        greenPaint.setColor(Color.GREEN);
        greenPaint.setAntiAlias(false);

        //Red Balls

        redPaint.setColor(Color.RED);
        redPaint.setAntiAlias(false);

        scorePaint.setColor(Color.WHITE);
        scorePaint.setTextSize(70);
        scorePaint.setTypeface(Typeface.DEFAULT_BOLD);
        scorePaint.setAntiAlias(true);

        Life[0] = BitmapFactory.decodeResource(getResources(), R.drawable.hearts);
        Life[1] = BitmapFactory.decodeResource(getResources(), R.drawable.heart_grey);

        fishY = 550;

        score = 0;
        lifeCounterOffish = 3;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvasWidth = canvas.getWidth();
        canvasHeight = canvas.getHeight();

        canvas.drawBitmap(backgroundImage,0,0,null);

        int minFishY = fish[0].getHeight();
        int maxFishY = canvasHeight - fish[0].getHeight() * 3;
        fishY = fishY + fishSpeed;

        if(fishY < minFishY){
            fishY = minFishY;
        } if(fishY > maxFishY){
            fishY = maxFishY;
        }
        fishSpeed = fishSpeed + 2;

        if(touch){
           canvas.drawBitmap(fish[1], fishX, fishY, null);
           touch = false;
        }
        else
        {
            canvas.drawBitmap(fish[0],fishX, fishY, null);
        }

        yellowX = yellowX - yellowSpeed;
        if(hitBallChecker(yellowX, yellowY))
        {
            score = score + 10;
            yellowX = -100;
        }
        if(yellowX < 0)
        {
            yellowX = canvasWidth + 21;
            yellowY = (int) Math.floor(Math.random() * (maxFishY - minFishY)) + minFishY;
        }
        canvas.drawCircle(yellowX, yellowY, 30, yellowPaint);

        //For Green Balls
        greenX = greenX - greenSpeed;
        if(hitBallChecker(greenX, greenY))
        {
            score = score + 20;
            greenX = -100;
        }
        if(greenX < 0)
        {
            greenX = canvasWidth + 21;
            greenY = (int) Math.floor(Math.random() * (maxFishY - minFishY)) + minFishY;
        }
        canvas.drawCircle(greenX, greenY, 70, greenPaint);

        //Red Balls

        redX = redX - redSpeed;
        if(hitBallChecker(redX, redY))
        {
            redX = -100;
            lifeCounterOffish --;

            if(lifeCounterOffish ==0)
            {
                Toast.makeText(getContext(),"Game Over", Toast.LENGTH_SHORT).show();

                Intent gameOverIntent = new Intent(getContext(), GameOverActivity.class);

                gameOverIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                gameOverIntent.putExtra("score",score);


                getContext().startActivity(gameOverIntent);
            }
        }
        if(redX < 0)
        {
            redX = canvasWidth + 21;
            redY = (int) Math.floor(Math.random() * (maxFishY - minFishY)) + minFishY;
        }
        canvas.drawCircle(redX, redY, 50, redPaint);

        canvas.drawText("Score: " + score, 20,60,scorePaint);

        for(int i=0; i<3; i++) {
            int x = (int) (580 + Life[0].getWidth() * 1.5 * i);
            int y = 30;

            if (i < lifeCounterOffish) {
                canvas.drawBitmap(Life[0], x, y, null);
            } else {
                canvas.drawBitmap(Life[1], x, y, null);
            }
        }
    }

    public boolean hitBallChecker(int x, int y){
        if(fishX < x && x < (fishX + fish[0].getWidth())  && fishY < y && y < (fishY + fish[0].getHeight())){
            return true;
        }

        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_DOWN){
            touch = true;
            fishSpeed = -22;

        }

        return  true;
    }
}