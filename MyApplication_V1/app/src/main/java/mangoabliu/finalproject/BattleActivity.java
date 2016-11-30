package mangoabliu.finalproject;


import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextPaint;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import mangoabliu.finalproject.Layout.CardLayout;
import mangoabliu.finalproject.Layout.FontTextView;
import mangoabliu.finalproject.Model.BattleModel;
import mangoabliu.finalproject.Model.GameModel;

import mangoabliu.finalproject.explosionfield.*;
/**
 * Created by Lyris on 29/11/16.
 * Modify by SHI Zhongqi on 29/11/16
 */

public class BattleActivity extends AppCompatActivity {

    private FontTextView tv_searching;
    private static MediaPlayer bgm;

    BattleModel battleModel;
    GameModel gameModel;
    Button btn_test,btn_card_choose_confirm,btn_exit;
    RelativeLayout rl_battle_waiting_up,rl_battle_waiting_down,rl_battle_waiting_other_side;
    RelativeLayout rl_battle_otherCard_container1,rl_battle_otherCard_container2,rl_battle_otherCard_container3;
    RelativeLayout rl_battle_mycard_container1,rl_battle_mycard_container2,rl_battle_mycard_container3;
    ImageView imageView_battle_waiting,imageView_battle_border,imageView_battle_win,imageView_battle_lose;
    CardLayout myCard1,myCard2,myCard3,otherCard1,otherCard2,otherCard3;
    int int_state=0;//0在等待匹配，1在选卡，2在等待对方选卡，3在对战；
    int int_last_selectedMycard=0;
    ImageView imageView_attackMark;
    TextView textView_hurt;
    boolean animationFinished=true;
    Animation animationAttackCardGoDown,animationAttackCardGoUp;
    ExplosionField explosionField;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameModel= GameModel.getInstance();
        gameModel.addActivity(this);
        battleModel= BattleModel.getInstance();
        battleModel.initialBattle();
        battleModel.setBattleActivity(this);
        battleModel.setUserCards(gameModel.getUserCards());
        battleModel.setUserAccount(gameModel.getUserAccount());
        Log.i("Battle Activity", gameModel.getUserAccount().getUserName());
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initBattleView();
        //blink
        Animation animSearch= new AlphaAnimation(0.0f, 1.0f);
        animSearch.setDuration(1500); //You can manage the blinking time with this parameter
        animSearch.setStartOffset(20);
        animSearch.setRepeatMode(Animation.REVERSE);
        animSearch.setRepeatCount(Animation.INFINITE);
        tv_searching.startAnimation(animSearch);


        animationAttackCardGoUp = new TranslateAnimation(
                TranslateAnimation.RELATIVE_TO_SELF, 0.0f,
                TranslateAnimation.RELATIVE_TO_SELF, 0.0f,
                TranslateAnimation.RELATIVE_TO_SELF, 0.0f,
                TranslateAnimation.RELATIVE_TO_SELF, -0.035f);
        animationAttackCardGoUp.setDuration(100);
        animationAttackCardGoUp.setFillAfter(true);
        animationAttackCardGoUp.setAnimationListener(new generalAnimationListener());

        animationAttackCardGoDown = new TranslateAnimation(
                TranslateAnimation.RELATIVE_TO_SELF, 0.0f,
                TranslateAnimation.RELATIVE_TO_SELF, 0.0f,
                TranslateAnimation.RELATIVE_TO_SELF, 0.0f,
                TranslateAnimation.RELATIVE_TO_SELF, 0.035f);
        animationAttackCardGoDown.setDuration(100);
        animationAttackCardGoDown.setFillAfter(true);
        animationAttackCardGoDown.setAnimationListener(new generalAnimationListener());

        explosionField = ExplosionField.attach2Window(BattleActivity.this);

