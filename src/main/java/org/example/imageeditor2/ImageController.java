package org.example.imageeditor2;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class ImageController {

    private ImageFilterService imageFilterService;

    public ImageController(ImageFilterService imageFilterService) {
        this.imageFilterService = imageFilterService;
    }

    private byte[] mainImage;
    private byte[] finalImage;


    @PostMapping("/upload")
    public String createImg(@RequestParam("file") MultipartFile file) throws IOException {
        this.mainImage = file.getBytes();
        return "Success";
    }

    @GetMapping("/picture")
    public ResponseEntity<byte[]> getMainImage() {
        if (mainImage == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .body(mainImage);
    }
    @GetMapping("/filter/grey")
    public ResponseEntity<byte[]> getFilterGreyImage() throws IOException {
        if (mainImage == null) {
            return ResponseEntity.notFound().build();
        }
        finalImage = imageFilterService.greyFilter(mainImage);
        return ResponseEntity.ok()
                .body(finalImage);
    }

    @GetMapping("/filter/inversion")
    public ResponseEntity<byte[]> getFilterInversionImage() throws IOException {
        if (mainImage == null) {
            return ResponseEntity.notFound().build();
        }
        finalImage = imageFilterService.colorInversionFilter(mainImage);
        return ResponseEntity.ok().body(finalImage);
    }

    @GetMapping("/filter/blur")
    public ResponseEntity<byte[]> getImageBlur() throws IOException {
        if (mainImage == null) {
            return ResponseEntity.notFound().build();
        }
        finalImage = imageFilterService.blurEffect(mainImage);
        return ResponseEntity.ok().body(finalImage);
    }




}
