package user;

import cart.CartItem;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.Window;
import order.Order;
import order.OrdersListViewController;
import product.Product;
import utils.Utils;

import java.util.ArrayList;
import java.util.HashSet;

public class UserInfoController {

    @FXML
    private Label icoUsername;

    @FXML
    private TextField txtUsername;

    @FXML
    private Label icoTel;

    @FXML
    private TextField txtTel;

    @FXML
    private Label icoEmail;

    @FXML
    private TextField txtEmail;

    @FXML
    private Button btnSaveBasic;

    @FXML
    private Label icoPass;

    @FXML
    private PasswordField txtOldPassword;

    @FXML
    private Label icoNewPass;

    @FXML
    private PasswordField txtNewPassword;

    @FXML
    private Button btnSavePassword;

    @FXML
    private Label icoVerifyPass;

    @FXML
    private PasswordField txtVerifyPass;

    @FXML
    void initialize() {
        this.icoUsername.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.USER));
        this.icoEmail.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.ENVELOPE));
        this.icoTel.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.PHONE));

        this.icoPass.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.KEY));
        this.icoNewPass.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.KEY));
        this.icoVerifyPass.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.KEY));
        try {
            JSONObject result = Utils.sendGet(Utils.getUsersURL("/client/get-my-info"));
            JSONObject data = result.getJSONObject("data");
            txtUsername.setText(data.getString("username"));
            txtEmail.setText(data.getString("email"));
            txtTel.setText(data.getString("telephone"));
            Utils.username = data.getString("username");
            Utils.email = data.getString("email");
            Utils.tel = data.getString("telephone");
        } catch (Exception e) {
            Utils.reportError("Network Error", "Server was unable to dial. Please check your Internet connection.");
        }
    }

    private boolean isChanging = false;

    @FXML
    void btnSaveBasicClicked() {
        if (!isChanging) {
            // 切换到编辑模式
            isChanging = true;
            txtUsername.setDisable(false);
            txtEmail.setDisable(false);
            txtTel.setDisable(false);
            btnSaveBasic.setText("Save");
            return;
        }
        // 已经在编辑模式
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("old_username", Utils.username);
        jsonObject.put("username", txtUsername.getText());
        jsonObject.put("email", txtEmail.getText());
        jsonObject.put("telephone", txtTel.getText());
        try {
            JSONObject ret = Utils.sendPost(Utils.getUsersURL("/client/set-my-info"), jsonObject);
            if (ret.getInteger("code").equals(200)) {
                Utils.username = txtUsername.getText();
                Utils.email = txtEmail.getText();
                Utils.tel = txtTel.getText();
                Utils.username = Utils.username;
                Utils.token = ret.getJSONObject("data").getString("token");
                Utils.isProfileChanged = true;
                Utils.reportInfo("Operation Done","The profile was saved.");
            }
            else {
                txtUsername.setText(Utils.username);
                txtEmail.setText(Utils.email);
                txtTel.setText(Utils.tel);
            }
        } catch (Exception e) {
            Utils.reportError("Error", "The profile could not be saved.");
        }
        txtUsername.setDisable(true);
        txtEmail.setDisable(true);
        txtTel.setDisable(true);
        isChanging = false;
        this.btnSaveBasic.setText("Edit");
    }

    @FXML
    void btnSavePasswordClicked() {
        if(!txtNewPassword.getText().equals(txtVerifyPass.getText())){
            Utils.reportError("Error","The two passwords are not matching.");
        }
        else {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username", txtUsername.getText());
            jsonObject.put("old_password", txtOldPassword.getText());
            jsonObject.put("new_password", txtNewPassword.getText());
            try {
                JSONObject ret = Utils.sendPost(Utils.getUsersURL("/client/set-my-info"), jsonObject);
                if (ret.getInteger("code").equals(200)) {
                    txtNewPassword.setText("");
                    txtOldPassword.setText("");
                    txtVerifyPass.setText("");
                    Utils.reportInfo("Operation Done","The new password was saved. You have to re-login now.");
                    Utils.isReLoginNeeded = true;
                    Stage st = (Stage) this.btnSaveBasic.getScene().getWindow();
                    st.close();
                } else {
                    Utils.reportError("Error",
                            String.format("The new password could not be saved. Error: %s Code: %d", ret.getString("message"), ret.getInteger("exception")));
                }
            } catch (Exception e) {
                Utils.reportError("Error", "The new password could not be saved: Network error.");
            }
        }
    }

    @FXML // 点击了"查看我的订单"
    void btnMyOrdersClicked() {
        ArrayList<Order> orders = new ArrayList<>();
        try {
            JSONObject result = Utils.sendGet(Utils.getOrdersURL("/get-all-orders"));
            if (result.getInteger("code") != 200) {
                Utils.reportError("Error", "Query failed. Your current status is weird.");
                return;
            }
            // 准备parse一下data数组（一堆订单）
            JSONArray orderArray = result.getJSONArray("data");
            for (int i = 0; i < orderArray.size(); i++) {
                // 准备parse一下这个订单
                JSONObject order = orderArray.getJSONObject(i);
                JSONArray items = order.getJSONArray("items");
                ArrayList<CartItem> cartItems = new ArrayList<>();
                for (int j = 0; j < items.size(); j++) {
                    // 添加一个订单进入cart
                    JSONObject obj = items.getJSONObject(j);
                    String photo = obj.getString("photo_uri");
                    Image img = photo.toLowerCase().equals("null") ?
                            null :
                            new Image(photo);
                    int count = obj.getInteger("item_count");
                    Product prod = new Product(
                            Product.Category.NOT_APPLICABLE,
                            "",
                            obj.getString("name"),
                            obj.getFloat("t_price") / count,
                            img);
                    CartItem c = new CartItem(Utils.getCart(), prod, count);
                    cartItems.add(c);
                }
                // 将整个订单填入
                orders.add(new Order(cartItems, order.getLong("create_time") * 1000, 0, order.getString("order_id")));
            }
        } catch (Exception e) {
            Utils.reportError("Network Error", "Server was unable to dial. Please check your Internet connection.");
            return;
        }

        OrdersListViewController olvc = new OrdersListViewController(orders);
        // 显示
        Utils.showFXML(
                OrdersListViewController.class.getResource("OrdersListView.fxml"),
                "Historical Orders",
                olvc,
                this.btnSaveBasic.getScene().getWindow());
    }

}
