package ru.hogwarts.school.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
@Transactional
public class AvatarService {
    @Value("${students.avatars.dir.path}")
    private String avatarsDir;

    private final StudentService studentService;
    private final AvatarRepository avatarRepository;

    public AvatarService(StudentService studentService, AvatarRepository avatarRepository, StudentRepository studentRepository) {
        this.studentService = studentService;
        this.avatarRepository = avatarRepository;
    }
    public void uploadAvatar(Long studentId, MultipartFile avatarFile) throws IOException {
        Student student = studentService.findStudent(studentId);

        Path filePath = Path.of(avatarsDir, studentId + "." + getExtension(avatarFile.getOriginalFilename()));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);

        try (InputStream is = avatarFile.getInputStream();
             OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             BufferedOutputStream bos = new BufferedOutputStream(os, 1024);
             ) {
            bis.transferTo(bos);
        }

        Avatar avatar = findAvatar(studentId);
        avatar.setStudent(student);
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(avatarFile.getSize());
        avatar.setMediaType(avatarFile.getContentType());
        avatar.setData(avatarFile.getBytes());
        avatarRepository.save(avatar);
    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

//    public byte[] getAvatarData(Long studentId) throws FileNotFoundException {
//        Avatar avatar = findAvatar(studentId);
//        if (avatar == null || avatar.getData() == null) {
//            throw new FileNotFoundException("Avatar not found for student id: " + studentId);
//        }
//        return avatar.getData();
//    }
//
//    public byte[] getAvatarFromFileSystem(Long studentId) throws IOException {
//        Avatar avatar = findAvatar(studentId);
//        if (avatar == null) {
//            throw new FileNotFoundException("Avatar not found for student id: " + studentId);
//        }
//        Path filePath = Path.of(avatar.getFilePath());
//        if (!Files.exists(filePath)) {
//            throw new FileNotFoundException("Avatar not found for student id: " + studentId);
//        }
//        return Files.readAllBytes(filePath);
//    }
//
    public Avatar findAvatar(Long studentId) {
        return avatarRepository.findByStudentId(studentId).orElseThrow();
    }
}
//
//    private byte[] generateImageData(Path filePath) throws IOException {
//        try (InputStream is = Files.newInputStream(filePath);
//             BufferedInputStream bis = new BufferedInputStream(is, 1024);
//             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
//            BufferedImage image = ImageIO.read(bis);
//
//            int height = image.getHeight() / (image.getWidth() / 100);
//            BufferedImage preview = new BufferedImage(100, height, image.getType());
//            Graphics2D graphics = preview.createGraphics();
//            graphics.drawImage(image, 0, 0, 100, height, null);
//            graphics.dispose();
//
//            ImageIO.write(preview, getExtension(filePath.getFileName().toString()), baos);
//            return baos.toByteArray();
//        }
//    }
