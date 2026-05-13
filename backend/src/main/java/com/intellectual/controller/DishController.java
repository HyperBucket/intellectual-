package com.intellectual.controller;

import com.intellectual.dto.*;
import com.intellectual.service.DishService;
import com.intellectual.service.PhotoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/dishes")
@RequiredArgsConstructor
public class DishController {

    private final DishService dishService;
    private final PhotoService photoService;

    // ── List ────────────────────────────────────────────────────────────────

    @GetMapping
    public List<DishResponse> list(
            @RequestParam(defaultValue = "ORDER_COUNT") SortBy sortBy,
            @RequestParam(defaultValue = "LAST_MONTH")  TimePeriod timePeriod) {
        return dishService.listDishes(sortBy, timePeriod);
    }

    // ── CRUD ────────────────────────────────────────────────────────────────

    @PostMapping
    public ResponseEntity<DishResponse> create(@Valid @RequestBody DishRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(dishService.createDish(request));
    }

    @PutMapping("/{id}")
    public DishResponse update(@PathVariable Long id, @Valid @RequestBody DishRequest request) {
        return dishService.updateDish(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        dishService.deleteDish(id);
        return ResponseEntity.noContent().build();
    }

    // ── Photo ───────────────────────────────────────────────────────────────

    @GetMapping("/{id}/photo")
    public ResponseEntity<byte[]> getPhoto(@PathVariable Long id) {
        byte[] data = photoService.getPhoto(id);
        if (data == null) return ResponseEntity.notFound().build();

        String contentType = photoService.getContentType(id);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(
                        contentType != null ? contentType : MediaType.APPLICATION_OCTET_STREAM_VALUE))
                .cacheControl(CacheControl.maxAge(java.time.Duration.ofMinutes(30)))
                .body(data);
    }

    @PostMapping(value = "/{id}/photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> uploadPhoto(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) throws IOException {
        photoService.savePhoto(id, file);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/photo")
    public ResponseEntity<Void> deletePhoto(@PathVariable Long id) {
        photoService.deletePhoto(id);
        return ResponseEntity.noContent().build();
    }
}
