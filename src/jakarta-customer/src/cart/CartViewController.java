package cart;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import product.Product;
import utils.Utils;

import java.util.ArrayList;
import java.util.Optional;

public class CartViewController {
    @FXML
    private Label lblTotalPrice;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnBuy;

    @FXML
    private Button btnSelectAll;

    @FXML
    private VBox containerCart;

    @FXML
    private Label lblCartCount;


    private Cart cart;

    @FXML
    void initialize() {
        // 设置全局购物车
        this.cart = Utils.getCart();
        this.cart.setController(this);

        // 显示购物车内容
        this.containerCart.getChildren().addAll(this.cart.getProducts());
        this.updateData();
        this.setSelectedSomething();
    }

    @FXML
    void btnDeleteClicked() { // 删除选中的项目
        if (this.containerCart == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.titleProperty().set("Wrong");
            alert.headerTextProperty().set("购物车为空");
            alert.show();
        }
        else {
            this.cart.deleteSelectedProduct();
            this.setSelectedSomething();
        }
    }
    @FXML
    void btnBuyClicked() {
        String cont = "Total price: $" + String.format("%.2f", this.cart.getTotalPrice());
        switch (Utils.askUser("Confirm Proceed", cont)) {
            case YES:
                // 加载支付页面
                Utils.showAndWaitFXML(PayViewController.class.getResource("PayView.fxml"), "PayView");
        }
    }

    // 尝试更新本页数据
    public void updateData() {
        // 更新数量
        this.lblTotalPrice.setText(String.format("%.2f", cart.getTotalPrice()));
        this.lblCartCount.setText(String.valueOf(cart.getProducts().size()));
    }

    // 从容器中扔掉某个记录
    public void deleteFromContainer(CartItem ci) {
        this.containerCart.getChildren().remove(ci);
    }

    @FXML
    void btnSelectAllClicked() {
        this.cart.selectAll();
        this.updateData();
        this.setSelectedSomething();
        this.btnSelectAll.setDisable(true);
    }

    // 确有选中任何物品时，启用相关按钮、刷新
    public void setSelectedSomething() {
        boolean sel = this.cart.isSelectedSomething();
        this.btnBuy.setDisable(!sel);
        this.btnDelete.setDisable(!sel);
        this.updateData();
    }
}
