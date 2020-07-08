package utils;

import cart.Cart;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import com.alibaba.fastjson.JSON;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
public class Utils {

    private static final Cart _cart = new Cart();

    public static Cart getCart() {
        return _cart;
    }

    // 公共信息及标志位
    public static String token;
    public static String username;
    public static String email;
    public static String tel;
    public static boolean isReLoginNeeded = false;
    public static boolean isProfileChanged = false;

    static public void reportError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    static public void reportInfo(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public enum ConfirmType {
        YES, NO, NOT_APPLICABLE
    }

    static public ConfirmType askUser(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm");
        alert.setHeaderText(title);
        alert.setContentText(content);
        Optional<ButtonType> _buttonType = alert.showAndWait();
        if (!_buttonType.isPresent()) {
            return ConfirmType.NOT_APPLICABLE;
        } else if (_buttonType.get().getButtonData().equals(ButtonBar.ButtonData.OK_DONE)) {
            return ConfirmType.YES;
        } else {
            return ConfirmType.NO;
        }
    }

    static public Stage showFXML(URL fxmlPath, String title) {
        Stage s = null;
        try {
            s = insideShow(fxmlPath, title);
            s.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return s;
    }

    static public void showAndWaitFXML(URL fxmlPath, String title) {
        try {
            Stage s = insideShow(fxmlPath, title);
            s.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // *** 内部函数 ***
    private static Stage insideShow(URL fxmlPath, String title) throws IOException {
        Stage s = new Stage();
        Parent root = FXMLLoader.load(fxmlPath);
        Scene scene = new Scene(root);
        s.setScene(scene);
        scene.getStylesheets().add(Utils.class.getResource("../resource/bootstrap3.css").toExternalForm());
        s.setTitle(title);
        return s;
    }

    // TODO - 获取服务器地址 - 可能存放在配置文件中！
    private static String getServerURL() {
        return "http://127.0.0.1:8080";
    }

    // 获取和用户相关的接口地址
    public static String getUsersURL(String inter) {
        return getServerURL() + "/users-api" + inter;
    }

    // 获取和菜单相关的接口地址
    public static String getMenusURL(String inter) {
        return getServerURL() + "/menus-api" + inter;
    }

    public static String sendGet(String url, String param) {
        StringBuilder result = new StringBuilder();
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result.toString();
    }

    public static JSONObject sendGet(String url) throws IOException {
        StringBuilder result = new StringBuilder();
        BufferedReader in = null;
        JSONObject jsonObject = null;
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            if (Utils.token != null) {
                connection.setRequestProperty("X-Token", Utils.token);
            }
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
            jsonObject = JSONObject.parseObject(result.toString());
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            throw e;
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return jsonObject;
    }

    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result.toString();
    }

    public static JSONObject sendPost(String url, JSON json) throws Exception {
        PrintWriter out = null;
        BufferedReader in = null;
        JSONObject jsonObject;
        StringBuilder result = new StringBuilder();
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            HttpURLConnection conn = (HttpURLConnection)realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestMethod("POST");
            // 发送POST请求必须设置下面的属性
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            // 设置请求属性
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            if (Utils.token != null) {
                conn.setRequestProperty("X-Token", Utils.token);
            }
            conn.connect();
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数【数据体！】
            out.print(json.toJSONString());
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
            // 将返回结果转换为字符串？
            jsonObject = JSONObject.parseObject(result.toString());
        } catch (Exception e) {
            throw new RuntimeException("远程通路异常 "+e.toString());
        }
        // 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return jsonObject;
    }

}
