import java.util.ArrayList;
import java.util.Arrays;
import java.security.SecureRandom;
public class AiBrain{

	int[] sentGift = new int[7];			//己方已送出禮物
	int[] sentGiftPlayer = new int[7];		//對方已送出禮物
	boolean[] usedAction = new boolean[4];	//己方已用動作
	boolean[] usedActionPlayer = new boolean[4];	//對方已用動作
	String[] artistStatus = new String[7];		//藝者歸屬
	ArrayList<GiftHands> hands = new ArrayList<>();//手牌陣列
	ArrayList<GiftHands> out =  new ArrayList<>();	//出牌陣列
	int[] secretDeal7statistics = new int[7];
	int[] lostDeal7statistics = new int[7];
	int[] sentDistance = new int[7];

	int action = 0;
	int cardss = 1;					//假性應戰，僅在行93附近用到

	private final int[] worthOfArtist = {5, 4, 3, 3, 2, 2, 2};	//藝者之魅力值(勿更動)
	private final int[] NumberOfGifts = {5, 4, 3, 3, 2, 2, 2};	//禮物之總數(勿更動)
	private double[] distanceExtra = {0.89, 1.06, 1, 1.06, 0.89};	//場牌差距加成(勿更動)
	private double[] fillExtra = {1.10, 1.08, 1.06, 1.06, 1.04, 1.04, 1.04};	//場牌差距加成(勿更動)
	int[] existGift7statistics = {5, 4, 3, 3, 2, 2, 2};	//扣除己方取捨所剩餘的禮物牌張數
	int[] hiddenGift7statistics = {5, 4, 3, 3, 2, 2, 2};	//扣除己方密約、己方取捨以及場上已送出的所剩餘的禮物牌張數
	int[] hands7statistics = new int[7];		//手中各類禮物牌統計
 	int[] trueSentGift = new int[7];		//紀錄場上已送出以及被密約的禮物牌總和

	double[] assumeWorth = {1.66, 1.6, 1.5, 1.5, 1.33, 1.33, 1.33};		//自己想出來的價值陣列初始化
	double[] cp = {1, 1, 1, 1, 1, 1, 1};		//純粹紀錄卡分比
	double[] trueCP = {1, 1, 1, 1, 1, 1, 1};		//考慮自身[密約]、[取捨]後的卡分比
	int[] getSecure = {3, 3, 2, 2, 2, 2, 2};		//若要以絕對多數贏得該藝者所需要的張數，簡稱絕對張數，此為絕對張數陣列
	boolean[] zero = new boolean[7];		//不考慮雙方[密約]何種禮物牌，某方已送出禮物大於等於絕對張數則標示為true
	boolean[] trueZero = new boolean[7];		//考慮自身[密約]、[取捨]後，某方已送出禮物大於等於絕對張數則標示為true
	boolean[] fill = new boolean[7];		//該種禮物牌所以未出現的禮物卡至少一張，且盡在我手則標示為true

	int[] markSecretDeal = new int[7];
	int[] markLostDeal = new int[7];
	int[] markTrash = {5, 5, 5, 5, 5, 5, 5};
	int[] markKindGift = new int[7];
	int[] markLoathGift = new int[7];

	int iWantYou = 0;
	int[] listCardKind = {0, 0, 0, 0, 0, 0, 0};
	int[][] actionCombo = {{0}, {0, 0}, {0, 0, 0}, {0, 0, 0, 0}};
	double[][] comboTable = {{0, 0, 0, 0},
			{0, 0, 0, 0},
			{0, 0, 0, 0},
			{0, 0, 0, 0},
			{0, 0, 0, 0},
			{0, 0, 0, 0},
			{0, 0, 0, 0}};

	public AiBrain(){
	}

