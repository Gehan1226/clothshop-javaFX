package org.example.controller.Admin;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import lombok.Setter;
import org.example.bo.BoFactory;
import org.example.bo.custom.UserBo;
import org.example.controller.AccountManage.ChangePasswordFormController;
import org.example.controller.AccountManage.HomePageFormController;
import org.example.util.BoType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class AdminProfilePageFormcontroller implements Initializable {
    @Setter
    private static Stage primaryStage;
    @Setter
    private static String adminEmail;
    public JFXTextField txtEmail;
    public JFXButton btnSaveChanges;
    private final Logger logger = LoggerFactory.getLogger(AdminProfilePageFormcontroller.class);
    private final UserBo userBo = BoFactory.getInstance().getBo(BoType.USER);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        txtEmail.setText(adminEmail);
    }

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

    public void btnMainmenuOnAction(ActionEvent actionEvent) {
        HomePageFormController.setPrimaryStage(loadScreen("view/home_page_from.fxml"));
    }

    public void txtEmailOnKeyReleased(KeyEvent keyEvent) {
        btnSaveChanges.setDisable(false);
    }

    public void btnSaveChangesOnAction(ActionEvent actionEvent) {
        String result = userBo.updateEmail(adminEmail, txtEmail.getText());
        new Alert(Alert.AlertType.INFORMATION, result).show();
        if (result.endsWith("Successfully !")) {
            AdminDashboardFormController.setAdminUserEmail(txtEmail.getText());
        }
    }

    public void btnChangePasswordOnAction(ActionEvent actionEvent) {
        ChangePasswordFormController.setAdmin(true);
        ChangePasswordFormController.setAdminUserEmail(txtEmail.getText());
        ChangePasswordFormController.setPrimaryStage(loadScreen("view/changePasswordForm.fxml"));
    }

    public void btnDashboardOnAction(ActionEvent actionEvent) {
        AdminDashboardFormController.setPrimaryStage(loadScreen("view/adminDashboard.fxml"));
    }

}
