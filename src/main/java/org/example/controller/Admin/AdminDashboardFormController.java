package org.example.controller.Admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import lombok.Setter;
import org.example.bo.BoFactory;
import org.example.bo.custom.EmployeeBo;
import org.example.bo.custom.ItemBo;
import org.example.bo.custom.SalesReportBo;
import org.example.bo.custom.SupplierBo;
import org.example.controller.Item.ItemListController;
import org.example.controller.Supplier.AddSupplieerFormcontroller;
import org.example.controller.Employee.EmployeeUpdateRemoveFormController;
import org.example.controller.Employee.UserRegistrationFormController;
import org.example.controller.AccountManage.HomePageFormController;
import org.example.controller.Item.AddItemFormController;
import org.example.controller.Item.UpdateRemoveItemFormController;
import org.example.controller.Supplier.UpdateRemoveSupplierFormController;
import org.example.util.BoType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.net.URL;


public class AdminDashboardFormController{
    @Setter
    private static Stage primaryStage;
    @Setter
    private static String adminUserEmail;
    private final Logger logger = LoggerFactory.getLogger(AdminDashboardFormController.class);

    private Stage loadScreen(String path){
        Stage stage = null;
        try{
            primaryStage.close();
            URL fxmlLocation = getClass().getClassLoader().getResource(path);
            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent parent = loader.load();
            stage = new Stage();
            stage.setScene(new Scene(parent));
            stage.show();
        }catch (IOException e) {
            logger.error("IOException occurred while loading FXML file: {}", path, e);
        } catch (Exception e) {
            logger.error("An unexpected error occurred", e);
        }
        return stage;
    }

    public void btnUserRegistrationOnAction(ActionEvent actionEvent) {
        UserRegistrationFormController.setPrimaryStage(loadScreen("view/userRegistrationForm.fxml"));
    }

    public void btnProfileOnAction(ActionEvent actionEvent) {
        AdminProfilePageFormcontroller.setAdminEmail(adminUserEmail);
        AdminProfilePageFormcontroller.setPrimaryStage(loadScreen("view/adminProfilePageForm.fxml"));
    }

    public void btnEmployeeUpdateRemoveOnAction(ActionEvent actionEvent) {
        EmployeeUpdateRemoveFormController.setPrimaryStage(loadScreen("view/employeeUpdateRemovePageForm.fxml"));
    }

    public void btnAddItemOnAction(ActionEvent actionEvent) {
        AddItemFormController.setAdmin(true);
        AddItemFormController.setPrimaryStage(loadScreen("view/addItemForm.fxml"));
    }

    public void btnItemUpdateRemoveOnAction(ActionEvent actionEvent) {
        UpdateRemoveItemFormController.setAdmin(true);
        UpdateRemoveItemFormController.setPrimaryStage(loadScreen("view/updateItemForm.fxml"));
    }

    public void btnAddSupplierOnAction(ActionEvent actionEvent) {
        AddSupplieerFormcontroller.setAdmin(true);
        AddSupplieerFormcontroller.setPrimaryStage(loadScreen("view/addSupplierForm.fxml"));
    }

    public void btnSpplierUpdateRemoveOnAction(ActionEvent actionEvent) {
        UpdateRemoveSupplierFormController.setAdmin(true);
        UpdateRemoveSupplierFormController.setPrimaryStage(loadScreen("view/updateSupplierForm.fxml"));
    }

    public void btnPrintEmployeeReport(ActionEvent actionEvent) {
        EmployeeBo emploeeBo = BoFactory.getInstance().getBo(BoType.EMPLOYEE);
        if (!emploeeBo.genarateEmployeeReport()){
            new Alert(Alert.AlertType.ERROR,"System Error in EmployeeReport!").show();
        }
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

    public void btnAnnualSalesOnAction(ActionEvent actionEvent) {
        SalesReportBo salesReportBo = BoFactory.getInstance().getBo(BoType.SALES);
        if (!salesReportBo.genaratYearlyReport()){
            new Alert(Alert.AlertType.ERROR,"System Error in AnnualSales!").show();
        }
    }

    public void btnMontlySalesOnAction(ActionEvent actionEvent) {
        SalesReportBo salesReportBo = BoFactory.getInstance().getBo(BoType.SALES);
        if (!salesReportBo.genarateMonthlyReport()){
            new Alert(Alert.AlertType.ERROR,"System Error in MonthlySales!").show();
        }
    }

    public void btnDailySalesOnAction(ActionEvent actionEvent) {
        SalesReportBo salesReportBo = BoFactory.getInstance().getBo(BoType.SALES);
        if (!salesReportBo.genarateDailyReport()){
            new Alert(Alert.AlertType.ERROR,"System Error in DailySales!").show();
        }
    }

    public void btnLogOutOnAction(ActionEvent actionEvent) {
        HomePageFormController.setPrimaryStage(loadScreen("view/home_page_from.fxml"));
    }

    public void btnItemListOnAction(ActionEvent actionEvent) {
        ItemListController.setAdmin(true);
        ItemListController.setPrimaryStage(loadScreen("view/itemListForm.fxml"));
    }
}
