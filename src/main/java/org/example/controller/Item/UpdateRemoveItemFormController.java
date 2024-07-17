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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.Setter;
import org.example.bo.BoFactory;
import org.example.bo.custom.DataValidationBo;
import org.example.bo.custom.ItemBo;
import org.example.bo.custom.SupplierBo;
import org.example.controller.AccountManage.HomePageFormController;
import org.example.controller.Admin.AdminDashboardFormController;
import org.example.controller.Supplier.AddSupplieerFormcontroller;
import org.example.controller.Supplier.UpdateRemoveSupplierFormController;
import org.example.controller.User.UserDashboardFormController;
import org.example.dto.Item;
import org.example.dto.Supplier;
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

public class UpdateRemoveItemFormController implements Initializable {
    @Setter
    private static Stage primaryStage;
    @Setter
    private static boolean isAdmin;
    public JFXTextField txtItemName;
    public Text txtEmailValidation;
    public Text txtMobileNumberValidation;
    public ImageView imgCloth;
    public JFXTextField txtPrice;
    public JFXTextField txtQTY;
    public JFXComboBox cmbSize;
    public JFXComboBox cmbCategorie;
    public JFXComboBox cmbSupplierID;
    public JFXButton btnSave;
    public JFXTextField txtItemID;
    public TableView<String[]> tblSupplier = new TableView<>();
    public TableColumn<String[], String> colSupplierID;
    public TableColumn<String[], String> colSupplierName;
    public Text txtDate;
    private List<String> cmbValues;
    private final List<String> selectedSuplierIDS = new ArrayList<>();
    private String currentItemID;
    private String imagePath;
    private final ItemBo itemBo = BoFactory.getInstance().getBo(BoType.ITEM);
    private final DataValidationBo dataValidationBo = BoFactory.getInstance().getBo(BoType.VALIDATE);
    private final SupplierBo supplierBo = BoFactory.getInstance().getBo(BoType.SUPPLIER);
    private final Logger logger = LoggerFactory.getLogger(UpdateRemoveItemFormController.class);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colSupplierID.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[0]));
        colSupplierName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[1]));

        String[] sizesArr = {"XSMALL","SMALL","MEDIUM","LARGE","X LARGE","2X LARGE","3X LARGE","4X LARGE"};
        cmbSize.getItems().addAll(sizesArr);

        cmbValues = supplierBo.getAllIDSAndNames();

        String[] categoriesArr = {"Ladies","Gents","Kids"};
        cmbCategorie.getItems().addAll(categoriesArr);

        txtDate.setText(LocalDate.now().toString());
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

    public void btnAddOnAction(ActionEvent actionEvent) {
        if (cmbSupplierID.getValue() != null){
            Object selectedItem = cmbSupplierID.getSelectionModel().getSelectedItem();
            String temp = selectedItem.toString();
            String[] arr = temp.split(" - ");
            tblSupplier.getItems().add(arr);
            cmbSupplierID.getItems().remove(selectedItem);
            cmbSupplierID.getSelectionModel().clearSelection();
            selectedSuplierIDS.add(arr[0]);
        }
    }

    public void btnSaveOnAction(ActionEvent actionEvent) {
        boolean isAllFieldsNotEmpty = dataValidationBo.isAllFieldsNotEmpty(
                currentItemID,
                txtItemName.getText(),
                txtPrice.getText(),
                txtQTY.getText()
        );
        boolean isComboboxSelected = !cmbSize.getSelectionModel().isEmpty()
                                        && !cmbCategorie.getSelectionModel().isEmpty();

        if (isAllFieldsNotEmpty && isComboboxSelected){
            Item item = new Item(
                    currentItemID,
                    txtItemName.getText(),
                    cmbSize.getSelectionModel().getSelectedItem().toString(),
                    Double.parseDouble(txtPrice.getText()),
                    Integer.parseInt(txtQTY.getText()),
                    cmbCategorie.getSelectionModel().getSelectedItem().toString(),
                    imgCloth.getImage().getUrl().toString()
            );
            if (itemBo.updateItem(item,selectedSuplierIDS)){
                clearFields();
                new Alert(Alert.AlertType.INFORMATION, "✅ Item Update Successfully !").show();
                return;
            }
            new Alert(Alert.AlertType.ERROR, "❌ Item Update Failed!").show();
            return;
        }
        new Alert(Alert.AlertType.INFORMATION, "Please Fill All Field!").show();
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {
        if (currentItemID == null){
            return;
        }
        if(itemBo.deleteItem(currentItemID)){
            clearFields();
            new Alert(Alert.AlertType.INFORMATION, "✅ Item Delete Successfully !").show();
            return;
        }
        new Alert(Alert.AlertType.ERROR, "❌ Item Delete Failed!").show();
    }

    public void btnSearchOnAction(ActionEvent actionEvent) {
        Item item = itemBo.retrieveById(txtItemID.getText());
        if (item != null){
            clearFields();
            txtItemID.setText(item.getItemId());
            currentItemID = item.getItemId();
            txtItemName.setText(item.getItemName());
            txtPrice.setText(item.getPrice()+"");
            txtQTY.setText(item.getQtyOnHand()+"");
            cmbSize.getSelectionModel().select(item.getSize());
            cmbCategorie.getSelectionModel().select(item.getCategorie());

            if (item.getItemImagePath() != null){
                Image image = new Image(item.getItemImagePath());
                imgCloth.setImage(image);
            }

            List<Supplier> supplier = item.getSupplierList();
            for (Supplier sup : supplier){
                String[] temp = {sup.getSupID(),sup.getFirstName()+" "+sup.getLastName()};
                tblSupplier.getItems().add(temp);
                selectedSuplierIDS.add(sup.getSupID());
                cmbValues.remove(temp[0]+" - "+temp[1]);
            }
            cmbSupplierID.getItems().clear();
            cmbSupplierID.getItems().addAll(cmbValues);
            new Alert(Alert.AlertType.INFORMATION, "Item  Found!").show();
            return;
        }
        new Alert(Alert.AlertType.ERROR, "❌ Item Not Found!").show();
    }
    public void btnChangeImgOnAction(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "Image Files", "*.png", "*.jpg", "*.jpeg");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showOpenDialog(primaryStage);
        if (file != null) {
            imagePath = file.toURI().toString();
            imgCloth.setImage(new Image(imagePath));
        }
    }
    private void clearFields(){
        currentItemID = null;
        txtItemID.setText("");
        txtItemName.setText("");
        txtPrice.setText("");
        txtQTY.setText("");

        cmbCategorie.getSelectionModel().clearSelection();
        cmbSupplierID.getItems().clear();
        cmbSize.getSelectionModel().clearSelection();

        imagePath = null;
        Image image = new Image("D://cloth-shop-management//" +
                "src//main//resources//asset//images" +
                "//360_F_248426448_NVKLywWqArG2ADUxDq6QprtIzsF82dMF-removebg-preview.png");
        imgCloth.setImage(image);
        selectedSuplierIDS.clear();
        tblSupplier.getItems().clear();
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

    public void btnAddSupplierOnAction(ActionEvent actionEvent) {
        AddSupplieerFormcontroller.setAdmin(isAdmin);
        AddSupplieerFormcontroller.setPrimaryStage(loadScreen("view/addSupplierForm.fxml"));
    }

    public void btnAddProductOnAction(ActionEvent actionEvent) {
        AddItemFormController.setAdmin(isAdmin);
        AddItemFormController.setPrimaryStage(loadScreen("view/addItemForm.fxml"));
    }

    public void btnUpdateOrRemoveSupplierOnAction(ActionEvent actionEvent) {
        UpdateRemoveSupplierFormController.setAdmin(isAdmin);
        UpdateRemoveSupplierFormController.setPrimaryStage(loadScreen("view/updateSupplierForm.fxml"));
    }

    public void btnInventoryReportOnAction(ActionEvent actionEvent) {
        if (!itemBo.genarateInventoryReport()){
            new Alert(Alert.AlertType.ERROR,"System Error in InventoryReport!").show();
        }
    }

    public void btnItemLisOnAction(ActionEvent actionEvent) {
        ItemListController.setAdmin(isAdmin);
        ItemListController.setPrimaryStage(loadScreen("view/itemListForm.fxml"));
    }

}
