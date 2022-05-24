import java.util.ArrayList;
import java.util.Arrays;
import java.security.SecureRandom;
public class AiBrain{

	int[] sentGift = new int[7];			//�v��w�e�X§��
	int[] sentGiftPlayer = new int[7];		//���w�e�X§��
	boolean[] usedAction = new boolean[4];	//�v��w�ΰʧ@
	boolean[] usedActionPlayer = new boolean[4];	//���w�ΰʧ@
	String[] artistStatus = new String[7];		//�����k��
	ArrayList<GiftHands> hands = new ArrayList<>();//��P�}�C
	ArrayList<GiftHands> out =  new ArrayList<>();	//�X�P�}�C
	int[] secretDeal7statistics = new int[7];
	int[] lostDeal7statistics = new int[7];
	int[] sentDistance = new int[7];

	int action = 0;
	int cardss = 1;					//�������ԡA�Ȧb��93����Ψ�

	private final int[] worthOfArtist = {5, 4, 3, 3, 2, 2, 2};	//���̤��y�O��(�ŧ��)
	private final int[] NumberOfGifts = {5, 4, 3, 3, 2, 2, 2};	//§�����`��(�ŧ��)
	private double[] distanceExtra = {0.89, 1.06, 1, 1.06, 0.89};	//���P�t�Z�[��(�ŧ��)
	private double[] fillExtra = {1.10, 1.08, 1.06, 1.06, 1.04, 1.04, 1.04};	//���P�t�Z�[��(�ŧ��)
	int[] existGift7statistics = {5, 4, 3, 3, 2, 2, 2};	//�����v����˩ҳѾl��§���P�i��
	int[] hiddenGift7statistics = {5, 4, 3, 3, 2, 2, 2};	//�����v��K���B�v����˥H�γ��W�w�e�X���ҳѾl��§���P�i��
	int[] hands7statistics = new int[7];		//�⤤�U��§���P�έp
 	int[] trueSentGift = new int[7];		//�������W�w�e�X�H�γQ�K����§���P�`�M

	double[] assumeWorth = {1.66, 1.6, 1.5, 1.5, 1.33, 1.33, 1.33};		//�ۤv�Q�X�Ӫ����Ȱ}�C��l��
	double[] cp = {1, 1, 1, 1, 1, 1, 1};		//�º�����d����
	double[] trueCP = {1, 1, 1, 1, 1, 1, 1};		//�Ҽ{�ۨ�[�K��]�B[����]�᪺�d����
	int[] getSecure = {3, 3, 2, 2, 2, 2, 2};		//�Y�n�H����h��Ĺ�o�����̩һݭn���i�ơA²�ٵ���i�ơA��������i�ư}�C
	boolean[] zero = new boolean[7];		//���Ҽ{����[�K��]���§���P�A�Y��w�e�X§���j�󵥩󵴹�i�ƫh�Хܬ�true
	boolean[] trueZero = new boolean[7];		//�Ҽ{�ۨ�[�K��]�B[����]��A�Y��w�e�X§���j�󵥩󵴹�i�ƫh�Хܬ�true
	boolean[] fill = new boolean[7];		//�Ӻ�§���P�ҥH���X�{��§���d�ܤ֤@�i�A�B�ɦb�ڤ�h�Хܬ�true

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

	//#1���q�����D���W�w�e�X��§���P�ƶq
	public void initBaseImfoSentGift(int[] sent_Gift, int[] sent_Gift_Player){
		System.out.print("\n\t\t\t�� �� �� �� �� �� ��");
		System.out.print("\n�ª�sentGift : \t\t"+Arrays.toString(sentGift));
		System.out.print("\n�ª�sentGiftPlayer : \t"+Arrays.toString(sentGiftPlayer));
		/*�H�U���ϥΨϥΰ}�C�������ϥ�=�}�Ѫ���]�O�A�}�ѷ|����W�r���W��
		*�W�ߨ�Ӱ}�C�A���q���P�_�ɨϥΦۤv���ذ}�C�A�n���w���p��ɤ]������K*/
		System.arraycopy(sent_Gift, 0, sentGift, 0, sent_Gift.length);
		System.arraycopy(sent_Gift_Player, 0, sentGiftPlayer, 0, sent_Gift.length);
		System.out.print("\n�s��sentGift : \t\t"+Arrays.toString(sentGift));
		System.out.print("\n�s��sentGiftPlayer : \t"+Arrays.toString(sentGiftPlayer));
	}

