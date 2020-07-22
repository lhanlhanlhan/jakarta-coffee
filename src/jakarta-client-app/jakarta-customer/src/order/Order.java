package order;

import cart.CartItem;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import utils.Utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class Order {
    // 是否支付完成（true = 支付完成）
    public boolean approved = false;

    // 订单号
    public String order_id = null;

    // 商品清单及价钱
    private final ArrayList<CartItem> items;
    private final float addFee;
    private float price = 0;

    // 时间戳
    public final long timestamp;

    // 创建订单
    public Order(Collection<CartItem> item, long timestamp, float addFee, String id) {
        this.items = new ArrayList<>();
        this.order_id = id;
        this.timestamp = timestamp;
        for (CartItem ci: item) {
            this.items.add(ci);
            this.price += ci.getSubTotalPrice();
        }
        this.addFee = addFee;
    }

    // 获取清单
    public Collection<CartItem> getItems() {
        return Collections.unmodifiableCollection(this.items);
    }

    // 获取总价
    public float getPrice() {
        return price + addFee;
    }

    // 获取额外价格
    public float getAddFee() {
        return addFee;
    }

    // TODO - 提交订单到服务器的接口
    public boolean submit() {
        JSONObject order = new JSONObject();
        order.put("customer_name", Utils.username);

        JSONArray itemArr = new JSONArray();
        for (CartItem ci : items) {
            JSONObject item = new JSONObject();
            item.put("item_id", ci.getProductNumber());
            item.put("item_count", ci.getCount());
            item.put("price", ci.getPrice());

            itemArr.add(item);
        }

        order.put("items", itemArr);

        try {
            JSONObject ret = Utils.sendPost(Utils.getOrdersURL("/new-order"), order);
            if (ret.getInteger("code").equals(200)) {
                //Utils.token = ret.getJSONObject("data").getString("token");
                this.approved = true;
                return true;
            } else {
                Utils.reportError("Order Error", "There is something wrong while handling your order. Some products in your order were successfully admitted, but others not.");
                return false;
            }
        } catch (Exception e) {
            Utils.reportError("Network Error", "Server was unable to dial. Please check your Internet connection.");
            return false;
        }
    }
}
