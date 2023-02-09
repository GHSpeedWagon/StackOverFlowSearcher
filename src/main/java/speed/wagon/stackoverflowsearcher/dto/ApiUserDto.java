package speed.wagon.stackoverflowsearcher.dto;

import lombok.Data;

@Data
public class ApiUserDto {
    private Long user_id;
    private String display_name;
    private Long answer_count;
    private Long question_count;
    private Long reputation;
    private String location;
    private String profile_image;
}
