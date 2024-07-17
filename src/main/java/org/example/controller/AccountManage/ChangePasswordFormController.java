package org.example.controller.AccountManage;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lombok.Setter;
import org.example.bo.BoFactory;
import org.example.bo.custom.DataValidationBo;
import org.example.bo.custom.UserBo;
import org.example.controller.Admin.AdminDashboardFormController;
import org.example.controller.User.UserDashboardFormController;
import org.example.util.BoType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ChangePasswordFormController implements Initializable {
    @Setter
    private static Stage primaryStage;
    @Setter
    private static String employeeUserEmail;
    @Setter
    private static String adminUserEmail;
    @Setter
    private static boolean isAdmin;
    public JFXPasswordField txtConfirmPassword;
    public JFXTextField txtCurrentPassword;
    public JFXPasswordField txtPassword;
    public JFXButton btnConfirm;
    public JFXButton btnUpdatePassword;
    public Text txtEmail;
    private final UserBo userBo = BoFactory.getInstance().getBo(BoType.USER);
    private final DataValidationBo dataValidationBo = BoFactory.getInstance().getBo(BoType.VALIDATE);
    private final Logger logger = LoggerFactory.getLogger(ChangePasswordFormController.class);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (isAdmin){
            txtEmail.setText(adminUserEmail);
        }else {
            txtEmail.setText(employeeUserEmail);
        }
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

    public void btnConfirmOnAction(ActionEvent actionEvent) {
        boolean isValidPassword = dataValidationBo.isValidPassword(txtCurrentPassword.getText());
        boolean isConfirmed = userBo.confirmPassword(
                employeeUserEmail != null ? employeeUserEmail : adminUserEmail,
                txtCurrentPassword.getText()
        );
        if (isValidPassword) {
            if (isConfirmed) {
                txtPassword.setDisable(false);
                txtConfirmPassword.setDisable(false);
                btnConfirm.setDisable(true);
                btnUpdatePassword.setDisable(false);
                new Alert(Alert.AlertType.INFORMATION, "Password confirmed.Now you can update password.").show();
                return;
            }
            new Alert(Alert.AlertType.ERROR, "❌ Wrong Password!").show();
            return;
        }
        new Alert(Alert.AlertType.ERROR, "❌ Wrong password pattern!").show();
    }

    public void btnResetPasswordOnAction(ActionEvent actionEvent) {
        if (isAdmin){
            ResetPasswordFormController.setCurrentEmail(adminUserEmail);
        }else {
            ResetPasswordFormController.setCurrentEmail(employeeUserEmail);
        }
        ResetPasswordFormController.setPrimaryStage(loadScreen("view/resetPasswordForm.fxml"));
    }

    public void btndashboardOnAction(ActionEvent actionEvent) {
        if (employeeUserEmail != null) {
            UserDashboardFormController.setPrimaryStage(loadScreen("view/userDashboardForm.fxml"));
        } else {
            AdminDashboardFormController.setPrimaryStage(loadScreen("view/adminDashboard.fxml"));
        }
    }

    public void btnMainmenuOnAction(ActionEvent actionEvent) {
        HomePageFormController.setPrimaryStage(loadScreen("view/home_page_from.fxml"));
    }

    public void btnUpdatePasswordOnAction(ActionEvent actionEvent) {
        boolean allFieldsNotEmpty = dataValidationBo.isAllFieldsNotEmpty(
                txtPassword.getText(), txtConfirmPassword.getText());
        boolean validPassword = dataValidationBo.isValidPassword(txtPassword.getText());

        if (allFieldsNotEmpty && validPassword) {
            String result = userBo.updatePassword(
                    employeeUserEmail != null ? employeeUserEmail : adminUserEmail,
                    txtPassword.getText()
            );
            btnConfirm.setDisable(false);
            txtCurrentPassword.setText("");
            txtConfirmPassword.setText("");
            txtPassword.setText("");
            txtConfirmPassword.setDisable(true);
            txtPassword.setDisable(true);
            new Alert(Alert.AlertType.INFORMATION, result).show();
            return;
        }
        new Alert(Alert.AlertType.INFORMATION, "Enter correct password pattern !").show();
    }
}
