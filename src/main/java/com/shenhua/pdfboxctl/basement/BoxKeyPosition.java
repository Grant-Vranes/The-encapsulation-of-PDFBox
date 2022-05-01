package com.shenhua.pdfboxctl.basement;

import com.shenhua.pdfboxctl.model.RectPos;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: pdfboxctl
 * @Description: 调用PDFbox的底层方法
 * @Author: Akio and xu,ziyun
 * @Create: 2022-05-01 13:45
 **/
/**
 * notice:
 * 1、对于keyword,此程序执行大小写分明，当前已经修改为不区分大小写
 * 2、对于BoxKeyPosition，所有数据页面的下标都是从1开始的，而对于之后会遇到的PDFRenderer（用于截图）的下标则是从0开始****
 */
public class BoxKeyPosition extends PDFTextStripper {

    // 关键字字符数组
    private char[] key;
    // PDF 源 byte[]
    private byte[] src;
    // 坐标信息集合
    private List<RectPos> list = new ArrayList<RectPos>();
    // 当前页信息集合
    private List<RectPos> pagelist = new ArrayList<RectPos>();

    public BoxKeyPosition(byte[] src) throws IOException {
        super();
        super.setSortByPosition(true);
        this.src = src;
    }

    public BoxKeyPosition(String keyWords, byte[] src) throws IOException {
        super();
        super.setSortByPosition(true);
        this.src = src;

        char[] key = new char[keyWords.length()];
        for (int i = 0; i < keyWords.length(); i++) {
            //String.charAt返回指定索引的字符
            key[i] = keyWords.charAt(i);
        }
        this.key = key;
    }

    public char[] getKey() {
        return key;
    }

    public void setKey(char[] key) {
        this.key = key;
    }

    public byte[] getSrc() {
        return src;
    }

    public void setSrc(byte[] src) {
        this.src = src;
    }

    /**
     * 遍历所有页，获取所有存在此关键字的矩形
     *
     * @return
     * @throws IOException
     */
    public List<RectPos> getPosition() throws IOException {
        try {
            document = PDDocument.load(src);
            //获取pdf总页数
            int pages = document.getNumberOfPages();
            //遍历每一页
            for (int i = 1; i <= pages; i++) {

                pagelist.clear();

                super.setSortByPosition(true);
                //这将设置该类提取的第一页
                super.setStartPage(i);
                //这将设置该类提取的最后一页
                super.setEndPage(i);
                Writer dummy = new OutputStreamWriter(new ByteArrayOutputStream());
                super.writeText(document, dummy);//这将获取一个PDDocument，并将该文档的文本写入打印器，为pagelist赋值
                for (RectPos li : pagelist) {
                    li.setPageFrom(i);//存入当前页码数-1
                }
                list.addAll(pagelist);
            }
            return list;

        } finally {
            if (document != null) {
                document.close();
            }
        }
    }

    /**
     * 从当前页面开始遍历，获取所有存在此关键字的矩形
     *
     * @param startIndex
     * @return
     * @throws IOException
     */
    public List<RectPos> getPosition(Integer startIndex) throws IOException {
        try {
            document = PDDocument.load(src);
            //获取pdf总页数
            int pages = document.getNumberOfPages();
            //遍历每一页
            for (int i = startIndex; i <= pages; i++) {

                pagelist.clear();

                super.setSortByPosition(true);
                //这将设置该类提取的第一页
                super.setStartPage(i);
                //这将设置该类提取的最后一页
                super.setEndPage(i);
                Writer dummy = new OutputStreamWriter(new ByteArrayOutputStream());
                super.writeText(document, dummy);//这将获取一个PDDocument，并将该文档的文本写入打印器，为pagelist赋值
                for (RectPos li : pagelist) {
                    li.setPageFrom(i);//存入当前页码数-1
                }
                list.addAll(pagelist);
            }
            return list;

        } finally {
            if (document != null) {
                document.close();
            }
        }
    }

    /**
     * 查找固定范围内，存在此关键字的矩形
     *
     * @param scope
     * @return
     * @throws IOException
     */
    public List<RectPos> getPosition(Integer[] scope) throws IOException {
        try {
            document = PDDocument.load(src);
            //遍历固定范围
            for (int i = scope[0]; i <= scope[1]; i++) {

                pagelist.clear();

                super.setSortByPosition(true);
                //这将设置该类提取的第一页
                super.setStartPage(i);
                //这将设置该类提取的最后一页
                super.setEndPage(i);
                Writer dummy = new OutputStreamWriter(new ByteArrayOutputStream());
                super.writeText(document, dummy);//这将获取一个PDDocument，并将该文档的文本写入打印器，为pagelist赋值
                for (RectPos li : pagelist) {
                    li.setPageFrom(i);//存入当前页码数-1
                }
                list.addAll(pagelist);
            }
            return list;

        } finally {
            if (document != null) {
                document.close();
            }
        }
    }

