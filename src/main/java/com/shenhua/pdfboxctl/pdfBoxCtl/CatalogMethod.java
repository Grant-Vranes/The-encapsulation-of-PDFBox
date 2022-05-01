package com.shenhua.pdfboxctl.pdfBoxCtl;

import com.shenhua.pdfboxctl.basement.PdfboxUtils;
import com.shenhua.pdfboxctl.model.RectPos;

import java.awt.*;
import java.io.File;
import java.util.List;


/**
 * @program: pdfboxctl
 * @Description: 针对PDF目录有关方法
 * @Author: Akio
 * @Create: 2022-05-01 15:24
 **/
public class CatalogMethod {
    /**
     * 判断目录在哪一页，需要传入确认目录的关键词
     * To determine which page the directory is on, do you need to pass in the keywords to confirm the directory
     * @param file
     * @param catalog_keywords 确认目录的关键词，如“目录”，“Table of content”
     * @return 返回-1代表没找到这个关键词，即客观判断无目录页
     */
    public static Integer whereIsCatalog(File file, String catalog_keywords) {
        Integer where = -1;
        try {
            RectPos rectPos = KeyLocation.getKeyLocationFirst(file, catalog_keywords);
            if (rectPos.getPageFrom() != null) {
                where = rectPos.getPageFrom();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return where;
    }

    // TODO 需要判断这个范围是否左闭右开
    /**
     * 判断目录所在范围
     * @param file
     * @param catalog_keywords 确认目录的关键词，如“目录”，“Table of content”
     * @param catalog_features 目录特征词，如“......”，可以指定，传入null则默认为'......'
     * @return 目录所在范围[x, y],x目录开始页面，y目录结束页面当前页
     */
    public static Integer[] ScopeOfCatalog(File file, String catalog_keywords, String catalog_features) {
        Integer[] scope = new Integer[]{-1, -1};
        try {
            if (catalog_features == null || "".equals(catalog_features)) {
                catalog_features = "......";
            }
            //找到目录页起始位置,赋值给scope[0]
            scope[0] = whereIsCatalog(file, catalog_keywords);

            scope[1] = scope[0];
            //判断下一页是否还有......(目录中的特征)
            while (KeyLocation.getKeyLocationFirst(file, catalog_features, scope[1] + 1).getPageFrom() != null) {
                scope[1]++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return scope;
    }

    /**
     * 从圈定的目录范围查找存在的关键字，并返回目录映射页码,二次确认，能排除避免可能出现的表头也有关键字的情况，默认以“...”作为目录特征词
     *
     * @param file
     * @param titleName
     * @param scope 传入的范围可以通过ScopeOfCatalog()方法获取
     * @return
     */
    public static Integer getPageNumFromCatalog(File file, String titleName, Integer[] scope) {
        int contentNum = -1;
        try {
            // 列出范围内可能存在的关键字
            List<RectPos> maybePos = KeyLocation.getKeyLocation(file, titleName, scope);
            if (maybePos.size() == 0) {
                return contentNum;
            }
            for (RectPos rectPos : maybePos) {
                // 定义一个Rectangle
                Rectangle textRect = new Rectangle(rectPos.getX(), rectPos.getY(), rectPos.getPageWidth()-rectPos.getX(), rectPos.getHeight());
                // 提取区域内文本
                String tempText = PdfboxUtils.readRectangelText(file, rectPos.getPageFrom(), textRect);
                if (tempText != null) {
                    if ("...".contains(tempText)) {
                        String[] split = tempText.trim().split("\\.{3,}");
                        contentNum = Integer.parseInt(split[split.length - 1].trim());
                        if (contentNum != -1) {
                            break;
                        }
                    } else {
                        //如果到这个分支，说明就算有目录，我这个关键词目录里也不存在
                        continue;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contentNum;
    }

    /**
     * 从圈定的目录范围查找存在的关键字，并返回目录映射页码,二次确认，能排除避免可能出现的表头也有关键字的情况,可自定义目录特征词
     *
     * @param file
     * @param titleName
     * @param scope 传入的范围可以通过ScopeOfCatalog()方法获取
     * @param catalog_features 目录特征词，如“......”
     * @return
     */
    public static Integer getPageNumFromCatalog(File file, String titleName, Integer[] scope, String catalog_features) {
        int contentNum = -1;
        try {
            // 列出范围内可能存在的关键字
            List<RectPos> maybePos = KeyLocation.getKeyLocation(file, titleName, scope);
            if (maybePos.size() == 0) {
                return contentNum;
            }
            for (RectPos rectPos : maybePos) {
                Rectangle textRect = new Rectangle(rectPos.getX(), rectPos.getY(), rectPos.getWidth() + 500, rectPos.getHeight());
                String tempText = PdfboxUtils.readRectangelText(file, rectPos.getPageFrom(), textRect);
                if (tempText != null) {
                    if (catalog_features.contains(tempText)) {
                        String[] split = tempText.trim().split(tempText + "{3,}");
                        contentNum = Integer.parseInt(split[split.length - 1].trim());
                        if (contentNum != -1) {
                            break;
                        }
                    } else {
                        //如果到这个分支，说明就算有目录，我这个关键词目录里也不存在
                        continue;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contentNum;
    }
}
