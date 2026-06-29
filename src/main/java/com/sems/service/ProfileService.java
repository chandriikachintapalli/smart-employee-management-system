package com.sems.service;

import com.sems.dto.ChangePasswordRequest;
import com.sems.entity.*;
import com.sems.exception.ResourceNotFoundException;
import com.sems.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.*;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final UserRepository userRepo;
    private final EmployeeRepository empRepo;
    private final PasswordEncoder encoder;

    @Value("${app.upload.dir}") private String uploadDir;

    public Employee currentProfile(String username) {
        var u = userRepo.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return empRepo.findByUserId(u.getId())
            .orElseThrow(() -> new ResourceNotFoundException("Employee profile not found"));
    }

    public Employee updateProfile(String username, Employee patch) {
        var e = currentProfile(username);
        if (patch.getFirstName() != null) e.setFirstName(patch.getFirstName());
        if (patch.getLastName()  != null) e.setLastName(patch.getLastName());
        if (patch.getPhone()     != null) e.setPhone(patch.getPhone());
        if (patch.getJobTitle()  != null) e.setJobTitle(patch.getJobTitle());
        return empRepo.save(e);
    }

    public void changePassword(String username, ChangePasswordRequest req) {
        var u = userRepo.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (!encoder.matches(req.getCurrentPassword(), u.getPassword()))
            throw new IllegalArgumentException("Current password is incorrect");
        u.setPassword(encoder.encode(req.getNewPassword()));
        userRepo.save(u);
    }

    public String uploadPicture(String username, MultipartFile file) {
        try {
            var e = currentProfile(username);
            Path dir = Paths.get(uploadDir);
            Files.createDirectories(dir);
            String original = file.getOriginalFilename() == null ? "img" : file.getOriginalFilename();
            String ext = original.contains(".") ? original.substring(original.lastIndexOf('.')) : "";
            String name = "profile_" + e.getId() + "_" + UUID.randomUUID() + ext;
            Path target = dir.resolve(name);
            file.transferTo(target.toFile());
            e.setProfilePicture("/uploads/" + name);
            empRepo.save(e);
            return e.getProfilePicture();
        } catch (Exception ex) {
            throw new RuntimeException("Upload failed: " + ex.getMessage(), ex);
        }
    }
}
