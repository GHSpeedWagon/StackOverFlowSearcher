package speed.wagon.stackoverflowsearcher.service;

import org.springframework.stereotype.Service;
import speed.wagon.stackoverflowsearcher.dto.ApiUsersResponseDto;
import speed.wagon.stackoverflowsearcher.dto.ApiTagDto;
import speed.wagon.stackoverflowsearcher.dto.ApiTagsResponseDto;
import speed.wagon.stackoverflowsearcher.dto.ApiUserDto;
import speed.wagon.stackoverflowsearcher.util.http.HttpClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StackOverFlowSearcherService {
    private static final String GENERAL_URL = "https://api.stackexchange.com/2.3/users";
    private static final HttpClient httpClient = new HttpClient();
    private static List<String> urlComponents = new ArrayList<>();
    private List<ApiUserDto> allUsers = new ArrayList<>();
    private List<ApiUserDto> filteredUsers = new ArrayList<>();
    public List<ApiUserDto> parseUsers() {
        urlComponents.add(GENERAL_URL);
        urlComponents.add("?order=desc&sort=reputation&site=stackoverflow&filter=!0Z-LvgkSiw7F*kKokgwZ_hl9g");
        urlComponents.add("&page=1");
        String finalUrl = String.join("", urlComponents);
        ApiUsersResponseDto apiResponseDto = httpClient.get(finalUrl, ApiUsersResponseDto.class);
        filterByRepLocAns(apiResponseDto);
        int i = 2;
        while (apiResponseDto.isHas_more()) {
            String pageNumber = "&page=" + i;
            urlComponents.set(2, pageNumber);
            finalUrl = String.join("", urlComponents);
            apiResponseDto = httpClient.get(finalUrl, ApiUsersResponseDto.class);
            if (apiResponseDto.getItems() != null) {
                filterByRepLocAns(apiResponseDto);
            } else {
                break;
            }
            i++;
        }
        return getFormatedAndFilteredUsersList();
    }

    public List<ApiUserDto> getFormatedAndFilteredUsersList() {
        allUsers.stream()
                        .filter(this::parseAndFilterByTags).forEach(filteredUsers::add);
        return filteredUsers;
    }

    private void filterByRepLocAns(ApiUsersResponseDto apiResponseDto) {
        apiResponseDto.getItems().stream()
                .filter(f -> f.getLocation() != null)
                .filter(f -> f.getLocation().contains("Moldova") || f.getLocation().contains("Romania"))
                .filter(u -> u.getReputation() != null)
                .filter(u -> u.getReputation() >= 223)
                .filter(u -> u.getAnswer_count() >= 1)
                .forEach(allUsers::add);
    }

    private Boolean parseAndFilterByTags(ApiUserDto user) {
        urlComponents.clear();
        urlComponents.add(GENERAL_URL + "/");
        urlComponents.add(user.getUser_id().toString());
        urlComponents.add("/tags?&order=desc&sort=popular&site=stackoverflow&filter=!9ewGKXLQS&");
        urlComponents.add("page=1");
        String finalUrl = String.join("", urlComponents);
        ApiTagsResponseDto apiTagsResponseDto = httpClient.get(finalUrl, ApiTagsResponseDto.class);
        return filterByTags(apiTagsResponseDto);
    }

    private boolean filterByTags(ApiTagsResponseDto apiTagsResponseDto) {
        Optional<String> first = apiTagsResponseDto.getItems()
                .stream().map(ApiTagDto::getName)
                .filter(t -> t.contains("java") || t.contains(".net") || t.contains("docker") || t.contains("C#"))
                .findFirst();
        int i = 2;
        while (apiTagsResponseDto.isHas_more()) {
            if (first.isPresent()) {
                break;
            }
            String pageNumber = "page=" + i;
            urlComponents.set(3, pageNumber);
            String subUrlNext = String.join("", urlComponents);
            apiTagsResponseDto = httpClient.get(subUrlNext, ApiTagsResponseDto.class);
            first = apiTagsResponseDto.getItems()
                    .stream().map(ApiTagDto::getName)
                    .filter(t -> t.contains("java") || t.contains(".net") || t.contains("docker") || t.contains("C#"))
                    .findFirst();
        }
        return first.isPresent();
    }
}
