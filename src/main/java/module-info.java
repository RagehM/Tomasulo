module gui {
  requires javafx.controls;
  requires javafx.fxml;

  requires org.controlsfx.controls;
  opens instructions to javafx.base;

  exports gui;
  opens gui to javafx.fxml;
  exports gui.setupStage;
  opens gui.setupStage to javafx.fxml;
  opens units to javafx.base;
  opens units.stage.addressStage to javafx.base;
  opens units.stage.aluStage to javafx.base;
  opens units.stage to javafx.base;
}