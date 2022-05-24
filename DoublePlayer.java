//0916
import java.security.SecureRandom;
import java.util.Scanner;
import java.util.Arrays;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.animation.Animation;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.animation.FadeTransition;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.geometry.Pos;


//image載入圖片, imageView是圖片的變數單位(我的理解啦)
public class DoublePlayer{

	Label aiAction1 = new Label(null, new ImageView("pic/a1p.png"));
	Label aiAction2 = new Label(null, new ImageView("pic/a2p.png"));
	Label aiAction3 = new Label(null, new ImageView("pic/a3p.png"));
	Label aiAction4 = new Label(null, new ImageView("pic/a4p.png"));
	Label playerOrder = new Label("Player");
	Label cardNumber = new Label("剩餘張數 : ");
	Label tip = new Label("首位玩家按下產生鈕產生房號之後按開始，次位玩家請輸入房號後按確認 ");
	Label room = new Label("房間代碼 : ");
	Label pleaseEnterActionCode = new Label("請輸入對家動作代碼 : ");
	Label yourAN = new Label("          Your action number   :   ");
	Label[] aiAction = {aiAction1, aiAction2, aiAction3, aiAction4};
	TextField roomNum = new TextField();
	TextField blank = new TextField();
	TextField actionDiscribe = new TextField("000000");

	int existStackN = 9;
	int action = 0;		//紀錄當前動作號碼 密約是1
	int actionOppo = 0;		//紀錄當前對家動作號碼 密約是1
	int[] sentGift = new int[7];
	int[] sentGiftOppo = new int[7];
	int currentHandsN = 6;
	int plusTurnEqualHandsNumber = 6;
	int turn = 0;		//遊戲一局將進行4輪(turn)8回合(times) 抽牌會使數值+1
	int times = 0;		//抽牌會使數值+1 & 確認也會使數值+1
	int player = 2;		//先手玩家為1，後手為2。按下一個只有先手玩家會按的buttton[產生]的時候會改為1
	int choice = 0;
	int choiceOppo = 0;		//我是record4Oppo[0][0] 其實這樣寫不夠好
	int[] fakeA = new int[21];	//第一位玩家生產房間號碼備份
	int[] reA = new int[42];	//第二位玩家構築牌堆用(重建)
	int[] reS = new int[21];	//第二位玩家構築牌堆用(還原)
	int[][] record4 = {{7}, {7}, {7, 7}, {7, 7, 7}, {7, 7, 7, 7}};		//actionDecisionOrder
	int[][] record4Oppo = {{7}, {7}, {7, 7}, {7, 7, 7}, {7, 7, 7, 7}};	//record4Oppo[0][0]存5表示贈與回應 6競爭回應
	int playerExistAction = 4;
	//int player2ExistAction = 4;
	int outGifts = 0;		//計算送出的禮物張數
	boolean currentTurn = false;	//若為我方回合為true
	boolean showSDalready = false;
	boolean[] doneAction = new boolean[5];
	boolean[] doneActionOppo = new boolean[5];
	boolean[] pickCard = new boolean[7];
	boolean[] existHands = {true, true, true, true, true, true, true};


	Pane pane = new Pane();
	Scene scene2;

	//音樂按鈕圖片
	ImageView musicOn = new  ImageView(new Image("pic/music.png"));
	ImageView musicOff = new  ImageView(new Image("pic/musicN.png"));


	//藝者ImageView
	Button[] artistChange = new Button[7];
	int[] abcdef = {0, 0, 0, 0, 0, 0, 0};

	ImageView artist0 = new  ImageView(new Image("pic/artist0first.png"));
	ImageView artist1 = new  ImageView(new Image("pic/artist1first.png"));
	ImageView artist2 = new  ImageView(new Image("pic/artist2first.png"));
	ImageView artist3 = new  ImageView(new Image("pic/artist3first.png"));
	ImageView artist4 = new  ImageView(new Image("pic/artist4first.png"));
	ImageView artist5 = new  ImageView(new Image("pic/artist5first.png"));
	ImageView artist6 = new  ImageView(new Image("pic/artist6first.png"));

	ImageView artist0player = new  ImageView(new Image("pic/artist0player.png"));
	ImageView artist1player = new  ImageView(new Image("pic/artist1player.png"));
	ImageView artist2player = new  ImageView(new Image("pic/artist2player.png"));
	ImageView artist3player = new  ImageView(new Image("pic/artist3player.png"));
	ImageView artist4player = new  ImageView(new Image("pic/artist4player.png"));
	ImageView artist5player = new  ImageView(new Image("pic/artist5player.png"));
	ImageView artist6player = new  ImageView(new Image("pic/artist6player.png"));

	ImageView artist0neutral = new  ImageView(new Image("pic/artist0neutral.png"));
	ImageView artist1neutral = new  ImageView(new Image("pic/artist1neutral.png"));
	ImageView artist2neutral = new  ImageView(new Image("pic/artist2neutral.png"));
	ImageView artist3neutral = new  ImageView(new Image("pic/artist3neutral.png"));
	ImageView artist4neutral = new  ImageView(new Image("pic/artist4neutral.png"));
	ImageView artist5neutral = new  ImageView(new Image("pic/artist5neutral.png"));
	ImageView artist6neutral = new  ImageView(new Image("pic/artist6neutral.png"));

	ImageView artist0opponent = new  ImageView(new Image("pic/artist0opponent.png"));
	ImageView artist1opponent = new  ImageView(new Image("pic/artist1opponent.png"));
	ImageView artist2opponent = new  ImageView(new Image("pic/artist2opponent.png"));
	ImageView artist3opponent = new  ImageView(new Image("pic/artist3opponent.png"));
	ImageView artist4opponent = new  ImageView(new Image("pic/artist4opponent.png"));
	ImageView artist5opponent = new  ImageView(new Image("pic/artist5opponent.png"));
	ImageView artist6opponent = new  ImageView(new Image("pic/artist6opponent.png"));
	//ImageView[] artist7Set = {artist0, artist1, artist2, artist3, artist4, artist5, artist6};
	ImageView[][] currentIdea = { {artist0, artist1, artist2, artist3, artist4, artist5, artist6}, 
				{artist0player, artist1player, artist2player, artist3player, artist4player, artist5player, artist6player},
				{artist0neutral, artist1neutral, artist2neutral, artist3neutral, artist4neutral, artist5neutral, artist6neutral}, 
				{artist0opponent, artist1opponent, artist2opponent, artist3opponent, artist4opponent, artist5opponent, artist6opponent}};


