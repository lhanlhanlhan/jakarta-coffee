package order;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import utils.Utils;

public class OrdersListItem extends HBox {
    @FXML
    private Label lblOrderCat;

    @FXML
    private Label lblDate;

    @FXML
    private Label lblCurrencyMark;

    @FXML
    private Label lblTotalPrice;

    @FXML
    private Button btnGo;

    // 映射到的order对象
    private final Order order;

    @FXML
    void btnGoClicked() {
        // 打开检视订单详情页面
        OrderViewerViewController ovvc = new OrderViewerViewController(order);
        Utils.showFXML(OrderViewerViewController.class.getResource("OrderViewerView.fxml"),
                "Order Details",
                ovvc,
                null);
    }

    OrdersListItem(Order order) {
        this.order = order;
        Utils.loadWidget(getClass().getResource("OrdersListItemCard.fxml"), this);
    }


    @FXML
    void initialize() {
        // 设置按钮
        this.btnGo.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.EYE));
        // 设置各种label
        this.lblCurrencyMark.setText("$");
        this.lblOrderCat.setText("Eat-In Order");
        this.lblDate.setText(Utils.getStdDate(this.order.timestamp));
        this.lblTotalPrice.setText(Utils.getStdCash(this.order.getPrice()));
    }
}
