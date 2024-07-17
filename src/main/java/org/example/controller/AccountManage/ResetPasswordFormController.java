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
import org.example.util.BoType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ResetPasswordFormController implements Initializable {
    @Setter
    private static Stage primaryStage;
    @Setter
    private static String currentEmail;
    public JFXPasswordField txtConfirmPassword;
    public JFXTextField txtOTPCode;
    public JFXPasswordField txtPassword;
    public Text txtEmail;
    public JFXButton btnConfirmOtp;
    public JFXButton btnSendOtp;
    public JFXButton btnResetPassword;
    private final UserBo userBo = BoFactory.getInstance().getBo(BoType.USER);
    private final DataValidationBo dataValidationBo = BoFactory.getInstance().getBo(BoType.VALIDATE);
    private final Logger logger = LoggerFactory.getLogger(ResetPasswordFormController.class);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        txtEmail.setText(currentEmail);
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

    public void btnLoginOnAction(ActionEvent actionEvent) {
        LoginPageFormController.setPrimaryStage(loadScreen("view/loginPageForm.fxml"));
    }

    public void btnResetPasswordOnAction(ActionEvent actionEvent) {
        boolean allFieldsNotEmpty = dataValidationBo.isAllFieldsNotEmpty(
                txtConfirmPassword.getText(), txtPassword.getText()
        );
        boolean isSamePassword = txtPassword.getText().equals(txtConfirmPassword.getText());
        boolean isValidPassword = dataValidationBo.isValidPassword(txtPassword.getText());

        if (allFieldsNotEmpty && isSamePassword && isValidPassword) {
            new Alert(Alert.AlertType.INFORMATION, userBo.updatePassword(
                    txtEmail.getText(), txtPassword.getText())
            ).show();
            txtPassword.setText("");
            txtConfirmPassword.setText("");
        } else {
            new Alert(Alert.AlertType.ERROR, "Please Enter Same password !").show();
        }
    }

    public void btnConfirmOnAction(ActionEvent actionEvent) {
        if (userBo.isEqualsOTP(Integer.parseInt(txtOTPCode.getText()))) {
            btnConfirmOtp.setDisable(true);
            btnSendOtp.setDisable(true);
            txtOTPCode.setDisable(true);
            txtPassword.setDisable(false);
            txtConfirmPassword.setDisable(false);
            btnResetPassword.setDisable(false);
            new Alert(Alert.AlertType.INFORMATION, "✅ OTP Verified ! \n Now you can change the passsword.").show();
            return;
        }
        new Alert(Alert.AlertType.ERROR, "❌ OTP Verification Failed !").show();
    }

    private boolean sendOTP() {
        return userBo.sendOTPTo(currentEmail);
    }

    public void btnSendOTPOnAction(ActionEvent actionEvent) {
        if (sendOTP()) {
            new Alert(Alert.AlertType.INFORMATION, "✅ OTP Send Successfully ! Check your email address").show();
            return;
        }
        new Alert(Alert.AlertType.ERROR, "❌ OTP Send Failed ! Retry after countdown.").show();
    }
}
