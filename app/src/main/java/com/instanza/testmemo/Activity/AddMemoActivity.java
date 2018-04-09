package com.instanza.testmemo.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.instanza.testmemo.Adapter.TagListItemAdapter;
import com.instanza.testmemo.Bean.MemoListItem;
import com.instanza.testmemo.DB.MemoDao;
import com.instanza.testmemo.DB.MemoService;
import com.instanza.testmemo.Helper.System.SystemHelper;
import com.instanza.testmemo.Helper.Time.TimeHelper;
import com.instanza.testmemo.ListHolder.TagListViewHolder;
import com.instanza.testmemo.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by apple on 2017/9/29.
 */

public class AddMemoActivity extends AppCompatActivity {
    private static final int FinishAddMemo = 1;
    private EditText editText = null;
    private Toolbar toolbar;
    private Boolean isEDitMode = false;
    private MemoListItem memolistitem = new MemoListItem();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addmemo);

        Intent intent = getIntent();


        editText = (EditText)findViewById(R.id.memotext);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        final MenuItem menuItem= toolbar.getMenu().add(Menu.NONE,FinishAddMemo,FinishAddMemo,R.string.action_edit);
        menuItem.setIcon(R.drawable.okbutton);
        MenuItemCompat.setShowAsAction(menuItem, MenuItem.SHOW_AS_ACTION_ALWAYS);

        menuItem.setOnMenuItemClickListener(myOnMenuItemClickListener);

        TextView createtime =(TextView)findViewById(R.id.lastedittime);
        final ImageView choosetag = (ImageView)findViewById(R.id.choosetag);

        choosetag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = null;
                if (dialog == null || !dialog.isShowing()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddMemoActivity.this);
                    builder.setTitle(R.string.settag);
                    final String[] tags = AddMemoActivity.this.getResources().getStringArray(R.array.tags);
                    int index= 0;
                    if( memolistitem.getTag() == null){
                        index = 0;
                    }else{
                        for(int i=0;i<tags.length;i++){
                            if(memolistitem.getTag().equals(tags[i])){
                                index = i;
                            }
                        }
                    }
                    builder.setSingleChoiceItems(tags, index, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            choosetag.setImageDrawable(AddMemoActivity.this.getResources().getDrawable(TagListItemAdapter.drawables[which]));
                            memolistitem.setTag(tags[which]);
                            dialog.dismiss();
                        }
                    });
                    dialog = builder.create();
                    dialog.setCanceledOnTouchOutside(true);
                }
                dialog.show();
                menuItem.setVisible(true);
            }
        });




        if(intent.getBooleanExtra("editmode",false)){
            isEDitMode = true;
            toolbar.setTitle(R.string.editmemo);

            memolistitem =(MemoListItem) getIntent().getSerializableExtra("memoitem");

            if(memolistitem.getId() == 0 ){
                return;
            }
            int index = 0;
            for(int i=0;i<TagListItemAdapter.drawables.length;i++){
                if(memolistitem.getTag().equals(getResources().getStringArray(R.array.tags)[i])){
                    index = i;
                }
            }

            choosetag.setImageDrawable(getResources().getDrawable(TagListItemAdapter.drawables[index]));
            createtime.setText(memolistitem.getCreatetime());
            toEditMode(menuItem,memolistitem);
        }else{
            memolistitem.setTag(getResources().getString(R.string.none));
            isEDitMode = false;
            toolbar.setTitle(R.string.addmemo);
            createtime.setText(TimeHelper.getcurrentTime());
            toAddMode(menuItem);
        }

        toolbar.setNavigationIcon(R.drawable.backbutton);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });


    }
    public class MyOnMenuItemClickListener implements MenuItem.OnMenuItemClickListener {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            int i = 0;
            MemoService memoService =new MemoService(getApplicationContext());
            Map<String,String> map = new HashMap();
            map.put("content",editText.getText().toString());
            map.put("createtime", TimeHelper.getcurrentTime());
            map.put("tag",memolistitem.getTag());
            if(isEDitMode){
                map.put("type",memolistitem.getType());
                map.put("memoid",String.valueOf(memolistitem.getId()));
                memoService.updateMemo(map);
                Toast.makeText(AddMemoActivity.this,"edit "+editText.getText().toString(),Toast.LENGTH_SHORT).show();
            }else{
                map.put("type","type1");
                Toast.makeText(AddMemoActivity.this,"add "+editText.getText().toString(),Toast.LENGTH_SHORT).show();
                memoService.addMemo(map);
            }

            onBackPressed();
            return true;
        }
    }
    private MyOnMenuItemClickListener myOnMenuItemClickListener = new MyOnMenuItemClickListener();

    public void toEditMode(final MenuItem menuItem,MemoListItem memolistitem) {
        menuItem.setVisible(false);
        editText.setText(memolistitem.getContent());
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();  // 初始不让EditText得焦点
        editText.requestFocusFromTouch();
        editText.setCursorVisible(false);

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toAddMode(menuItem);
            }
        });
    }

    public void toAddMode(MenuItem menuItem) {
        editText.setCursorVisible(true);
        menuItem.setVisible(true);
    }
    @Override
    public void onBackPressed() {
        SystemHelper.getInstance(getApplicationContext()).hideIME(editText,true);
        super.onBackPressed();
    }
}
