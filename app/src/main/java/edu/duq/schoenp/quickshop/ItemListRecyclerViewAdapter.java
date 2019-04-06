package edu.duq.schoenp.quickshop;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import de.hdodenhof.circleimageview.CircleImageView;
import java.util.ArrayList;
import java.util.List;

/**
 * ItemListRecyclerViewAdapter for displaying the initial list of items from the database
 */
public class ItemListRecyclerViewAdapter extends
    RecyclerView.Adapter<ItemListRecyclerViewAdapter.ViewHolder> implements Filterable {

  /**
   * log tag
   */
  private static final String TAG = "ItemListRecyclerViewAdapter";
  /**
   * items the user has added to their shopping list
   */
  private ArrayList<StoreItem> shoppingList = new ArrayList<>();
  /**
   * broadcast receiver for receiving the item a user deleted from their shopping cart. This method
   * is needed if the user deletes an item from their cart and they must return to this screen to
   * add another item, the list will stay accurate and the deleted item will remain deleted. Ensures
   * list is always consistent across all screens.
   */
  private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      // Get extra data included in the Intent
      String itemName = intent.getStringExtra("removedItemName");
      Log.d(TAG, "onReceive: item name is " + itemName);
      shoppingList.remove(getPosByName(itemName));
    }

  };
  /**
   * temporary count ensuring that the onReceive method is only called once per item delete. If not,
   * the method would try to delete an item that has already been deleted.
   */
  private int tempCount = 0;
  /**
   * List of store item objects received from the database
   */
  private ArrayList<StoreItem> mItems = new ArrayList<>();
  /**
   * Copy of the store items received from the database. Used for filtering the results.
   */
  private ArrayList<StoreItem> mImageNameCopy = new ArrayList<>();
  /**
   * Images associated with each item in list
   */
  private ArrayList<String> mImages = new ArrayList<>();
  /**
   * application context
   */
  private Context mContext;
  /**
   * Filters out items based on user filter string
   */
  private Filter itemFilter = new Filter() {
    /**
     * filters results based on user input
     * @param charSequence sequence of filter characters the user inputs
     * @return list of filtered items
     */
    @Override
    protected FilterResults performFiltering(CharSequence charSequence) {
      List<StoreItem> filteredList = new ArrayList<>();
      if (charSequence == null || charSequence.length() == 0) {
        filteredList.addAll(mImageNameCopy);
      } else {
        String filterPattern = charSequence.toString().toLowerCase().trim();
        //for all the items in the list if their name matches the user filter pattern then add to the filtered list
        for (StoreItem item : mImageNameCopy) {
          if (item.getName().toLowerCase().contains(filterPattern)) {
            filteredList.add(item);

          }
        }
      }

      FilterResults results = new FilterResults();
      results.values = filteredList;
      return results;
    }

    /**
     * Publish the results of the filtered items
     * @param charSequence sequence of filter characters the user inputs
     * @param filterResults results from filtering
     */
    @Override
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
      mItems.clear();
      mItems.addAll((List) filterResults.values);
      notifyDataSetChanged();
    }
  };

  /**
   * Constructor for the ItemListRecyclerViewAdapter
   *
   * @param context application context
   * @param items list of StoreItems for selected store
   * @param images images associated with each StoreItem
   */
  public ItemListRecyclerViewAdapter(Context context, ArrayList<StoreItem> items,
      ArrayList<String> images) {
    mItems = items;
    mImages = images;
    mContext = context;

  }

  /**
   * Executed when the viewHolder is created. Inflates the layout
   *
   * @param viewGroup viewGroup
   * @param i see superClass
   * @return inflated ViewHolder
   */
  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

    View view = LayoutInflater.from(viewGroup.getContext())
        .inflate(R.layout.layout_listitem, viewGroup, false);
    ViewHolder holder = new ViewHolder(view);
    mImageNameCopy = new ArrayList<>(
        mItems);//had to put this here beacause only at this point is the primary list non-null and a copy can be made

    return holder;
  }

  /**
   * Executed for each item in list. Sets the list to be displayed
   *
   * @param holder viewHolder
   * @param position current position of list
   */
  @SuppressLint("ResourceAsColor")
  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder,
      @SuppressLint("RecyclerView") final int position) {
    Log.d(TAG, "onBindViewHolder: called");
    Glide.with(mContext)
        .asBitmap()
        .load(mImages.get(position))
        .into(holder.circleImageView);
    Typeface tf = ResourcesCompat.getFont(mContext, R.font.libre_franklin);
    holder.displayItem.setTypeface(tf);
    holder.displayItem.setText(mItems.get(position).getName());
    holder.displayItem.setTextColor(Color.parseColor("#FFFF8902"));

    holder.parentLayout.setOnClickListener(new View.OnClickListener() {
      /**
       * On item click show the user what item was clicked and add to shopping list
       * @param view view
       */
      @Override
      public void onClick(View view) {
        Log.d(TAG, "onClick: clicked on " + mItems.get(position).getName());
        Toast.makeText(mContext, mItems.get(position).getName(), Toast.LENGTH_SHORT).show();
        shoppingList.add(mItems.get(position));
        Log.d(TAG, "onClick: item is " + mItems.get(position).toString());
      }
    });

  }

  /**
   * get total count of items in list
   *
   * @return size of list
   */
  @Override
  public int getItemCount() {
    return mItems.size();
  }

  /**
   * get itemFilter
   *
   * @return itemFilter
   */
  @Override
  public Filter getFilter() {
    return itemFilter;
  }

  /**
   * Creates intent for sending the user to the shoppingCart activity
   *
   * @param view view
   */
  public void goToCart(View view) {
    Intent intent = new Intent(view.getContext(), ShoppingCart.class);
    intent.putParcelableArrayListExtra("shopping list", shoppingList);

    //limit the amount of times the received message calls the onReceive method, can only call it once for it to work properly
    if (tempCount == 0) {
      tempCount++;
      LocalBroadcastManager.getInstance(mContext).registerReceiver(mMessageReceiver,
          new IntentFilter("custom-message2"));
    }
    tempCount++;

    mContext.startActivity(intent);
  }

  /**
   * get item position in list by item name
   *
   * @param itemName name of item to search for
   * @return position of item
   */
  private int getPosByName(String itemName) {

    for (int i = 0; i < shoppingList.size(); i++) {
      Log.d(TAG, "getPosByName: current item name is " + shoppingList.get(i).getName());
      if (shoppingList.get(i).getName().equals(itemName)) {
        return i;
      }
    }
    Log.e(TAG, "getPosByName: error finding item, item not found in shoppingList");
    return 0;
  }

  /**
   * ViewHolder for RecyclerView
   */
  public class ViewHolder extends RecyclerView.ViewHolder {

    CircleImageView circleImageView;
    TextView displayItem;
    RelativeLayout parentLayout;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      circleImageView = itemView.findViewById(R.id.circleImage);
      displayItem = itemView.findViewById(R.id.image_name);
      parentLayout = itemView.findViewById(R.id.pLayout);

    }

  }


}
