package example.com.step.Result;

/**
 * Created by qinghua on 2016/7/29.
 */
public class ResultData {
    public static final String SUCCESS = "success";
    public static final String ERROR = "error";

    /**
     * 结果集状态
     */
    private String status;
    /**
     * 错误码
     */
    private String errorCode;
    /**
     * 结果集数据
     */
    private Object data;
    /**
     * accesstoken
     */
    private String accessToken;

    /**
     * 客户端系统配置
     */
    private Object config;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Object getConfig() {
        return config;
    }

    public void setConfig(Object config) {
        this.config = config;
    }
}
