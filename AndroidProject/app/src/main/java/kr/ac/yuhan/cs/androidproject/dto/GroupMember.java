package kr.ac.yuhan.cs.androidproject.dto;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class GroupMember implements Parcelable {

    @SerializedName("id")
    private long userId;

    @SerializedName("username")
    private String userName;

    public GroupMember() {}

    public GroupMember(long userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }

    protected GroupMember(Parcel in) {
        userId = in.readLong();
        userName = in.readString();
    }

    public static final Creator<GroupMember> CREATOR = new Creator<GroupMember>() {
        @Override
        public GroupMember createFromParcel(Parcel in) {
            return new GroupMember(in);
        }

        @Override
        public GroupMember[] newArray(int size) {
            return new GroupMember[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeLong(userId);
        parcel.writeString(userName);
    }

    public long getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }
}
