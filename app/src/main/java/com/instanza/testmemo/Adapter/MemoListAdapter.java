package com.instanza.testmemo.Adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.instanza.testmemo.Activity.AddMemoActivity;
import com.instanza.testmemo.Activity.MainActivity;
import com.instanza.testmemo.Bean.MemoListItem;
import com.instanza.testmemo.ListHolder.MemoListViewHolder;
import com.instanza.testmemo.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by apple on 2017/9/30.
 */

public class MemoListAdapter extends BaseAdapter{

    private AlertDialog dialog = null;
    private LayoutInflater mInflater = null;
    private int layoyrSource  = 0;
    private List<MemoListItem> listItemList;
    private Context context;
    private boolean isMultiSelect = false;// 是否处于多选状态
    private HashMap<Integer, Integer> isCheckBoxVisible;// 用来记录是否显示checkBox
    private HashMap<Integer, Boolean> isChecked;// 用来记录是否被选中
    public List<MemoListItem> getListItemList() {
        return listItemList;
    }

    public void setListItemList(List<MemoListItem> listItemList) {
        this.listItemList = listItemList;
    }

    public boolean isMultiSelect() {
        return isMultiSelect;
    }

    public void setMultiSelect(boolean multiSelect) {
        isMultiSelect = multiSelect;
    }

    public MemoListAdapter(Context context, int layoyrSource, List<MemoListItem> listItemList)
    {
        //根据context上下文加载布局，这里的是Demo17Activity本身，即this
        this.mInflater = LayoutInflater.from(context);
        this.layoyrSource = layoyrSource;
        this.listItemList = listItemList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return listItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return listItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        MemoListViewHolder holder = null;
        if (convertView == null) {

            holder=new MemoListViewHolder();
            convertView = mInflater.inflate(layoyrSource, null);
            holder.memoitem = (LinearLayout)convertView.findViewById(R.id.memoitem);
            holder.title = (TextView)convertView.findViewById(R.id.contexttext);
            holder.time = (TextView)convertView.findViewById(R.id.memoCreatetime);
            holder.checkBox = (CheckBox)convertView.findViewById(R.id.cb_select);
            convertView.setTag(holder);
        }else {
            holder = (MemoListViewHolder)convertView.getTag();
        }
        if(isMultiSelect){
            holder.checkBox.setVisibility(View.VISIBLE);
//            holder.checkBox.setClickable(false);
//            holder.checkBox.setClickable(true);
        }else{

            holder.checkBox.setVisibility(View.GONE);
        }

        holder.title.setText(listItemList.get(position).getContent().toString());
        holder.time.setText(listItemList.get(position).getCreatetime().toString());
        final MemoListViewHolder finalHolder = holder;
        holder.memoitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "You clicked "+v.getId(), Toast.LENGTH_SHORT).show();
                if(isMultiSelect){

                    if (finalHolder.checkBox.isChecked()) {
                        finalHolder.checkBox.setChecked(false);
//                        list_delete.remove(memoListAdapter.getListItemList().get(position));
                    } else {
                        finalHolder.checkBox.setChecked(true);
//                        list_delete.add(memoListAdapter.getListItemList().get(position));
                    }
//                    tv_sum.setText("共选择了" + list_delete.size() + "项");
                } else {
                    Intent intent = new Intent(context,AddMemoActivity.class);
                    intent.putExtra("memoitem", listItemList.get(position));
                    intent.putExtra("editmode",true);
                    context.startActivity(intent);
                }

            }
        });
//
//        holder.memoitem.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                if (dialog == null || !dialog.isShowing()) {
//                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                    builder.setTitle("test");
//
//                    final String[] Items=context.getResources().getStringArray(R.array.memo_action);
//                    builder.setItems(Items, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            switch (which){
//                                case 0:
//                                    try{
//                                        //获取剪贴板管理器：
//                                        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
//                                        // 创建普通字符型ClipData
//                                        ClipData mClipData = ClipData.newPlainText("Label", listItemList.get(position).getContent());
//                                        // 将ClipData内容放到系统剪贴板里。
//                                        cm.setPrimaryClip(mClipData);
//                                        Toast.makeText(context, "复制到剪贴板", Toast.LENGTH_SHORT).show();
//                                    }catch (Exception e){
//                                        e.printStackTrace();
//                                        Toast.makeText(context, "复制失败", Toast.LENGTH_SHORT).show();
//                                    }
//
//                                    break;
////                                case 1:
////                                    try{
////                                        memoService.deleteMemo(listItemList.get(position).getId());
////                                        onResume();
////                                        Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
////                                    }catch (Exception e){
////                                        e.printStackTrace();
////                                        Toast.makeText(context, "删除失败", Toast.LENGTH_SHORT).show();
////                                    }
////
////                                    break;
//                                case 2:
//                                    isMultiSelect = true;
////                                    list_delete.clear();
////                                    list_delete.add(memoListAdapter.getListItemList().get(position));
////                                    MultiSelect_ll.setVisibility(View.VISIBLE);
////                                    tv_sum.setText("共选择了" + list_delete.size() + "项");
////                                    memoListAdapter.setMultiSelect(true);
//                                    notifyDataSetChanged();
////                                    ((CheckBox)view.findViewById(R.id.cb_select)).setChecked(true);
//                                    // 根据position，设置ListView中对应的CheckBox为选中状态
//                                    break;
//                                default:
//                                    break;
//
//                            }
//                        }
//                    });
//                    dialog = builder.create();
//                    dialog.setCanceledOnTouchOutside(true);
//                }
//                dialog.show();
//                return true;
//            }
//        });
        return convertView;
    }


}
