package com.instanza.testmemo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.instanza.testmemo.DB.MemoService;
import com.instanza.testmemo.ListHolder.TagListViewHolder;
import com.instanza.testmemo.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by apple on 2017/10/11.
 */

public class TagListItemAdapter extends BaseAdapter {
    private List<String> listtagnames = new ArrayList<>();
//    private String[] tagnames = {"旅游","个人","生活","工作","无标签"};
    private String[] tagnames;
    public static int[] drawables={R.drawable.redpoint,R.drawable.greenpoint,R.drawable.bluepoint,R.drawable.yellowpoint,R.drawable.pppoint};
    private Context context;
    private int layoyrSource;
    private LayoutInflater mInflater = null;
    TagListViewHolder holder = null;

    public TagListItemAdapter(Context context,int layoyrSource,List<String> data) {
        this.mInflater = LayoutInflater.from(context);
        this.layoyrSource = layoyrSource;
        this.context = context;
        tagnames = context.getResources().getStringArray(R.array.tags);
    }

    @Override
    public int getCount() {
        return tagnames.length;
    }

    @Override
    public Object getItem(int position) {
        return tagnames.length;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        MemoService memoService = new MemoService(context);
        if (convertView == null) {

            holder=new TagListViewHolder();

            convertView = mInflater.inflate(layoyrSource, null);
            holder.tagLL = (RelativeLayout)convertView.findViewById(R.id.tagLL);
            holder.tagimageview = (ImageView) convertView.findViewById(R.id.tagImageView);
            holder.tagtextview = (TextView)convertView.findViewById(R.id.TagtextView);
            holder.tagnumber = (TextView) convertView.findViewById(R.id.tagsnumber);

            convertView.setTag(holder);

        }else {

            holder = (TagListViewHolder)convertView.getTag();
        }
        holder.tagtextview.setText(tagnames[position]);
        holder.tagimageview.setImageDrawable(context.getResources().getDrawable(drawables[position]));
        Map<String,String> map= new HashMap<String, String>();
        map.put("tag",tagnames[position]);
        holder.tagnumber.setText("( "+ memoService.selectMemoByParam(map).size() +" )");
        return convertView;
    }
}
