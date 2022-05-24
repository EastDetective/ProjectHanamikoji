import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class GiftHands extends Button{
	double[] actionRecommend = new double[4];

	int stackOrder;	//在牌庫的順序(0~20)
	int giftOrder; 	//卡片的號碼(0~20)
	int cardKinds;	//卡片的種類是第幾種
	int kindsAmount;	//該種類卡片的價值(2~5)
	String cardName;	//該張卡片的名字(同類相同)
	ImageView currentView;

	int suitSecretDeal = 0;
	int suitLostDeal = 0;
	int suitKindGift = 0;
	int suitLoathGIft = 0;
	int isTrash = 0;

	int clickTimes = 0;
	int outOrder = 0;
	boolean picked = false;
	ImageView[] typeStyle = new ImageView[8];
	int[] worthArray = {5, 4, 3, 3, 2, 2, 2};
	String[] nameArray = {"櫻花髮簪", "茶具", "油紙傘", "三味線", "日式筱笛", "古書卷軸", "舞扇"};


	public GiftHands(){
	}
	public GiftHands(int stackOrderIs, int giftOrderIs){
		stackOrder = stackOrderIs;
		giftOrder = giftOrderIs;
		cardKinds = askKinds(giftOrder);
		kindsAmount = worthArray[cardKinds];
		cardName = nameArray[cardKinds];
		System.out.print(cardName.substring(0, 1)+ "/");
		for(int i =0; i < 8; i++){
			typeStyle[i] = new ImageView(new Image("testPic/gift"+cardKinds+"Type"+i+".png"));
		}
		currentView = typeStyle[0];
		this.setGraphic(currentView);
	}

	/*public void initSet(int stackOrderIs, int giftOrderIs){

	}*/

	public int askKinds(int order){
		int kind = 0;
		if(order < 5)	kind = 0;
		else if(order < 9)	kind = 1;
		else if(order < 12)	kind = 2;
		else if(order < 15)	kind = 3;
		else if(order < 17)	kind = 4;
		else if(order < 19)	kind = 5;
		else 		kind = 6;

		return kind;
	}
	public void transType(int order){
		setClickTimes(getClickTimes() + 1);
		setPicked(!getPicked());
		setOutOrder((getPicked())? order : 0);
		currentView = typeStyle[getOutOrder()];
		this.setGraphic(currentView);
	}
	public ImageView getOneOfTheTypeStyle(int index){
		return typeStyle[index];
	}

	public int getStackOrder(){
		return stackOrder;
	}
	public void setStackOrde(int order){
		stackOrder = order;
	}
	public int getGiftOrder(){
		return giftOrder;
	}
	public void setGiftOrde(int order){
		giftOrder = order;
	}
	public int getCardKinds(){
		return cardKinds;
	}
	public void setCardKinds(int kinds){
		cardKinds = kinds;
		setKindsAmount(worthArray[kinds]);
		setCardName(nameArray[kinds]);
	}
	public int getKindsAmount(){
		return kindsAmount;
	}
	public void setKindsAmount(int amount){
		kindsAmount = amount;
	}
	public String getCardName(){
		return cardName;
	}
	public void setCardName(String name){
		cardName = name;
	}
	public ImageView getCurrentView(){
		return currentView;
	}
	public void setCurrentView(ImageView view){
		currentView = view;
		this.setGraphic(currentView);
	}
	public ImageView[] getTypeStyle(){
		return typeStyle;
	}
	private void setOutOrder(ImageView[] style){
		typeStyle = style;
	}
	public int getClickTimes(){
		return clickTimes;
	}
	public void setClickTimes(int times){
		clickTimes = times;
	}
	public int getOutOrder(){
		return outOrder;
	}
	public void setOutOrder(int order){
		outOrder = order;
	}
	public boolean getPicked(){
		return picked;
	}
	public void setPicked(boolean state){
		picked = state;
	}

	public double getActionRecommend(int index){
		if(index >= 4){
			System.out.print("白癡白癡白癡白癡白癡白癡白癡");	
			return -1000.0;
		}
		return actionRecommend[index];
	}
	public void setActionRecommend(int index, double value){
		if(index >= 4){
		System.out.print("白癡白癡白癡白癡白癡白癡白癡");	
			return;
		}
		actionRecommend[index] = value;
	}
}