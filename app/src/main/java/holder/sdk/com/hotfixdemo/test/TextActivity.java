package holder.sdk.com.hotfixdemo.test;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import holder.sdk.com.hotfixdemo.R;
import holder.sdk.com.hotfixdemo.test.hotfix.HotFixTest;

/**
 * Created by yangjian on 2019/2/26.
 */

public class TextActivity extends Activity implements View.OnClickListener{

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);
        findViewById(R.id.text).setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.text){
            HotFixTest.showToast(this,"点击了测试，但是没有修复");
        }
    }
}
