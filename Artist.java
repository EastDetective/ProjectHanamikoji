import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;

public class Artist extends Label{
	int outGifts = 0;		//�v��e�X�X�i§��
	int outGiftsOppo = 0;	//���e�X�X�i§��
	int artistOrder;		//���̧Ǹ�(�C�����ݱƦC�����X)
	String artistStatus;		//���̲{�b�Q�֫���
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