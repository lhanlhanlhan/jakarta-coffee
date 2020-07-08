package mainwindow;

import cart.CartViewController;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.stage.Stage;
import login.LoginViewController;
import user.UserInfoController;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import product.Product;
import product.Product.Category;
import utils.Utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;

public class MainWindowViewController {

    @FXML
    private Button btnUser;

    @FXML
    private Button btnCart;

    @FXML
    private ChoiceBox<String> choiceOrdering;

    @FXML
    private VBox containerDrinks;

    @FXML
    private VBox containerSnacks;

    @FXML
    private VBox containerStapleFoods;

    @FXML
    private VBox containerPopular;

    @FXML
    private TextField txtSearch;

    @FXML
    private Button btnSearch;

    private enum Ordering {
        NAME("Name"),
        RECENTLY_SOLD("Recently Sold"),
        PRICE("Price");

        String strCh;
        static final String[] choicesStr = new String[] { "Name", "Recently Sold", "Price" };
        static final Ordering[] choices = new Ordering[] { NAME, RECENTLY_SOLD, PRICE };

        Ordering(String ch) {
            this.strCh = ch;
        }

        @Override
        public String toString() {
            return this.strCh;
        }
    }

    // 按照销量排序的product
    private final TreeSet<Product> popularProducts = new TreeSet<>(
            // 默认排序方案：按照销量
            (left, right) -> right.getRecentSold() - left.getRecentSold()
    );

    // 目前显示的product
    private TreeSet<Product> products = new TreeSet<>(
            // 默认排序方案：按照名字
            Comparator.comparing(Product::getProductName)
    );

    @FXML
    public void initialize() { // 初始化
        // 绑定可用性
        this.btnSearch.disableProperty().bind(this.txtSearch.textProperty().isEmpty());

        // 初始化下拉框
        this.choiceOrdering.getItems().addAll(Ordering.choicesStr);
        this.choiceOrdering.getSelectionModel().select(0);
        this.choiceOrdering.setOnAction(event -> choiceOrderingChanged());
        btnUser.setText(Utils.username);
        // 实例1：普通drink商品
   /*     Product sampleProductA = new Product(
                Category.DRINK,         // 种类
                1010,       // 产品编号
                "Cappuccino",       // 品名
                43,                 // 价格
                553,            // 最近销量
                "Pour a cup of cappuccino for you.",    // 简介
                3);             // 星星数

        // 实例2：缺货drink商品
        Product sampleProductB = new Product(
                Category.DRINK,         // 种类
                1011,       // 产品编号
                "Latte",       // 品名
                42,                 // 价格
                1111,            // 最近销量
                "Pour a cup of latte for you.",    // 简介
                5);             // 星星数
        sampleProductB.setOutOfStock(true);

        //实例3：普通snack商品
        Product sampleProductC=new Product(
                Category.SNACK,         // 种类
                1100,       // 产品编号
                "cake",       // 品名
                50,                 // 价格
                666,            // 最近销量
                "Come on!",    // 简介
                5, // 星星数
                new Image(getClass().getResourceAsStream("../resource/cake.jpg"))); // HAN

        //实例4：普通StapleFoods
        Product sampleProductD = new Product (
                Category.STAPLE_FOODS,         // 种类
                1101,       // 产品编号
                "Hamburger",       // 品名
                999,                 // 价格
                1500,            // 最近销量
                "Why not have a hamburger?",    // 简介
                5, // 星星数
                new Image(getClass().getResourceAsStream("../resource/hamburger.jpg")));    // HAN

        addToMenu(sampleProductA);
        addToMenu(sampleProductB);
        addToMenu(sampleProductC);
        addToMenu(sampleProductD);*/

        doRefreshMenu();
    }

    @FXML
    void btnCartClicked() { // 点击了"查看购物车"
        Utils.showAndWaitFXML(CartViewController.class.getResource("CartView.fxml"), "Cart");
    }
    @FXML
    void btnUserClicked() {
        Utils.showAndWaitFXML(UserInfoController.class.getResource("UserInfoView.fxml"), "User Information");
        if (Utils.isReLoginNeeded) {
            // 重新登录
            Stage s = (Stage) this.btnUser.getScene().getWindow();
            s.close();
            Utils.showFXML(LoginViewController.class.getResource("LoginView.fxml"), "Login");
        }
        if (Utils.isProfileChanged) {
            // 更新姓名
            this.btnUser.setText(Utils.username);
        }
    }

