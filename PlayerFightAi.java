import java.security.SecureRandom;
import java.util.Scanner;
import java.util.Arrays;
import java.util.ArrayList;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
//import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

//單人模式主程式
public class PlayerFightAi{
	Stage primaryStage;
	Stage whiteBoxStage = new Stage();
	Stage nextCardStageLeft = new Stage();
	Stage nextCardStageRight = new Stage();
	Scene primaryScene, whiteBoxScene;
	Pane pane = new Pane();
	HBox myHandCardsGroup = new HBox();
	HBox myLine = new HBox();
	HBox aiActionGroup = new HBox();
	HBox aiHandCardsGroup, aiLine, artistGroup, mySelf, opponent, settingUI;
	VBox arena, infoUI, drawCardStack, ctrlUI;
	GridPane  myActionGroup;
	AiBrain aiThink = new AiBrain();
	Pane whiteBoxPane = new Pane();

	//對家動作指示物
	ImageView[] purpleActions = new ImageView[8];
	Label[] aiActionSet = new Label[4];

	//對家手牌
	//ImageView[] backs = new ImageView[10];
	ImageView[] aiHands = new ImageView[7];	//TODO : 不夠好

	//音樂
	ImageView musicOn = new  ImageView(new Image("pic/music.png"));
	ImageView musicOff = new  ImageView(new Image("pic/musicN.png"));
	Button musicSwitch = new Button(null, musicOn);
	final String music = getClass().getResource("WhatIsLove.mp3").toString();
	Media media = new Media(music);
	MediaPlayer mediaPlayer = new MediaPlayer(media);

	//設定按鈕
	ImageView gear = new ImageView("pic/gear2.png");
	Button settingBt = new Button(null, gear);
	Stage settingStage;
	Scene settingScene;

	//藝者ImageView
	Artist[] artist7Set = new  Artist[7];

	//抽牌按鈕圖樣與對家手牌圖樣(兩者相同，皆為禮物卡卡背)
	Image cardBack = new Image("pic/back.png");
	ImageView back = new ImageView(cardBack);
	ImageView back2 = new ImageView(cardBack);

	//抽牌按鈕
	Button stackButton = new Button("?", back);
	int stackAmount = 9;
	Label cardNumber = new Label("剩餘張數 : " + stackAmount);

	//使用者提示 房間號碼
	Label tip = new Label("請抽牌~ ");		//TODO : 改名字改成tips 因為會跟multiple搞混
	TextField roomNum = new TextField();		//用來讓使用者輸入房號 TODO : 讓他有用 或以設定鈕的選項取代之

	int handsAmount = 6;
	int openSdCountDown = 6;
	int action = 0;		//紀錄當前動作號碼 [密約]是1 TODO 確認密約真的是1 然後調整成0
	int actionOppo = 0;		//紀錄當前對家動作號碼 [密約]是1 TODO 確認密約真的是1 然後調整成0
	int[] sentGift = new int[7];		//紀錄送出的禮物張數 索引值與藝者的對應為{0-琉璃, 1-杏樹, 2-洋子, 3-千春, 4-絢音, 5-知世, 6-彩葉}
	int[] sentGiftOppo = new int[7];	//紀錄電腦送出的禮物張數
	int round = 0;		//翻譯 : 局(round) 輪(turn) 回合(times) 有更好的翻法請指教
	int turn = 0;		//遊戲一局(round)將進行4輪(turn)8回合(times) 抽牌會使數值+1
	int times = 0;
	int choice = 0;
	int choiceOppo = 0;		//紀錄當前選擇
	int playerOrder = 0;
	int[][] actionDecisionOrder = {{0}, {0, 0}, {0, 0, 0}, {0, 0, 0, 0}};		//actionDecisionOrder //usedCard
	//GiftHands[][] recordViewOfGiftsFor4Actions = {new GiftHands[1], new GiftHands[2], new GiftHands[3], new GiftHands[4]};
	ImageView[][] recordViewOfGiftsFor4Actions = {new ImageView[1], new ImageView[2], new ImageView[3], new ImageView[4]};
	int[][] record4Oppo = {{0}, {1, 1}, {2, 2, 2}, {3, 3, 3, 3}};	//儲存對手每次動作使用的禮物編號
	ImageView[][] recordViewOfGiftsFor4ActionsForAi = {new ImageView[1], new ImageView[2], new ImageView[3], new ImageView[4]};
	GiftHands[][] recordActionsOppo = {new GiftHands[1], new GiftHands[2], new GiftHands[3], new GiftHands[4]};	//TODO : 名字統一化 跟玩家的改成同樣規格
	int outGifts = 0;		//計算送出的禮物張數
	boolean nextRound = true;
	boolean coin = true;	//若為我方先手則標示為true
	boolean currentTurn = true;	//若為我方回合則標示 為true
	boolean showSDalready = false;
	boolean[] doneAction = new boolean[4];
	boolean[] doneActionOppo = new boolean[4];
	boolean again = false;	//若平手 again紀錄為真

	ImageView nextRoundImageView = new ImageView(new Image("pic/nextRound.png"));

	//動作ImageView     dA= doneAction, 1234=[密約][取捨][贈與][競爭], o=orange, p=purple
	ImageView action1ColorO = new ImageView("pic/a1o.png");
	ImageView action2ColorO = new ImageView("pic/a2o.png");
	ImageView action3ColorO = new ImageView("pic/a3o.png");
	ImageView action4ColorO = new ImageView("pic/a4o.png");
	ImageView done1o = new ImageView(new Image("pic/dA1o.png"));
	ImageView done2o = new ImageView(new Image("pic/dA2o.png"));
	ImageView done3o = new ImageView(new Image("pic/dA3o.png"));
	ImageView done4o = new ImageView(new Image("pic/dA4o.png"));
	ImageView[] actionViewSet = {action1ColorO, action2ColorO, action3ColorO, action4ColorO, done1o, done2o, done3o, done4o};	//存所有橘色的動作圖示 TODO : 
	Button myActionSD = new Button("", action1ColorO);
	Button myActionLD = new Button("", action2ColorO);
	Button myActionKG = new Button("", action3ColorO);
	Button myActionLG = new Button("", action4ColorO);
	Button[] myActionSet = {myActionSD, myActionLD, myActionKG, myActionLG};	//存四個動作的按鈕陣列


