package cart;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class PayViewController {
    @FXML
    private Button btnCancelBuy;
    @FXML
    private Button btnBuySuc;
    @FXML
    void btnBuySucClicked(){
        //TODO - 向服务器发送订单
        Stage stage=(Stage)btnBuySuc.getScene().getWindow();
        stage.close();
    }
    @FXML
    void btnCancelBuyClicked(){
        Stage stage=(Stage)btnCancelBuy.getScene().getWindow();
        stage.close();
    }
}
