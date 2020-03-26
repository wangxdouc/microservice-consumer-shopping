package ouc.isclab.microservice.controller;

import ouc.isclab.microservice.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * 服务消费者控制器
 */
@RestController
public class ShoppingController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/user/{id}")
    public User findUserById(@PathVariable Long id) {
        // 硬编码URL，通过RestTemplate请求微服务，返回数据自动封装为User
        return restTemplate.getForObject("http://localhost:8000/user/" + id, User.class);
    }

    @GetMapping("/users")
    public List<User> findUsers() {
        // 硬编码URL，通过RestTemplate请求微服务，返回数据自动封装为List
        return restTemplate.getForObject("http://localhost:8000/users", List.class);
    }
}
