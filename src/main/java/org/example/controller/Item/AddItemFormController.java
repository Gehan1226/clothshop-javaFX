package org.example.controller.Item;

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
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.Setter;
import org.example.bo.BoFactory;
import org.example.bo.custom.DataValidationBo;
import org.example.bo.custom.ItemBo;
import org.example.bo.custom.SupplierBo;
import org.example.controller.Admin.AdminDashboardFormController;
import org.example.controller.AccountManage.HomePageFormController;
import org.example.controller.Supplier.AddSupplieerFormcontroller;
import org.example.controller.User.UserDashboardFormController;
import org.example.dto.Item;
import org.example.util.BoType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AddItemFormController implements Initializable {
    @Setter
    private static Stage primaryStage;
    @Setter
    private static boolean isAdmin;
    public JFXTextField txtItemName;
    public JFXTextField txtQTY;
    public JFXTextField txtPrice;
    public JFXComboBox cmbSize;
    public JFXComboBox cmbSupplierID;
    public Text txtItemID;
    public JFXButton btnSave;
    public TableView<String[]> tblSuppliers = new TableView<>();
    public TableColumn<String[], String> colSupplierID;
    public TableColumn<String[], String> colSupplierName;
    public JFXComboBox cmbCategorie;
    private final ItemBo itemBo = BoFactory.getInstance().getBo(BoType.ITEM);
    private final DataValidationBo dataValidationBo = BoFactory.getInstance().getBo(BoType.VALIDATE);
    private final SupplierBo supplierBo = BoFactory.getInstance().getBo(BoType.SUPPLIER);
    private final Logger logger = LoggerFactory.getLogger(AddItemFormController.class);
    private final List<String> suplierIDS = new ArrayList<>();
    public Text txtCurrentDate;
    private List<String> allIDSAndNames;
    private String imagePath ;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colSupplierID.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[0]));
        colSupplierName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[1]));

        txtItemID.setText(itemBo.genarateItemID());

        String[] sizesArr = {"XSMALL", "SMALL", "MEDIUM", "LARGE", "X LARGE", "2X LARGE", "3X LARGE", "4X LARGE"};
        cmbSize.getItems().addAll(sizesArr);

        allIDSAndNames = supplierBo.getAllIDSAndNames();
        cmbSupplierID.getItems().addAll(allIDSAndNames);

        String[] categoriesArr = {"Ladies", "Gents", "Kids"};
        cmbCategorie.getItems().addAll(categoriesArr);

        addListenerTxtPrice();
        addListenerTxtQTY();

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

    private void addListenerTxtPrice() {
        txtPrice.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*([.]\\d*)?")) {
                txtPrice.setText(oldValue);
            }
        });
    }

    private void addListenerTxtQTY() {
        txtQTY.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                txtQTY.setText(oldValue);
            }
        });
    }

    public void btnSaveOnAction(ActionEvent actionEvent) {
        boolean isAllFieldsNotEmpty = dataValidationBo.isAllFieldsNotEmpty(
                txtItemName.getText(),
                txtPrice.getText(),
                txtQTY.getText(),
                imagePath
        );
        if (isAllFieldsNotEmpty && !cmbSize.getSelectionModel().isEmpty()
                && !cmbCategorie.getSelectionModel().isEmpty()) {
            Item item = new Item(
                    txtItemID.getText(),
                    txtItemName.getText(),
                    cmbSize.getSelectionModel().getSelectedItem().toString(),
                    Double.parseDouble(txtPrice.getText()),
                    Integer.parseInt(txtQTY.getText()),
                    cmbCategorie.getSelectionModel().getSelectedItem().toString(),
                    imagePath
            );

            if (itemBo.saveItem(item, suplierIDS)) {
                refreshPage();
                txtItemID.setText(itemBo.genarateItemID());
                new Alert(Alert.AlertType.INFORMATION, "✅ Item Saved!").show();
                return;
            }
            new Alert(Alert.AlertType.ERROR, "❌ Item Save Failed!").show();
        }
    }

    private void refreshPage() {
        txtItemName.setText("");
        txtPrice.setText("");
        txtQTY.setText("");
        cmbCategorie.getSelectionModel().clearSelection();
        cmbSupplierID.getSelectionModel().clearSelection();
        cmbSupplierID.getItems().clear();
        cmbSize.getSelectionModel().clearSelection();
        imagePath = null;
        suplierIDS.clear();
        tblSuppliers.getItems().clear();
        cmbSupplierID.getItems().addAll(allIDSAndNames);
    }

    public void btnAddOnAction(ActionEvent actionEvent) {
        if (cmbSupplierID.getValue() != null) {
            Object selectedItem = cmbSupplierID.getSelectionModel().getSelectedItem();
            String cmbValue = selectedItem.toString();
            String[] arr = cmbValue.split(" - ");
            tblSuppliers.getItems().add(arr);
            cmbSupplierID.getItems().remove(selectedItem);
            cmbSupplierID.getSelectionModel().clearSelection();
            suplierIDS.add(arr[0]);
        }
    }

    public void btnClearOnAction(ActionEvent actionEvent) {
        tblSuppliers.getItems().clear();
        cmbSupplierID.getItems().clear();
        cmbSupplierID.getItems().addAll(allIDSAndNames);
        suplierIDS.clear();
    }

    public void btnAddImageOnAction(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "Image Files", "*.png", "*.jpg", "*.jpeg"
        );
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showOpenDialog(primaryStage);
        if (file != null) {
            imagePath = file.toURI().toString();
        }
    }

    public void btnMainmenuOnAction(ActionEvent actionEvent) {
        HomePageFormController.setPrimaryStage(loadScreen("view/home_page_from.fxml"));
    }

    public void btnDashboardOnAction(ActionEvent actionEvent) {
        String path = isAdmin ? "view/adminDashboard.fxml" : "view/userDashboardForm.fxml";
        if (isAdmin) {
            AdminDashboardFormController.setPrimaryStage(loadScreen(path));
            return;
        }
        UserDashboardFormController.setPrimaryStage(loadScreen(path));
    }

    public void btnUpdateRemoveItemOnAction(ActionEvent actionEvent) {
        UpdateRemoveItemFormController.setAdmin(isAdmin);
        UpdateRemoveItemFormController.setPrimaryStage(loadScreen("view/updateItemForm.fxml"));
    }

    public void btnInventoryReportOnAction(ActionEvent actionEvent) {
        ItemBo itemBo = BoFactory.getInstance().getBo(BoType.ITEM);
        if (!itemBo.genarateInventoryReport()){
            new Alert(Alert.AlertType.ERROR,"System Error in InventoryReport!").show();
        }
    }

    public void btnAddSupplierOnAction(ActionEvent actionEvent) {
        AddSupplieerFormcontroller.setAdmin(isAdmin);
        AddSupplieerFormcontroller.setPrimaryStage(loadScreen("view/addSupplierForm.fxml"));
    }

    public void btnItemLisOnAction(ActionEvent actionEvent) {
        ItemListController.setAdmin(isAdmin);
        ItemListController.setPrimaryStage(loadScreen("view/itemListForm.fxml"));
    }
}