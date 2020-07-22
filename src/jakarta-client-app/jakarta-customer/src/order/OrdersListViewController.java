package order;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import utils.Utils;

import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

public class OrdersListViewController {

    @FXML
    private Label lblUsername;

    @FXML
    private VBox ordersHistoryContainer;

    private final List<Order> orders;

    @FXML
    void initialize() {
        ObservableList<Node> items = this.ordersHistoryContainer.getChildren();
        // 加入可视化列表中
        for (int i = orders.size() - 1; i >= 0; i--) {
            items.add(new OrdersListItem(orders.get(i)));
        }
        // 用户名更新
        this.lblUsername.setText(Utils.username);
    }

    public OrdersListViewController(List<Order> orders) {
        this.orders = orders;
    }
}
