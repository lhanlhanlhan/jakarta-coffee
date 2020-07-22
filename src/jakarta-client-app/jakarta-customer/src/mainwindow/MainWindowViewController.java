package mainwindow;

import cart.CartItem;
import cart.CartViewController;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
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

    @FXML
    private HBox searchBar;

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
        // 绑定事件
        this.doneButton.setOnAction(event -> btnDoneClicked());

        // 初始化下拉框
        this.choiceOrdering.getItems().addAll(Ordering.choicesStr);
        this.choiceOrdering.getSelectionModel().select(0);
        this.choiceOrdering.setOnAction(event -> choiceOrderingChanged());
        btnUser.setText(Utils.username);

        doRefreshMenu();
        // 获取购物车内容
        try {
            JSONObject result = Utils.sendGet(Utils.getCartsURL("/get-cart"));
            JSONArray data = result.getJSONArray("data");
            for (int i = 0; i < data.size(); i++){
                JSONObject obj = data.getJSONObject(i);
            // if (getProduct(obj.getString("item_id")) != null) { }
                Product prod = getProduct(obj.getString("item_id"));
                if (prod != null) {
                    CartItem cartItem = new CartItem(Utils.getCart(),
                            getProduct(obj.getString("item_id")),
                            obj.getInteger("item_count"));
                    Utils.getCart().addProduct(cartItem);
                }
            }
        } catch (Exception e) {
            Utils.reportError("The cart was unable to fetch.", "Exception: " + e);
            e.printStackTrace();
        }

        // 关闭loading
//        Utils.disableLoading();
    }

    @FXML
    void btnCartClicked() { // 点击了"查看购物车"
        Utils.showFXML(CartViewController.class.getResource("CartView.fxml"),
                "Cart", false, this.btnCart.getScene().getWindow());
    }
    @FXML
    void btnUserClicked() {
        Utils.showFXML(UserInfoController.class.getResource("UserInfoView.fxml"),
                "User Information", false, this.btnCart.getScene().getWindow());
        if (Utils.isReLoginNeeded) {
            // 重新登录
            Stage s = (Stage) this.btnUser.getScene().getWindow();
            s.close();
            Utils.showFXML(LoginViewController.class.getResource("LoginView.fxml"), "Login", false);
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
            result = Utils.sendGet(Utils.getMenusURL("/get-menu"));
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
                Product p = new Product(
                        c,
                        obj.getString("item_id"),
                        obj.getString("name"),
                        obj.getFloat("price"),
                        obj.getInteger("recent_sold"),
                        desc.equals("null") ? "" : desc,
                        obj.getInteger("stars")
                );
                if (obj.getInteger("in_stock") == 0) {
                    p.setOutOfStock(true);
                }
                addToMenu(p);
            } else {
                // 有图像！
                Product p = new Product(
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

    Button doneButton = new Button("Done");
    Button clearButton = new Button("x");
    Boolean isSearching = false;

    void btnDoneClicked() { // 点击了"完成"
        if (isSearching) {
            // 扔掉"完成"按钮
            this.searchBar.getChildren().remove(doneButton);
            this.searchBar.getChildren().remove(clearButton);
            isSearching = false;
            // 刷新正常菜单
            this.deliverMenus();
        }
    }

    void btnClearClicked() { // 点击了"清空"
        if (isSearching) {
            // 清空
            this.txtSearch.clear();
        }
    }

    @FXML
    void btnSearchClicked() { // 点击了"搜寻"
        if (!isSearching) {
            // 添加"完成"按钮
            this.searchBar.getChildren().add(doneButton);
            this.searchBar.getChildren().add(0, clearButton);
            isSearching = true;
        }

        // 开始搜寻
        this.containerDrinks.getChildren().clear();
        this.containerSnacks.getChildren().clear();
        this.containerStapleFoods.getChildren().clear();

        this.products.forEach(product -> {
            if (product.getProductName().contains(txtSearch.getText()))
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

        // 如果没有相关物品，则添加一个null
        if (this.containerDrinks.getChildren().isEmpty()) {
            this.containerDrinks.getChildren().add(Utils.newEmptyIndicator());
        }
        if (this.containerSnacks.getChildren().isEmpty()) {
            this.containerSnacks.getChildren().add(Utils.newEmptyIndicator());
        }
        if (this.containerStapleFoods.getChildren().isEmpty()) {
            this.containerStapleFoods.getChildren().add(Utils.newEmptyIndicator());
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

    // 从当前菜单中获取一个product checked
    private Product getProduct(String item_id) {
        for (Product p: this.products) {
            if (p.getProductID().equals(item_id)) {
                return p;
            }
        }
        return null;
    }
}
