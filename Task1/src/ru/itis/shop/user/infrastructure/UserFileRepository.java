package ru.itis.shop.user.infrastructure;

import ru.itis.shop.user.domain.User;
import ru.itis.shop.user.repository.UserRepository;

import java.io.*;
import java.util.UUID;

public class UserFileRepository implements UserRepository {
    private final String fileName;


    public UserFileRepository(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void save(User user) {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            String id = UUID.randomUUID().toString();
            user.setId(id);
            writer.write(user.getId() + "|" +
                    user.getEmail() + "|" +
                    user.getPassword() + "|" +
                    user.getProfileDescription());
            writer.newLine();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
    @Override
    public User findById(String id) {
        try(BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line = reader.readLine();
            while (line != null) {
                var userData = line.split("\\|");
                if (id.equals(userData[0])) {
                    User user = new User(id, userData[1], userData[2], userData[3]);
                    return user;
                }
                line = reader.readLine();
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found");
            throw new RuntimeException(e);
        } catch (IOException e) {
            System.err.println("Error reading file");
            throw new RuntimeException(e);
        }
        return null;
    }
}
