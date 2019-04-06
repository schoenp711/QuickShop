package edu.duq.schoenp.quickshop;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Swipe to delete items from user's shopping cart
 */
public class SwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback {

  /**
   * CartRecyclerViewAdapter
   */
  private CartRecyclerViewAdapter mAdapter;

  /**
   * Set swipe to delete as both right and left swipe
   *
   * @param adapter CartRecyclerViewAdapter
   */
  public SwipeToDeleteCallback(CartRecyclerViewAdapter adapter) {
    super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
    mAdapter = adapter;
  }

  /**
   * On item moved, never called
   *
   * @param recyclerView recyclerView
   * @param viewHolder viewHolder
   * @param viewHolder1 viewHolder1
   * @return always false
   */
  @Override
  public boolean onMove(@NonNull RecyclerView recyclerView,
      @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
    return false;
  }

  /**
   * Called when item is swiped. Deletes item from list
   *
   * @param viewHolder RecyclerView ViewHolder
   * @param direction which direction was swiped
   */
  @Override
  public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
    int position = viewHolder.getAdapterPosition();
    mAdapter.deleteItem(position);
  }
}