	//禮物image及ImageView Nil是空白禮物 c是click被選取之意 copy的都暫時沒用到
	Image giftNil = new Image("pic/1nil.png");
	Image gift0 = new Image("pic/5sak.png");
	Image gift1 = new Image("pic/4tea.png");
	Image gift2 = new Image("pic/3umb.png");
	Image gift3 = new Image("pic/3sha.png");
	Image gift4 = new Image("pic/2flu.png");
	Image gift5 = new Image("pic/2scr.png");
	Image gift6 = new Image("pic/2fan.png");
	Image gift0c = new Image("pic/5sakc.png");
	Image gift1c = new Image("pic/4teac.png");
	Image gift2c = new Image("pic/3umbc.png");
	Image gift3c = new Image("pic/3shac.png");
	Image gift4c = new Image("pic/2fluc.png");
	Image gift5c = new Image("pic/2scrc.png");
	Image gift6c = new Image("pic/2fanc.png");
	Image[] gift21 = {gift0, gift0, gift0, gift0, gift0, gift1, gift1, gift1, gift1, gift2, gift2, gift2, gift3, gift3, gift3, gift4, gift4, gift5, gift5, gift6, gift6};
	Image[] gift21copy = {gift0, gift0, gift0, gift0, gift0, gift1, gift1, gift1, gift1, gift2, gift2, gift2, gift3, gift3, gift3, gift4, gift4, gift5, gift5, gift6, gift6};
	Image[] gift21c = {gift0c, gift0c, gift0c, gift0c, gift0c, gift1c, gift1c, gift1c, gift1c, gift2c, gift2c, gift2c, gift3c, gift3c, gift3c, gift4c, gift4c, gift5c, gift5c, gift6c, gift6};
	ImageView gN = new ImageView( giftNil);
	ImageView g0n1 = new ImageView( gift0);
	ImageView g0n2 = new ImageView( gift0);
	ImageView g0n3 = new ImageView( gift0);
	ImageView g0n4 = new ImageView( gift0);
	ImageView g0n5 = new ImageView( gift0);
	ImageView g1n1 = new ImageView( gift1);
	ImageView g1n2 = new ImageView( gift1);
	ImageView g1n3 = new ImageView( gift1);
	ImageView g1n4 = new ImageView( gift1);
	ImageView g2n1 = new ImageView( gift2);
	ImageView g2n2 = new ImageView( gift2);
	ImageView g2n3 = new ImageView( gift2);
	ImageView g3n1 = new ImageView( gift3);
	ImageView g3n2 = new ImageView( gift3);
	ImageView g3n3 = new ImageView( gift3);
	ImageView g4n1 = new ImageView( gift4);
	ImageView g4n2 = new ImageView( gift4);
	ImageView g5n1 = new ImageView( gift5);
	ImageView g5n2 = new ImageView( gift5);
	ImageView g6n1 = new ImageView( gift6);
	ImageView g6n2 = new ImageView( gift6);
	ImageView g0n1c = new ImageView( gift0c);
	ImageView g0n2c = new ImageView( gift0c);
	ImageView g0n3c = new ImageView( gift0c);
	ImageView g0n4c = new ImageView( gift0c);
	ImageView g0n5c = new ImageView( gift0c);
	ImageView g1n1c = new ImageView( gift1c);
	ImageView g1n2c = new ImageView( gift1c);
	ImageView g1n3c = new ImageView( gift1c);
	ImageView g1n4c = new ImageView( gift1c);
	ImageView g2n1c = new ImageView( gift2c);
	ImageView g2n2c = new ImageView( gift2c);
	ImageView g2n3c = new ImageView( gift2c);
	ImageView g3n1c = new ImageView( gift3c);
	ImageView g3n2c = new ImageView( gift3c);
	ImageView g3n3c = new ImageView( gift3c);
	ImageView g4n1c = new ImageView( gift4c);
	ImageView g4n2c = new ImageView( gift4c);
	ImageView g5n1c = new ImageView( gift5c);
	ImageView g5n2c = new ImageView( gift5c);
	ImageView g6n1c = new ImageView( gift6c);
	ImageView g6n2c = new ImageView( gift6c);
	ImageView[] g21 = {g0n1, g0n2, g0n3, g0n4, g0n5, g1n1, g1n2, g1n3, g1n4, g2n1, g2n2, g2n3, g3n1, g3n2, g3n3, g4n1, g4n2, g5n1, g5n2, g6n1, g6n2};
	ImageView[] g21copy = {g0n1, g0n2, g0n3, g0n4, g0n5, g1n1, g1n2, g1n3, g1n4, g2n1, g2n2, g2n3, g3n1, g3n2, g3n3, g4n1, g4n2, g5n1, g5n2, g6n1, g6n2};
	ImageView[] g21c = {g0n1c, g0n2c, g0n3c, g0n4c, g0n5c, g1n1c, g1n2c, g1n3c, g1n4c, g2n1c, g2n2c, g2n3c, g3n1c, g3n2c, g3n3c, g4n1c, g4n2c, g5n1c, g5n2c, g6n1c, g6n2c};

	//被送出的禮物image
	Label[] sentGiftAreaOppo = new Label[7];
	Label[] sentGiftArea = new Label[7];
	ImageView g0x1 = new ImageView(new Image("pic/5sakBtm.png"));
	ImageView g0x2 = new ImageView(new Image("pic/5sakBtmX2.png"));
	ImageView g0x3 = new ImageView(new Image("pic/5sakBtmX3.png"));
	ImageView g0x4 = new ImageView(new Image("pic/5sakBtmX4.png"));
	ImageView g0x5 = new ImageView(new Image("pic/5sakBtmX5.png"));
	ImageView g0x1R = new ImageView(new Image("pic/5sakBtmR.png"));
	ImageView g0x2R = new ImageView(new Image("pic/5sakBtmX2R.png"));
	ImageView g0x3R = new ImageView(new Image("pic/5sakBtmX3R.png"));
	ImageView g0x4R = new ImageView(new Image("pic/5sakBtmX4R.png"));
	ImageView g0x5R = new ImageView(new Image("pic/5sakBtmX5R.png"));
	ImageView g1x1 = new ImageView(new Image("pic/4teaBtm.png"));
	ImageView g1x2 = new ImageView(new Image("pic/4teaBtmX2.png"));
	ImageView g1x3 = new ImageView(new Image("pic/4teaBtmX3.png"));
	ImageView g1x4 = new ImageView(new Image("pic/4teaBtmX4.png"));
	ImageView g1x1R = new ImageView(new Image("pic/4teaBtmR.png"));
	ImageView g1x2R = new ImageView(new Image("pic/4teaBtmX2R.png"));
	ImageView g1x3R = new ImageView(new Image("pic/4teaBtmX3R.png"));
	ImageView g1x4R = new ImageView(new Image("pic/4teaBtmX4R.png"));

	ImageView g2x1 = new ImageView(new Image("pic/3umbBtm.png"));
	ImageView g2x2 = new ImageView(new Image("pic/3umbBtmX2.png"));
	ImageView g2x3 = new ImageView(new Image("pic/3umbBtmX3.png"));
	ImageView g2x1R = new ImageView(new Image("pic/3umbBtmR.png"));
	ImageView g2x2R = new ImageView(new Image("pic/3umbBtmX2R.png"));
	ImageView g2x3R = new ImageView(new Image("pic/3umbBtmX3R.png"));
	ImageView g3x1 = new ImageView(new Image("pic/3shaBtm.png"));
	ImageView g3x2 = new ImageView(new Image("pic/3shaBtmX2.png"));
	ImageView g3x3 = new ImageView(new Image("pic/3shaBtmX3.png"));
	ImageView g3x1R = new ImageView(new Image("pic/3shaBtmR.png"));
	ImageView g3x2R = new ImageView(new Image("pic/3shaBtmX2R.png"));
	ImageView g3x3R = new ImageView(new Image("pic/3shaBtmX3R.png"));

	ImageView g4x1 = new ImageView(new Image("pic/2fluBtm.png"));
	ImageView g4x2 = new ImageView(new Image("pic/2fluBtmX2.png"));
	ImageView g4x1R = new ImageView(new Image("pic/2fluBtmR.png"));
	ImageView g4x2R = new ImageView(new Image("pic/2fluBtmX2R.png"));
	ImageView g5x1 = new ImageView(new Image("pic/2scrBtm.png"));
	ImageView g5x2 = new ImageView(new Image("pic/2scrBtmX2.png"));
	ImageView g5x1R = new ImageView(new Image("pic/2scrBtmR.png"));
	ImageView g5x2R = new ImageView(new Image("pic/2scrBtmX2R.png"));
	ImageView g6x1 = new ImageView(new Image("pic/2fanBtm.png"));
	ImageView g6x2 = new ImageView(new Image("pic/2fanBtmX2.png"));
	ImageView g6x1R = new ImageView(new Image("pic/2fanBtmR.png"));
	ImageView g6x2R = new ImageView(new Image("pic/2fanBtmX2R.png"));

	ImageView[][] sentGiftAreaPic = {{g0x1, g0x2, g0x3, g0x4, g0x5}, 
				{g1x1, g1x2, g1x3, g1x4}, 
				{g2x1, g2x2, g2x3}, {g3x1, g3x2, g3x3}, 
				{g4x1, g4x2}, {g5x1, g5x2}, {g6x1, g6x2}};

	ImageView[][] sentGiftAreaPicOppo = {{g0x1R, g0x2R, g0x3R, g0x4R, g0x5R}, 
				{g1x1R, g1x2R, g1x3R, g1x4R}, 
				{g2x1R, g2x2R, g2x3R}, {g3x1R, g3x2R, g3x3R}, 
				{g4x1R, g4x2R}, {g5x1R, g5x2R}, {g6x1R, g6x2R}};


	ImageView[] nullLabelSet = new ImageView[14];

	ArrayList<GiftHands> hHands = new ArrayList<>();
	ArrayList<GiftHands> allGifts = new ArrayList<>();
	ArrayList<GiftHands> aHands = new ArrayList<>();
	ArrayList<GiftHands> outHands = new ArrayList<>();
	ArrayList<GiftHands> givingByAi = new ArrayList<>();
	ArrayList<GiftHands> leftHands = new ArrayList<>();
	ArrayList<GiftHands> rightHands = new ArrayList<>();
	ArrayList<GiftHands> kGList = new ArrayList<>();
	ArrayList<GiftHands> combination1 = new ArrayList<>();
	ArrayList<GiftHands> combination2 = new ArrayList<>();
	ArrayList<ImageView> backs = new ArrayList<>();
	ArrayList<ImageView> aBack = new ArrayList<>();
	int[] handsRecordOppo = new int[7];
	int[] cardStack = new int[21];

