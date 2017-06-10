package example.com.step.bean;

import java.io.Serializable;

/**
 * Created by qinghua on 2016/11/19.
 */

public class MessageBean implements Serializable{
    private int id;
    private int mIndex;
    private int head_img_id;
    private String m_name;
    private String m_content;
    private String msg_date;
    private  String url;
    public  MessageBean()
    {

    }

    public MessageBean(int id, String m_name, String m_content, String url, int mIndex) {
        this.id = id;
        this.m_name = m_name;
        this.m_content = m_content;
        this.url = url;
        this.mIndex = mIndex;
    }

    public MessageBean(String m_name, String m_content, String msg_date, String url) {
        this.m_name = m_name;
        this.m_content = m_content;
        this.msg_date = msg_date;
        this.url=url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getmIndex() {
        return mIndex;
    }

    public void setmIndex(int mIndex) {
        this.mIndex = mIndex;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getHead_img_id() {
        return head_img_id;
    }

    public void setHead_img_id(int head_img_id) {
        this.head_img_id = head_img_id;
    }

    public String getM_name() {
        return m_name;
    }

    public void setM_name(String m_name) {
        this.m_name = m_name;
    }

    public String getM_content() {
        return m_content;
    }

    public void setM_content(String m_content) {
        this.m_content = m_content;
    }

    public String getMsg_date() {
        return msg_date;
    }

    public void setMsg_date(String msg_date) {
        this.msg_date = msg_date;
    }
}
