package fr.fasar.postform.controller;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PartEntity {
    private final String fileName;
    private final String fieldName;
    private final String contentType;
    private final Map<String, List<String>> headers;
    private final long size;
    private final String storedFileName;

    public PartEntity(String fileName, String fieldName, String contentType, Map<String, List<String>> headers, long size, String storedFileName) {
        this.fileName = fileName;
        this.fieldName = fieldName;
        this.contentType = contentType;
        this.headers = headers;
        this.size = size;
        this.storedFileName = storedFileName;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getContentType() {
        return contentType;
    }

    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    public long getSize() {
        return size;
    }

    public String getStoredFileName() {
        return storedFileName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PartEntity that = (PartEntity) o;
        return size == that.size &&
                Objects.equals(fileName, that.fileName) &&
                Objects.equals(fieldName, that.fieldName) &&
                Objects.equals(contentType, that.contentType) &&
                Objects.equals(headers, that.headers) &&
                Objects.equals(storedFileName, that.storedFileName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileName, fieldName, contentType, headers, size, storedFileName);
    }

    @Override
    public String toString() {
        return "PartEntity{" +
                "fileName='" + fileName + '\'' +
                ", fieldName='" + fieldName + '\'' +
                ", contentType='" + contentType + '\'' +
                ", headers=" + headers +
                ", size=" + size +
                ", storedFileName='" + storedFileName + '\'' +
                '}';
    }
}
