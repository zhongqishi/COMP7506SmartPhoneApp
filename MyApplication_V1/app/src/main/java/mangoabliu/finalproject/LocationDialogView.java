package mangoabliu.finalproject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.Random;

import mangoabliu.finalproject.Model.GameModel;
import mangoabliu.finalproject.Model.StepService;

import static mangoabliu.finalproject.DisplayImageOptionsUtil.getDisplayImageOptions;

/**
 * Created by 10836 on 2016-11-16.
 */

// Add PossibleCard by Lyrisxu  11-27

public class LocationDialogView extends Dialog {

    GameModel gameModel;
    int clickedLoc;
    ImageView image_pCard;


    //Add Style /Lyris 11-26
    protected LocationDialogView(Context context, int loc, int style ) {
        super(context, style);
        clickedLoc = loc;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置不显示对话框标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置对话框显示哪个布局文件
        setContentView(R.layout.dialog_location);
        //getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        //对话框也可以通过资源id找到布局文件中的组件，从而设置点击侦听
        Button cancel = (Button) findViewById(R.id.locationDialogCancel);
        Button start = (Button) findViewById(R.id.locationGo);
        Button drop = (Button) findViewById(R.id.dropCard);

        //PossibleCardDisplay
        image_pCard =(ImageView) findViewById(R.id.locationImage);
        int pCardID = possibleCardGenerator();
        initCardData(pCardID);

        gameModel = GameModel.getInstance();

        if (gameModel.getUserAccount().getCurrentLocId() == clickedLoc)
        {
            start.setVisibility(View.GONE);
            start.setEnabled(false);
        }

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        start.setOnClickListener(new startTripListener());

        drop.setOnClickListener(new drop_DropCardListener());

    }

    //PossibleCardGenerator - Random
    private int possibleCardGenerator(){
        Random rand = new Random();
        int pOneCard = rand.nextInt(18);
        Resources res = this.getContext().getResources();
        String[] CardsName = res.getStringArray(R.array.cards_name);
        Context context = image_pCard.getContext();
        int pCardID = context.getResources().getIdentifier(CardsName[pOneCard], "drawable", context.getPackageName());
        return pCardID;
    }

    //Init Card
    private void initCardData(int pCardID){

        ImageLoader imageLoader;
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(this.getContext()));
        String imageUriFront = "drawable://" + pCardID;
        ImageLoader.getInstance().displayImage(imageUriFront, image_pCard, getDisplayImageOptions());
    }


    private class drop_DropCardListener implements View.OnClickListener {

        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(v.getContext(), CardDropActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            v.getContext().startActivity(intent);

        }
    }

    private class startTripListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            v.getContext().startService(new Intent(v.getContext(),StepService.class));
            //v.getContext().stopService(new Intent(v.getContext(),StepService.class));
            gameModel.updateTargetLocation(gameModel.getUserAccount().getUserId(),
                    clickedLoc);
            dismiss();
        }

    }


}

