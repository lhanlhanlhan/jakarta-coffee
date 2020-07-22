package cart;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;

public class Cart {
    // 购物车容器
    private final HashSet<CartItem> cart;
    // 选中的产品
    private final LinkedHashSet<CartItem> selected;
    // 总价（选中商品）
    private float totalPrice;
    // 所属controller
    private CartViewController controller;

    public Cart() {
        this.cart = new HashSet<>();
        this.selected = new LinkedHashSet<>();
        this.controller = null;
    }

    // 设置controller
    public void setController(CartViewController cc) {
        this.controller = cc;
    }

    public CartViewController getController() {
        return this.controller;
    }

    // 添加货物
    public void addProduct(CartItem p) {
        // 先查找是否已经存在商品了
        for (CartItem ci : cart) {
            if (ci.getProductNumber() == p.getProductNumber()) {
                ci.setCount(ci.getCount() + 1);
                return;
            }
        }

        this.cart.add(p);
        if (p.isChecked()) {
            this.selected.add(p);
        }
    }

    // 删除某个货物
    public void deleteProduct(CartItem ci) {
        ci.setChecked(false);
        this.cart.remove(ci); // 从购物车移除
        this.controller.deleteFromContainer(ci); // 从界面容器删除
        this.selected.remove(ci);             // 从选中列表中移除
        this.totalPrice -= ci.getSubTotalPrice();
    }

    // 删除选定的货物
    public void deleteSelectedProduct() {
        Iterator<CartItem> i = this.selected.iterator();
        CartItem ci;
        while (i.hasNext()) {
            ci = i.next();
            ci.setChecked(false);
            this.cart.remove(ci); // 从购物车移除
            this.controller.deleteFromContainer(ci); // 从界面容器删除
            i.remove();             // 从选中列表中移除
            this.totalPrice -= ci.getSubTotalPrice();
        }
    }

    // 选中或不选中货物
    public void selectOrDeselectProduct(CartItem product) {
        if (product.isChecked()) { // 切换不选中
            this.selected.remove(product);
            product.setChecked(false);
            // 更新价格
            this.totalPrice -= product.getSubTotalPrice();
        } else { // 切换选中
            this.selected.add(product);
            product.setChecked(true);
            // 更新价格
            this.totalPrice += product.getSubTotalPrice();
        }
        // 通知主界面更新数据
        this.controller.updateData();
        this.controller.setSelectedSomething();
    }

    // 获取购物车容器
    public HashSet<CartItem> getProducts() {
        return cart;
    }
    // 获取选中之商品容器
    public LinkedHashSet<CartItem> getSelectedProducts() {
        return selected;
    }

    // 购物车全选
    public void selectAll() {
        this.totalPrice = 0;
        for (CartItem p : cart) {
            this.selected.add(p);
            p.setChecked(true);
            this.totalPrice += p.getSubTotalPrice();
        }
    }

    // 获取总价
    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float price) {
        this.totalPrice = price;
        this.controller.updateData();
    }

    // 各种属性
    public boolean isCartEmpty() {
        return this.cart.isEmpty();
    }

    public boolean isSelectedSomething() {
        return !this.selected.isEmpty();
    }
}
