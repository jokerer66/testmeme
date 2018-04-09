package com.instanza.testmemo.Activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.instanza.testmemo.R;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.marshalchen.ultimaterecyclerview.itemTouchHelper.SimpleItemTouchHelperCallback;
import com.marshalchen.ultimaterecyclerview.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.os.Handler;
import java.util.logging.LogRecord;

/**
 * Created by apple on 2017/12/6.
 */

public class gradviewActivity extends AppCompatActivity {
    UltimateRecyclerView recyclerView;

    View headerView;
    private StringAdapter adapter;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gridview_activity);
//        ButterKnife.bind(this);
        handler = new Handler();
        recyclerView = (UltimateRecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        headerView = LayoutInflater.from(this).inflate(R.layout.header,null);
        List<String> data = new ArrayList<>();
        for(int i=0;i<12;i++){
            data.add("test  = "+i);
        }
        adapter = new StringAdapter(data);
        recyclerView.setAdapter(adapter);

        //为每一个item加入头部的布局   这里运用到的事实上就是RecyclerView.ItemDecoration
        //没错  可能一般都是用这个来实现item之间的分隔线的  可是线也是一个view，但这个view够大的时候，就是一个头部了
        StickyRecyclerHeadersDecoration stickyRecyclerHeadersDecoration = new StickyRecyclerHeadersDecoration(adapter);
        recyclerView.addItemDecoration(stickyRecyclerHeadersDecoration);


        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter){
            //这种方法还有别的方法能够重载  能够控制如滑动删除等功能


            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;//控制拖动的方向   这里设置了智能上下拖动交换位置
                final int swipeFlags = ItemTouchHelper.LEFT ;//控制滑动删除的方向  这里设置了仅仅能左滑删除
//                final int swipeFlags = ItemTouchHelper.LEFT |ItemTouchHelper.RIGHT;//左右滑删除

                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean isItemViewSwipeEnabled() {
                return super.isItemViewSwipeEnabled();//这里控制开启或关闭item能否够滑动删除的功能
            }

            @Override
            public boolean isLongPressDragEnabled() {
                return super.isLongPressDragEnabled();//控制长按拖动功能
            }
        };

        final ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView.mRecyclerView);
        //设置头部一定要在setAdapter后面，由于这个操作会调用adapter的方法来显示头部，假设adapter为null，则出错
        recyclerView.setParallaxHeader(headerView);
        recyclerView.enableDefaultSwipeRefresh(true);//开启下拉刷新
        recyclerView.reenableLoadmore();//开启上拉载入很多其它
        recyclerView.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setRefreshing(false);
                    }
                },2000);
            }
        });
    }

    class StringAdapter extends UltimateViewAdapter<StringViewHolder>{
        private List<String> stringList;

        public StringAdapter(List<String> stringList) {
            this.stringList = stringList;
        }

        public StringViewHolder getViewHolder(View view) {
            return new StringViewHolder(view,false);
            //这个getViewHolder方法在内部实现中仅仅有在获取头部、载入很多其它、下拉刷新的时候会调用
            //直接设置itemView为GONE，所以不须要初始化什么的。仅仅要返回的是个ViewHolder即可
        }


        @Override
        public StringViewHolder newFooterHolder(View view) {
            return null;
        }

        @Override
        public StringViewHolder newHeaderHolder(View view) {
            return null;
        }

        @Override
        public StringViewHolder onCreateViewHolder(ViewGroup parent) {
            View view  = LayoutInflater.from(gradviewActivity.this).inflate(R.layout.item,null);
            return new StringViewHolder(view,true);
        }



        @Override
        public int getAdapterItemCount() {
            return stringList==null?0:stringList.size();
            //这里返回的是你的item的个数  不包含头部和载入view
        }

        @Override
        public long generateHeaderId(int position) {
//            if (getItem(position).length() > 0)
//                return getItem(position).charAt(0);
//            else return -1;
            if(customHeaderView!=null){
                position-=1;
            }
            String s = position+"";
            return s.charAt(0);
            //为每一项item生成头部的View。假设返回－1。则不生成，假如多个连续的item返回同一个id，
            //则仅仅会生成一个头部View
            //这里提取position的第一个数作为id
            //1 10 11 12 14等返回的id是一样的  为1
        }

        @Override
        public void onBindViewHolder(StringViewHolder holder, int position) {
            //一定要加这个推断  由于UltimateRecyclerView本身有加了头部和尾部  这种方法返回的是包含头部和尾部在内的
            if (position < getItemCount() && (customHeaderView != null ? position <= stringList.size() : position < stringList.size()) && (customHeaderView != null ? position > 0 : true)) {
                position  -= customHeaderView==null?0:1;
                holder.tv.setText(stringList.get(position));
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
            View view  = LayoutInflater.from(gradviewActivity.this).inflate(R.layout.item,null);
            return new StringViewHolder(view,true);
            //初始化item的头部布局  这里为了方便 就直接用StringViewHolder,实际使用能够使用不同于item的布局
        }

        @Override
        public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
            if(customHeaderView!=null){
                position-=1;
            }
            ((StringViewHolder)holder).tv.setText("header  "+(position+"").charAt(0));
            ((StringViewHolder)holder).tv.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            //绑定item头部view的数据，这里提取每一个view的position的第一个数来作为头部显示数据
            //即10 11 12  13  14这些  返回的是1
            //20 21 22等是2
        }

        @Override
        public void onItemMove(int fromPosition, int toPosition) {
//            swapPositions(data,fromPosition,toPosition);
            //假设开启的拖动移动位置的功能
            //要重写这种方法  由于假设不重写  交换的仅仅是view的位置，数据的位置没有交换 一拖动。就会变成原来的样子
            super.onItemMove(fromPosition, toPosition);
        }

        @Override
        public void onItemDismiss(int position) {
//            remove(data,position);//控制删除的
            super.onItemDismiss(position);
        }
    }

    public class StringViewHolder extends UltimateRecyclerviewViewHolder{
        public TextView tv;
        public StringViewHolder(View itemView,boolean isItem) {
            super(itemView);
            if(isItem){
                tv = (TextView) itemView;
            }
        }
    }

    private class MyVH extends UltimateRecyclerviewViewHolder{
        TextView textView;
        ImageView imageView;
        public MyVH(View itemView) {
            super(itemView);
            textView =(TextView) itemView.findViewById(R.id.text);
            imageView = (ImageView) itemView.findViewById(R.id.image);
        }
    }

    private class MyVH2 extends UltimateRecyclerviewViewHolder{
//        TextView title;
//        Button left;
        public MyVH2(View itemView) {
            super(itemView);
//            title =(TextView) itemView.findViewById(R.id.title);
//            left = (Button) itemView.findViewById(R.id.left);
        }
    }


}
