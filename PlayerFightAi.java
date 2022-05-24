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

//��H�Ҧ��D�{��
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

	//��a�ʧ@���ܪ�
	ImageView[] purpleActions = new ImageView[8];
	Label[] aiActionSet = new Label[4];

	//��a��P
	//ImageView[] backs = new ImageView[10];
	ImageView[] aiHands = new ImageView[7];	//TODO : �����n

	//����
	ImageView musicOn = new  ImageView(new Image("pic/music.png"));
	ImageView musicOff = new  ImageView(new Image("pic/musicN.png"));
	Button musicSwitch = new Button(null, musicOn);
	final String music = getClass().getResource("WhatIsLove.mp3").toString();
	Media media = new Media(music);
	MediaPlayer mediaPlayer = new MediaPlayer(media);

	//�]�w���s
	ImageView gear = new ImageView("pic/gear2.png");
	Button settingBt = new Button(null, gear);
	Stage settingStage;
	Scene settingScene;

	//����ImageView
	Artist[] artist7Set = new  Artist[7];

	//��P���s�ϼ˻P��a��P�ϼ�(��̬ۦP�A�Ҭ�§���d�d�I)
	Image cardBack = new Image("pic/back.png");
	ImageView back = new ImageView(cardBack);
	ImageView back2 = new ImageView(cardBack);

	//��P���s
	Button stackButton = new Button("?", back);
	int stackAmount = 9;
	Label cardNumber = new Label("�Ѿl�i�� : " + stackAmount);

	//�ϥΪ̴��� �ж����X
	Label tip = new Label("�Щ�P~ ");		//TODO : ��W�r�令tips �]���|��multiple�d�V
	TextField roomNum = new TextField();		//�Ψ����ϥΪ̿�J�и� TODO : ���L���� �ΥH�]�w�s���ﶵ���N��

	int handsAmount = 6;
	int openSdCountDown = 6;
	int action = 0;		//������e�ʧ@���X [�K��]�O1 TODO �T�{�K���u���O1 �M��վ㦨0
	int actionOppo = 0;		//������e��a�ʧ@���X [�K��]�O1 TODO �T�{�K���u���O1 �M��վ㦨0
	int[] sentGift = new int[7];		//�����e�X��§���i�� ���ޭȻP���̪�������{0-�[��, 1-����, 2-�v�l, 3-�d�K, 4-����, 5-���@, 6-�m��}
	int[] sentGiftOppo = new int[7];	//�����q���e�X��§���i��
	int round = 0;		//½Ķ : ��(round) ��(turn) �^�X(times) ����n��½�k�Ы���
	int turn = 0;		//�C���@��(round)�N�i��4��(turn)8�^�X(times) ��P�|�ϼƭ�+1
	int times = 0;
	int choice = 0;
	int choiceOppo = 0;		//������e���
	int playerOrder = 0;
	int[][] actionDecisionOrder = {{0}, {0, 0}, {0, 0, 0}, {0, 0, 0, 0}};		//actionDecisionOrder //usedCard
	//GiftHands[][] recordViewOfGiftsFor4Actions = {new GiftHands[1], new GiftHands[2], new GiftHands[3], new GiftHands[4]};
	ImageView[][] recordViewOfGiftsFor4Actions = {new ImageView[1], new ImageView[2], new ImageView[3], new ImageView[4]};
	int[][] record4Oppo = {{0}, {1, 1}, {2, 2, 2}, {3, 3, 3, 3}};	//�x�s���C���ʧ@�ϥΪ�§���s��
	ImageView[][] recordViewOfGiftsFor4ActionsForAi = {new ImageView[1], new ImageView[2], new ImageView[3], new ImageView[4]};
	GiftHands[][] recordActionsOppo = {new GiftHands[1], new GiftHands[2], new GiftHands[3], new GiftHands[4]};	//TODO : �W�r�Τ@�� �򪱮a���令�P�˳W��
	int outGifts = 0;		//�p��e�X��§���i��
	boolean nextRound = true;
	boolean coin = true;	//�Y���ڤ����h�Хܬ�true
	boolean currentTurn = true;	//�Y���ڤ�^�X�h�Х� ��true
	boolean showSDalready = false;
	boolean[] doneAction = new boolean[4];
	boolean[] doneActionOppo = new boolean[4];
	boolean again = false;	//�Y���� again�������u

	ImageView nextRoundImageView = new ImageView(new Image("pic/nextRound.png"));

	//�ʧ@ImageView     dA= doneAction, 1234=[�K��][����][�ػP][�v��], o=orange, p=purple
	ImageView action1ColorO = new ImageView("pic/a1o.png");
	ImageView action2ColorO = new ImageView("pic/a2o.png");
	ImageView action3ColorO = new ImageView("pic/a3o.png");
	ImageView action4ColorO = new ImageView("pic/a4o.png");
	ImageView done1o = new ImageView(new Image("pic/dA1o.png"));
	ImageView done2o = new ImageView(new Image("pic/dA2o.png"));
	ImageView done3o = new ImageView(new Image("pic/dA3o.png"));
	ImageView done4o = new ImageView(new Image("pic/dA4o.png"));
	ImageView[] actionViewSet = {action1ColorO, action2ColorO, action3ColorO, action4ColorO, done1o, done2o, done3o, done4o};	//�s�Ҧ���⪺�ʧ@�ϥ� TODO : 
	Button myActionSD = new Button("", action1ColorO);
	Button myActionLD = new Button("", action2ColorO);
	Button myActionKG = new Button("", action3ColorO);
	Button myActionLG = new Button("", action4ColorO);
	Button[] myActionSet = {myActionSD, myActionLD, myActionKG, myActionLG};	//�s�|�Ӱʧ@�����s�}�C


	//§��image��ImageView Nil�O�ť�§�� c�Oclick�Q������N copy�����ȮɨS�Ψ�
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

	//�Q�e�X��§��image
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
//=======================================================���欰280��=========
	//�D�n�禡
	public void play(Stage mainMenu, Stage primaryStage){
		this.primaryStage = primaryStage;	//�N�Ƕi���D�n�����}�ѽƻs�쥻����primaryStage(at��39)

		//initLayout();			//�D�C�������ƪ���l��
		drawCardSetting(stackButton);	//�q�P�w��P�����s�]�m
		fourActionUseSetting();		//�|�Ӱʧ@�����s�]�w
		//displayHands();			//�i�ܤ�P
		musicSetting();			//���֫��s
		settingButton(mainMenu);		//�]�w���s
		initLayout();			//�D�C�������ƪ���l��
		fourActionReviewSetting();		//�|�Ӱʧ@���s�^�U�]�w
		showPrimaryStage();		//�D�n�����]�m�H�ήi��

		whiteBoxSetting();			//�]�m�սc

		//if(round == 1) coin = TossTheCoin();	//if�n�����ݭn �]���o��n���u�|�]�@��
		coin = TossTheCoin();		//�M�w�ѽ֥���
		currentTurn = (coin)? true : false;	//�T�w��e�^�X	//TODO �γ\�i�H��X���@�� ��coin �� currentTurn�O�_����L�γ~
		//System.out.print("====="+((coin)? "�ѧA":"�q��")+"����=====");
		//if(!currentTurn) aiCode = aiThink.thinkOut(sentGiftOppo, sentGift, artistStatus, doneActionOppo, doneAction, aHands, sd7s, ld7s);
		//processAiCode(primaryStage, aiCode, record4Oppo);
	}//main Stage End

