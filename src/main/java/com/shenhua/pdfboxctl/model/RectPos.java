package com.shenhua.pdfboxctl.model;

/**
 * @program: pdfboxctl
 * @Description: rectangle矩形坐标及扩展寬高
 * @Author: Akio
 * @Create: 2022-05-01 13:47
 **/
public class RectPos {
    /*
        x,y
            ----------

            ----------
                    endX,endY pdfbox,fontbox 2.0.13才支持
     */
    private Integer x;
    private Integer y;
    private Integer width;
    private Integer height;
    private Integer pageFrom;
    private Integer endX; // 当前无用
    private Integer endY; // 当前无用
    private Integer pageWidth; // 当页PDF宽度
    private Integer pageHeight; // 当页PDF高度

    public RectPos() {}

    public RectPos(Integer x, Integer y, Integer width, Integer height, Integer pageFrom, Integer endX, Integer endY, Integer pageWidth, Integer pageHeight) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.pageFrom = pageFrom;
        this.endX = endX;
        this.endY = endY;
        this.pageWidth = pageWidth;
        this.pageHeight = pageHeight;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getPageFrom() {
        return pageFrom;
    }

    public void setPageFrom(Integer pageFrom) {
        this.pageFrom = pageFrom;
    }

    public Integer getEndX() {
        return endX;
    }

    public void setEndX(Integer endX) {
        this.endX = endX;
    }

    public Integer getEndY() {
        return endY;
    }

    public void setEndY(Integer endY) {
        this.endY = endY;
    }

    public Integer getPageWidth() {
        return pageWidth;
    }

    public void setPageWidth(Integer pageWidth) {
        this.pageWidth = pageWidth;
    }

    public Integer getPageHeight() {
        return pageHeight;
    }

    public void setPageHeight(Integer pageHeight) {
        this.pageHeight = pageHeight;
    }
}
