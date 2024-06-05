package ua.bibusukraine.fitlifebot.services;

import com.itextpdf.text.DocumentException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

public interface ReportService {
  <T> File getReport(List<T> items, Class<?> clazz) throws DocumentException, FileNotFoundException;
}
