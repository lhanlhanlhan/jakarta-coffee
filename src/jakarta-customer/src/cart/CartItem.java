package cart;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import product.Product;

import java.io.IOException;

public class CartItem extends HBox {

    @FXML
    private ImageView imgProduct;

    @FXML
    private Label lblProdName;

    @FXML
    private Label lblSubTotalPrice;

    @FXML
    private Label lblPrice;

    @FXML
    private Button btnSub;

    @FXML
    private Label lblCount;

    @FXML
    private Label lblSmallCount;

    @FXML
    private Button btnAdd;

    @FXML
    private CheckBox check;

    private final Product prod;
    private int count;
    private boolean checkedFlag;
    private final Cart cart; // 所属购物车

    public Product getProduct() {
        return prod;
    }

    @FXML
    void btnAddClicked() {
        this.setCount(count + 1);
        this.btnSub.setDisable(false);
        // 如果有被选中，增加购物车总价
        if (this.checkedFlag) {
            this.cart.setTotalPrice(this.cart.getTotalPrice() + this.prod.getPrice());
        }
    }

    @FXML
    void btnSubClicked() {
        this.setCount(this.count - 1);
        this.btnSub.setDisable(this.count == 1);
        // 如果有被选中，减少购物车总价
        if (this.checkedFlag) {
            this.cart.setTotalPrice(this.cart.getTotalPrice() - this.prod.getPrice());
        }
    }

    @FXML
    void checkWillChange() {
        this.checkedFlag = !this.check.isSelected();
        this.cart.selectOrDeselectProduct(this);
    }


    public CartItem(Cart target, Product prod, int count) {
        this.cart = target;
        this.prod = prod;
        this.count = count;

        // 加载界面元素
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                "CartItemCard.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @FXML
    public void initialize() {
        Image img = this.prod.getImage();
        if (img != null) {
            this.imgProduct.setImage(img);
        }
        // 设置界面元素
        this.lblProdName.setText(this.prod.getProductName());
        this.lblCount.setText(String.valueOf(this.count));
        this.lblSmallCount.setText(String.valueOf(this.count));
        this.lblSubTotalPrice.setText(String.valueOf(this.getSubTotalPrice()));
        this.lblPrice.setText(String.valueOf(this.prod.getPrice()));
    }

    // 获取产品编号
    public String getProductNumber() {
        return prod.getProductNumber();
    }

    // 获取数量
    public int getCount() {
        return count;
    }

    // 设置数量
    public void setCount(int count) {
        this.count = count;
        // 显示count
        this.lblCount.setText(String.valueOf(this.count));
        this.lblSmallCount.setText(String.valueOf(this.count));
        // 计算总价并显示
        this.lblSubTotalPrice.setText(
                String.format("%.2f", this.count * this.prod.getPrice())
        );
    }

    // Count++;
    public void incCount() {
        this.setCount(count + 1);
    }

    // Count--;
    public void decCount() {
        this.setCount(count - 1);
    }

    // 获取单价
    public float getPrice() {
        return prod.getPrice();
    }

    // 获取总价
    public float getSubTotalPrice() {
        return count * prod.getPrice();
    }

    // 设置选中
    public void setChecked(boolean checked) {
        this.checkedFlag = checked;
        this.check.setSelected(checked);
    }

    // 是否选中
    public boolean isChecked() {
        return checkedFlag;
    }
}
