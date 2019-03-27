package holder.sdk.com.hotfixdemo;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.yj.hotfixsdk.builder.hotfix.XmlDocuData;
import com.yj.hotfixsdk.document.XMLDocumentUtils;
import holder.sdk.com.hotfixdemo.test.TextActivity;

public class MainActivity extends Activity implements View.OnClickListener{

    private EditText mVersionEdit,mVersionCodeEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.mVersionCodeEdit = this.findViewById(R.id.edit_versioncode);
        this.mVersionEdit = this.findViewById(R.id.edit_version);
        findViewById(R.id.create_xml).setOnClickListener(this);
        findViewById(R.id.text).setOnClickListener(this);
        findViewById(R.id.parse_xml).setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.exit(0);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.text){

            startActivity(new Intent(this, TextActivity.class));
        }else if(view.getId() == R.id.create_xml){

            if(this.mVersionEdit.getText() == null ||
                    "".equals(this.mVersionEdit.getText().toString()) ||
                    this.mVersionCodeEdit.getText() == null ||
                    "".equals(this.mVersionCodeEdit.getText().toString()) ){

                Toast.makeText(this,"请输入正确的versioncode或者version",Toast.LENGTH_SHORT).show();
                return;
            }
            XmlDocuData data = new XmlDocuData();
            data.lastUpdated = System.currentTimeMillis();
            data.version = mVersionEdit.getText().toString();
            data.versonCode = Long.parseLong(mVersionCodeEdit.getText().toString());

            if(XMLDocumentUtils.createDocument("hotfixxml",  "hotfix_demo" ,data,XmlDocuData.class)){
                Toast.makeText(this,"生成xml成功",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this,"生成xml失败",Toast.LENGTH_SHORT).show();
            }
        }else if(view.getId() == R.id.parse_xml){

            XmlDocuData data = XMLDocumentUtils.parseDocument("hotfixxml",  "hotfix_demo",XmlDocuData.class);
            if(data != null){

                StringBuffer buffer = new StringBuffer();
                buffer.append("解析结果为 ： " + "versioncode" +
                        data.versonCode + " version : " +
                        data.version+ " updataTime : " + data.lastUpdated);
                Toast.makeText(this,buffer.toString(),Toast.LENGTH_SHORT).show();

            }else{
                Toast.makeText(this,"解析xml失败",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