	//#2��l�ƹq���i��P�_�һݡB���F���W�w�e�X§���P�H�~����¦��T
	public void initBaseImfoElse(String[] artistBelongTo, boolean[] used_Action, boolean[] used_Action_Player, ArrayList<GiftHands> handsCard, int[] sd7statistics, int[] ld7statistics){
		System.out.print("\n\n��l�ƹq���i��P�_�һݡB���F���W�w�e�X§���P�H�~����¦��T............");

		//�H�U�K�欰�q���P�_�Ӱ���ذʧ@�ݭn���K�Ӱ}�C
		//usedAction = used_Action;
		iWantYou = 0;
		usedActionPlayer = used_Action_Player;
		artistStatus = artistBelongTo;
		hands = handsCard;
		secretDeal7statistics = sd7statistics;
		lostDeal7statistics = ld7statistics;

		countHands7statistics();			//�έp��P
		changeTrueSentGift(trueSentGift);		//���ܯu��e�X��§���ƶq
		changeExistGift7statistics(existGift7statistics);	//���ܯu��s�b��§���ƶq�P���̪��L�b���o�i��
		changeHiddenGift7statistics(hiddenGift7statistics);	//���ܥ������P�q
		checkZero(zero, trueZero);			//�s�ˬd
		//�o��i�H�Ҽ{�����existGift7statistics�A�״�h���P�� �ثe���\��Ȧ��Q��checkFill ����i�����v�|�ݭn
		checkFill(fill);				//���ˬd
		handsCheck(hands7statistics, markSecretDeal, markLostDeal, markTrash, markKindGift, markLoathGift);
		updateAssumeWorth();			//��s�C��§�����ܶv����
		updateTrueCP();				//��s�C��§�����u��CP��
		printSet();
	}

