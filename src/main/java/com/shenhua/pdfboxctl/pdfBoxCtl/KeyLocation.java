package com.shenhua.pdfboxctl.pdfBoxCtl;

import com.shenhua.pdfboxctl.basement.BoxKeyPosition;
import com.shenhua.pdfboxctl.model.RectPos;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * @program: pdfboxctl
 * @Description: 封装对于PDFBox组件的基本操作方法：提取关键词在PDF文件中所在的RectPos
 * @Author: Akio
 * @Create: 2022-05-01 14:02
 **/
public class KeyLocation {
    /**
     * 获取此关键词再PDF文件中所在位置的RectPos模型集合
     * Gets the collection of RectPos models at the location of this keywords in the PDF file
     * @param file
     * @param keyWords 关键词
     * @return
     * @throws Exception
     */
    public static List<RectPos> getKeyLocation(File file, String keyWords) {
        List<RectPos> position = null;
        try (
                PDDocument doc = PDDocument.load(file);
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))
        ) {
            //transfer to byte array
            byte[] fileBytes = new byte[bis.available()];
            bis.read(fileBytes);

            BoxKeyPosition pdf = new BoxKeyPosition(keyWords, fileBytes);
            position = pdf.getPosition();//get all of position from keywords
        } catch (IOException e) {
            e.printStackTrace();
        }
        return position;
    }

    /**
     * 查找此关键词在PDF文件scope范围内所有位置的RectPos的集合
     * Find the collection of RectPos for all locations within the scope of the PDF file for this keywords
     * @param file
     * @param keyWords 关键词
     * @param scope 圈定的页面范围，[x,y]，包括x和y当页
     * @return
     */
    public static List<RectPos> getKeyLocation(File file, String keyWords, Integer[] scope) {
        List<RectPos> position = null;
        try (
                PDDocument doc = PDDocument.load(file);
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))
        ) {
            //transfer to byte array
            byte[] fileBytes = new byte[bis.available()];
            bis.read(fileBytes);

            BoxKeyPosition pdf = new BoxKeyPosition(keyWords, fileBytes);
            position = pdf.getPosition(scope);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return position;
    }

    /**
     * 获取该关键词在PDF文件中的第一个RectPos对象
     * Gets the first RectPos object for the keywords in the PDF file
     * @param file
     * @param keyWords 关键词
     * @return
     */
    public static RectPos getKeyLocationFirst(File file, String keyWords) {
        RectPos position = null;
        try (
                PDDocument doc = PDDocument.load(file);
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))
        ) {
            //transfer to byte array
            byte[] fileBytes = new byte[bis.available()];
            bis.read(fileBytes);

            BoxKeyPosition pdf = new BoxKeyPosition(keyWords, fileBytes);
            position = pdf.getPositionFirst();//get all of position from keywords
        } catch (IOException e) {
            e.printStackTrace();
        }
        return position;
    }

    /**
     * 从设定的startIndex页数查找后面所有的keyWords(关键词)的RectPos对象的第一个（包括startIndex此页）
     * The first RectPos object (including the startIndex page) that looks up all the following keyWords from the set startIndex page
     * @param file
     * @param keyWords 关键词
     * @param startIndex 起始页面
     * @return
     */
    public static RectPos getKeyLocationFirst(File file, String keyWords, Integer startIndex) {
        RectPos position = null;
        try (
                PDDocument doc = PDDocument.load(file);
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))
        ) {
            //transfer to byte array
            byte[] fileBytes = new byte[bis.available()];
            bis.read(fileBytes);

            BoxKeyPosition pdf = new BoxKeyPosition(keyWords, fileBytes);
            position = pdf.getPositionFirst(startIndex);//get all of position from keywords from startIndex(pageNum)
        } catch (IOException e) {
            e.printStackTrace();
        }
        return position;
    }

    /**
     * 从圈定的范围中找所有的keyWords的RectPos的第一个
     * Find the first RectPos of all keyWords from the defined range
     * @param file
     * @param keyWords 关键词
     * @param scope 圈定的页面范围，[x,y]，包括x和y当页
     * @return
     */
    public static RectPos getKeyLocationFirst(File file, String keyWords, Integer[] scope) {
        RectPos position = null;
        try (
                PDDocument doc = PDDocument.load(file);
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))
        ) {
            //transfer to byte array
            byte[] fileBytes = new byte[bis.available()];
            bis.read(fileBytes);

            BoxKeyPosition pdf = new BoxKeyPosition(keyWords, fileBytes);
            position = pdf.getPositionFirst(scope);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return position;
    }

    /**
     * 从设定的startIndex页面开始找后面的keyWords数组中某一个的RectPos的第一个
     * keyWords数组中根据顺序遍历，找到谁算谁的
     * @param file
     * @param keyWords
     * @param startIndex
     * @return
     */
    public static RectPos getKeyLocationFirst(File file, String keyWords[], Integer startIndex) {
        RectPos position = null;
        try (
                PDDocument doc = PDDocument.load(file);
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))
        ) {
            //transfer to byte array
            byte[] fileBytes = new byte[bis.available()];
            bis.read(fileBytes);

            for (String key : keyWords) {
                BoxKeyPosition pdf = new BoxKeyPosition(key, fileBytes);
                position = pdf.getPositionFirst(startIndex);//get all of position from keywords from startIndex(pageNum)
                if (position.getPageFrom() != null) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return position;
    }
}
