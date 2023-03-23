package speed.wagon.stackoverflowsearcher.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ApiUsersResponseDto {
    private List<ApiUserDto> items;
    private boolean hasMore;
    private Long quotaMax;
    private Long quotaRemaining;
}
