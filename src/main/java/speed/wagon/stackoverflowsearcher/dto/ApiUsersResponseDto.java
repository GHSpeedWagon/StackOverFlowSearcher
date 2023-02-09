package speed.wagon.stackoverflowsearcher.dto;

import lombok.Data;

import java.util.List;

@Data
public class ApiUsersResponseDto {
    private List<ApiUserDto> items;
    private boolean has_more;
    private Long quota_max;
    private Long quota_remaining;
}
