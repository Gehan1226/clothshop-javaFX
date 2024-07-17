package org.example.controller.Item;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lombok.Setter;
import org.example.bo.BoFactory;
import org.example.bo.custom.ItemBo;
import org.example.controller.AccountManage.HomePageFormController;
import org.example.controller.Admin.AdminDashboardFormController;
import org.example.controller.Supplier.AddSupplieerFormcontroller;
import org.example.controller.User.UserDashboardFormController;
import org.example.dto.Item;
import org.example.util.BoType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ItemListController implements Initializable {
    @Setter
    private static Stage primaryStage;
    @Setter
    private static boolean isAdmin;
    public GridPane gridpane;
    private final ItemBo itemBo = BoFactory.getInstance().getBo(BoType.ITEM);
    private final Logger logger = LoggerFactory.getLogger(ItemListController.class);
    public Text txtCurrentDate;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        txtCurrentDate.setText(LocalDate.now().toString());

        List<Item> allItems = itemBo.getAllItems();

        TilePane tilepane = new TilePane();
        tilepane.setHgap(10);
        tilepane.setVgap(10);
        tilepane.getStyleClass().add("tilepane-style");

        for (Item item : allItems) {
            Image image = new Image(item.getItemImagePath());
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(170);
            imageView.setPreserveRatio(true);
            tilepane.getChildren().add(imageView);

            Label itemIDLbl = new Label("Item ID : "+item.getItemId());
            Label itemSizeLbl = new Label("Item Size : "+item.getSize());
            Label catagorieLbl = new Label("Categorie : "+item.getCategorie());
            Label priceLbl = new Label("Price : Rs "+item.getPrice().toString());

            String styleClass = "label-style";
            itemIDLbl.getStyleClass().add(styleClass);
            itemSizeLbl.getStyleClass().add(styleClass);
            catagorieLbl.getStyleClass().add(styleClass);
            priceLbl.getStyleClass().add("label-style-price");

            VBox vbox = new VBox();
            vbox.setSpacing(5);
            vbox.getChildren().addAll(imageView, itemIDLbl, itemSizeLbl, catagorieLbl, priceLbl);
            vbox.setAlignment(Pos.CENTER);
            vbox.getStyleClass().add("vbox-style");
            tilepane.getChildren().add(vbox);
        }
        ScrollPane scrollPane = new ScrollPane(tilepane);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        gridpane.add(scrollPane, 2, 2);
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

    public void btnAddProductOnAction(ActionEvent actionEvent) {
        AddItemFormController.setAdmin(isAdmin);
        AddItemFormController.setPrimaryStage(loadScreen("view/addItemForm.fxml"));
    }
}

