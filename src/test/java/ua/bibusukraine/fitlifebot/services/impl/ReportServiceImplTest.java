package ua.bibusukraine.fitlifebot.services.impl;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ua.bibusukraine.fitlifebot.model.Product;
import ua.bibusukraine.fitlifebot.service.impl.ReportServiceImpl;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ReportServiceImplTest {

  @Mock
  private Document document;


  @InjectMocks
  private ReportServiceImpl reportService;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void getReportShouldThrowExceptionWhenDocumentExceptionOccurs() throws DocumentException {
    when(document.add(any())).thenThrow(new DocumentException());

    assertThrows(DocumentException.class, () -> reportService.getReport(Collections.singletonList(new Product()), Product.class));
  }

  @Test
  public void initShouldNotThrowExceptionWhenFontIsLoadedSuccessfully() {
    assertDoesNotThrow(() -> reportService.init());
  }
}