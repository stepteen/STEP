package example.com.step.bean;

/**
 * Created by HMH on 2016/12/3.
 * 点赞实体
 */

public class Thumb {
    private Integer thumbId;
    private Integer synamicId;//发表的朋友圈的编号
    private  String thumberId;//点赞者ID
    private  String thumbHeadPicture;//点赞者头像
    private Integer thumbCount;//点赞数
    public Integer getThumbId() {
        return thumbId;
    }

    public void setThumbId(Integer thumbId) {
        this.thumbId = thumbId;
    }

    public Integer getSynamicId() {return synamicId;}

    public void setSynamicId(Integer synamicId) {this.synamicId = synamicId;}

    public String getThumberId() {return thumberId;}

    public void setThumberId(String thumberId) {this.thumberId = thumberId;}

    public String getThumbHeadPicture() {return thumbHeadPicture;}

    public void setThumbHeadPicture(String thumbHeadPicture) {this.thumbHeadPicture = thumbHeadPicture;}

    public Integer getThumbCount() {return thumbCount;}

    public void setThumbCount(Integer thumbCount) {this.thumbCount = thumbCount;}
}
