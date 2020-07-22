package cart;

import com.alibaba.fastjson.JSONObject;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import order.Order;
import utils.Utils;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.LinkedHashSet;

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
    void btnDeleteClicked() { // 删除选中的项目 checked
        // 判断下
        if (this.containerCart == null) {
            Utils.reportError("Error", "A null cart detected.");
            return;
        }

        // 先从服务器删除
        for (CartItem item : this.cart.getSelectedProducts()) {
            JSONObject ret = Utils.sendPost(Utils.getCartsURL("/del-from-cart"),
                    "item_id=" + item.getProductNumber());
            if (ret.getInteger("code") != 200) {
                item.setChecked(!item.isChecked());
            }
        }
        this.cart.deleteSelectedProduct();
        this.setSelectedSomething();
    }
    @FXML // 点击了"结算"
    void btnBuyClicked() {
        ConfirmViewController cvc = new ConfirmViewController();
        // 修改窗口内容
        cvc.setOrder(new Order(this.cart.getSelectedProducts(), 0, 0, ""));
        Utils.showFXML(
                ConfirmViewController.class.getResource("ConfirmView.fxml"),
                "Confirm Your Order",
                cvc,
                this.btnBuy.getScene().getWindow());
        if (cvc.isPaymentSucceeded()) {
            // 购物车中删除选中的项目
            this.cart.getSelectedProducts()
                    .forEach(cartItem -> {
                        this.cart.getProducts().remove(cartItem);
                        this.containerCart.getChildren().remove(cartItem);
                    });
            this.cart.getSelectedProducts().clear();
        }
        // 关闭购物车
        Stage stage = (Stage) this.btnBuy.getScene().getWindow();
        stage.close();
    }

    // 尝试更新本页数据
    public void updateData() {
        // 更新数量
        this.lblTotalPrice.setText(Utils.getStdCash(cart.getTotalPrice()));
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
