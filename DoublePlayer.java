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


//image���J�Ϥ�, imageView�O�Ϥ����ܼƳ��(�ڪ��z�Ѱ�)
public class DoublePlayer{

	Label aiAction1 = new Label(null, new ImageView("pic/a1p.png"));
	Label aiAction2 = new Label(null, new ImageView("pic/a2p.png"));
	Label aiAction3 = new Label(null, new ImageView("pic/a3p.png"));
	Label aiAction4 = new Label(null, new ImageView("pic/a4p.png"));
	Label playerOrder = new Label("Player");
	Label cardNumber = new Label("�Ѿl�i�� : ");
	Label tip = new Label("���쪱�a���U���Ͷs���ͩи�������}�l�A���쪱�a�п�J�и�����T�{ ");
	Label room = new Label("�ж��N�X : ");
	Label pleaseEnterActionCode = new Label("�п�J��a�ʧ@�N�X : ");
	Label yourAN = new Label("          Your action number   :   ");
	Label[] aiAction = {aiAction1, aiAction2, aiAction3, aiAction4};
	TextField roomNum = new TextField();
	TextField blank = new TextField();
	TextField actionDiscribe = new TextField("000000");

	int existStackN = 9;
	int action = 0;		//������e�ʧ@���X �K���O1
	int actionOppo = 0;		//������e��a�ʧ@���X �K���O1
	int[] sentGift = new int[7];
	int[] sentGiftOppo = new int[7];
	int currentHandsN = 6;
	int plusTurnEqualHandsNumber = 6;
	int turn = 0;		//�C���@���N�i��4��(turn)8�^�X(times) ��P�|�ϼƭ�+1
	int times = 0;		//��P�|�ϼƭ�+1 & �T�{�]�|�ϼƭ�+1
	int player = 2;		//���⪱�a��1�A��⬰2�C���U�@�ӥu�����⪱�a�|����buttton[����]���ɭԷ|�אּ1
	int choice = 0;
	int choiceOppo = 0;		//�ڬOrecord4Oppo[0][0] ���o�˼g�����n
	int[] fakeA = new int[21];	//�Ĥ@�쪱�a�Ͳ��ж����X�ƥ�
	int[] reA = new int[42];	//�ĤG�쪱�a�c�v�P���(����)
	int[] reS = new int[21];	//�ĤG�쪱�a�c�v�P���(�٭�)
	int[][] record4 = {{7}, {7}, {7, 7}, {7, 7, 7}, {7, 7, 7, 7}};		//actionDecisionOrder
	int[][] record4Oppo = {{7}, {7}, {7, 7}, {7, 7, 7}, {7, 7, 7, 7}};	//record4Oppo[0][0]�s5����ػP�^�� 6�v���^��
	int playerExistAction = 4;
	//int player2ExistAction = 4;
	int outGifts = 0;		//�p��e�X��§���i��
	boolean currentTurn = false;	//�Y���ڤ�^�X��true
	boolean showSDalready = false;
	boolean[] doneAction = new boolean[5];
	boolean[] doneActionOppo = new boolean[5];
	boolean[] pickCard = new boolean[7];
	boolean[] existHands = {true, true, true, true, true, true, true};


	Pane pane = new Pane();
	Scene scene2;

	//���֫��s�Ϥ�
	ImageView musicOn = new  ImageView(new Image("pic/music.png"));
	ImageView musicOff = new  ImageView(new Image("pic/musicN.png"));


	//����ImageView
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


	//�ʧ@ImageView     dA= doneAction, 1234=�K�������ػP�v��, o=orange, p=purple
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

	//�d�Iimage��ImageView
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

	//§��image��ImageView
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

	//�Q�����§��image
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

