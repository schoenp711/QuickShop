package edu.duq.schoenp.quickshop;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import de.hdodenhof.circleimageview.CircleImageView;
import java.util.ArrayList;
import java.util.Collections;

/**
 * RecyvlerViewAdapter for the users shopping cart
 */
public class CartRecyclerViewAdapter extends
    RecyclerView.Adapter<CartRecyclerViewAdapter.ViewHolder> {

  /**
   * log tag
   */
  private static final String TAG = "CartRecyclerViewAdapter";
  /**
   * user's shopping list
   */
  private ArrayList<StoreItem> shoppingList = new ArrayList<>();
  /**
   * images that go with each item
   */
  private ArrayList<String> mImages = new ArrayList<>();
  /**
   * application context
   */
  private Context mContext;

  /**
   * Constructor for CartRecyvlerViewAdapter
   *
   * @param context context
   * @param receivedList received shopping list
   * @param images images for each item
   */
  public CartRecyclerViewAdapter(Context context, ArrayList<StoreItem> receivedList,
      ArrayList<String> images) {
    shoppingList = receivedList;
    mImages = images;
    mContext = context;

  }

  /**
   * Inflate recyclerView
   *
   * @param viewGroup viewGroup
   * @param i i
   * @return viewHolder for recyclerView
   */
  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    View view = LayoutInflater.from(viewGroup.getContext())
        .inflate(R.layout.layout_listitem, viewGroup, false);

    return new ViewHolder(view);
  }

  /**
   * Populates the recycler view
   *
   * @param holder view holder
   * @param position current position in list
   */
  @SuppressLint("ResourceAsColor")
  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
    Log.d(TAG, "onBindViewHolder: called");
    Glide.with(mContext)
        .asBitmap()
        .load(mImages.get(position))
        .into(holder.circleImageView);

    Typeface tf = ResourcesCompat.getFont(mContext, R.font.libre_franklin);
    holder.imageName.setTypeface(tf);
    holder.imageName.setText(shoppingList.get(position).getName());
    holder.imageName.setTextColor(Color.parseColor("#FFFF8902"));

  }

  /**
   * get number of items in list
   *
   * @return number of items in list
   */
  @Override
  public int getItemCount() {
    return shoppingList.size();
  }

  /**
   * Delete an item from the list
   *
   * @param position position of the item to be removed
   */
  public void deleteItem(int position) {
    String mRecentlyDeletedItem = shoppingList.get(position).getName();
    shoppingList.remove(position);
    notifyItemRemoved(position);
    //Send broadcast containing the item that is to be deleted
    Intent intent = new Intent("custom-message2");
    intent.putExtra("removedItemName", mRecentlyDeletedItem);
    Log.d(TAG, "deleteItem: sending broadcast ");
    LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

  }

  /**
   * Called when user is done editing/view their shopping list brings to shopping activity
   *
   * @param view current view
   */
  public void goToShop(View view) {
    Intent intent = new Intent(view.getContext(), Shopping.class);
    //sort the list of items by location 0-9 A-Z
    Collections.sort(shoppingList, new StoreItem());
    intent.putParcelableArrayListExtra("shopping list", optimizePath(shoppingList));
    mContext.startActivity(intent);
  }

  /**
   * Compares the location of each item and determines its place in the final list based on what
   * aisle number it is in, and what half of the aisle it is in (A or B)
   *
   * @param items current sorted user shopping list
   * @return optimized user shopping list
   */
  private ArrayList<StoreItem> optimizePath(ArrayList<StoreItem> items) {
    /* We must look at the current item and the next two items in the list to determine
       if we need to skip over an item and move it further down the list.
       Ex. if we have item locations 2A, 2B, 3A, 3B. The optimal path is 2A,2B,3B,3A. We had
       to skip over 3A and move it further down the list since we were already at B from the
       previous aisle. */
    for (int i = 0; i < items.size(); i++) {
      if ((i + 2) >= items.size()) {
        return items;
      } else {
        StoreItem currItem = items.get(i);
        StoreItem nextItem = items.get(i + 1);
        StoreItem nextNextItem = items.get(i + 2);
        char currentItemAisleLocation = currItem.getLocation().charAt(2);
        char nextItemAisleLocation = nextItem.getLocation().charAt(2);
        char nextNextItemAisleLocation = nextNextItem.getLocation().charAt(2);

        if (currentItemAisleLocation == 'B') {
          if (nextItemAisleLocation == 'A') {
            if (nextNextItemAisleLocation == 'B') {
              Collections.swap(items, i + 2, i + 1);
            }
          }
        }


      }

    }

    return null;
  }

  /**
   * Creates the view holder for this recycler view, finds the XML elements
   */
  public class ViewHolder extends RecyclerView.ViewHolder {

    CircleImageView circleImageView;
    TextView imageName;
    RelativeLayout parentLayout;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      circleImageView = itemView.findViewById(R.id.circleImage);
      imageName = itemView.findViewById(R.id.image_name);
      parentLayout = itemView.findViewById(R.id.pLayout);
    }
  }


}