	String[] artistStatus = {"first", "first", "first", "first", "first", "first", "first"};
	int[] sd7s = {0, 0, 0, 0, 0, 0, 0};
	int[] ld7s = {0, 0, 0, 0, 0, 0, 0};
	String aiCode = "";
	GiftHands[] pendding= null;
	int passCode = 0;
	boolean turnDone = false;
//=======================================================此行為280行=========
	//主要函式
	public void play(Stage mainMenu, Stage primaryStage){
		this.primaryStage = primaryStage;	//將傳進的主要視窗址參複製到本身的primaryStage(at行39)

		//initLayout();			//主遊戲視窗排版初始化
		drawCardSetting(stackButton);	//從牌庫抽牌的按鈕設置
		fourActionUseSetting();		//四個動作的按鈕設定
		//displayHands();			//展示手牌
		musicSetting();			//音樂按鈕
		settingButton(mainMenu);		//設定按鈕
		initLayout();			//主遊戲視窗排版初始化
		fourActionReviewSetting();		//四個動作按鈕回顧設定
		showPrimaryStage();		//主要視窗設置以及展示

		whiteBoxSetting();			//設置白箱

		//if(round == 1) coin = TossTheCoin();	//if好像不需要 因為這行好像只會跑一次
		coin = TossTheCoin();		//決定由誰先手
		currentTurn = (coin)? true : false;	//確定當前回合	//TODO 或許可以整合成一行 看coin 跟 currentTurn是否有其他用途
		//System.out.print("====="+((coin)? "由你":"電腦")+"先手=====");
		//if(!currentTurn) aiCode = aiThink.thinkOut(sentGiftOppo, sentGift, artistStatus, doneActionOppo, doneAction, aHands, sd7s, ld7s);
		//processAiCode(primaryStage, aiCode, record4Oppo);
	}//main Stage End

//=======================================================
//============我=是=分隔=線==============主stage結束線===========
//=======================================================

	//#11從牌庫抽牌的按鈕設置
	public void drawCardSetting(Button stackButton){
		//滑鼠部分
		stackButton.setOnAction(e ->{
			currentTurn = true;
			stackButton.setDisable(true);
			stackAmount--;
			cardNumber.setText("剩餘張數 : "+stackAmount);
			System.out.print("\n[程序員] 抽牌! 牌堆張數剩"+stackAmount+"張\n");
			turn++;			//新的一輪(可能會因為後手而須加判斷)
			System.out.print("\n=================[程序員] 現在是第 " + turn + "輪=================");
			lockAll(1);			//使手牌可以選取
			hHands.add(allGifts.get(0));	//抽牌
			allGifts.remove(0);		//將抽出的牌移除
			System.out.print("\n\n[程序員] 現在最上面的卡是"+allGifts.get(0).getCardKinds()+"\n\n");
			knowNextCard();
			//myHandCardsGroup.getChildren().add(hHands.size()-1);
			myHandCardsGroup.getChildren().add(hHands.get(handsAmount));
			handsAmount++;
			addHandsAnimate();
			//addHandsAnimateKeyBoard();
			tip.setText("請選取你欲執行動作之相對應張數的牌 並雙擊該動作圖示");
			choiceOppo = 0;
		});

		//鍵盤部分(TODO : 很後面的整合工作 分割出獨立Method)
		//stackButton.setOnKeyPressed(e->{

	}//[結束]從牌庫抽牌的按鈕設置

	//#12四個動作的按鈕設定
	public void fourActionUseSetting(){
		//原始註解 : 裡面分四段 第一段action = routine() 第二三段 動手牌&後續動作 第四段 電腦動作
		for(int i = 0; i < myActionSet.length; i++){
			final int ii = i;
			myActionSet[ii].setOnMouseClicked(f -> {
				System.out.print("\n[程序員] 偵測到有一動作被按下，選取張數為 : "+outGifts+"\n");
				if(outGifts == (ii+1) && !doneAction[ii]){	//按的數量對而且動作還沒做
					action = (ii+1);				//此回合動作
					doneAction[ii] = true;			//做過的動作要做標記
					myActionSet[ii].setGraphic(actionViewSet[ii+4]);	//使這個動作變成用過的圖案
					outGifts = 0;				//送出的禮物張數降為0
					handsAmount -= (ii+1);			//持有手牌數減少
					openSdCountDown--;
					System.out.print("\n[程序員] 密約倒數..."+openSdCountDown);
					finalState(openSdCountDown);

					//記錄並移除選取的牌
					removeAndRecord(outHands, recordViewOfGiftsFor4Actions[action-1], actionDecisionOrder[action-1], hHands, myHandCardsGroup);
					outHands = new ArrayList<GiftHands>();
					lockAll(0);					//使手牌無法再被點選

					//電腦對贈與競爭反應，若不需要則直接執行回合
					aiReactAndExcuteTurn(primaryStage, sentGiftOppo, sentGift, artistStatus, doneActionOppo, doneAction, aHands, sd7s, ld7s, actionDecisionOrder[action-1], outHands);
					//電腦做動作(主視窗(贈與競爭做視窗跟隨才會用到), 電腦已放上的禮物, 我已放上的禮物, 藝者狀態, 電腦動作狀況, 我的動作狀況,
					//										電腦的牌, 電腦密約狀況, 取捨狀況, 卡號紀錄, 選定牌池)
				}
				else{				
					final Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("小提示");
					alert.setHeaderText("選取的張數不對或已使用");
					alert.setContentText("請選取"+(ii+1)+"張卡");
					alert.showAndWait();
				}
			});
		}
	}//[結束]四個動作的按鈕設定

	//#13四個動作按鈕回顧設定
	public void fourActionReviewSetting(){
		Stage actionStage = new Stage();
		actionStage.setAlwaysOnTop(true);
		for(int i = 0; i < myActionSet.length; i++){
			final int ii = i;
			myActionSet[ii].setOnMouseEntered(e ->{	//懸浮視窗
				if(doneAction[ii]){	//TODO: +bind 加bind
					showUsedAction(actionStage, e.getSceneX()-e.getX(), e.getSceneY()-e.getY(), recordViewOfGiftsFor4Actions[ii], 1);
				}
			});
			myActionSet[ii].setOnMouseExited(e ->{
				actionStage.hide();
			});
			aiActionSet[ii].setOnMouseEntered(e ->{	//懸浮視窗
				if(doneActionOppo[ii]){	//TODO: +bind 加bind
					showUsedAction(actionStage, e.getSceneX()-e.getX(), e.getSceneY()-e.getY(), recordViewOfGiftsFor4ActionsForAi[ii], -1);
				}
			});
			aiActionSet[ii].setOnMouseExited(e ->{
				actionStage.hide();
			});
		}
	}//[結束]四個動作按鈕回顧設定

	//#15音樂按鈕
	public void musicSetting(){
		//音樂
		musicSwitch.setOnAction(e->{
			if(mediaPlayer.getStatus() == Status.PLAYING){
				mediaPlayer.pause();
				musicSwitch.setGraphic(musicOff);
			}
			else{
				mediaPlayer.play();
				musicSwitch.setGraphic(musicOn);
			}
		});
	}//[結束]音樂按鈕

	//#16設定按鈕
	public void settingButton(Stage mainMenu){
		//設定
		settingStage = initSettingLayout(mainMenu, primaryStage, settingStage, settingScene, mediaPlayer);
		settingBt.setOnAction(e->{
			mediaPlayer.setVolume(0.1);
			settingStage.show();
		});
	}//[結束]設定按鈕

