package mangoabliu.finalproject;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

/**
 * Created by herenjie on 2016/11/17.
 */

public class LocationDialogView extends Dialog {
    protected LocationDialogView(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置不显示对话框标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置对话框显示哪个布局文件
        setContentView(R.layout.locationdialog);
        //getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //对话框也可以通过资源id找到布局文件中的组件，从而设置点击侦听
        Button bt = (Button) findViewById(R.id.locationDialogCancel);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
