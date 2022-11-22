package com.mszlu.courseware.utils;


import com.documents4j.api.DocumentType;
import com.documents4j.api.IConverter;
import com.documents4j.job.LocalConverter;
import com.itextpdf.text.pdf.PdfReader;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.*;
import java.util.Objects;

@Slf4j
public class FilePagesUtil {

    private static final String UPLOAD_PATH = "D:/JavaWebFile/HnustPrint1.0/courseware-api/src/main/resources/static/file/";

    /**
     *
     * @param fileInputStream  文件流
     * @param fileType  文件后缀
     * @return 页数
     * @throws IOException
     */
    public static int filesPage(InputStream fileInputStream, String fileType, String filename) throws IOException {
        //log.info(fileType);
        int count = 0;
        String filePath = UPLOAD_PATH + filename.substring(0, filename.length()-5) + ".pdf";
        if (".docx".equals(fileType)) {
            docxWordToPdf(fileInputStream, filePath);
            count = countPdfPage(filePath);
            log.info(removeFile(filePath) ? "已删除文件" : "删除失败");
        }
        if (".doc".equals(fileType)) {
            docWordToPdf(fileInputStream, filePath);
            count = countPdfPage(filePath);
            log.info(removeFile(filePath) ? "已删除文件" : "删除失败");
        }
        if (".pdf".equals(fileType)) {
            count = countPdfPage(fileInputStream);
        }
        return count;
    }

    /**
     * 计算PDF格式文档的页数
     */
    public static int countPdfPage(InputStream fileInputStream) {
        int pageCount = 0;
        PdfReader reader = null;
        try {
            reader = new PdfReader(fileInputStream);
            pageCount = reader.getNumberOfPages();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Objects.requireNonNull(reader).close();
        }
        return pageCount;
    }

    public static int countPdfPage(String filePath) throws IOException {
        PdfReader reader = new PdfReader(filePath);
        return reader.getNumberOfPages();
    }

    /**
     * 计算PPTX格式文档的页数
     */
//    public static int countPPTPage(InputStream fileInputStream) throws IOException {
//        int pageCount = 0;
//        ZipSecureFile.setMinInflateRatio(-1.0d);
//
//        HSLFSlideShow hslfSlideShow = new HSLFSlideShow(fileInputStream);
//        try (fileInputStream) {
//            pageCount = hslfSlideShow.getSlides().size();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return pageCount;
//    }

    /**
     * 计算PPTX格式文档的页数
     */
    public static int countPPTXPage(InputStream fileInputStream) throws IOException {
        int pageCount = 0;
        ZipSecureFile.setMinInflateRatio(-1.0d);
        try (fileInputStream) {
            XMLSlideShow pptxFile = new XMLSlideShow(fileInputStream);
            pageCount = pptxFile.getSlides().size();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pageCount;
    }

    /**
     * 计算WORD2007(*.docx)格式文档的页数
     */
    public static int countWord2007Page(InputStream fileInputStream) throws IOException {
        int pageCount = 0;
        ZipSecureFile.setMinInflateRatio(-1.0d);
        try (XWPFDocument docx = new XWPFDocument(fileInputStream)) {
            pageCount = docx.getProperties().getExtendedProperties().getUnderlyingProperties().getPages();   // 总页数
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pageCount;
    }

    /**
     * 计算WORD2003(*.doc)格式文档的页数
     */
    /*public static int countWord2003Page(InputStream fileInputStream) throws IOException {
        int pageCount = 0;
        ZipSecureFile.setMinInflateRatio(-1.0d);
        try (WordExtractor doc = new WordExtractor(fileInputStream)) {
            //.doc格式Word文件提取器
            pageCount = doc.getSummaryInformation().getPageCount();//总页数
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pageCount;
        return -1;
    }*/

    /**
     *  实现 word.docx 转 pdf
     */
    public static void docxWordToPdf(InputStream docxInputStream, String targetPath) throws FileNotFoundException {
        File outputFile = new File(targetPath);
        try  {
            OutputStream outputStream = new FileOutputStream(outputFile);
            IConverter converter = LocalConverter.builder().build();
            converter.convert(docxInputStream)
                    .as(DocumentType.DOCX)
                    .to(outputStream)
                    .as(DocumentType.PDF).execute();
            outputStream.close();
            docxInputStream.close();
        } catch (Exception e) {
            log.error("[documents4J] word.docx转pdf失败:{}", e.toString());
        }
    }

    /**
     *  实现 word.doc 转 pdf
     */
    public static void docWordToPdf(InputStream docInputStream, String targetPath) {
        File outputFile = new File(targetPath);
        try {
            OutputStream outputStream = new FileOutputStream(outputFile);
            IConverter converter = LocalConverter.builder().build();
            converter.convert(docInputStream)
                    .as(DocumentType.DOC)
                    .to(outputStream)
                    .as(DocumentType.PDF).execute();
            outputStream.close();
            docInputStream.close();
        } catch (Exception e) {
            log.error("[documents4J] word.doc转pdf失败:{}", e.toString());
        }
    }

    private static boolean removeFile(String filePath) {
        if (filePath != null) {
            File file = new File(filePath);
            if (file.exists() && file.isFile()) {
                log.info(filePath);
                System.gc();
                return file.delete();
            }
        }
        return false;
    }
}
