package speed.wagon.stackoverflowsearcher.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ApiTagsResponseDto {
    private List<ApiTagDto> items;
    private boolean hasMore;
}