	//動作ImageView     dA= doneAction, 1234=密約取捨贈與競爭, o=orange, p=purple
	ImageView dontWork = new ImageView(new Image("pic/dA1o.png"));
	ImageView done1o = new ImageView(new Image("pic/dA1o.png"));
	ImageView done2o = new ImageView(new Image("pic/dA2o.png"));
	ImageView done3o = new ImageView(new Image("pic/dA3o.png"));
	ImageView done4o = new ImageView(new Image("pic/dA4o.png"));
	ImageView done1p = new ImageView(new Image("pic/aif1u.png"));
	ImageView done2p = new ImageView(new Image("pic/aif2u.png"));
	ImageView done3p = new ImageView(new Image("pic/aif3u.png"));
	ImageView done4p = new ImageView(new Image("pic/aif4u.png"));
	ImageView[] doneActionO = {dontWork, done1o, done2o, done3o, done4o};

	//卡背image及ImageView
	Image cardBack = new Image("pic/back.png");
	ImageView back = new ImageView(cardBack);
	ImageView back2 = new ImageView(cardBack);
	ImageView b1 = new ImageView(cardBack);
	ImageView b2 = new ImageView(cardBack);
	ImageView b3 = new ImageView(cardBack);
	ImageView b4 = new ImageView(cardBack);
	ImageView b5 = new ImageView(cardBack);
	ImageView b6 = new ImageView(cardBack);
	ImageView b7 = new ImageView(cardBack);
	ImageView[] aiHands = {b1, b2, b3, b4, b5, b6, b7};

	//禮物image及ImageView
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

	//被選取的禮物image
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

	//所有的按鈕都在這裡
	Button musicSwitch = new Button(null, musicOn);
	Button setting = new Button(null, new ImageView("pic/gear2.png"));
	Stage settingStage;
	Scene settingScene;
	Button myAction1 = new Button("密約", new ImageView("pic/a1o.png"));
	Button myAction2 = new Button("取捨", new ImageView("pic/a2o.png"));
	Button myAction3 = new Button("贈與", new ImageView("pic/a3o.png"));
	Button myAction4 = new Button("競爭", new ImageView("pic/a4o.png"));
	Button[] myAction = {new Button("密約"), new Button("取捨"), new Button("贈與"), new Button("競爭")};
	Button stackButton = new Button("?", back);
	Button produce = new Button("產生");
	Button startButton = new Button("開始");
	Button check = new Button("確認");
	Button showSDandResult = new Button("開密約");
	Button hands0 = new Button();
	Button hands1 = new Button();
	Button hands2 = new Button();
	Button hands3 = new Button();
	Button hands4 = new Button();
	Button hands5 = new Button();
	Button hands6 = new Button();
	Button hands7 = new Button();
	Button hands8 = new Button();
	Button hands9 = new Button();
	Button hands10 = new Button();
	Button hands11 = new Button();
	Button hands12 = new Button();
	Button hands13 = new Button();
	ArrayList<Button> handsBS = new ArrayList<>();
	Button[] actionSet = {stackButton, myAction1, myAction2, myAction3, myAction4};
	Button[] handsButtonSet = {hands0, hands1, hands2, hands3, hands4, hands5, hands6};
	int[] handsCount = new int[7];
	int[] handsRecord = new int[7];
	int[] cardStack = new int[21];

	HBox myHandCardsGroup = new HBox();

	final String music = getClass().getResource("WhatIsLove.mp3").toString();
	Media media = new Media(music);
	MediaPlayer mediaPlayer = new MediaPlayer(media);

