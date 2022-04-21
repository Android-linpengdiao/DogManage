package com.base.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.base.R;
import com.base.view.GlideBlurTransformation;
import com.base.view.GlideRoundTransform;
import com.base.view.RoundedCornersTransform;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

public class GlideLoader {

    public static void LoderImage(Context context, String url, ImageView view) {
        LoderImage(context, url, view, 0);
    }

    public static void LoderImage(Context context, String url, ImageView view, int round) {
        Glide.with(context)
                .load(url)
                .centerCrop()
                .transform(new GlideRoundTransform(context, round))
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(view);
    }

    public static void LoderStaffImage(Context context, String url, ImageView view) {
        Glide.with(context)
                .load(url)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(view);
    }

    public static void LoderBannerImage(Context context, String url, ImageView view, int round) {
        Glide.with(context)
                .load(url)
                .centerCrop()
                .transform(new GlideRoundTransform(context, round))
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(view);
    }

    public static void LoderLinkImage(Context context, String url, ImageView view, int round) {
        Glide.with(context)
                .load(url)
                .centerCrop()
                .transform(new GlideRoundTransform(context, round))
                .placeholder(R.mipmap.app_icon)
                .error(R.mipmap.app_icon)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(view);
    }

    public static void LoderAlbumImage(Context context, String url, ImageView view, int round) {
        Glide.with(context)
                .load(url)
                .centerCrop()
                .transform(new GlideRoundTransform(context, round))
//                .placeholder(R.mipmap.upload)
//                .error(R.mipmap.upload)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(view);
    }

    public static void LoderCircleImage(Context context, String url, ImageView view) {
        Glide.with(context)
                .load(url)
                .centerCrop()
                .transform(new GlideRoundTransform(context, 100))
                .placeholder(R.drawable.head)
                .error(R.drawable.head)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(view);
    }

    public static void LoderBlurImage(Context context, String url, ImageView view) {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transform(new GlideBlurTransformation(context));
        Glide.with(context)
                .load(url)
                .apply(requestOptions)
                .into(view);
    }

    public static void LoderImageUrl(Context context, String url, ImageView view) {
        Glide.with(context)
                .load(url)
                .centerCrop()
                .placeholder(R.drawable.head)
                .error(R.drawable.head)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(view);
    }

    public static void LoderVideoCover(Context context, String url, ImageView view) {
        Glide.with(context)
                .load(url)
                .centerInside()
                .placeholder(R.color.black)
                .error(R.color.black)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(view);
    }

    public static void LoderRoundedImage(Context context, String url, ImageView view, int round) {
        RoundedCornersTransform transform = new RoundedCornersTransform(context, CommonUtil.dip2px(context, round));
        transform.setNeedCorner(true, true, false, false);
        Glide.with(context)
                .load(url)
                .placeholder(R.drawable.button_top_gray)
                .error(R.drawable.button_top_gray)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transform(transform)
                .into(view);

    }

    public static void LoderClipImage(Context context, String url, ImageView view) {
        Glide.with(context)
                .load(url)
                .centerInside()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(view);
    }

    public static void LoderImageOrGif(Context context, String url, ImageView view) {
        if (url.endsWith("gif")) {
            Glide.with(context)
                    .asGif()
                    .load(url)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(view);
        } else {
            Glide.with(context)
                    .load(url)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(view);
        }
    }

    public static void LoderGalleryImage(Context context, String url, ImageView view) {
        if (!CommonUtil.isBlank(url)) {
            LoderGalleryImage(context, url.startsWith("http") ? url : "file://" + url, view, 0);
        }
    }

    public static void LoderGalleryImage(Context context, String url, ImageView view, int round) {
        Glide.with(context)
                .load(url)
                .fitCenter()
                .thumbnail(0.1f)
                .placeholder(R.drawable.web_ic_picture_default)
                .error(R.drawable.web_ic_picture_default)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(view);
    }

    public static void LoderMedia(Context context, String url, ImageView view) {
        Glide.with(context)
                .load(url)
                .centerCrop()
                .transform(new GlideRoundTransform(context, 0))
                .placeholder(R.color.media_color)
                .error(R.color.media_color)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(view);
    }

    public static void LoderMediaImage(Context context, String url, ImageView view) {
        Glide.with(context)
                .load("file://" + url)
                .centerCrop()
                .placeholder(R.color.gray)
                .error(R.color.gray)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(view);
    }

    public static void LoderLoadImage(Context context, String url, ImageView view, int round) {
        Glide.with(context)
                .load("file://" + url)
                .centerCrop()
                .transform(new GlideRoundTransform(context, round))
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(view);
    }

    public static void LoderDrawable(Context context, int drawable, ImageView view) {
        Glide.with(context)
                .load(drawable)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(view);
    }

    public static void LoderDrawable(Context context, int drawable, ImageView view, int round) {
        Glide.with(context)
                .load(drawable)
                .fitCenter()
                .transform(new GlideRoundTransform(context, round))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(view);
    }

    public static void LoderDrawableVip(Context context, int drawable, ImageView view, int round) {
        RoundedCornersTransform transform = new RoundedCornersTransform(context, CommonUtil.dip2px(context, round));
        transform.setNeedCorner(true, true, false, false);
        Glide.with(context)
                .load(drawable)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transform(transform)
                .into(view);
    }

    public static Bitmap load(Context context, String url) {
        try {
            return Glide.with(context)
                    .asBitmap()
                    .load(url)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
