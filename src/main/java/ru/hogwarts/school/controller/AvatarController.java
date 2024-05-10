package ru.hogwarts.school.controller;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.service.AvatarService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static sun.font.CreatedFontTracker.MAX_FILE_SIZE;

@RestController
@RequestMapping("/avatar")
public class AvatarController {
    private AvatarService avatarService;

    public AvatarController(AvatarService avatarService) {
        this.avatarService = avatarService;
    }

//    @PostMapping(value = "/{id}/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<String> uploadAvatar(@PathVariable Long id, @RequestParam MultipartFile avatar) throws IOException {
//        if (avatar.getSize() > MAX_FILE_SIZE) {
//            return ResponseEntity.badRequest().body("File is too big");
//        }
//        avatarService.uploadAvatar(id, avatar);
//        return ResponseEntity.ok().build();
//    }
//
//    @GetMapping(value = "/{id}/avatar/data")
//    public ResponseEntity<byte[]> downloadCover(@PathVariable Long studentId) {
//        Avatar avatar = avatarService.findAvatar(studentId);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.parseMediaType(avatar.getMediaType()));
//        headers.setContentLength(avatar.getData().length);
//
//        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(avatar.getData());
//    }
//
//    @GetMapping(value = "/{id}/avatar")
//    public void downloadAvatar (@PathVariable Long id, HttpServletResponse response) throws IOException {
//        Avatar avatar = avatarService.findAvatar(id);
//
//        Path path = Path.of(avatar.getFilePath());
//
//        try (InputStream is = Files.newInputStream(path);
//             OutputStream os = response.getOutputStream();) {
//            response.setContentType(avatar.getMediaType());
//            response.setContentLength((int) avatar.getFileSize());
//            is.transferTo(os);
//        }
//    }

    @PostMapping(value = "/{studentId}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadAvatar(@PathVariable Long studentId, @RequestParam MultipartFile avatar) throws IOException{
        avatarService.uploadAvatar(studentId, avatar);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/{id}/from-db")
    public ResponseEntity‹byte[]› downloadAvatar(@PathVariable Long id) {
        Avatar avatar = avatarService.findAvatar(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(avatar.getMediaType()));
        headers.setContentLength(avatar.getData().length);
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(avatar.getData());
    }


//    @GetMapping("/{id}/data")
//    public ResponseEntity<byte[]> getAvatarData(@PathVariable Long id) {
//        Avatar avatar = avatarService.findAvatar(id);
//        if (avatar == null || avatar.getData() == null) {
//            return ResponseEntity.notFound().build();
//        }
//        return ResponseEntity.ok()
//                .contentType(MediaType.parseMediaType(avatar.getMediaType()))
//                .body(avatar.getData());
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<byte[]> getAvatarFromFile(@PathVariable Long id) {
//        try {
//            byte[] imageData = avatarService.getAvatarFromFileSystem(id);
//            Avatar avatar = avatarService.findAvatar(id);
//            if (avatar == null) {
//                return ResponseEntity.notFound().build();
//            }
//            return ResponseEntity.ok()
//                    .contentType(MediaType.parseMediaType(avatar.getMediaType()))
//                    .body(imageData);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return ResponseEntity.notFound().build();
//        }
//    }
}