//=======================================================
//============��=�O=���j=�u==============�Dstage�����u===========
//=======================================================

	//#11�q�P�w��P�����s�]�m
	public void drawCardSetting(Button stackButton){
		//�ƹ�����
		stackButton.setOnAction(e ->{
			currentTurn = true;
			stackButton.setDisable(true);
			stackAmount--;
			cardNumber.setText("�Ѿl�i�� : "+stackAmount);
			System.out.print("\n[�{�ǭ�] ��P! �P��i�Ƴ�"+stackAmount+"�i\n");
			turn++;			//�s���@��(�i��|�]�����Ӷ��[�P�_)
			System.out.print("\n=================[�{�ǭ�] �{�b�O�� " + turn + "��=================");
			lockAll(1);			//�Ϥ�P�i�H���
			hHands.add(allGifts.get(0));	//��P
			allGifts.remove(0);		//�N��X���P����
			System.out.print("\n\n[�{�ǭ�] �{�b�̤W�����d�O"+allGifts.get(0).getCardKinds()+"\n\n");
			knowNextCard();
			//myHandCardsGroup.getChildren().add(hHands.size()-1);
			myHandCardsGroup.getChildren().add(hHands.get(handsAmount));
			handsAmount++;
			addHandsAnimate();
			//addHandsAnimateKeyBoard();
			tip.setText("�п���A������ʧ@���۹����i�ƪ��P �������Ӱʧ@�ϥ�");
			choiceOppo = 0;
		});

		//��L����(TODO : �ܫ᭱����X�u�@ ���ΥX�W��Method)
		//stackButton.setOnKeyPressed(e->{

	}//[����]�q�P�w��P�����s�]�m

	//#12�|�Ӱʧ@�����s�]�w
	public void fourActionUseSetting(){
		//��l���� : �̭����|�q �Ĥ@�qaction = routine() �ĤG�T�q �ʤ�P&����ʧ@ �ĥ|�q �q���ʧ@
		for(int i = 0; i < myActionSet.length; i++){
			final int ii = i;
			myActionSet[ii].setOnMouseClicked(f -> {
				System.out.print("\n[�{�ǭ�] �����즳�@�ʧ@�Q���U�A����i�Ƭ� : "+outGifts+"\n");
				if(outGifts == (ii+1) && !doneAction[ii]){	//�����ƶq��ӥB�ʧ@�٨S��
					action = (ii+1);				//���^�X�ʧ@
					doneAction[ii] = true;			//���L���ʧ@�n���аO
					myActionSet[ii].setGraphic(actionViewSet[ii+4]);	//�ϳo�Ӱʧ@�ܦ��ιL���Ϯ�
					outGifts = 0;				//�e�X��§���i�ƭ���0
					handsAmount -= (ii+1);			//������P�ƴ��
					openSdCountDown--;
					System.out.print("\n[�{�ǭ�] �K���˼�..."+openSdCountDown);
					finalState(openSdCountDown);

					//�O���ò���������P
					removeAndRecord(outHands, recordViewOfGiftsFor4Actions[action-1], actionDecisionOrder[action-1], hHands, myHandCardsGroup);
					outHands = new ArrayList<GiftHands>();
					lockAll(0);					//�Ϥ�P�L�k�A�Q�I��

					//�q�����ػP�v�������A�Y���ݭn�h��������^�X
					aiReactAndExcuteTurn(primaryStage, sentGiftOppo, sentGift, artistStatus, doneActionOppo, doneAction, aHands, sd7s, ld7s, actionDecisionOrder[action-1], outHands);
					//�q�����ʧ@(�D����(�ػP�v�����������H�~�|�Ψ�), �q���w��W��§��, �ڤw��W��§��, ���̪��A, �q���ʧ@���p, �ڪ��ʧ@���p,
					//										�q�����P, �q���K�����p, ���˪��p, �d������, ��w�P��)
				}
				else{				
					final Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("�p����");
					alert.setHeaderText("������i�Ƥ���Τw�ϥ�");
					alert.setContentText("�п��"+(ii+1)+"�i�d");
					alert.showAndWait();
				}
			});
		}
	}//[����]�|�Ӱʧ@�����s�]�w

	//#13�|�Ӱʧ@���s�^�U�]�w
	public void fourActionReviewSetting(){
		Stage actionStage = new Stage();
		actionStage.setAlwaysOnTop(true);
		for(int i = 0; i < myActionSet.length; i++){
			final int ii = i;
			myActionSet[ii].setOnMouseEntered(e ->{	//�a�B����
				if(doneAction[ii]){	//TODO: +bind �[bind
					showUsedAction(actionStage, e.getSceneX()-e.getX(), e.getSceneY()-e.getY(), recordViewOfGiftsFor4Actions[ii], 1);
				}
			});
			myActionSet[ii].setOnMouseExited(e ->{
				actionStage.hide();
			});
			aiActionSet[ii].setOnMouseEntered(e ->{	//�a�B����
				if(doneActionOppo[ii]){	//TODO: +bind �[bind
					showUsedAction(actionStage, e.getSceneX()-e.getX(), e.getSceneY()-e.getY(), recordViewOfGiftsFor4ActionsForAi[ii], -1);
				}
			});
			aiActionSet[ii].setOnMouseExited(e ->{
				actionStage.hide();
			});
		}
	}//[����]�|�Ӱʧ@���s�^�U�]�w

	//#15���֫��s
	public void musicSetting(){
		//����
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
	}//[����]���֫��s

	//#16�]�w���s
	public void settingButton(Stage mainMenu){
		//�]�w
		settingStage = initSettingLayout(mainMenu, primaryStage, settingStage, settingScene, mediaPlayer);
		settingBt.setOnAction(e->{
			mediaPlayer.setVolume(0.1);
			settingStage.show();
		});
	}//[����]�]�w���s

	//�C���}�l�έ��ҷs���ɩI�sreset���s�]�w
	public void reset(){
		//�����ʼаO�Ϥ�
		purpleActions = new ImageView[8];
		for(int i = 0; i < purpleActions.length; i++){
			purpleActions[i] = new ImageView(new Image("pic/ap"+i+".png"));
			purpleActions[i].fitWidthProperty().bind(pane.widthProperty().divide(22));	//���e���O��ӿù��e�� 1/22
			purpleActions[i].fitHeightProperty().bind(purpleActions[i].fitWidthProperty());	//������e�@�ˤj
		}
		//����ʼаO�Ϥ�




	}
	/*#17�D�C�������ƪ���l��
	*�N��ӵ�����������4*3��12��
	*[�Ʀr]��ܦ��q�|��b���q
	*���ХܧY��ܦ��q���{���X���ݨ���e�{*/
	public void initLayout(){
		pane = new Pane();	//TODO : �o�䰣�F���mpane�H�~ ��L�]�n��l�� �٨S�g(����@�~)
		/*
		*(���q�վ��@�}�l)(�Τ���)
		*
		*
		*
		*
		*/
		/*//�ۮa���s�]�m
		for(int i = 0; i < myActionSet.length; i++){
			myActionSet[i].setMinHeight(42);
			myActionSet[i].setMinWidth(42);
			myActionSet[i].prefWidthProperty().bind(pane.widthProperty().divide(17));
			myActionSet[i].prefHeightProperty().bind(myActionSet[i].prefWidthProperty());
			myActionSet[i].setScaleX(1);
			myActionSet[i].setScaleY(1);
		}

		//���a���ʧ@���X
		for(int i = 0; i < actionViewSet.length; i++){
			actionViewSet[i].fitWidthProperty().bind(pane.widthProperty().divide(16));
			actionViewSet[i].fitHeightProperty().bind(actionViewSet[i].fitWidthProperty());
			actionViewSet[i].setScaleX(0.75);
			actionViewSet[i].setScaleY(0.75);
		}*/


		//�q�����ʧ@���X(�M��֨�aiLine)[1]
		for(int i = 0; i < purpleActions.length; i++){
			purpleActions[i] = new ImageView(new Image("pic/ap"+i+".png"));
			purpleActions[i].fitWidthProperty().bind(pane.widthProperty().divide(22));	//���e���O��ӿù��e�� 1/22
			purpleActions[i].fitHeightProperty().bind(purpleActions[i].fitWidthProperty());	//������e�@�ˤj
		}
		for(int i = 0; i < aiActionSet.length; i++){
			aiActionSet[i] = new Label();
			aiActionSet[i].setGraphic(purpleActions[i]);
			aiActionSet[i].prefWidthProperty().bind(pane.widthProperty().divide(22));	//���e���O��ӿù��e�� 1/22
			aiActionSet[i].prefHeightProperty().bind(aiActionSet[i].prefWidthProperty());	//������e�@�ˤj	//������e�@�ˤj
			aiActionGroup.getChildren().add(aiActionSet[i]);
		}
		aiActionGroup.spacingProperty().bind(pane.widthProperty().divide(192));

		//��a��P�]�m(�M��֨�aiLine)[2~3]
		for(int i = 0; i < 10; i++){
			backs.add(new ImageView(cardBack));
			backs.get(i).fitWidthProperty().bind(pane.widthProperty().divide(22));		//���e���O��ӿù��e�� 1/22
			backs.get(i).fitHeightProperty().bind(backs.get(i).fitWidthProperty().multiply(1.38));	//�����O�e��1.38��
		}
		aiHandCardsGroup = new HBox();
		for(int i = 0; i < 6; i++){	//aHands.size(); i++){
			aBack.add(backs.get(i));
			aiHandCardsGroup.getChildren().add(aBack.get(i));
		}

		aiHandCardsGroup.setSpacing(10);
		//�W��ai��T�C(�X�֥H�W��ӵ{���q)[1~3]
		aiLine = new HBox(aiActionGroup, aiHandCardsGroup);
		aiLine.setAlignment(Pos.CENTER);
		System.out.print("\n[�{�ǭ�] ai��P�H�ΰʧ@ �ǳƧ���");

		//���̳]�m[5~7���h]
		for(int i = 0; i < artist7Set.length; i++){
			artist7Set[i] = new Artist(i, "first");
			artist7Set[i].getArtistView().fitWidthProperty().bind(pane.widthProperty().divide(8.5));
			artist7Set[i].getArtistView().fitHeightProperty().bind(artist7Set[i].getArtistView().fitWidthProperty().multiply(1.53));
		}
		artistGroup = new HBox(artist7Set[0], artist7Set[1], artist7Set[2], artist7Set[3], artist7Set[4], artist7Set[5], artist7Set[6]);
		artistGroup.spacingProperty().bind(pane.widthProperty().divide(135));
		//���e�X��§���C�ժO
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
		//��誺§���C�]�m[5~7�W�h]
		opponent = new HBox(sentGiftAreaOppo[0], sentGiftAreaOppo[1], sentGiftAreaOppo[2], sentGiftAreaOppo[3], sentGiftAreaOppo[4], sentGiftAreaOppo[5], sentGiftAreaOppo[6]);
		opponent.spacingProperty().bind(artistGroup.spacingProperty().multiply(4));
		opponent.setAlignment(Pos.CENTER);
		//�ڤ誺§���C�]�m[5~7�U�h]
		mySelf = new HBox(sentGiftArea[0], sentGiftArea[1], sentGiftArea[2], sentGiftArea[3], sentGiftArea[4], sentGiftArea[5], sentGiftArea[6]);
		mySelf.spacingProperty().bind(artistGroup.spacingProperty().multiply(4));
		mySelf.setAlignment(Pos.CENTER);
		//��X���̥H������e�X§���C[5~7]
		arena = new VBox(opponent, artistGroup, mySelf);
		arena.spacingProperty().bind(pane.heightProperty().divide(128));
		System.out.print("\n[�{�ǭ�] ���̺[����e�X§���C �ǳƧ���");
		//��X�������a�Ҩ���T[1~3, 5~7]
		infoUI = new VBox(aiLine, arena);
		infoUI.spacingProperty().bind(pane.heightProperty().divide(32));
		infoUI.layoutXProperty().bind(pane.widthProperty().divide(192));
		infoUI.layoutYProperty().bind(pane.heightProperty().divide(128));
		pane.getChildren().add(infoUI);

		//���ּ���
		mediaPlayer.pause();	//TODO ���������i�אּplay �o�s�gpause�O�]�����յ{�����ɭԧڤ��Q�@��ť�}�Y
		mediaPlayer.setVolume(0.5);
		mediaPlayer.setCycleCount(Timeline.INDEFINITE);
		//���q�γ]�w�s�]�m[4]
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
		System.out.print("\n[�{�ǭ�] �]�w���s�H�έ��q���s �ǳƧ���");
		System.out.print("\n[�{�ǭ�] ��P : ");		//���F�bCMD�s���ɼg��
		//drawCardStack���P�w�����X[8]
		drawCardStack = new VBox(cardNumber, stackButton);
		drawCardStack.spacingProperty().bind(pane.heightProperty().divide(128));
		back.fitWidthProperty().bind(pane.widthProperty().divide(10));
		back.fitHeightProperty().bind(back.fitWidthProperty().multiply(1.38));
		//ctrlUI�����q�]�w�P��P�P�w���T��UI���X
		ctrlUI = new VBox(settingUI, drawCardStack);
		ctrlUI.spacingProperty().bind(pane.heightProperty().divide(10));	//TODO:�ƪ���n�� ���O���H32����[�h��property �ݤ��������ѥi�R�� �w�g���n�F
		ctrlUI.layoutXProperty().bind(pane.widthProperty().divide(1.13));
		ctrlUI.layoutYProperty().bind(pane.heightProperty().divide(54));
		pane.getChildren().add(ctrlUI);

		//���a����
		tip.layoutXProperty().bind(pane.widthProperty().divide(2.4));
		tip.layoutYProperty().bind(pane.heightProperty().divide(1.5));
		pane.getChildren().add(tip);

		//�ۮa���s�]�m
		for(int i = 0; i < myActionSet.length; i++){
			myActionSet[i].setMinHeight(20);
			myActionSet[i].setMinWidth(20);
			myActionSet[i].prefWidthProperty().bind(pane.widthProperty().divide(18));
			myActionSet[i].prefHeightProperty().bind(myActionSet[i].prefWidthProperty());
			myActionSet[i].setScaleX(1);
			myActionSet[i].setScaleY(1);
		}

		//���a���ʧ@���X
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
	}//[����]�D�C�������ƪ���l��

	//#18�D�n�����]�m�H�ήi��
	public void showPrimaryStage(){
		//primaryScene = new Scene(pane, 1280, 800);
		primaryScene = new Scene(pane, 1600, 1080);	//1007���ե� �ݨ�i�H��^1280 800
		//primaryScene = new Scene(pane, 1920, 1080);	//����
		primaryStage.setTitle("Hanamikoji~");		//���D
		primaryStage.setScene(primaryScene);		//�R�O������
		randomStack(cardStack);			//�üƵP��
		displayHands();				//�i�ܤ�P
		//primaryStage.setFullScreen(true);		//���ù� �n��esc�Ѱ�������
		lockAll(0);					//�Ϥ�P�����
		primaryStage.show();			//�R�O
	}

	//�P��]�m�C�üƵP��A�H�νվ�U§���P���۰��Y��C�Ĥ@�� �}�C�������w���ޭȡF��C�� �H���洫��m �@���o���~�P�᪺���G
			//TODO �ݬݦ۰��Y��ण�ள��initLayout
	public void randomStack(int[] stack){
		for(int i = 0; i < stack.length; i++) stack[i] = i;
		for(int i = 0; i < stack.length; i++){
			int random = (int)(Math.random()*21);
			int tempInt = stack[i];
			stack[i] = stack[random];
			stack[random] = tempInt;
		}
		//1015�s��ק� �ݨ�i�H�������}���ѨçR���U�C2��
		/*	0-4 ���v¯
		*	5-8 ����
		*	9-11 �o�ȳ�
		*	12-14 �T���u
		*	15-16 �馡�N��
		*	17-18 �j�Ѩ��b
		*	19-20 �R��*/
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

	//��n�����P�P���ܹ�a��P�ƶq
	public void displayHands(){
		for(int k = 0; k < 6; k++){
			hHands.add(allGifts.get(2*k+playerOrder));   //�N�ӹ�ڽs���۹�쪺§�����
			aHands.add(allGifts.get(2*k+1-playerOrder));
			myHandCardsGroup.getChildren().add(hHands.get(k));
		}
		myHandCardsGroup.spacingProperty().bind(pane.widthProperty().divide(192));
		myLine = new HBox(myHandCardsGroup, myActionGroup);
		myLine.spacingProperty().bind(pane.widthProperty().divide(19.2));
		/*myLine.setLayoutX(15);
		myLine.setLayoutY(770);*/ //0929���� �ݨ�i�H���}���� �M��R����1100����
		myLine.spacingProperty().bind(pane.widthProperty().divide(19.2));
		myLine.layoutXProperty().bind(pane.widthProperty().divide(128));
		myLine.layoutYProperty().bind(pane.heightProperty().divide(1.4));
		pane.getChildren().add(myLine);
    		for(int twelveTimes = 0; twelveTimes < 12; twelveTimes++) allGifts.remove(0);


		//0986aiHands[6].imageProperty().set(giftNil);	//����b7�|�Q����NIL�Ϯ׳�!!
	}

	//�Ϥ�P�֦�����ʵe(�ƹ�) TODO : (�γ\���ݭn)�ϥ����P�諸�P�������ݩ�
	public void addHandsAnimate(){
		for(int i = 0; i < hHands.size(); i++){			//�C�i��P����u�U���X��{���X��
			final int ii = i;
			hHands.get(ii).setOnAction(e ->{		//���s�ƥ�
				System.out.print("\n[�{�ǭ�] "+((hHands.get(ii).getPicked())? "���惡�i§��":"��ܦ��i§��")+"�A��§���C���� : "+hHands.get(ii).getOutOrder());
				if(!hHands.get(ii).getPicked()){	//�Y���P�B�󥼳Q��������A
					outGifts++;
					hHands.get(ii).transType(outGifts);		//���A���ഫ�I
					System.out.print("oytgGIft : "+outGifts);
					outHands.add(hHands.get(ii));		//�s�٦�[�J�X�P�C��(�o�O�䤤�@�ӳQ�X�X�h���P)
					if(outGifts == 4){
						changeImageView(outHands);	//�p�G4�i(�|�ϥ��v��)�N�ܹϤ�(�ܦ����զ�)
						lockAll(0);
					}
				}
				else{				//�Y���P�w��
					outGifts--;
					//�U���o�檺���� �j��(����o�i�P���Ǹ�(1or2or3or4); �b����X�P�C�����פj(<4)�����p�U; ������W) �X�P�C�����o�i�P���X�P�s�����1
					for(int j = hHands.get(ii).getOutOrder(); j < outHands.size(); j++) outHands.get(j).setOutOrder( outHands.get(j).getOutOrder()-1 );
					outHands.remove(hHands.get(ii).getOutOrder()-1);	//���}�X�P�C��
					hHands.get(ii).transType(outGifts);			//���A���ഫ�I
					hHands.get(ii).setOutOrder(0);			//���惡§���F�A�N���Q�ﶶ���k�s
					reloadImageView(outHands);				//��s�Ϥ�
					lockAll(1);
				}

			});
		}
	}
	//�p�G��P�w��4�i(�|�ϥ��v��) �e�T�i�N�ܹϤ�(�ܦ����զ�)
	public void changeImageView(ArrayList<GiftHands> al){
		for(int i = 0; i <= 2; i++) al.get(i).setCurrentView( al.get(i).getTypeStyle()[al.get(i).getOutOrder()+4]);
	}
	//�X�P�C�����Ǹ��Y�ܴN�n��s�Ϥ�	//TODO �o�ӵ{���w��I�s����GiftHands�̭�����kgetTypeStyle()�O�_�Ҽ{�����̭����t�@��getOneOfTheTypeStyle
	public void reloadImageView(ArrayList<GiftHands> al){
		for(int i = 0; i < al.size(); i++) al.get(i).setCurrentView( al.get(i).getTypeStyle()[al.get(i).getOutOrder()]);
	}

	//�����Q�������P�H�β�����P�`�I
	public void removeAndRecord(ArrayList<GiftHands> outHand, ImageView[] record4, int[] recordOrder, ArrayList<GiftHands> hHand, HBox group){
		//����
		for(int k = 0; k < outHand.size(); k++){
			record4[k] = outHand.get(k).getOneOfTheTypeStyle(0);
			recordOrder[k] = outHand.get(k).getCardKinds();
			System.out.print("\n[�{�ǭ�] ���^�X�ҨϥεP���P�ﶶ�� : "+outHand.get(k).getStackOrder());
		}
		//����
		for(int k = hHands.size() - 1; k >= 0; k--){
			if(hHand.get(k).getPicked()){
				group.getChildren().remove(k);
				hHand.remove(k);
			}
		}
	}

	//(for�q��)������P�H�ά����Q�������P
	public void removeAndRecordAiHands(ArrayList<GiftHands> outHand, ImageView[] aiRecord4, int[] aiRecordOrder, ArrayList<GiftHands> aiHand){
		System.out.print("\n[�{�ǭ�] �i�����e��P���׬O"+aiHand.size());
		//����
		for(int k = 0; k < outHand.size(); k++){
			aiRecord4[k] = outHand.get(k).getOneOfTheTypeStyle(0);
			aiRecordOrder[k] = outHand.get(k).getCardKinds();
			System.out.print("\n[�{�ǭ�] ���^�X�ҨϥεP���P�ﶶ�� : "+outHand.get(k).getStackOrder()+", �d�ج� <"+outHand.get(k).getCardKinds()+">");
			//aBack.remove(aHands.size());
			//aiHandCardsGroup.getChildren().remove(aHands.size()-1);
		}
		//����
		for(int k = aiHand.size() - 1; k >= 0; k--){	
			if(aiHand.get(k).getPicked()){
				aiHand.remove(k);
			}
		}
		System.out.print("\n[�{�ǭ�] �i�������P���׬O"+ aiHand.size());

		//return aiHand;	
	}

	//����P�P�}�Ҥ�P �޼Ƭ�0�Y���Ҧ����������P�A�޼Ƭ�1�h�}�ҩҦ����������P
	public void lockAll(int kW){
		for(int i = 0; i<hHands.size(); i++){
			if(!hHands.get(i).getPicked()) hHands.get(i).setDisable((kW == 0)? true:false);
		}
	}

	//�q�����ػP�v�������A�Y���ݭn�h��������^�X
	//public void aiReactAndExcuteTurn(Stage masterStage, int action, int choiceOppo, int[] sent_Gift, int[] sent_Gift_Player, String[] artistBelongTo, boolean[] used_Action, boolean[] used_Action_Player, ArrayList<GiftHands> handsCard, int[] sd7statistics, int[] ld7statistics){
	public void aiReactAndExcuteTurn(Stage ownerStage, int[] sent_Gift, int[] sent_Gift_Player, String[] artistBelongTo, boolean[] used_Action, boolean[] used_Action_Player, ArrayList<GiftHands> aiHandsCard, int[] sd7statistics, int[] ld7statistics, int[] someGift, ArrayList<GiftHands> alms){
		if(action == 3){
			System.out.print("\n�{���X�o�̻ݭn�[�W�q����Үɶ�");
			System.out.print("\n\t\t\t�� �� �� �� �� �� ��");
			System.out.print("\n�ǻ��esentGift : \t"+Arrays.toString(sent_Gift));
			System.out.print("\n�ǻ��esentGiftPlayer : \t"+Arrays.toString(sent_Gift_Player));
			choiceOppo = aiThink.reactKindGift(sent_Gift, sent_Gift_Player, artistBelongTo, used_Action, used_Action_Player, aiHandsCard, sd7statistics, ld7statistics, someGift);
			reactKindGiftOppo(ownerStage, choiceOppo, recordViewOfGiftsFor4Actions[action-1]);
		}
		else if(action == 4){
			//+�q����Үɶ�
			choiceOppo = aiThink.reactLoathGift(sent_Gift, sent_Gift_Player, artistBelongTo, used_Action, used_Action_Player, aiHandsCard, sd7statistics, ld7statistics, combination1, combination2);
			reactLoathGiftOppo(ownerStage, choiceOppo, recordViewOfGiftsFor4Actions[action-1]);
		}
		//+�q����Үɶ�
		stackAmount--;		//�q����P
		//aBack.add(new Label("", backs[aHands.size()]));
		//aiHandCardsGroup.getChildren().add(aBack.get(aHands.size()-1));
		aHands.add(allGifts.get(0));	//�q����P
		allGifts.remove(0);		//�N��X���P����
		currentTurn = false;
		cardNumber.setText("�Ѿl�i�� : "+stackAmount);
		System.out.print("\n[�{�ǭ�] ����q��");
		givingByAi = aiThink.thinkOut(sent_Gift, sentGift, artistStatus, doneActionOppo, doneAction, aHands, sd7s, ld7s);
		processAiGiving(ownerStage,givingByAi);
	}


	//��Ū�q�����ʧ@
	public void processAiGiving(Stage ownerStage, ArrayList<GiftHands> giving){
		System.out.print("\n[�{�ǭ�] ��Ū�ɤ�P���׬O"+ giving.size());
		String[] actionName = {"[�K��]", "[����]", "[�ػP]", "[�v��]"};
		String[] actionReact = {" ����A�F\t�Щ�P", "�п�ܤ@�i�A�Q�n���P"};
		actionOppo = giving.size() - 1;		//-1��] : �K���|��1�i�d�Asize��1�A���K���ꬰ�ʧ@0�C��L�ʧ@�̦������C
		doneActionOppo[actionOppo] = true;		//�Хܹq�����L�o�Ӱʧ@�F
		tip.setText("\n���ϥ� "+actionName[actionOppo]+"�A"+actionReact[actionOppo/2]);
		System.out.print("\n[�{�ǭ�] ���ϥ� "+actionName[actionOppo]+"�A"+actionReact[actionOppo/2]);
		removeAndRecordAiHands(giving, recordViewOfGiftsFor4ActionsForAi[actionOppo], record4Oppo[actionOppo], aHands);
		aiActionSet[actionOppo].setGraphic(purpleActions[actionOppo+4]);

		//�ػP�H���v���������a�^�� ��L���p�N�����Ѷ}�P�w�����I������ �����a�i�H��P
		switch(actionOppo){
		case 0 :
		case 1 :
			stackButton.setDisable(false);
			break;
		case 2 :
			threePickOne(ownerStage, giving, recordViewOfGiftsFor4ActionsForAi[actionOppo]);	//�ػP
			break;
		case 3 :
			twoGroupPickOne(ownerStage, giving, recordViewOfGiftsFor4ActionsForAi[actionOppo]);	//�v��
			break;
		default :
			System.out.print("\nerror!!! The action isn't normal");
		}
	}



	//�ڤ�ϥ�[�ػP]�ӹ�誺�^���O.....
	public Stage reactKindGiftOppo(Stage masterStage, int reactChoice, ImageView[] record4){
		Button resultgot = new Button("���D�F~");
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
		resultKG.initOwner(masterStage);	//�������H
		resultKG.setAlwaysOnTop(true);	//�����m�� : �Y���^�X����ϥ���/�v���^�X���\�b��誺��/�v���e
		resultKG.show();
//		for(int i = 0; i < )

		//���o�̪�record4.length�N�O3 �T�i�� �Q�D�������i�����§���ذe��+1 ��l���ۤv§���ذe��+1
		for(int i = 0; i < record4.length; i++){
			if(i == reactChoice) countSentGiftAndChange(sentGiftOppo, sentGiftAreaOppo, sentGiftAreaPicOppo, actionDecisionOrder[2][i]);
			else countSentGiftAndChange(sentGift, sentGiftArea, sentGiftAreaPic, actionDecisionOrder[2][i]);
		}
		//��������
		resultgot.setOnAction(e->{
			resultKG.close();
		});

		return resultKG;
	}	
	//�ڤ�ϥ�[�v��]�ӹ�誺�^���O.....	//loathGift
	public void reactLoathGiftOppo(Stage masterStage, int reactChoice, ImageView[] record4){
		Button resultGot = new Button("���D�{");
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
		resultLG.initOwner(masterStage);	//�������H
		resultLG.setAlwaysOnTop(true);	//�����m�� : �Y���^�X����ϥ���/�v���^�X���\�b��誺��/�v���e
		resultLG.show();
		//thread.sleep();

		//���o�̪�record4.length�N�O4 ��դ� �Q�D�������լ����§���ذe��+1 ��l���ۤv§���ذe��+1
		for(int i = 0; i < record4.length; i++){
			if(i == reactChoice*2 || i == reactChoice*2+1) countSentGiftAndChange(sentGiftOppo, sentGiftAreaOppo, sentGiftAreaPicOppo, actionDecisionOrder[3][i]);
			else countSentGiftAndChange(sentGift, sentGiftArea, sentGiftAreaPic, actionDecisionOrder[3][i]);
		}
		//��������
		resultGot.setOnAction(e->{
			resultLG.close();
		});
	}

	//���ϥ�[�ػP]
	public int threePickOne(Stage ownerStage, ArrayList<GiftHands> giving, ImageView[] record4){
		Button[] choices = new Button[3];
		for(int i = 0; i < choices.length; i++) choices[i] = new Button("", record4[i]);	//�إߤT�ӵL�r������Ϥ������s��i�ϰ�}�Cchoices

		Label explain = new Label("���ϥ�[�ػP] �п�ܤ@�i�A�Q�n��§��");
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
				tip.setText("�Щ�P");
				//���o�̪�record4.length�N�O3 �T�i�� �ۤv�D�������i���ۤv§���ذe��+1 ��l�����§���ذe��+1
				for(int j = 0; j < record4.length; j++){
					if(j == choice) countSentGiftAndChange(sentGift, sentGiftArea, sentGiftAreaPic, record4Oppo[actionOppo][j]);
					else countSentGiftAndChange(sentGiftOppo, sentGiftAreaOppo, sentGiftAreaPicOppo, record4Oppo[actionOppo][j]);
				}
				openSdCountDown--;
				System.out.print("\n[�{�ǭ�] �K���˼�..."+openSdCountDown);
				finalState(openSdCountDown);
			});
		}

		return choice;
	}

	//���ϥ�[�v��]
	public int twoGroupPickOne(Stage ownerStage, ArrayList<GiftHands> giving, ImageView[] record4){
		Button[] choices = {new Button("�o��"), new Button("�o��")};
		Label[] lgLabel = new Label[4];
		for(int i = 0; i < lgLabel.length; i++) lgLabel[i] = new Label("", record4[i]);

		Label explain = new Label("���ϥ�[�v��] �п�ܤ@�էA�Q�n��§��");
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
				tip.setText("�Щ�P");
				//���o�̪�record4.length�N�O4 ��դ� �ۤv�D�������լ��ۤv§���ذe��+1 ��l�����§���ذe��+1
				for(int j = 0; j < record4.length; j++){
					if(j == choice*2 || j == choice*2+1) countSentGiftAndChange(sentGift, sentGiftArea, sentGiftAreaPic, record4Oppo[actionOppo][j]);
					else countSentGiftAndChange(sentGiftOppo, sentGiftAreaOppo, sentGiftAreaPicOppo, record4Oppo[actionOppo][j]);
				}
				openSdCountDown--;
				System.out.print("\n[�{�ǭ�] �K���˼�..."+openSdCountDown);
				finalState(openSdCountDown);
			});
		}

		return choice;
	}

	//�ưe�X��§���åB���(�֪��e§���, �P��, �U����§���i��, �b�P�諸����)(�o��)
	/*public void countSentGiftAndChange(int[] sentGift, int[] cardStack, Label[] sentGiftArea, ImageView[][] sentGiftAreaPic, int stackOrder){
		System.out.print("\nstackOrder is "+stackOrder + "///   sentGift.length is" + sentGift.length);
		sentGift[stackOrder]++;
		for(int i = 0; i < sentGift.length; i++){
			if(sentGift[i] <= 0) continue;
			sentGiftArea[i].setGraphic(sentGiftAreaPic[   i   ][ sentGift[i]-1 ]);
		}
	}*/

	//�ưe�X��§���åB���(�U����§���i��, (���a���ιq����)�e§���, (���a���ιq����)�v���ϹϤ��w, §������)
	public void countSentGiftAndChange(int[] sentGift, Label[] sentGiftArea, ImageView[][] arenaPic, int cardKind){
		sentGift[cardKind]++;
		for(int i = 0; i < sentGift.length; i++){
			if(sentGift[i] <= 0) continue;
			sentGiftArea[i].setGraphic(arenaPic[   i   ][ sentGift[i]-1 ]);
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

	//�}[�K��]
	public void finalState(int countDown){
		if(countDown == 0){
			countSentGiftAndChange(sentGift, sentGiftArea, sentGiftAreaPic, actionDecisionOrder[0][0]);
			countSentGiftAndChange(sentGiftOppo, sentGiftAreaOppo, sentGiftAreaPicOppo, record4Oppo[0][0]);

			Label sdOppo = new Label("��誺�K��", recordViewOfGiftsFor4ActionsForAi[0][0]);
			Label sd = new Label("�A��[�K��]", recordViewOfGiftsFor4Actions[0][0]);
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


			Stage resultStage = new Stage();	//���G����
			endRound(resultStage, sentGift, sentGiftOppo);
	
			/*final Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("�p����");	//TODO ��r�� �AĹ�S
			alert.setHeaderText("������i�Ƥ���Τw�ϥ�");	//�C������ �i�J�U�@��
			//alert.setContentText("�Ы��T�w");
			alert.showAndWait();*/

			cardNumber.setText("v �Q�D�����P�O <"+allGifts.get(0).getCardName()+">");
			stackButton.setDisable(false);
			stackButton.setGraphic(allGifts.get(0).getOneOfTheTypeStyle(0));
			stackButton.setOnAction(e ->{
			});
			//show���G���a�B���� �i�J�Nshow, ���}�Nclose
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

	//�������G
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
		Scene endScene = new Scene(endList, 300, 200);
		result.setScene(endScene);
		//Stage endStage = new Stage();
		//endStage.setScene(endScene);
		//endStage.show();

		//gotEnd.setOnAction(e -> { endStage.close();});
	}

	//�i�ܥιL���ʧ@(�a�B����)
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

	//�սc���]�m
	public void whiteBoxSetting(){
		initWhiteBoxTest(primaryStage);
		whiteBoxScene = new Scene(whiteBoxPane, 1800, 800);
		whiteBoxStage.setTitle("White Box");
		whiteBoxStage.setScene(whiteBoxScene);
		//whiteBoxStage.show();
	}

	//�M�w�ѽ֥���
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
 		surfTwoHands_Left.setSpacing(-100);	//�]�Ϥ��O�������Y�p�ɭP�������Z���Y�~�����ϷPı�b�쥻��m
		surfTwoHands_Right.setSpacing(-100);	//�]�Ϥ��O�������Y�p�ɭP�������Z���Y�~�����ϷPı�b�쥻��m


		HBox surfTwoHands_All = new HBox(surfTwoHands_Left, surfTwoHands_Right);
		surfTwoHands_All.setLayoutX(-10);	//�]�Ϥ��O�������Y�p�ɭP���������~�����ϷPı�b�쥻��m
		surfTwoHands_All.setLayoutY(250);
		surfTwoHands_All.setSpacing(230);
		whiteBoxPane.getChildren().add(surfTwoHands_All);

		//�P�w�w�����s�˶i�սc
		Button stackPreview = new Button(null, new ImageView(new Image("pic/back.png")));
		stackPreview.setLayoutX(780);
		stackPreview.setLayoutY(30);
		whiteBoxPane.getChildren().add(stackPreview);
		//whiteBoxStage.initOwner(primaryStage);

		//������r�˶i�սc
		Label worthy = new Label("����");
		Label fillText = new Label("���ˬd");
		Label zeroText = new Label("�s�ˬd");
		Label tZeroText = new Label("�u�s�ˬd");
		Label sdSymbol = new Label("�K���аO");
		Label ldSymbol = new Label("���˼аO");
		Label trashSymbol = new Label("�U���аO");
		Label kgSymbol = new Label("�ػP�аO");
		Label lgSymbol = new Label("�v���аO");
		VBox textVBox = new VBox(worthy, fillText, zeroText, tZeroText, sdSymbol, ldSymbol, trashSymbol, kgSymbol, lgSymbol);
		textVBox.setSpacing(15);
		textVBox.setAlignment(Pos.CENTER);
		textVBox.setLayoutX(830);
		textVBox.setLayoutY(480);
		whiteBoxPane.getChildren().add(textVBox);

		//�P�w�w�����s���s�ƥ�
		boolean viewAll = false;
		knowNextCard();
		/*stackPreview.setOnAction(e->{
			if(viewAll){
				viewAll = false;
				//�s�W����d�P�i�ܱԭz.show();
			}
			else{
				viewAll = true;
				//�s�W����d�P�i�ܱԭz.close();			
			}
		});*/
		stackPreview.setOnMouseEntered(e ->{
			//final boolean viewAlll = viewAll;	//�o��S��
			//if(!viewAlll) nextCardStage.show();
			double left_up_pointX = e.getSceneX()-e.getX();
			double left_up_pointY = e.getSceneY()-e.getY();
			System.out.print("�зǦ�mX/Y : " + left_up_pointX + "/" + left_up_pointY + "\n");
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

	//��s�սc�|��ܪ��U�@�i�d
	public void knowNextCard(){
		//GiftHands aiNextCard;
		//GiftHands playerNextCard;
		ImageView aiNextCard;
		ImageView playerNextCard;

		/*if(currentTurn){
			aiNextCard = allGifts.get(1).getTypeOriginal();
			playerNextCard = allGifts.get(0).getTypeOriginal();
			System.out.print("CUUUUUURRR��\n");
		}
		else{
			aiNextCard = allGifts.get(0).getTypeOriginal();
			playerNextCard = allGifts.get(1).getTypeOriginal();
			System.out.print("���w������q\n");
		}
			aiNextCard = allGifts.get(1).getTypeOriginal();
			playerNextCard = allGifts.get(0).getTypeOriginal();*/

		/*Scene nextCardSceneLeft = new Scene(new Pane(aiNextCard));
		Scene nextCardSceneRight = new Scene(new Pane(playerNextCard));
		nextCardStageLeft.setScene(nextCardSceneLeft);
		nextCardStageRight.setScene(nextCardSceneRight);*/
	}
}






/*		KeyCode���g�k
		Button.setOnKeyPressed(e->{
			if(e.getCode() == KeyCode.ENTER){
				//some code
			}
		});
*/