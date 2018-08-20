package x.x.com.oneandroidtest1;

public class MessageBean {

    private Boolean fromOthers;//信息的来源，true发过来，false发过去
    private String name;
    private Long time;
    private String content;
    private String headImgUrl;
    private boolean isSendSuccessful = true;
    private String messageType = TEXT;//文本(text);连接(url);音频(voice);视频(video);图片(image);图文(news)

    public static final String TEXT = "text";

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public boolean isSendSuccessful() {
        return isSendSuccessful;
    }

    public void setSendSuccessful(boolean sendSuccessful) {
        isSendSuccessful = sendSuccessful;
    }


    public Boolean getFromOthers() {
        return fromOthers;
    }

    public void setFromOthers(Boolean fromOthers) {
        this.fromOthers = fromOthers;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        name = name;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }
}
