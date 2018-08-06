package com.widget.imagepicker;

import java.io.File;

import android.content.Context;
import android.widget.ImageView;

import com.app.xstore.R;
import com.squareup.picasso.Picasso;

/**
 * Picasso图片加载器
 * @author Ni Guijun
 *
 */
public class PicassoLoader implements ImageLoader {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void displayImage(Context context, String path, ImageView imageView) {
		Picasso.with(context)
				.load(new File(path))
				.placeholder(R.drawable.imageselector_photo)
				.resize(120, 120) 
				.centerCrop()
				.into(imageView);
	}

}