	//主要舞台
	public void play(Stage mainMenu, Stage primaryStage){
		mediaPlayer.play();
		mediaPlayer.setVolume(0.5);
		mediaPlayer.setCycleCount(Timeline.INDEFINITE);
		//對家按鈕設置
		for(int i = 1; i < actionSet.length; i++){
			actionSet[i].setMinHeight(104);
			actionSet[i].setMinWidth(74);
			actionSet[i].setScaleX(1);
			actionSet[i].setScaleY(1);
			//aiAction[i].setMinHeight(104);
			//aiAction[i].setMinWidth(74);
			//aiAction[i].setScaleX(1);
			//aiAction[i].setScaleY(1);
		}

		//對家手牌設置
		for(int i = 0; i < 7; i++){
			aiHands[i].setFitHeight(104);
			aiHands[i].setFitWidth(74);
			aiHands[i].setScaleX(1);
			aiHands[i].setScaleY(1);
		}
		//音量及設定鈕設置
		musicSwitch.setMaxWidth(80);
		musicSwitch.setMaxHeight(80);
		musicSwitch.setLayoutX(1700);
		musicSwitch.setLayoutY(20);
		setting.setMaxWidth(80);
		setting.setMaxHeight(80);
		setting.setLayoutX(1800);
		setting.setLayoutY(20);

		for(int i = 0; i < sentGiftAreaOppo.length; i++){
			sentGiftArea[i] = new Label("", new ImageView(new Image("pic/nullLabel.png")));
			sentGiftAreaOppo[i] = new Label("", new ImageView(new Image("pic/nullLabel.png")));
		}
		showSDandResult.setDisable(true);
		stackButton.setDisable(true);

		handsBS.add(hands7);
		handsBS.add(hands8);
		handsBS.add(hands9);
		handsBS.add(hands10);
		handsBS.add(hands11);
		handsBS.add(hands12);
		handsBS.add(hands13);
		for(int i = 0; i < handsBS.size(); i++) handsBS.get(i).setGraphic(new ImageView("pic/back.png"));

		//startButton.setDisable(true);
		check.setDisable(true);
		actionDiscribe.setEditable(false);
		for(int i = 0; i < handsBS.size(); i++) handsBS.get(i).setDisable(true);

		for(int i = 0; i < 7; i++) handsButtonSet[i].setGraphic(new ImageView("pic/back.png"));

		//aiActionGroup為電腦的動作集合
		HBox aiActionGroup = new HBox(aiAction1, aiAction2, aiAction3, aiAction4);
		aiActionGroup.setSpacing(10);
		//aiHandCardsGroup為ai的手牌集合
		HBox aiHandCardsGroup = new HBox(aiHands[0], aiHands[1], aiHands[2], aiHands[3], aiHands[4], aiHands[5], aiHands[6]);
		aiHandCardsGroup.setSpacing(10);

		//上方玩家列
		HBox aiLine = new HBox(aiActionGroup, aiHandCardsGroup);
		aiLine.setAlignment(Pos.CENTER);
		aiLine.setLayoutY(30);
		aiLine.setSpacing(10);
		//pane.getChildren().add(aiLine);

		//stack為牌庫的集合
		BorderPane stack = new BorderPane();
		stack.setTop(playerOrder);
		stack.setCenter(cardNumber);
		stack.setBottom(stackButton);



	for(int i = 0; i < artistChange.length; i++){
		artistChange[i] = new Button(null, currentIdea[0][i]);
	}

	for(int i = 0; i < artistChange.length; i++){
		final int ii = i;
		artistChange[ii].setOnAction(e ->{
			abcdef[ii]++;
			if(abcdef[ii] > 3) abcdef[ii] = 0;
			artistChange[ii].setGraphic(currentIdea[  abcdef[ii]  ][ii]);
		});
	}




	//artistGroup為藝者與牌庫的集合
	HBox artistGroup = new HBox(artistChange[0], artistChange[1], artistChange[2], artistChange[3], artistChange[4], artistChange[5], artistChange[6], stack);
	artistGroup.setSpacing(15);

		//房間號碼那排集合
		HBox produceRoom = new HBox(produce, room, roomNum);
		//blankAndCheck.setSpacing(0);
		HBox blankAndCheck = new HBox(pleaseEnterActionCode, blank, check);
		blankAndCheck.setSpacing(10);
		HBox anDiscribe = new HBox(yourAN, actionDiscribe);
		//anDiscribe.setSpacing(0);
		HBox hh = new HBox(produceRoom, startButton, blankAndCheck, anDiscribe, showSDandResult);
		hh.setSpacing(40);

		//myActionGroup為玩家的動作集合
		GridPane myActionGroup = new GridPane();
		myActionGroup.add(myAction1, 0, 0);
		myActionGroup.add(myAction2, 1, 0);
		myActionGroup.add(myAction3, 0, 1);
		myActionGroup.add(myAction4, 1, 1);

		for(int i = 0; i < handsBS.size(); i++) myHandCardsGroup.getChildren().add(handsBS.get(i));
		myHandCardsGroup.setSpacing(10);
		HBox myLine = new HBox(myHandCardsGroup, myActionGroup);	//下方玩家列
		myLine.setSpacing(100);

		HBox mySelf = new HBox(sentGiftArea[0], sentGiftArea[1], sentGiftArea[2], sentGiftArea[3], sentGiftArea[4], sentGiftArea[5], sentGiftArea[6]);
		HBox opponent = new HBox(sentGiftAreaOppo[0], sentGiftAreaOppo[1], sentGiftAreaOppo[2], sentGiftAreaOppo[3], sentGiftAreaOppo[4], sentGiftAreaOppo[5], sentGiftAreaOppo[6]);
		mySelf.setSpacing(60);
		mySelf.setPrefWidth(202);
		mySelf.setPrefHeight(103);
		//mySelf.setAlignment(Pos.CENTER);
		opponent.setPrefWidth(202);
		opponent.setPrefHeight(103);
		opponent.setSpacing(60);
		//opponent.setAlignment(Pos.TOP_CENTER);
		//vv統整整個畫面
		VBox v1 = new VBox(tip, hh, myLine);
		v1.setSpacing(10);
		VBox vv = new VBox(aiLine, opponent, artistGroup, mySelf, v1);
		vv.setSpacing(10);

		
		pane.getChildren().add(vv);
		pane.getChildren().add(musicSwitch);
		pane.getChildren().add(setting);

//===============================段落:動畫 起始線=======================
		//手牌的閃爍動畫設置區
		FadeTransition[] ftHands= new FadeTransition[7];
		for(int i = 0; i < ftHands.length; i++){
			ftHands[i] = new FadeTransition(Duration.millis(800));
			ftHands[i].setNode(handsBS.get(i));
			ftHands[i].setFromValue(1.0);
			ftHands[i].setToValue(0.5);
			ftHands[i].setCycleCount(Timeline.INDEFINITE);
			ftHands[i].setAutoReverse(true);
		}

		//按鈕的閃爍動畫設置區
		FadeTransition[] ftActions= {new FadeTransition(Duration.millis(800)), new FadeTransition(Duration.millis(800)), new FadeTransition(Duration.millis(800)), new FadeTransition(Duration.millis(800)), new FadeTransition(Duration.millis(800))};
		ftActions[0].setNode(stackButton);
		ftActions[1].setNode(myAction1);
		ftActions[2].setNode(myAction2);
		ftActions[3].setNode(myAction3);
		ftActions[4].setNode(myAction4);
		for(int i = 0; i < ftActions.length; i++){
			ftActions[i].setFromValue(1.0);
			ftActions[i].setToValue(0.5);
			ftActions[i].setCycleCount(Timeline.INDEFINITE);
			ftActions[i].setAutoReverse(true);
		}
		ftActions[0].setCycleCount(12);

//===============================段落:動畫 終結線=======================
		//音樂開關
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
		//設定
		settingStage = initSettingLayout(mainMenu, primaryStage, settingStage, settingScene, mediaPlayer);
		setting.setOnAction(e->{
			mediaPlayer.setVolume(0.1);
			settingStage.show();
		});
		//產生鈕被按下
		produce.setOnAction(e->{
			player = 1;	//將預設player = 2改為1
			currentTurn = true;
			setGame();
			tip.setText("請按開始~");
		});

		//開始鈕被按下(改變陳設、顯示手牌)
		startButton.setOnAction(e ->{
			produce.setDisable(true);
			startButton.setDisable(true);
			if(player == 1) stackButton.setDisable(false);
			roomNum.setEditable(false);
			check.setDisable(false);
			playerOrder.setText("You're Player "+player);
			tip.setText("請抽牌~");
			if(player == 1) ftActions[0].play();
			ftActions[0].jumpTo(Duration.ZERO);
			if(player == 2){
				stackButton.setDisable(true);
				int temp = 0;
				String rN = roomNum.getText();
				for(int i =0; i < rN.length()/2; i++){
					temp = Integer.parseInt(rN.substring(2*i, 2*i+2) );
					reA[2*i] = temp / 7;
					reA[1+2*i] = temp % 7;
				}
				reA = theExchange(reA);	//一三換位 二頭尾換
				reS = become7Quaternary(reA);

				for(int i = 0; i < reS.length; i++) reS[i] -= 20;
				for(int i = 0; i < cardStack.length; i++) cardStack[i+reS[i]] = i;

			}

			vv.setSpacing(10);
			cardNumber.setText("剩餘張數 : "+existStackN);

			//System.out.print("\nreA : " + Arrays.toString(reA));
			setGraph();

			primaryStage.show();//舞臺
		});

		//確認鈕被按下(補 按ENTER的情況也執行)
		check.setOnAction(e->{
			actionOppo = lastActionOppo(blank.getText(), actionOppo, record4Oppo[0]);

			boolean codeCorrect = checkADcorrectAndRecordOppoOutgifts(blank.getText(), player, actionOppo, record4Oppo[actionOppo]);
			//先檢查 檢查有過將會執行	雖然可以使用迴圈 但因"確認"按鈕可以被重複點擊 所以不需要
			if(codeCorrect){
				if(turn == 4 & player == 1)times += 2;
				else times++;	//動作描述正確 使回合數+1
				check.setDisable(true);
				stackButton.setDisable(false);

				//先看對方有沒有使用贈與或競爭
				if(actionOppo == 3) choice = reactKindGift(record4Oppo[actionOppo]);
				else if(actionOppo == 4) choice = reactLoathGift(record4Oppo[actionOppo]);

				if(turn != 4){
					tip.setText("請抽牌~");
					//ftActions[0]是牌庫的閃亮動畫
					ftActions[0].setCycleCount(6);
					ftActions[0].play();
					ftActions[0].jumpTo(Duration.ZERO);
				}
				if(times >= 9) showSDandResult.setDisable(false);

				if(action == 3) reactKindGiftOppo(record4Oppo[0][0], record4[3]);
				if(action == 4) reactLoathGiftOppo(record4Oppo[0][0], record4[4]);
			}
			else{
				final Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("小提示");
				alert.setHeaderText("輸入不正確！");
				alert.setContentText("可能是您將自己的動作代號填入了");
				alert.showAndWait();
			}

			//無論正確與否都清空輸入框
			blank.setText("");
		});

		//抽牌
		stackButton.setOnAction(e ->{
			//ftActions[0].jumpTo(Duration.ZERO);
			currentTurn = true;
			ftActions[0].stop();
			stackButton.setDisable(true);
			check.setDisable(false);
			existStackN--;
			cardNumber.setText("剩餘張數 : "+existStackN);
			turn++;			//新的一輪
			times++;			//回合數+1
			actionDiscribe.setText("");
			if(player == 1) handsRecord[currentHandsN] = cardStack[2*(6+turn-1)];
			else handsRecord[currentHandsN] = cardStack[2*(6+turn-1) + 1];
			if(currentHandsN < (plusTurnEqualHandsNumber + turn))
				handsBS.get(currentHandsN).setGraphic(g21[handsRecord[currentHandsN] ]);
			lockAll(1);
			currentHandsN++;
			tip.setText("請選取你欲執行動作之相對應張數的牌 並雙擊該動作圖示");

		});

//===============================段落:手牌按鈕 起始線=======================
		for(int i = 0; i < handsBS.size(); i++){
			final int ii = i;
			handsBS.get(ii).setOnAction(e ->{
				handsCount[ii]++;
				if(handsCount[ii] % 2 == 1){
					handsBS.get(ii).setGraphic(g21c[handsRecord[ii] ]);
					pickCard[ii] = true;
					outGifts++;
				}
				else{
					handsBS.get(ii).setGraphic(g21[handsRecord[ii] ]);
					pickCard[ii] = false;
					outGifts--;
				}
			});
		}
//===============================段落:手牌按鈕 終結線=======================

//===============================段落:四個動作按鈕 起始線=======================
		//四個動作按鈕密約取捨贈與競爭
		for(int i = 1; i < actionSet.length; i++){
			final int ii = i;
			actionSet[ii].setOnAction(e ->{
				pauseAnimation(ftActions);
				if(ftActions[ii].getStatus() == Animation.Status.STOPPED) ftActions[ii].play();
				else pauseAnimation(ftActions);
				actionSet[ii].setOnMouseClicked(f -> {	//點兩下觸發判定
					if(f.getClickCount() >= 2) 
						if(outGifts == ii && !doneAction[ii]){
							currentTurn = true;
							action = ii;
							doneAction[ii] = true;
							actionSet[ii].setGraphic(doneActionO[ii]);
							//actionSet[ii].setDisable(true);
							ftActions[ii].pause();
							outGifts -= ii;
							moveHands(ftHands, record4[ii]);
							currentHandsN -= ii;
							plusTurnEqualHandsNumber -= ii;
							killNode();
							//下一行沒有IF也不會錯的樣子
							if(turn/4 == 0) handsBS.get(plusTurnEqualHandsNumber + turn).setGraphic(back2);
							Arrays.fill(handsCount, 0);
							Arrays.fill(pickCard, false);

							////System.out.print("\nchoiceOppo is " + record4Oppo[0][0]);
							if(ii == 4) chooseLoathGiftCombination(record4[4]);
							else actionDiscribe.setText(produceAD(ii, record4[ii], choice) );	//此行不加else亦可 但你可能會覺得奇怪 因上一行呼叫的method中也有呼叫這一行 這是因為下一行會先跑完
							if(turn/4 == 0) tip.setText("請給對家本回合代碼 並等候對家回傳動作代碼。按確認就可以抽牌囉^^");
							else tip.setText("。");
							existStackN--;
							cardNumber.setText("剩餘張數 : "+existStackN);
						}
						else{
							final Alert alert = new Alert(AlertType.INFORMATION);
							alert.setTitle("小提示");
							alert.setHeaderText("選取的張數不對");
							alert.setContentText("請選取"+ii+"張卡");
							alert.showAndWait();
							pauseAnimation(ftActions);
						}
				});
			});
		}
		Stage actionStage = new Stage();
		actionStage.setAlwaysOnTop(true);
		for(int i = 1; i < actionSet.length; i++){
			final int ii = i;
			actionSet[ii].setOnMouseEntered(e ->{
				if(doneAction[ii]) switch(ii){
					case 1 :	 showSelfSecretDeal(actionStage, e.getSceneX()-e.getX(), e.getSceneY()-e.getY()); break;
					case 2 :	showSelfLostDeal(actionStage, e.getSceneX()-e.getX(), e.getSceneY()-e.getY()); break;
					case 3 :	showSelfKindGift(actionStage, e.getSceneX()-e.getX(), e.getSceneY()-e.getY()); break;
					case 4 :	showSelfLoathGift(actionStage, e.getSceneX()-e.getX(), e.getSceneY()-e.getY()); break;
				}
			});
			actionSet[ii].setOnMouseExited(e ->{
				actionStage.hide();
			});
		}
//===============================段落:四個動作按鈕 終結線=======================
		Stage resultStage = new Stage();
		//開密約&顯示結果按鈕
		showSDandResult.setOnAction(e -> {
			if(!showSDalready) showSecretDeal();
			showSDalready = true;
			showSDandResult.setText("顯示結果");
			endRound(resultStage, sentGift, sentGiftOppo);
		});


		showSDandResult.setOnMouseEntered(e ->{
			if(showSDalready) resultStage.show();
		});
		showSDandResult.setOnMouseExited(e ->{
			if(showSDalready) resultStage.close();
		});

		//primaryStage.setFullScreen(true);	//全螢幕 要按esc解除的那種
		Scene scene2 = new Scene(pane, 1920, 1080);//布景
		primaryStage.setTitle("Hanamikoji~");
		primaryStage.setScene(scene2);//舞臺之布景
		primaryStage.show();//舞臺
	}

//============我=是=分隔=線==============主stage結束線===========


//=================房間號碼產生與加密解密 開始線====================
	//隱藏對方按鈕 以及 亂數牌堆
	public void setGame(){

		//給定陣列元素值 = 索引值
		for(int i = 0; i < cardStack.length; i++) cardStack[i] = i;
		//做陣列元素給定索引值 後七行做隨機交換位置 作為這次洗牌後的結果
		for(int i = 0; i < cardStack.length; i++){
			int random = (int)(Math.random()*21);
	
			int tempInt = cardStack[i];
			cardStack[i] = cardStack[random];
			cardStack[random] = tempInt;
		}
		roomNum.setText(produceRoomNum());
	}
	//產生房間號碼
	public String produceRoomNum(){
		String rn = "";
		int[] mA = new int[21];
		for(int i = 0; i < cardStack.length; i++)
			for(int j = 0; j < cardStack.length; j++){
				if(i == cardStack[j]){
					mA[i]  = j - i;
					mA[i] += 20;
					rn = rn + mA[i];
					break;
				}
			}

		rn = rn + "//";
		for(int i = 0; i < cardStack.length; i++) rn = rn + cardStack[i] + " ";

		return encode(mA);
	}
	//加密
	public String encode(int[] array){
		int[] all7Quaternary = new int[42];
		int index = 0;
		String roomNumberStr = "";
		for(int i = 0; i < array.length; i++){	//生成7進位(明文)
			all7Quaternary[index++] = array[i] / 7;
			all7Quaternary[index++] = array[i] % 7;
		}

		all7Quaternary = theExchange(all7Quaternary);	//一三換位 二頭尾換
		fakeA = become7Quaternary(all7Quaternary);
		//for(int e : fakeA) roomNumberStr += ((fakeA[e] > 9)? ""+fakeA[e] : "0"+fakeA[e]);
		for(int k = 0; k < fakeA.length; k++) roomNumberStr += ((fakeA[k] > 9)? ""+fakeA[k] : "0"+fakeA[k]);

		return roomNumberStr;
	}
	//一三換位 二頭尾換(備註 其實也可以將回傳型態設為void)
	public int[] theExchange(int[] array){
		if(array.length % 3 != 0){
			System.out.print("\n加密時陣列長度不為3的倍數!!\n");
			System.exit(-1);
		}
		int temp = 0;
		/*範例[1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12]->[3, 11, 1, 6, 8, 4, 9, 5, 7, 12, 2, 10]
		*           ↑__↑		↑跟2換*/
		for(int i = 0; i < array.length; i +=3){		//7進位後三位一組 一位三位彼此間依序交換
			temp = array[i];
			array[i] = array[i + 2];
			array[i + 2] = temp;
		}
		for(int i = 1; i < 21; i +=3){			//7進位後三位一組 二位自己間頭尾交換
			temp = array[i];
			array[i] = array[41-i];
			array[41-i] = temp;
		}
		return array;
	}
	//將42個元素的整數陣列兩兩一組生成21個元素的整數陣列(備註 其實也可以將回傳型態設為void)
	public int[] become7Quaternary(int[] array42){
		int[] array21 = new int[21];
		//生成7進位(密文)
		for(int i = 0; i < array21.length; i++) array21[i] = (array42[2*i] * 7 + array42[1+2*i]);
		return array21;
	}
//=================房間號碼產生與加密解密 結束線====================
	//產生手牌與改變對家手牌數量
	public void setGraph(){
		if(player == 1){
			for(int k = 0; k < 6; k++){
				handsRecord[k] = cardStack[2*k];
				handsBS.get(k).setGraphic(g21[handsRecord[k] ]);
			}
		}
		else{
			for(int k = 0; k < 6; k++){
				handsRecord[k] = cardStack[2*k+1];
				handsBS.get(k).setGraphic(g21[handsRecord[k] ]);
			}
		}
		handsButtonSet[6].setGraphic(gN);
		handsButtonSet[6].setDisable(true);
		aiHands[6].imageProperty().set(giftNil);	//此時b7會被換成NIL圖案喔!!
	}
	//將手牌的牌移動位置
	//public void moveHands(ArrayList<FadeTransition> ft, int[] record){
	public void moveHands(FadeTransition[] ft, int[] record){
		int temp = 0;	//充當第一個迴圈的index
		int j;
		boolean arriveEnd = false;
		for(int i = 0; i < plusTurnEqualHandsNumber + turn; i++)
			//如果此張牌有被選到
			if(pickCard[i]){
				//ft[i].setDelay(Duration.millis(3000));
				//ft[i].play();
				//這邊上面兩行是讓被選到的牌進行閃亮動畫 下面五行是我找到且有嘗試過的方法(未成功)
				/*        try {
				            Thread.sleep(300);
				        } catch (InterruptedException e) {
				            e.printStackTrace(); 
				        }*/

				//記錄在相對應陣列並用temp作index跳動 紀錄的東西是在亂數牌堆的相對位置
				record[temp++] = linearSearch(cardStack, handsRecord[i]);
			}

		for(int i = 0; i < ft.length; i++) ft[i].pause();
		for(int i = 0; i < plusTurnEqualHandsNumber + turn; i++)
			if(pickCard[i]){
				j = i;
				do{
					j++;
					arriveEnd = true;
					if(j >= plusTurnEqualHandsNumber + turn) break;
					arriveEnd = false;
				}while(pickCard[j]);
				if(arriveEnd) break;
				pickCard[j] = true;
				handsBS.get(i).setGraphic(handsBS.get(j).getGraphic());
				handsRecord[i] = handsRecord[j];	//將HandsRecord改成arraylist
			}
			lockAll(0);
	}//將手牌移動位置 結束括號