	//遊戲開始及重啟新局時呼叫reset重新設定
	public void reset(){
		//紫色行動標記圖片
		purpleActions = new ImageView[8];
		for(int i = 0; i < purpleActions.length; i++){
			purpleActions[i] = new ImageView(new Image("pic/ap"+i+".png"));
			purpleActions[i].fitWidthProperty().bind(pane.widthProperty().divide(22));	//讓寬約是整個螢幕寬的 1/22
			purpleActions[i].fitHeightProperty().bind(purpleActions[i].fitWidthProperty());	//讓高跟寬一樣大
		}
		//橘色行動標記圖片




	}
	/*#17主遊戲視窗排版初始化
	*將整個視窗布局切成4*3的12塊
	*[數字]表示此段會放在哪段
	*未標示即表示此段落程式碼不需受到呈現*/
	public void initLayout(){
		pane = new Pane();	//TODO : 這邊除了重置pane以外 其他也要初始化 還沒寫(後期作業)
		/*
		*(音量調整到一開始)(或不用)
		*
		*
		*
		*
		*/
		/*//自家按鈕設置
		for(int i = 0; i < myActionSet.length; i++){
			myActionSet[i].setMinHeight(42);
			myActionSet[i].setMinWidth(42);
			myActionSet[i].prefWidthProperty().bind(pane.widthProperty().divide(17));
			myActionSet[i].prefHeightProperty().bind(myActionSet[i].prefWidthProperty());
			myActionSet[i].setScaleX(1);
			myActionSet[i].setScaleY(1);
		}

		//玩家的動作集合
		for(int i = 0; i < actionViewSet.length; i++){
			actionViewSet[i].fitWidthProperty().bind(pane.widthProperty().divide(16));
			actionViewSet[i].fitHeightProperty().bind(actionViewSet[i].fitWidthProperty());
			actionViewSet[i].setScaleX(0.75);
			actionViewSet[i].setScaleY(0.75);
		}*/


		//電腦的動作集合(然後併到aiLine)[1]
		for(int i = 0; i < purpleActions.length; i++){
			purpleActions[i] = new ImageView(new Image("pic/ap"+i+".png"));
			purpleActions[i].fitWidthProperty().bind(pane.widthProperty().divide(22));	//讓寬約是整個螢幕寬的 1/22
			purpleActions[i].fitHeightProperty().bind(purpleActions[i].fitWidthProperty());	//讓高跟寬一樣大
		}
		for(int i = 0; i < aiActionSet.length; i++){
			aiActionSet[i] = new Label();
			aiActionSet[i].setGraphic(purpleActions[i]);
			aiActionSet[i].prefWidthProperty().bind(pane.widthProperty().divide(22));	//讓寬約是整個螢幕寬的 1/22
			aiActionSet[i].prefHeightProperty().bind(aiActionSet[i].prefWidthProperty());	//讓高跟寬一樣大	//讓高跟寬一樣大
			aiActionGroup.getChildren().add(aiActionSet[i]);
		}
		aiActionGroup.spacingProperty().bind(pane.widthProperty().divide(192));

		//對家手牌設置(然後併到aiLine)[2~3]
		for(int i = 0; i < 10; i++){
			backs.add(new ImageView(cardBack));
			backs.get(i).fitWidthProperty().bind(pane.widthProperty().divide(22));		//讓寬約是整個螢幕寬的 1/22
			backs.get(i).fitHeightProperty().bind(backs.get(i).fitWidthProperty().multiply(1.38));	//讓高是寬的1.38倍
		}
		aiHandCardsGroup = new HBox();
		for(int i = 0; i < 6; i++){	//aHands.size(); i++){
			aBack.add(backs.get(i));
			aiHandCardsGroup.getChildren().add(aBack.get(i));
		}

		aiHandCardsGroup.setSpacing(10);
		//上方ai資訊列(合併以上兩個程式段)[1~3]
		aiLine = new HBox(aiActionGroup, aiHandCardsGroup);
		aiLine.setAlignment(Pos.CENTER);
		System.out.print("\n[程序員] ai手牌以及動作 準備完成");

		//藝者設置[5~7中層]
		for(int i = 0; i < artist7Set.length; i++){
			artist7Set[i] = new Artist(i, "first");
			artist7Set[i].getArtistView().fitWidthProperty().bind(pane.widthProperty().divide(8.5));
			artist7Set[i].getArtistView().fitHeightProperty().bind(artist7Set[i].getArtistView().fitWidthProperty().multiply(1.53));
		}
		artistGroup = new HBox(artist7Set[0], artist7Set[1], artist7Set[2], artist7Set[3], artist7Set[4], artist7Set[5], artist7Set[6]);
		artistGroup.spacingProperty().bind(pane.widthProperty().divide(135));
		//兩方送出的禮物列白板
		for(int i = 0; i < nullLabelSet.length; i++){
			nullLabelSet[i] = new ImageView(new Image("pic/nullLabel.png"));
			nullLabelSet[i].fitWidthProperty().bind(artist7Set[i % 7].getArtistView().fitWidthProperty().multiply(0.8));
			nullLabelSet[i].fitHeightProperty().bind(nullLabelSet[i].fitWidthProperty().multiply(0.5));
		}
		for(int i = 0; i < sentGiftAreaOppo.length; i++){
			sentGiftArea[i] = new Label("", nullLabelSet[i]);
			sentGiftAreaOppo[i] = new Label("", nullLabelSet[i+7]);
		}
		for(int qq = 0; qq < sentGiftAreaPic.length; qq++) for(int kk = 0; kk < sentGiftAreaPic[qq].length; kk++){
			sentGiftAreaPic[qq][kk].fitWidthProperty().bind(artist7Set[qq].getArtistView().fitWidthProperty().multiply(0.8));
			sentGiftAreaPic[qq][kk].fitHeightProperty().bind(sentGiftAreaPic[qq][kk].fitWidthProperty().multiply(0.5));
			sentGiftAreaPicOppo[qq][kk].fitWidthProperty().bind(artist7Set[qq].getArtistView().fitWidthProperty().multiply(0.8));
			sentGiftAreaPicOppo[qq][kk].fitHeightProperty().bind(sentGiftAreaPicOppo[qq][kk].fitWidthProperty().multiply(0.5));
		}
		//對方的禮物列設置[5~7上層]
		opponent = new HBox(sentGiftAreaOppo[0], sentGiftAreaOppo[1], sentGiftAreaOppo[2], sentGiftAreaOppo[3], sentGiftAreaOppo[4], sentGiftAreaOppo[5], sentGiftAreaOppo[6]);
		opponent.spacingProperty().bind(artistGroup.spacingProperty().multiply(4));
		opponent.setAlignment(Pos.CENTER);
		//我方的禮物列設置[5~7下層]
		mySelf = new HBox(sentGiftArea[0], sentGiftArea[1], sentGiftArea[2], sentGiftArea[3], sentGiftArea[4], sentGiftArea[5], sentGiftArea[6]);
		mySelf.spacingProperty().bind(artistGroup.spacingProperty().multiply(4));
		mySelf.setAlignment(Pos.CENTER);
		//整合藝者以及雙方送出禮物列[5~7]
		arena = new VBox(opponent, artistGroup, mySelf);
		arena.spacingProperty().bind(pane.heightProperty().divide(128));
		System.out.print("\n[程序員] 藝者暨雙方送出禮物列 準備完成");
		//整合場面玩家所見資訊[1~3, 5~7]
		infoUI = new VBox(aiLine, arena);
		infoUI.spacingProperty().bind(pane.heightProperty().divide(32));
		infoUI.layoutXProperty().bind(pane.widthProperty().divide(192));
		infoUI.layoutYProperty().bind(pane.heightProperty().divide(128));
		pane.getChildren().add(infoUI);

		//音樂播放
		mediaPlayer.pause();	//TODO 正式版應可改為play 這編寫pause是因為測試程式的時候我不想一直聽開頭
		mediaPlayer.setVolume(0.5);
		mediaPlayer.setCycleCount(Timeline.INDEFINITE);
		//音量及設定鈕設置[4]
		musicSwitch.setMaxWidth(80);
		musicSwitch.setMaxHeight(80);
		musicOn.fitWidthProperty().bind(pane.widthProperty().divide(24));
		musicOn.fitHeightProperty().bind(musicOn.fitWidthProperty());
		musicOff.fitWidthProperty().bind(pane.widthProperty().divide(24));
		musicOff.fitHeightProperty().bind(musicOff.fitWidthProperty());
		settingBt.setMaxWidth(80);
		settingBt.setMaxHeight(80);
		gear.fitWidthProperty().bind(pane.widthProperty().divide(24));
		gear.fitHeightProperty().bind(gear.fitWidthProperty());
		settingUI = new HBox(musicSwitch, settingBt);
		System.out.print("\n[程序員] 設定按鈕以及音量按鈕 準備完成");
		System.out.print("\n[程序員] 手牌 : ");		//為了在CMD瀏覽時寫的
		//drawCardStack為牌庫的集合[8]
		drawCardStack = new VBox(cardNumber, stackButton);
		drawCardStack.spacingProperty().bind(pane.heightProperty().divide(128));
		back.fitWidthProperty().bind(pane.widthProperty().divide(10));
		back.fitHeightProperty().bind(back.fitWidthProperty().multiply(1.38));
		//ctrlUI為音量設定與抽牌牌庫的三個UI集合
		ctrlUI = new VBox(settingUI, drawCardStack);
		ctrlUI.spacingProperty().bind(pane.heightProperty().divide(10));	//TODO:排版更好看 精算是除以32之後加多少property 看不懂此註解可刪除 已經夠好了
		ctrlUI.layoutXProperty().bind(pane.widthProperty().divide(1.13));
		ctrlUI.layoutYProperty().bind(pane.heightProperty().divide(54));
		pane.getChildren().add(ctrlUI);

		//玩家提示
		tip.layoutXProperty().bind(pane.widthProperty().divide(2.4));
		tip.layoutYProperty().bind(pane.heightProperty().divide(1.5));
		pane.getChildren().add(tip);

		//自家按鈕設置
		for(int i = 0; i < myActionSet.length; i++){
			myActionSet[i].setMinHeight(20);
			myActionSet[i].setMinWidth(20);
			myActionSet[i].prefWidthProperty().bind(pane.widthProperty().divide(18));
			myActionSet[i].prefHeightProperty().bind(myActionSet[i].prefWidthProperty());
			myActionSet[i].setScaleX(1);
			myActionSet[i].setScaleY(1);
		}

		//玩家的動作集合
		for(int i = 0; i < actionViewSet.length; i++){
			actionViewSet[i].fitWidthProperty().bind(pane.widthProperty().divide(16));
			actionViewSet[i].fitHeightProperty().bind(actionViewSet[i].fitWidthProperty());
			actionViewSet[i].setScaleX(0.75);
			actionViewSet[i].setScaleY(0.75);
		}
		myActionGroup = new GridPane();
		myActionGroup.add(myActionSD, 0, 0);
		myActionGroup.add(myActionLD, 1, 0);
		myActionGroup.add(myActionKG, 0, 1);
		myActionGroup.add(myActionLG, 1, 1);
	}//[結束]主遊戲視窗排版初始化

