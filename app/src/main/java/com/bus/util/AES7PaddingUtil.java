package com.bus.util;


import android.util.Base64;

import org.json.JSONObject;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.UUID;

import framework.util.Loger;

public class AES7PaddingUtil {

//    public static Map<String, String> toAES7Padding(Map<String, String> paramMap) {
//        paramMap.put("nonce", UUID.randomUUID().toString());
//        String data = JSON.toJSONString(paramMap);
//        Object localObject = Base64.encodeToString(new AES().encrypt(data.getBytes(), DataConstant.AESKey.getBytes()), 0);
//        try {
//            localObject = URLEncoder.encode((String) localObject, DataConstant.charSet);
//            paramMap = (Map<String, String>) localObject;
//        } catch (UnsupportedEncodingException localUnsupportedEncodingException) {
//            localUnsupportedEncodingException.printStackTrace();
//        }
//        localObject = new HashMap();
//        ((Map) localObject).put("a", "Android");
//        ((Map) localObject).put("b", paramMap);
//        return (Map<String, String>) localObject;
//    }

    public static JSONObject toAES7Padding(JSONObject paramMap) {
        JSONObject localObject = new JSONObject();
        try {
            paramMap.put("nonce", UUID.randomUUID().toString());
            String data = paramMap.toString();
            Loger.INSTANCE.w("paramMap " + data);
            data = Base64.encodeToString(new AES().encrypt(data.getBytes(), DataConstant.AESKey.getBytes()), 0);
            Loger.INSTANCE.w("outPut 1 " + data);
            localObject.put("a", "Android");
            data = URLEncoder.encode(data, DataConstant.charSet);
            Loger.INSTANCE.w("outPut 2 " + data);
            localObject.put("b", data);
            String re = URLDecoder.decode(data, DataConstant.charSet);
            Loger.INSTANCE.w("outPut -r1 " + re);
            byte[] aesByte = Base64.decode(re.getBytes("US-ASCII"), 0);
            byte[] paramMapByte = new AES().decrypt(aesByte, DataConstant.AESKey.getBytes());
            String paramMapRe = new String(paramMapByte, Charset.defaultCharset());
            Loger.INSTANCE.w("outPut -r2 " + paramMapRe);

            //return new String(encode(input, flags), "US-ASCII");

            Loger.INSTANCE.w("paramMap " + localObject.toString());

        } catch (Exception localUnsupportedEncodingException) {
            localUnsupportedEncodingException.printStackTrace();
        }
        return localObject;
    }

    public static void reverse(String data) {
        try {
            Loger.INSTANCE.e("###########" );
            Loger.INSTANCE.i("paramMap " + data);
            String re = URLDecoder.decode(data, DataConstant.charSet);
            Loger.INSTANCE.w("outPut -r1 " + re);
            byte[] aesByte = Base64.decode(re.getBytes("US-ASCII"), 0);
            byte[] paramMapByte = new AES().decrypt(aesByte, DataConstant.AESKey.getBytes());
            String paramMapRe = new String(paramMapByte, Charset.defaultCharset());
            Loger.INSTANCE.e("outPut -r2 " + paramMapRe);
            Loger.INSTANCE.e("###########" );
            Loger.INSTANCE.e("" );
            Loger.INSTANCE.e("" );

        } catch (Exception localUnsupportedEncodingException) {
            localUnsupportedEncodingException.printStackTrace();
        }
    }
}
