package org.example.controller.Employee;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import org.example.bo.custom.UserBo;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class EmployeeUpdateRemoveFormController implements Initializable {
    @Setter
    private static Stage primaryStage;
    public JFXTextField txtFirstName;
    public JFXTextField txtEmail;
    public JFXTextField txtLastName;
    public JFXTextField txtNicNo;
    public JFXTextField txtMobileNumber;
    public JFXTextField txtEmpID;
    public JFXComboBox cmbProvince;
    public JFXComboBox cmbDistrict;
    public Text txtEmailValid;
    public Text txtmobileNumberValid;
    public JFXButton btnSave;
    public Text txtCurrentDate;
    private String empId;
    private final DataValidationBo dataValidationBo = BoFactory.getInstance().getBo(BoType.VALIDATE);
    private final EmployeeBo employeeBo = BoFactory.getInstance().getBo(BoType.EMPLOYEE);
    private final UserBo userBo = BoFactory.getInstance().getBo(BoType.USER);
    private final Logger logger = LoggerFactory.getLogger(EmployeeUpdateRemoveFormController.class);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String[] provinceArr = {"Central", "Eastern", "North Central",
                "Northern", "North West", "Sabaragamuwa", "Southern", "Uva", "Western"};
        String[] districtArr = {
                "Ampara", "Anuradhapura", "Badulla", "Batticaloa", "Colombo",
                "Galle", "Gampaha", "Hambantota", "Jaffna", "Kalutara",
                "Kandy", "Kegalle", "Kilinochchi", "Kurunegala", "Mannar",
                "Matale", "Matara", "Monaragala", "Mullaitivu", "Nuwara Eliya",
                "Polonnaruwa", "Puttalam", "Ratnapura", "Trincomalee", "Vavuniya"
        };
        cmbProvince.getItems().addAll(provinceArr);
        cmbDistrict.getItems().addAll(districtArr);

        txtCurrentDate.setText(LocalDate.now().toString());
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

    public void btnSaveOnAction(ActionEvent actionEvent) {
        boolean allFieldsNotEmpty = dataValidationBo.isAllFieldsNotEmpty(
                empId,
                txtFirstName.getText(),
                txtLastName.getText(),
                txtEmail.getText(),
                txtNicNo.getText(),
                txtMobileNumber.getText()
        );
        if (allFieldsNotEmpty) {
            Employee employee = new Employee(
                    empId,
                    txtFirstName.getText(),
                    txtLastName.getText(),
                    txtNicNo.getText(),
                    txtMobileNumber.getText(),
                    cmbProvince.getSelectionModel().getSelectedItem().toString(),
                    cmbDistrict.getSelectionModel().getSelectedItem().toString(),
                    txtEmail.getText()
            );
            if (employeeBo.replace(employee)) {
                txtEmpID.setText("");
                txtFirstName.setText("");
                txtLastName.setText("");
                txtEmail.setText("");
                txtNicNo.setText("");
                txtMobileNumber.setText("");
                cmbProvince.getSelectionModel().clearSelection();
                cmbDistrict.getSelectionModel().clearSelection();
                new Alert(Alert.AlertType.INFORMATION, "✅ Data Saved!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "❌ Data Save Failed!").show();
            }
            return;
        }
        new Alert(Alert.AlertType.ERROR, "❌ Enter valid data !").show();
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {
        Employee employee = new Employee(
                empId,
                txtFirstName.getText(),
                txtLastName.getText(),
                txtNicNo.getText(),
                txtMobileNumber.getText(),
                cmbProvince.getSelectionModel().getSelectedItem().toString(),
                cmbDistrict.getSelectionModel().getSelectedItem().toString(),
                txtEmail.getText()
        );
        boolean isDeletedEmployee = employeeBo.deleteEmployee(employee);
        boolean isDeletedUserAccount = userBo.deleteUserAccount(employee);
        if (isDeletedEmployee && isDeletedUserAccount) {
            txtEmpID.setText("");
            txtFirstName.setText("");
            txtLastName.setText("");
            txtEmail.setText("");
            txtNicNo.setText("");
            txtMobileNumber.setText("");
            cmbProvince.getSelectionModel().clearSelection();
            cmbDistrict.getSelectionModel().clearSelection();
            new Alert(Alert.AlertType.INFORMATION, "✅ Employee Deleted!").show();
        } else {
            new Alert(Alert.AlertType.ERROR, "❌ Employee Delete Failed!").show();
        }
    }

    public void btnDashboardOnAction(ActionEvent actionEvent) {
        AdminDashboardFormController.setPrimaryStage(loadScreen("view/adminDashboard.fxml"));
    }

    public void btnSearchOnAction(ActionEvent actionEvent) {
        if (dataValidationBo.isValidEmpID(txtEmpID.getText())) {
            Employee employee = employeeBo.retrieveById(txtEmpID.getText());

            if (employee != null) {
                txtFirstName.setText(employee.getFirstName());
                txtLastName.setText(employee.getLastName());
                txtEmail.setText(employee.getEmail());
                txtNicNo.setText(employee.getNic());
                txtMobileNumber.setText(employee.getMobileNumber());

                cmbProvince.getSelectionModel().select(employee.getProvince());
                cmbDistrict.getSelectionModel().select(employee.getDistrict());

                empId = employee.getEmpID();
                new Alert(Alert.AlertType.INFORMATION, "✅ Data Founded! Now you can update data.").show();
                return;
            }
            new Alert(Alert.AlertType.INFORMATION, "❌ Data Not Found!").show();
            return;
        }
        new Alert(Alert.AlertType.ERROR, "❌ Wrong ID").show();
    }

    public void txtEmailOnKeyReleased(KeyEvent keyEvent) {
        boolean isValidEmail = dataValidationBo.isValidEmail(txtEmail.getText());
        if (!isValidEmail) {
            txtEmailValid.setVisible(true);
            btnSave.setDisable(true);
            return;
        }
        btnSave.setDisable(false);
        txtEmailValid.setVisible(false);
    }

    public void txtMobileNumberOnKeyReleased(KeyEvent keyEvent) {
        boolean isValidMobileNumber = dataValidationBo.isValidMobileNumber(txtMobileNumber.getText());
        if (!isValidMobileNumber) {
            txtmobileNumberValid.setVisible(true);
            btnSave.setDisable(true);
            return;
        }
        btnSave.setDisable(false);
        txtmobileNumberValid.setVisible(false);
    }

    public void btnUserRegisterOnAction(ActionEvent actionEvent) {
        UserRegistrationFormController.setPrimaryStage(loadScreen("view/userRegistrationForm.fxml"));
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