	//#18主要視窗設置以及展示
	public void showPrimaryStage(){
		//primaryScene = new Scene(pane, 1280, 800);
		primaryScene = new Scene(pane, 1600, 1080);	//1007測試用 看到可以改回1280 800
		//primaryScene = new Scene(pane, 1920, 1080);	//布景
		primaryStage.setTitle("Hanamikoji~");		//標題
		primaryStage.setScene(primaryScene);		//舞臺之布景
		randomStack(cardStack);			//亂數牌堆
		displayHands();				//展示手牌
		//primaryStage.setFullScreen(true);		//全螢幕 要按esc解除的那種
		lockAll(0);					//使手牌不能按
		primaryStage.show();			//舞臺
	}

	//牌堆設置。亂數牌堆，以及調整各禮物牌面自動縮放。第一行 陣列元素給定索引值；後七行 隨機交換位置 作為這次洗牌後的結果
			//TODO 看看自動縮放能不能拿到initLayout
	public void randomStack(int[] stack){
		for(int i = 0; i < stack.length; i++) stack[i] = i;
		for(int i = 0; i < stack.length; i++){
			int random = (int)(Math.random()*21);
			int tempInt = stack[i];
			stack[i] = stack[random];
			stack[random] = tempInt;
		}
		//1015編輯修改 看到可以直接打開註解並刪除下列2行
		/*	0-4 櫻花髮簪
		*	5-8 茶具
		*	9-11 油紙傘
		*	12-14 三味線
		*	15-16 日式筱笛
		*	17-18 古書卷軸
		*	19-20 舞扇*/
		/*int[] fakeStack1 = {9, 10, 11, 15, 17, 19, 14, 5, 6, 7, 8};
		//int[] fakeStack2 = {16, 18, 20, 12, 13, 0, 1, 2, 3, 4};
		for(int i = 0; i < fakeStack1.length; i++){
			stack[2*i] = fakeStack1[i];
			if(i > 9) break;
			stack[2*i+1] = fakeStack2[i];
		}*/
//		System.arraycopy(fakeStack, 0, stack, 0, stack.length);
		for(int i = 0; i < stack.length; i++){
			allGifts.add(new GiftHands(i, stack[i]));
			for(int j = 0; j < 8; j++){
				allGifts.get(i).getTypeStyle()[j].fitWidthProperty().bind(pane.widthProperty().divide(10));
				allGifts.get(i).getTypeStyle()[j].fitHeightProperty().bind(allGifts.get(i).getTypeStyle()[j].fitWidthProperty().multiply(1.38));
			}
		}
	}

	//抽好雙方手牌與改變對家手牌數量
	public void displayHands(){
		for(int k = 0; k < 6; k++){
			hHands.add(allGifts.get(2*k+playerOrder));   //將該實際編號相對到的禮物顯示
			aHands.add(allGifts.get(2*k+1-playerOrder));
			myHandCardsGroup.getChildren().add(hHands.get(k));
		}
		myHandCardsGroup.spacingProperty().bind(pane.widthProperty().divide(192));
		myLine = new HBox(myHandCardsGroup, myActionGroup);
		myLine.spacingProperty().bind(pane.widthProperty().divide(19.2));
		/*myLine.setLayoutX(15);
		myLine.setLayoutY(770);*/ //0929測試 看到可以打開註解 然後刪除行1100附近的
		myLine.spacingProperty().bind(pane.widthProperty().divide(19.2));
		myLine.layoutXProperty().bind(pane.widthProperty().divide(128));
		myLine.layoutYProperty().bind(pane.heightProperty().divide(1.4));
		pane.getChildren().add(myLine);
    		for(int twelveTimes = 0; twelveTimes < 12; twelveTimes++) allGifts.remove(0);


		//0986aiHands[6].imageProperty().set(giftNil);	//此時b7會被換成NIL圖案喔!!
	}

	//使手牌擁有選取動畫(滑鼠) TODO : (或許不需要)使全部牌堆的牌都有此屬性
	public void addHandsAnimate(){
		for(int i = 0; i < hHands.size(); i++){			//每張手牌都遵守下面幾行程式碼唷
			final int ii = i;
			hHands.get(ii).setOnAction(e ->{		//按鈕事件
				System.out.print("\n[程序員] "+((hHands.get(ii).getPicked())? "不選此張禮物":"選擇此張禮物")+"，原禮物列車序 : "+hHands.get(ii).getOutOrder());
				if(!hHands.get(ii).getPicked()){	//若此牌處於未被選取的狀態
					outGifts++;
					hHands.get(ii).transType(outGifts);		//型態◆轉換！
					System.out.print("oytgGIft : "+outGifts);
					outHands.add(hHands.get(ii));		//新夥伴加入出牌列車(她是其中一個被出出去的牌)
					if(outGifts == 4){
						changeImageView(outHands);	//如果4張(會使用競爭)就變圖片(變成分組色)
						lockAll(0);
					}
				}
				else{				//若此牌已選
					outGifts--;
					//下面這行的註解 迴圈(拿到這張牌的序號(1or2or3or4); 在不比出牌列車長度大(<4)的情況下; 控制元遞增) 出牌列車的這張牌的出牌編號減少1
					for(int j = hHands.get(ii).getOutOrder(); j < outHands.size(); j++) outHands.get(j).setOutOrder( outHands.get(j).getOutOrder()-1 );
					outHands.remove(hHands.get(ii).getOutOrder()-1);	//離開出牌列車
					hHands.get(ii).transType(outGifts);			//型態◆轉換！
					hHands.get(ii).setOutOrder(0);			//不選此禮物了，將之被選順序歸零
					reloadImageView(outHands);				//更新圖片
					lockAll(1);
				}

			});
		}
	}
	//如果手牌已選4張(會使用競爭) 前三張就變圖片(變成分組色)
	public void changeImageView(ArrayList<GiftHands> al){
		for(int i = 0; i <= 2; i++) al.get(i).setCurrentView( al.get(i).getTypeStyle()[al.get(i).getOutOrder()+4]);
	}
	//出牌列車的序號若變就要更新圖片	//TODO 這個程式針對呼叫物件GiftHands裡面的方法getTypeStyle()是否考慮換成裡面的另一個getOneOfTheTypeStyle
	public void reloadImageView(ArrayList<GiftHands> al){
		for(int i = 0; i < al.size(); i++) al.get(i).setCurrentView( al.get(i).getTypeStyle()[al.get(i).getOutOrder()]);
	}

	//紀錄被移除的牌以及移除手牌節點
	public void removeAndRecord(ArrayList<GiftHands> outHand, ImageView[] record4, int[] recordOrder, ArrayList<GiftHands> hHand, HBox group){
		//紀錄
		for(int k = 0; k < outHand.size(); k++){
			record4[k] = outHand.get(k).getOneOfTheTypeStyle(0);
			recordOrder[k] = outHand.get(k).getCardKinds();
			System.out.print("\n[程序員] 此回合所使用牌之牌堆順序 : "+outHand.get(k).getStackOrder());
		}
		//移除
		for(int k = hHands.size() - 1; k >= 0; k--){
			if(hHand.get(k).getPicked()){
				group.getChildren().remove(k);
				hHand.remove(k);
			}
		}
	}

	//(for電腦)移除手牌以及紀錄被移除的牌
	public void removeAndRecordAiHands(ArrayList<GiftHands> outHand, ImageView[] aiRecord4, int[] aiRecordOrder, ArrayList<GiftHands> aiHand){
		System.out.print("\n[程序員] 進移除前手牌長度是"+aiHand.size());
		//紀錄
		for(int k = 0; k < outHand.size(); k++){
			aiRecord4[k] = outHand.get(k).getOneOfTheTypeStyle(0);
			aiRecordOrder[k] = outHand.get(k).getCardKinds();
			System.out.print("\n[程序員] 此回合所使用牌之牌堆順序 : "+outHand.get(k).getStackOrder()+", 卡種為 <"+outHand.get(k).getCardKinds()+">");
			//aBack.remove(aHands.size());
			//aiHandCardsGroup.getChildren().remove(aHands.size()-1);
		}
		//移除
		for(int k = aiHand.size() - 1; k >= 0; k--){	
			if(aiHand.get(k).getPicked()){
				aiHand.remove(k);
			}
		}
		System.out.print("\n[程序員] 進移除後手牌長度是"+ aiHand.size());

		//return aiHand;	
	}

