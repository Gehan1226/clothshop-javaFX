package org.example.controller.Supplier;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lombok.Setter;
import org.example.bo.BoFactory;
import org.example.bo.custom.DataValidationBo;
import org.example.bo.custom.ItemBo;
import org.example.bo.custom.SupplierBo;
import org.example.controller.Admin.AdminDashboardFormController;
import org.example.controller.AccountManage.HomePageFormController;
import org.example.controller.Item.AddItemFormController;
import org.example.controller.Order.PlaceOrderFormController;
import org.example.controller.User.UserDashboardFormController;
import org.example.dto.Supplier;
import org.example.util.BoType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AddSupplieerFormcontroller implements Initializable {
    @Setter
    private static Stage primaryStage;
    @Setter
    private static boolean isAdmin;
    @Setter
    private String empID;
    public JFXTextField txtFirstName;
    public JFXTextField txtCompany;
    public Text txtEmailValidation;
    public Text txtMobileNumberValidation;
    public JFXTextField txtLastName;
    public JFXTextField txtEmail;
    public JFXTextField txtMobileNumber;
    public JFXButton btnResetPassword;
    public TableView<String[]> tblItems = new TableView<>();
    public TableColumn<String[], String> colItemId;
    public TableColumn<String[], String> colItemName;
    public Text txtSupplierID;
    public JFXComboBox cmbItem;
    public Text txtCurrentDate;
    private List<String> allIDSAndNames;
    private boolean isValidEmail;
    private boolean isValidMobileNo;
    private final List<String> itemIDS = new ArrayList<>();
    private final SupplierBo supplierBo = BoFactory.getInstance().getBo(BoType.SUPPLIER);
    private final ItemBo itemBo = BoFactory.getInstance().getBo(BoType.ITEM);
    private final DataValidationBo dataValidationBo = BoFactory.getInstance().getBo(BoType.VALIDATE);
    private final Logger logger = LoggerFactory.getLogger(AddSupplieerFormcontroller.class);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colItemId.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[0]));
        colItemName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[1]));

        txtSupplierID.setText(supplierBo.genarateSupplierID());

        allIDSAndNames = itemBo.getAllIDSAndNames();
        cmbItem.getItems().addAll(allIDSAndNames);

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

    public void txtEmailOnKeyReleased(KeyEvent keyEvent) {
        if (!dataValidationBo.isValidEmail(txtEmail.getText())) {
            txtEmailValidation.setVisible(true);
            isValidEmail = false;
            return;
        }
        txtEmailValidation.setVisible(false);
        isValidEmail = true;
    }

    public void btnAddItemOnAction(ActionEvent actionEvent) {
        if (cmbItem.getValue() != null) {
            Object selectedItem = cmbItem.getSelectionModel().getSelectedItem();
            String cmbValue = selectedItem.toString();
            String[] arr = cmbValue.split(" - ");
            tblItems.getItems().add(arr);

            cmbItem.getItems().remove(selectedItem);
            cmbItem.getSelectionModel().clearSelection();
            itemIDS.add(arr[0]);
        }
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

    public void btnSaveOnAction(ActionEvent actionEvent) {

        if (isAllFieldsNotEmpty() && isValidEmail && isValidMobileNo) {
            Supplier supplier = new Supplier(
                    txtSupplierID.getText(),
                    txtFirstName.getText(),
                    txtLastName.getText(),
                    txtCompany.getText(),
                    txtEmail.getText(),
                    txtMobileNumber.getText()
            );
            if (supplierBo.saveSupplier(supplier, itemIDS)) {
                txtSupplierID.setText(supplierBo.genarateSupplierID());
                txtFirstName.setText("");
                txtEmail.setText("");
                txtLastName.setText("");
                txtCompany.setText("");
                txtMobileNumber.setText("");
                cmbItem.getItems().removeAll();
                cmbItem.getItems().addAll(allIDSAndNames);
                tblItems.getItems().clear();

                new Alert(Alert.AlertType.INFORMATION, "✅ Item Saved !").show();
                return;
            }
            new Alert(Alert.AlertType.ERROR, "❌ Item Save Failed !").show();
            return;
        }
        new Alert(Alert.AlertType.INFORMATION, "Please fill every field!").show();
    }

    private boolean isAllFieldsNotEmpty() {
        return dataValidationBo.isAllFieldsNotEmpty(
                txtFirstName.getText(),
                txtLastName.getText(),
                txtEmail.getText(),
                txtCompany.getText(),
                txtMobileNumber.getText()
        );
    }

    public void btnClearOnAction(ActionEvent actionEvent) {
        tblItems.getItems().clear();
        cmbItem.getItems().clear();
        cmbItem.getItems().addAll(allIDSAndNames);
        itemIDS.clear();
    }

    public void btnDashboardOnAction(ActionEvent actionEvent) {
        String path = isAdmin ? "view/adminDashboard.fxml" : "view/userDashboardForm.fxml";
        if (isAdmin) {
            AdminDashboardFormController.setPrimaryStage(loadScreen(path));
            return;
        }
        UserDashboardFormController.setPrimaryStage(loadScreen(path));
    }

    public void btnMainmenuOnAction(ActionEvent actionEvent) {
        HomePageFormController.setPrimaryStage(loadScreen("view/home_page_from.fxml"));
    }

    public void btnSupplierUpdateRemoveOnAction(ActionEvent actionEvent) {
        UpdateRemoveSupplierFormController.setAdmin(isAdmin);
        UpdateRemoveSupplierFormController.setPrimaryStage(loadScreen("view/updateSupplierForm.fxml"));
    }

    public void btnSupplierReportOnAction(ActionEvent actionEvent) {
        if (supplierBo.genarateSupplierReport()){
            new Alert(Alert.AlertType.ERROR,"System Error in SupplierReport!").show();
        }
    }

    public void btnAddProductOnAction(ActionEvent actionEvent) {
        AddItemFormController.setAdmin(isAdmin);
        AddItemFormController.setPrimaryStage(loadScreen("view/addItemForm.fxml"));
    }
}
