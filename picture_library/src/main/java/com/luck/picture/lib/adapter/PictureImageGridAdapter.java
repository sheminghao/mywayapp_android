package com.luck.picture.lib.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.media.SoundPool;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.luck.picture.lib.R;
import com.luck.picture.lib.model.FunctionConfig;
import com.yalantis.ucrop.dialog.OptAnimationLoader;
import com.yalantis.ucrop.entity.LocalMedia;
import com.yalantis.ucrop.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dee on 15/11/19.
 */
public class PictureImageGridAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int TYPE_CAMERA = 1;
    public static final int TYPE_PICTURE = 2;
    private Context context;
    private boolean showCamera = true;
    private OnPhotoSelectChangedListener imageSelectChangedListener;
    private int maxSelectNum;
    private List<LocalMedia> images = new ArrayList<LocalMedia>();
    private List<LocalMedia> selectImages = new ArrayList<LocalMedia>();
    private boolean enablePreview;
    private int selectMode = FunctionConfig.MODE_MULTIPLE;
    private boolean enablePreviewVideo = false;
    private int cb_drawable;
    private boolean is_checked_num;
    private int type;
    private boolean isGif;
    private boolean clickVideo;
    private SoundPool soundPool;
    private int soundID;
    private Animation animation;

    public PictureImageGridAdapter(Context context, boolean isGif, boolean showCamera, int maxSelectNum, int mode, boolean enablePreview, boolean enablePreviewVideo, int cb_drawable, boolean is_checked_num, int type, boolean clickVideo, SoundPool soundPool, int soundID) {
        this.context = context;
        this.selectMode = mode;
        this.showCamera = showCamera;
        this.maxSelectNum = maxSelectNum;
        this.enablePreview = enablePreview;
        this.enablePreviewVideo = enablePreviewVideo;
        this.cb_drawable = cb_drawable;
        this.is_checked_num = is_checked_num;
        this.type = type;
        this.isGif = isGif;
        this.clickVideo = clickVideo;
        this.soundPool = soundPool;
        this.soundID = soundID;
        animation = OptAnimationLoader.loadAnimation(context, R.anim.modal_in);
    }

    public void bindImagesData(List<LocalMedia> images) {
        this.images = images;
        notifyDataSetChanged();
    }

    /**
     * 播放点击声音
     */
    private void playSound() {
        if (clickVideo) {
            soundPool.play(
                    soundID,
                    0.1f,   //左耳道音量【0~1】
                    0.5f,   //右耳道音量【0~1】
                    0,     //播放优先级【0表示最低优先级】
                    1,     //循环模式【0表示循环一次，-1表示一直循环，其他表示数字+1表示当前数字对应的循环次数】
                    1     //播放速度【1是正常，范围从0~2】
            );
        }
    }

    public void bindSelectImages(List<LocalMedia> images) {
        this.selectImages = images;
        subSelectPosition();
        notifyDataSetChanged();
        if (imageSelectChangedListener != null) {
            imageSelectChangedListener.onChange(selectImages);
        }
    }

    public List<LocalMedia> getSelectedImages() {
        return selectImages;
    }

    public List<LocalMedia> getImages() {
        return images;
    }

    @Override
    public int getItemViewType(int position) {
        if (showCamera && position == 0) {
            return TYPE_CAMERA;
        } else {
            return TYPE_PICTURE;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_CAMERA) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.picture_item_camera, parent, false);
            return new HeaderViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.picture_image_grid_item, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (getItemViewType(position) == TYPE_CAMERA) {
            HeaderViewHolder headerHolder = (HeaderViewHolder) holder;
            headerHolder.headerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (imageSelectChangedListener != null) {
                        imageSelectChangedListener.onTakePhoto();
                    }
                }
            });
        } else {
            final ViewHolder contentHolder = (ViewHolder) holder;
            final LocalMedia image = images.get(showCamera ? position - 1 : position);
            image.position = contentHolder.getAdapterPosition();
            String path = image.getPath();
            final int type = image.getType();
            contentHolder.check.setBackgroundResource(cb_drawable);
            if (selectMode == FunctionConfig.MODE_SINGLE) {
                contentHolder.ll_check.setVisibility(View.GONE);
            } else {
                contentHolder.ll_check.setVisibility(View.VISIBLE);
            }
            if (is_checked_num) {
                notifyCheckChanged(contentHolder, image);
            }

            selectImage(contentHolder, isSelected(image), false);

            if (type == FunctionConfig.TYPE_VIDEO) {
                Glide.with(context).load(path).diskCacheStrategy(DiskCacheStrategy.RESULT).crossFade()
                        .centerCrop().override(150, 150).into(contentHolder.picture);
                long duration = image.getDuration();
                if (contentHolder.tv_duration.getVisibility() == View.GONE) {
                    contentHolder.tv_duration.setVisibility(View.VISIBLE);
                }
                contentHolder.tv_duration.setText(timeParse(duration));

            } else {
                DiskCacheStrategy result;
                if (isGif) {
                    result = DiskCacheStrategy.SOURCE;
                } else {
                    result = DiskCacheStrategy.RESULT;
                }
                Glide.with(holder.itemView.getContext())
                        .load(path)
                        .placeholder(R.drawable.image_placeholder)
                        .diskCacheStrategy(result)
                        .crossFade()
                        .centerCrop()
                        .override(150, 150)
                        .into(contentHolder.picture);
                if (contentHolder.tv_duration.getVisibility() == View.VISIBLE) {
                    contentHolder.tv_duration.setVisibility(View.GONE);
                }
            }
            if (enablePreview || enablePreviewVideo) {
                contentHolder.ll_check.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!Utils.isFastDoubleClick2()) {
                            changeCheckboxState(contentHolder, image);
                        }
                    }
                });
            }
            contentHolder.contentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (type == FunctionConfig.TYPE_VIDEO && (selectMode == FunctionConfig.MODE_SINGLE || enablePreviewVideo) && imageSelectChangedListener != null) {
                        int index = showCamera ? position - 1 : position;
                        imageSelectChangedListener.onPictureClick(image, index);
                    } else if (type == FunctionConfig.TYPE_IMAGE && (selectMode == FunctionConfig.MODE_SINGLE || enablePreview) && imageSelectChangedListener != null) {
                        int index = showCamera ? position - 1 : position;
                        imageSelectChangedListener.onPictureClick(image, index);
                    } else {
                        if (!Utils.isFastDoubleClick2()) {
                            changeCheckboxState(contentHolder, image);
                        }
                    }
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return showCamera ? images.size() + 1 : images.size();
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        View headerView;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            headerView = itemView;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView picture;
        TextView check;
        TextView tv_duration;
        View contentView;
        LinearLayout ll_check;

        public ViewHolder(View itemView) {
            super(itemView);
            contentView = itemView;
            picture = (ImageView) itemView.findViewById(R.id.picture);
            check = (TextView) itemView.findViewById(R.id.check);
            ll_check = (LinearLayout) itemView.findViewById(R.id.ll_check);
            tv_duration = (TextView) itemView.findViewById(R.id.tv_duration);
        }
    }

    public boolean isSelected(LocalMedia image) {
        for (LocalMedia media : selectImages) {
            if (media.getPath().equals(image.getPath())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 选择按钮更新
     */
    private void notifyCheckChanged(ViewHolder viewHolder, LocalMedia imageBean) {
        viewHolder.check.setText("");
        for (LocalMedia media : selectImages) {
            if (media.getPath().equals(imageBean.getPath())) {
                imageBean.setNum(media.getNum());
                media.setPosition(imageBean.getPosition());
                viewHolder.check.setText(String.valueOf(imageBean.getNum()));
            }
        }
    }

    /**
     * 改变图片选中状态
     *
     * @param contentHolder
     * @param image
     */

    private void changeCheckboxState(ViewHolder contentHolder, LocalMedia image) {
        boolean isChecked = contentHolder.check.isSelected();

        if (selectImages.size() >= maxSelectNum && !isChecked) {
            switch (type) {
                case FunctionConfig.TYPE_IMAGE:
                    Toast.makeText(context, context.getString(R.string.picture_message_max_num, maxSelectNum), Toast.LENGTH_LONG).show();
                    break;
                case FunctionConfig.TYPE_VIDEO:
                    Toast.makeText(context, context.getString(R.string.picture_message_video_max_num, maxSelectNum), Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
            return;
        }
        if (isChecked) {
            for (LocalMedia media : selectImages) {
                if (media.getPath().equals(image.getPath())) {
                    selectImages.remove(media);
                    subSelectPosition();
                    break;
                }
            }
        } else {
            selectImages.add(image);
            image.setNum(selectImages.size());
            playSound();
        }
        //通知点击项发生了改变
        notifyItemChanged(contentHolder.getAdapterPosition());
        selectImage(contentHolder, !isChecked, true);
        if (imageSelectChangedListener != null) {
            imageSelectChangedListener.onChange(selectImages);
        }
    }

    /**
     * 更新选择的顺序
     */
    private void subSelectPosition() {
        if (is_checked_num) {
            int size = selectImages.size();
            for (int index = 0, length = size; index < length; index++) {
                LocalMedia media = selectImages.get(index);
                media.setNum(index + 1);
                notifyItemChanged(media.position);
            }
        }
    }

    public void selectImage(ViewHolder holder, boolean isChecked, boolean isAnim) {
        holder.check.setSelected(isChecked);
        if (isChecked) {
            if (isAnim) {
                if (animation != null)
                    holder.check.startAnimation(animation);
            }
            holder.picture.setColorFilter(ContextCompat.getColor(context, R.color.image_overlay2), PorterDuff.Mode.SRC_ATOP);
        } else {
            holder.picture.setColorFilter(ContextCompat.getColor(context, R.color.image_overlay), PorterDuff.Mode.SRC_ATOP);
        }
    }

    public interface OnPhotoSelectChangedListener {
        void onTakePhoto();

        void onChange(List<LocalMedia> selectImages);


        void onPictureClick(LocalMedia media, int position);
    }

    public void setOnPhotoSelectChangedListener(OnPhotoSelectChangedListener imageSelectChangedListener) {
        this.imageSelectChangedListener = imageSelectChangedListener;
    }

    /**
     * 毫秒转时分秒
     *
     * @param duration
     * @return
     */
    public static String timeParse(long duration) {
        String time = "";
        long minute = duration / 60000;
        long seconds = duration % 60000;
        long second = Math.round((float) seconds / 1000);
        if (minute < 10) {
            time += "0";
        }
        time += minute + ":";
        if (second < 10) {
            time += "0";
        }
        time += second;
        return time;
    }


}
