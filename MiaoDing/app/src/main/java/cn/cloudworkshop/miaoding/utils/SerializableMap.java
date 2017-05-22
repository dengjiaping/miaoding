package cn.cloudworkshop.miaoding.utils;

import java.io.Serializable;
import java.util.Map;

/**
 * Author：Libin on 2016/10/28 11:41
 * Email：1993911441@qq.com
 * Describe：Map序列化，bundle传值
 */
public class SerializableMap implements Serializable {
    private Map<Integer, Integer> IntegerMap;
    private Map<Integer, String> StringMap;


    public Map<Integer, Integer> getIntegerMap() {
        return IntegerMap;
    }

    public void setIntegerMap(Map<Integer, Integer> integerMap) {
        IntegerMap = integerMap;
    }

    public Map<Integer, String> getStringMap() {
        return StringMap;
    }

    public void setStringMap(Map<Integer, String> stringMap) {
        StringMap = stringMap;
    }
}
