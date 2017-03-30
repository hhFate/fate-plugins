package cn.reinforce.utils.office.html;

import org.apache.poi.hslf.usermodel.HSLFSlide;
import org.apache.poi.hslf.usermodel.HSLFSlideShow;
import org.apache.poi.hssf.converter.ExcelToHtmlConverter;
import org.apache.poi.hssf.usermodel.HSSFPictureData;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.PicturesManager;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.usermodel.Picture;
import org.apache.poi.hwpf.usermodel.PictureType;
import org.w3c.dom.Document;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Fate
 * @create 2017/3/30
 */
public class Office2HtmlUtil {
    //word 转 html
    public static void word2Html(String fileName, HttpServletResponse response)
            throws TransformerException, IOException,
            ParserConfigurationException {

        HWPFDocument wordDocument = new HWPFDocument(new FileInputStream(fileName));
//        HWPFDocumentCore documentCore = WordToHtmlUtils.loadDoc(new FileInputStream(fileName));
        //兼容2007 以上版本
//        XSSFWorkbook xssfwork=new XSSFWorkbook(new FileInputStream(fileName));
        WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(
                DocumentBuilderFactory.newInstance().newDocumentBuilder()
                        .newDocument());
        wordToHtmlConverter.setPicturesManager(new PicturesManager() {
            public String savePicture(byte[] content,
                                      PictureType pictureType, String suggestedName,
                                      float widthInches, float heightInches) {
                return "test/" + suggestedName;
            }
        });
        wordToHtmlConverter.processDocument(wordDocument);
        //save pictures
        List pics = wordDocument.getPicturesTable().getAllPictures();
        if (pics != null) {
            for (int i = 0; i < pics.size(); i++) {
                Picture pic = (Picture) pics.get(i);
                System.out.println();
                try {
                    pic.writeImageContent(new FileOutputStream("D:/test/"
                            + pic.suggestFullFileName()));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        Document htmlDocument = wordToHtmlConverter.getDocument();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DOMSource domSource = new DOMSource(htmlDocument);
        StreamResult streamResult = new StreamResult(out);


        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer serializer = tf.newTransformer();
        serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        serializer.setOutputProperty(OutputKeys.INDENT, "yes");
        serializer.setOutputProperty(OutputKeys.METHOD, "HTML");
        serializer.transform(domSource, streamResult);
        out.close();
        response.getOutputStream().write(out.toByteArray());
        response.getOutputStream().flush();
    }


    public static void excel2Html(String fileName, HttpServletResponse response)
            throws TransformerException, IOException,
            ParserConfigurationException {

        ToHtml toHtml = ToHtml.create(fileName, new PrintWriter(response.getOutputStream()));
        toHtml.setCompleteHTML(true);
        toHtml.printPage();
    }

    public static void excel2Html2(String fileName, HttpServletResponse response)
            throws TransformerException, IOException,
            ParserConfigurationException {

        InputStream input = new FileInputStream(fileName);
        HSSFWorkbook excelBook = new HSSFWorkbook(input);
        ExcelToHtmlConverter excelToHtmlConverter = new ExcelToHtmlConverter(DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
        excelToHtmlConverter.processWorkbook(excelBook);
        List<HSSFPictureData> pics = excelBook.getAllPictures();
        if (pics != null) {
            for (int i = 0; i < pics.size(); i++) {
                HSSFPictureData pic = pics.get(i);
                try {
                    File file = new File("D:/test/" + pic.suggestFileExtension());
                    FileOutputStream fos = new FileOutputStream(file);
                    fos.write(pic.getData());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        Document htmlDocument = excelToHtmlConverter.getDocument();
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        DOMSource domSource = new DOMSource(htmlDocument);
        StreamResult streamResult = new StreamResult(outStream);
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer serializer = tf.newTransformer();
        serializer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
        serializer.setOutputProperty(OutputKeys.INDENT, "yes");
        serializer.setOutputProperty(OutputKeys.METHOD, "html");
        serializer.transform(domSource, streamResult);
        outStream.close();

        response.getOutputStream().write(outStream.toByteArray());
        response.getOutputStream().flush();
    }

    public static void ppt2Image(String fileName, HttpServletResponse response) throws IOException {
        int slidenum = -1;
        float scale = 1.0F;

        FileInputStream is = new FileInputStream(fileName);
        HSLFSlideShow ppt = new HSLFSlideShow(is);
        is.close();
        Dimension pgsize = ppt.getPageSize();
        int width = (int) ((float) pgsize.width * scale);
        int height = (int) ((float) pgsize.height * scale);
        Iterator i$ = ppt.getSlides().iterator();
        List<String> filenames = new ArrayList<>();
        while (true) {
            HSLFSlide slide;
            do {
                if (!i$.hasNext()) {
                    return;
                }

                slide = (HSLFSlide) i$.next();
            } while (slidenum != -1 && slidenum != slide.getSlideNumber());

            String title = slide.getTitle();
            System.out.println("Rendering slide " + slide.getSlideNumber() + (title == null ? "" : ": " + title));
            BufferedImage img = new BufferedImage(width, height, 1);
            Graphics2D graphics = img.createGraphics();
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
            graphics.setPaint(Color.white);
            graphics.fill(new java.awt.geom.Rectangle2D.Float(0.0F, 0.0F, (float) width, (float) height));
            graphics.scale((double) width / (double) pgsize.width, (double) height / (double) pgsize.height);
            slide.draw(graphics);
            String fname = fileName.replaceAll("\\.ppt", "-" + slide.getSlideNumber() + ".png");
            filenames.add(fname);
//            FileOutputStream out = new FileOutputStream(fname);
            ImageIO.write(img, "png", response.getOutputStream());
            response.getOutputStream().flush();
//            out.close();
            if(!i$.hasNext()){
                break;
            }
        }

    }
}
