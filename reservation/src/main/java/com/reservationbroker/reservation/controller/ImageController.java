package com.reservationbroker.reservation.controller;

import com.reservationbroker.reservation.entities.Setting;
import com.reservationbroker.reservation.services.SettingsService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/images")
public class ImageController {

    private static final String UPLOAD_DIR = "./uploaded_images/";
    private final Path rootLocation = Paths.get(UPLOAD_DIR);
    private final SettingsService settingsService;

    // Upload single image and save the filename in the settings table
    @PostMapping("/upload")
    @PreAuthorize("hasAnyRole('SADMIN', 'CADMIN')")
    public ResponseEntity<Map<String, Object>> uploadImage(@RequestParam("file") MultipartFile file,
                                                           @RequestParam("companyId") Long companyId,
                                                           @RequestParam("columnName") String columnName) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (!Files.exists(rootLocation)) {
                Files.createDirectories(rootLocation);
                System.out.println("Created directory: " + rootLocation.toAbsolutePath());
            }

            // Generate unique filename
            String originalFilename = Objects.requireNonNull(file.getOriginalFilename());
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String uniqueFilename = "company_" + companyId + "_" + UUID.randomUUID().toString() + extension;

            // Save the file to the filesystem
            Path targetLocation = rootLocation.resolve(uniqueFilename);
            System.out.println("Saving file to: " + targetLocation.toAbsolutePath());
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // Save the filename in the database
            settingsService.saveImageUrl(companyId, uniqueFilename, columnName);

            // Construct the image URL
            String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
            String imageUrl = baseUrl +"/api"+ "/images/" + uniqueFilename;

            response.put("message", "Image uploaded successfully.");
            response.put("imageUrl", imageUrl);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (IOException e) {
            response.put("error", "Error occurred while uploading the image: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Delete image and remove the filename from the settings table
    @DeleteMapping("/delete/{filename}")
    @PreAuthorize("hasAnyRole('SADMIN', 'CADMIN')")
    public ResponseEntity<Map<String, Object>> deleteImage(@PathVariable String filename, @RequestParam("companyId") Long companyId) {
        Map<String, Object> response = new HashMap<>();

        // Remove any leading or trailing slashes from the filename
        filename = filename.replaceAll("^/+", "").replaceAll("/+$", "");

        Path path = rootLocation.resolve(filename);

        if (Files.exists(path)) {
            try {
                Files.delete(path);

                settingsService.removeImageUrl(companyId, filename);  // Remove the filename from the database

                response.put("message", "Image deleted successfully.");
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } catch (IOException e) {
                response.put("error", "Error occurred while deleting the image: " + e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        } else {
            response.put("error", "Image not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    // Remove the serveFile method to avoid conflicts with static resource handler

    // Upload multiple images and save them in the filesystem
    @PostMapping(value = "/uploadMultiple", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('SADMIN', 'CADMIN')")
    public ResponseEntity<Map<String, Object>> uploadMultipleImages(@RequestParam("files") MultipartFile[] files,
                                                                    @RequestParam("companyId") Long companyId) {
        Map<String, Object> response = new HashMap<>();
        List<String> uploadedImages = new ArrayList<>();

        try {
            Setting setting = settingsService.getSettingsByCompanyId(companyId)
                    .orElseThrow(() -> new RuntimeException("Settings not found"));

            int currentImageCount = setting.getNumberOfImages();
            int totalImagesAfterUpload = currentImageCount + files.length;

            if (totalImagesAfterUpload > 10) {
                response.put("error", "Cannot add more than 10 images.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            for (MultipartFile file : files) {
                String originalFilename = Objects.requireNonNull(file.getOriginalFilename());
                String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
                String uniqueFilename = "company_" + companyId + "_" + UUID.randomUUID().toString() + extension;

                // Save the file to the filesystem
                Path targetLocation = rootLocation.resolve(uniqueFilename);
                Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

                // Save each image URL to the setting
                setting.addImageUrl(uniqueFilename);
                uploadedImages.add(uniqueFilename);
            }

            // Save the updated settings
            settingsService.saveSettings(setting);

            // Construct full URLs
            String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
            List<String> uploadedImageUrls = new ArrayList<>();
            for (String filename : uploadedImages) {
                uploadedImageUrls.add(baseUrl +"/api"+ "/images/" + filename);
            }

            response.put("message", "Images uploaded successfully.");
            response.put("uploadedImages", uploadedImageUrls);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (IOException e) {
            response.put("error", "Error occurred while uploading images: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Get all images (logo, background, and additional images) for a company
    @GetMapping("/company/{companyId}/images")
    @PreAuthorize("hasAnyRole('SADMIN', 'CADMIN', 'CUSTOMER')")
    public ResponseEntity<Map<String, Object>> getImagesForCompany(@PathVariable Long companyId) {
        Map<String, Object> imageUrls = new HashMap<>();

        try {
            Setting setting = settingsService.getSettingsByCompanyId(companyId)
                    .orElseThrow(() -> new RuntimeException("Settings not found for company"));

            String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();

            // Prepare URLs for logo, background, and additional images
            if (setting.getImageUrlLogo() != null) {
                imageUrls.put("logo", baseUrl +"/api"+ "/images/" + setting.getImageUrlLogo());
            }

            if (setting.getImageUrlBackground() != null) {
                imageUrls.put("background", baseUrl +"/api"+ "/images/" + setting.getImageUrlBackground());
            }

            // Load additional images
            List<String> additionalImages = setting.getImageUrl10();  // This is the list of up to 10 images
            List<String> additionalImageUrls = new ArrayList<>();
            for (String filename : additionalImages) {
                additionalImageUrls.add(baseUrl +"/api"+ "/images/" + filename);
            }
            imageUrls.put("additionalImages", additionalImageUrls);

            return ResponseEntity.ok(imageUrls);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}


