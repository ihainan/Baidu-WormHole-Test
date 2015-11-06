package me.ihainan.utils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * 网络工具
 */
public class NetworkUtil {
    public static class IPv4 {
        int baseIPNumeric;  // IPv4 地址的数值表示
        int netmaskNumeric; // 子网掩码的数值表示

        /**
         * @param symbolicIP IP 地址
         * @param netmask    子网掩码
         * @throws NumberFormatException IP 地址或者子网掩码不符合规范
         */
        public IPv4(String symbolicIP, String netmask) throws NumberFormatException {
            /* 分割 IP */
            String[] st = symbolicIP.split("\\.");

            /* 检测 IP 地址是否符合规范并将地址转换成整形数值 */
            if (st.length != 4) {
                throw new NumberFormatException("Invalid IP address: " + symbolicIP);
            }
            int i = 24;
            baseIPNumeric = 0;
            for (int n = 0; n < st.length; n++) {
                int value = Integer.parseInt(st[n]);
                if (value != (value & 0xff)) {
                    throw new NumberFormatException("Invalid IP address: " + symbolicIP);
                }
                baseIPNumeric += value << i;
                i -= 8;
            }

            /* 子网掩码 */
            st = netmask.split("\\.");

            /* 检测子网掩码是否符合规范并将地址转换成整形数值 */
            if (st.length != 4) {
                throw new NumberFormatException("Invalid netmask address: "
                        + netmask);
            }

            i = 24;
            netmaskNumeric = 0;
            if (Integer.parseInt(st[0]) < 255) {
                throw new NumberFormatException(
                        "The first byte of netmask can not be less than 255");
            }

            for (int n = 0; n < st.length; n++) {
                int value = Integer.parseInt(st[n]);
                if (value != (value & 0xff)) {
                    throw new NumberFormatException("Invalid netmask address: " + netmask);
                }
                netmaskNumeric += value << i;
                i -= 8;
            }

            /**
             * 检测子网掩码是否存在夹在 1 中间的 0
             */
            boolean encounteredOne = false;
            int ourMaskBitPattern = 1;
            for (i = 0; i < 32; i++) {
                if ((netmaskNumeric & ourMaskBitPattern) != 0) {
                    encounteredOne = true; // the bit is 1
                } else { // the bit is 0
                    if (encounteredOne == true)
                        throw new NumberFormatException("Invalid netmask: " + netmask + " (bit " + (i + 1) + ")");
                }
                ourMaskBitPattern = ourMaskBitPattern << 1;
            }
        }

        /**
         * 获取原始的字符串 IP 地址
         *
         * @return 原始的字符串 IP 地址
         */
        public String getIP() {
            return convertNumericIpToSymbolic(baseIPNumeric);
        }

        /**
         * 整形数值转换为字符串 IP 地址
         *
         * @param ip 需要转换的整形数值
         * @return 转换得到的 IP 地址
         */
        private String convertNumericIpToSymbolic(Integer ip) {
            StringBuffer sb = new StringBuffer(15);
            for (int shift = 24; shift > 0; shift -= 8) {
                sb.append(Integer.toString((ip >>> shift) & 0xff));
                sb.append('.');
            }
            sb.append(Integer.toString(ip & 0xff));
            return sb.toString();
        }

        /**
         * 获取 CIDR
         *
         * @return CIDR
         */
        public String getCIDR() {
            int i;
            for (i = 0; i < 32; i++) {
                if ((netmaskNumeric << i) == 0)
                    break;
            }
            return convertNumericIpToSymbolic(baseIPNumeric & netmaskNumeric) + "/" + i;
        }

        /**
         * 获取网段内所有有效的 IP 地址
         *
         * @param maxNumberOfIPs 最多返回 IP 地址数目，若为 -1 则不限制
         * @return 包含所有有效 IP 地址的 List
         */
        public List<String> getAvailableIPs(int maxNumberOfIPs) {
            ArrayList<String> result = new ArrayList<String>();

            int numberOfBits;
            for (numberOfBits = 0; numberOfBits < 32; numberOfBits++) {
                if ((netmaskNumeric << numberOfBits) == 0) break;
            }

            Integer numberOfIPs = 0;
            for (int n = 0; n < (32 - numberOfBits); n++) {
                numberOfIPs = numberOfIPs << 1;
                numberOfIPs = numberOfIPs | 0x01;
            }

            if (maxNumberOfIPs == -1) maxNumberOfIPs = numberOfIPs;

            Integer baseIP = baseIPNumeric & netmaskNumeric;
            for (int i = 1; i < (numberOfIPs) && i < maxNumberOfIPs; i++) {
                Integer ourIP = baseIP + i;
                String ip = convertNumericIpToSymbolic(ourIP);
                result.add(ip);
            }

            return result;
        }
    }

    /**
     * 连接超时
     */
    public static final int TIME_OUT = 500;

    /**
     * 判断特定 IP 地址主机的特定端口是否打开
     *
     * @param host 主机 IP 地址
     * @param port 端口
     * @return 返回 <code>true</code> 说明端口已经打开，返回 <code>false</code> 说明主机不可达或者端口未打开
     */
    public static boolean isReachable(String host, int port) {
        SocketAddress sockAddress = new InetSocketAddress(host, port);
        Socket socket = new Socket();
        try {
            socket.connect(sockAddress, TIME_OUT);
        } catch (IOException e) {
            return false;
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println("关闭到 " + socket.getInetAddress().getAddress() + " 连接失败");
                e.printStackTrace();
            }
        }
        return true;
    }

    public static final int PORT_ONE = 40310;    // 扫描端口 1
    public static final int PORT_TWO = 6259;     // 扫描端口 2

    public static void main(String[] args) {
        IPv4 iPv4 = new IPv4("10.4.20.1", "255.255.254.0");
        System.out.println("CIDR: " + iPv4.getCIDR());
        System.out.println("IP Address: " + iPv4.getIP());
        List<String> ips = iPv4.getAvailableIPs(-1);
        ips.clear();
        ips.add("10.62.32.140");
        for (String ip : ips) {
//            for (int port = 0; port < 65535; ++port) {
//                if (isReachable(ip, port)) {
//                    System.out.println("port " + port + " is open");
//                }
//            }

            if (isReachable(ip, PORT_TWO)) System.out.println(ip + " " + PORT_TWO + " is open");
            if (isReachable(ip, PORT_ONE)) System.out.println(ip + " " + PORT_ONE + " is open");
        }
    }
}