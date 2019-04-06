package edu.duq.schoenp.quickshop;

import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import com.hanks.htextview.base.HTextView;

/**
 * QuickShop is an Android application that optimizes the users path through a selected grocery
 * store based on the items in their shopping list.
 *
 * Minimum Android version 8.0.0 SDK 26
 *
 * @author Phillip Schoen
 * @version 1.0
 * @since 2019-04-24
 */
public class SplashScreen extends AppCompatActivity {

  /**
   * log tag
   */
  private static final String TAG = "SplashScreen";
  /**
   * Animation drawable for animating the cart
   */
  AnimationDrawable cartAnimation;

  /**
   * Creates the activity
   *
   * @param savedInstanceState saved instance state for the application
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.splash_screen);
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayShowHomeEnabled(true);
      actionBar.setIcon(R.mipmap.ic_launcher2);
    }
    Typeface tf = ResourcesCompat.getFont(this, R.font.rubik_medium);
    //sets animation for text
    HTextView howToFade = findViewById(R.id.howTo);
    howToFade.setTypeface(tf);
    howToFade.animateText(howToFade.getText());

  }

  /**
   * Used to animate cart on the home screen. Called whenever the activity is started
   */
  @Override
  protected void onStart() {
    super.onStart();
    setContentView(R.layout.splash_screen);

    ImageView cartImage = (ImageView) findViewById(R.id.cart);
    cartImage.setBackgroundResource(R.drawable.cart);
    cartAnimation = (AnimationDrawable) cartImage.getBackground();
    cartAnimation.start();
  }

  /**
   * Creates an intent to the next activity, choosing a store
   *
   * @param view the current view
   */
  public void showMap(View view) {
    Intent intent = new Intent(this, PickStore.class);
    startActivity(intent);
  }


}
