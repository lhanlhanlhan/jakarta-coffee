package login;

import com.alibaba.fastjson.JSONObject;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import mainwindow.MainWindowViewController;
import utils.Utils;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class AdminLoginViewController {
    @FXML
    private Label imgUsername;

    @FXML
    private TextField txtUsername;

    @FXML
    private Label imgPassword;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Button btnSignIn;

    private boolean isSignInSucceeded = false;

    public boolean isSignInSucceeded() {
        return isSignInSucceeded;
    }

    @FXML
    void initialize() {
        this.imgUsername.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.USER));
        this.imgPassword.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.KEY));
    }

    @FXML
    void btnSignInClicked() { //点击了“登录”
        this.btnSignIn.setDisable(true);
        Alert al = new Alert(Alert.AlertType.ERROR);

        al.titleProperty().set("Error");
        if (null == txtUsername.getText() || "".equals(txtUsername.getText())) {
            al.headerTextProperty().set("Please provide username.");
            al.show();
            this.btnSignIn.setDisable(false);
        }
        else if (null == txtPassword.getText() || "".equals(txtPassword.getText())) {
            al.headerTextProperty().set("Please provide password.");
            al.show();
            this.btnSignIn.setDisable(false);
        }
        else if(checkAdminLogin(txtUsername.getText(), txtPassword.getText())) {
            isSignInSucceeded = true;
            // 关闭本窗口
            ((Stage) btnSignIn.getScene().getWindow()).close();
        } else {
            this.btnSignIn.setDisable(false);
        }
    }

    // 向服务器发送登录用户名及密码，检查是否可登录
    private boolean checkAdminLogin(String username, String password) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", username);
        jsonObject.put("password", password);
        try {
            JSONObject ret = Utils.sendPost(Utils.getUsersURL("/admin-login"), jsonObject);
            if (ret.getInteger("code").equals(200)) {
                return true;
            } else {
                Utils.reportError("Login Error", "Wrong ID or password. Try again.");
                return false;
            }
        } catch (Exception e) {
            Utils.reportError("Network Error", "Server was unable to dial. Please check your Internet connection.");
            return false;
        }
    }
}
