package product;

import cart.Cart;
import cart.CartItem;
import com.alibaba.fastjson.JSONObject;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import utils.Utils;

import java.io.IOException;

public class Product extends HBox {

    @FXML
    private ImageView imgProduct;

    @FXML
    private Label lblProdName;

    @FXML
    private Label lblStars;

    @FXML
    private Label lblBrief;

    @FXML
    private Label lblRecentlySold;

    @FXML
    private Label lblPrice;

    @FXML
    private Button btnAddToCart;

    private String productNumber;
    private String productName;
    private float price;
    private int starCount;
    private int recentlySold;
    private Category category;

    public enum Category {
        DRINK,
        SNACK,
        STAPLE_FOODS,
        NOT_APPLICABLE
    }

    @FXML
    void btnAddToCartClicked() {
        // 点击了"「添加到购物车」checked
        String send = "item_id=" + this.productNumber;
        JSONObject ret = Utils.sendPost(Utils.getCartsURL("/add-cart"), send);
        if (ret.getInteger("code") == 200) {
            System.out.println("商品：\"" + lblProdName.getText() + "\"被加入购物车了");
            // 加入购物车
            Cart cart = Utils.getCart();
            cart.addProduct(new CartItem(cart, this, 1));
            // 显示"added"
            this.btnAddToCart.setText("Added!");
            this.btnAddToCart.setDisable(true);
            Timeline animation = new Timeline(new KeyFrame(Duration.millis(1000),
                    (event) -> {
                        this.btnAddToCart.setText("Add to Cart");
                        this.btnAddToCart.setDisable(false);
                    }));
            animation.setCycleCount(1);
            animation.play();
        }
    }
    public Product() {
        this.category = null;
        this.productNumber = null;
        this.lblProdName.setText(null);
        this.productName = null;
        this.lblPrice.setText(null);
        this.price = 0;
    }

    public Product(Product orig) {
        this(orig.category, orig.productNumber, orig.productName, orig.price, orig.recentlySold, orig.getBrief(), orig.starCount, orig.getImage());
    }

    public Product(Category cat, String productNumber, String name, float price) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                "ProductCard.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.category = cat;
        this.productNumber = productNumber;
        this.lblProdName.setText(name);
        this.productName = name;
        this.lblPrice.setText(Utils.getStdCash(price));
        this.price = price;
    }

    public Product(Category cat, String productNumber, String name, float price, int recentlySold) {
        this(cat, productNumber, name, price);
        this.lblRecentlySold.setText(String.valueOf(recentlySold));
        this.recentlySold = recentlySold;
    }

    public Product(Category cat, String productNumber, String name, float price, int recentSold, String brief) {
        this(cat, productNumber, name, price, recentSold);
        this.lblBrief.setText(brief);
    }

    public Product(Category cat, String productNumber, String name, float price, int recentSold, String brief, int starsCount) {
        this(cat, productNumber, name, price, recentSold, brief);
        this.setStarsCount(starsCount);
    }

    public Product(Category cat, String productNumber, String name, float price, int recentSold, String brief, int starsCount, Image image) {
        this(cat, productNumber, name, price, recentSold, brief);
        this.setStarsCount(starsCount);
        this.setImgProduct(image);
    }

    public Product(Category cat, String productNumber, String name, float price, Image image) {
        this(cat, productNumber, name, price);
        this.lblRecentlySold.setText(String.valueOf(recentlySold));
        this.recentlySold = recentlySold;
        this.setImgProduct(image);
    }

    // 设置、获取星星数量
    public void setStarsCount(int count) {
        StringBuilder stars = new StringBuilder();
        while (count-- > 0) {
            stars.append("★");
        }
        this.lblStars.setText(stars.toString());
        this.starCount = count;
    }
    public String getBrief(){return this.lblBrief.getText();}
    public int getStarCount() {
        return this.starCount;
    }

    public int getRecentSold(){return this.recentlySold;}

    // 设置、获取商品名字
    public void setProductName(String name) {
        this.lblProdName.setText(name);
        this.productName = name;
    }
    public String getProductName() {
        return this.productName;
    }

    // 设置、获取价格
    public void setPrice(float price) {
        this.lblPrice.setText(Utils.getStdCash(price));
        this.price = price;
    }
    public float getPrice() {
        return price;
    }

    // 设置、获取种类
    public void setCategory(Category cat) {
        this.category = cat;
    }
    public Category getCategory() {
        return this.category;
    }
    public String getProductNumber(){return this.productNumber;}
    // 设置是否缺货
    public void setOutOfStock(boolean isOutOfStock) {
        this.btnAddToCart.setDisable(isOutOfStock);
        if (isOutOfStock) {
            this.btnAddToCart.setText("Sorry! Out of Stock");
        } else {
            this.btnAddToCart.setText("Add to Cart");
        }
    }
    public boolean getOutOfStock(){
        return this.btnAddToCart.isDisable();
    }
    public void setImgProduct(Image image){
        imgProduct.setImage(image);
    }
    public Image getImage(){return this.imgProduct.getImage();}
    public String getProductID(){
        return this.productNumber;
    }
}
