package org.example.controller.AccountManage;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Setter;
import org.example.controller.AccountManage.CreateAccountFormController;
import org.example.controller.AccountManage.LoginPageFormController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;

public class HomePageFormController {
    @Setter
    private static Stage primaryStage;
    public JFXButton btnCreateaccount;
    public JFXButton btnLogin;
    private final Logger logger = LoggerFactory.getLogger(HomePageFormController.class);

    private Stage loadScreen(String path) {
        Stage stage = null;
        try {
            primaryStage.close();
            URL fxmlLocation = getClass().getClassLoader().getResource(path);
            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent parent = loader.load();
            stage = new Stage();
            stage.setScene(new Scene(parent));
            stage.show();
        } catch (IOException e) {
            logger.error("IOException occurred while loading FXML file: {}", path, e);
        } catch (Exception e) {
            logger.error("An unexpected error occurred", e);
        }
        return stage;
    }

    public void createButtonAction(ActionEvent actionEvent) {
        CreateAccountFormController.setPrimaryStage(loadScreen("view/createAccountForm.fxml"));
    }

    public void loginButtonOnAction(ActionEvent actionEvent) throws IOException {
        LoginPageFormController.setPrimaryStage(loadScreen("view/loginPageForm.fxml"));
    }

}