	//#1讓電腦知道場上已送出的禮物牌數量
	public void initBaseImfoSentGift(int[] sent_Gift, int[] sent_Gift_Player){
		System.out.print("\n\t\t\t櫻 茶 傘 弦 笛 書 扇");
		System.out.print("\n舊的sentGift : \t\t"+Arrays.toString(sentGift));
		System.out.print("\n舊的sentGiftPlayer : \t"+Arrays.toString(sentGiftPlayer));
		/*以下兩行使用使用陣列拷貝不使用=址參的原因是，址參會讓兩名字不獨立
		*獨立兩個陣列，讓電腦判斷時使用自己內建陣列，要做預先計算時也較為方便*/
		System.arraycopy(sent_Gift, 0, sentGift, 0, sent_Gift.length);
		System.arraycopy(sent_Gift_Player, 0, sentGiftPlayer, 0, sent_Gift.length);
		System.out.print("\n新的sentGift : \t\t"+Arrays.toString(sentGift));
		System.out.print("\n新的sentGiftPlayer : \t"+Arrays.toString(sentGiftPlayer));
	}

	//#2初始化電腦進行判斷所需、除了場上已送出禮物牌以外的基礎資訊
	public void initBaseImfoElse(String[] artistBelongTo, boolean[] used_Action, boolean[] used_Action_Player, ArrayList<GiftHands> handsCard, int[] sd7statistics, int[] ld7statistics){
		System.out.print("\n\n初始化電腦進行判斷所需、除了場上已送出禮物牌以外的基礎資訊............");

		//以下八行為電腦判斷該做何種動作需要的八個陣列
		//usedAction = used_Action;
		iWantYou = 0;
		usedActionPlayer = used_Action_Player;
		artistStatus = artistBelongTo;
		hands = handsCard;
		secretDeal7statistics = sd7statistics;
		lostDeal7statistics = ld7statistics;

		countHands7statistics();			//統計手牌
		changeTrueSentGift(trueSentGift);		//改變真實送出的禮物數量
		changeExistGift7statistics(existGift7statistics);	//改變真實存在的禮物數量與藝者的過半取得張數
		changeHiddenGift7statistics(hiddenGift7statistics);	//改變未知的牌量
		checkZero(zero, trueZero);			//零檢查
		//這邊可以考慮塞個讓existGift7statistics再度減去場牌的 目前此功能僅有利於checkFill 往後可能算機率會需要
		checkFill(fill);				//滿檢查
		handsCheck(hands7statistics, markSecretDeal, markLostDeal, markTrash, markKindGift, markLoathGift);
		updateAssumeWorth();			//更新每種禮物的椽鈞價值
		updateTrueCP();				//更新每種禮物的真實CP值
		printSet();
	}

