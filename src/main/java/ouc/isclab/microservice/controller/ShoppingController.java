package ouc.isclab.microservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
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
@Slf4j
@RestController
public class ShoppingController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @GetMapping("/user/{id}")
    public User findUserById(@PathVariable Long id) {
        // Ribbon负载均衡支持，通过RestTemplate请求微服务，返回数据自动封装为User
        return restTemplate.getForObject("http://microservice-provider-user/" + id, User.class);
    }

    @GetMapping("/users")
    public List<User> findUsers() {
        // Ribbon负载均衡支持，通过RestTemplate请求微服务，返回数据自动封装为List
        return restTemplate.getForObject("http://microservice-provider-user/users", List.class);
    }

    @GetMapping("/user-service-instance-log")
    public void logUserServiceInstance() {
        ServiceInstance serviceInstance = loadBalancerClient.choose("microservice-provider-user");
        log.info("{} : {} : {}", serviceInstance.getServiceId(), serviceInstance.getHost(), serviceInstance.getPort());
    }
}
