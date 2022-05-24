//主程式Hanamikoji 2018/07/08 U10516025徐椽鈞

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;

public class Hanamikoji extends Application{
	Stage currentStage;
	Stage playerVsAiStage, doublePlayerStage, ruleStage, invectorStage;
	Scene playerVsAiScene, doublePlayerScene, ruleScene, invectorScene;
	Button[] backMenuButton = new Button[2];
	Button playerVsAiButton = new Button("1 Player");
	Button doublePlayerButton = new Button("2 Players");
	Button ruleButton = new Button("Rules");
	Button invectorButton = new Button("invector");

	PlayerFightAi playerFightAi = new PlayerFightAi();
	DoublePlayer doublePlayerClass = new DoublePlayer();

	public Hanamikoji(){
		for(int i = 0; i < backMenuButton.length; i++){
			backMenuButton[i] = new Button("回主選單");
			backMenuButton[i].setMinWidth(80);
			backMenuButton[i].setMinHeight(80);
			backMenuButton[i].setMaxWidth(80);
			backMenuButton[i].setMaxHeight(80);
		}

		playerVsAiButton.setMinWidth(300);
		playerVsAiButton.setMinHeight(50);
		playerVsAiScene = new Scene(new Button("打電腦"));
		playerVsAiStage = new Stage();
		playerVsAiStage.setScene(playerVsAiScene);

		doublePlayerButton.setMinWidth(300);
		doublePlayerButton.setMinHeight(50);
		doublePlayerScene = new Scene(new Button("雙人局"));
		doublePlayerStage = new Stage();
		doublePlayerStage.setScene(doublePlayerScene);

		ruleButton.setMinWidth(300);
		ruleButton.setMinHeight(50);
		ruleScene = new Scene(new VBox(backMenuButton[0]), 900, 650);
		ruleStage = new Stage();
		ruleStage.setScene(ruleScene);

		invectorButton.setMinWidth(300);
		invectorButton.setMinHeight(50);
		invectorScene = new Scene(backMenuButton[1], 900, 650);
		invectorStage = new Stage();
		invectorStage.setScene(invectorScene);
	};

	public void start(Stage menu){
		VBox menuVbox = new VBox(playerVsAiButton, doublePlayerButton, ruleButton, invectorButton);
		menuVbox.setSpacing(20);
		menuVbox.setAlignment(Pos.CENTER);
		Scene menuScene = new Scene(menuVbox, 900, 650);
		menu.setScene(menuScene);
		menu.show();
		//[單人遊戲]按鈕
		playerVsAiButton.setOnAction(e->{
			playerFightAi.play(menu, playerVsAiStage);
			menu.hide();
		});
		playerVsAiButton.setOnKeyPressed(e->{
			if(e.getCode() == KeyCode.ENTER){
				playerFightAi.play(menu, playerVsAiStage);
				menu.hide();
			}
		});
		//[雙人遊戲]按鈕
		doublePlayerButton.setOnAction(e->{
			doublePlayerClass.play(menu, doublePlayerStage);
			menu.hide();
		});
		doublePlayerButton.setOnKeyPressed(e->{
			if(e.getCode() == KeyCode.ENTER){
				doublePlayerClass.play(menu, doublePlayerStage);
				menu.hide();
			}
		});
		//[規則說明]按鈕
		ruleButton.setOnAction(e->{
			menu.hide();
			ruleStage.show();
			currentStage = ruleStage;
		});
		ruleButton.setOnKeyPressed(e->{
			if(e.getCode() == KeyCode.ENTER){
				menu.hide();
				ruleStage.show();
				currentStage = ruleStage;
			}
		});
		//[開發者]按鈕
		invectorButton.setOnAction(e->{
			menu.hide();
			invectorStage.show();
			currentStage = invectorStage;
		});
		invectorButton.setOnKeyPressed(e->{
			if(e.getCode() == KeyCode.ENTER){
				menu.hide();
				invectorStage.show();
				currentStage = invectorStage;
			}
		});
		//[回主選單]按鈕
		for(int i = 0; i < backMenuButton.length; i++){
			final int ii = i;
			backMenuButton[ii].setOnAction(e->{
				menu.show();
				currentStage.close();
			});
			backMenuButton[ii].setOnKeyPressed(e->{
				if(e.getCode() == KeyCode.ENTER){
					menu.show();
					currentStage.close();
				}
			});
		}
	}
}