package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.response.*;

import java.io.File;
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
import java.sql.SQLOutput;
import java.util.*;
import java.util.stream.Collectors;


public class UsersMenu {
    private static final String USER_URL =  "https://jsonplaceholder.typicode.com";


    public static void nameInfo() throws URISyntaxException, IOException, InterruptedException {
        System.out.println("Write user Name");
        Scanner sc = new Scanner(System.in);
        String userName = sc.nextLine();
        ObjectMapper mapper = new ObjectMapper();
        String encoded = URLEncoder.encode(userName, StandardCharsets.UTF_8.toString());

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(new URI(USER_URL+ "/users?name="+encoded))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request,HttpResponse.BodyHandlers.ofString());
        List<UserDTO> user = mapper.readValue(response.body(), new TypeReference<List<UserDTO>>() {});
        user.forEach(System.out::println);
    }

    public static void idInfo() throws IOException, InterruptedException, URISyntaxException {
        System.out.println("Write user ID");
        Scanner sc = new Scanner(System.in);
        int usersID = sc.nextInt();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(new URI(USER_URL + "/users/"+ usersID))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        UserDTO user = mapper.readValue(response.body(), UserDTO.class);
        System.out.println(user);
    }


    public static void All() throws URISyntaxException, IOException, InterruptedException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(new URI(USER_URL + "/users")).
                GET().
                build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<UserDTO> users = mapper.readValue(response.body(), new TypeReference<List<UserDTO>>(){});
        users.forEach(System.out::println);
    }

    public static void createUser() throws URISyntaxException, IOException, InterruptedException {
        UserDTO userDTO = UserDTO.builder()
                .id(1)
                .name("John Doe")
                .email("john.doe@example.com")
                .phone("+4219073457")
                .address(AddressDTO.builder()
                        .city("New York")
                        .geo(GeoDTO.builder()
                                .lng(BigDecimal.valueOf(3123121))
                                .lat(BigDecimal.valueOf(3221123))
                                .build())
                        .suite("Apt. 559")
                        .street("Wolf Street")
                        .zipcode("84100")
                        .build())
                .username("Willy")
                .website("arronconsalt.com")
                .company(CompanyDTO.builder()
                        .bs("synergize scalable supply-chains")
                        .catchPhrase("Proactive contingency")
                        .name("Crew")
                        .build())
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String request = objectMapper.writeValueAsString(userDTO);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder(new URI("https://jsonplaceholder.typicode.com/users"))
                .POST(HttpRequest.BodyPublishers.ofString(request))
                .header("Content-Type","application/json")
                .build();
        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println("Response.statusCode() = " + response.statusCode());
        System.out.println("Response Body: " + response.body());
        UserDTO user = objectMapper.readValue(response.body(),UserDTO.class);
        System.out.println(user);
    }

    public static void updateUsers(int id , UserDTO userDTO) throws URISyntaxException, IOException, InterruptedException
    {
        ObjectMapper mapper = new ObjectMapper();
        String request = mapper.writeValueAsString(userDTO);
        //System.out.println("Sending JSON: " + request);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder(new URI(USER_URL+"/users/"+id))
                .PUT(HttpRequest.BodyPublishers.ofString(request))
                .header("Content-Type","application/json")
                .build();
        HttpResponse<String>response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println("Response Status: " + response.statusCode());
        System.out.println("Response Body: " + response.body());
        UserDTO updUser = mapper.readValue(response.body(), UserDTO.class);
        System.out.println(updUser);
    }

    public static void deleteUser(int userID) throws URISyntaxException, IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(new URI(USER_URL+"/users/"+userID))
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request,HttpResponse.BodyHandlers.ofString());
        System.out.println("response.statusCode() = " + response.statusCode());
        System.out.println("Response Body: " + response.body());
    }

    public static void searchPost(int postID) throws URISyntaxException, IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        ObjectMapper mapper = new ObjectMapper();

        HttpRequest request = HttpRequest.newBuilder(new URI(USER_URL+"/users/"+postID+"/posts"))
                .build();

        HttpResponse<String> response = client.send(request,HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        var post = mapper.readValue(response.body(), new TypeReference<PostDTO[]>() {});
        PostDTO  lastPost = Arrays.stream(post)
                .max(Comparator.comparing(PostDTO::id))
                .orElseThrow(()-> new RuntimeException("Post not found"));

        HttpRequest commentsRequest = HttpRequest.newBuilder(new URI(USER_URL+"/posts/"+lastPost.id()+"/comments"))
                .GET()
                .build();

        HttpResponse<String> responseComm = client.send(commentsRequest,HttpResponse.BodyHandlers.ofString());
        System.out.println(responseComm);
        String fileName = postID + "-post-" + lastPost.id() + "-comments.json";
        Files.writeString(Path.of(fileName), responseComm.body());
    }

    public static void openTasks(int userID) throws URISyntaxException, IOException, InterruptedException
    {
        ObjectMapper mapper = new ObjectMapper();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(new URI(USER_URL+"/users/"+userID+"/todos"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request,HttpResponse.BodyHandlers.ofString());
        List<TodoDTO> todo = mapper.readValue(response.body(), new TypeReference<List<TodoDTO>>() {});

        List<TodoDTO> sortTodo = todo.stream()
                .filter(todos -> !todos.completed())
                .collect(Collectors.toList());

        sortTodo.forEach(todos -> System.out.println("Todo ID: " + todos.id() + ", Title: " + todos.title()));
    }
    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {
        //UsersMenu.All();
        //UsersMenu.idInfo();
       // UsersMenu.nameInfo();
        //createUser();
        /*UserDTO userDTO = UserDTO.builder()
                .id(1)
                .name("Jane Doe")
                .username("janedoe")
                .email("jane.doe@example.com")
                .phone("123-456-7890")
                .website("example.com")
                .address(AddressDTO.builder()
                        .street("123 Elm St")
                        .suite("Apt 9")
                        .city("Some City")
                        .zipcode("12345")
                        .geo(GeoDTO.builder()
                                .lat(BigDecimal.valueOf(40.7128))
                                .lng(BigDecimal.valueOf(-74.0060))
                                .build())
                        .build())
                .company(CompanyDTO.builder()
                        .name("Doe Enterprises")
                        .catchPhrase("Excellence in Everything")
                        .bs("innovate strategically")
                        .build())
                .build();
        updateUsers(3,userDTO);*/
        //UsersMenu.All();
        //deleteUser(4);
        //searchPost(4);
        openTasks(3);
    }


}
