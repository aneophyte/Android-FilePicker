package droidninja.filepicker.cursors.loadercallbacks;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import droidninja.filepicker.cursors.AudioFilesLoader;
import droidninja.filepicker.models.Audio;

import static android.provider.BaseColumns._ID;
import static android.provider.MediaStore.MediaColumns.DATA;
import static android.provider.MediaStore.MediaColumns.DISPLAY_NAME;

public class AudioFilesLoaderCallbacks implements LoaderManager.LoaderCallbacks<Cursor> {
    private WeakReference<Context> context;
    private FileResultCallback<Audio> resultCallback;

    public AudioFilesLoaderCallbacks(Context context, FileResultCallback<Audio> resultCallback) {
        this.context = new WeakReference<>(context);
        this.resultCallback = resultCallback;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AudioFilesLoader(context.get());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data == null) return;
        List<Audio> audioFiles = new ArrayList<>();

        while (data.moveToNext()) {
            int imageId = data.getInt(data.getColumnIndexOrThrow(_ID));
            String path = data.getString(data.getColumnIndexOrThrow(DATA));
            String fileName = data.getString(data.getColumnIndexOrThrow(DISPLAY_NAME));
            long duration = data.getLong(data.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
            long size = data.getLong(data.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
            audioFiles.add(new Audio(imageId, fileName, path, duration, size));
        }

        if (resultCallback != null) {
            resultCallback.onResultCallback(audioFiles);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}