	//手牌的節點只會減少不會增加 所以此method用來減少手牌節點
	public void killNode(){
		HBox thief = new HBox();
		int theNextHands = plusTurnEqualHandsNumber + turn + 1 - turn/4;	//下次有幾張
		for(int i = theNextHands; i <= 6; i++)	//因陣列從0開始 所以不會刪到第theNextHands張
			if(existHands[i]){
				thief = new HBox(handsBS.get(i));
				existHands[i] = false;
			}
	}
	//鎖住手牌與開啟手牌 引數為0即鎖住
	public void lockAll(int switc){
		for(int i = 0; i<handsButtonSet.length; i++) handsBS.get(i).setDisable((switc == 0)? true:false);
		//for(int i = 1; i<actionSet.length; i++) actionSet[i].setDisable((switc == 0)? true:false);
	}
	//暫停閃爍動畫 跳回閃爍前畫面
	public void pauseAnimation(FadeTransition[] ft){
		for(int i = 0; i < ft.length; i++){
			ft[i].jumpTo(Duration.ZERO);
			ft[i].pause();
		}
	}
	//產出我方動作描述 回傳至ActionDescribe
	public String produceAD(int action, int[] record, int choice){
		String ad;
		if(actionOppo == 3||actionOppo == 4) ad = ("5"+ choice) + action;
		else ad = "" + action;
		for(int i = 0; i < record.length; i++)
			 ad += (record[i]>=10)? record[i] : "0"+record[i];
		
		return ad;
	}
	//記錄對方上回合所做動作與對贈與競爭的回應
	public int lastActionOppo(String ad, int action, int react[]){
		//下行註解 : 若將動作代碼第一碼轉整數後小於4即我們所要的對方上回合所做動作 若不是則會在第三碼
		action = (int)( ((int)(ad.charAt(0)-48) <= 4)? ad.charAt(0)-48 : ad.charAt(2)-48 );
		react[0] = (int)(ad.charAt(1)-48);	//react[0] 為record4Oppo[0][0]

		return action;
	}
	//檢查對方動作代碼 避免錯誤(避免把自己的動作代碼放進自己確認框)
	public boolean checkADcorrectAndRecordOppoOutgifts(String ad, int player, int action, int[] record4){
		boolean noProblem = true;
		int actionD;
		actionD = Integer.parseInt(	(ad.length() > 9)? ad.substring(2, ad.length()) : ad	);
		//int actionD = Integer.parseInt(ad.substring(2, ad.length()));
		for(int i = action-1, next = 0; i >= 0; i--, next+=2){	//順序可以改一下i = ACTION-1
if(record4.length == 1) System.out.print("\n看哪！ 有人密約茶具ㄎㄎ\n"); //0916新增 以後看到不知在寫啥可刪去 不影響程式
			record4[i] = (int)(actionD % Math.pow(10, next+2) / Math.pow(10, next));
			if(record4[i]%2 == (player-1)){	//檢查是否有自己不可能出的牌
				noProblem = false;
				break;
			}
		}

		return noProblem;
	}
	//我方使用贈與而對方的回應是.....
	public Stage reactKindGiftOppo(int reactChoice, int[] record4){
		Button resultgot = new Button("知道了~");
		HBox my2gifts = new HBox(g21[cardStack[record4[0]]], g21[cardStack[record4[1]]], g21[cardStack[record4[2]]]);
		my2gifts.setAlignment(Pos.TOP_CENTER);
		VBox showResult = new VBox(g21[cardStack[record4[reactChoice]]], resultgot, my2gifts);
		showResult.setAlignment(Pos.TOP_CENTER);
		showResult.setSpacing(30);
		Scene ss= new Scene(showResult, 900, 650);
		Stage resultKG = new Stage();
		resultKG.setScene(ss);
		resultKG.show();

		//其實這裡的record4.length就是3 三張中 被挑走的那張為對方禮物贈送區+1 其餘為自己禮物贈送區+1
		for(int i = 0; i < record4.length; i++){
			if(i == reactChoice) countSentGiftAndChange(sentGiftOppo, cardStack, sentGiftAreaOppo, sentGiftAreaPicOppo, record4[i]);
			else countSentGiftAndChange(sentGift, cardStack, sentGiftArea, sentGiftAreaPic, record4[i]);
		}
		//關閉視窗
		resultgot.setOnAction(e->{
			resultKG.close();
		});

		return resultKG;
	}	
	//我方使用競爭而對方的回應是.....
	public void reactLoathGiftOppo(int reactChoice, int[] record4){
		Button resultGot = new Button("知道ㄌ");
		HBox my2gifts = new HBox(g21[cardStack[record4[0]]], g21[cardStack[record4[1]]], g21[cardStack[record4[2]]], g21[cardStack[record4[3]]]);
		my2gifts.setAlignment(Pos.TOP_CENTER);
		HBox picked;
		if(reactChoice == 0) picked = new HBox(g21[cardStack[record4[0]]], g21[cardStack[record4[1]]]);
		else		picked = new HBox(g21[cardStack[record4[2]]], g21[cardStack[record4[3]]]);
		picked.setAlignment(Pos.TOP_CENTER);
		VBox showResult = new VBox(picked, resultGot, my2gifts);
		showResult.setSpacing(30);
		showResult.setAlignment(Pos.TOP_CENTER);
		Scene ss= new Scene(showResult, 900, 650);
		Stage resultLG = new Stage();
		resultLG.setScene(ss);
		resultLG.show();

		//其實這裡的record4.length就是4 兩組中 被挑走的那組為對方禮物贈送區+1 其餘為自己禮物贈送區+1
		for(int i = 0; i < record4.length; i++){
			if(i == reactChoice*2 || i == reactChoice*2+1) countSentGiftAndChange(sentGiftOppo, cardStack, sentGiftAreaOppo, sentGiftAreaPicOppo, record4[i]);
			else countSentGiftAndChange(sentGift, cardStack, sentGiftArea, sentGiftAreaPic, record4[i]);
		}
		//關閉視窗
		resultGot.setOnAction(e->{
			resultLG.close();
		});
	}