	//�Ҧ������s���b�o��
	Button musicSwitch = new Button(null, musicOn);
	Button setting = new Button(null, new ImageView("pic/gear2.png"));
	Stage settingStage;
	Scene settingScene;
	Button myAction1 = new Button("�K��", new ImageView("pic/a1o.png"));
	Button myAction2 = new Button("����", new ImageView("pic/a2o.png"));
	Button myAction3 = new Button("�ػP", new ImageView("pic/a3o.png"));
	Button myAction4 = new Button("�v��", new ImageView("pic/a4o.png"));
	Button[] myAction = {new Button("�K��"), new Button("����"), new Button("�ػP"), new Button("�v��")};
	Button stackButton = new Button("?", back);
	Button produce = new Button("����");
	Button startButton = new Button("�}�l");
	Button check = new Button("�T�{");
	Button showSDandResult = new Button("�}�K��");
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

	//�D�n�R�x
	public void play(Stage mainMenu, Stage primaryStage){
		mediaPlayer.play();
		mediaPlayer.setVolume(0.5);
		mediaPlayer.setCycleCount(Timeline.INDEFINITE);
		//��a���s�]�m
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

		//��a��P�]�m
		for(int i = 0; i < 7; i++){
			aiHands[i].setFitHeight(104);
			aiHands[i].setFitWidth(74);
			aiHands[i].setScaleX(1);
			aiHands[i].setScaleY(1);
		}
		//���q�γ]�w�s�]�m
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

		//aiActionGroup���q�����ʧ@���X
		HBox aiActionGroup = new HBox(aiAction1, aiAction2, aiAction3, aiAction4);
		aiActionGroup.setSpacing(10);
		//aiHandCardsGroup��ai����P���X
		HBox aiHandCardsGroup = new HBox(aiHands[0], aiHands[1], aiHands[2], aiHands[3], aiHands[4], aiHands[5], aiHands[6]);
		aiHandCardsGroup.setSpacing(10);

		//�W�誱�a�C
		HBox aiLine = new HBox(aiActionGroup, aiHandCardsGroup);
		aiLine.setAlignment(Pos.CENTER);
		aiLine.setLayoutY(30);
		aiLine.setSpacing(10);
		//pane.getChildren().add(aiLine);

		//stack���P�w�����X
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




	//artistGroup�����̻P�P�w�����X
	HBox artistGroup = new HBox(artistChange[0], artistChange[1], artistChange[2], artistChange[3], artistChange[4], artistChange[5], artistChange[6], stack);
	artistGroup.setSpacing(15);

		//�ж����X���ƶ��X
		HBox produceRoom = new HBox(produce, room, roomNum);
		//blankAndCheck.setSpacing(0);
		HBox blankAndCheck = new HBox(pleaseEnterActionCode, blank, check);
		blankAndCheck.setSpacing(10);
		HBox anDiscribe = new HBox(yourAN, actionDiscribe);
		//anDiscribe.setSpacing(0);
		HBox hh = new HBox(produceRoom, startButton, blankAndCheck, anDiscribe, showSDandResult);
		hh.setSpacing(40);

		//myActionGroup�����a���ʧ@���X
		GridPane myActionGroup = new GridPane();
		myActionGroup.add(myAction1, 0, 0);
		myActionGroup.add(myAction2, 1, 0);
		myActionGroup.add(myAction3, 0, 1);
		myActionGroup.add(myAction4, 1, 1);

		for(int i = 0; i < handsBS.size(); i++) myHandCardsGroup.getChildren().add(handsBS.get(i));
		myHandCardsGroup.setSpacing(10);
		HBox myLine = new HBox(myHandCardsGroup, myActionGroup);	//�U�誱�a�C
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
		//vv�ξ��ӵe��
		VBox v1 = new VBox(tip, hh, myLine);
		v1.setSpacing(10);
		VBox vv = new VBox(aiLine, opponent, artistGroup, mySelf, v1);
		vv.setSpacing(10);

		
		pane.getChildren().add(vv);
		pane.getChildren().add(musicSwitch);
		pane.getChildren().add(setting);

//===============================�q��:�ʵe �_�l�u=======================
		//��P���{�{�ʵe�]�m��
		FadeTransition[] ftHands= new FadeTransition[7];
		for(int i = 0; i < ftHands.length; i++){
			ftHands[i] = new FadeTransition(Duration.millis(800));
			ftHands[i].setNode(handsBS.get(i));
			ftHands[i].setFromValue(1.0);
			ftHands[i].setToValue(0.5);
			ftHands[i].setCycleCount(Timeline.INDEFINITE);
			ftHands[i].setAutoReverse(true);
		}

		//���s���{�{�ʵe�]�m��
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

//===============================�q��:�ʵe �׵��u=======================
		//���ֶ}��
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
		//�]�w
		settingStage = initSettingLayout(mainMenu, primaryStage, settingStage, settingScene, mediaPlayer);
		setting.setOnAction(e->{
			mediaPlayer.setVolume(0.1);
			settingStage.show();
		});
		//���Ͷs�Q���U
		produce.setOnAction(e->{
			player = 1;	//�N�w�]player = 2�אּ1
			currentTurn = true;
			setGame();
			tip.setText("�Ы��}�l~");
		});

		//�}�l�s�Q���U(���ܳ��]�B��ܤ�P)
		startButton.setOnAction(e ->{
			produce.setDisable(true);
			startButton.setDisable(true);
			if(player == 1) stackButton.setDisable(false);
			roomNum.setEditable(false);
			check.setDisable(false);
			playerOrder.setText("You're Player "+player);
			tip.setText("�Щ�P~");
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
				reA = theExchange(reA);	//�@�T���� �G�Y����
				reS = become7Quaternary(reA);

				for(int i = 0; i < reS.length; i++) reS[i] -= 20;
				for(int i = 0; i < cardStack.length; i++) cardStack[i+reS[i]] = i;

			}

			vv.setSpacing(10);
			cardNumber.setText("�Ѿl�i�� : "+existStackN);

			//System.out.print("\nreA : " + Arrays.toString(reA));
			setGraph();

			primaryStage.show();//�R�O
		});

