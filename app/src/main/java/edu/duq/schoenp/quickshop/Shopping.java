package edu.duq.schoenp.quickshop;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.Toast;
import java.util.ArrayList;

/**
 * Shopping activity
 */
public class Shopping extends AppCompatActivity {

  /**
   * log tag
   */
  private static final String TAG = "Shopping";
  /**
   * list of received items from user shopping list
   */
  ArrayList<StoreItem> receivedItems = new ArrayList<>();
  /**
   * Chronometer for keeping time spent shopping
   */
  private Chronometer chronometer;
  /**
   * Broadcast receiver for keeping accurate time and starting next activity once the list is empty
   */
  public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
    /**
     * When a message is received get clock time and start next activity
     * @param context application context
     * @param intent intent that called the broadcast receiver
     */
    @Override
    public void onReceive(Context context, Intent intent) {
      // Get extra data included in the Intent
      String ItemName = intent.getStringExtra("done");
      Toast.makeText(Shopping.this, ItemName, Toast.LENGTH_SHORT).show();
      Intent intent2 = new Intent(Shopping.this, Share.class);
      long chronoTime = SystemClock.elapsedRealtime() - chronometer.getBase();
      intent2.putExtra("chronoTime", chronoTime);
      startActivity(intent2);
    }
  };
  /**
   * Flag to determine if the clock is running
   */
  private boolean running;
  /**
   * offset that keeps track of time spent while paused
   */
  private long pauseOffset;
  /**
   * List of store items sent to adapter
   */
  private ArrayList<StoreItem> adapterItems = new ArrayList<>();
  /**
   * list of images for each store item
   */
  private ArrayList<String> mImageUrls = new ArrayList<>();
  /**
   * recycler view adapter for shopping activity
   */
  ShoppingRecyclerViewAdapter adapter = new ShoppingRecyclerViewAdapter(this, adapterItems,
      mImageUrls);

  /**
   * When activity is created start timer and parse received items
   *
   * @param savedInstanceState current instance state
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_shopping);
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayShowHomeEnabled(true);
      actionBar.setIcon(R.mipmap.ic_launcher2);
    }
    chronometer = findViewById(R.id.chronometer);
    chronometer.setFormat("Time spent Shopping: %s");
    chronometer.start();
    running = true;

    receivedItems = getIntent().getParcelableArrayListExtra("shopping list");
    Log.d(TAG, "onCreate: rec items" + receivedItems.get(0));
    for (int i = 0; i < receivedItems.size(); i++) {
      Log.d(TAG, "onCreate SHOPPING: item is " + receivedItems.get(i).toString());
    }

    initImageBitmaps();

    LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
        new IntentFilter("custom-message"));


  }

  /**
   * copy items received to items sent to adapter to be displayed
   */
  private void initImageBitmaps() {
    Log.d(TAG, "initImageBitmaps: prepareing bitmaps");

    ArrayList<StoreItem> testList = new ArrayList<>();
    //converting specialty item locations from internal numeric representation to display location
    for (int i = 0; i < receivedItems.size(); i++) {

      if (receivedItems.get(i).getLocation().startsWith("0")) {
        receivedItems.get(i).setLocation(receivedItems.get(i).getLocation().substring(1));
      }
      if (receivedItems.get(i).getLocation().equals("99A")) {
        receivedItems.get(i).setLocation("Dairy");
      }
      if (receivedItems.get(i).getLocation().equals("00A")) {
        receivedItems.get(i).setLocation("Produce");
      }

      testList.add(receivedItems.get(i));
    }

    for (int i = 0; i < testList.size(); i++) {
      String imageURL = "data:image/png;base64,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";
      mImageUrls.add(imageURL);
      StoreItem currItem = testList.get(i);
      adapterItems.add(currItem);
    }

    initRecyclerView();

  }

  /**
   * initialize recycler view adapter
   */
  private void initRecyclerView() {
    Log.d(TAG, "initRecyclerView: init recyclerView");
    RecyclerView recyclerView = findViewById(R.id.recycler_view3);
    recyclerView.setAdapter(adapter);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    ItemTouchHelper itemTouchHelper = new
        ItemTouchHelper(new SwipeToDeleteCallbackShoppingScreen(adapter));
    itemTouchHelper.attachToRecyclerView(recyclerView);
  }

  /**
   * resume timer
   *
   * @param v current view
   */
  public void resumeChronometer(View v) {
    if (!running) {
      chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
      chronometer.start();
      running = true;
    }
  }

  /**
   * pause timer
   *
   * @param v current view
   */
  public void pauseChronometer(View v) {
    if (running) {
      chronometer.stop();
      pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
      running = false;
    }
  }

}
