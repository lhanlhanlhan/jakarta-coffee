package cart;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import login.AdminLoginViewController;
import mainwindow.MainWindowViewController;
import order.Order;
import product.Product;
import utils.Utils;

public class ConfirmViewController {

    @FXML
    private VBox confirmItemContainer;

    @FXML
    private Label lblAddFee;

    @FXML
    private Label lblTotalPrice;

    @FXML
    private TextArea txtDemands;

    @FXML
    private Button btnPay;

    private Order theOrder;

    private boolean isPaymentSucceeded = false;

    public boolean isPaymentSucceeded() {
        return isPaymentSucceeded;
    }

    @FXML
    void initialize() {
        this.confirmItemContainer.getChildren().add(
                Utils.newEmptyIndicator()
        );
        // 更新界面
        ObservableList<Node> children = this.confirmItemContainer.getChildren();
        children.clear();



        this.theOrder.getItems().forEach(cartItem ->
                children.add(
                        new ConfirmItem(cartItem.getProduct(), cartItem.getCount())
                ));
        this.lblAddFee.setText("0.00");
        this.lblTotalPrice.setText(Utils.getStdCash(this.theOrder.getPrice()));
    }

    @FXML
    void btnPayClicked() {
        Utils.reportInfo("Please ask for a stuff for help", "Payment acknowledgement.");
        AdminLoginViewController alvc = Utils.showFXML(
                AdminLoginViewController.class.getResource("AdminLoginView.fxml"),
                "Admin Confirmation",
                true,
                this.btnPay.getScene().getWindow()
        );
        if (alvc != null && alvc.isSignInSucceeded()) {
            // 成功地让服务员登录了
            if (this.theOrder.submit()) {
                Utils.reportInfo("Payment Succeeded",
                        "Your order was successfully confirmed. We'll do our best and start preparing your order right away. Thank you!");
                // 关闭该窗口
                this.isPaymentSucceeded = true;
                Stage thisWin = (Stage) this.btnPay.getScene().getWindow();
                thisWin.close();
            } else {
                Utils.reportError("Payment Failed",
                        "Your order was not submitted. One of your order item seems to be out-of-stock.");
                // 关闭该窗口
                this.isPaymentSucceeded = false;
                Stage thisWin = (Stage) this.btnPay.getScene().getWindow();
                thisWin.close();
            }
        }
    }

    // 设定该窗口所对的订单
    void setOrder(Order order) {
        this.theOrder = order;
    }
}
