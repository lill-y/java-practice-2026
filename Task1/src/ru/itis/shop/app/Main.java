package ru.itis.shop.app;

import ru.itis.shop.user.api.UserConsoleOperations;
import ru.itis.shop.user.infrastructure.UserFileRepository;

public class Main {
    static void main(String[] args) {
        UserFileRepository userFileRepository = new UserFileRepository("users.txt");
        UserConsoleOperations operations = new UserConsoleOperations(userFileRepository);

        while(true){
            operations.showMenu();
        }
    }
}