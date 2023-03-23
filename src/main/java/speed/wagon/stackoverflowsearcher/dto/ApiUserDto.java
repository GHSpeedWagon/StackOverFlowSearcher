package speed.wagon.stackoverflowsearcher.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ApiUserDto {
    private Long user_id;
    private String displayName;
    private Long answerCount;
    private Long questionCount;
    private Long reputation;
    private String location;
    private String profileImage;
}
