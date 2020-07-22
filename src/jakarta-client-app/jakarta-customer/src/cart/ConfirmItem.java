package cart;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import product.Product;
import utils.Utils;

import java.io.IOException;

public class ConfirmItem extends HBox {

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

    private final Product prod;
    private final String pref;
    private final int count;

    public ConfirmItem(Product prod, int count) {
        this.prod = prod;
        this.count = count;
        // LH_TODO - 设置产品偏好
        switch (prod.getCategory()) {
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

        // 加载界面元素
        Utils.loadWidget(getClass().getResource("ConfirmItemCard.fxml"), this);
    }

    @FXML
    public void initialize() {
        // 设置界面元素
        this.lblProdName.setText(this.prod.getProductName());
        this.lblCount.setText(String.valueOf(this.count));
        this.lblSubTotalPrice.setText(Utils.getStdCash(this.prod.getPrice()));
        this.lblCurrencyMark.setText("$");
        this.lblProdPreferences.setText(this.pref);
    }
}
