package org.example;

import org.example.dto.UserDto;
import org.example.service.ApiService;
import org.example.utils.UserUtils;

import java.io.IOException;
import java.net.URISyntaxException;

public class Main {
    public static void main(String[] args) throws IOException, URISyntaxException, InterruptedException {
        ApiService apiService = new ApiService();
        UserDto getUser = UserUtils.getUser();
        UserDto getNewUser = UserUtils.getNewUser();

        System.out.println(apiService.getAllUsers());
        System.out.println(apiService.getUserById(5));
        System.out.println(apiService.getUsersByName("Leanne Graham"));
        System.out.println(apiService.createUser(getNewUser));
        System.out.println(apiService.updateUsers(3, getUser));
        System.out.println("Status code after delete:" + apiService.deleteUser(4));
        apiService.getAndWritePostsInFileById(4);
        System.out.println(apiService.getOpenTasksByUserId(3));
    }
}
