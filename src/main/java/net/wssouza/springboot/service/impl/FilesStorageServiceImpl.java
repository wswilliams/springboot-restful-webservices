package net.wssouza.springboot.service.impl;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.nio.file.StandardCopyOption;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import net.wssouza.springboot.entity.Order;
import net.wssouza.springboot.entity.Product;
import net.wssouza.springboot.entity.User;
import net.wssouza.springboot.utils.StorageException;
import org.springframework.beans.factory.annotation.Autowired;
import net.wssouza.springboot.service.FilesStorageService;
import net.wssouza.springboot.utils.StorageProperties;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FilesStorageServiceImpl implements FilesStorageService {

    private final Path rootLocation;

    @Autowired
    public FilesStorageServiceImpl(StorageProperties properties) {

        if(properties.getLocation().trim().length() == 0){
            throw new StorageException("File upload location can not be Empty.");
        }

        this.rootLocation = Paths.get(properties.getLocation());
    }

    @Override
    public String store(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file.");
            }
            Path destinationFile = this.rootLocation.resolve(
                            Paths.get(file.getOriginalFilename()))
                    .normalize().toAbsolutePath();
            if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
                // This is a security check
                throw new StorageException(
                        "Cannot store file outside current directory.");
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile,
                        StandardCopyOption.REPLACE_EXISTING);
            }
            System.out.println(destinationFile.getParent() + "/"+ destinationFile.getFileName());
            return destinationFile.getFileName().toString();
        }
        catch (IOException e) {
            throw new StorageException("Failed to store file.", e);
        }
    }

    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }


    @Override
    public List<User> openFile(String filename) {
        Path file = load(filename);
        File arq = new File(file.getParent().toString(), file.getFileName().toString());
        Map<Long, User> userMap = new HashMap<>();

        try {
            FileReader fileReader = new FileReader(arq);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String linha;
            while ( ( linha = bufferedReader.readLine() ) != null) {
//                    System.out.println(linha);
                    Long userId = Long.parseLong(linha.substring(0, 10).trim());
                    String userName = linha.substring(10, 55).trim();
                    Long orderId = Long.parseLong(linha.substring(56, 65).trim());
                    Long prodId = Long.parseLong(linha.substring(66, 75).trim());
                    double value = Double.parseDouble(linha.substring(76, 87).trim());
                    String dateString = linha.substring(87, 95).trim();
                    Date date = new SimpleDateFormat("yyyyMMdd").parse(dateString);

                    User _user = new User(userId, userName);

                    // Criar objetos
                        User user = userMap.computeIfAbsent(userId, k -> new User(userId, userName));
                        Order order = user.getOrder(orderId, date);
                        if (order == null) {
                            order = new Order(orderId, value, date);
                            user.addOrder(order);
                        }
                        Product product = order.getProduct(prodId);
                        if (product == null) {
                            product = new Product(prodId, value);
                           // product.addOrder(order);
                            order.addProduct(product);
                        }
            }
            fileReader.close();
            bufferedReader.close();

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return new ArrayList<>(userMap.values());
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        }
        catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }
}