	//對方使用贈與
	public int reactKindGift(int[] record4){
		Button[] choices = new Button[3];
		for(int i = 0; i < choices.length; i++) choices[i] = new Button("", g21[cardStack[record4[i]]]);

		Label explain = new Label("對方使用贈與 請選擇一張你想要的禮物");
		HBox kgList = new HBox(choices[0], choices[1], choices[2]);
		kgList.setSpacing(15);
		kgList.setAlignment(Pos.TOP_CENTER);
		VBox kgVbox = new VBox(explain, kgList);
		kgVbox.setSpacing(30);
		kgVbox.setAlignment(Pos.TOP_CENTER);
		Scene kgScene = new Scene(kgVbox, 900, 650);
		Stage kgStage = new Stage();
		kgStage.setScene(kgScene);
		kgStage.show();

		for(int i = 0; i < choices.length; i++){
			final int ii = i;
			choices[ii].setOnAction(e ->{
				kgStage.close();
				choice = ii;
				//其實這裡的record4.length就是3 三張中 自己挑走的那張為自己禮物贈送區+1 其餘為對方禮物贈送區+1
				for(int j = 0; j < record4.length; j++){
					if(j == choice) countSentGiftAndChange(sentGift, cardStack, sentGiftArea, sentGiftAreaPic, record4[j]);
					else countSentGiftAndChange(sentGiftOppo, cardStack, sentGiftAreaOppo, sentGiftAreaPicOppo, record4[j]);
				}
				if(turn == 4) actionDiscribe.setText("0"+choice);
			});
		}

		return choice;
	}

