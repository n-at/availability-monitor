package ru.doublebyte.availabilitymonitor.types;

import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.SocketTimeoutException;

public class UrlChecker {

    private static Logger logger = LoggerFactory.getLogger(UrlChecker.class);

    private String url;
    private int respondTimeout;

    private HttpClient httpClient;
    private HttpGet httpGet;

    ///////////////////////////////////////////////////////////////////////////

    public UrlChecker(String url, int respondTimeout) {
        this.url = url;
        this.respondTimeout = respondTimeout;

        httpClient = getHttpClient();
        httpGet = new HttpGet(url);
    }

    public Result check() {
        try {
            HttpResponse response = httpClient.execute(httpGet);

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                logger.debug("Bad status code: {}, url: {}", statusCode, url);
                return Result.BAD_STATUS;
            }

            EntityUtils.consume(response.getEntity());
        } catch (ConnectTimeoutException e) {
            logger.debug("Request ConnectTimeoutException: {}, url: {}", e.getMessage(), url);
            return Result.TIMEOUT;
        } catch (SocketTimeoutException e) {
            logger.debug("Request SocketTimeoutException: {}, url: {}", e.getMessage(), url);
            return Result.TIMEOUT;
        } catch (ClientProtocolException e) {
            logger.debug("Request ClientProtocolException: {}, url: {}", e.getMessage(), url);
            return Result.NO_CONNECTION;
        } catch (NoHttpResponseException e) {
            logger.debug("Request NoHttpResponseException: {}, url: {}", e.getMessage(), url);
            return Result.NO_CONNECTION;
        } catch (IOException e) {
            logger.debug("Request IOException: {}, url: {}", e.getMessage(), url);
            return Result.NO_CONNECTION;
        }

        return Result.SUCCESS;
    }

    ///////////////////////////////////////////////////////////////////////////

    private RequestConfig getRequestConfig() {
        return RequestConfig.custom()
                .setConnectionRequestTimeout(respondTimeout)
                .setConnectTimeout(respondTimeout)
                .setSocketTimeout(respondTimeout)
                .build();
    }

    private HttpClient getHttpClient() {
        return HttpClientBuilder.create()
                .disableAuthCaching()
                .disableAutomaticRetries()
                .disableContentCompression()
                .disableCookieManagement()
                .setDefaultRequestConfig(getRequestConfig())
                .setMaxConnPerRoute(1)
                .setMaxConnTotal(1)
                .build();
    }

    ///////////////////////////////////////////////////////////////////////////

    public static enum Result {
        SUCCESS(true, "SUCCESS"),
        NO_CONNECTION(false, "NO CONNECTION"),
        TIMEOUT(false, "TIMEOUT"),
        BAD_STATUS(false, "BAD STATUS"),
        ;

        private boolean success;
        private String display;

        Result(boolean success, String display) {
            this.success = success;
            this.display = display;
        }

        @Override
        public String toString() {
            return display;
        }
    }

}
