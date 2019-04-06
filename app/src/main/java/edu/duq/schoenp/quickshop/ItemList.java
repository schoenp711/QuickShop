package edu.duq.schoenp.quickshop;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Displays the list of items retrieved from the database from the chosen store
 */
public class ItemList extends AppCompatActivity {

  /**
   * log tag
   */
  private static final String TAG = "ItemList";
  /**
   * List of StoreItem Objects from database
   */
  private ArrayList<StoreItem> mItems = new ArrayList<>();
  /**
   * list of URLs for each StoreItem object from database
   */
  private ArrayList<String> mImageUrls = new ArrayList<>();
  /**
   * The ItemListRecyclerViewAdapter for this recycler view
   */
  ItemListRecyclerViewAdapter adapter = new ItemListRecyclerViewAdapter(this, mItems, mImageUrls);

  /**
   * Create the current page
   *
   * @param savedInstanceState current saved instance state
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.item_list);
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayShowHomeEnabled(true);
      actionBar.setIcon(R.mipmap.ic_launcher2);
    }
    Intent intent = getIntent();
    //the store name is passed as a string extra from the intent that started this activity
    String store = intent.getStringExtra(PickStore.EXTRA_MESSAGE);
    Log.d(TAG, "onCreate: started");
    downloadJSON();
  }

  /**
   * Initializes the recycler view for this list
   */
  private void initRecyclerView() {
    Log.d(TAG, "initRecyclerView: init recyclerView");
    RecyclerView recyclerView = findViewById(R.id.recycler_view);
    recyclerView.setAdapter(adapter);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));

  }

  /**
   * calls the goToCart method in ItemListRecyclerViewAdapter
   *
   * @param view current view
   */
  public void adapterGoToCart(View view) {
    adapter.goToCart(view);
  }

  /**
   * Downloads the JSON created from the store_service.php script
   */

  private void downloadJSON() {
    Log.d(TAG, "downloadJSON: in download JSON method");
    @SuppressLint("StaticFieldLeak")
    class DownloadJSON extends AsyncTask<Void, Void, String> {

      @Override
      protected void onPreExecute() {
        super.onPreExecute();
      }


      @Override
      protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
          loadIntoListView(s);
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }

      @Override
      protected String doInBackground(Void... voids) {
        try {
          //server that hosts the database where the items are located
          //store_service.php is a php script on the server. The script queries the database and returns the items as a JSON object
          URL url = new URL("http://35.188.254.160/store_service.php");
          HttpURLConnection con = (HttpURLConnection) url.openConnection();
          StringBuilder sb = new StringBuilder();
          BufferedReader bufferedReader = new BufferedReader(
              new InputStreamReader(con.getInputStream()));
          String json;
          while ((json = bufferedReader.readLine()) != null) {
            sb.append(json).append("\n");
          }
          return sb.toString().trim();
        } catch (Exception e) {
          return null;
        }
      }
    }
    DownloadJSON getJSON = new DownloadJSON();
    getJSON.execute();
  }

  /**
   * Parses the JSON string and constructs StoreItem objects for each item returned by the database.
   * Inserts each item in the RecyclerView list
   *
   * @param json JSON string returned from database
   * @throws JSONException Error in JSON parsing
   */
  private void loadIntoListView(String json) throws JSONException {
    Log.d(TAG, "loadIntoListView: json is " + json);
    JSONArray jsonArray = new JSONArray(json);

    String[] items = new String[jsonArray.length()];
    for (int i = 0; i < jsonArray.length(); i++) {
      JSONObject obj = jsonArray.getJSONObject(i);
      items[i] = obj.getString("item") + " " + obj.getString("location");
      StoreItem item = new StoreItem();
      item.setName(obj.getString("item"));
      item.setLocation(obj.getString("location"));
      Log.d(TAG, "loadIntoListView: item as constructed into object it " + item.toString());
      mItems.add(item);
      String imageURL = "data:image/png;base64,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";
      mImageUrls.add(imageURL);
      Log.d(TAG, "loadIntoListView: " + items[i].toString());
    }
    initRecyclerView();
  }

  /**
   * Used to search through the entire list of items if the user wants to filter results.
   *
   * @param menu Menubar where the search icon is located
   * @return Returns boolean. Return true for the menu to be displayed; if return false it will not
   * be shown.
   */
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.example_menu, menu);

    MenuItem searchItem = menu.findItem(R.id.action_search);
    SearchView searchView = (SearchView) searchItem.getActionView();

    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
      @Override
      public boolean onQueryTextSubmit(String s) {
        return false;
      }

      @Override
      public boolean onQueryTextChange(String s) {
        adapter.getFilter().filter(s);
        return false;
      }
    });
    return true;
  }


}
