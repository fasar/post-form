package fr.fasar.postform.controller;

import javax.servlet.http.Cookie;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class RequestEntity {

    private Instant ts;
    private String method;
    private String remoteAddr;
    private String requestURL;
    private String queryString;
    private String contentType;
    private String characterEncoding;
    private List<Cookie> cookies;
    private Map<String, String[]> parameterMap;
    byte[] bytes;

    public RequestEntity(Instant ts, String method, String remoteAddr, String requestURL, String queryString, String contentType, String characterEncoding, List<Cookie> cookies, Map<String, String[]> parameterMap, byte[] bytes) {
        this.ts = ts;
        this.method = method;
        this.remoteAddr = remoteAddr;
        this.requestURL = requestURL;
        this.queryString = queryString;
        this.contentType = contentType;
        this.characterEncoding = characterEncoding;
        this.cookies = cookies;
        this.parameterMap = parameterMap;
        this.bytes = bytes;
    }

    public Instant getTs() {
        return ts;
    }

    public void setTs(Instant ts) {
        this.ts = ts;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getRemoteAddr() {
        return remoteAddr;
    }

    public void setRemoteAddr(String remoteAddr) {
        this.remoteAddr = remoteAddr;
    }

    public String getRequestURL() {
        return requestURL;
    }

    public void setRequestURL(String requestURL) {
        this.requestURL = requestURL;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getCharacterEncoding() {
        return characterEncoding;
    }

    public void setCharacterEncoding(String characterEncoding) {
        this.characterEncoding = characterEncoding;
    }

    public List<Cookie> getCookies() {
        return cookies;
    }

    public void setCookies(List<Cookie> cookies) {
        this.cookies = cookies;
    }

    public Map<String, String[]> getParameterMap() {
        return parameterMap;
    }

    public void setParameterMap(Map<String, String[]> parameterMap) {
        this.parameterMap = parameterMap;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    @Override
    public String toString() {
        return "RequestEntity{" +
                "ts=" + ts +
                ", method='" + method + '\'' +
                ", remoteAddr='" + remoteAddr + '\'' +
                ", requestURL='" + requestURL + '\'' +
                ", queryString='" + queryString + '\'' +
                ", contentType='" + contentType + '\'' +
                ", characterEncoding='" + characterEncoding + '\'' +
                ", cookies=" + cookies +
                ", parameterMap=" + parameterMap +
                ", bytes=" + Arrays.toString(bytes) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequestEntity that = (RequestEntity) o;
        return Objects.equals(ts, that.ts) &&
                Objects.equals(method, that.method) &&
                Objects.equals(remoteAddr, that.remoteAddr) &&
                Objects.equals(requestURL, that.requestURL) &&
                Objects.equals(queryString, that.queryString) &&
                Objects.equals(contentType, that.contentType) &&
                Objects.equals(characterEncoding, that.characterEncoding) &&
                Objects.equals(cookies, that.cookies) &&
                Objects.equals(parameterMap, that.parameterMap) &&
                Arrays.equals(bytes, that.bytes);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(ts, method, remoteAddr, requestURL, queryString, contentType, characterEncoding, cookies, parameterMap);
        result = 31 * result + Arrays.hashCode(bytes);
        return result;
    }
}
