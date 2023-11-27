package net.wssouza.springboot.service;

import java.nio.file.Path;
import java.util.List;

import net.wssouza.springboot.entity.User;
import org.springframework.web.multipart.MultipartFile;
public interface FilesStorageService {

    void init();

    String store(MultipartFile file);

    Path load(String filename);

    List<User> openFile(String filename);

    void deleteAll();
}
