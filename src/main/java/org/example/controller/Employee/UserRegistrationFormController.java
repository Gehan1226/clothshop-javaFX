package org.example.controller.Employee;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lombok.Setter;
import org.example.bo.BoFactory;
import org.example.bo.custom.DataValidationBo;
import org.example.bo.custom.EmployeeBo;
import org.example.controller.Admin.AdminDashboardFormController;
import org.example.controller.AccountManage.HomePageFormController;
import org.example.controller.Item.AddItemFormController;
import org.example.controller.Supplier.AddSupplieerFormcontroller;
import org.example.dto.Employee;
import org.example.util.BoType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class UserRegistrationFormController implements Initializable {
    @Setter
    private static Stage primaryStage;
    public JFXTextField txtFirstName;
    public JFXTextField txtLastName;
    public JFXTextField txtNicNo;
    public JFXTextField txtMobileNumber;
    public JFXComboBox cmbProvince;
    public JFXComboBox cmbDistrict;
    public JFXTextField txtEmail;
    public JFXButton btnResetPassword;
    public Text txtEmailValidation;
    public Text txtMobileNumberValidation;
    public Text txtEmpID;
    private boolean isValidEmail;
    private boolean isValidMobileNo;
    private final DataValidationBo dataValidationBo = BoFactory.getInstance().getBo(BoType.VALIDATE);
    private final EmployeeBo employeeBo = BoFactory.getInstance().getBo(BoType.EMPLOYEE);
    private final Logger logger = LoggerFactory.getLogger(UserRegistrationFormController.class);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        txtEmpID.setText(employeeBo.genarateEmployeeID());
        String[] provinceArr = {"Central", "Eastern", "North Central",
                "Northern", "North West", "Sabaragamuwa", "Southern", "Uva", "Western"
        };
        String[] districtArr = {
                "Ampara", "Anuradhapura", "Badulla", "Batticaloa", "Colombo",
                "Galle", "Gampaha", "Hambantota", "Jaffna", "Kalutara",
                "Kandy", "Kegalle", "Kilinochchi", "Kurunegala", "Mannar",
                "Matale", "Matara", "Monaragala", "Mullaitivu", "Nuwara Eliya",
                "Polonnaruwa", "Puttalam", "Ratnapura", "Trincomalee", "Vavuniya"
        };
        cmbProvince.getItems().addAll(provinceArr);
        cmbDistrict.getItems().addAll(districtArr);
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

    public void btnRegisterOnAction(ActionEvent actionEvent) {
        boolean allFieldsNotEmpty = dataValidationBo.isAllFieldsNotEmpty(
                txtFirstName.getText(),
                txtLastName.getText(),
                txtNicNo.getText()
        );
        boolean isSelectedcmbBoxes = !cmbProvince.getSelectionModel().isEmpty()
                && !cmbDistrict.getSelectionModel().isEmpty();
        if (allFieldsNotEmpty && isValidEmail && isValidMobileNo && isSelectedcmbBoxes) {
            Employee employee = new Employee(
                    txtEmpID.getText(),
                    txtFirstName.getText(),
                    txtLastName.getText(),
                    txtNicNo.getText(),
                    txtMobileNumber.getText(),
                    cmbProvince.getSelectionModel().getSelectedItem().toString(),
                    cmbDistrict.getSelectionModel().getSelectedItem().toString(),
                    txtEmail.getText()
            );
            if (employeeBo.save(employee)) {
                txtEmpID.setText(employeeBo.genarateEmployeeID());
                txtFirstName.setText("");
                txtLastName.setText("");
                txtMobileNumber.setText("");
                txtEmail.setText("");
                txtNicNo.setText("");
                new Alert(Alert.AlertType.INFORMATION, "✅ User Registration Successfully !").show();
                return;
            }
            new Alert(Alert.AlertType.ERROR, "❌ User Registration Failed!").show();
        }
    }

    public void txtEmailOnKeyReleased(KeyEvent keyEvent) {
        if (!dataValidationBo.isValidEmail(txtEmail.getText())) {
            txtEmailValidation.setVisible(true);
            isValidEmail = false;
            return;
        }
        txtEmailValidation.setVisible(false);
        isValidEmail = true;
    }

    public void txtMobileNoOnKeyReleased(KeyEvent keyEvent) {
        if (!dataValidationBo.isValidMobileNumber(txtMobileNumber.getText())) {
            txtMobileNumberValidation.setVisible(true);
            isValidMobileNo = false;
            return;
        }
        txtMobileNumberValidation.setVisible(false);
        isValidMobileNo = true;
    }

    public void btnDashboardOnAction(ActionEvent actionEvent) {
        AdminDashboardFormController.setPrimaryStage(loadScreen("view/adminDashboard.fxml"));
    }

    public void btnUpdateRemoveOnAction(ActionEvent actionEvent) {
        EmployeeUpdateRemoveFormController.setPrimaryStage(loadScreen("view/employeeUpdateRemovePageForm.fxml"));
    }

    public void btnAddSupplierOnAction(ActionEvent actionEvent) {
        AddSupplieerFormcontroller.setAdmin(true);
        AddSupplieerFormcontroller.setPrimaryStage(loadScreen("view/addSupplierForm.fxml"));
    }

    public void btnAddProductOnAction(ActionEvent actionEvent) {
        AddItemFormController.setAdmin(true);
        AddItemFormController.setPrimaryStage(loadScreen("view/addItemForm.fxml"));
    }

    public void btnEmployeeReportOnAction(ActionEvent actionEvent) {
        EmployeeBo emploeeBo = BoFactory.getInstance().getBo(BoType.EMPLOYEE);
        if (!emploeeBo.genarateEmployeeReport()){
            new Alert(Alert.AlertType.ERROR,"System Error in EmployeeReport!").show();
        }
    }
}