	//#3 : 電腦主要的判斷 引數列:(int[] 對家禮物, int[] 己方禮物, String 藝者歸屬, boolean[] 對方已用動作, boolean[] 己方已用動作, int[] 手牌)
	public ArrayList<GiftHands> thinkOut(int[] sent_Gift, int[] sent_Gift_Player, String[] artistBelongTo, boolean[] used_Action, boolean[] used_Action_Player, ArrayList<GiftHands> handsCard, int[] sd7statistics, int[] ld7statistics){
		initBaseImfoSentGift(sent_Gift, sent_Gift_Player);	//更新送出禮物數
		initBaseImfoElse(artistBelongTo, used_Action, used_Action_Player, handsCard, sd7statistics, ld7statistics);	//*#2 初始化
		out = new ArrayList<GiftHands>();


		System.out.print("\n[程序員] 進判斷，手牌長度是"+ handsCard.size());

		for(int i = 0; i < 7; i++){
			if(i < hands.size()){
				listCardKind[i] = hands.get(i).getCardKinds();
			}
			else{
				listCardKind[i] = -1;
			}
		}

		fuzzy(sent_Gift, sent_Gift_Player, listCardKind, comboTable);


		iWantYou = analyis(comboTable, actionCombo, hands.size());	//回傳最薛且還沒做過的動作
		usedAction[iWantYou] = true;
		for(int i = 0; i <= iWantYou; i++){
			hands.get( actionCombo[iWantYou][i] ).setPicked(true);
			out.add(hands.get(i));
		}

/*		//以下4行為假性應戰，實戰程式碼應移除
		for(int i = 0; i < cardss; i++){	//這個迴圈為假性應戰
			hands.get(i).setPicked(true);
			out.add(hands.get(i));
		}
		cardss++;
*/
		return out;
	}
	//#4電腦對於玩家使用動作[贈與] 的回應
	public int reactKindGift(int[] sent_Gift, int[] sent_Gift_Player, String[] artistBelongTo, boolean[] used_Action, boolean[] used_Action_Player, ArrayList<GiftHands> handsCard, int[] sd7statistics, int[] ld7statistics, int[] kindGift){
		initBaseImfoSentGift(sent_Gift, sent_Gift_Player);	//*#1更新送出禮物數
		kindGift = statisticsKGift(3, kindGift);		//*#5統計張數並將必定的結果率先納入現有狀況計算(for贈與)
		if(kindGift.length == 1){
			System.out.print("\n\n「什麼啊，三張根本一樣！」");
			System.out.print("\n[回應贈與] 選\"某張禮物\"\n\n");
			return 0;		//三張重複就直接選第一張
		}
		//排除三張相同的情況後，接下來的程式碼將判斷選擇哪一張比較好
		initBaseImfoElse(artistBelongTo, used_Action, used_Action_Player, handsCard, sd7statistics, ld7statistics);	//*#2 初始化
		int choice = 0;				//先假設選第一張價值最高
		double max = assumeWorth[ kindGift[0] ];	//先將第一張的價值給定max
		double temp;
		System.out.print("\n揀選最有價值的禮物牌 : "+Arrays.toString(kindGift));
		for(int i = 1; i < kindGift.length; i++){
			temp = assumeWorth[ kindGift[i] ];
			if(temp > max){
				max = temp;
				choice = i;
			}
			else if(temp == max){
				if(trueCP[kindGift[i]] > trueCP[kindGift[choice]])
					choice = i;
			}
		}

		return choice;
	}
	//#5統計張數並將必定的結果率先納入現有狀況計算(for贈與)☆僅受 #4 reactKindGift呼叫☆
	public int[] statisticsKGift(int action, int[] someGift){
		int index = 0;			//回傳新陣列時作遞進的索引值使用
		int copyLength = 3;			//需要拷貝的張數，即相異的禮物牌張數
		int[] copySomeGift = new int[3];
		int[] newSomeGift = new int[3];	//初始化欲回傳陣列
		int[] kg7s = new int[7];		//7張禮物的統計
		Arrays.fill(kg7s, 0);
		for(int e : someGift){
			if(kg7s[e] == 0){		//倘若此種禮物牌未出現
				kg7s[e]++;
				copySomeGift[index++] = e;	//那便是需要拷貝的
			}
			else{
				sentGiftPlayer[e]++;	//玩家得到重複的禮物牌
				copyLength--;		//需拷貝的禮物牌張數減一
			}
		}
		newSomeGift = new int[copyLength];
		System.arraycopy(copySomeGift, 0, newSomeGift, 0, copyLength);

		return newSomeGift;
	}

	//#6電腦對於玩家使用動作[競爭] 的回應
	public int reactLoathGift(int[] sent_Gift, int[] sent_Gift_Player, String[] artistBelongTo, boolean[] used_Action, boolean[] used_Action_Player, ArrayList<GiftHands> handsCard,
				int[] sd7statistics, int[] ld7statistics, ArrayList<GiftHands> combination1, ArrayList<GiftHands> combination2){//*/

		initBaseImfoSentGift(sent_Gift, sent_Gift_Player);		//*#1更新送出禮物數
		statisticsLGift(combination1, combination2);		//*#7統計張數並將必定的結果率先納入現有狀況計算(for競爭)
		initBaseImfoElse(artistBelongTo, used_Action, used_Action_Player, handsCard, sd7statistics, ld7statistics);	//*#2 初始化

		int choice = 0;
		double a = 0;
		double b = 0;
		for(int i = 0; i < combination1.size(); i++){
			a += assumeWorth[ combination1.get(i).getCardKinds() ];
			b += assumeWorth[ combination2.get(i).getCardKinds() ];
		}
		System.out.print("組合1價值 : " + a + " / 組合2價值 : " + b +"\n");
		if(a == b){
			System.out.print("\n就決定是你了，正電拍拍\n");
			SecureRandom random = new SecureRandom();
			choice = random.nextInt(2);
		}
		else if(a > b)	choice = 1;
		else		choice = 2;

		return choice;
	}