		//�T�{�s�Q���U(�� ��ENTER�����p�]����)
		check.setOnAction(e->{
			actionOppo = lastActionOppo(blank.getText(), actionOppo, record4Oppo[0]);

			boolean codeCorrect = checkADcorrectAndRecordOppoOutgifts(blank.getText(), player, actionOppo, record4Oppo[actionOppo]);
			//���ˬd �ˬd���L�N�|����	���M�i�H�ϥΰj�� ���]"�T�{"���s�i�H�Q�����I�� �ҥH���ݭn
			if(codeCorrect){
				if(turn == 4 & player == 1)times += 2;
				else times++;	//�ʧ@�y�z���T �Ϧ^�X��+1
				check.setDisable(true);
				stackButton.setDisable(false);

				//���ݹ�観�S���ϥ��ػP���v��
				if(actionOppo == 3) choice = reactKindGift(record4Oppo[actionOppo]);
				else if(actionOppo == 4) choice = reactLoathGift(record4Oppo[actionOppo]);

				if(turn != 4){
					tip.setText("�Щ�P~");
					//ftActions[0]�O�P�w���{�G�ʵe
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
				alert.setTitle("�p����");
				alert.setHeaderText("��J�����T�I");
				alert.setContentText("�i��O�z�N�ۤv���ʧ@�N����J�F");
				alert.showAndWait();
			}

			//�L�ץ��T�P�_���M�ſ�J��
			blank.setText("");
		});

		//��P
		stackButton.setOnAction(e ->{
			//ftActions[0].jumpTo(Duration.ZERO);
			currentTurn = true;
			ftActions[0].stop();
			stackButton.setDisable(true);
			check.setDisable(false);
			existStackN--;
			cardNumber.setText("�Ѿl�i�� : "+existStackN);
			turn++;			//�s���@��
			times++;			//�^�X��+1
			actionDiscribe.setText("");
			if(player == 1) handsRecord[currentHandsN] = cardStack[2*(6+turn-1)];
			else handsRecord[currentHandsN] = cardStack[2*(6+turn-1) + 1];
			if(currentHandsN < (plusTurnEqualHandsNumber + turn))
				handsBS.get(currentHandsN).setGraphic(g21[handsRecord[currentHandsN] ]);
			lockAll(1);
			currentHandsN++;
			tip.setText("�п���A������ʧ@���۹����i�ƪ��P �������Ӱʧ@�ϥ�");

		});

//===============================�q��:��P���s �_�l�u=======================
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
//===============================�q��:��P���s �׵��u=======================

//===============================�q��:�|�Ӱʧ@���s �_�l�u=======================
		//�|�Ӱʧ@���s�K�������ػP�v��
		for(int i = 1; i < actionSet.length; i++){
			final int ii = i;
			actionSet[ii].setOnAction(e ->{
				pauseAnimation(ftActions);
				if(ftActions[ii].getStatus() == Animation.Status.STOPPED) ftActions[ii].play();
				else pauseAnimation(ftActions);
				actionSet[ii].setOnMouseClicked(f -> {	//�I��UĲ�o�P�w
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
							//�U�@��S��IF�]���|�����ˤl
							if(turn/4 == 0) handsBS.get(plusTurnEqualHandsNumber + turn).setGraphic(back2);
							Arrays.fill(handsCount, 0);
							Arrays.fill(pickCard, false);

							////System.out.print("\nchoiceOppo is " + record4Oppo[0][0]);
							if(ii == 4) chooseLoathGiftCombination(record4[4]);
							else actionDiscribe.setText(produceAD(ii, record4[ii], choice) );	//���椣�[else��i ���A�i��|ı�o�_�� �]�W�@��I�s��method���]���I�s�o�@�� �o�O�]���U�@��|���]��
							if(turn/4 == 0) tip.setText("�е���a���^�X�N�X �õ��Թ�a�^�ǰʧ@�N�X�C���T�{�N�i�H��P�o^^");
							else tip.setText("�C");
							existStackN--;
							cardNumber.setText("�Ѿl�i�� : "+existStackN);
						}
						else{
							final Alert alert = new Alert(AlertType.INFORMATION);
							alert.setTitle("�p����");
							alert.setHeaderText("������i�Ƥ���");
							alert.setContentText("�п��"+ii+"�i�d");
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
//===============================�q��:�|�Ӱʧ@���s �׵��u=======================
		Stage resultStage = new Stage();
		//�}�K��&��ܵ��G���s
		showSDandResult.setOnAction(e -> {
			if(!showSDalready) showSecretDeal();
			showSDalready = true;
			showSDandResult.setText("��ܵ��G");
			endRound(resultStage, sentGift, sentGiftOppo);
		});


		showSDandResult.setOnMouseEntered(e ->{
			if(showSDalready) resultStage.show();
		});
		showSDandResult.setOnMouseExited(e ->{
			if(showSDalready) resultStage.close();
		});

		//primaryStage.setFullScreen(true);	//���ù� �n��esc�Ѱ�������
		Scene scene2 = new Scene(pane, 1920, 1080);//����
		primaryStage.setTitle("Hanamikoji~");
		primaryStage.setScene(scene2);//�R�O������
		primaryStage.show();//�R�O
	}

//============��=�O=���j=�u==============�Dstage�����u===========


//=================�ж����X���ͻP�[�K�ѱK �}�l�u====================
	//���ù����s �H�� �üƵP��
	public void setGame(){

		//���w�}�C������ = ���ޭ�
		for(int i = 0; i < cardStack.length; i++) cardStack[i] = i;
		//���}�C�������w���ޭ� ��C�氵�H���洫��m �@���o���~�P�᪺���G
		for(int i = 0; i < cardStack.length; i++){
			int random = (int)(Math.random()*21);
	
			int tempInt = cardStack[i];
			cardStack[i] = cardStack[random];
			cardStack[random] = tempInt;
		}
		roomNum.setText(produceRoomNum());
	}
	//���ͩж����X
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
	//�[�K
	public String encode(int[] array){
		int[] all7Quaternary = new int[42];
		int index = 0;
		String roomNumberStr = "";
		for(int i = 0; i < array.length; i++){	//�ͦ�7�i��(����)
			all7Quaternary[index++] = array[i] / 7;
			all7Quaternary[index++] = array[i] % 7;
		}

		all7Quaternary = theExchange(all7Quaternary);	//�@�T���� �G�Y����
		fakeA = become7Quaternary(all7Quaternary);
		//for(int e : fakeA) roomNumberStr += ((fakeA[e] > 9)? ""+fakeA[e] : "0"+fakeA[e]);
		for(int k = 0; k < fakeA.length; k++) roomNumberStr += ((fakeA[k] > 9)? ""+fakeA[k] : "0"+fakeA[k]);

		return roomNumberStr;
	}
	//�@�T���� �G�Y����(�Ƶ� ���]�i�H�N�^�ǫ��A�]��void)
	public int[] theExchange(int[] array){
		if(array.length % 3 != 0){
			System.out.print("\n�[�K�ɰ}�C���פ���3������!!\n");
			System.exit(-1);
		}
		int temp = 0;
		/*�d��[1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12]->[3, 11, 1, 6, 8, 4, 9, 5, 7, 12, 2, 10]
		*           ��__��		����2��*/
		for(int i = 0; i < array.length; i +=3){		//7�i���T��@�� �@��T�쩼�����̧ǥ洫
			temp = array[i];
			array[i] = array[i + 2];
			array[i + 2] = temp;
		}
		for(int i = 1; i < 21; i +=3){			//7�i���T��@�� �G��ۤv���Y���洫
			temp = array[i];
			array[i] = array[41-i];
			array[41-i] = temp;
		}
		return array;
	}
	//�N42�Ӥ�������ư}�C���@�եͦ�21�Ӥ�������ư}�C(�Ƶ� ���]�i�H�N�^�ǫ��A�]��void)
	public int[] become7Quaternary(int[] array42){
		int[] array21 = new int[21];
		//�ͦ�7�i��(�K��)
		for(int i = 0; i < array21.length; i++) array21[i] = (array42[2*i] * 7 + array42[1+2*i]);
		return array21;
	}
//=================�ж����X���ͻP�[�K�ѱK �����u====================
	//���ͤ�P�P���ܹ�a��P�ƶq
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
		aiHands[6].imageProperty().set(giftNil);	//����b7�|�Q����NIL�Ϯ׳�!!
	}
	//�N��P���P���ʦ�m
	//public void moveHands(ArrayList<FadeTransition> ft, int[] record){
	public void moveHands(FadeTransition[] ft, int[] record){
		int temp = 0;	//�R��Ĥ@�Ӱj�骺index
		int j;
		boolean arriveEnd = false;
		for(int i = 0; i < plusTurnEqualHandsNumber + turn; i++)
			//�p�G���i�P���Q���
			if(pickCard[i]){
				//ft[i].setDelay(Duration.millis(3000));
				//ft[i].play();
				//�o��W�����O���Q��쪺�P�i��{�G�ʵe �U������O�ڧ��B�����չL����k(�����\)
				/*        try {
				            Thread.sleep(300);
				        } catch (InterruptedException e) {
				            e.printStackTrace(); 
				        }*/

				//�O���b�۹����}�C�å�temp�@index���� �������F��O�b�üƵP�諸�۹��m
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
				handsRecord[i] = handsRecord[j];	//�NHandsRecord�令arraylist
			}
			lockAll(0);
	}//�N��P���ʦ�m �����A��

	//��P���`�I�u�|��֤��|�W�[ �ҥH��method�ΨӴ�֤�P�`�I
	public void killNode(){
		HBox thief = new HBox();
		int theNextHands = plusTurnEqualHandsNumber + turn + 1 - turn/4;	//�U�����X�i
		for(int i = theNextHands; i <= 6; i++)	//�]�}�C�q0�}�l �ҥH���|�R���theNextHands�i
			if(existHands[i]){
				thief = new HBox(handsBS.get(i));
				existHands[i] = false;
			}
	}
	//����P�P�}�Ҥ�P �޼Ƭ�0�Y���
	public void lockAll(int switc){
		for(int i = 0; i<handsButtonSet.length; i++) handsBS.get(i).setDisable((switc == 0)? true:false);
		//for(int i = 1; i<actionSet.length; i++) actionSet[i].setDisable((switc == 0)? true:false);
	}
	//�Ȱ��{�{�ʵe ���^�{�{�e�e��
	public void pauseAnimation(FadeTransition[] ft){
		for(int i = 0; i < ft.length; i++){
			ft[i].jumpTo(Duration.ZERO);
			ft[i].pause();
		}
	}
	//���X�ڤ�ʧ@�y�z �^�Ǧ�ActionDescribe
	public String produceAD(int action, int[] record, int choice){
		String ad;
		if(actionOppo == 3||actionOppo == 4) ad = ("5"+ choice) + action;
		else ad = "" + action;
		for(int i = 0; i < record.length; i++)
			 ad += (record[i]>=10)? record[i] : "0"+record[i];
		
		return ad;
	}
	//�O�����W�^�X�Ұ��ʧ@�P���ػP�v�����^��
	public int lastActionOppo(String ad, int action, int react[]){
		//�U����� : �Y�N�ʧ@�N�X�Ĥ@�X���ƫ�p��4�Y�ڭ̩ҭn�����W�^�X�Ұ��ʧ@ �Y���O�h�|�b�ĤT�X
		action = (int)( ((int)(ad.charAt(0)-48) <= 4)? ad.charAt(0)-48 : ad.charAt(2)-48 );
		react[0] = (int)(ad.charAt(1)-48);	//react[0] ��record4Oppo[0][0]

		return action;
	}
	//�ˬd���ʧ@�N�X �קK���~(�קK��ۤv���ʧ@�N�X��i�ۤv�T�{��)
	public boolean checkADcorrectAndRecordOppoOutgifts(String ad, int player, int action, int[] record4){
		boolean noProblem = true;
		int actionD;
		actionD = Integer.parseInt(	(ad.length() > 9)? ad.substring(2, ad.length()) : ad	);
		//int actionD = Integer.parseInt(ad.substring(2, ad.length()));
		for(int i = action-1, next = 0; i >= 0; i--, next+=2){	//���ǥi�H��@�Ui = ACTION-1
if(record4.length == 1) System.out.print("\n�ݭ��I ���H�K������}�}\n"); //0916�s�W �H��ݨ줣���b�gԣ�i�R�h ���v�T�{��
			record4[i] = (int)(actionD % Math.pow(10, next+2) / Math.pow(10, next));
			if(record4[i]%2 == (player-1)){	//�ˬd�O�_���ۤv���i��X���P
				noProblem = false;
				break;
			}
		}

		return noProblem;
	}
	//�ڤ�ϥ��ػP�ӹ�誺�^���O.....
	public Stage reactKindGiftOppo(int reactChoice, int[] record4){
		Button resultgot = new Button("���D�F~");
		HBox my2gifts = new HBox(g21[cardStack[record4[0]]], g21[cardStack[record4[1]]], g21[cardStack[record4[2]]]);
		my2gifts.setAlignment(Pos.TOP_CENTER);
		VBox showResult = new VBox(g21[cardStack[record4[reactChoice]]], resultgot, my2gifts);
		showResult.setAlignment(Pos.TOP_CENTER);
		showResult.setSpacing(30);
		Scene ss= new Scene(showResult, 900, 650);
		Stage resultKG = new Stage();
		resultKG.setScene(ss);
		resultKG.show();

		//���o�̪�record4.length�N�O3 �T�i�� �Q�D�������i�����§���ذe��+1 ��l���ۤv§���ذe��+1
		for(int i = 0; i < record4.length; i++){
			if(i == reactChoice) countSentGiftAndChange(sentGiftOppo, cardStack, sentGiftAreaOppo, sentGiftAreaPicOppo, record4[i]);
			else countSentGiftAndChange(sentGift, cardStack, sentGiftArea, sentGiftAreaPic, record4[i]);
		}
		//��������
		resultgot.setOnAction(e->{
			resultKG.close();
		});

		return resultKG;
	}	
	//�ڤ�ϥ��v���ӹ�誺�^���O.....
	public void reactLoathGiftOppo(int reactChoice, int[] record4){
		Button resultGot = new Button("���D�{");
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

		//���o�̪�record4.length�N�O4 ��դ� �Q�D�������լ����§���ذe��+1 ��l���ۤv§���ذe��+1
		for(int i = 0; i < record4.length; i++){
			if(i == reactChoice*2 || i == reactChoice*2+1) countSentGiftAndChange(sentGiftOppo, cardStack, sentGiftAreaOppo, sentGiftAreaPicOppo, record4[i]);
			else countSentGiftAndChange(sentGift, cardStack, sentGiftArea, sentGiftAreaPic, record4[i]);
		}
		//��������
		resultGot.setOnAction(e->{
			resultLG.close();
		});
	}

	//���ϥ��ػP
	public int reactKindGift(int[] record4){
		Button[] choices = new Button[3];
		for(int i = 0; i < choices.length; i++) choices[i] = new Button("", g21[cardStack[record4[i]]]);

		Label explain = new Label("���ϥ��ػP �п�ܤ@�i�A�Q�n��§��");
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
				//���o�̪�record4.length�N�O3 �T�i�� �ۤv�D�������i���ۤv§���ذe��+1 ��l�����§���ذe��+1
				for(int j = 0; j < record4.length; j++){
					if(j == choice) countSentGiftAndChange(sentGift, cardStack, sentGiftArea, sentGiftAreaPic, record4[j]);
					else countSentGiftAndChange(sentGiftOppo, cardStack, sentGiftAreaOppo, sentGiftAreaPicOppo, record4[j]);
				}
				if(turn == 4) actionDiscribe.setText("0"+choice);
			});
		}

		return choice;
	}

	//���ϥ��v��
	public int reactLoathGift(int[] record4){
		Button[] choices = {new Button("�o��"), new Button("�o��")};
		Label[] lgLabel = new Label[4];
		for(int i = 0; i < lgLabel.length; i++) lgLabel[i] = new Label("", g21[cardStack[record4[i]]]);

		Label explain = new Label("���ϥ��v�� �п�ܤ@�էA�Q�n��§��");
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

				//���o�̪�record4.length�N�O4 ��դ� �ۤv�D�������լ��ۤv§���ذe��+1 ��l�����§���ذe��+1
				for(int j = 0; j < record4.length; j++){
					if(j == choice*2 || j == choice*2+1) countSentGiftAndChange(sentGift, cardStack, sentGiftArea, sentGiftAreaPic, record4[j]);
					else countSentGiftAndChange(sentGiftOppo, cardStack, sentGiftAreaOppo, sentGiftAreaPicOppo, record4[j]);
				}
				if(turn == 4) actionDiscribe.setText("0"+choice);
			});
		}

		return choice;
	}
	//�ưe�X��§���åB���(�֪��e§���, �P��, �U����§���i��, �b�P�諸����(��̫�O�]���n��....))
	public void countSentGiftAndChange(int[] sentGift, int[] cardStack, Label[] sentGiftArea, ImageView[][] sentGiftAreaPic, int stackOrder){
		int index = OrderToAskGiftN(cardStack[stackOrder]);
		sentGift[index]++;
		for(int i = 0; i < sentGift.length; i++){
			if(sentGift[i] <= 0) continue;
			sentGiftArea[i].setGraphic(sentGiftAreaPic[   i   ][ sentGift[i]-1 ]);
		}
	}
	//�P�諸���i�P��ڤW�b�}�C���N��0~6 0�O���v¯ 6�O�R�� �Ȩ�countSentGiftAndChange�I�s
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

	//�u�ʷj�M �Ȩ�moveHands�I�s(697�����)
	public int linearSearch(int[] array, int key){
		for(int i = 0; i < array.length; i++) if(array[i] == key) return i;
		return -1;
	}
	//�ʧ@�|�v���n��ܥ|�i�d���զX (���P�_���X�زզX�覡 �A)���X�P���ۤv��ܲզX
	public void chooseLoathGiftCombination(int[] card4){
		//�o��i�[�@�q�P�_���i�঳�X�زզX���{���X

		int i;	//i�Ψӷ�for���ܼ�
		ImageView[][] loathGiftIV = new ImageView[3][4];
		for(int h = 0; h<loathGiftIV.length; h++)   for(i = 0; i<loathGiftIV[h].length; i++)
			loathGiftIV[h][i] = new ImageView(gift21[ cardStack[card4[i]] ]);
		Label[] vs = {new Label("v.s."), new Label("v.s."), new Label("v.s.")};
		Button[] option012 = new Button[3];
		for(i = 0; i < option012.length; i++){
			option012[i] = new Button("�o�ˤ�");
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


		//�H�U�����t�@��method(�i�H����)
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

	//�}�K��
	public void showSecretDeal(){
		countSentGiftAndChange(sentGift, cardStack, sentGiftArea, sentGiftAreaPic, record4[1][0]);
		countSentGiftAndChange(sentGiftOppo, cardStack, sentGiftAreaOppo, sentGiftAreaPicOppo, record4Oppo[1][0]);
		Label sdOppo = new Label("��誺�K��", g21[cardStack[record4Oppo[1][0]]]);
		Label sd = new Label("�A���K��", g21[cardStack[record4[1][0]]]);
		Button gotResult = new Button("�n");
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

	//�������G
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

		//Button gotEnd = new Button("��������");
		Label numbers = new Label("\n�A���y�O�� : \t\t" + point + "\n�A�����̤H�� : \t" + person + "\n��誺�y�O�� : \t" + pointOppo + "\n��誺���̤H�� : \t" + personOppo);
		Label winLose;
		if(point >= 11) winLose = new Label("�AĹ�S");
		else if(pointOppo >= 11) winLose = new Label("�A��{");
		else if(person >= 4) winLose = new Label("�AĹ�S");
		else if(personOppo >= 4) winLose = new Label("�A��{");
		else{
			 winLose = new Label("���� �i�J�U�@��");
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

	//�i�ܦۤv���K��(�a�B����)
	public void showSelfSecretDeal(Stage sdStage, double layoutX, double layoutY){
		StackPane sdPane = new StackPane(g21[ cardStack[record4[1][0]] ]);
		Scene sdScene= new Scene(sdPane, 210, 300);
		sdStage.setScene(sdScene);
		sdStage.setX(layoutX-40);
		sdStage.setY(layoutY-340);
		//sdStage.initStyle(StageStyle.TRANSPARENT);
		sdStage.show();
	}
	//�i�ܦۤv������(�a�B����)
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

	//�i�ܦۤv���ػP(�a�B����)
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

	//�i�ܦۤv���v��(�a�B����)
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




	//�]�w�s���U�h���X�����ƪ��P���s�ƥ�]�m
	public Stage initSettingLayout(Stage mainMenu, Stage masterStage, Stage settingStage, Scene settingScene, MediaPlayer mp){
		Button backGame = new Button("��^�C��");
		Button backMenu = new Button("�^�D���");
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
		//��^�C����
		backGame.setOnAction(e->{
			for(double g = 0.1; g <= 0.5; g+=0.00003) mp.setVolume(g);
			stageCopy.close();
		});
		//��^�D�����
		backMenu.setOnAction(e->{
			mp.stop();
			mainMenu.show();
			masterStage.close();
		});

		return settingStage;
	}

}