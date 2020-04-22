package ouc.isclab.microservice.controller;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.RequestParam;
import ouc.isclab.microservice.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import ouc.isclab.microservice.feign.UserFeignClient;

import java.util.List;
import java.util.Map;

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

    @Autowired
    private UserFeignClient userFeignClient;

    @GetMapping("/user/{id}")
    public User findUserById(@PathVariable Long id) {
        // Ribbon负载均衡支持，通过RestTemplate请求微服务，返回数据自动封装为User
        // return restTemplate.getForObject("http://microservice-provider-user/" + id, User.class);
        // 使用Feign完成REST API调用
        return userFeignClient.findById(id);
    }

    @GetMapping("/users")
    public List<User> findUsers() {
        // Ribbon负载均衡支持，通过RestTemplate请求微服务，返回数据自动封装为List
        return restTemplate.getForObject("http://microservice-provider-user/users", List.class);
    }

    @GetMapping("/search")
    public User[] findByUserInfo(@RequestParam("name") String name, @RequestParam("age") Integer age) {
        Map<String, Object> paramMap = Maps.newHashMap();
        paramMap.put("name", name);
        paramMap.put("age", age);
        return restTemplate.getForObject("http://microservice-provider-user/search?name={name}&age={age}",
                User[].class, paramMap);
    }

    @GetMapping("/user-service-instance-log")
    public void logUserServiceInstance() {
        ServiceInstance serviceInstance = loadBalancerClient.choose("microservice-provider-user");
        log.info("{} : {} : {}", serviceInstance.getServiceId(), serviceInstance.getHost(), serviceInstance.getPort());
    }
}