	//#7統計張數並將必定的結果率先納入現有狀況計算(for競爭)☆僅受 #6 reactLoathGift呼叫☆
	public void statisticsLGift(ArrayList<GiftHands> choice1, ArrayList<GiftHands> choice2){
		int checkKind = 0;
		boolean secondCheck = false;

		//交錯比對兩組禮物的重複處
		for(int i = 0; i < choice1.size(); i++){
			for(int j = 0; j < choice2.size(); j++){
				checkKind = choice1.get(i).getCardKinds();
				if(checkKind ==  choice2.get(j).getCardKinds() ){	//若有相同禮物
					sentGift[ checkKind ]++;
					sentGiftPlayer[ checkKind ]++;	//雙方此種禮物各加一
					choice1.remove(i);
					choice2.remove(j);			//從兩組禮物移除這個相同禮物
					if(i == 0) secondCheck = true;		//剩餘各一個禮物是否相同之檢查啟動
					break;
				}
			}
		}

		if(secondCheck){
			checkKind = choice1.get(0).getCardKinds();
			if(checkKind == choice2.get(0).getCardKinds()){
				sentGift[ checkKind ]++;
				sentGiftPlayer[ checkKind ]++;
				choice1.remove(0);
				choice2.remove(0);
			}
		}
	}



	public String checkActionWorth(double[] aw, int action){
		String actionCode = "";
		double actionWorth = 0;
		int better = 0;
		/*switch(action){
		case 1:
			for(int



		case 2:


		case 3:


		case 4:
		}*/

		return actionCode;
	}

	//印出套餐printSet會印出一些基礎資訊陣列
	public void printSet(){
		System.out.print("\n手牌 : ");
		for(int i = 0; i < hands.size(); i++) System.out.print(hands.get(i).getCardName()+" / ");
		System.out.print("\n櫻 茶 傘 弦 笛 書 扇");
		System.out.print("\n"+Arrays.toString(hands7statistics)+"\t<-電腦手牌");

		if(usedAction[0]){
			System.out.print("\n"+Arrays.toString(secretDeal7statistics)+"\t<-[密約]");
			System.out.print("\n"+Arrays.toString(trueSentGift)+"\t<-[密約]後真實送出場面禮物統計");
		}
		else System.out.print("\n"+Arrays.toString(sentGift)+"\t<-電腦場面禮物量");

		if(usedAction[1]){
			System.out.print("\n"+Arrays.toString(lostDeal7statistics)+"\t<-[取捨]");
			System.out.print("\n"+Arrays.toString(existGift7statistics)+"\t<-[取捨]後真實禮物統計");
		}
		else System.out.print("\n"+Arrays.toString(NumberOfGifts)+"\t<-總禮物量");
		System.out.print("\n"+Arrays.toString(sentGiftPlayer)+"\t<-玩家禮物量");

		System.out.print("\n\n"+Arrays.toString(hiddenGift7statistics)+"\t<-未知的禮物牌");
		System.out.print("\n"+Arrays.toString(hands7statistics)+"\t<-電腦手牌\n");
		for(int i = 0; i < trueZero.length; i++)
			if(i == 0)   System.out.print(  "[" + ((trueZero[i])? "T" : "f"));
			else System.out.print( ", " + ((trueZero[i])? "T" : "f") );
		System.out.print("]\t<-真‧零檢查\n");
		for(int i = 0; i < zero.length; i++)
			if(i == 0)   System.out.print(  "[" + ((trueZero[i])? "T" : "f"));
			else System.out.print( ", " + ((zero[i])? "T" : "f") );
		System.out.print("]\t<-零檢查\n");
		for(int i = 0; i < fill.length; i++)
			if(i == 0)   System.out.print(  "[" + ((trueZero[i])? "T" : "f"));
			else System.out.print( ", " + ((fill[i])? "T" : "f") );
		System.out.print("]\t<-滿檢查\n");
		double[] abc = Arrays.copyOf(assumeWorth, assumeWorth.length);
		for(int i = 0; i<abc.length; i++) abc[i] -= 1.0;
		System.out.print("\n"+Arrays.toString(abc)+"\t<-椽鈞價值");
		System.out.print("\n"+Arrays.toString(trueCP)+"\t<-真實cp值");
		System.out.print("\n"+Arrays.toString(markSecretDeal)+"\t<-密約標記");
		System.out.print("\n"+Arrays.toString(markLostDeal)+"\t<-取捨標記");
		System.out.print("\n"+Arrays.toString(markTrash)+"\t<-垃圾標記");
		System.out.print("\n"+Arrays.toString(markKindGift)+"\t<-贈與標記");
		System.out.print("\n"+Arrays.toString(markLoathGift)+"\t<-競爭標記");
		System.out.print("\n");
	}