	//鎖住手牌與開啟手牌 引數為0即鎖住所有未選取之手牌，引數為1則開啟所有未選取之手牌
	public void lockAll(int kW){
		for(int i = 0; i<hHands.size(); i++){
			if(!hHands.get(i).getPicked()) hHands.get(i).setDisable((kW == 0)? true:false);
		}
	}

	//電腦對贈與競爭反應，若不需要則直接執行回合
	//public void aiReactAndExcuteTurn(Stage masterStage, int action, int choiceOppo, int[] sent_Gift, int[] sent_Gift_Player, String[] artistBelongTo, boolean[] used_Action, boolean[] used_Action_Player, ArrayList<GiftHands> handsCard, int[] sd7statistics, int[] ld7statistics){
	public void aiReactAndExcuteTurn(Stage ownerStage, int[] sent_Gift, int[] sent_Gift_Player, String[] artistBelongTo, boolean[] used_Action, boolean[] used_Action_Player, ArrayList<GiftHands> aiHandsCard, int[] sd7statistics, int[] ld7statistics, int[] someGift, ArrayList<GiftHands> alms){
		if(action == 3){
			System.out.print("\n程式碼這裡需要加上電腦思考時間");
			System.out.print("\n\t\t\t櫻 茶 傘 弦 笛 書 扇");
			System.out.print("\n傳遞前sentGift : \t"+Arrays.toString(sent_Gift));
			System.out.print("\n傳遞前sentGiftPlayer : \t"+Arrays.toString(sent_Gift_Player));
			choiceOppo = aiThink.reactKindGift(sent_Gift, sent_Gift_Player, artistBelongTo, used_Action, used_Action_Player, aiHandsCard, sd7statistics, ld7statistics, someGift);
			reactKindGiftOppo(ownerStage, choiceOppo, recordViewOfGiftsFor4Actions[action-1]);
		}
		else if(action == 4){
			//+電腦思考時間
			choiceOppo = aiThink.reactLoathGift(sent_Gift, sent_Gift_Player, artistBelongTo, used_Action, used_Action_Player, aiHandsCard, sd7statistics, ld7statistics, combination1, combination2);
			reactLoathGiftOppo(ownerStage, choiceOppo, recordViewOfGiftsFor4Actions[action-1]);
		}
		//+電腦思考時間
		stackAmount--;		//電腦抽牌
		//aBack.add(new Label("", backs[aHands.size()]));
		//aiHandCardsGroup.getChildren().add(aBack.get(aHands.size()-1));
		aHands.add(allGifts.get(0));	//電腦抽牌
		allGifts.remove(0);		//將抽出的牌移除
		currentTurn = false;
		cardNumber.setText("剩餘張數 : "+stackAmount);
		System.out.print("\n[程序員] 輪到電腦");
		givingByAi = aiThink.thinkOut(sent_Gift, sentGift, artistStatus, doneActionOppo, doneAction, aHands, sd7s, ld7s);
		processAiGiving(ownerStage,givingByAi);
	}


	//解讀電腦的動作
	public void processAiGiving(Stage ownerStage, ArrayList<GiftHands> giving){
		System.out.print("\n[程序員] 解讀時手牌長度是"+ giving.size());
		String[] actionName = {"[密約]", "[取捨]", "[贈與]", "[競爭]"};
		String[] actionReact = {" 輪到你了\t請抽牌", "請選擇一張你想要的牌"};
		actionOppo = giving.size() - 1;		//-1原因 : 密約會傳1張卡，size為1，但密約實為動作0。其他動作依此類推。
		doneActionOppo[actionOppo] = true;		//標示電腦做過這個動作了
		tip.setText("\n對方使用 "+actionName[actionOppo]+"，"+actionReact[actionOppo/2]);
		System.out.print("\n[程序員] 對方使用 "+actionName[actionOppo]+"，"+actionReact[actionOppo/2]);
		removeAndRecordAiHands(giving, recordViewOfGiftsFor4ActionsForAi[actionOppo], record4Oppo[actionOppo], aHands);
		aiActionSet[actionOppo].setGraphic(purpleActions[actionOppo+4]);

		//贈與以及競爭須給玩家回應 其他情況就直接解開牌庫不能點的限制 讓玩家可以抽牌
		switch(actionOppo){
		case 0 :
		case 1 :
			stackButton.setDisable(false);
			break;
		case 2 :
			threePickOne(ownerStage, giving, recordViewOfGiftsFor4ActionsForAi[actionOppo]);	//贈與
			break;
		case 3 :
			twoGroupPickOne(ownerStage, giving, recordViewOfGiftsFor4ActionsForAi[actionOppo]);	//競爭
			break;
		default :
			System.out.print("\nerror!!! The action isn't normal");
		}
	}



	//我方使用[贈與]而對方的回應是.....
	public Stage reactKindGiftOppo(Stage masterStage, int reactChoice, ImageView[] record4){
		Button resultgot = new Button("知道了~");
		HBox gifts = new HBox(record4[0], record4[1], record4[2]);
		gifts.setAlignment(Pos.TOP_CENTER);
		HBox picked;
		if(reactChoice == 0) picked = new HBox(record4[0]);
		else if(reactChoice == 1) picked = new HBox(record4[1]);
		else picked = new HBox(record4[2]);
		picked.setAlignment(Pos.TOP_CENTER);
		VBox showResult = new VBox(picked, resultgot, gifts);
		showResult.setAlignment(Pos.TOP_CENTER);
		showResult.setSpacing(50);
		Scene ss= new Scene(showResult, 900, 650);
		Stage resultKG = new Stage();
		resultKG.setScene(ss);
		resultKG.initOwner(masterStage);	//視窗跟隨
		resultKG.setAlwaysOnTop(true);	//視窗置頂 : 若此回合對方亦使用贈/競此回饋需擺在對方的贈/競之前
		resultKG.show();
//		for(int i = 0; i < )

		//其實這裡的record4.length就是3 三張中 被挑走的那張為對方禮物贈送區+1 其餘為自己禮物贈送區+1
		for(int i = 0; i < record4.length; i++){
			if(i == reactChoice) countSentGiftAndChange(sentGiftOppo, sentGiftAreaOppo, sentGiftAreaPicOppo, actionDecisionOrder[2][i]);
			else countSentGiftAndChange(sentGift, sentGiftArea, sentGiftAreaPic, actionDecisionOrder[2][i]);
		}
		//關閉視窗
		resultgot.setOnAction(e->{
			resultKG.close();
		});

		return resultKG;
	}	
	//我方使用[競爭]而對方的回應是.....	//loathGift
	public void reactLoathGiftOppo(Stage masterStage, int reactChoice, ImageView[] record4){
		Button resultGot = new Button("知道ㄌ");
		HBox my2gifts = new HBox(record4[0], record4[1], record4[2], record4[3]);
		my2gifts.setAlignment(Pos.TOP_CENTER);
		HBox picked;
		if(reactChoice == 0) picked = new HBox(record4[0], record4[1]);
		else		picked = new HBox(record4[2], record4[3]);
		picked.setAlignment(Pos.TOP_CENTER);
		VBox showResult = new VBox(picked, resultGot, my2gifts);
		showResult.setSpacing(30);
		showResult.setAlignment(Pos.TOP_CENTER);
		Scene ss= new Scene(showResult, 900, 650);
		Stage resultLG = new Stage();
		resultLG.setScene(ss);
		resultLG.initOwner(masterStage);	//視窗跟隨
		resultLG.setAlwaysOnTop(true);	//視窗置頂 : 若此回合對方亦使用贈/競此回饋需擺在對方的贈/競之前
		resultLG.show();
		//thread.sleep();

		//其實這裡的record4.length就是4 兩組中 被挑走的那組為對方禮物贈送區+1 其餘為自己禮物贈送區+1
		for(int i = 0; i < record4.length; i++){
			if(i == reactChoice*2 || i == reactChoice*2+1) countSentGiftAndChange(sentGiftOppo, sentGiftAreaOppo, sentGiftAreaPicOppo, actionDecisionOrder[3][i]);
			else countSentGiftAndChange(sentGift, sentGiftArea, sentGiftAreaPic, actionDecisionOrder[3][i]);
		}
		//關閉視窗
		resultGot.setOnAction(e->{
			resultLG.close();
		});
	}

