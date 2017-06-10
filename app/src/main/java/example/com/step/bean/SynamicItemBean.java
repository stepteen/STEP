package example.com.step.bean;

/**
 * Created by qinghua on 2016/10/6.
 */

public class SynamicItemBean {
    private int SynamicNo;//动态编号
    private String headpicture;//头像字符串
    private String usename;//发布者
    private String m_duration;//时长,是指在多少时间之前发布的动态，要你后台自己输入
    private String pictureString;//图片的字符串
    private String str_content;//文本内容
    private String commentNub;//评论数量
    private String likeNub;//点赞人数
    public SynamicItemBean()
    {

    }
    public SynamicItemBean(String usename,String commentNub,String likeNub)
    {
        this.usename=usename;
        this.commentNub=commentNub;
        this.likeNub=likeNub;
    }


    public String getUsename() {
        return usename;
    }

    public void setUsename(String usename) {
        this.usename = usename;
    }

    public String getM_duration() {
        return m_duration;
    }

    public void setM_duration(String m_duration) {
        this.m_duration = m_duration;
    }

    public String getPictureString() {
        return pictureString;
    }

    public void setPictureString(String pictureString) {
        this.pictureString = pictureString;
    }

    public String getHeadpicture() {
        return headpicture;
    }

    public void setHeadpicture(String headpicture) {
        this.headpicture = headpicture;
    }

    public String getStr_content() {
        return str_content;
    }

    public void setStr_content(String str_content) {
        this.str_content = str_content;
    }

    public String getCommentNub() {
        return commentNub;
    }

    public void setCommentNub(String commentNub) {
        this.commentNub = commentNub;
    }

    public String getLikeNub() {
        return likeNub;
    }

    public void setLikeNub(String likeNub) {
        this.likeNub = likeNub;
    }
}