	//統計手牌各類禮物張數
	public void countHands7statistics(){
		Arrays.fill(hands7statistics, 0);
		for(int i = 0; i < hands.size(); i++) hands7statistics[ hands.get(i).getCardKinds() ]++;
	}
	//若使用過密約 陣列trueSentGift會記錄真實的已送出禮物張數分布
	public void changeTrueSentGift(int[] atsg7s){
		System.arraycopy(sentGift, 0, atsg7s, 0, 7);	//1015新增TODO : 這樣做是不好的 應使用密約當下呼叫一次
		if(usedAction[0]) for(int i = 0; i < atsg7s.length; i++) atsg7s[i] += secretDeal7statistics[i];

		/*TODO : 物件導向 1013新增此TODO
		*宜將程式碼變得更物件化
		*第一行不變
		*atsg7s[secretDealCard.getCardKinds]++;
		*/
	}
	//若己方使用過取捨 陣列existGift7statistics會記錄扣除己方取捨後剩餘的禮物牌張數 與 改變該對應藝者的過半取得張數
	public void changeExistGift7statistics(int[] eg7s){
		System.arraycopy(NumberOfGifts, 0, eg7s, 0, 7);	//1015新增TODO : 這樣做是不好的 應使用取捨當下呼叫一次
		if(usedAction[1]) for(int i = 0; i < eg7s.length; i++){
			eg7s[i] -= lostDeal7statistics[i];
			getSecure[i] = eg7s[i]/2 + 1;
		}
		/*TODO : 物件導向 1013新增此TODO
		*宜將程式碼變得更物件化
		*程式碼可改為下列兩行***java這裡可以問昆翰自己的想法是不是確實較佳
		*eg7s[lostDealCard1.getCardKinds]--;
		*eg7s[lostDealCard2.getCardKinds]--;
		*getSecure.....晚點想 科科
		*/
	}
	//若有人出牌或己方密約取捨，會改變未知的牌量
	public void changeHiddenGift7statistics(int[] hg7s){
		System.arraycopy(NumberOfGifts, 0, hg7s, 0, 7);		//1015新增TODO : 這樣做是不好的 應每個需要改變的當下確認
		for(int i = 0; i < hg7s.length; i++) hg7s[i] -= (sentGift[i] + sentGiftPlayer[i] + secretDeal7statistics[i] + lostDeal7statistics[i]);
	}

	/*零檢查checkZero改變zero陣列的元素值 若使用過密約取捨則會一同改變TrueZero的元素值
	*zero只單純看場上牌量 不考慮我方密約及取捨的情況下 哪張牌已被一方"確定擁有" 失去價值
	*trueZero則考慮我方密約取捨的情況下 確立哪張牌已被一方"確定擁有" 失去價值
	*現有禮物數 > 1/2總禮物數 即"確定擁有" (移項) 2*現有禮物數 > 總禮物數 即"確定擁有"*/
	public void checkZero(boolean[] z, boolean[] tz){
		int doubleSentGift;
		for(int i = 0; i < z.length; i++){
			if(z[i]) continue;
			doubleSentGift = 2 * sentGiftPlayer[i];// + ((db.playerOwn[i])? 1 : 0);
			if(doubleSentGift > NumberOfGifts[i]) tz[i] = z[i] = true;
			doubleSentGift = 2 * sentGift[i];//+ ((db.aiOwn[i])? 1 : 0);
			if(doubleSentGift > NumberOfGifts[i]) tz[i] = z[i] = true;

			if(tz[i]) continue;
			doubleSentGift = 2 * sentGiftPlayer[i];// + ((db.playerOwn[i])? 1 : 0);
			if(doubleSentGift > existGift7statistics[i]) tz[i] = true;
			doubleSentGift = 2 * trueSentGift[i];// + ((db.aiOwn[i])? 1 : 0);
			if(doubleSentGift > existGift7statistics[i]) tz[i] = true;
		}
	}
	//滿檢查checkFill檢查我方現存的手牌裡面有沒有恰能與場上的已送出禮物(含我方密約)相加後為真實現存張數
	public void checkFill(boolean[] f){
		for(int i = 0; i < f.length; i++){
			boolean bo1 = hands7statistics[i] > 0;
			//1015註解boolean bo2 = sentGiftPlayer[i] + trueSentGift[i] + hands7statistics[i] == existGift7statistics[i];
			boolean bo2 = hands7statistics[i] == hiddenGift7statistics[i];
			f[i] = (bo1 && bo2)? true : false ;
		}
	}

