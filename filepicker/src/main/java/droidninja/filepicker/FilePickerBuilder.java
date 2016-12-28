package droidninja.filepicker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.util.ArrayList;

/**
 * Created by droidNinja on 29/07/16.
 */
public class FilePickerBuilder {

    private final Bundle mPickerOptionsBundle;

    public FilePickerBuilder()
    {
        mPickerOptionsBundle = new Bundle();
    }

    public static FilePickerBuilder getInstance()
    {
        return new FilePickerBuilder();
    }

    public FilePickerBuilder setMaxCount(int maxCount)
    {
        PickerManager.getInstance().setMaxCount(maxCount);
        return this;
    }

    public FilePickerBuilder setActivityTheme(int theme)
    {
        PickerManager.getInstance().setTheme(theme);
        return this;
    }

    public FilePickerBuilder setSelectedFiles(ArrayList<String> selectedPhotos)
    {
        mPickerOptionsBundle.putStringArrayList(FilePickerConst.KEY_SELECTED_PHOTOS, selectedPhotos);
        return this;
    }

    public void pickPhoto(Activity context)
    {
       mPickerOptionsBundle.putInt(FilePickerConst.EXTRA_PICKER_TYPE,FilePickerConst.PHOTO_PICKER);
        start(context,FilePickerConst.PHOTO_PICKER);
    }

    public void pickPhoto(Fragment context)
    {
        mPickerOptionsBundle.putInt(FilePickerConst.EXTRA_PICKER_TYPE,FilePickerConst.PHOTO_PICKER);
        start(context,FilePickerConst.PHOTO_PICKER);
    }

    public void pickDocument(Activity context)
    {
        mPickerOptionsBundle.putInt(FilePickerConst.EXTRA_PICKER_TYPE,FilePickerConst.DOC_PICKER);
        start(context,FilePickerConst.DOC_PICKER);
    }

    public void pickDocument(Fragment context)
    {
        mPickerOptionsBundle.putInt(FilePickerConst.EXTRA_PICKER_TYPE,FilePickerConst.DOC_PICKER);
        start(context,FilePickerConst.DOC_PICKER);
    }

    public void pickAudio(Activity context) {
        int pickerType = FilePickerConst.AUDIO_PICKER;
        mPickerOptionsBundle.putInt(FilePickerConst.EXTRA_PICKER_TYPE, pickerType);
        start(context, pickerType);
    }

    public void pickAudio(Fragment context) {
        int pickerType = FilePickerConst.AUDIO_PICKER;
        mPickerOptionsBundle.putInt(FilePickerConst.EXTRA_PICKER_TYPE, pickerType);
        start(context, pickerType);
    }

    private void start(Activity context, int pickerType)
    {
        Intent intent = new Intent(context, FilePickerActivity.class);
        intent.putExtras(mPickerOptionsBundle);

        if(pickerType==FilePickerConst.PHOTO_PICKER)
            context.startActivityForResult(intent,FilePickerConst.REQUEST_CODE_PHOTO);
        else if (pickerType == FilePickerConst.AUDIO_PICKER)
            context.startActivityForResult(intent, FilePickerConst.REQUEST_CODE_AUDIO);
        else
            context.startActivityForResult(intent,FilePickerConst.REQUEST_CODE_DOC);
    }

    private void start(Fragment fragment, int pickerType)
    {
        Intent intent = new Intent(fragment.getActivity(), FilePickerActivity.class);
        intent.putExtras(mPickerOptionsBundle);
        if(pickerType==FilePickerConst.PHOTO_PICKER)
            fragment.startActivityForResult(intent,FilePickerConst.REQUEST_CODE_PHOTO);
        else
            fragment.startActivityForResult(intent,FilePickerConst.REQUEST_CODE_DOC);
    }

}
