package speed.wagon.stackoverflowsearcher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import speed.wagon.stackoverflowsearcher.dto.ApiUserDto;
import speed.wagon.stackoverflowsearcher.service.StackOverFlowSearcherService;

import java.util.List;

@SpringBootApplication
public class StackOverFlowSearcherApplication {
    public static void main(String[] args) {
        SpringApplication.run(StackOverFlowSearcherApplication.class, args);
        StackOverFlowSearcherService stackOverFlowSearcherService = new StackOverFlowSearcherService();
        List<ApiUserDto> apiUserDtoList = stackOverFlowSearcherService.parseUsers();
        apiUserDtoList.forEach(System.out::println);
    }

}
