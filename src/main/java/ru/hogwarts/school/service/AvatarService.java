package ru.hogwarts.school.service;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(AvatarService.class);
    @Value("${students.avatars.dir.path}")
    private String avatarsDir;

    private final StudentService studentService;
    private final AvatarRepository avatarRepository;

    public AvatarService(StudentService studentService, AvatarRepository avatarRepository, StudentRepository studentRepository) {
        this.studentService = studentService;
        this.avatarRepository = avatarRepository;
    }
    public void uploadAvatar(Long studentId, MultipartFile avatarFile) throws IOException {
        logger.info("Uploading avatar for student with id = {}", studentId);

        Student student = studentService.findStudent(studentId);

        Path filePath = buildFilePath(student, avatarFile.getOriginalFilename());
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);

        try (InputStream is = avatarFile.getInputStream();
             OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             BufferedOutputStream bos = new BufferedOutputStream(os, 1024);
             ) {
            bis.transferTo(bos);
        } catch (IOException e) {
            logger.error("Failed to upload avatar for student with id = {}", studentId, e);
            throw e;
        }

        saveInDB(studentId, student, filePath, avatarFile);
        logger.debug("Avatar uploaded and saved in DB for student with id = {}", studentId);
    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
    public Avatar findAvatar(Long studentId) {
        logger.debug("Finding avatar for student with id = {}", studentId);
        return avatarRepository.findByStudentId(studentId).orElseGet(() -> {
            logger.warn("Avatar not found for student with id = {}", studentId);
            return new Avatar();
        });
    }

    private Path buildFilePath(Student student, String fileName) {
        return Path.of(avatarsDir, student.getId() + "_" + student.getName() + "_" + getExtension(fileName));
    }

    private void saveInDB(Long studentId, Student student, Path filePath, MultipartFile avatarFile) throws IOException {
        Avatar avatar = findAvatar(studentId);

        avatar.setStudent(student);
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(avatarFile.getSize());
        avatar.setMediaType(avatarFile.getContentType());
        avatar.setData(avatarFile.getBytes());

        avatarRepository.save(avatar);
        logger.debug("Avatar information saved in database for student with id = {}", studentId);
    }
}