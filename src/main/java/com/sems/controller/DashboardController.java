package com.sems.controller;

import com.sems.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {
    private final DashboardService svc;
    @GetMapping("/stats") public Map<String,Object> stats() { return svc.stats(); }
}
