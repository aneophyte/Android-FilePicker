package droidninja.filepicker.cursors;

import android.content.Context;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AudioFilesLoader extends CursorLoader {
    public static final String[] PROJECTION = new String[]{
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.SIZE
    };

    public static final String[] AUDIO_TYPES = new String[]{
            MimeTypeMap.getSingleton().getMimeTypeFromExtension("ac3"),
            MimeTypeMap.getSingleton().getMimeTypeFromExtension("mp3"),
            MimeTypeMap.getSingleton().getMimeTypeFromExtension("aac"),
            MimeTypeMap.getSingleton().getMimeTypeFromExtension("ogg"),
            MimeTypeMap.getSingleton().getMimeTypeFromExtension("m4a"),
            MimeTypeMap.getSingleton().getMimeTypeFromExtension("amr")
    };

    public AudioFilesLoader(Context context) {
        super(context);
        setUri(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        setProjection(PROJECTION);
        List<String> supportedAudioTypes = new ArrayList<>();
        for (String audioType : AUDIO_TYPES) {
            if (audioType != null) {
                supportedAudioTypes.add(audioType);
            }
        }
        String[] placeholders = new String[supportedAudioTypes.size()];
        Arrays.fill(placeholders, "?");
        setSelection(MediaStore.Audio.Media.MIME_TYPE + " IN " + TextUtils.concat("(", TextUtils.join(", ", placeholders), ")"));
        setSelectionArgs(supportedAudioTypes.toArray(new String[]{}));
        setSortOrder(MediaStore.Audio.Media.DATE_MODIFIED + " DESC");
    }
}
