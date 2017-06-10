package example.com.step.Result;

import android.util.Log;
import com.alibaba.fastjson.JSON;

/**
 * Created by qinghua on 2016/7/29.
 * * @Description: 服务端返回结果集相关
 */
public class ResultDataUtils {
    /**
     * 解析返回值
     * @param result
     * @returnu
     */
    public static ResultData paraResult(String result){
        return JSON.parseObject(result, ResultData.class);
    }


    /**
     * 判断返回的数据是不是
     *
     * @param resultData
     * @return
     */
    public static boolean isError(ResultData resultData) {

        if (resultData.getStatus().equals(ResultData.ERROR)) {
            return true;
        }
        return false;
    }

    /**
     * 打印错误信息
     * @param TAG
     * @param errorCode
     * @param errorMsg
     */
    public static void printErrorInfo(String TAG, int errorCode, String errorMsg) {
        Log.e(TAG, "ErrorCode:" + errorCode + ",ErrorMsg:" + errorMsg);
    }
}