    /**
     * 若水三千，我只取startIndex之后的第一瓢
     *
     * @return
     * @throws IOException
     */
    public RectPos getPositionFirst(Integer startIndex) throws IOException {
        RectPos rectPos = new RectPos();
        try {
            document = PDDocument.load(src);
            //获取pdf总页数
            int pages = document.getNumberOfPages();
            //遍历每一页
            for (int i = startIndex; i <= pages; i++) {
                pagelist.clear();
                super.setSortByPosition(true);
                //这将设置该类提取的第一页
                super.setStartPage(i);
                //这将设置该类提取的最后一页
                super.setEndPage(i);
                Writer dummy = new OutputStreamWriter(new ByteArrayOutputStream());
                super.writeText(document, dummy);//这将获取一个PDDocument，并将该文档的文本写入打印器，为pagelist赋值
                if (pagelist.size() > 0) {
                    pagelist.get(0).setPageFrom(i);
                    rectPos = pagelist.get(0);
                    break;
                }
            }
            return rectPos;
        } finally {
            if (document != null) {
                document.close();
            }
        }
    }


    public Integer getAllPage() {
        Integer pages = null;
        try {
            document = PDDocument.load(src);
            //获取pdf总页数
            pages = document.getNumberOfPages();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pages;
    }

    /**
     * 从圈定的目录范围内，查找第一个
     *
     * @param scope
     * @return
     * @throws IOException
     */
    public RectPos getPositionFirst(Integer[] scope) throws IOException {
        RectPos rectPos = new RectPos();
        try {
            document = PDDocument.load(src);
            //获取pdf总页数
            int pages = document.getNumberOfPages();
            //遍历每一页
            for (int i = scope[0]; i <= scope[1]; i++) {
                pagelist.clear();
                super.setSortByPosition(true);
                //这将设置该类提取的第一页
                super.setStartPage(i);
                //这将设置该类提取的最后一页
                super.setEndPage(i);
                Writer dummy = new OutputStreamWriter(new ByteArrayOutputStream());
                super.writeText(document, dummy);//这将获取一个PDDocument，并将该文档的文本写入打印器，为pagelist赋值
                if (pagelist.size() > 0) {
                    pagelist.get(0).setPageFrom(i);
                    rectPos = pagelist.get(0);
                    break;
                }
            }
            return rectPos;
        } finally {
            if (document != null) {
                document.close();
            }
        }
    }

    public RectPos getPositionFirst() throws IOException {
        RectPos rectPos = new RectPos();
        try {
            document = PDDocument.load(src);
            //获取pdf总页数
            int pages = document.getNumberOfPages();
            //遍历每一页
            for (int i = 1; i <= pages; i++) {
                pagelist.clear();
                super.setSortByPosition(true);
                //这将设置该类提取的第一页
                super.setStartPage(i);
                //这将设置该类提取的最后一页
                super.setEndPage(i);
                Writer dummy = new OutputStreamWriter(new ByteArrayOutputStream());
                super.writeText(document, dummy);//这将获取一个PDDocument，并将该文档的文本写入打印器，为pagelist赋值
                if (pagelist.size() > 0) {
                    pagelist.get(0).setPageFrom(i);
                    rectPos = pagelist.get(0);
                    break;
                }
            }
            return rectPos;

        } finally {
            if (document != null) {
                document.close();
            }
        }
    }

    /**
     * 数据填充项目
     *
     * @param string
     * @param textPositions
     * @throws IOException
     */
    @Override
    protected void writeString(String string, List<TextPosition> textPositions) throws IOException {
        for (int i = 0; i < textPositions.size(); i++) {
            String str = textPositions.get(i).getUnicode();
            if (str.equalsIgnoreCase(key[0] + "")) { // TODO 可以设置是否大小写分明
                int count = 0;
                for (int j = 1; j < key.length; j++) {
                    String s = "";
                    try {
                        s = textPositions.get(i + j).getUnicode();
                    } catch (Exception e) {
                        s = "";
                    }
                    if (s.equalsIgnoreCase(key[j] + "")) { // TODO 可以设置是否大小写分明
                        count++;
                    }

                }
                if (count == key.length - 1) {
                    RectPos idx = new RectPos();
//                    idx.setX((int)(textPositions.get(i).getX()+key.length*textPositions.get(i).getWidth()/2));
                    idx.setX((int) (textPositions.get(i).getX()));
//                    idx.setY((int)(textPositions.get(i).getY()-textPositions.get(i).getHeight()));
//                    idx.setY((int)(textPositions.get(i).getY()-textPositions.get(i).getFontSizeInPt()));
                    idx.setY((int) (textPositions.get(i).getY() - textPositions.get(i).getHeight()));
                    idx.setWidth((int) (textPositions.get(i).getFontSizeInPt() * (key.length)));//TODO 对于中英文还是要斟酌，用getWidth()会不会好一点
                    idx.setHeight((int) (textPositions.get(i).getFontSizeInPt()));
                    idx.setEndX((int) (textPositions.get(i).getEndX()));
                    idx.setPageWidth((int) (textPositions.get(i).getPageWidth()));
                    idx.setPageHeight((int) (textPositions.get(i).getPageHeight()));
//                    idx.setWidth((int)textPositions.get(i).getWidth());
//                    idx.setHeight((int)textPositions.get(i).getHeight());
//                    idx.setPageFrom(i+1);//你可以这里写，也可以上一个方法写，但在上一个方法中写，开发的时候debug更好
                    pagelist.add(idx);
                }
            }

        }
    }
}
