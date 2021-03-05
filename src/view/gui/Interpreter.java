package view.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import controller.SelectController;

public class Interpreter extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("fxml/listOfPrograms.fxml"));
        Parent root = loader.load();
        SelectController cntr = loader.getController();
        cntr.setStage(primaryStage);
        primaryStage.setTitle("Toy Language");
        primaryStage.setScene(new Scene(root, 900, 700));
        primaryStage.show();
    }

    public void run(String[] args) {
        launch(args);
    }
}
