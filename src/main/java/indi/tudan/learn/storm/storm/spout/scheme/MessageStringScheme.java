package indi.tudan.learn.storm.storm.spout.scheme;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import indi.tudan.learn.storm.utils.JSONUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.storm.kafka.StringScheme;
import org.apache.storm.tuple.Values;

import java.nio.ByteBuffer;
import java.util.List;

/**
 * 报文反序列化
 *
 * @author wangtan
 * @date 2020-04-14 16:56:41
 * @since 1.0
 */
@Slf4j
public class MessageStringScheme extends StringScheme {

    /**
     * 反序列化
     * <p>
     * 将消息字符串反序列化为消息对象
     *
     * @param bytes 数据
     * @return List<Object>
     * @date 2020-04-14 18:45:43
     */
    public List<Object> deserialize(ByteBuffer bytes) {
        return new Values(parseJson(deserializeString(bytes)));
    }

    /**
     * 反序列化为 Json
     *
     * @param string 字符串
     * @return JSONObject
     * @date 2020-04-14 18:46:18
     * @since 1.0
     */
    public JSONObject parseJson(String string) {
        if (JSONUtils.isValidJSON(string)) {
            return JSON.parseObject(string);
        } else {
            log.error("不是有效的 Json。");
            return null;
        }
    }

}
