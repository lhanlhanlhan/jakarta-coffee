package login;

import com.alibaba.fastjson.JSONArray;
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

public class LoginViewController {
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

    @FXML
    void initialize() {
        this.imgUsername.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.USER));
        this.imgPassword.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.KEY));
    }

    @FXML
    void btnSignInClicked() {//点击了“登录”
        Alert al = new Alert(Alert.AlertType.ERROR);

        al.titleProperty().set("Error");
        if (null == txtUsername.getText() || "".equals(txtUsername.getText())) {
            al.headerTextProperty().set("Please provide username.");
            al.show();
        }
        else if (null == txtPassword.getText() || "".equals(txtPassword.getText())) {
            al.headerTextProperty().set("Please provide password.");
            al.show();
        }
        else if(checkLogin(txtUsername.getText(), txtPassword.getText())) {
            // 登陆成功
            Stage s = Utils.showFXML(MainWindowViewController.class.getResource("MainWindowView.fxml"), "Jakarta Coffee House");
            Utils.isReLoginNeeded = false;
            // 设置新窗口的关闭事件
            s.setOnCloseRequest(event -> {
                event.consume();
                Utils.ConfirmType ct =  Utils.askUser("Quit Jakarta Coffee House", "Are you sure to leave Jakarta Menu?");
                if (ct == Utils.ConfirmType.YES) {
                    s.close();
                }
            });

            // 关闭本窗口
            ((Stage) btnSignIn.getScene().getWindow()).close();
        }
        else {
            Utils.reportError("Login Error", "Wrong ID or password. Try again.");
        }
    }
    @FXML
    void btnSignUpClicked() {
        try {
            URI uri = new URI("http://reg.jakarta.han-li.cn:9898");
            Desktop.getDesktop().browse(uri);
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnForgotClicked() {
        try {
            URI uri = new URI("http://forgot.jakarta.han-li.cn:9898");
            Desktop.getDesktop().browse(uri);
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
    }

    // 向服务器发送登录用户名及密码，检查是否可登录
    private boolean checkLogin(String username, String password) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", username);
        jsonObject.put("password", password);
        try {
            JSONObject ret = Utils.sendPost(Utils.getUsersURL("/client-login"), jsonObject);
            if (ret.getInteger("code").equals(200)) {
                // 保存token
                Utils.token = ret.getJSONObject("data").getString("token");
                Utils.username=username;
                return true;
            }
        } catch (Exception e) {
            Utils.reportError("Network Error", "Server was unable to dial. Please check your Internet connection.");
        }
        return false;
    }
}
