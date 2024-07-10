package org.example.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.dto.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

import static org.example.constants.Constants.LOGIN_URL;
import static org.example.constants.Constants.USER_URL;


public class ApiService {
    public List<UserDto> getUsersByName(String userName) throws URISyntaxException, IOException, InterruptedException {
        ObjectMapper mapper = new ObjectMapper();
        String encoded = URLEncoder.encode(userName, StandardCharsets.UTF_8.toString());
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(new URI(USER_URL+ "/users?name="+encoded))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request,HttpResponse.BodyHandlers.ofString());
        List<UserDto> users = mapper.readValue(response.body(), new TypeReference<List<UserDto>>() {});
        return users;
    }

    public  UserDto getUserById(int userId) throws IOException, InterruptedException, URISyntaxException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(new URI(USER_URL +LOGIN_URL+ userId))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        UserDto user = mapper.readValue(response.body(), UserDto.class);
        return user;
    }


    public List<UserDto> getAllUsers() throws URISyntaxException, IOException, InterruptedException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(new URI(USER_URL + "/users")).
                GET().
                build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<UserDto> users = mapper.readValue(response.body(), new TypeReference<List<UserDto>>(){});
        return users;
    }

    public  UserDto createUser(UserDto userDto) throws URISyntaxException, IOException, InterruptedException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String request = objectMapper.writeValueAsString(userDto);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder(new URI("https://jsonplaceholder.typicode.com/users"))
                .POST(HttpRequest.BodyPublishers.ofString(request))
                .header("Content-Type","application/json")
                .build();
        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        UserDto user = objectMapper.readValue(response.body(), UserDto.class);
        return user;
    }

    public UserDto updateUsers(int id , UserDto userDTO) throws URISyntaxException, IOException, InterruptedException
    {
        ObjectMapper mapper = new ObjectMapper();
        String request = mapper.writeValueAsString(userDTO);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder(new URI(USER_URL+LOGIN_URL+id))
                .PUT(HttpRequest.BodyPublishers.ofString(request))
                .header("Content-Type","application/json")
                .build();
        HttpResponse<String>response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        UserDto updUser = mapper.readValue(response.body(), UserDto.class);
        return updUser;
    }

    public int deleteUser(int userID) throws URISyntaxException, IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(new URI(USER_URL+LOGIN_URL+userID))
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request,HttpResponse.BodyHandlers.ofString());
        return response.statusCode();
    }

    public void getAndWritePostsInFileById(int postID) throws URISyntaxException, IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        ObjectMapper mapper = new ObjectMapper();

        HttpRequest request = HttpRequest.newBuilder(new URI(USER_URL+LOGIN_URL+postID+"/posts"))
                .build();

        HttpResponse<String> response = client.send(request,HttpResponse.BodyHandlers.ofString());
        var post = mapper.readValue(response.body(), new TypeReference<PostDto[]>() {});
        PostDto lastPost = Arrays.stream(post)
                .max(Comparator.comparing(PostDto::id))
                .orElseThrow(()-> new RuntimeException("Post not found"));

        HttpRequest commentsRequest = HttpRequest.newBuilder(new URI(USER_URL+"/posts/"+lastPost.id()+"/comments"))
                .GET()
                .build();

        HttpResponse<String> responseComm = client.send(commentsRequest,HttpResponse.BodyHandlers.ofString());
        String fileName = postID + "-post-" + lastPost.id() + "-comments.json";
        Files.writeString(Path.of(fileName), responseComm.body());
    }

    public List<TodoDto> getOpenTasksByUserId(int userID) throws URISyntaxException, IOException, InterruptedException
    {
        ObjectMapper mapper = new ObjectMapper();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(new URI(USER_URL+LOGIN_URL+userID+"/todos"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request,HttpResponse.BodyHandlers.ofString());
        List<TodoDto> todo = mapper.readValue(response.body(), new TypeReference<List<TodoDto>>() {});

        List<TodoDto> sortTodo = todo.stream()
                .filter(todos -> !todos.completed())
                .collect(Collectors.toList());
        return sortTodo;
    }
}