	//更新每種禮物的椽鈞價值
	public void updateAssumeWorth(){
		System.out.print("\n\nold assumeWoth : \t\t"+Arrays.toString(assumeWorth)+"\n");

		for(int i = 0; i < 7; i++){
			if(assumeWorth[i] == 0) continue;	//價值歸零是一個單局內無法還原的判斷 已是0就直接判斷下一個
			if(trueZero[i]){
				assumeWorth[i] = 0;
			}
			else{				//TODO然後有些特別的加成 還沒寫
				assumeWorth[i] = (double)(worthOfArtist[i]) / ((double)((double)(existGift7statistics[i]) + 1.0) / 2.0);
				assumeWorth[i] *= distanceExtra[ sentDistance[i]+2 ];	//場牌拉鋸加成
				if(fill[i]) assumeWorth[i] *= fillExtra[i];			//滿檢查加成
				//assumeWorth[i] *= distanceExtra[ sentDistance[i]+2 ];	//手牌掌控加成(還沒寫 考慮把滿檢查併進來)
				assumeWorth[i] = (Math.round(assumeWorth[i]*100.0))/100.0; //讓小數點為二位
			}
		}

		System.out.print("new assumeWoth : \t\t"+Arrays.toString(assumeWorth)+"\n");
  	}
	//更新每種禮物的真實CP值
	public void updateTrueCP(){
		System.out.print("\nold trueCP : \t\t\t"+Arrays.toString(trueCP)+"\n");

		for(int i = 0; i < 7; i++){
			if(trueCP[i] == 0) continue;		//價值歸零是一個單局內無法還原的判斷 已是0就直接判斷下一個
			//下一行是增加第一局往後的版本 Adventure可能是0or 0.5這樣
			//if(db.playerSentGift[i]+db.playerAdventure >= getSecure[i] || db.aiSentGift[i]+db.aiAdventure >= getSecure[i]) trueCP[i] = 0;
			if(trueZero[i]) trueCP[i] = 0;
			else trueCP[i] = (double)(worthOfArtist[i]) / (double)(existGift7statistics[i]);
			trueCP[i] = (Math.round(trueCP[i] *100.0))/100.0;
			cp[i] = trueCP[i];			//先讓cp與trueCP相同 要改
		}

		System.out.print("new trueCP : \t\t\t"+Arrays.toString(trueCP)+"\n");
	}
	//手牌體檢h是手牌統計 長度7 滿元素
	public void handsCheck(int[] h, int[] mSD, int[] mLD, int[] mT, int[] mKG, int[] mLG){
		sentDistance = arrayEleDif(sentGift, sentGiftPlayer);
		for(int i = 0; i < h.length; i++){
			mSD[i] = mLD[i] = mT[i] = mKG[i] = mLG[i] = 0;
			if(zero[i]) mT[i] = 1;
			else if(trueZero[i]) mT[i] = 3;
			if(h[i] == 0) continue;	//沒有此牌就不討論
			//if(h[i] == 0 || mT[i] < 5) continue;	//沒有此牌或他是垃圾就不討論
			//對了 opponent在玩家眼中即"對手"之意 故opponent指的其實就是電腦本身
			if(!usedAction[0]){
				//雙方場面平手，僅剩一張在手
				if(h[i] == 1 && fill[i] && sentDistance[i] == 0 ){
					if(artistStatus[i].equals("opponent")) 	mSD[i] = (usedAction[1])? 4 : 6 ;
					else				mSD[i] = 5;	//這邊差一張密約別人中立都設為相同分
				}
				//不是以上情況 但差一張就能穩拿這位
				else if(sentGift[i] + 1 == getSecure[i]) mSD[i] = 3;
				//1021註解 太冗 TODO : 弄懂是否上面那行既精簡又意義相同else if(!trueZero[i] && 2*(sentGift[i]+1) > existGift7statistics[i]) mSD[i] = 3;
			}
			if(!usedAction[1]){
				//丟掉就拿到(1張)
				if(h[i] == 1 && fill[i] && sentGift[i] >= sentGiftPlayer[i]){
					if(artistStatus[i].equals("opponent")) 	mLD[i] = (6-sentDistance[i]);	//若是對手的 依牌量差而配分 最高價值6分
					else				mLD[i] = (5-sentDistance[i]);	//若不是對手的 依牌量差而配分 最高價值5分
				}
				//丟掉就拿到(2張)
				else if(h[i] == 2 && fill[i] && sentGift[i] >= sentGiftPlayer[i]){
					if(artistStatus[i].equals("opponent")) 	mLD[i] = (4-sentDistance[i]);	//(兩張的情況)若是對手的 依牌量差而配分 最高價值4分
					else				mLD[i] = (3-sentDistance[i]);	//(兩張的情況)若不是對手的 依牌量差而配分 最高價值3分
				}
				//丟一張(搭配
				else if(h[i] == 3 && fill[i] && sentDistance[i] == 0 && artistStatus[i].equals("opponent") && usedAction[3]){
					mLD[i] = 5;
				}
			}
			if(!usedAction[2]){
				if(h[i] == 3 && fill[i] && sentDistance[i] == 0 ){
					mKG[i] = 6;
					if(artistStatus[i].equals("opponent") && !usedAction[1] && !usedAction[3]) mKG[i] = 3;
				}
				else if(artistStatus[i].equals("player")){
					mKG[i] = 2;
				}
			}
			if(!usedAction[3]){
				if(h[i] == 4 && fill[i] && sentDistance[i] == 0 && artistStatus[i].equals("opponent")) mLG[i] = 4;
				else if(h[i] == 2 && fill[i] && sentDistance[i] == 0 && artistStatus[i].equals("opponent")) mLG[i] = 5;
				else if(h[i] == 3 && fill[i] && sentDistance[i] == 0 && artistStatus[i].equals("opponent")) mLG[i] = 1;
				else if(h[i] == 2 && fill[i] && sentDistance[i] == 1 && artistStatus[i].equals("first")) mLG[i] = 3;
			}
		}
	}
	public int[] arrayEleDif(int[] a, int[] b){
		int length = Math.min(a.length, b.length);
		int[] difArray = new int[length];
		for(int i = 0; i < difArray.length; i++)
			difArray[i] = a[i] - b[i];
		System.out.print("dif : "+ Arrays.toString(difArray) +"\n");
		return difArray;
	}

