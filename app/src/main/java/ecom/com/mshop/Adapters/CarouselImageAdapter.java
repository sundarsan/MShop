package ecom.com.mshop.Adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

import ecom.com.mshop.R;

/**
 * Created by Pandey on 17-11-2016.
 */
public class CarouselImageAdapter extends PagerAdapter {

    int []banner;
    LayoutInflater mLayoutInflater;
    Context mContext;

    public CarouselImageAdapter(Context context,int[]images) {
        mContext = context;
        banner=images;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return banner.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
        imageView.setImageResource(banner[position]);
        container.addView(itemView);
        return itemView;

    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }

}



