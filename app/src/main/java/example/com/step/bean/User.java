package example.com.step.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: ZQH
 * @Date: 2016/11/10 10：10
 * @Description: 用户信息的实体
 */
public class User implements Serializable {
    private Integer userId;
    private String userHeadPicture;
    private String phoneNum;
    private String userPwd;
    private String userName;
    private Integer userAge;
    private String userSex;
    private String userHeight;
    private Date createTime;
    private String channelId;
    private String userWeight;
    public User() {
    }

    public User(String phoneNum, String userPwd, String userName, Integer userAge, String userSex, String userHeight, Date createTime, String channelId, String userWeight) {
        this.phoneNum = phoneNum;
        this.userPwd = userPwd;
        this.userName = userName;
        this.userAge = userAge;
        this.userSex = userSex;
        this.userHeight = userHeight;
        this.createTime = createTime;
        this.channelId = channelId;
        this.userWeight = userWeight;
    }

    public Integer getUserId() {return userId;}

    public void setUserId(Integer userId) {this.userId = userId;}

    public String getUserHeadPicture() {return userHeadPicture;}

    public void setUserHeadPicture(String userHeadPicture) {this.userHeadPicture = userHeadPicture;}

    public String getPhoneNum() {return phoneNum;}

    public void setPhoneNum(String phoneNum) {this.phoneNum = phoneNum;}

    public String getUserPwd() {return userPwd;}

    public void setUserPwd(String userPwd) {this.userPwd = userPwd;}

    public String getUserName() {return userName;}

    public void setUserName(String userName) {this.userName = userName;}

    public Integer getUserAge() {return userAge;}

    public void setUserAge(Integer userAge) {this.userAge = userAge;}

    public String getUserSex() {return userSex;}

    public void setUserSex(String userSex) {this.userSex = userSex;}

    public String getUserHeight() {return userHeight;}

    public void setUserHeight(String userHeight) {this.userHeight = userHeight;}

    public Date getCreateTime() {return createTime;}

    public void setCreateTime(Date createTime) {this.createTime = createTime;}

    public String getChannelId() {return channelId;}

    public void setChannelId(String channelId) {this.channelId = channelId;}

    public String getUserWeight() {return userWeight;}

    public void setUserWeight(String userWeight) {this.userWeight = userWeight;}
}
