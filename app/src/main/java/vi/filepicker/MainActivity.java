package vi.filepicker;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import droidninja.filepicker.models.Audio;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MainActivity extends AppCompatActivity {

    private int MAX_ATTACHMENT_COUNT = 10;
    private ArrayList<String> photoPaths = new ArrayList<>();
    private ArrayList<String> docPaths = new ArrayList<>();
    private List<String> audioPaths = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void pickPhotoClicked(View view) {
        MainActivityPermissionsDispatcher.onPickPhotoWithCheck(this);
    }

    public void pickDocClicked(View view) {
        MainActivityPermissionsDispatcher.onPickDocWithCheck(this);
    }

    public void pickAudioClicked(View view) {
        MainActivityPermissionsDispatcher.onPickAudioWithCheck(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case FilePickerConst.REQUEST_CODE_PHOTO:
                if(resultCode== Activity.RESULT_OK && data!=null)
                {
                    photoPaths = new ArrayList<>();
                    photoPaths.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_PHOTOS));

                }
                break;

            case FilePickerConst.REQUEST_CODE_DOC:
                if(resultCode== Activity.RESULT_OK && data!=null)
                {
                    docPaths = new ArrayList<>();
                    docPaths.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS));
                }
                break;

            case FilePickerConst.REQUEST_CODE_AUDIO:
                if(resultCode== Activity.RESULT_OK && data!=null)
                {
                    audioPaths.clear();
                    List<Audio> audioList = data.getParcelableArrayListExtra(FilePickerConst.KEY_SELECTED_AUDIO);
                    for (Audio audio : audioList) {
                        audioPaths.add(audio.getPath());
                    }
                }
        }

        addThemToView(photoPaths,docPaths, audioPaths);
    }

    private void addThemToView(ArrayList<String> imagePaths, ArrayList<String> docPaths, List<String> audioPaths) {
        ArrayList<String> filePaths = new ArrayList<>();
        if(imagePaths!=null)
            filePaths.addAll(imagePaths);

        if(docPaths!=null)
            filePaths.addAll(docPaths);

        filePaths.addAll(audioPaths);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        if(recyclerView!=null) {
            StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, OrientationHelper.VERTICAL);
            layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
            recyclerView.setLayoutManager(layoutManager);

            ImageAdapter imageAdapter = new ImageAdapter(this, filePaths);

            recyclerView.setAdapter(imageAdapter);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
        }

        Toast.makeText(this, "Num of files selected: "+ filePaths.size(), Toast.LENGTH_SHORT).show();
    }

    @NeedsPermission({Manifest.permission.READ_EXTERNAL_STORAGE})
    public void onPickPhoto() {
        int maxCount = MAX_ATTACHMENT_COUNT-docPaths.size() - audioPaths.size();
        if((getTotalItemsSize())==MAX_ATTACHMENT_COUNT)
            Toast.makeText(this, "Cannot select more than " + MAX_ATTACHMENT_COUNT + " items", Toast.LENGTH_SHORT).show();
        else
            FilePickerBuilder.getInstance().setMaxCount(maxCount)
                    .setSelectedFiles(photoPaths)
                    .setActivityTheme(R.style.FilePickerTheme)
                    .pickPhoto(this);
    }

    @NeedsPermission({Manifest.permission.READ_EXTERNAL_STORAGE})
    public void onPickDoc() {
        int maxCount = MAX_ATTACHMENT_COUNT-photoPaths.size() - audioPaths.size();
        if((getTotalItemsSize())==MAX_ATTACHMENT_COUNT)
            Toast.makeText(this, "Cannot select more than " + MAX_ATTACHMENT_COUNT + " items", Toast.LENGTH_SHORT).show();
        else
            FilePickerBuilder.getInstance().setMaxCount(maxCount)
                    .setSelectedFiles(docPaths)
                    .setActivityTheme(R.style.FilePickerTheme)
                    .pickDocument(this);
    }

    @NeedsPermission({Manifest.permission.READ_EXTERNAL_STORAGE})
    public void onPickAudio() {
        int maxCount = MAX_ATTACHMENT_COUNT-photoPaths.size() - docPaths.size();
        if(getTotalItemsSize() ==MAX_ATTACHMENT_COUNT)
            Toast.makeText(this, "Cannot select more than " + MAX_ATTACHMENT_COUNT + " items", Toast.LENGTH_SHORT).show();
        else
        FilePickerBuilder.getInstance().setMaxCount(maxCount)
                .setSelectedFiles(docPaths)
                .setActivityTheme(R.style.FilePickerTheme)
                .pickAudio(this);
    }

    private int getTotalItemsSize() {
        return docPaths.size()+photoPaths.size() + audioPaths.size();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this,requestCode,grantResults);
    }

    public void onOpenFragmentClicked(View view) {
        Intent intent = new Intent(this, FragmentActivity.class);
        startActivity(intent);
    }
}
