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

/**
 * Recycler view adapter for the shopping activity
 */
public class ShoppingRecyclerViewAdapter extends
    RecyclerView.Adapter<ShoppingRecyclerViewAdapter.ViewHolder> {

  /**
   * log tag
   */
  private static final String TAG = "ShoppingRecyclerViewAdapter";
  /**
   * shopping list received from cart activity
   */
  private ArrayList<StoreItem> shoppingList = new ArrayList<>();
  /**
   * images for each item
   */
  private ArrayList<String> mImages = new ArrayList<>();
  /**
   * application context
   */
  private Context mContext;

  /**
   * Constructor for adapter
   *
   * @param context application context
   * @param receivedList received shopping list
   * @param images images for each item
   */
  public ShoppingRecyclerViewAdapter(Context context, ArrayList<StoreItem> receivedList,
      ArrayList<String> images) {
    shoppingList = receivedList;
    mImages = images;
    mContext = context;

  }

  /**
   * inflate view
   *
   * @param viewGroup viewGroup
   * @param i i
   * @return new ViewHolder
   */
  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    View view = LayoutInflater.from(viewGroup.getContext())
        .inflate(R.layout.layout_listitem, viewGroup, false);

    return new ViewHolder(view);
  }

  /**
   * populate recycler view with received shopping list
   *
   * @param holder view holder
   * @param position current position in list
   */
  @SuppressLint("SetTextI18n")
  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
    Log.d(TAG, "onBindViewHolder: called");
    Glide.with(mContext)
        .asBitmap()
        .load(mImages.get(position))
        .into(holder.circleImageView);
    Typeface tf = ResourcesCompat.getFont(mContext, R.font.libre_franklin);
    holder.imageName.setTypeface(tf);
    holder.imageName.setText(
        shoppingList.get(position).getName() + "\n" + shoppingList.get(position).getLocation());
    holder.imageName.setTextColor(Color.parseColor("#FFFF8902"));

  }

  /**
   * get total item count
   *
   * @return total item count
   */
  @Override
  public int getItemCount() {
    return shoppingList.size();
  }

  /**
   * send broadcast that an item has been deleted from the list so the displayed list can be
   * updated
   *
   * @param position position of item deleted
   */
  public void deleteItem(int position) {
    shoppingList.remove(position);
    notifyItemRemoved(position);
    if (shoppingList.size() == 0) {
      final String DONE = "DONE";
      Intent intent = new Intent("custom-message");
      intent.putExtra("done", DONE);
      Log.d(TAG, "deleteItem: sending broadcast ");
      LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);


    }
  }

  /**
   * view holder constructor
   */
  public class ViewHolder extends RecyclerView.ViewHolder {

    CircleImageView circleImageView;
    TextView imageName;
    RelativeLayout parentLayout;

    /**
     * get XML attributes
     *
     * @param itemView itemView
     */
    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      circleImageView = itemView.findViewById(R.id.circleImage);
      imageName = itemView.findViewById(R.id.image_name);
      parentLayout = itemView.findViewById(R.id.pLayout);
    }
  }


}