
package org.harvest.crawler.bean;

public class BaseMessage {
    /**
     * 消息id
     */
    private int id;

    /**
     * 消息下发时间
     */
    private long time;

    /**
     * 消息类型
     */
    private int type;

    /**
     * 发送者唯一标识
     */
    private String tag;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;

        if (getClass() != obj.getClass())
            return false;

        BaseMessage baseMessage = (BaseMessage) obj;
        if (baseMessage.id != id) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return "BaseMessage [id=" + id + ", time=" + time + ", type=" + type + ", tag=" + tag + "]";
    }
}
