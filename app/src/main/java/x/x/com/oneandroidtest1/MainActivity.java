package x.x.com.oneandroidtest1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    private RecyclerView rv_test1;
    private Button b_changeActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission();
        initRv();
        initView();
    }

    private void initView(){
        b_changeActivity = findViewById(R.id.b_changeActivity);
        b_changeActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChatActivity.actionStart(MainActivity.this,"小二");
            }
        });
    }
    private void initRv(){
        rv_test1 = findViewById(R.id.rv_test1);
        ArrayList<String> listTemp = new ArrayList<String>();
        for(int i=0;i<20;i++){
            listTemp.add(randString());
        }
//        LinearLayoutManager llm = new LinearLayoutManager(this);//默认纵向布局
//        llm.setOrientation(LinearLayoutManager.HORIZONTAL);//横向布局
//        rv_test1.setLayoutManager(llm);

        StaggeredGridLayoutManager sglm =//瀑布流布局
                new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        rv_test1.setLayoutManager(sglm);

       RvAdapter adapter = new RvAdapter(listTemp);
        rv_test1.setAdapter(adapter);
    }

    private String randString(){
        String result;
        Random random = new Random();
        int row = random.nextInt(10);
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < row ; i++){
            builder.append("row" + i + "\n");
        }
        return builder.toString();
    }
    /**android6.0以上权限申请,需要support-v4包*/
    private void checkPermission() {
        //检查权限（NEED_PERMISSION）是否被授权 PackageManager.PERMISSION_GRANTED表示同意授权
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //用户已经拒绝过一次，再次弹出权限申请对话框需要给用户一个解释,用户选择不再询问时，此方法返回 false。
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission
                    .WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(this, "请开通相关权限，否则无法正常使用本应用！", Toast.LENGTH_SHORT).show();
            }
            //申请权限,第三个参数是请求码便于在onRequestPermissionsResult 方法中根据requestCode进行判断.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS}, REQUEST_WRITE_EXTERNAL_STORAGE);

        } else {
            // Toast.makeText(this, "授权成功！", Toast.LENGTH_SHORT).show();
            Log.d("checkPermission", "checkPermission: 已经授权！");
        }
    }
}
