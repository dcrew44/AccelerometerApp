package com.example.accelerometerapp;

public class  Bee {
    public float mX;
    public float mY;
    public int mWidth;
    public float mVelocityX;
    public float mVelocityY;

    //COLLISION BOUNDARIES FOR TOP, BOTTOM, LEFT, AND RIGHT
    public int top;
    public int bottom;
    public int left;
    public int right;

    public Bee() {
        mX = 250;
        mY = 250;
        mVelocityX = 0;
        mVelocityY = 0;
    }

    public void setBoundaries(int top, int bottom, int left, int right){
        mWidth = 504;
        this.top = top;
        this.bottom = bottom - mWidth;
        this.left = left;
        this.right = right - mWidth;


    }
    public void move() {
        //TASK 1: MOVE THE BEE
        mX = mX - mVelocityX;
        mY = mY + mVelocityY;

        //TASK 2: CHECK FOR COLLISION WITH THE TOP AND BOTTOM WALL.
        if (mY < top) {
            mY = top;
        } else if (mY > bottom) {
            mY = bottom;
        }

        //TASK 3: CHECK FOR COLLISION WITH THE LEFT AND RIGHT WALL.
        if (mX < left) {
            mX = left;
        } else if (mX > right) {
            mX = right;
        }

    }
}
      
    