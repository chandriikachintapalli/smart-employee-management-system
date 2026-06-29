package com.sems.controller;

import com.sems.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService svc;

    @GetMapping("/employees/pdf")
    public ResponseEntity<byte[]> pdf() {
        byte[] data = svc.employeesPdf();
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=employees.pdf")
            .contentType(MediaType.APPLICATION_PDF).body(data);
    }

    @GetMapping("/employees/excel")
    public ResponseEntity<byte[]> excel() {
        byte[] data = svc.employeesExcel();
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=employees.xlsx")
            .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
            .body(data);
    }
}
