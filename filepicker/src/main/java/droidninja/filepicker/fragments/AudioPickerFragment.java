package droidninja.filepicker.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import droidninja.filepicker.R;
import droidninja.filepicker.adapters.AudioListAdapter;
import droidninja.filepicker.cursors.loadercallbacks.AudioFilesLoaderCallbacks;
import droidninja.filepicker.cursors.loadercallbacks.FileResultCallback;
import droidninja.filepicker.models.Audio;


public class AudioPickerFragment extends BaseFragment {
    public static final String SELECTED_PATHS = "SelectedPaths";
    RecyclerView recyclerView;
    TextView emptyView;
    private ArrayList<String> selectedPaths;

    public AudioPickerFragment() {
        // Required empty public constructor
    }

    public static AudioPickerFragment newInstance(ArrayList<String> selectedPaths) {
        AudioPickerFragment audioPickerFragment = new AudioPickerFragment();
        Bundle arguments = new Bundle();
        arguments.putStringArrayList(SELECTED_PATHS, selectedPaths);
        audioPickerFragment.setArguments(arguments);
        return audioPickerFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_files, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.selectedPaths = getArguments().getStringArrayList(SELECTED_PATHS);
        setView(view);
        initView();
        getActivity().getSupportLoaderManager().initLoader(0, null, new AudioFilesLoaderCallbacks(getContext(), new FileResultCallback<Audio>() {
            @Override
            public void onResultCallback(List<Audio> files) {
                updateList(files);
            }
        }));
    }

    private void setView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        emptyView = (TextView) view.findViewById(R.id.empty_view);
    }

    private void initView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setVisibility(View.GONE);
    }

    public void updateList(List<Audio> dirs) {
        if (getView() == null)
            return;

        if (dirs.size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);

            AudioListAdapter audioListAdapter = (AudioListAdapter) recyclerView.getAdapter();
            if (audioListAdapter == null) {
                audioListAdapter = new AudioListAdapter(getActivity(), dirs, selectedPaths);

                recyclerView.setAdapter(audioListAdapter);
            } else {
                audioListAdapter.setData(dirs);
                audioListAdapter.notifyDataSetChanged();
            }
        } else {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }
    }
}
