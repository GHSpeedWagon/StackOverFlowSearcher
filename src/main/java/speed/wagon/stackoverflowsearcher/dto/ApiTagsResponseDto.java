package speed.wagon.stackoverflowsearcher.dto;

import lombok.Data;

import java.util.List;

@Data
public class ApiTagsResponseDto {
    private List<ApiTagDto> items;
    private boolean has_more;
}