        battleModel.applyForFight();
        BGMInit();
    }


    private void initBattleView(){

        setContentView(R.layout.activity_battle);
        btn_test = (Button) findViewById(R.id.btn_battle_test);
        btn_exit = (Button) findViewById(R.id.btn_battle_exit);
        btn_card_choose_confirm=(Button)findViewById(R.id.btn_battle_choose_card_finished);
        rl_battle_waiting_down = (RelativeLayout) findViewById(R.id.relativeLayout_battle_waiting_bg_down);
        rl_battle_waiting_up = (RelativeLayout) findViewById(R.id.relativeLayout_battle_waiting_bg_up);
        rl_battle_waiting_other_side=(RelativeLayout)findViewById(R.id.relativeLayout_battle_waiting_otherside);
        imageView_battle_waiting = (ImageView) findViewById(R.id.imageView_battle_loading);
        imageView_battle_border=(ImageView)findViewById(R.id.imageView_battle_border);
        imageView_battle_win=(ImageView)findViewById(R.id.imageView_win);
        imageView_battle_lose=(ImageView) findViewById(R.id.imageView_lose);
        tv_searching = (FontTextView)findViewById(R.id.battle_searching_text);

        myCard1=(CardLayout)findViewById(R.id.card_battle_mycard_1);
        myCard2=(CardLayout)findViewById(R.id.card_battle_mycard_2);
        myCard3=(CardLayout)findViewById(R.id.card_battle_mycard_3);

        otherCard1=(CardLayout)findViewById(R.id.card_battle_otherside_card_1);
        otherCard2=(CardLayout)findViewById(R.id.card_battle_otherside_card_2);
        otherCard3=(CardLayout)findViewById(R.id.card_battle_otherside_card_3);

        rl_battle_otherCard_container1 = (RelativeLayout) findViewById(R.id.relatvieLayout_battle_otherCard_container_1);
        rl_battle_otherCard_container2 = (RelativeLayout) findViewById(R.id.relatvieLayout_battle_otherCard_container_2);
        rl_battle_otherCard_container3 = (RelativeLayout) findViewById(R.id.relatvieLayout_battle_otherCard_container_3);

        rl_battle_mycard_container1 = (RelativeLayout) findViewById(R.id.relatvieLayout_battle_mycard_container_1);
        rl_battle_mycard_container2 = (RelativeLayout) findViewById(R.id.relatvieLayout_battle_mycard_container_2);
        rl_battle_mycard_container3 = (RelativeLayout) findViewById(R.id.relatvieLayout_battle_mycard_container_3);


        myCard1.setOnClickListener(new clickListener_myCard(1));
        myCard2.setOnClickListener(new clickListener_myCard(2));
        myCard3.setOnClickListener(new clickListener_myCard(3));

        otherCard1.setOnClickListener(new clickListener_otherCard(1));
        otherCard2.setOnClickListener(new clickListener_otherCard(2));
        otherCard3.setOnClickListener(new clickListener_otherCard(3));

        float center_height=imageView_battle_waiting.getDrawable().getIntrinsicHeight();
        float center_width=imageView_battle_waiting.getDrawable().getIntrinsicHeight();
        RotateAnimation animationWaitingIcon = new RotateAnimation(0, 360.0f,center_width/2,center_height/2);
        animationWaitingIcon.setDuration(2000);
        animationWaitingIcon.setStartOffset(120);
        animationWaitingIcon.setRepeatMode(Animation.RESTART);
        animationWaitingIcon.setRepeatCount(Animation.INFINITE);
        LinearInterpolator lin = new LinearInterpolator();
        animationWaitingIcon.setInterpolator(lin);

        imageView_battle_waiting.startAnimation(animationWaitingIcon);

        btn_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                waitingRoomFinished();
            }
        });

        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameFinished();
            }
        });

        btn_card_choose_confirm.setOnClickListener(new clickListener_Confirm());

        btn_exit.setVisibility(View.INVISIBLE);
        btn_exit.setClickable(false);
        imageView_battle_win.setVisibility(View.INVISIBLE);
        imageView_battle_lose.setVisibility(View.INVISIBLE);

        Animation animSearch= new AlphaAnimation(0.0f, 1.0f);
        animSearch.setDuration(1500); //You can manage the blinking time with this parameter
        animSearch.setStartOffset(20);
        animSearch.setRepeatMode(Animation.REVERSE);
        animSearch.setRepeatCount(Animation.INFINITE);
        tv_searching.startAnimation(animSearch);
    }

    public void playerWin(){
        Log.i("BattleActivity","playerWin Called");
        btn_exit.setClickable(true);
        btn_exit.setVisibility(View.VISIBLE);
        AlphaAnimation  ShowAnimation = new AlphaAnimation(0.0f, 1.0f);
        ShowAnimation.setDuration( 1000 );
        ShowAnimation.setFillAfter( true );
        imageView_battle_win.startAnimation(ShowAnimation);
    //用户赢了
    }

    public void OtherWin(){
        Log.i("BattleActivity","OtherWin Called");
        btn_exit.setClickable(true);
        btn_exit.setVisibility(View.VISIBLE);
        AlphaAnimation  ShowAnimation = new AlphaAnimation(0.0f, 1.0f);
        ShowAnimation.setDuration( 1000 );
        ShowAnimation.setFillAfter( true );
        imageView_battle_lose.startAnimation(ShowAnimation);
    //对面赢了
    }

    public void playerAttackOther(int hurt, int mycard, int othercard){
    //用户攻击对面后对面减少的HP
        Log.i("BattleActivity","PlayerAttackOther Called");
        Log.i("BattleActivity","Hurt:"+hurt);
        CardLayout myCard,otherCard;
        switch(mycard){
            case 1:myCard=myCard1;break;
            case 2:myCard=myCard2;break;
            case 3:myCard=myCard3;break;
            default:myCard = null;
        }
        switch(othercard){
            case 1:otherCard=otherCard1;break;
            case 2:otherCard=otherCard2;break;
            case 3:otherCard=otherCard3;break;
            default:otherCard = null;
        }

        otherCard.setCardHP(battleModel.getOtherCardHP(othercard));

        imageView_attackMark = new ImageView(BattleActivity.this);
        imageView_attackMark.setImageResource(R.drawable.attack_mark);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
        lp.addRule(RelativeLayout.CENTER_VERTICAL);
        imageView_attackMark.setLayoutParams(lp);

        otherCard.addView(imageView_attackMark);

        Animation ani_shake = new TranslateAnimation(
                TranslateAnimation.RELATIVE_TO_SELF, 0.01f,
                TranslateAnimation.RELATIVE_TO_SELF, -0.01f);
        ani_shake.setDuration(50);
        ani_shake.setRepeatCount(5);
        ani_shake.setRepeatMode(Animation.REVERSE);
        otherCard.startAnimation(ani_shake);

        float center_height = otherCard.getMeasuredHeight();
        float center_width = otherCard.getMeasuredWidth();
        RotateAnimation animationAttackCard = new RotateAnimation(-3.0f, 3.0f, center_width / 2, center_height / 2);
        animationAttackCard.setDuration(600);
        animationAttackCard.setRepeatMode(Animation.RESTART);

        myCard.startAnimation(animationAttackCard);

        RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp2.addRule(RelativeLayout.CENTER_HORIZONTAL);
        lp2.addRule(RelativeLayout.CENTER_VERTICAL);

        textView_hurt = new TextView(BattleActivity.this);
        textView_hurt.setTextColor(Color.rgb(255, 50, 50));
        textView_hurt.setTextSize(25);
        textView_hurt.setLayoutParams(lp2);//设置TextView的布局
        textView_hurt.setText("-"+hurt);
        TextPaint tp = textView_hurt.getPaint();
        tp.setFakeBoldText(true);
        textView_hurt.setShadowLayer(5F, 5F,5F, Color.BLACK);
        otherCard1.addView(textView_hurt);

        Animation animationHurtTextGoUp = new TranslateAnimation(
                TranslateAnimation.RELATIVE_TO_SELF, 0.0f,
                TranslateAnimation.RELATIVE_TO_SELF, 0.0f,
                TranslateAnimation.RELATIVE_TO_SELF, 0.0f,
                TranslateAnimation.RELATIVE_TO_SELF, -1.5f);
        animationHurtTextGoUp.setDuration(600);
        animationHurtTextGoUp.setFillAfter(true);

        AlphaAnimation  HideAnimation = new AlphaAnimation(1.0f, 0.0f);
        HideAnimation.setDuration( 1000 );
        HideAnimation.setFillAfter( true );

        AnimationSet animationSet = new AnimationSet(false);
        animationSet.addAnimation(animationHurtTextGoUp);
        animationSet.addAnimation(HideAnimation);
        animationSet.setAnimationListener(new myCardHurtFinishAnimation(mycard,othercard));

        imageView_attackMark.startAnimation(HideAnimation);
        textView_hurt.startAnimation(animationSet);

        if(battleModel.getOtherCardHP(othercard)<=0){
            explosionField.explode(otherCard);
        }

    }


    public void otherSideAttackPlayer(int hurt,int mycard,int othercard){
        //对面攻击用户减少的HP
        Log.i("BattleActivity","otherSideAttackPlayer Called");
        Log.i("BattleActivity","Hurt:"+hurt);
        CardLayout myCard,otherCard;
        switch(mycard){
            case 1:myCard=myCard1;break;
            case 2:myCard=myCard2;break;
            case 3:myCard=myCard3;break;
            default:myCard = null;
        }
        switch(othercard){
            case 1:otherCard=otherCard1;break;
            case 2:otherCard=otherCard2;break;
            case 3:otherCard=otherCard3;break;
            default:otherCard = null;
        }

        myCard.setCardHP(battleModel.getMyCardHP(mycard));

        imageView_attackMark = new ImageView(BattleActivity.this);
        imageView_attackMark.setImageResource(R.drawable.attack_mark);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
        lp.addRule(RelativeLayout.CENTER_VERTICAL);
        imageView_attackMark.setLayoutParams(lp);

        myCard.addView(imageView_attackMark);

        Animation ani_shake = new TranslateAnimation(
                TranslateAnimation.RELATIVE_TO_SELF, 0.01f,
                TranslateAnimation.RELATIVE_TO_SELF, -0.01f);
        ani_shake.setDuration(50);
        ani_shake.setRepeatCount(5);
        ani_shake.setRepeatMode(Animation.REVERSE);
        myCard.startAnimation(ani_shake);

        float center_height = otherCard.getMeasuredHeight();
        float center_width = otherCard.getMeasuredWidth();
        RotateAnimation animationAttackCard = new RotateAnimation(-3.0f, 3.0f, center_width / 2, center_height / 2);
        animationAttackCard.setDuration(600);
        animationAttackCard.setRepeatMode(Animation.RESTART);

        otherCard.startAnimation(animationAttackCard);

        RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp2.addRule(RelativeLayout.CENTER_HORIZONTAL);
        lp2.addRule(RelativeLayout.CENTER_VERTICAL);

        textView_hurt = new TextView(BattleActivity.this);
        textView_hurt.setTextColor(Color.rgb(255, 50, 50));
        textView_hurt.setTextSize(25);
        textView_hurt.setLayoutParams(lp2);//设置TextView的布局
        textView_hurt.setText("-"+hurt);
        TextPaint tp = textView_hurt.getPaint();
        tp.setFakeBoldText(true);
        textView_hurt.setShadowLayer(5F, 5F,5F, Color.BLACK);
        myCard.addView(textView_hurt);

        Animation animationHurtTextGoUp = new TranslateAnimation(
                TranslateAnimation.RELATIVE_TO_SELF, 0.0f,
                TranslateAnimation.RELATIVE_TO_SELF, 0.0f,
                TranslateAnimation.RELATIVE_TO_SELF, 0.0f,
                TranslateAnimation.RELATIVE_TO_SELF, -1.5f);
        animationHurtTextGoUp.setDuration(600);
        animationHurtTextGoUp.setFillAfter(true);

        AlphaAnimation  HideAnimation = new AlphaAnimation(1.0f, 0.0f);
        HideAnimation.setDuration( 1000 );
        HideAnimation.setFillAfter( true );

        AnimationSet animationSet = new AnimationSet(false);
        animationSet.addAnimation(animationHurtTextGoUp);
        animationSet.addAnimation(HideAnimation);
        animationSet.setAnimationListener(new otherCardHurtFinishAnimation(mycard,othercard));

        imageView_attackMark.startAnimation(HideAnimation);
        textView_hurt.startAnimation(animationSet);

        if(battleModel.getOtherCardHP(othercard)<=0){
            explosionField.explode(otherCard);
        }
    }

    public void setTurn(String userName){
        Log.i("BattleActivity","setTurn Called");
        //设置中间的Turn信息
    }

    public void setTime(int times){
        //设置旁边的时间
    }

    public void setOtherSideUserName(String userName){
        //设置对面用户名字
    }

    public void waitingRoomFinished(){
        Animation animationWiatingDown = new TranslateAnimation(
                TranslateAnimation.RELATIVE_TO_SELF, 0.0f,
                TranslateAnimation.RELATIVE_TO_SELF, 0.0f,
                TranslateAnimation.RELATIVE_TO_SELF, 0.0f,
                TranslateAnimation.RELATIVE_TO_SELF, 1);
        animationWiatingDown.setDuration(1000);
        animationWiatingDown.setFillAfter(true);
        Animation animationWaitingUp = new TranslateAnimation(
                TranslateAnimation.RELATIVE_TO_SELF, 0.0f,
                TranslateAnimation.RELATIVE_TO_SELF, 0.0f,
                TranslateAnimation.RELATIVE_TO_SELF, 0.0f,
                TranslateAnimation.RELATIVE_TO_SELF, -1);
        animationWaitingUp.setDuration(1000);
        animationWaitingUp.setFillAfter(true);

        rl_battle_waiting_down.startAnimation(animationWiatingDown);
        rl_battle_waiting_up.startAnimation(animationWaitingUp);
        imageView_battle_waiting.clearAnimation();
        tv_searching.clearAnimation();
        imageView_battle_waiting.setVisibility(View.INVISIBLE);
//        imageView_battle_border.setVisibility(View.INVISIBLE);
        rl_battle_waiting_down.setVisibility(View.INVISIBLE);
        rl_battle_waiting_up.setVisibility(View.INVISIBLE);
        tv_searching.setVisibility(View.INVISIBLE);
        int_state=1;
    }

    //请不要改这个...
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return false;
        }
        return false;
    }

    //请不要改这个...
    public void gameFinished(){
        finish();
    }
    //请不要改这个...
    public void updateMyCard(int CardID,int index){

        if(index==1) {
            myCard1.setCardBack(CardID);
            myCard1.setCardHP(battleModel.getCard(CardID).getCardHP());
            myCard1.setCardArmor(battleModel.getCard(CardID).getCardArmor());
            myCard1.setCardAttack(battleModel.getCard(CardID).getCardAttack());
        }

        if(index==2){
            myCard2.setCardBack(CardID);
            myCard2.setCardHP(battleModel.getCard(CardID).getCardHP());
            myCard2.setCardArmor(battleModel.getCard(CardID).getCardArmor());
            myCard2.setCardAttack(battleModel.getCard(CardID).getCardAttack());
        }

        if(index==3){
            myCard3.setCardBack(CardID);
            myCard3.setCardHP(battleModel.getCard(CardID).getCardHP());
            myCard3.setCardArmor(battleModel.getCard(CardID).getCardArmor());
            myCard3.setCardAttack(battleModel.getCard(CardID).getCardAttack());
        }
    }

    //请不要修改范围外的代码
    public void updateOtherSideCard(int CardID,int index){

        if(index==1) {
            otherCard1.setCardBack(CardID);
            otherCard1.setCardHP(battleModel.getOtherCard(CardID).getCardHP());
            otherCard1.setCardArmor(battleModel.getOtherCard(CardID).getCardArmor());
            otherCard1.setCardAttack(battleModel.getOtherCard(CardID).getCardAttack());
        }

        if(index==2){
            otherCard2.setCardBack(CardID);
            otherCard2.setCardHP(battleModel.getOtherCard(CardID).getCardHP());
            otherCard2.setCardArmor(battleModel.getOtherCard(CardID).getCardArmor());
            otherCard2.setCardAttack(battleModel.getOtherCard(CardID).getCardAttack());
        }

        if(index==3){
            otherCard3.setCardBack(CardID);
            otherCard3.setCardHP(battleModel.getOtherCard(CardID).getCardHP());
            otherCard3.setCardArmor(battleModel.getOtherCard(CardID).getCardArmor());
            otherCard3.setCardAttack(battleModel.getOtherCard(CardID).getCardAttack());
            /*可修改范围*/
            Animation animationWaitingUp = new TranslateAnimation(
                    TranslateAnimation.RELATIVE_TO_SELF, 0.0f,
                    TranslateAnimation.RELATIVE_TO_SELF, 0.0f,
                    TranslateAnimation.RELATIVE_TO_SELF, 0.0f,
                    TranslateAnimation.RELATIVE_TO_SELF, -1);
            animationWaitingUp.setDuration(1000);
            animationWaitingUp.setFillAfter(true);
            rl_battle_waiting_other_side.startAnimation(animationWaitingUp);
            /*可修改范围*/
            int_state=3;
        }
    }

    private class clickListener_Confirm implements View.OnClickListener {
        public void onClick(View v) {
            Log.i("BattleActivity","int_state:"+int_state);
            Log.i("BattleActivity","int_state:"+battleModel.chosedCardNo());
            if(int_state==1&&battleModel.chosedCardNo()==3) {

                battleModel.playerCardPickConfirm();
                int_state = 2;
            }
        }
    }

    //请不要改这个...
    private class clickListener_otherCard implements View.OnClickListener {
        int index;//从左往右第几张牌

        public clickListener_otherCard(int id){
            this.index=id;
        }

        public void onClick(View v) {
            if(!animationFinished)
                return;
            if(int_state==3&&battleModel.getOtherCardHP(index)>=0)
                battleModel.attackOtherTarget(index);
        }
    }

    //请不要改这个...
    private class clickListener_myCard implements View.OnClickListener {
        int index;//从左往右第几张牌

        public clickListener_myCard(int id){
            this.index=id;
        }

        public void onClick(View v) {
            if(!animationFinished)
                return;
            if(int_state==1)
                startPickUpCard(index);
            if(int_state==3&&battleModel.getMyCardHP(index)>0) {
                battleModel.chooseMyCard(index);
                switch(index){
                    case 1:myCard1.startAnimation(animationAttackCardGoUp);break;
                    case 2:myCard2.startAnimation(animationAttackCardGoUp);break;
                    case 3:myCard3.startAnimation(animationAttackCardGoUp);break;
                }
                switch(int_last_selectedMycard){
                    case 1:myCard1.startAnimation(animationAttackCardGoDown);break;
                    case 2:myCard2.startAnimation(animationAttackCardGoDown);break;
                    case 3:myCard3.startAnimation(animationAttackCardGoDown);break;
                }
                int_last_selectedMycard=index;

            }

        }
    }
    //请不要改这个...
    public void startPickUpCard(int index){
        BattlePickCardDialog cardPicker = new BattlePickCardDialog(BattleActivity.this,R.style.DialogTranslucent,index);
        cardPicker.show();
    }
    //显示Toast
    public void displayMessage(String msg){
            gameModel.showToast(BattleActivity.this, msg);

    }

    public void restartConfirm(){
        this.int_state=1;
    }

    private class myCardHurtFinishAnimation implements Animation.AnimationListener {
        int mycard,othercard;
        myCardHurtFinishAnimation(int mycard,int othercard){
            this.mycard=mycard;
            this.othercard=othercard;
        }
        @Override
        public void onAnimationRepeat(Animation animation) {

        }

        @Override
        public void onAnimationStart(Animation animation) {
            animationFinished = false;
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            animationFinished = true;
            CardLayout myCard,otherCard;
            switch(mycard){
                case 1:myCard=myCard1;break;
                case 2:myCard=myCard2;break;
                case 3:myCard=myCard3;break;
                default:myCard = null;
            }
            switch(othercard){
                case 1:otherCard=otherCard1;break;
                case 2:otherCard=otherCard2;break;
                case 3:otherCard=otherCard3;break;
                default:otherCard = null;
            }
            otherCard.removeView(imageView_attackMark);
            otherCard.removeView(textView_hurt);
            myCard.startAnimation(animationAttackCardGoDown);
        }
    }

    private class otherCardHurtFinishAnimation implements Animation.AnimationListener {
        int mycard,othercard;
        otherCardHurtFinishAnimation(int mycard,int othercard){
            this.mycard=mycard;
            this.othercard=othercard;
        }
        @Override
        public void onAnimationRepeat(Animation animation) {

        }

        @Override
        public void onAnimationStart(Animation animation) {
            animationFinished = false;
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            animationFinished = true;
            CardLayout myCard,otherCard;
            switch(mycard){
                case 1:myCard=myCard1;break;
                case 2:myCard=myCard2;break;
                case 3:myCard=myCard3;break;
                default:myCard = null;
            }
            myCard.removeView(imageView_attackMark);
            myCard.removeView(textView_hurt);
        }
    }

    private class generalAnimationListener implements Animation.AnimationListener {
        @Override
        public void onAnimationRepeat(Animation animation) {

        }

        @Override
        public void onAnimationStart(Animation animation) {
            animationFinished = false;
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            animationFinished = true;
        }
    }

    public void BGMInit(){
        //循环播放
        if(gameModel.isMusicOn()==1){
            bgm = MediaPlayer.create(this,R.raw.index_bg);
            bgm.start();
            bgm.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
                @Override
                public void onCompletion(MediaPlayer mp) {
                    bgm.start();
                }
            });
        }
    }

    //跳转、中断暂停播放，回activity继续播放
    @Override
    protected void onStart() {
        super.onStart();
        if(gameModel.isMusicOn()==1)  bgm.start();
    }


    @Override
    protected void onStop() {
        super.onStop();
        if(bgm!= null)
            bgm.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(bgm!= null)
            bgm.release();
    }

}