	//重設ai設置
	public void reset(){
		System.out.print("");



	}



	public int analyis(double[][] comboTable, int[][] actionCombo, int limit){
		double max = -1;
		double sum = 0;
		double[] maxs = {0, 0, 0, 0};
		maxs[0] = 0;
		if(!usedAction[0]){
			for(int i = 0; i < limit; i++){
				if(comboTable[i][0] > max){
					max = comboTable[i][0];
					actionCombo[0][0] = i;
				}
			}
		}
		maxs[0] = max;
		maxs[1] = 0;
		max = -1;
		if(!usedAction[1]){
			for(int i = 0; i < limit - 1; i++){
			for(int j = i+1; j < limit; j++){
				sum = comboTable[i][1] + comboTable[j][1];
				if(sum > max){
					max = sum;
					actionCombo[1][0] = i;
					actionCombo[1][1] = j;
				}
			}
			}
		}
		maxs[1] = max;
		maxs[2] = 0;
		max = -1;
		if(!usedAction[2]){
			for(int i = 0; i < limit - 2; i++){
			for(int j = i+1; j < limit-1; j++){
			for(int k = j+1; k < limit; k++){
				sum = comboTable[i][2] + comboTable[j][2] + comboTable[k][2];
				if(sum > max){
					max = sum;
					actionCombo[2][0] = i;
					actionCombo[2][1] = j;
					actionCombo[2][2] = k;
				}
			}
			}
			}
		}
		maxs[2] = max;
		maxs[3] = 0;
		max = -1;
		if(!usedAction[3]){		//TODO  改
			for(int i = 0; i < limit - 3; i++){
			for(int j = i+1; j < limit-2; j++){
			for(int k = j+1; k < limit-1; k++){
			for(int m = k+1; m < limit; m++){
				sum = comboTable[i][3] + comboTable[j][3] + comboTable[k][3] + comboTable[m][3];
				if(sum > max){
					max = sum;
					actionCombo[3][0] = i;
					actionCombo[3][1] = j;
					actionCombo[3][2] = k;
					actionCombo[3][3] = m;
				}
			}
			}
			}
			}
		}
		maxs[3] = max;
		int mmmax = 0;
		max = 0;
		for(int i = 0; i < maxs.length; i++){
			maxs[i] /= (i+1);
			if(maxs[i] > max){
				max = maxs[i];
				mmmax = i;
			}
		}
		usedAction[mmmax] = true;

		return mmmax;
	}

