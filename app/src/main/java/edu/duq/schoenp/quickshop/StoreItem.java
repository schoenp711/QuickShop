package edu.duq.schoenp.quickshop;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import java.util.Comparator;
import java.util.Objects;

/**
 * Store item object class
 */
public class StoreItem implements Parcelable, Comparator<StoreItem> {

  /**
   * Makes StoreItem objects parcelable. Makes it so they can be sent via intents and broadcasts
   */
  public static final Parcelable.Creator<StoreItem> CREATOR = new Parcelable.Creator<StoreItem>() {
    public StoreItem createFromParcel(Parcel in) {
      return new StoreItem(in);
    }

    public StoreItem[] newArray(int size) {
      return new StoreItem[size];
    }
  };
  /**
   * Name of item
   */
  public String name;
  /**
   * Location of item
   */
  private String location;

  /**
   * Default constructor required for calls to DataSnapshot.getValue(StoreItem.class)
   */
  public StoreItem() {
  }

  /**
   * StoreItem constructor
   *
   * @param name item name
   * @param location item location
   */
  public StoreItem(String name, String location) {
    this.name = name;
    this.location = location;
  }

  /**
   * Parcelable constructor
   *
   * @param in parcel received
   */
  private StoreItem(Parcel in) {
    name = in.readString();
    location = in.readString();
  }

  /**
   * get item name
   *
   * @return item name
   */
  public String getName() {
    return name;
  }

  /**
   * set item name
   *
   * @param name item name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * get item location
   *
   * @return location of item
   */
  public String getLocation() {
    return location;
  }

  /**
   * set item location
   *
   * @param location item location
   */
  public void setLocation(String location) {
    this.location = location;
  }

  /**
   * StoreItem toString
   *
   * @return StoreItem as string of name and location
   */
  @NonNull
  @Override
  public String toString() {
    return ("item name is: " + this.getName() + " item location is: " + this.getLocation());
  }

  /**
   * Equals
   *
   * @param o StoreItem object
   * @return true if objects have same name and location, false otherwise
   */
  @Override
  public boolean equals(Object o) {

    if (o == this) {
      return true;
    }
    if (!(o instanceof StoreItem)) {
      return false;
    }
    StoreItem storeItem = (StoreItem) o;
    return location.equals(storeItem.location) &&
        Objects.equals(name, storeItem.name);
  }

  /**
   * Hash StoreItem
   *
   * @return hash of StoreItem
   */
  @Override
  public int hashCode() {
    return Objects.hash(name, location);
  }

  /**
   * Unused.
   *
   * @return 0
   */
  @Override
  public int describeContents() {
    return 0;
  }

  /**
   * Transform StoreItem into parcel
   *
   * @param parcel parcel
   * @param i i
   */
  @Override
  public void writeToParcel(Parcel parcel, int i) {
    parcel.writeString(this.name);
    parcel.writeString(this.location);
  }

  /**
   * Compare to StoreItems based on item location
   *
   * @param item1 first item to compare
   * @param item2 second item to compare
   * @return -1 if item1 location is less than item2 location, 0 if equal, 1 if item1 location is
   * greater than item 2 location
   */
  @Override
  public int compare(StoreItem item1, StoreItem item2) {
    Integer n1 = Integer.parseInt(item1.getLocation().substring(0, 2));
    Integer n2 = Integer.parseInt(item2.getLocation().substring(0, 2));

    return n1.compareTo(n2);

  }


}
