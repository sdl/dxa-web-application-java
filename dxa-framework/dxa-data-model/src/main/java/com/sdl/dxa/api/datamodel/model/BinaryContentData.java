package com.sdl.dxa.api.datamodel.model;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.base.Objects;

@JsonTypeName
public class BinaryContentData implements JsonPojo {
    public BinaryContentData() {
    }

    public BinaryContentData(String fileName, long fileSize, String mimeType, String url) {
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.mimeType = mimeType;
        this.url = url;
    }

    private String fileName;

    private long fileSize;

    private String mimeType;

    private String url;

    public String getFileName() {
        return fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public String getMimeType() {
        return mimeType;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BinaryContentData that = (BinaryContentData) o;
        return fileSize == that.fileSize &&
                Objects.equal(fileName, that.fileName) &&
                Objects.equal(mimeType, that.mimeType) &&
                Objects.equal(url, that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(fileName, fileSize, mimeType, url);
    }

    @Override
    public String toString() {
        return "BinaryContentData{" +
                "fileName='" + fileName + '\'' +
                ", fileSize=" + fileSize +
                ", mimeType='" + mimeType + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