	//#3 : �q���D�n���P�_ �޼ƦC:(int[] ��a§��, int[] �v��§��, String �����k��, boolean[] ���w�ΰʧ@, boolean[] �v��w�ΰʧ@, int[] ��P)
	public ArrayList<GiftHands> thinkOut(int[] sent_Gift, int[] sent_Gift_Player, String[] artistBelongTo, boolean[] used_Action, boolean[] used_Action_Player, ArrayList<GiftHands> handsCard, int[] sd7statistics, int[] ld7statistics){
		initBaseImfoSentGift(sent_Gift, sent_Gift_Player);	//��s�e�X§����
		initBaseImfoElse(artistBelongTo, used_Action, used_Action_Player, handsCard, sd7statistics, ld7statistics);	//*#2 ��l��
		out = new ArrayList<GiftHands>();


		System.out.print("\n[�{�ǭ�] �i�P�_�A��P���׬O"+ handsCard.size());

		for(int i = 0; i < 7; i++){
			if(i < hands.size()){
				listCardKind[i] = hands.get(i).getCardKinds();
			}
			else{
				listCardKind[i] = -1;
			}
		}

		fuzzy(sent_Gift, sent_Gift_Player, listCardKind, comboTable);


		iWantYou = analyis(comboTable, actionCombo, hands.size());	//�^�ǳ����B�٨S���L���ʧ@
		usedAction[iWantYou] = true;
		for(int i = 0; i <= iWantYou; i++){
			hands.get( actionCombo[iWantYou][i] ).setPicked(true);
			out.add(hands.get(i));
		}

/*		//�H�U4�欰�������ԡA��Ե{���X������
		for(int i = 0; i < cardss; i++){	//�o�Ӱj�鬰��������
			hands.get(i).setPicked(true);
			out.add(hands.get(i));
		}
		cardss++;
*/
		return out;
	}
	//#4�q����󪱮a�ϥΰʧ@[�ػP] ���^��
	public int reactKindGift(int[] sent_Gift, int[] sent_Gift_Player, String[] artistBelongTo, boolean[] used_Action, boolean[] used_Action_Player, ArrayList<GiftHands> handsCard, int[] sd7statistics, int[] ld7statistics, int[] kindGift){
		initBaseImfoSentGift(sent_Gift, sent_Gift_Player);	//*#1��s�e�X§����
		kindGift = statisticsKGift(3, kindGift);		//*#5�έp�i�ƨñN���w�����G�v���ǤJ�{�����p�p��(for�ػP)
		if(kindGift.length == 1){
			System.out.print("\n\n�u����ڡA�T�i�ڥ��@�ˡI�v");
			System.out.print("\n[�^���ػP] ��\"�Y�i§��\"\n\n");
			return 0;		//�T�i���ƴN������Ĥ@�i
		}
		//�ư��T�i�ۦP�����p��A���U�Ӫ��{���X�N�P�_��ܭ��@�i����n
		initBaseImfoElse(artistBelongTo, used_Action, used_Action_Player, handsCard, sd7statistics, ld7statistics);	//*#2 ��l��
		int choice = 0;				//�����]��Ĥ@�i���ȳ̰�
		double max = assumeWorth[ kindGift[0] ];	//���N�Ĥ@�i�����ȵ��wmax
		double temp;
		System.out.print("\n�z��̦����Ȫ�§���P : "+Arrays.toString(kindGift));
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
	//#5�έp�i�ƨñN���w�����G�v���ǤJ�{�����p�p��(for�ػP)���Ȩ� #4 reactKindGift�I�s��
	public int[] statisticsKGift(int action, int[] someGift){
		int index = 0;			//�^�Ƿs�}�C�ɧ@���i�����ޭȨϥ�
		int copyLength = 3;			//�ݭn�������i�ơA�Y�۲���§���P�i��
		int[] copySomeGift = new int[3];
		int[] newSomeGift = new int[3];	//��l�Ʊ��^�ǰ}�C
		int[] kg7s = new int[7];		//7�i§�����έp
		Arrays.fill(kg7s, 0);
		for(int e : someGift){
			if(kg7s[e] == 0){		//�խY����§���P���X�{
				kg7s[e]++;
				copySomeGift[index++] = e;	//���K�O�ݭn������
			}
			else{
				sentGiftPlayer[e]++;	//���a�o�쭫�ƪ�§���P
				copyLength--;		//�ݫ�����§���P�i�ƴ�@
			}
		}
		newSomeGift = new int[copyLength];
		System.arraycopy(copySomeGift, 0, newSomeGift, 0, copyLength);

		return newSomeGift;
	}

	//#6�q����󪱮a�ϥΰʧ@[�v��] ���^��
	public int reactLoathGift(int[] sent_Gift, int[] sent_Gift_Player, String[] artistBelongTo, boolean[] used_Action, boolean[] used_Action_Player, ArrayList<GiftHands> handsCard,
				int[] sd7statistics, int[] ld7statistics, ArrayList<GiftHands> combination1, ArrayList<GiftHands> combination2){//*/

		initBaseImfoSentGift(sent_Gift, sent_Gift_Player);		//*#1��s�e�X§����
		statisticsLGift(combination1, combination2);		//*#7�έp�i�ƨñN���w�����G�v���ǤJ�{�����p�p��(for�v��)
		initBaseImfoElse(artistBelongTo, used_Action, used_Action_Player, handsCard, sd7statistics, ld7statistics);	//*#2 ��l��

		int choice = 0;
		double a = 0;
		double b = 0;
		for(int i = 0; i < combination1.size(); i++){
			a += assumeWorth[ combination1.get(i).getCardKinds() ];
			b += assumeWorth[ combination2.get(i).getCardKinds() ];
		}
		System.out.print("�զX1���� : " + a + " / �զX2���� : " + b +"\n");
		if(a == b){
			System.out.print("\n�N�M�w�O�A�F�A���q���\n");
			SecureRandom random = new SecureRandom();
			choice = random.nextInt(2);
		}
		else if(a > b)	choice = 1;
		else		choice = 2;

		return choice;
	}

	//#7�έp�i�ƨñN���w�����G�v���ǤJ�{�����p�p��(for�v��)���Ȩ� #6 reactLoathGift�I�s��
	public void statisticsLGift(ArrayList<GiftHands> choice1, ArrayList<GiftHands> choice2){
		int checkKind = 0;
		boolean secondCheck = false;

		//��������§�������ƳB
		for(int i = 0; i < choice1.size(); i++){
			for(int j = 0; j < choice2.size(); j++){
				checkKind = choice1.get(i).getCardKinds();
				if(checkKind ==  choice2.get(j).getCardKinds() ){	//�Y���ۦP§��
					sentGift[ checkKind ]++;
					sentGiftPlayer[ checkKind ]++;	//���覹��§���U�[�@
					choice1.remove(i);
					choice2.remove(j);			//�q���§�������o�ӬۦP§��
					if(i == 0) secondCheck = true;		//�Ѿl�U�@��§���O�_�ۦP���ˬd�Ұ�
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

	//�L�X�M�\printSet�|�L�X�@�ǰ�¦��T�}�C
	public void printSet(){
		System.out.print("\n��P : ");
		for(int i = 0; i < hands.size(); i++) System.out.print(hands.get(i).getCardName()+" / ");
		System.out.print("\n�� �� �� �� �� �� ��");
		System.out.print("\n"+Arrays.toString(hands7statistics)+"\t<-�q����P");

		if(usedAction[0]){
			System.out.print("\n"+Arrays.toString(secretDeal7statistics)+"\t<-[�K��]");
			System.out.print("\n"+Arrays.toString(trueSentGift)+"\t<-[�K��]��u��e�X����§���έp");
		}
		else System.out.print("\n"+Arrays.toString(sentGift)+"\t<-�q������§���q");

		if(usedAction[1]){
			System.out.print("\n"+Arrays.toString(lostDeal7statistics)+"\t<-[����]");
			System.out.print("\n"+Arrays.toString(existGift7statistics)+"\t<-[����]��u��§���έp");
		}
		else System.out.print("\n"+Arrays.toString(NumberOfGifts)+"\t<-�`§���q");
		System.out.print("\n"+Arrays.toString(sentGiftPlayer)+"\t<-���a§���q");

		System.out.print("\n\n"+Arrays.toString(hiddenGift7statistics)+"\t<-������§���P");
		System.out.print("\n"+Arrays.toString(hands7statistics)+"\t<-�q����P\n");
		for(int i = 0; i < trueZero.length; i++)
			if(i == 0)   System.out.print(  "[" + ((trueZero[i])? "T" : "f"));
			else System.out.print( ", " + ((trueZero[i])? "T" : "f") );
		System.out.print("]\t<-�u�E�s�ˬd\n");
		for(int i = 0; i < zero.length; i++)
			if(i == 0)   System.out.print(  "[" + ((trueZero[i])? "T" : "f"));
			else System.out.print( ", " + ((zero[i])? "T" : "f") );
		System.out.print("]\t<-�s�ˬd\n");
		for(int i = 0; i < fill.length; i++)
			if(i == 0)   System.out.print(  "[" + ((trueZero[i])? "T" : "f"));
			else System.out.print( ", " + ((fill[i])? "T" : "f") );
		System.out.print("]\t<-���ˬd\n");
		double[] abc = Arrays.copyOf(assumeWorth, assumeWorth.length);
		for(int i = 0; i<abc.length; i++) abc[i] -= 1.0;
		System.out.print("\n"+Arrays.toString(abc)+"\t<-�ܶv����");
		System.out.print("\n"+Arrays.toString(trueCP)+"\t<-�u��cp��");
		System.out.print("\n"+Arrays.toString(markSecretDeal)+"\t<-�K���аO");
		System.out.print("\n"+Arrays.toString(markLostDeal)+"\t<-���˼аO");
		System.out.print("\n"+Arrays.toString(markTrash)+"\t<-�U���аO");
		System.out.print("\n"+Arrays.toString(markKindGift)+"\t<-�ػP�аO");
		System.out.print("\n"+Arrays.toString(markLoathGift)+"\t<-�v���аO");
		System.out.print("\n");
	}

	//�έp��P�U��§���i��
	public void countHands7statistics(){
		Arrays.fill(hands7statistics, 0);
		for(int i = 0; i < hands.size(); i++) hands7statistics[ hands.get(i).getCardKinds() ]++;
	}
	//�Y�ϥιL�K�� �}�CtrueSentGift�|�O���u�ꪺ�w�e�X§���i�Ƥ���
	public void changeTrueSentGift(int[] atsg7s){
		System.arraycopy(sentGift, 0, atsg7s, 0, 7);	//1015�s�WTODO : �o�˰��O���n�� ���ϥαK����U�I�s�@��
		if(usedAction[0]) for(int i = 0; i < atsg7s.length; i++) atsg7s[i] += secretDeal7statistics[i];

		/*TODO : ����ɦV 1013�s�W��TODO
		*�y�N�{���X�ܱo�󪫥��
		*�Ĥ@�椣��
		*atsg7s[secretDealCard.getCardKinds]++;
		*/
	}
	//�Y�v��ϥιL���� �}�CexistGift7statistics�|�O�������v����˫�Ѿl��§���P�i�� �P ���ܸӹ������̪��L�b���o�i��
	public void changeExistGift7statistics(int[] eg7s){
		System.arraycopy(NumberOfGifts, 0, eg7s, 0, 7);	//1015�s�WTODO : �o�˰��O���n�� ���ϥΨ��˷�U�I�s�@��
		if(usedAction[1]) for(int i = 0; i < eg7s.length; i++){
			eg7s[i] -= lostDeal7statistics[i];
			getSecure[i] = eg7s[i]/2 + 1;
		}
		/*TODO : ����ɦV 1013�s�W��TODO
		*�y�N�{���X�ܱo�󪫥��
		*�{���X�i�אּ�U�C���***java�o�̥i�H�ݩ����ۤv���Q�k�O���O�T�����
		*eg7s[lostDealCard1.getCardKinds]--;
		*eg7s[lostDealCard2.getCardKinds]--;
		*getSecure.....���I�Q ���
		*/
	}
	//�Y���H�X�P�Τv��K�����ˡA�|���ܥ������P�q
	public void changeHiddenGift7statistics(int[] hg7s){
		System.arraycopy(NumberOfGifts, 0, hg7s, 0, 7);		//1015�s�WTODO : �o�˰��O���n�� ���C�ӻݭn���ܪ���U�T�{
		for(int i = 0; i < hg7s.length; i++) hg7s[i] -= (sentGift[i] + sentGiftPlayer[i] + secretDeal7statistics[i] + lostDeal7statistics[i]);
	}

	/*�s�ˬdcheckZero����zero�}�C�������� �Y�ϥιL�K�����˫h�|�@�P����TrueZero��������
	*zero�u��¬ݳ��W�P�q ���Ҽ{�ڤ�K���Ψ��˪����p�U ���i�P�w�Q�@��"�T�w�֦�" ���h����
	*trueZero�h�Ҽ{�ڤ�K�����˪����p�U �T�߭��i�P�w�Q�@��"�T�w�֦�" ���h����
	*�{��§���� > 1/2�`§���� �Y"�T�w�֦�" (����) 2*�{��§���� > �`§���� �Y"�T�w�֦�"*/
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
	//���ˬdcheckFill�ˬd�ڤ�{�s����P�̭����S�����P���W���w�e�X§��(�t�ڤ�K��)�ۥ[�ᬰ�u��{�s�i��
	public void checkFill(boolean[] f){
		for(int i = 0; i < f.length; i++){
			boolean bo1 = hands7statistics[i] > 0;
			//1015����boolean bo2 = sentGiftPlayer[i] + trueSentGift[i] + hands7statistics[i] == existGift7statistics[i];
			boolean bo2 = hands7statistics[i] == hiddenGift7statistics[i];
			f[i] = (bo1 && bo2)? true : false ;
		}
	}

	//��s�C��§�����ܶv����
	public void updateAssumeWorth(){
		System.out.print("\n\nold assumeWoth : \t\t"+Arrays.toString(assumeWorth)+"\n");

		for(int i = 0; i < 7; i++){
			if(assumeWorth[i] == 0) continue;	//�����k�s�O�@�ӳ槽���L�k�٭쪺�P�_ �w�O0�N�����P�_�U�@��
			if(trueZero[i]){
				assumeWorth[i] = 0;
			}
			else{				//TODO�M�ᦳ�ǯS�O���[�� �٨S�g
				assumeWorth[i] = (double)(worthOfArtist[i]) / ((double)((double)(existGift7statistics[i]) + 1.0) / 2.0);
				assumeWorth[i] *= distanceExtra[ sentDistance[i]+2 ];	//���P�Կ��[��
				if(fill[i]) assumeWorth[i] *= fillExtra[i];			//���ˬd�[��
				//assumeWorth[i] *= distanceExtra[ sentDistance[i]+2 ];	//��P�x���[��(�٨S�g �Ҽ{�⺡�ˬd�ֶi��)
				assumeWorth[i] = (Math.round(assumeWorth[i]*100.0))/100.0; //���p���I���G��
			}
		}

		System.out.print("new assumeWoth : \t\t"+Arrays.toString(assumeWorth)+"\n");
  	}
	//��s�C��§�����u��CP��
	public void updateTrueCP(){
		System.out.print("\nold trueCP : \t\t\t"+Arrays.toString(trueCP)+"\n");

		for(int i = 0; i < 7; i++){
			if(trueCP[i] == 0) continue;		//�����k�s�O�@�ӳ槽���L�k�٭쪺�P�_ �w�O0�N�����P�_�U�@��
			//�U�@��O�W�[�Ĥ@�����᪺���� Adventure�i��O0or 0.5�o��
			//if(db.playerSentGift[i]+db.playerAdventure >= getSecure[i] || db.aiSentGift[i]+db.aiAdventure >= getSecure[i]) trueCP[i] = 0;
			if(trueZero[i]) trueCP[i] = 0;
			else trueCP[i] = (double)(worthOfArtist[i]) / (double)(existGift7statistics[i]);
			trueCP[i] = (Math.round(trueCP[i] *100.0))/100.0;
			cp[i] = trueCP[i];			//����cp�PtrueCP�ۦP �n��
		}

		System.out.print("new trueCP : \t\t\t"+Arrays.toString(trueCP)+"\n");
	}
	//��P����h�O��P�έp ����7 ������
	public void handsCheck(int[] h, int[] mSD, int[] mLD, int[] mT, int[] mKG, int[] mLG){
		sentDistance = arrayEleDif(sentGift, sentGiftPlayer);
		for(int i = 0; i < h.length; i++){
			mSD[i] = mLD[i] = mT[i] = mKG[i] = mLG[i] = 0;
			if(zero[i]) mT[i] = 1;
			else if(trueZero[i]) mT[i] = 3;
			if(h[i] == 0) continue;	//�S�����P�N���Q��
			//if(h[i] == 0 || mT[i] < 5) continue;	//�S�����P�ΥL�O�U���N���Q��
			//��F opponent�b���a�����Y"���"���N �Gopponent�������N�O�q������
			if(!usedAction[0]){
				//�����������A�ȳѤ@�i�b��
				if(h[i] == 1 && fill[i] && sentDistance[i] == 0 ){
					if(artistStatus[i].equals("opponent")) 	mSD[i] = (usedAction[1])? 4 : 6 ;
					else				mSD[i] = 5;	//�o��t�@�i�K���O�H���߳��]���ۦP��
				}
				//���O�H�W���p ���t�@�i�N��í���o��
				else if(sentGift[i] + 1 == getSecure[i]) mSD[i] = 3;
				//1021���� �Ӥ� TODO : �����O�_�W������J��²�S�N�q�ۦPelse if(!trueZero[i] && 2*(sentGift[i]+1) > existGift7statistics[i]) mSD[i] = 3;
			}
			if(!usedAction[1]){
				//�ᱼ�N����(1�i)
				if(h[i] == 1 && fill[i] && sentGift[i] >= sentGiftPlayer[i]){
					if(artistStatus[i].equals("opponent")) 	mLD[i] = (6-sentDistance[i]);	//�Y�O��⪺ �̵P�q�t�Ӱt�� �̰�����6��
					else				mLD[i] = (5-sentDistance[i]);	//�Y���O��⪺ �̵P�q�t�Ӱt�� �̰�����5��
				}
				//�ᱼ�N����(2�i)
				else if(h[i] == 2 && fill[i] && sentGift[i] >= sentGiftPlayer[i]){
					if(artistStatus[i].equals("opponent")) 	mLD[i] = (4-sentDistance[i]);	//(��i�����p)�Y�O��⪺ �̵P�q�t�Ӱt�� �̰�����4��
					else				mLD[i] = (3-sentDistance[i]);	//(��i�����p)�Y���O��⪺ �̵P�q�t�Ӱt�� �̰�����3��
				}
				//��@�i(�f�t
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

	//���]ai�]�m
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
		if(!usedAction[3]){		//TODO  ��
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
