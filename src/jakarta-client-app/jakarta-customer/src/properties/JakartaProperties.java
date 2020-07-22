package properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * properties文件读取类
 *
 */
public class JakartaProperties {

    public static final Map<String, String> prop = getProperties();

    public static String getServerAddr() {
        return prop.get("gate_server");
    }

    // 获取jar包的目录
    private static String getPath() {
        String path = JakartaProperties.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        if (System.getProperty("os.name").contains("dows")) {
            path = path.substring(1);
        }
        if (path.contains("jar")) {
            path = path.substring(0,path.lastIndexOf("."));
            return path.substring(0,path.lastIndexOf("/"));
        }
        return path.replace("target/classes/", "");
    }

    private static Map<String, String> getProperties() {
        Properties properties = new Properties();//获取Properties实例
        Map<String, String> prop = new HashMap<>();
        try {
            InputStream inStream = new FileInputStream(new File(getPath() + File.separator + "jakarta.properties")); //获取配置文件输入流
            properties.load(inStream); //载入输入流
            Enumeration<?> enumeration = properties.propertyNames(); //取得配置文件里所有的key值
            while (enumeration.hasMoreElements()){
                String key = (String) enumeration.nextElement();
                String value = properties.getProperty(key);
                prop.put(key, value);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return prop;
        }
        return prop;
    }
}
