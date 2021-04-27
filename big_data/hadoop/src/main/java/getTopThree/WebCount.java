package getTopThree;

public class WebCount implements Comparable<WebCount> {
    private int num;
    private String web;

    public WebCount(int num, String web) {
        this.num = num;
        this.web = web;
    }

    public WebCount() {
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public int compareTo(WebCount o) {

        return o.getNum()-this.num;
        //升序排序：当前对象的值-目标对象的值
        //降序排列：目标对象的值-当前对象的值，或者是在升序的情况下加个负号 -(this.age - o.age)
    }
}