	//對方使用競爭
	public int reactLoathGift(int[] record4){
		Button[] choices = {new Button("這組"), new Button("這組")};
		Label[] lgLabel = new Label[4];
		for(int i = 0; i < lgLabel.length; i++) lgLabel[i] = new Label("", g21[cardStack[record4[i]]]);

		Label explain = new Label("對方使用競爭 請選擇一組你想要的禮物");
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
		lgStage.show();
		for(int i = 0; i < choices.length; i++){
			final int ii = i;
			choices[ii].setOnAction(e ->{
				lgStage.close();
				choice = ii;

				//其實這裡的record4.length就是4 兩組中 自己挑走的那組為自己禮物贈送區+1 其餘為對方禮物贈送區+1
				for(int j = 0; j < record4.length; j++){
					if(j == choice*2 || j == choice*2+1) countSentGiftAndChange(sentGift, cardStack, sentGiftArea, sentGiftAreaPic, record4[j]);
					else countSentGiftAndChange(sentGiftOppo, cardStack, sentGiftAreaOppo, sentGiftAreaPicOppo, record4[j]);
				}
				if(turn == 4) actionDiscribe.setText("0"+choice);
			});
		}

		return choice;
	}
	//數送出的禮物並且顯示(誰的送禮顯示, 牌堆, 各種類禮物張數, 在牌堆的順序(放最後是因為要做....))
	public void countSentGiftAndChange(int[] sentGift, int[] cardStack, Label[] sentGiftArea, ImageView[][] sentGiftAreaPic, int stackOrder){
		int index = OrderToAskGiftN(cardStack[stackOrder]);
		sentGift[index]++;
		for(int i = 0; i < sentGift.length; i++){
			if(sentGift[i] <= 0) continue;
			sentGiftArea[i].setGraphic(sentGiftAreaPic[   i   ][ sentGift[i]-1 ]);
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

	//線性搜尋 僅受moveHands呼叫(697行附近)
	public int linearSearch(int[] array, int key){
		for(int i = 0; i < array.length; i++) if(array[i] == key) return i;
		return -1;
	}
	//動作四競爭要選擇四張卡的組合 (先判斷有幾種組合方式 再)給出牌的自己選擇組合
	public void chooseLoathGiftCombination(int[] card4){
		//這邊可加一段判斷有可能有幾種組合的程式碼

		int i;	//i用來當for的變數
		ImageView[][] loathGiftIV = new ImageView[3][4];
		for(int h = 0; h<loathGiftIV.length; h++)   for(i = 0; i<loathGiftIV[h].length; i++)
			loathGiftIV[h][i] = new ImageView(gift21[ cardStack[card4[i]] ]);
		Label[] vs = {new Label("v.s."), new Label("v.s."), new Label("v.s.")};
		Button[] option012 = new Button[3];
		for(i = 0; i < option012.length; i++){
			option012[i] = new Button("這樣分");
			option012[i].setMinWidth(100);
			option012[i].setMinHeight(100);
		}
		HBox[] combination123 = new HBox[3];
		for(i = 0; i < combination123.length; i++){
			combination123[i] = new HBox(loathGiftIV[i][0], loathGiftIV[i][i+1], vs[i], loathGiftIV[i][(4-i)/2], loathGiftIV[i][(7-i)/2], option012[i]);
			combination123[i].setSpacing(15);
			combination123[i].setAlignment(Pos.CENTER);
		}
		VBox threeCombination = new VBox(combination123[0], combination123[1], combination123[2]);
		threeCombination.setSpacing(50);
		threeCombination.setAlignment(Pos.TOP_CENTER);
		Scene loathScene = new Scene(new StackPane(threeCombination), 1100, 900);
		Stage loathStage = new Stage();
		loathStage.setScene(loathScene);
		loathStage.show();


		//以下做成另一個method(可以的話)
		for(i = 0; i<option012.length; i++){
			final int ii = i;
			int temp = card4[1];
			option012[ii].setOnAction(e -> {
				card4[1] = card4[ii+1];
				card4[ii+1] = temp;
				actionDiscribe.setText(produceAD(4, record4[4], choice) );
				loathStage.close();
			});
		}
	}

	//開密約
	public void showSecretDeal(){
		countSentGiftAndChange(sentGift, cardStack, sentGiftArea, sentGiftAreaPic, record4[1][0]);
		countSentGiftAndChange(sentGiftOppo, cardStack, sentGiftAreaOppo, sentGiftAreaPicOppo, record4Oppo[1][0]);
		Label sdOppo = new Label("對方的密約", g21[cardStack[record4Oppo[1][0]]]);
		Label sd = new Label("你的密約", g21[cardStack[record4[1][0]]]);
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
	}

	//公布結果
	//public boolean endRound(Stage result, int[] sentGift, int[] sentGiftOppo){
	public void endRound(Stage result, int[] sentGift, int[] sentGiftOppo){
		boolean nextRound = false;
		int[] worth = {5, 4, 3, 3, 2, 2, 2};
		int point = 0;
		int pointOppo = 0;
		int person = 0;
		int personOppo = 0;
		for(int i = 0; i < sentGift.length; i++){
			if(sentGift[i] > sentGiftOppo[i]){
				person++;
				point += worth[i];
			}
			else if(sentGiftOppo[i] > sentGift[i]){
				personOppo++;
				pointOppo += worth[i];
			}
		}

		//Button gotEnd = new Button("喔喔喔喔喔喔喔");
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
		Scene endScene = new Scene(endList, 300, 300);
		result.setScene(endScene);
		//Stage endStage = new Stage();
		//endStage.setScene(endScene);
		//endStage.show();

		//gotEnd.setOnAction(e -> { endStage.close();});
	}

	//展示自己的密約(懸浮視窗)
	public void showSelfSecretDeal(Stage sdStage, double layoutX, double layoutY){
		StackPane sdPane = new StackPane(g21[ cardStack[record4[1][0]] ]);
		Scene sdScene= new Scene(sdPane, 210, 300);
		sdStage.setScene(sdScene);
		sdStage.setX(layoutX-40);
		sdStage.setY(layoutY-340);
		//sdStage.initStyle(StageStyle.TRANSPARENT);
		sdStage.show();
	}
	//展示自己的取捨(懸浮視窗)
	public void showSelfLostDeal(Stage ldStage, double layoutX, double layoutY){
		HBox ldHbox = new HBox(g21[ cardStack[record4[2][0]] ], g21[ cardStack[record4[2][1]] ]);
		ldHbox.setSpacing(15);
		StackPane ldPane = new StackPane(ldHbox);
		Scene ldScene= new Scene(ldPane, 420, 300);
		ldStage.setScene(ldScene);
		ldStage.setX(layoutX-40);
		ldStage.setY(layoutY-340);
		//ldStage.initStyle(StageStyle.TRANSPARENT);
		ldStage.show();
	}

	//展示自己的贈與(懸浮視窗)
	public void showSelfKindGift(Stage kgStage, double layoutX, double layoutY){
		//kgStage = reactKindGiftOppo(record4Oppo[0][0], record4[3]);
		HBox kgHbox = new HBox(g21[ cardStack[record4[3][0]] ], g21[ cardStack[record4[3][1]] ], g21[ cardStack[record4[3][2]] ]);
		kgHbox.setSpacing(15);
		StackPane kgPane = new StackPane(kgHbox);
		Scene kgScene= new Scene(kgPane, 630, 300);
		kgStage.setScene(kgScene);
		kgStage.setX(layoutX-40);
		kgStage.setY(layoutY-340);
		//kgStage.initStyle(StageStyle.TRANSPARENT);
		kgStage.show();
	}

	//展示自己的競爭(懸浮視窗)
	public void showSelfLoathGift(Stage lgStage, double layoutX, double layoutY){
		//lgStage = reactLoathGiftOppo(record4Oppo[0][0], record4[4]);
		HBox lgHbox = new HBox(g21[ cardStack[record4[4][0]] ], g21[ cardStack[record4[4][1]] ], g21[ cardStack[record4[4][2]] ], g21[ cardStack[record4[4][3]] ]);
		lgHbox.setSpacing(15);
		StackPane lgPane = new StackPane(lgHbox);
		Scene lgScene = new Scene(lgPane, 840, 300);
		lgStage.setScene(lgScene);
		lgStage.setX(layoutX-40);
		lgStage.setY(layoutY-340);
		//lgStage.initStyle(StageStyle.TRANSPARENT);
		lgStage.show();
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

}