    // 更换排序方式
    void changeOrdering(Ordering ord) {
        final TreeSet<Product> newProducts;
        switch (ord) {
            case NAME:
                newProducts = new TreeSet<> (
                        Comparator.comparing(Product::getProductName)
                );
                break;
            case PRICE:
                newProducts = new TreeSet<> (
                        Comparator.comparing(Product::getPrice)
                );
                break;
            case RECENTLY_SOLD:
            default:
                newProducts = new TreeSet<>(
                        Comparator.comparing(Product::getRecentSold).reversed()
                );
                break;
        }
        // 复制
        newProducts.addAll(this.products);
        this.products = null;
        this.products = newProducts;

        // 刷新显示
        this.deliverMenus();
    }

    void doRefreshMenu() {
        JSONObject result = null;
        try {
            result = Utils.sendGet(Utils.getMenusURL("/client/get-menu"));
        } catch (Exception e) {
            Utils.reportError("Error fetching menu", "The menu was unable to fetch.");
            System.exit(1);
        }
        JSONArray menu = result.getJSONArray("data");
        JSONObject obj;
        for (int i = 0; i < menu.size(); i++) {
            obj = menu.getJSONObject(i);
            Category c;
            switch (obj.getString("category")) {
                case "SN":
                    c = Category.SNACK; break;
                case "SF":
                    c = Category.STAPLE_FOODS; break;
                case "DR":
                default:
                    c = Category.DRINK; break;
            }
            String image_uri = obj.getString("photo_uri");
            String desc = obj.getString("description");
            if (image_uri.toLowerCase().equals("null")) {
                // 没有图像！
                Product p=new Product(
                        c,
                        obj.getString("item_id"),
                        obj.getString("name"),
                        obj.getFloat("price"),
                        obj.getInteger("recent_sold"),
                        desc == null ? "" : desc,
                        obj.getInteger("stars")
                );
                if(obj.getInteger("in_stock")==0){
                    p.setOutOfStock(true);
                }
                addToMenu(p);
            } else {
                // 有图像！
                Product p=new Product(
                        c,
                        obj.getString("item_id"),
                        obj.getString("name"),
                        obj.getFloat("price"),
                        obj.getInteger("recent_sold"),
                        desc == null ? "" : desc,
                        obj.getInteger("stars"),
                        new Image(image_uri)
                );
                if(obj.getInteger("in_stock")==0){
                    p.setOutOfStock(true);
                }
                addToMenu(p);
            }
        }
        // 显示菜单
        deliverMenus();
        initPopularContainer();
    }

    @FXML
    void btnRefreshClicked() { // 点击了"刷新"
        btnUser.setText(Utils.username);
        products.clear();
        doRefreshMenu();
    }

    @FXML
    void choiceOrderingChanged() { // "排列方式有所变化"
        // 获取排列方式框选项
        int selectedOrdering = this.choiceOrdering.getSelectionModel().getSelectedIndex();
        System.out.println("选择的排列方式：" + Ordering.choicesStr[selectedOrdering]);
        this.changeOrdering(Ordering.choices[selectedOrdering]);
    }

    boolean isSearching = false;
    @FXML
    void btnSearchClicked() { // 点击了"搜寻"
        isSearching = !isSearching;
        if (isSearching) {
            btnSearch.setText("Done");
            this.products.forEach(product ->{
                if(product.getProductName().contains(txtSearch.getText()))switch (product.getCategory()) {
                    case DRINK:
                        this.containerDrinks.getChildren().clear();
                        this.containerDrinks.getChildren().add(product);
                        break;
                    case SNACK:
                        this.containerSnacks.getChildren().clear();
                        this.containerSnacks.getChildren().add(product);
                        break;
                    case STAPLE_FOODS:
                        this.containerStapleFoods.getChildren().clear();
                        this.containerStapleFoods.getChildren().add(product);
                        break;
                }
            });
        } else {
            btnSearch.setText("Search");
            this.deliverMenus();
        }
    }

    // 将按照popular排序的菜单扔到popularContainer里
    void initPopularContainer() {
        // 输出前10
        Iterator<Product> it = this.popularProducts.iterator();
        ObservableList
                <Node> m = this.containerPopular.getChildren();
        m.clear();
        for (int i = 0; i < 10 && it.hasNext(); i++) {
            m.add(it.next());
        }
    }

    // 将菜单分发到各个container里
    void deliverMenus() {
        // 清洁
        this.containerSnacks.getChildren().clear();
        this.containerDrinks.getChildren().clear();
        this.containerStapleFoods.getChildren().clear();

        // 扔到对应菜单
        this.products.forEach(product -> {
            switch (product.getCategory()) {
                case DRINK:
                    this.containerDrinks.getChildren().add(product);
                    break;
                case SNACK:
                    this.containerSnacks.getChildren().add(product);
                    break;
                case STAPLE_FOODS:
                    this.containerStapleFoods.getChildren().add(product);
                    break;
            }
        });
    }

    void addToMenu(Product product) {
        popularProducts.add(product);
        products.add(new Product(product));
    }
}
