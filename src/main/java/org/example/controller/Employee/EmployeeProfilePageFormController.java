package org.example.controller.Employee;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.example.bo.BoFactory;
import org.example.bo.custom.EmployeeBo;
import org.example.controller.AccountManage.ChangePasswordFormController;
import org.example.controller.AccountManage.HomePageFormController;
import org.example.controller.User.UserDashboardFormController;
import org.example.dto.Employee;
import org.example.util.BoType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EmployeeProfilePageFormController implements Initializable {
    @Setter
    private static Stage primaryStage;
    @Setter
    private static String employeeUserEmail;
    public JFXTextField txtName;
    public JFXTextField txtNic;
    public JFXTextField txtMobileNo;
    public JFXTextField txtEmail;
    public JFXTextField txtProvince;
    public JFXTextField txtDistrict;
    public Text txtEmpId;
    private final EmployeeBo employeeBo = BoFactory.getInstance().getBo(BoType.EMPLOYEE);
    private final Logger logger = LoggerFactory.getLogger(EmployeeProfilePageFormController.class);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Employee employee = employeeBo.retrieveByEmail(employeeUserEmail);
        txtEmpId.setText(employee.getEmpID());
        txtName.setText(employee.getFirstName() + " " + employee.getLastName());
        txtEmail.setText(employee.getEmail());
        txtNic.setText(employee.getNic());
        txtProvince.setText(employee.getProvince());
        txtDistrict.setText(employee.getDistrict());
        txtMobileNo.setText(employee.getMobileNumber());
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

    public void btnChangePasswordOnAction(ActionEvent actionEvent) {
        ChangePasswordFormController.setAdmin(false);
        ChangePasswordFormController.setEmployeeUserEmail(employeeUserEmail);
        ChangePasswordFormController.setPrimaryStage(loadScreen("view/changePasswordForm.fxml"));
    }

    public void btnDashboardOnAction(ActionEvent actionEvent) {
        UserDashboardFormController.setPrimaryStage(loadScreen("view/userDashboardForm.fxml"));
    }
}
