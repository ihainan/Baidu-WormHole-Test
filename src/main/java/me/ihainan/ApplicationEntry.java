package me.ihainan;

import me.ihainan.utils.NetworkUtil;
import me.ihainan.utils.Wormhole;

/**
 * 程序入口
 */
public class ApplicationEntry {
    public static void main(String[] args) {
        Wormhole wormhole = new Wormhole("192.168.31.210", NetworkUtil.PORT_ONE);
        System.out.println("Search Box Info: " + wormhole.getSearchBoxInfo());
        System.out.println("App List: " + wormhole.getAppList());
        System.out.println("Geo Location: " + wormhole.getGeoLocation());
        System.out.println("APN: " + wormhole.getAPN());
        System.out.println("Package Info: " + wormhole.getPackageInfo());
        System.out.println("Service Info: " + wormhole.getServiceInfo());
        System.out.println("IMEI: " + wormhole.getIMEI());
    }
}
