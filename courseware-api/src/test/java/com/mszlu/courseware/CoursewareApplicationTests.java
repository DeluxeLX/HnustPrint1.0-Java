package com.mszlu.courseware;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.date.DateTime;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.documents4j.api.DocumentType;
import com.documents4j.api.IConverter;
import com.documents4j.job.LocalConverter;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import com.mszlu.courseware.mapper.OrderMapper;
import com.mszlu.courseware.pojo.Order;
import com.mszlu.courseware.service.OrderService;
import com.mszlu.courseware.service.OssService;
import com.mszlu.courseware.utils.FilePagesUtil;
import com.mszlu.courseware.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.util.Arrays;

@SpringBootTest
@Slf4j
public class CoursewareApplicationTests {

    @Value("${knowledge.keywords}")
    private String[] keywords;

    @Test
    public void test() {
        System.out.println(Arrays.toString(keywords));
    }

//
//    @Autowired
//    private RedisUtil redisUtil;
//
//    @Autowired
//    private OssService ossService;
//
//    @Autowired
//    private OrderService orderService;
//
//    @Value("${wxmini.appid}")
//    private String appid;
//
//    @Test
//    public void getAppid() {
//        System.out.println(appid);
//    }
//
//    @Test
//    public void jsonTest() {
//        String res = "\"{\\\"gender\\\":\\\"0\\\",\\\"id\\\":12,\\\"nickname\\\":\\\"小郎君.\\\",\\\"openId\\\":\\\"oo1LJ4kEMCS3k92roV4OcxrnF5bk\\\",\\\"phoneNumber\\\":\\\"\\\",\\\"portrait\\\":\\\"https://thirdwx.qlogo.cn/mmopen/vi_32/y4aNtBS6jU0Ds7VyE211AwT871EB5Jb4NQsibwk23U5HJqCc1QKcFOa9DS4KyEjbYsRaHLMwn2tf6vTGV3hf47A/132\\\",\\\"token\\\":\\\"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6MTIsImV4cCI6MTY2NDc4MjI1NX0.hdjr8vKujjXD4B8tUBpdB--jiARdxWug83CEdv3jXWg\\\"}\"";
//        System.out.println("原始字符串：" + res);
//
//        String replaceAll = res.replaceAll("\\\\", "");
//        System.out.println("replaceAll：" + replaceAll);
//
//        String substring = replaceAll.substring(1, replaceAll.length()-1);
//        System.out.println("substring：" + substring);
//
//        JSONObject jsonObject = JSONObject.parseObject(substring);
//        System.out.println("jsonObject：" + jsonObject);
//        System.out.println("jsonObject中的id：" + jsonObject.getString("id"));
//        System.out.println(jsonObject.getInnerMap());
//    }
//
//    @Test
//    public void getPagesNum() {
//        try{
//            //int docNum = ReaderUtil.getFilePageNum("C:\\Users\\liaozihao\\Desktop\\附件5：湖南科技大学2022年大学生科研创新计划项目（srip）申报书.doc");
//            //System.out.println("Office2003之前版本的Word：11 pages.doc 的页数为：" + docNum);
//            //int docxNum =  ReaderUtil.getFilePageNum("D:\\重要文件\\PS培训讲解.docx");
//            //System.out.println("Office2003之后版本的Word：4 pages.docx 的页数为：" + docxNum);
////            Long startTime = System.currentTimeMillis();
////            documents4jWordToPdf("C:\\Users\\liaozihao\\Desktop\\9.13\\20级数据结构课程设计报告.docx",
////                    "C:\\Users\\liaozihao\\Desktop\\9.13\\20级数据结构课程设计报告.pdf");
////            int docxNum2 = ReaderUtil.getFilePageNum("C:\\Users\\liaozihao\\Desktop\\9.13\\20级数据结构课程设计报告.pdf");
////            System.out.println("Office2003之后版本的Word：37 pages.docx 的页数为：" + docxNum2);
////            Long endTime = System.currentTimeMillis();
////            System.out.println(endTime-startTime);
//            //int pdfNum = ReaderUtil.getFilePageNum("C:\\Users\\liaozihao\\Desktop\\9.13\\中学科目二二级简答题汇总.pdf");
//            //System.out.println("PDF：14 pages.pdf 的页数为：" + pdfNum);
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void documents4jWordToPdf(String sourcePath, String targetPath) {
//        //File inputWord = new File(sourcePath);
//        //File outputFile = new File(targetPath);
//        try  {
//            BufferedInputStream docxInputStream = new BufferedInputStream(new FileInputStream(sourcePath));
//            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(targetPath));
//            //FileInputStream docxInputStream = new FileInputStream(sourcePath);
//            //FileOutputStream outputStream = new FileOutputStream(targetPath);
//            IConverter converter = LocalConverter.builder().build();
//            converter.convert(docxInputStream)
//                    .as(DocumentType.DOCX)
//                    .to(outputStream)
//                    .as(DocumentType.PDF).execute();
//            outputStream.close();
//        } catch (Exception e) {
//            log.error("[documents4J] word转pdf失败:{}", e.toString());
//        }
//    }
//
//    @Test
//    public void getString() {
//        String str = "src\\main\\resources\\static\\file\\20级数据结构课程设计报告（廖子豪新）(1)(2).docx";
//        System.out.println(str.substring(0, str.length()-5));
//    }
//
//    @Test
//    public void deleteFileTest() {
//        String filePath = "D:/JavaWebFile/HnustPrint1.0/courseware-api/src/main/resources/static/file/个人简历.pdf";
//        File file = new File(filePath);
//        System.out.println(file.delete());
//    }
//
//    @Test
//    public void getPagesNum2() {
//        // 建立ActiveX部件
//        ActiveXComponent wordCom = new ActiveXComponent("Word.Application");
//        //word应用程序不可见
//        wordCom.setProperty("Visible", false);
//        // 返回wrdCom.Documents的Dispatch
//        Dispatch wrdDocs = wordCom.getProperty("Documents").toDispatch();
//        // 调用wrdCom.Documents.Open方法打开指定的word文档，返回wordDoc
//        Dispatch wordDoc = Dispatch.call(wrdDocs, "Open", "C:\\Users\\liaozihao\\Desktop\\个人简历.docx", false, true, false).toDispatch();
//        Dispatch selection = Dispatch.get(wordCom, "Selection").toDispatch();
//        int pages = Integer.parseInt(Dispatch.call(selection,"information",4).toString());
//        System.err.println("docx页数："+pages);
//        //关闭文档且不保存
//        Dispatch.call(wordDoc, "Close", new Variant(false));
//        //退出进程对象
//        wordCom.invoke("Quit", new Variant[] {});
//    }
//
//    @Test
//    public void getPagesNumByPoi() throws IOException {
////        FileInputStream fileInputStream = new FileInputStream("C:\\Users\\liaozihao\\Desktop\\9.13\\20级数据结构课程设计报告.docx");
////        XWPFDocument xwpfDocument = new XWPFDocument(fileInputStream);
////        PdfOptions pdfOptions = PdfOptions.create();
////        FileOutputStream fileOutputStream = new FileOutputStream("C:\\Users\\liaozihao\\Desktop\\9.13\\20级数据结构课程设计报告.pdf");
////        PdfConverter.getInstance().convert(xwpfDocument,fileOutputStream,pdfOptions);
////        fileInputStream.close();
////        fileOutputStream.close();
//    }
//
//    @Test
//    public void getPagesNumByAspose2() throws Exception {
//        Document doc = new Document("C:\\Users\\liaozihao\\Desktop\\9.13\\11pagesDOC.doc");
//        int num = doc.getPageCount();
//        System.out.println(num);
//    }
//
//    @Test
//    public void documents4j() throws FileNotFoundException {
//        InputStream docInputStream = new FileInputStream("C:\\Users\\liaozihao\\Desktop\\9.13\\个人简历.docx");
//        long start = System.currentTimeMillis();
//        FilePagesUtil.docWordToPdf(docInputStream, "C:\\Users\\liaozihao\\Desktop\\9.13\\个人简历.pdf");
//        System.out.println(System.currentTimeMillis() - start);
//    }
//
//    @Test
//    public void uploadBase64ToRedis() {
//        long start = System.currentTimeMillis();
//        String base64File = Base64.encode(new File("C:\\Users\\liaozihao\\Desktop\\9.13\\11pagesDOC.doc"));
//        redisUtil.set("小郎君.11pagesDOC.doc", base64File, 3600);
//        System.out.println(System.currentTimeMillis() - start);
//    }
//
//    @Test
//    public void downloadBase64ToRedis() {
//        long start = System.currentTimeMillis();
//        String base64File = (String) redisUtil.get("小郎君.11pagesDOC.doc");
//        byte[] byteArr = Base64.decode(base64File);
//        InputStream inputStream = new ByteArrayInputStream(byteArr);
//        String url = ossService.publishFileToOSS(inputStream, "小郎君.11pagesDOC.doc");
//        System.out.println(System.currentTimeMillis() - start);
//        System.out.println(url);
//    }
//
//    @Test
//    public void produceOrderId() {
//        String id1 = new DateTime().toString("yyyyMMdd");
//        System.out.println(id1);
//        String id2 = "";
//    }
//
//    @Test
//    public void getCountByToday() {
//        LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.like(Order::getId, new DateTime().toString("yyyyMMdd"));
//        long count = orderService.count(queryWrapper);
//        System.out.println(count);
//    }
}
