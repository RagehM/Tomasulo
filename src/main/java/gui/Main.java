package gui;

import gui.setupStage.SetupStage;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

  public void start(Stage stage) {
    Stage FirstStage = SetupStage.setupMainStage();
    FirstStage.setTitle("Tomasulo");
    FirstStage.show();
  }

  public static void main(String[] args) {
    launch();
  }
}
