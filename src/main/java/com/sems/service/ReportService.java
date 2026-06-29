package com.sems.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.sems.entity.Employee;
import com.sems.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final EmployeeRepository empRepo;

    public byte[] employeesPdf() {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document doc = new Document(PageSize.A4.rotate(), 24, 24, 24, 24);
            PdfWriter.getInstance(doc, out);
            doc.open();
            com.itextpdf.text.Font title =
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.DARK_GRAY);
            doc.add(new Paragraph("Employee Report", title));
            doc.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(7);
            table.setWidthPercentage(100);
            String[] headers = {"ID","First Name","Last Name","Email","Job Title","Department","Status"};
            com.itextpdf.text.Font h =
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, BaseColor.WHITE);            for (String hh : headers) {
                PdfPCell c = new PdfPCell(new Phrase(hh, h));
                c.setBackgroundColor(new BaseColor(33,37,41));
                c.setPadding(6);
                table.addCell(c);
            }
            for (Employee e : empRepo.findAll()) {
                table.addCell(String.valueOf(e.getId()));
                table.addCell(safe(e.getFirstName()));
                table.addCell(safe(e.getLastName()));
                table.addCell(safe(e.getEmail()));
                table.addCell(safe(e.getJobTitle()));
                table.addCell(e.getDepartment() != null ? e.getDepartment().getName() : "-");
                table.addCell(e.getStatus() != null ? e.getStatus().name() : "-");
            }
            doc.add(table);
            doc.close();
            return out.toByteArray();
        } catch (Exception ex) {
            throw new RuntimeException("Failed generating PDF", ex);
        }
    }

    public byte[] employeesExcel() {
        try (Workbook wb = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = wb.createSheet("Employees");
            String[] headers = {"ID","First Name","Last Name","Email","Phone","Job Title","Salary","Department","Status","Hire Date"};
            Row head = sheet.createRow(0);
            CellStyle hs = wb.createCellStyle();
            org.apache.poi.ss.usermodel.Font f = wb.createFont();
            for (int i=0;i<headers.length;i++) {
                Cell c = head.createCell(i); c.setCellValue(headers[i]); c.setCellStyle(hs);
            }
            List<Employee> list = empRepo.findAll();
            for (int i=0;i<list.size();i++) {
                Employee e = list.get(i);
                Row r = sheet.createRow(i+1);
                r.createCell(0).setCellValue(e.getId() != null ? e.getId() : 0);
                r.createCell(1).setCellValue(safe(e.getFirstName()));
                r.createCell(2).setCellValue(safe(e.getLastName()));
                r.createCell(3).setCellValue(safe(e.getEmail()));
                r.createCell(4).setCellValue(safe(e.getPhone()));
                r.createCell(5).setCellValue(safe(e.getJobTitle()));
                r.createCell(6).setCellValue(e.getSalary() != null ? e.getSalary().doubleValue() : 0);
                r.createCell(7).setCellValue(e.getDepartment() != null ? e.getDepartment().getName() : "-");
                r.createCell(8).setCellValue(e.getStatus() != null ? e.getStatus().name() : "-");
                r.createCell(9).setCellValue(e.getHireDate() != null ? e.getHireDate().toString() : "-");
            }
            for (int i=0;i<headers.length;i++) sheet.autoSizeColumn(i);
            wb.write(out);
            return out.toByteArray();
        } catch (Exception ex) {
            throw new RuntimeException("Failed generating Excel", ex);
        }
    }

    private String safe(String s){ return s==null?"":s; }
}
