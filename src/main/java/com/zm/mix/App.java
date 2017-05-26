class App {

    static String GCIDPREFIX = "111111111122222222223333333333000000000";
    static String HIT = "HIT";
    static String MISS = "MISS";
    static String LOGFORMAT = "223.75.1.191 baishancloud.mars.baofeng.net video/mp4 [09/Apr/2017:05:00:00 +0800] \"GET http://baishancloud.mars.baofeng.net/0A/1B/%s?bfKey=94d7075c40ef0ba36651df8dd9b96bb6&bfTime=1491684689 HTTP/1.1\" 206 45106 0 %s - Mozilla/5.0+(Windows+NT+10.0;+WOW64)+AppleWebKit/537.36+(KHTML,+like+Gecko)+Chrome/51.0.2704.103+Safari/537.36\r\n";

    public static void main(String[] args) {
        int[] hits = {8, 2 ,3 ,6, 1, 7, 9, 2, 4, 9, 3, 2};
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < 10; i++) {
            sb.append(genLog(GCIDPREFIX + i, hits[i]));
        }
        System.out.println(sb.toString());
        System.out.println(genLog("1111111111222222222233333333330000000010", 10));
    }
    public static String genLog(String gcid, int hit) {
        StringBuffer ret = new StringBuffer();
        for(int i = 0; i < hit; i ++) {
            ret.append(String.format(LOGFORMAT, gcid, HIT));
        }
        for(int i = hit; i < 10; i++) {
            ret.append(String.format(LOGFORMAT, gcid, MISS));
        }
        return ret.toString();
    }
}