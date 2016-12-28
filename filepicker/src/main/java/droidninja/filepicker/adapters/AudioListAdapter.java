package droidninja.filepicker.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import droidninja.filepicker.PickerManager;
import droidninja.filepicker.R;
import droidninja.filepicker.models.BaseFile;
import droidninja.filepicker.models.Audio;
import droidninja.filepicker.views.SmoothCheckBox;

/**
 * Created by droidNinja on 29/07/16.
 */
public class AudioListAdapter extends SelectableAdapter<AudioListAdapter.AudioViewHolder, Audio>{
    private final Context context;

    public AudioListAdapter(Context context, List<Audio> items, List<String> selectedPaths) {
        super(items, selectedPaths);
        this.context = context;
    }

    @Override
    public AudioViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_doc_layout, parent, false);

        return new AudioViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final AudioViewHolder holder, int position) {
        final Audio document = getItems().get(position);

        holder.fileNameTextView.setText(new File(document.getPath()).getName());
        holder.fileSizeTextView.setText(Formatter.formatShortFileSize(context, document.getSize()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PickerManager.getInstance().getMaxCount()==1)
                    PickerManager.getInstance().add(document);
                else
                    onItemClicked(document,holder);
            }
        });

        //in some cases, it will prevent unwanted situations
        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClicked(document,holder);
            }
        });

        //if true, your checkbox will be selected, else unselected
        holder.checkBox.setChecked(isSelected(document));

        holder.itemView.setBackgroundResource(isSelected(document)?R.color.bg_gray:android.R.color.white);
        holder.checkBox.setVisibility(isSelected(document) ? View.VISIBLE : View.GONE);

        holder.checkBox.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
                toggleSelection(document);
                holder.itemView.setBackgroundResource(isChecked?R.color.bg_gray:android.R.color.white);

            }
        });
    }

    private void onItemClicked(BaseFile baseFile, AudioViewHolder holder)
    {
        if(holder.checkBox.isChecked() || PickerManager.getInstance().shouldAdd()) {
            holder.checkBox.setChecked(!holder.checkBox.isChecked(), true);
        }

        if (holder.checkBox.isChecked())
        {
            holder.checkBox.setVisibility(View.VISIBLE);
            PickerManager.getInstance().add(baseFile);
        }
        else
        {
            holder.checkBox.setVisibility(View.GONE);
            PickerManager.getInstance().remove(baseFile);
        }
    }

    @Override
    public int getItemCount() {
        return getItems().size();
    }

    public static class AudioViewHolder extends RecyclerView.ViewHolder {
        SmoothCheckBox checkBox;

        ImageView imageView;

        TextView fileNameTextView;

        TextView fileSizeTextView;

        public AudioViewHolder(View itemView) {
            super(itemView);
            checkBox = (SmoothCheckBox) itemView.findViewById(R.id.checkbox);
            imageView = (ImageView) itemView.findViewById(R.id.file_iv);
            fileNameTextView = (TextView) itemView.findViewById(R.id.file_name_tv);
            fileSizeTextView = (TextView) itemView.findViewById(R.id.file_size_tv);
        }
    }
}
