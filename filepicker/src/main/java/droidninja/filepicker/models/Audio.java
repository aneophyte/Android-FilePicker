package droidninja.filepicker.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Audio extends BaseFile implements Parcelable {
    private final long duration;
    private final long size;

    public Audio(int id, String name, String path, long duration, long size) {
        super(id, name, path);
        this.duration = duration;
        this.size = size;
    }

    public long getDuration() {
        return duration;
    }

    public long getSize() {
        return size;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Audio audio = (Audio) o;

        if (duration != audio.duration) return false;
        return size == audio.size;
    }

    @Override
    public int hashCode() {
        int result = (int) (duration ^ (duration >>> 32));
        result = 31 * result + (int) (size ^ (size >>> 32));
        return result;
    }

    protected Audio(Parcel in) {
        super(in);
        duration = in.readLong();
        size = in.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeLong(duration);
        dest.writeLong(size);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Audio> CREATOR = new Parcelable.Creator<Audio>() {
        @Override
        public Audio createFromParcel(Parcel in) {
            return new Audio(in);
        }

        @Override
        public Audio[] newArray(int size) {
            return new Audio[size];
        }
    };
}
