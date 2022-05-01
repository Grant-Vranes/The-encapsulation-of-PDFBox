package com.shenhua.pdfboxctl.pdfBoxCtl;

import com.shenhua.pdfboxctl.basement.Base64Util;
import com.shenhua.pdfboxctl.basement.PdfboxUtils;
import com.shenhua.pdfboxctl.basement.StringUtil;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @program: pdfboxctl
 * @Description: 确定区域之后的方法
 * @Author: Akio and xu,ziyun
 * @Create: 2022-05-01 16:57
 **/
public class RegionalMethod {
    /**
     * 提取[startIndex, endIndex]范围内的图片，转换为Base64格式集合
     * 注意：图片提取的顺序是PDF创建时图片插入的顺序，并非页面图片展示的顺序
     *
     * @param file
     * @param startIndex 开始页
     * @param endIndex 结束页
     * @throws Exception
     */
    public static List<String> getPhotos2Base64(File file, int startIndex, int endIndex) {
        List<String> photos = new ArrayList<>();
        if (startIndex == endIndex) {
            endIndex++;
        }
        try (PDDocument document = PDDocument.load(file)) {
            //TODO这里扣setuphoto图加字,对页数做了减1处理
            for (int i = startIndex - 1; i < endIndex; i++) {
                //抠图的时候pdf对应页数要减1
                PDPage pdfpage = document.getPage(i);
                // get resource of pdf
                PDResources pdResources = pdfpage.getResources();
                Iterable<COSName> xObjectNames = pdResources.getXObjectNames();
                Iterator<COSName> iterator = xObjectNames.iterator();
                while (iterator.hasNext()) {
                    PDXObject o = pdResources.getXObject(iterator.next());
                    if (o instanceof PDImageXObject) {
                        //得到BufferedImage对象
                        BufferedImage image = ((PDImageXObject) o).getImage();

                        // 如果存在logo，因为logo的图片都不高，可以限制图片高度或者宽度排除，如下
//                        System.out.println("setuphoto图片高度为:" + image.getHeight());
//                        if (image.getHeight() < 180) {
//                            continue;
//                        }

                        String base64img = Base64Util.convertimgtoBase64(image);

                        // 图片输出本地
//                        String imglocation = "C:\\folder\\";
//                        File imgfile = new File(imglocation + StringUtil.get32UUID() + ".png");
//                        ImageIO.write(image, "png", imgfile);

                        photos.add("data:image/jpg;base64," + base64img);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return photos;
    }

    /**
     * 截取pageIndex页面的rect_cut矩形为base64格式图片
     * @param file
     * @param pageIndex
     * @param rect_cut
     * @return
     */
    public static String getRectImage(File file, Integer pageIndex, Rectangle rect_cut) {
        String photo = "";

        try {
            BufferedImage bufImage = PdfboxUtils.readRectangelImage(file, pageIndex, rect_cut);
            String Rectbase64 = Base64Util.convertimgtoBase64(bufImage);
            photo = "data:image/jpg;base64," + Rectbase64;

            // 图片输出本地
//                String imglocation = "C:\\folder\\";
//                File imgfile = new File(imglocation + StringUtil.get32UUID() + ".png");
//                ImageIO.write(bufImage, "png", imgfile);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return photo;
    }

    /**
     * 截取pageIndex页面中的矩形集合为base64图片集合
     * @param file
     * @param pageIndex
     * @param rect_cut
     * @return
     */
    public static List<String> getRectImage(File file, Integer pageIndex, List<Rectangle> rect_cut) {
        List<String> photoPro = new ArrayList<>();

        try {
            for (int i = 0; i < rect_cut.size(); i++) {
                BufferedImage bufImage = PdfboxUtils.readRectangelImage(file, pageIndex, rect_cut.get(i));
                String Rectbase64 = Base64Util.convertimgtoBase64(bufImage);
                photoPro.add("data:image/jpg;base64," + Rectbase64);

                // 图片输出本地
//                String imglocation = "C:\\folder\\";
//                File imgfile = new File(imglocation + StringUtil.get32UUID() + ".png");
//                ImageIO.write(bufImage, "png", imgfile);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return photoPro;
    }

    /**
     * 截取[startIndex, endIndex]中的矩形为图片，每个页面对应一个rect_cut集合中一个矩形
     * 注意：需要确保endIndex-startIndex=rect_cut.size()
     * @param file
     * @param startIndex
     * @param endIndex
     * @param rect_cut
     * @return
     */
    public static List<String> getRectImage(File file, Integer startIndex, Integer endIndex, List<Rectangle> rect_cut) {
        List<String> photoPro = new ArrayList<>();

        // 开始遍历“起始关键字”页码到”结束关键字“页码，提取矩形中的所有文字
        if (startIndex == endIndex) {   //如果起始关键字和结束关键字在同一页，那么结束位置往后推一页，把当页面截取一下
            endIndex++;
        }

        Integer index = 0;
        for (int i = startIndex; i < endIndex; i++) {
            try {
                BufferedImage bufImage = PdfboxUtils.readRectangelImage(file, i, rect_cut.get(index));
                String Rectbase64 = Base64Util.convertimgtoBase64(bufImage);
                photoPro.add("data:image/jpg;base64," + Rectbase64);

                index++;

                // 图片输出本地
//                String imglocation = "C:\\folder\\";
//                File imgfile = new File(imglocation + StringUtil.get32UUID() + ".png");
//                ImageIO.write(bufImage, "png", imgfile);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return photoPro;
    }
}
