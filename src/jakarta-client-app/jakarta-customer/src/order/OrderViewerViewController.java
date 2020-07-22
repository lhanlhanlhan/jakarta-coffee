package order;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import utils.Utils;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class OrderViewerViewController {
    @FXML
    private VBox orderItemContainer;

    @FXML
    private Label lblAddFee;

    @FXML
    private Label lblTotalPrice;

    @FXML
    private Label lblCreatedTime;

    @FXML
    private Label lblOrderID;

    private final Order order;

    @FXML
    void btnCustServiceClicked() {

    }

    @FXML
    void btnWrongClicked() {

    }

    @FXML
    void initialize() {
        // 遍历整个order加入容器
        ObservableList<Node> children = this.orderItemContainer.getChildren();
        order.getItems().forEach(cartItem -> children.add(new OrderItem(cartItem)));
        // 数字等更新
        this.lblAddFee.setText(Utils.getStdCash(order.getAddFee()));
        this.lblTotalPrice.setText(Utils.getStdCash(order.getPrice()));
        this.lblOrderID.setText(order.order_id == null ? "N/A" : order.order_id);
        this.lblCreatedTime.setText(Utils.getStdDate(order.timestamp));
    }

    // 创建一个显示整个order的视图
    public OrderViewerViewController(Order order) {
        this.order = order;
    }
}