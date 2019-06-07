package fr.fasar.postform.controller;

import javax.servlet.http.Cookie;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class RequestEntity {

    private String id;
    private Instant ts;
    private String method;
    private String remoteAddr;
    private String requestURL;
    private String characterEncoding;
    private Map<String, List<String>>  headers;
    private Map<String, String[]> parameterMap;
    private List<PartEntity> parts;
    byte[] bytes;

    public RequestEntity(
            String id, Instant ts, String method, String remoteAddr, String requestURL,
            String characterEncoding, Map<String, String[]> parameterMap, Map<String, List<String>> headers,
            List<PartEntity> parts, byte[] bytes) {
        this.id = id;
        this.ts = ts;
        this.method = method;
        this.remoteAddr = remoteAddr;
        this.requestURL = requestURL;
        this.characterEncoding = characterEncoding;
        this.parameterMap = parameterMap;
        this.headers = headers;
        this.parts = parts;
        this.bytes = bytes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getCharacterEncoding() {
        return characterEncoding;
    }

    public void setCharacterEncoding(String characterEncoding) {
        this.characterEncoding = characterEncoding;
    }

    public Map<String, String[]> getParameterMap() {
        return parameterMap;
    }

    public void setParameterMap(Map<String, String[]> parameterMap) {
        this.parameterMap = parameterMap;
    }

    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, List<String>> headers) {
        this.headers = headers;
    }

    public List<PartEntity> getParts() {
        return parts;
    }

    public void setParts(List<PartEntity> parts) {
        this.parts = parts;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequestEntity that = (RequestEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(ts, that.ts) &&
                Objects.equals(method, that.method) &&
                Objects.equals(remoteAddr, that.remoteAddr) &&
                Objects.equals(requestURL, that.requestURL) &&
                Objects.equals(characterEncoding, that.characterEncoding) &&
                Objects.equals(headers, that.headers) &&
                Objects.equals(parameterMap, that.parameterMap) &&
                Objects.equals(parts, that.parts) &&
                Arrays.equals(bytes, that.bytes);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, ts, method, remoteAddr, requestURL, characterEncoding, headers, parameterMap, parts);
        result = 31 * result + Arrays.hashCode(bytes);
        return result;
    }

    @Override
    public String toString() {
        return "RequestEntity{" +
                "id='" + id + '\'' +
                ", ts=" + ts +
                ", method='" + method + '\'' +
                ", remoteAddr='" + remoteAddr + '\'' +
                ", requestURL='" + requestURL + '\'' +
                ", characterEncoding='" + characterEncoding + '\'' +
                ", headers=" + headers +
                ", parameterMap=" + parameterMap +
                ", parts=" + parts +
                ", bytes=" + Arrays.toString(bytes) +
                '}';
    }
}
