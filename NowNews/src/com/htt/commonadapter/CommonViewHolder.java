package com.htt.commonadapter;

import com.example.nownews.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 
 * @author huangtengteng
 *         创建一个万用的ViewHolder，避免每次实现Adapter时都需要定义一个特定的ViewHolder,提高代码复用性
 *
 */
public class CommonViewHolder {

	Context context;
	int position;
	View mConvertView;
	// 泛型类的使用?? 直接使用View类就可以了，因为放进去的组件都是View的子类
	// 考虑一下泛类的使用场景？？
	SparseArray<View> mViewsArray;

	/**
	 * 构造方法设置为私有，不允许用户通过构造方法来得到ViewHolder实例，只能通过静态方法 getInstance()来获取
	 * 
	 * @param context
	 * @param parent
	 * @param layoutId
	 *            布局文件ID
	 * @param position
	 *            item索引
	 */
	private CommonViewHolder(Context context, ViewGroup parent, int layoutId, int position) {
		this.context = context;
		this.position = position;
		mViewsArray = new SparseArray<View>();
		mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
		mConvertView.setTag(this);
	}

	/**
	 * 获取ViewHolder对象实例，通过convertView来判断是否需要构造实例，如果convertView为空，则返回
	 * 新构造的实例，否则返回convertView.getTag()方法获取到的实例
	 * 
	 * @param context
	 * @param convertView
	 * @param parent
	 * @param layoutId
	 * @param position
	 * @return
	 */
	static CommonViewHolder getInstance(Context context, View convertView, ViewGroup parent, int layoutId,
			int position) {
		CommonViewHolder viewholder = null;
		if (convertView == null) {// convertView为空，新建viewholder
			viewholder = new CommonViewHolder(context, parent, layoutId, position);
		} else {
			viewholder = (CommonViewHolder) convertView.getTag();
			viewholder.position = position;// 考虑到代码复用，position的值是会动态改变的
		}
		return viewholder;
	}

	/**
	 * 获得修改后的convertVIew
	 * 
	 * @return
	 */
	public View getConvertView() {
		return this.mConvertView;
	}

	/**
	 * 使用泛型<T extends View>作为返回值和直接使用View的区别在哪？？？
	 * 返回指定id的空间的引用，通过CommonViewHolder
	 * 
	 * @param id
	 *            需要的控件的id
	 */
	public <T extends View> T getViewById(int id) {
		View v;
		if (null == (v = mViewsArray.get(id, null))) {
			v = mConvertView.findViewById(id);
			mViewsArray.put(id, v);
		}

		return (T) v;
	}

	// 我们还可以在ViewHolder中进一步封装一些函数，来方便操作，比如我们经常使用的控件TextView,ImageView
	// 等等，我们对其进行再一次封装。
	// 返回值设置为CommonViewHolder使得我们可以进行链式编程

	/**
	 * 为指定id的TextView设定Text值
	 * 
	 * @param id
	 * @param string
	 * @return
	 */
	public CommonViewHolder setText(int id, String string) {
		TextView textView = getViewById(id);
		textView.setText(string);
		return this;
	}

	/**
	 * 为指定id的imgView设置img，图片源来自于资源库
	 * 
	 * @param id
	 * @param resId
	 *            图片源的id
	 * @return
	 */
	public CommonViewHolder setImgResource(int id, int resId) {
		ImageView imageView = getViewById(id);
		imageView.setImageResource(resId);
		return this;
	}

	/**
	 * 为指定id的imgView设置img，图片源为指定的bitmap
	 * 
	 * @param id
	 * @param bitmap
	 *            图片源
	 * @return
	 */
	public CommonViewHolder setImgResource(int id, Bitmap bitmap) {
		ImageView imageView = getViewById(id);
		imageView.setImageBitmap(bitmap);
		return this;
	}

	/**
	 * 为指定的ImageView添加来自网络的图片，使用ImageLoader的loadImage方法
	 * 
	 * @param id
	 * @param url
	 * @param options
	 *            设置图片显示选项，如果传入的选项为空，则使用方法内默认的显示选项，保存到内存，
	 *            保存到disk，且以RGB——565的形式显示。
	 * @return
	 */
	public CommonViewHolder setImgURL_loadImg(int id, String url, DisplayImageOptions options) {
		final ImageView imageView = getViewById(id);
		DisplayImageOptions mOptions;

		if (options == null) {
			mOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true)
					.bitmapConfig(Bitmap.Config.RGB_565).build();
		} else {
			mOptions = options;
		}

		ImageLoader.getInstance().loadImage(url, mOptions, new ImageLoadingListener() {

			@Override
			public void onLoadingStarted(String imageUri, View view) {

			}

			@Override
			public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

			}

			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				imageView.setImageBitmap(loadedImage);
			}

			@Override
			public void onLoadingCancelled(String imageUri, View view) {

			}
		});
		return this;
	}

	/**
	 * 为指定id的ImageView设置图片，
	 * 
	 * @param id
	 *            待设置图片的ImageView的id
	 * @param url
	 *            图片的网络地址
	 * @return
	 */
	public CommonViewHolder setImageURL_displayImage(int id, String url) {
		final ImageView imageView = getViewById(id);
		DisplayImageOptions options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.ic_launcher)
				.showImageOnFail(R.drawable.ic_launcher).cacheInMemory(true).cacheOnDisk(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();

		ImageLoader.getInstance().displayImage(url, imageView, options);
		return this;
	}

}
