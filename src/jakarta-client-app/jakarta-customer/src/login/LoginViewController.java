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
        this.btnSignIn.setText("Please Wait...");
        this.btnSignIn.setDisable(true);
        Alert al = new Alert(Alert.AlertType.ERROR);

        al.titleProperty().set("Error");
        if (null == txtUsername.getText() || "".equals(txtUsername.getText())) {
            al.headerTextProperty().set("Please provide username.");
            al.show();
            this.btnSignIn.setText("Login");
            this.btnSignIn.setDisable(false);
        }
        else if (null == txtPassword.getText() || "".equals(txtPassword.getText())) {
            al.headerTextProperty().set("Please provide password.");
            al.show();
            this.btnSignIn.setText("Login");
            this.btnSignIn.setDisable(false);
        }
        // 显示loading
        if (checkLogin(txtUsername.getText(), txtPassword.getText())) {
            // 登陆成功
            Stage s = Utils.showFXML(MainWindowViewController.class.getResource("MainWindowView.fxml"), "Jakarta Coffee House", false);
            Utils.isReLoginNeeded = false;
            // 设置新窗口的关闭事件
            assert s != null;
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
            this.btnSignIn.setText("Login");
            this.btnSignIn.setDisable(false);
        }
    }
    @FXML
    void btnSignUpClicked() {
        Utils.reportInfo("Register", "Your default computer browser will be opened and directed to the Jakarta ID register page.");
        try {
            URI uri = new URI("http://jakarta.han-li.cn:9001/#/register");
            Desktop.getDesktop().browse(uri);
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnForgotClicked() {
        Utils.reportInfo("Forgot Password",
                "If you forgot your Jakarta ID username and/or password, please contact one of our staffs as soon as possible, or contact the Jakarta Customer Service at (852) 2333 2555.");
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
                Utils.username = username;
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
