package order;

import cart.CartItem;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import product.Product;
import utils.Utils;

public class OrderItem extends HBox {

    @FXML
    private Label lblProdName;

    @FXML
    private Label lblProdPreferences;

    @FXML
    private Label lblCount;

    @FXML
    private Label lblCurrencyMark;

    @FXML
    private Label lblSubTotalPrice;

    private final String itemID;
    private final String itemName;
    private final String pref;
    private final int count;
    private final float price;
    private final Product.Category category;

    public OrderItem(CartItem ci) {
        this.itemID = ci.getProductNumber();
        this.itemName = ci.getProduct().getProductName();
        this.count = ci.getCount();
        this.price = ci.getSubTotalPrice();
        this.category = ci.getProduct().getCategory();
        // 设置产品偏好
        switch (this.category) {
            case DRINK:
                this.pref = "Regular, Tall";
                break;
            case SNACK:
                this.pref = "Size Big, With Butter";
                break;
            case STAPLE_FOODS:
                this.pref = "Medium Bowl, Chinese Prickly Ash";
                break;
            default:
                this.pref = "Not Applicable";
                break;
        }

        Utils.loadWidget(getClass().getResource("OrderItemCard.fxml"), this);
    }

    public OrderItem(String itemID, String itemName, int count, float price, String pref, Product.Category category) {
        this.itemID = itemID;
        this.itemName = itemName;
        this.count = count;
        this.price = price;
        this.pref = pref;
        this.category = category;

        Utils.loadWidget(getClass().getResource("OrderItemCard.fxml"), this);
    }

    @FXML
    void initialize() {
        this.lblCount.setText(String.valueOf(this.count));
        this.lblProdName.setText(this.itemName);
        this.lblSubTotalPrice.setText(Utils.getStdCash(this.price));
        this.lblCurrencyMark.setText("$");
        this.lblProdPreferences.setText(this.pref);
    }
}