	//對方使用[贈與]
	public int threePickOne(Stage ownerStage, ArrayList<GiftHands> giving, ImageView[] record4){
		Button[] choices = new Button[3];
		for(int i = 0; i < choices.length; i++) choices[i] = new Button("", record4[i]);	//建立三個無字串但附圖片的按鈕放進區域陣列choices

		Label explain = new Label("對方使用[贈與] 請選擇一張你想要的禮物");
		HBox kgList = new HBox(choices[0], choices[1], choices[2]);
		kgList.setSpacing(15);
		kgList.setAlignment(Pos.TOP_CENTER);
		VBox kgVbox = new VBox(explain, kgList);
		kgVbox.setSpacing(30);
		kgVbox.setAlignment(Pos.TOP_CENTER);
		Scene kgScene = new Scene(kgVbox, 900, 650);
		Stage kgStage = new Stage();
		kgStage.setScene(kgScene);
		kgStage.initOwner(ownerStage);
		kgStage.show();

		for(int i = 0; i < choices.length; i++){
			final int ii = i;
			choices[ii].setOnAction(e ->{
				if(stackAmount != 1) stackButton.setDisable(false);
				kgStage.close();
				choice = ii;
				tip.setText("請抽牌");
				//其實這裡的record4.length就是3 三張中 自己挑走的那張為自己禮物贈送區+1 其餘為對方禮物贈送區+1
				for(int j = 0; j < record4.length; j++){
					if(j == choice) countSentGiftAndChange(sentGift, sentGiftArea, sentGiftAreaPic, record4Oppo[actionOppo][j]);
					else countSentGiftAndChange(sentGiftOppo, sentGiftAreaOppo, sentGiftAreaPicOppo, record4Oppo[actionOppo][j]);
				}
				openSdCountDown--;
				System.out.print("\n[程序員] 密約倒數..."+openSdCountDown);
				finalState(openSdCountDown);
			});
		}

		return choice;
	}

	//對方使用[競爭]
	public int twoGroupPickOne(Stage ownerStage, ArrayList<GiftHands> giving, ImageView[] record4){
		Button[] choices = {new Button("這組"), new Button("這組")};
		Label[] lgLabel = new Label[4];
		for(int i = 0; i < lgLabel.length; i++) lgLabel[i] = new Label("", record4[i]);

		Label explain = new Label("對方使用[競爭] 請選擇一組你想要的禮物");
		HBox lgList1 = new HBox(lgLabel[0], lgLabel[1], choices[0]);
		lgList1.setSpacing(15);
		lgList1.setAlignment(Pos.TOP_CENTER);
		HBox lgList2 = new HBox(lgLabel[2], lgLabel[3], choices[1]);
		lgList2.setSpacing(15);
		lgList2.setAlignment(Pos.TOP_CENTER);

		VBox lgVbox = new VBox(explain, lgList1, lgList2);
		lgVbox.setSpacing(30);
		lgVbox.setAlignment(Pos.TOP_CENTER);
		Scene lgScene= new Scene(lgVbox, 900, 650);
		Stage lgStage = new Stage();
		lgStage.setScene(lgScene);
		lgStage.initOwner(ownerStage);
		lgStage.show();
		for(int i = 0; i < choices.length; i++){
			final int ii = i;
			choices[ii].setOnAction(e ->{
				if(stackAmount != 1) stackButton.setDisable(false);
				lgStage.close();
				choice = ii;
				tip.setText("請抽牌");
				//其實這裡的record4.length就是4 兩組中 自己挑走的那組為自己禮物贈送區+1 其餘為對方禮物贈送區+1
				for(int j = 0; j < record4.length; j++){
					if(j == choice*2 || j == choice*2+1) countSentGiftAndChange(sentGift, sentGiftArea, sentGiftAreaPic, record4Oppo[actionOppo][j]);
					else countSentGiftAndChange(sentGiftOppo, sentGiftAreaOppo, sentGiftAreaPicOppo, record4Oppo[actionOppo][j]);
				}
				openSdCountDown--;
				System.out.print("\n[程序員] 密約倒數..."+openSdCountDown);
				finalState(openSdCountDown);
			});
		}

		return choice;
	}

	//數送出的禮物並且顯示(誰的送禮顯示, 牌堆, 各種類禮物張數, 在牌堆的順序)(廢棄)
	/*public void countSentGiftAndChange(int[] sentGift, int[] cardStack, Label[] sentGiftArea, ImageView[][] sentGiftAreaPic, int stackOrder){
		System.out.print("\nstackOrder is "+stackOrder + "///   sentGift.length is" + sentGift.length);
		sentGift[stackOrder]++;
		for(int i = 0; i < sentGift.length; i++){
			if(sentGift[i] <= 0) continue;
			sentGiftArea[i].setGraphic(sentGiftAreaPic[   i   ][ sentGift[i]-1 ]);
		}
	}*/

	//數送出的禮物並且顯示(各種類禮物張數, (玩家的或電腦的)送禮顯示, (玩家的或電腦的)競爭區圖片庫, 禮物種類)
	public void countSentGiftAndChange(int[] sentGift, Label[] sentGiftArea, ImageView[][] arenaPic, int cardKind){
		sentGift[cardKind]++;
		for(int i = 0; i < sentGift.length; i++){
			if(sentGift[i] <= 0) continue;
			sentGiftArea[i].setGraphic(arenaPic[   i   ][ sentGift[i]-1 ]);
		}
	}

	//牌堆的哪張牌實際上在陣列的代號0~6 0是櫻花髮簪 6是舞扇 僅受countSentGiftAndChange呼叫
	public int OrderToAskGiftN(int n){
		int ans = 0;
		if(n<5) ans = 0;
		else if(n < 9) ans = 1;
		else if(n < 12) ans = 2;
		else if(n < 15) ans = 3;
		else if(n < 17) ans = 4;
		else if(n < 19) ans = 5;
		else if(n < 21) ans = 6;
		else ans = -1;

		return ans;
	}

	//開[密約]
	public void finalState(int countDown){
		if(countDown == 0){
			countSentGiftAndChange(sentGift, sentGiftArea, sentGiftAreaPic, actionDecisionOrder[0][0]);
			countSentGiftAndChange(sentGiftOppo, sentGiftAreaOppo, sentGiftAreaPicOppo, record4Oppo[0][0]);

			Label sdOppo = new Label("對方的密約", recordViewOfGiftsFor4ActionsForAi[0][0]);
			Label sd = new Label("你的[密約]", recordViewOfGiftsFor4Actions[0][0]);
			Button gotResult = new Button("好");
			VBox vv = new VBox(sdOppo, gotResult, sd);
			vv.setAlignment(Pos.TOP_CENTER);
			vv.setSpacing(20);
			StackPane pp = new StackPane(vv);
			Scene ss = new Scene(pp, 900, 650);
			Stage showSD = new Stage();
			showSD.setScene(ss);
			showSD.show();
			gotResult.setOnAction(e -> { showSD.close();});


			Stage resultStage = new Stage();	//結果視窗
			endRound(resultStage, sentGift, sentGiftOppo);
	
			/*final Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("小提示");	//TODO 塞字串 你贏惹
			alert.setHeaderText("選取的張數不對或已使用");	//遊戲結束 進入下一關
			//alert.setContentText("請按確定");
			alert.showAndWait();*/

			cardNumber.setText("v 被挑掉的牌是 <"+allGifts.get(0).getCardName()+">");
			stackButton.setDisable(false);
			stackButton.setGraphic(allGifts.get(0).getOneOfTheTypeStyle(0));
			stackButton.setOnAction(e ->{
			});
			//show結果的懸浮視窗 進入就show, 離開就close
			stackButton.setOnMouseEntered(e ->{
				resultStage.show();
				if(nextRound) stackButton.setGraphic(nextRoundImageView);
			});
			stackButton.setOnMouseExited(e ->{
				resultStage.close();
				stackButton.setGraphic(allGifts.get(0).getOneOfTheTypeStyle(0));
			});
		}
	}

	//公布結果
	//public boolean endRound(Stage result, int[] sentGift, int[] sentGiftOppo){
	public void endRound(Stage result, int[] sentGift, int[] sent_Gift_Oppo){
		nextRound = false;
		int[] worth = {5, 4, 3, 3, 2, 2, 2};
		int point = 0;
		int pointOppo = 0;
		int person = 0;
		int personOppo = 0;
		for(int i = 0; i < sentGift.length; i++){
			if(sentGift[i] > sent_Gift_Oppo[i]){
				person++;
				point += worth[i];
			}
			else if(sent_Gift_Oppo[i] > sentGift[i]){
				personOppo++;
				pointOppo += worth[i];
			}
		}

		Label numbers = new Label("\n你的魅力值 : \t\t" + point + "\n你的藝者人數 : \t" + person + "\n對方的魅力值 : \t" + pointOppo + "\n對方的藝者人數 : \t" + personOppo);
		Label winLose;
		if(point >= 11) winLose = new Label("你贏惹");
		else if(pointOppo >= 11) winLose = new Label("你輸ㄌ");
		else if(person >= 4) winLose = new Label("你贏惹");
		else if(personOppo >= 4) winLose = new Label("你輸ㄌ");
		else{
			 winLose = new Label("平手 進入下一局");
			nextRound = true;
		}

		//VBox endList = new VBox(numbers, winLose, gotEnd);
		VBox endList = new VBox(numbers, winLose);

		endList.setAlignment(Pos.TOP_CENTER); 
		endList.setSpacing(20);
		Scene endScene = new Scene(endList, 300, 200);
		result.setScene(endScene);
		//Stage endStage = new Stage();
		//endStage.setScene(endScene);
		//endStage.show();

		//gotEnd.setOnAction(e -> { endStage.close();});
	}

