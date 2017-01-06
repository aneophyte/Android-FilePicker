package droidninja.filepicker.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

import droidninja.filepicker.PickerManager;
import droidninja.filepicker.R;
import droidninja.filepicker.models.Photo;
import droidninja.filepicker.views.SmoothCheckBox;

public class PhotoGridAdapter extends SelectableAdapter<PhotoGridAdapter.PhotoViewHolder, Photo>{

  private final Context context;
  private int imageSize;

  public PhotoGridAdapter(Context context, ArrayList<Photo> photos, ArrayList<String> selectedPaths)
  {
    super(photos, selectedPaths);
    this.context = context;
    setColumnNumber(context,3);
  }

  @Override
  public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View itemView = LayoutInflater.from(context).inflate(R.layout.item_photo_layout, parent, false);

    return new PhotoViewHolder(itemView);
  }

  @Override
  public void onBindViewHolder(final PhotoViewHolder holder, int position) {
    final Photo photo = getItems().get(position);

    Glide.with(holder.imageView.getContext()).load(new File(photo.getPath())).into(holder.imageView);

    holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if(PickerManager.getInstance().getMaxCount()==1)
          PickerManager.getInstance().add(photo);
        else
          if (holder.checkBox.isChecked() || PickerManager.getInstance().shouldAdd()) {
          holder.checkBox.setChecked(!holder.checkBox.isChecked(), true);
        }
      }
    });

    //in some cases, it will prevent unwanted situations
    holder.checkBox.setVisibility(View.GONE);
    holder.checkBox.setOnCheckedChangeListener(null);
    holder.checkBox.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if(holder.checkBox.isChecked() || PickerManager.getInstance().shouldAdd()) {
          holder.checkBox.setChecked(!holder.checkBox.isChecked(), true);
        }
      }
    });

    //if true, your checkbox will be selected, else unselected
    holder.checkBox.setChecked(isSelected(photo));

    holder.selectBg.setVisibility(isSelected(photo) ? View.VISIBLE : View.GONE);
    holder.checkBox.setVisibility(isSelected(photo) ? View.VISIBLE : View.GONE);

    holder.checkBox.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
        toggleSelection(photo);
        holder.selectBg.setVisibility(isChecked ? View.VISIBLE : View.GONE);

          if (isChecked)
          {
            holder.checkBox.setVisibility(View.VISIBLE);
            PickerManager.getInstance().add(photo);
          }
          else
          {
            holder.checkBox.setVisibility(View.GONE);
            PickerManager.getInstance().remove(photo);
          }
        }
      });
    }

  private void setColumnNumber(Context context, int columnNum) {
    WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    DisplayMetrics metrics = new DisplayMetrics();
    wm.getDefaultDisplay().getMetrics(metrics);
    int widthPixels = metrics.widthPixels;
    imageSize = widthPixels / columnNum;
  }

  @Override
  public int getItemCount() {
    return getItems().size()+1;
  }

  public static class PhotoViewHolder extends RecyclerView.ViewHolder {

      SmoothCheckBox checkBox;

      ImageView imageView;

      View selectBg;

    public PhotoViewHolder(View itemView) {
      super(itemView);
      checkBox = (SmoothCheckBox) itemView.findViewById(R.id.checkbox);
      imageView = (ImageView) itemView.findViewById(R.id.iv_photo);
      selectBg = itemView.findViewById(R.id.transparent_bg);
    }
  }
}
