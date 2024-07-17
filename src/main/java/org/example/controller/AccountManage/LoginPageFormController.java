package org.example.controller.AccountManage;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import lombok.Setter;
import org.example.bo.BoFactory;
import org.example.bo.custom.DataValidationBo;
import org.example.bo.custom.UserBo;
import org.example.controller.Admin.AdminDashboardFormController;
import org.example.controller.User.UserDashboardFormController;
import org.example.dto.User;
import org.example.util.BoType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;

public class LoginPageFormController {
    @Setter
    private static Stage primaryStage;
    public JFXTextField txtEmailAddress;
    public JFXPasswordField txtPasssword;
    public JFXToggleButton btnUserType;
    private final UserBo userBo = BoFactory.getInstance().getBo(BoType.USER);
    private final DataValidationBo dataValidationBo = BoFactory.getInstance().getBo(BoType.VALIDATE);
    private final Logger logger = LoggerFactory.getLogger(LoginPageFormController.class);
    private Boolean type = false;

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

    public void btnForgetpwOnAction(ActionEvent actionEvent) {
        if (!txtEmailAddress.getText().isEmpty()) {
            if (userBo.isUser(txtEmailAddress.getText())) {
                ResetPasswordFormController.setCurrentEmail(txtEmailAddress.getText());
                ResetPasswordFormController.setPrimaryStage(loadScreen("view/resetPasswordForm.fxml"));
            } else {
                new Alert(Alert.AlertType.ERROR, "❌ This Email is Not Registered !").show();
            }
        }
    }

    public void btnLogInOnAction(ActionEvent actionEvent) {
        boolean allFieldsNotEmpty = dataValidationBo.isAllFieldsNotEmpty(
                txtEmailAddress.getText(), txtPasssword.getText()
        );
        boolean isValidPassword = dataValidationBo.isValidPassword(txtPasssword.getText());
        boolean isValidEmail = dataValidationBo.isValidEmail(txtEmailAddress.getText());

        if (allFieldsNotEmpty && isValidPassword && isValidEmail) {
            User user = new User(txtEmailAddress.getText(), txtPasssword.getText(), type);
            boolean result = userBo.loginRequest(user);

            if (result && Boolean.TRUE.equals(type)) {
                AdminDashboardFormController.setAdminUserEmail(txtEmailAddress.getText());
                AdminDashboardFormController.setPrimaryStage(loadScreen("view/adminDashboard.fxml"));
            } else if (result) {
                UserDashboardFormController.setEmployeeUserEmail(txtEmailAddress.getText());
                UserDashboardFormController.setPrimaryStage(loadScreen("view/userDashboardForm.fxml"));
            } else {
                new Alert(Alert.AlertType.ERROR, "Login Failed !").show();
            }
        } else {
            new Alert(Alert.AlertType.ERROR, "❌ Login Failed!\n Please Select correct data!").show();
        }
    }

    public void btnCreateAccountOnAction(ActionEvent actionEvent) {
        CreateAccountFormController.setPrimaryStage(loadScreen("view/createAccountForm.fxml"));
    }

    public void btnToogleUserType(ActionEvent actionEvent) {
        type = !type;
        btnUserType.setText(Boolean.TRUE.equals(type) ? "Admin" : "Employee");
    }
}