	//展示用過的動作(懸浮視窗)
	public void showUsedAction(Stage usedStage, double layoutX, double layoutY, ImageView[] viewRecorder, int direction){
		int cardLength = viewRecorder.length;
		int xMove = (direction > 0)? -40 : 0;
		int yMove = (direction > 0)? -340 : 140;
		HBox usedActionHBox = new HBox();
		usedActionHBox.setAlignment(Pos.CENTER);
		for(int i = 0; i < cardLength; i++) usedActionHBox.getChildren().add(viewRecorder[i]);
		StackPane usedPane = new StackPane(usedActionHBox);
		Scene usedScene = new Scene(usedPane, 210*cardLength, 300);
		usedStage.setScene(usedScene);
		usedStage.setX(layoutX+xMove);
		usedStage.setY(layoutY+yMove);
		//usedStage.initStyle(StageStyle.TRANSPARENT);
		usedStage.show();
	}

	//設定鈕按下去跳出視窗排版與按鈕事件設置
	public Stage initSettingLayout(Stage mainMenu, Stage masterStage, Stage settingStage, Scene settingScene, MediaPlayer mp){
		Button backGame = new Button("返回遊戲");
		Button backMenu = new Button("回主選單");
		backGame.setLayoutX(15);
		backGame.setLayoutY(250);
		backMenu.setLayoutX(205);
		backMenu.setLayoutY(250);
		Pane settingPane = new Pane();
		settingPane.getChildren().add(backGame);
		settingPane.getChildren().add(backMenu);
		settingScene = new Scene(settingPane, 300, 300);
		settingStage = new Stage();
		settingStage.setScene(settingScene);
		settingStage.initStyle(StageStyle.UNDECORATED);
		settingStage.initOwner(masterStage);

		final Stage stageCopy = settingStage;
		//返回遊戲鍵
		backGame.setOnAction(e->{
			for(double g = 0.1; g <= 0.5; g+=0.00003) mp.setVolume(g);
			stageCopy.close();
		});
		//返回主選單鍵
		backMenu.setOnAction(e->{
			mp.stop();
			mainMenu.show();
			masterStage.close();
		});

		return settingStage;
	}

	//白箱的設置
	public void whiteBoxSetting(){
		initWhiteBoxTest(primaryStage);
		whiteBoxScene = new Scene(whiteBoxPane, 1800, 800);
		whiteBoxStage.setTitle("White Box");
		whiteBoxStage.setScene(whiteBoxScene);
		//whiteBoxStage.show();
	}

	//決定由誰先手
	public boolean TossTheCoin(){
		SecureRandom sr = new SecureRandom();
		int randomN = sr.nextInt(2);
		if(randomN == 0) return true;

		return false;
	}

	public void initWhiteBoxTest(Stage primaryStage){
		GiftHands gh_temp;
		for(int i = 0; i < 6; i++){
			gh_temp = new GiftHands(2*i+1, cardStack[2*i+1]);
			gh_temp.setScaleX(0.5);
			gh_temp.setScaleY(0.5);
			leftHands.add(gh_temp);
			gh_temp = new GiftHands(2*i, cardStack[2*i]);
			gh_temp.setScaleX(0.5);
			gh_temp.setScaleY(0.5);
			rightHands.add(gh_temp);
		}

		HBox surfTwoHands_Left = new HBox();
		HBox surfTwoHands_Right = new HBox();
		for (int i = 0; i < 6; i++) {
			surfTwoHands_Left.getChildren().add(leftHands.get(i));
			surfTwoHands_Right.getChildren().add(rightHands.get(i));
		}
 		surfTwoHands_Left.setSpacing(-100);	//因圖片是往中間縮小導致必須間距內縮才能讓圖感覺在原本位置
		surfTwoHands_Right.setSpacing(-100);	//因圖片是往中間縮小導致必須間距內縮才能讓圖感覺在原本位置


		HBox surfTwoHands_All = new HBox(surfTwoHands_Left, surfTwoHands_Right);
		surfTwoHands_All.setLayoutX(-10);	//因圖片是往中間縮小導致必須左移才能讓圖感覺在原本位置
		surfTwoHands_All.setLayoutY(250);
		surfTwoHands_All.setSpacing(230);
		whiteBoxPane.getChildren().add(surfTwoHands_All);

		//牌庫預覽按鈕裝進白箱
		Button stackPreview = new Button(null, new ImageView(new Image("pic/back.png")));
		stackPreview.setLayoutX(780);
		stackPreview.setLayoutY(30);
		whiteBoxPane.getChildren().add(stackPreview);
		//whiteBoxStage.initOwner(primaryStage);

		//說明文字裝進白箱
		Label worthy = new Label("價值");
		Label fillText = new Label("滿檢查");
		Label zeroText = new Label("零檢查");
		Label tZeroText = new Label("真零檢查");
		Label sdSymbol = new Label("密約標記");
		Label ldSymbol = new Label("取捨標記");
		Label trashSymbol = new Label("垃圾標記");
		Label kgSymbol = new Label("贈與標記");
		Label lgSymbol = new Label("競爭標記");
		VBox textVBox = new VBox(worthy, fillText, zeroText, tZeroText, sdSymbol, ldSymbol, trashSymbol, kgSymbol, lgSymbol);
		textVBox.setSpacing(15);
		textVBox.setAlignment(Pos.CENTER);
		textVBox.setLayoutX(830);
		textVBox.setLayoutY(480);
		whiteBoxPane.getChildren().add(textVBox);

		//牌庫預覽按鈕按鈕事件
		boolean viewAll = false;
		knowNextCard();
		/*stackPreview.setOnAction(e->{
			if(viewAll){
				viewAll = false;
				//新增後續卡牌展示敘述.show();
			}
			else{
				viewAll = true;
				//新增後續卡牌展示敘述.close();			
			}
		});*/
		stackPreview.setOnMouseEntered(e ->{
			//final boolean viewAlll = viewAll;	//這行沒用
			//if(!viewAlll) nextCardStage.show();
			double left_up_pointX = e.getSceneX()-e.getX();
			double left_up_pointY = e.getSceneY()-e.getY();
			System.out.print("標準位置X/Y : " + left_up_pointX + "/" + left_up_pointY + "\n");
			nextCardStageLeft.setX(left_up_pointX-150);
			nextCardStageLeft.setY(left_up_pointY+90);
			nextCardStageRight.setX(left_up_pointX+280);
			nextCardStageRight.setY(left_up_pointY+90);
			nextCardStageLeft.show();
			nextCardStageRight.show();
		});
		stackPreview.setOnMouseExited(e ->{
			nextCardStageLeft.close();
			nextCardStageRight.close();
		});
	}

	//更新白箱會顯示的下一張卡
	public void knowNextCard(){
		//GiftHands aiNextCard;
		//GiftHands playerNextCard;
		ImageView aiNextCard;
		ImageView playerNextCard;

		/*if(currentTurn){
			aiNextCard = allGifts.get(1).getTypeOriginal();
			playerNextCard = allGifts.get(0).getTypeOriginal();
			System.out.print("CUUUUUURRR啦\n");
		}
		else{
			aiNextCard = allGifts.get(0).getTypeOriginal();
			playerNextCard = allGifts.get(1).getTypeOriginal();
			System.out.print("馬德里不思義\n");
		}
			aiNextCard = allGifts.get(1).getTypeOriginal();
			playerNextCard = allGifts.get(0).getTypeOriginal();*/

		/*Scene nextCardSceneLeft = new Scene(new Pane(aiNextCard));
		Scene nextCardSceneRight = new Scene(new Pane(playerNextCard));
		nextCardStageLeft.setScene(nextCardSceneLeft);
		nextCardStageRight.setScene(nextCardSceneRight);*/
	}
}






/*		KeyCode的寫法
		Button.setOnKeyPressed(e->{
			if(e.getCode() == KeyCode.ENTER){
				//some code
			}
		});
*/