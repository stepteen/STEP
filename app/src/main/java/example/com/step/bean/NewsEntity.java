package example.com.step.bean;
import java.io.Serializable;

/**
 * Created by HMH on 2016/12/10.
 */
public class NewsEntity implements Serializable{

    private static final long serialVersionUID=1L;
    private  Integer linkId;
    private String linkHref;//链接的地址
    private String linkText;//链接的标题
    private String linkPic;//图片SRC
    private  String time;//爬虫时间

    public NewsEntity() {
    }

    public NewsEntity(String linkHref, String linkText, String linkPic, String time) {
        this.linkHref = linkHref;
        this.linkText = linkText;
        this.linkPic = linkPic;
        this.time = time;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getLinkId() {
        return linkId;
    }

    public void setLinkId(Integer linkId) {
        this.linkId = linkId;
    }

    public String getLinkHref() {
        return linkHref;
    }

    public void setLinkHref(String linkHref) {
        this.linkHref = linkHref;
    }

    public String getLinkText() {
        return linkText;
    }

    public void setLinkText(String linkText) {
        this.linkText = linkText;
    }

    public String getLinkPic() {
        return linkPic;
    }

    public void setLinkPic(String linkPic) {
        this.linkPic = linkPic;
    }

    public String getTime() {return time;}

    public void setTime(String time) {
        time=time.substring(5,10);
        this.time = time;

    }
}

