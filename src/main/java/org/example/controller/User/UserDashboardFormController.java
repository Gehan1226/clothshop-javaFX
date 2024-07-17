package org.example.controller.User;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import lombok.Setter;
import org.example.bo.BoFactory;
import org.example.bo.custom.*;
import org.example.controller.Item.ItemListController;
import org.example.controller.Supplier.AddSupplieerFormcontroller;
import org.example.controller.Employee.EmployeeProfilePageFormController;
import org.example.controller.AccountManage.HomePageFormController;
import org.example.controller.Item.AddItemFormController;
import org.example.controller.Item.UpdateRemoveItemFormController;
import org.example.controller.Order.CancelOrderFormController;
import org.example.controller.Order.PlaceOrderFormController;
import org.example.controller.Supplier.UpdateRemoveSupplierFormController;
import org.example.util.BoType;

import java.io.IOException;
import java.net.URL;

public class UserDashboardFormController {
    @Setter
    private static String employeeUserEmail;
    @Setter
    private static Stage primaryStage;
    private final EmployeeBo employeeBo = BoFactory.getInstance().getBo(BoType.EMPLOYEE);
    private final String empID = employeeBo.retrieveByEmail(employeeUserEmail).getEmpID();

    public Stage loadScreen(String path){
        Stage stage = null;
        try{
            primaryStage.close();
            URL fxmlLocation = getClass().getClassLoader().getResource(path);
            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent parent = loader.load();
            stage = new Stage();
            stage.setScene(new Scene(parent));
            stage.show();
        }catch (IOException e){}
        return stage;
    }

    public void btnProfileOnAction(ActionEvent actionEvent) {
        EmployeeProfilePageFormController.setEmployeeUserEmail(employeeUserEmail);
        EmployeeProfilePageFormController.setPrimaryStage(loadScreen("view/employeeProfilePageForm.fxml"));
    }

    public void btnAddItemOnAction(ActionEvent actionEvent) {
        AddItemFormController.setAdmin(false);
        AddItemFormController.setPrimaryStage(loadScreen("view/addItemForm.fxml"));
    }

    public void btnItemUpdateRemoveOnAction(ActionEvent actionEvent) {
        UpdateRemoveItemFormController.setAdmin(false);
        UpdateRemoveItemFormController.setPrimaryStage(loadScreen("view/updateItemForm.fxml"));
    }

    public void btnAddSupplierOnAction(ActionEvent actionEvent) {
        AddSupplieerFormcontroller.setAdmin(false);
        AddSupplieerFormcontroller.setPrimaryStage(loadScreen("view/addSupplierForm.fxml"));
    }

    public void btnSupplierUpdateRemoveOnAction(ActionEvent actionEvent) {
        UpdateRemoveSupplierFormController.setAdmin(false);
        UpdateRemoveSupplierFormController.setPrimaryStage(loadScreen("view/updateSupplierForm.fxml"));
    }

    public void btnPlaceOrderOnAction(ActionEvent actionEvent) {
        PlaceOrderFormController.setEmployeeID(empID);
        PlaceOrderFormController.setPrimaryStage(loadScreen("view/placeOrderForm.fxml"));
    }

    public void btnCancelorderOnAction(ActionEvent actionEvent) {
        CancelOrderFormController.setAdmin(false);
        CancelOrderFormController.setPrimaryStage(loadScreen("view/cancelOrderForm.fxml"));
    }

    public void btnInventoryReportOnAction(ActionEvent actionEvent) {
        ItemBo itemBo = BoFactory.getInstance().getBo(BoType.ITEM);
        if (!itemBo.genarateInventoryReport()){
            new Alert(Alert.AlertType.ERROR,"System Error in InventoryReport!").show();
        }
    }

    public void btnSupplierReportOnAction(ActionEvent actionEvent) {
        SupplierBo supplierBo = BoFactory.getInstance().getBo(BoType.SUPPLIER);
        if (supplierBo.genarateSupplierReport()){
            new Alert(Alert.AlertType.ERROR,"System Error in SupplierReport!").show();
        }
    }

    public void btnLogOutOnAction(ActionEvent actionEvent) {
        HomePageFormController.setPrimaryStage(loadScreen("view/home_page_from.fxml"));
    }

    public void btnPrintEmployeeReportOnAction(ActionEvent actionEvent) {
        EmployeeBo emploeeBo = BoFactory.getInstance().getBo(BoType.EMPLOYEE);
        if (!emploeeBo.genarateEmployeeReport()){
            new Alert(Alert.AlertType.ERROR,"System Error in EmployeeReport!").show();
        }
    }

    public void btnItemListOnAction(ActionEvent actionEvent) {
        ItemListController.setAdmin(false);
        ItemListController.setPrimaryStage(loadScreen("view/itemListForm.fxml"));
    }
}
