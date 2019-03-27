package holder.sdk.com.hotfixdemo.test.hotfix;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by yangjian on 2019/3/1.
 */

public class HotFixTest {

    public static void showToast(Context context ,String showToast){

        Toast.makeText(context,showToast,Toast.LENGTH_SHORT).show();
    }
}
