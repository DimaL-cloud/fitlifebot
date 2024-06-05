package ua.bibusukraine.fitlifebot.services.impl;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ua.bibusukraine.fitlifebot.annotation.ReportInclude;
import ua.bibusukraine.fitlifebot.services.ReportService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ReportServiceImpl implements ReportService {
  private static final Logger LOG = LoggerFactory.getLogger(ReportServiceImpl.class);
  private static final String DEFAULT_FILE_PATH = "activities.pdf";
  private static final String FONT_PATH = "fonts/Ubuntu-Regular.ttf";
  private Font font;

  @PostConstruct
  public void init() throws DocumentException, IOException {
    BaseFont bf = BaseFont.createFont(FONT_PATH, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
    font = new Font(bf);
  }

  @Override
  public <T> File getReport(List<T> items, Class<?> clazz) throws DocumentException, FileNotFoundException {
    Document document = createDocumentFrame(clazz);
    PdfPTable table = createTableFrame(clazz);
    addHeaders(table, clazz);
    fillTable(table, items, clazz);
    document.add(table);
    document.close();
    return new File(DEFAULT_FILE_PATH);
  }

  private Document createDocumentFrame(Class<?> itemClazz) throws FileNotFoundException, DocumentException {
    Document document = new Document();
    PdfWriter.getInstance(document, new FileOutputStream(DEFAULT_FILE_PATH));
    document.open();
    String titleText = itemClazz.isAnnotationPresent(ReportInclude.class) &&
        StringUtils.hasLength(itemClazz.getAnnotation(ReportInclude.class).title()) ?
        itemClazz.getAnnotation(ReportInclude.class).title() :
        itemClazz.getName();
    Paragraph title = new Paragraph(titleText, font);
    title.setSpacingAfter(20);
    title.setAlignment(Element.ALIGN_CENTER);
    document.add(title);
    return document;
  }

  private PdfPTable createTableFrame(Class<?> clazz) throws DocumentException {
    int[] columnWidth = Arrays.stream(clazz.getDeclaredFields())
            .filter(field -> field.isAnnotationPresent(ReportInclude.class))
            .map(field -> field.getAnnotation(ReportInclude.class)
                    .columnWidth()).mapToInt(Integer::intValue).toArray();

    PdfPTable table = new PdfPTable(columnWidth.length);
    table.setWidths(columnWidth);
    return table;
  }

  private <T> void fillTable(PdfPTable table, List<T> data, Class<?> clazz) {
    List<Field> writableFields = Arrays.stream(clazz.getDeclaredFields())
        .filter(field -> field.isAnnotationPresent(ReportInclude.class))
        .peek(field -> field.setAccessible(true))
        .toList();
    data.forEach(item -> writableFields.forEach(field -> {
      try {
        Object value = field.get(item);
        addCell(table, value != null ? value.toString() : "");
      } catch (IllegalAccessException e) {
        throw new RuntimeException(e);
      }
    }));
  }

  private void addHeaders(PdfPTable table, Class<?> clazz) {
    Arrays.stream(clazz.getDeclaredFields())
        .filter(field -> field.isAnnotationPresent(ReportInclude.class))
        .forEach(field -> {
          ReportInclude anno = field.getAnnotation(ReportInclude.class);
          String headerText = StringUtils.hasLength(anno.columnName()) ? anno.columnName() : field.getName();
          addCell(table, headerText);
        });
  }

  private void addCell(PdfPTable table, String text) {
    PdfPCell cell = new PdfPCell(new Phrase(text, font));
    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    table.addCell(cell);
  }
}
