import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;

public class Artist extends Label{
	int outGifts = 0;		//己方送出幾張禮物
	int outGiftsOppo = 0;	//對方送出幾張禮物
	int artistOrder;		//藝者序號(遊戲中需排列之號碼)
	String artistStatus;		//藝者現在被誰持有
	ImageView artistView;

	public Artist(){
	}
	public Artist(int order, String status){
		artistOrder = order;
		artistStatus = status;
		artistView = new ImageView(new Image(("pic/artist"+order+status+".png")));
		this.setGraphic(artistView);
	}

	public int getOutGifts(){
		return outGifts;
	}
	public void setOutGifts(int volume){
		outGifts = volume;
	}
	public int getOutGiftsOppo(){
		return outGiftsOppo;
	}
	public void setOutGiftsOppo(int volume){
		outGiftsOppo = volume;
	}
	public int getArtistOrder(){
		return artistOrder;
	}
	public void setArtistOrder(int volume){
		artistOrder = volume;
	}
	public String getArtistStatus(){
		return artistStatus;
	}
	public void setArtistStatus(String state){
		artistStatus = state;
	}
	public ImageView getArtistView(){
		return artistView;
	}
	public void setArtistView(ImageView view){
		artistView = view;
	}

}