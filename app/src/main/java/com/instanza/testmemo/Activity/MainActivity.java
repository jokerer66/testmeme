package com.instanza.testmemo.Activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.instanza.testmemo.Adapter.MemoListAdapter;
import com.instanza.testmemo.Adapter.NewPresent;
import com.instanza.testmemo.Adapter.TagListItemAdapter;
import com.instanza.testmemo.Bean.MemoListItem;
import com.instanza.testmemo.DB.MemoService;
import com.instanza.testmemo.Fragment.FragmentBase.FragmentHelper;
import com.instanza.testmemo.Fragment.SettingFragment;
import com.instanza.testmemo.Helper.System.SystemHelper;
import com.instanza.testmemo.Helper.httprequest;
import com.instanza.testmemo.R;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private AlertDialog dialog = null;

    public Handler handler = new Handler();
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private LinearLayout setting_menu_LL;
    private List<MemoListItem> mydata;
    private MemoListAdapter memoListAdapter;
    private TagListItemAdapter tagListItemAdapter;
    private EditText search_edittext;
    private ListView memolist;
    private TextView settings,tv_sum;
    private LinearLayout MultiSelect_ll;
    private static final int AddMemo = 1;
    MemoService memoService;
    private List<MemoListItem> list_delete = new ArrayList<>();// 需要删除的数据
    private boolean isMultiSelect = false;// 是否处于多选状态
    private Map<String,String> sqlmap = new HashMap<>();
    private Button canceldeleteButton,deleteButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("MainActivity onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        memoService = new MemoService(getApplicationContext());
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("My Title");
        toolbar.setSubtitle("Sub title");
//        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        setting_menu_LL = (LinearLayout)findViewById(R.id.setting_menu_LL);


        MultiSelect_ll = (LinearLayout)findViewById(R.id.MultiSelect_ll);
        tv_sum = (TextView)findViewById(R.id.tv_sum);
        canceldeleteButton = (Button)findViewById(R.id.bt_cancel);
        deleteButton = (Button)findViewById(R.id.bt_delete);
        canceldeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



        MyOnclickListen myOnclickListener = new MyOnclickListen();
        findViewById(R.id.text1).setOnClickListener(myOnclickListener);
        findViewById(R.id.text2).setOnClickListener(myOnclickListener);
        toolbar.setNavigationIcon(R.drawable.setting);
        if (toolbar != null) {
            ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.action_edit, R.string.action_settings);
            drawerToggle.syncState();
            drawerLayout.setDrawerListener(drawerToggle);
        }


