package edu.duq.schoenp.quickshop;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.hanks.htextview.base.HTextView;

/**
 * Share activity
 */
public class Share extends AppCompatActivity {

  /**
   * log tag
   */
  private static final String TAG = "Share";
  /**
   * minutes spent shopping
   */
  public long minutes;
  /**
   * animated cart
   */
  AnimationDrawable cartAnimation;

  /**
   * When the activity is created get and convert time to minutes to be displayed
   *
   * @param savedInstanceState current instance state
   */
  @SuppressLint("SetTextI18n")
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_share);
    long receivedTimeMillis = getIntent().getLongExtra("chronoTime", 0);
    minutes = (receivedTimeMillis / 1000) / 60;
    int seconds = (int) ((receivedTimeMillis / 1000) % 60);
    Log.d(TAG, "onCreate: time is " + minutes + " minutes and " + seconds + " seconds ");
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayShowHomeEnabled(true);
      actionBar.setIcon(R.mipmap.ic_launcher2);
    }

    TextView timeTextView = findViewById(R.id.completionTime);
    timeTextView.setText(
        "Congratulation! it only took you \n " + minutes + " minutes and " + seconds
            + " seconds \n  to complete your shopping! \n Share your results with friends so they can save time too!");
    Typeface tf = ResourcesCompat.getFont(this, R.font.rubik_medium);
    HTextView congratsFade = (HTextView) timeTextView;
    congratsFade.setTypeface(tf);
    congratsFade.animateText(congratsFade.getText());
    ImageView cartImage = (ImageView) findViewById(R.id.cart2);
    cartImage.setBackgroundResource(R.drawable.cart);
    cartAnimation = (AnimationDrawable) cartImage.getBackground();
    cartAnimation.start();

  }

  /**
   * Create share intent
   *
   * @param view current view
   */
  public void shareResults(View view) {
    Intent intent = new Intent();
    intent.setAction(Intent.ACTION_SEND);

    intent.setType("text/plain");
    intent.putExtra(Intent.EXTRA_TEXT,
        "I optimized my grocery trip using the QuickShop app and it only took me " + minutes
            + " minutes to get done shopping! \n You can save time shopping too by downloading the QuickShop app https://github.com/schoenp711/QuickShop");
    startActivity(Intent.createChooser(intent, "Share"));
  }
}
