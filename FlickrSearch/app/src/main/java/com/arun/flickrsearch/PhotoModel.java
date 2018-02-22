package com.arun.flickrsearch;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.TextView;

/**
 * Created by arun on 22/2/18.
 */

class PhotoModel implements Parcelable{
    private String title;
    private String owner;
    private String id;

    protected PhotoModel() {

    }

    protected PhotoModel(Parcel in) {
        title = in.readString();
        owner = in.readString();
        id = in.readString();
    }

    public static final Creator<PhotoModel> CREATOR = new Creator<PhotoModel>() {
        @Override
        public PhotoModel createFromParcel(Parcel in) {
            return new PhotoModel(in);
        }

        @Override
        public PhotoModel[] newArray(int size) {
            return new PhotoModel[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public String getOwner() {
        return owner;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getId() {
        return id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(owner);
        parcel.writeString(id);
    }
}
