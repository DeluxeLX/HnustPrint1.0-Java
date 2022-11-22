package com.mszlu.courseware.utils;

import com.itextpdf.text.pdf.PdfReader;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
//import org.apache.poi.ooxml.POIXMLDocument;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import java.io.IOException;

public class ReaderUtil {

//    public static int getFilePageNum(String filePath) throws IOException {
//        int pageNum = 0;
//        String lowerFilePath = filePath.toLowerCase();
//        if (lowerFilePath.endsWith(".docx")) {
//            XWPFDocument docx = new XWPFDocument(POIXMLDocument.openPackage(lowerFilePath));
//            pageNum = docx.getProperties().getExtendedProperties().getUnderlyingProperties().getPages();
//        } else if (lowerFilePath.endsWith(".doc")) {
//            //下方的方法不好使，经常只统计出一页
////            HWPFDocument wordDoc = new HWPFDocument(new FileInputStream(lowerFilePath));
////            pageNum = wordDoc.getSummaryInformation().getPageCount();
//            //采用如下方法
//            pageNum = getDocPageNum(lowerFilePath);
//        } else if (lowerFilePath.endsWith(".pdf")){
//            PdfReader reader = new PdfReader(filePath);
//            pageNum = reader.getNumberOfPages();
//        }
//        return pageNum;
//    }
//
//    private static int getDocPageNum(String filePath) {
//        int pageNum = 0;
//        try{
//            // 建立ActiveX部件
//            ActiveXComponent wordCom = new ActiveXComponent("Word.Application");
//            //word应用程序不可见
//            wordCom.setProperty("Visible", false);
//            // 返回wrdCom.Documents的Dispatch
//            Dispatch wrdDocs = wordCom.getProperty("Documents").toDispatch();//Documents表示word的所有文档窗口（word是多文档应用程序）
//            // 调用wrdCom.Documents.Open方法打开指定的word文档，返回wordDoc
//            Dispatch wordDoc = Dispatch.call(wrdDocs, "Open", filePath, false, true, false).toDispatch();
//            Dispatch selection = Dispatch.get(wordCom, "Selection").toDispatch();
//            pageNum = Integer.parseInt(Dispatch.call(selection,"information",4).toString());//总页数 //显示修订内容的最终状态
//            //关闭文档且不保存
//            Dispatch.call(wordDoc, "Close", new Variant(false));
//            //退出进程对象
//            wordCom.invoke("Quit", new Variant[] {});
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return pageNum;
//    }
}
