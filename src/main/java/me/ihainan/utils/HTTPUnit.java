package me.ihainan.utils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * HTTP 报文请求工具
 */
public class HTTPUnit {
    /**
     * HTTP 请求方法
     */
    public static enum METHOD {
        GET, POST
    }

    /**
     * HTTP 请求结果
     */
    public static class RequestResult {
        public String result;
        public String errorMessage;

        /**
         * 构造函数
         *
         * @param result       请求结果
         * @param errorMessage 错误信息，如为 null 说明无错误
         */
        public RequestResult(String result, String errorMessage) {
            this.result = result;
            this.errorMessage = errorMessage;
        }
    }

    /**
     * 发送 HTTP 报文
     *
     * @param targetURL     目标 URL
     * @param urlParameters 发送参数
     * @param method        发送方法
     * @return 报文请求结果
     */
    public static RequestResult makeRequest(String targetURL, String urlParameters, METHOD method) {
        // System.out.println(targetURL + "?" + urlParameters);
        HttpURLConnection connection = null;
        try {
            URL url = null;
            try {
                url = new URL(targetURL);
            } catch (MalformedURLException e) {
                new RequestResult(null, "Parse URL " + targetURL + " failed: unsupported protocol \n" + e.getMessage());
            }

            try {
                connection = (HttpURLConnection) url.openConnection();
            } catch (IOException e) {
                return new RequestResult(null, "Cannot connect to server：an I/O exception occurs \n" + e.getMessage());
            }

            try {
                connection.setRequestMethod(method.name());
            } catch (ProtocolException e) {
                return new RequestResult(null, "Cannot parse" + method + ": unsupported method \n" + e.getMessage());
            }

            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            if (urlParameters.length() != 0) connection.setRequestProperty("Content-Length",
                    Integer.toString(urlParameters.getBytes().length));
            connection.setUseCaches(false);
            connection.setDoOutput(true);
            connection.setRequestProperty("remote-addr", "127.0.0.1");
            // connection.setRequestProperty("referer", "http://www.baidu.com");

            DataOutputStream wr;
            try {
                if (urlParameters.length() != 0) {
                    wr = new DataOutputStream(
                            connection.getOutputStream());
                    wr.writeBytes(urlParameters);
                    wr.close();
                }
            } catch (IOException e) {
                return new RequestResult(null, "Failed to write data：an I/O exception occurs \n");
            }

            InputStream is;
            StringBuilder response = new StringBuilder();
            try {
                is = connection.getInputStream();
                BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                String line;
                while ((line = rd.readLine()) != null) {
                    response.append(line);
                    response.append('\n');
                }
                rd.close();
            } catch (IOException e) {
                return new RequestResult(null, "Failed to read data：an I/O exception occurs: " + e);
            }
            String result = response.toString().replace("\n", "");
            return new RequestResult(result, null);
        } finally {
            if (connection != null) connection.disconnect();
        }
    }

    public static void main(String[] args) {
        RequestResult requestResult = makeRequest("http://www.baidu.com", "", METHOD.GET);
        System.out.println(requestResult.errorMessage != null ? requestResult.errorMessage : requestResult.result);
    }
}