	public void fuzzy(int[] sent_Gift, int[] sent_Gift_Player, int[] listCardKind, double[][] comboTable){
		double[][] copy = {	{0.8, 0.7, 0.6, 0.0},
				{0.9, 0.7, 0.6, 0.4},
				{0.8, 0.7, 0.5, 0.3},
				{0.8, 0.7, 0.6, 0.5},
				{0.6, 0.1, 0.1, 0.1},
				{0.8, 0.4, 0.2, 0.1},
				{0.8, 0.7, 0.6, 0.5}};
		int[] dife = {0, 0, 0, 0, 0, 0, 0};
		for(int i = 0; i < dife.length; i++){
			dife[i] = sent_Gift[i] - sent_Gift_Player[i];
		}
		int kind = 0;
		for(int i = 0; i < listCardKind.length; i++){
			kind = listCardKind[i];
			if(kind == -1) continue;
			if(dife[kind] < 0){
				if(hands7statistics[kind] < 2){
					copy[kind][0] = 0.11;
					copy[kind][1] = 0.66;
					copy[kind][2] = 0.25;
					copy[kind][3] = 0.22;
				}
				else if(hands7statistics[kind] < 3){
					copy[kind][0] = 0.14;
					copy[kind][1] = 0.38;
					copy[kind][2] = 0.48;
					copy[kind][3] = 0.68;
				}
				else{
					copy[kind][0] = 0.19;
					copy[kind][1] = 0.51;
					copy[kind][2] = 0.26;
					copy[kind][3] = 0.22;
				}
			}
			else if(dife[kind] == 0){
				if(hands7statistics[kind] < 2){
					copy[kind][0] = 0.85;
					copy[kind][1] = 0.71;
					copy[kind][2] = 0.55;
					copy[kind][3] = 0.23;
				}
				else if(hands7statistics[kind] < 3){
					copy[kind][0] = 0.78;
					copy[kind][1] = 0.88;
					copy[kind][2] = 0.48;
					copy[kind][3] = 0.68;
				}
				else{
					copy[kind][0] = 0.96;
					copy[kind][1] = 0.58;
					copy[kind][2] = 1.0;
					copy[kind][3] = 0.42;
				}
			}
			else if(dife[kind] > 0){
				if(hands7statistics[kind] < 2){
					copy[kind][0] = 0.77;
					copy[kind][1] = 0.81;
					copy[kind][2] = 0.63;
					copy[kind][3] = 0.22;
				}
				else if(hands7statistics[kind] < 3){
					copy[kind][0] = 0.18;
					copy[kind][1] = 0.92;
					copy[kind][2] = 0.28;
					copy[kind][3] = 0.73;
				}
				else{
					copy[kind][0] = 0.54;
					copy[kind][1] = 0.62;
					copy[kind][2] = 0.77;
					copy[kind][3] = 0.26;
				}
			}
		}

		for(int i = 0; i < comboTable.length; i++){
			for(int j = 0; j < comboTable[i].length; j++){
				comboTable[i][j] = copy[i][j];
			}
		}
	}
}
