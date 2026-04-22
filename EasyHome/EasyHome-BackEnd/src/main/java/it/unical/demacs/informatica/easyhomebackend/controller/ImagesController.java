package it.unical.demacs.informatica.easyhomebackend.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequestMapping("/api/open/images")
public class ImagesController {



    @GetMapping("{folder}/{folder1}/{imageName}")
    public ResponseEntity<byte[]> getImage(@PathVariable String folder ,@PathVariable String folder1, @PathVariable String imageName) throws IOException {
        File imgFile = new File(folder + "/" + folder1 + "/" + imageName);


        if (!imgFile.exists()) {
            return ResponseEntity.notFound().build();
        }

        Path imagePath = imgFile.toPath();
        byte[] imageBytes = Files.readAllBytes(imagePath);
        String contentType = Files.probeContentType(imagePath);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(imageBytes);
    }
}
