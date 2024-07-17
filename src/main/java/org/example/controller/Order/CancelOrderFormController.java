package org.example.controller.Order;

import com.jfoenix.controls.JFXButton;
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
import javafx.stage.Stage;
import lombok.Setter;
import org.example.bo.BoFactory;
import org.example.bo.custom.OrderBo;
import org.example.controller.Admin.AdminDashboardFormController;
import org.example.controller.AccountManage.HomePageFormController;
import org.example.controller.Item.AddItemFormController;
import org.example.controller.Item.UpdateRemoveItemFormController;
import org.example.controller.Supplier.AddSupplieerFormcontroller;
import org.example.controller.User.UserDashboardFormController;
import org.example.dto.Item;
import org.example.dto.Order;
import org.example.util.BoType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class CancelOrderFormController implements Initializable {
    @Setter
    private static Stage primaryStage;
    @Setter
    private static boolean isAdmin;
    public Text txtPayementType;
    public Text txtOrderDate;
    public Text txtCustomername;
    public Text txtCustomerEmail;
    public Text txtCustomerMobileNumber;
    public Text txtFullPrice;
    public JFXTextField txtOrderID;
    public TableView<String[]> tblItem= new TableView<>();
    public TableColumn<String[], String> colItemID;
    public TableColumn<String[], String> colItemName;
    public TableColumn<String[], String> colSize;
    public TableColumn<String[], String> colQuantity;
    public TableColumn<String[], String> colPrice;
    public JFXButton btnCancelOrder;
    public Text txtDate;
    private final OrderBo orderBo = BoFactory.getInstance().getBo(BoType.ORDER);
    private final Logger logger = LoggerFactory.getLogger(CancelOrderFormController.class);
    private Order order;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colItemID.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[0]));
        colItemName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[1]));
        colSize.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[2]));
        colQuantity.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[3]));
        colPrice.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[4]));

        txtDate.setText(String.valueOf(LocalDate.now()));
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

    public void btncancelorderOnAction(ActionEvent actionEvent) {
        if (!Objects.equals(order.getOrderDate(), LocalDate.now())){
            new Alert(Alert.AlertType.ERROR, "You can't delete this order!").show();
            clearFields();
            return;
        }
        if (orderBo.deleteOrder(txtOrderID.getText())){
            clearFields();
            new Alert(Alert.AlertType.INFORMATION, "Order Delete Successfully !").show();
            return;
        }
        new Alert(Alert.AlertType.ERROR, "Order Delete Failed !").show();
    }

    public void btnSearch(ActionEvent actionEvent) {
        order = orderBo.getOrder(txtOrderID.getText());

        if (order != null){
            btnCancelOrder.setDisable(false);

            txtCustomername.setText(order.getCustomer().getName());
            txtCustomerEmail.setText(order.getCustomer().getEmail());
            txtCustomerMobileNumber.setText(order.getCustomer().getEmail());
            txtFullPrice.setText(order.getFullPrice().toString());
            txtPayementType.setText(order.getPayementType());
            txtOrderDate.setText(order.getOrderDate().toString());
            btnCancelOrder.setDisable(false);

            List<Item> itemList = order.getItemList();

            for(int i=0;i<itemList.size();i++){
                String[] rowData = new String[5];
                rowData[0] = itemList.get(i).getItemId();
                rowData[1] = itemList.get(i).getItemName();
                rowData[2] = itemList.get(i).getSize();
                rowData[3] = order.getItemQtyList().get(i).toString();
                rowData[4] = (order.getItemQtyList().get(i) * itemList.get(i).getPrice())+"";
                tblItem.getItems().add(rowData);
            }
            long dataGap = ChronoUnit.DAYS.between(LocalDate.now(),order.getOrderDate());
            if (dataGap > 7){
                new Alert(Alert.AlertType.INFORMATION, "Sorry You can't cancel this order!").show();
                btnCancelOrder.setDisable(true);
                return;
            }
            new Alert(Alert.AlertType.INFORMATION, "Order found !").show();
            return;
        }
        clearFields();
        new Alert(Alert.AlertType.INFORMATION, "Order Not found !").show();
    }

    private void clearFields(){
        txtOrderDate.setText("");
        txtCustomername.setText("");
        txtFullPrice.setText("");
        txtPayementType.setText("");
        txtCustomerEmail.setText("");
        txtOrderID.setText("");
        tblItem.getItems().clear();
        txtCustomerMobileNumber.setText("");
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

    public void btnPlaceOrderOnAction(ActionEvent actionEvent) {
        if (!isAdmin){
            PlaceOrderFormController.setPrimaryStage(loadScreen("view/placeOrderForm.fxml"));
        }
    }

    public void btnAddSupplierOnAction(ActionEvent actionEvent) {
        AddSupplieerFormcontroller.setAdmin(isAdmin);
        AddSupplieerFormcontroller.setPrimaryStage(loadScreen("view/addSupplierForm.fxml"));
    }

    public void btnAddProductOnAction(ActionEvent actionEvent) {
        AddItemFormController.setAdmin(isAdmin);
        AddItemFormController.setPrimaryStage(loadScreen("view/addItemForm.fxml"));
    }
}
