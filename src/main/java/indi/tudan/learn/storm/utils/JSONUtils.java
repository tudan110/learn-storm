package indi.tudan.learn.storm.utils;

import com.alibaba.fastjson.JSON;

/**
 * Json 工具类
 *
 * @author wangtan
 * @date 2020-04-14 18:44:04
 * @since 1.0
 */
public class JSONUtils {

    /**
     * 是否是有效的 Json 字符串
     *
     * @param string 字符串
     * @return boolean
     * @date 2020-04-14 18:43:59
     * @since 1.0
     */
    public static boolean isValidJSON(String string) {
        try {
            JSON.parse(string);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