//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (drawerLayout.isDrawerOpen(setting_menu_LL)) {
//                    drawerLayout.closeDrawer(setting_menu_LL);
//                    SystemHelper.getInstance(getApplicationContext()).hideIME(search_edittext,true);
//                } else {
//                    drawerLayout.openDrawer(setting_menu_LL);
//                    SystemHelper.getInstance(getApplicationContext()).hideIME(search_edittext,true);
//                }
//            }
//        });

        final MenuItem menuItem= toolbar.getMenu().add(Menu.NONE,AddMemo,AddMemo,R.string.action_edit);
        menuItem.setIcon(R.drawable.add_memo);
        MenuItemCompat.setShowAsAction(menuItem, MenuItem.SHOW_AS_ACTION_ALWAYS);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                String msg = "";
                switch (item.getItemId()) {
                    case AddMemo:
                        Intent intent = new Intent(MainActivity.this,AddMemoActivity.class);
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        mydata = getdata();
        memoListAdapter = new MemoListAdapter(MainActivity.this,R.layout.memoitem,mydata);
        search_edittext = (EditText)findViewById(R.id.search_edittext);
        search_edittext.clearFocus();
        getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        memolist = (ListView)findViewById(R.id.memolist);
        memolist.setAdapter(memoListAdapter);

//        memolist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(MainActivity.this, "You clicked "+view.getId(), Toast.LENGTH_SHORT).show();
//                if(isMultiSelect){
//                    CheckBox cb = (CheckBox) view.findViewById(R.id.cb_select);
//                    if (cb.isSelected()) {
//                        cb.setChecked(false);
//                        list_delete.remove(memoListAdapter.getListItemList().get(position));
//                    } else {
//                        cb.setChecked(true);
//                        list_delete.add(memoListAdapter.getListItemList().get(position));
//                    }
//                    tv_sum.setText("共选择了" + list_delete.size() + "项");
//                }else{
//                    Intent intent = new Intent(MainActivity.this,AddMemoActivity.class);
//                    intent.putExtra("memoitem", memoListAdapter.getListItemList().get(position));
//                    intent.putExtra("editmode",true);
//                    startActivity(intent);
//                }
//
//            }
//        });

        memolist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, final int position, long id) {
                if (dialog == null || !dialog.isShowing()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("test");

                    final String[] Items=getResources().getStringArray(R.array.memo_action);
                    builder.setItems(Items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case 0:
                                    try{
                                        //获取剪贴板管理器：
                                        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                        // 创建普通字符型ClipData
                                        ClipData mClipData = ClipData.newPlainText("Label", memoListAdapter.getListItemList().get(position).getContent());
                                        // 将ClipData内容放到系统剪贴板里。
                                        cm.setPrimaryClip(mClipData);
                                        Toast.makeText(MainActivity.this, "复制到剪贴板", Toast.LENGTH_SHORT).show();
                                    }catch (Exception e){
                                        e.printStackTrace();
                                        Toast.makeText(MainActivity.this, "复制失败", Toast.LENGTH_SHORT).show();
                                    }

                                    break;
                                case 1:
                                    try{
                                        memoService.deleteMemo(memoListAdapter.getListItemList().get(position).getId());
                                        onResume();
                                        Toast.makeText(MainActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                                    }catch (Exception e){
                                        e.printStackTrace();
                                        Toast.makeText(MainActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
                                    }

                                    break;
                                case 2:
//                                    isMultiSelect = true;
//                                    list_delete.clear();
//                                    list_delete.add(memoListAdapter.getListItemList().get(position));
//                                    MultiSelect_ll.setVisibility(View.VISIBLE);
//                                    tv_sum.setText("共选择了" + list_delete.size() + "项");
                                    memoListAdapter.setMultiSelect(true);
                                    memoListAdapter.notifyDataSetChanged();
//                                    ((CheckBox)view.findViewById(R.id.cb_select)).setChecked(true);
                                    // 根据position，设置ListView中对应的CheckBox为选中状态
                                    break;
                                default:
                                    break;

                            }
                        }
                    });
                    dialog = builder.create();
                    dialog.setCanceledOnTouchOutside(true);
                }
                dialog.show();
                return true;
            }
        });

        ListView taglist = (ListView) findViewById(R.id.taglist);
        tagListItemAdapter = new TagListItemAdapter(getApplicationContext(),R.layout.taglistitem,null);
        taglist.setAdapter(tagListItemAdapter);
        taglist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                drawerLayout.closeDrawer(setting_menu_LL);
                SystemHelper.getInstance(getApplicationContext()).hideIME(search_edittext,true);
                String tagname = MainActivity.this.getResources().getStringArray(R.array.tags)[position];
                sqlmap.put("tag",tagname);
                toolbar.setTitle(tagname);
                sqlmap.put("content",search_edittext.getText().toString());

                memoListAdapter.setListItemList(memoService.selectMemoByParam(sqlmap));
                memoListAdapter.notifyDataSetChanged();
            }
        });

        search_edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()<0){
                    return;
                }else{
                    sqlmap.put("content",search_edittext.getText().toString());
                    memoListAdapter.setListItemList(memoService.selectMemoByParam(sqlmap));
                    memoListAdapter.notifyDataSetChanged();

                }
            }
        });


        settings  = (TextView)findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentHelper fh = new FragmentHelper(MainActivity.this,R.id.main_framelayout);
                fh.startFragment(SettingFragment.class,null);
            }
        });
