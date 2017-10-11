package com.test.ksi.testmap.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.test.ksi.testmap.R;
import com.test.ksi.testmap.models.googlePlacesApis.Results;

import java.util.ArrayList;


/**
 *
 */
public class ViewPagerAdapter extends PagerAdapter implements View.OnClickListener {

    private Context context;
    private ArrayList<Results> intro;

    // private CircleImageView ivPager;
    private ViewPager viepager;
    private TextView tvViewPagerName;
    //private TextView tvPrice, tvSpecial;
    //  private TextView tvLoader;
    // private boolean isFeatured;
    // private String strPrice;

    public ViewPagerAdapter(Context context, ArrayList<Results> intro, ViewPager viepager) {
        this.context = context;
        this.intro = intro;
        this.viepager = viepager;
        //  this.isFeatured = isFutuered;
        //   this.strPrice = strPrice;
        //   Log.e("price",strPrice+"iii");
    }

    @Override
    public int getCount() {
        return intro.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View inner = inflater.inflate(R.layout.xml_view_pager, null);
        //  ivPager = (CircleImageView) inner.findViewById(R.id.iv_pager);
        tvViewPagerName = (TextView) inner.findViewById(R.id.tvViewPagerName);
        //ivPager.setOnClickListener(this);
        //

        // ivPager.setImageURI(imagesLinks.get(position));
        //
//==================
        //  ivPager.setImageResource(intro.get(position).getImg());
        tvViewPagerName.setText(intro.get(position).getName());
        //=======================


        ((ViewPager) container).addView(inner, 0);
        return inner;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }

    @Override
    public void onClick(View v) {

    }

}