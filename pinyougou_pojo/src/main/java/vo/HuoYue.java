package vo;

import cn.itcast.core.pojo.user.User;

import java.util.List;

public class HuoYue {
    private List<User> huoList;
    private List<User> buList;
    private int huoCount;
    private int buCount;

    public List<User> getHuoList() {
        return huoList;
    }

    public void setHuoList(List<User> huoList) {
        this.huoList = huoList;
    }

    public List<User> getBuList() {
        return buList;
    }

    public void setBuList(List<User> buList) {
        this.buList = buList;
    }

    public int getHuoCount() {
        return huoCount;
    }

    public void setHuoCount(int huoCount) {
        this.huoCount = huoCount;
    }

    public int getBuCount() {
        return buCount;
    }

    public void setBuCount(int buCount) {
        this.buCount = buCount;
    }
}