//        Button search_button = (Button)findViewById(R.id.search_button);
//        search_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String str = search_edittext.getText().toString();
//                updatedata(memoListAdapter.getListItemList(),str.toString());
//
//                memoListAdapter.notifyDataSetChanged();
//            }
//        });


    }

    private Handler mHandler = new Handler();


    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("MainActivity start");
        mHandler.post(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    @Override
    protected void onResume() {
        System.out.println("MainActivity onResume");
        super.onResume();
        memoListAdapter.setListItemList(memoService.selectMemoByParam(sqlmap));
        memoListAdapter.notifyDataSetChanged();
        tagListItemAdapter.notifyDataSetChanged();
    }


    public List<MemoListItem> getdata(){
        List<MemoListItem> list = new ArrayList<>();


        list = memoService.getAllMemo();

        return list;
    }

//    public List<MemoListItem> updatedata(List<MemoListItem> mydata,String select_content,String select_tag){
//        mydata.clear();
//        List<MemoListItem> list2 = getdata();
//        for(MemoListItem memitem:mydata){
//            if(memitem.getContent().contains(select_content.toLowerCase().toString())  && memitem.getTag().equals(select_tag)){
//                mydata.add(memitem);
//            }
//        }
//        return mydata;
//
//    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        super.onCreateOptionsMenu(menu);
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }

    private String pathdd ;
    static public NewPresent present;

    public String getPathdd() {
        return pathdd;
    }

    public void setPathdd(String pathdd) {
        this.pathdd = pathdd;
    }

    public class MyOnclickListen implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.text1:
                    startActivity(new Intent().setClass(MainActivity.this,WebViewActivity.class));
////                    Toast.makeText(getApplicationContext(),"text1",Toast.LENGTH_SHORT).show();
//                    httprequest req= new httprequest("http://192.168.2.203:8080",new urlCallback2(){
////                        httprequest req= new httprequest("http://images2015.cnblogs.com/blog/703548/201611/703548-20161101183119986-1932473427.png",new urlCallback2(){
//                        @Override
//                        public void docallback(String path) {
//                            super.docallback(path);
//                            setPathdd(path);
////                            Toast.makeText(getApplicationContext(),"get pic",Toast.LENGTH_SHORT).show();
////                            handler.post()
////                            handler.post(new Runnable() {
////                                @Override
////                                public void run() {
////                                    ((TextView)findViewById(R.id.text1)).setText("ok");
////                                }
////                            });
//
//                        }
//                    });
//                    new Thread(req).start();
                    break;


                case R.id.text2:
//                    playMusic();
                    Intent intent = new Intent(MainActivity.this,SimplePhotoActivity.class);
                    startActivity(intent);
//                    Bitmap bitmap = null;
//                    try {
//                        bitmap = ImageUtil.fileToBitmapThumb(getPathdd(),400,300);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    ((ImageView)findViewById(R.id.testimageview)).setImageBitmap(bitmap);
//                    Toast.makeText(getApplicationContext(),"text2",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    public class urlCallback2 implements httprequest.urlCallback{
        @Override
        public void docallback(String path) {

        }
    }

    private void playMusic(){
//        File file = new File("/Internal storage/Download/aDrinkUp.mp3");
        File file = getDiskCacheDir(this);
        Log.e("file path","file path = "+ file.getAbsolutePath());
        MediaPlayer audioPlayer ;
        try {
            if(file.exists()){
                Log.e("file path","file path = exists");

                audioPlayer = new MediaPlayer();
                audioPlayer.setDataSource(file.getAbsolutePath());
//            audioPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                @Override
//                public void onCompletion(MediaPlayer mediaPlayer) {
//                    if (!playlist.isEmpty() && playlist.size() > 1) {
//                        playNextMessage(true);
//                    } else {
//                        cleanupPlayer(true, true);
//                    }
//                }
//            });
                audioPlayer.prepare();
                audioPlayer.start();
            }else{
                Log.e("file path","file path = error");
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public File getDiskCacheDir(Context context) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        File file = new File(Environment.getExternalStorageDirectory(),"Download/aDrinkUp.mp3");
        return  file;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("MainActivity onDestroy");
    }
}
