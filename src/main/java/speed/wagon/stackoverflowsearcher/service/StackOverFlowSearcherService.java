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
    private static final String FILTER = "?order=desc&sort=reputation&site=stackoverflow&filter=!0Z-LvgkSiw7F*kKokgwZ_hl9g";
    private static final String FILTER_FOR_TAGS = "?&order=desc&sort=popular&site=stackoverflow&filter=!9ewGKXLQS";
    private static final String FIRST_PAGE = "&page=1";
    private static final String PAGE_WITHOUT_NUMBER = "&page=";
    private static final String TAGS_ENDPOINT = "/tags";
    private static final String MOLDOVA_COUNTRY = "Moldova";
    private static final String ROMANIA_COUNTRY = "Romania";
    private static final String JAVA = "java";
    private static final String C_SHARP = "C#";
    private static final String DOT_NET = ".net";
    private static final String DOCKER = "docker";
    private static final String REVERSE_SLASH = "/";
    private static final HttpClient httpClient = new HttpClient();
    private final List<String> urlComponents = new ArrayList<>();
    private final List<ApiUserDto> allUsers = new ArrayList<>();
    private final List<ApiUserDto> filteredUsers = new ArrayList<>();

    public List<ApiUserDto> parseUsers() {
        urlComponents.add(GENERAL_URL);
        urlComponents.add(FILTER);
        urlComponents.add(FIRST_PAGE);
        String finalUrl = String.join("", urlComponents);
        ApiUsersResponseDto apiResponseDto = httpClient.get(finalUrl, ApiUsersResponseDto.class);
        filterByReputationLocationAnswerCount(apiResponseDto);
        int i = 2;
        while (apiResponseDto.isHasMore()) {
            String pageNumber = PAGE_WITHOUT_NUMBER + i;
            urlComponents.set(2, pageNumber);
            finalUrl = String.join("", urlComponents);
            apiResponseDto = httpClient.get(finalUrl, ApiUsersResponseDto.class);
            if (apiResponseDto.getItems() != null) {
                filterByReputationLocationAnswerCount(apiResponseDto);
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

    private void filterByReputationLocationAnswerCount(ApiUsersResponseDto apiResponseDto) {
        apiResponseDto.getItems().stream()
            .filter(f -> f.getLocation() != null)
            .filter(f -> f.getLocation().contains(MOLDOVA_COUNTRY)
                    || f.getLocation().contains(ROMANIA_COUNTRY))
            .filter(u -> u.getReputation() != null)
            .filter(u -> u.getReputation() >= 223)
            .filter(u -> u.getAnswerCount() >= 1)
            .forEach(allUsers::add);
    }

    private Boolean parseAndFilterByTags(ApiUserDto user) {
        urlComponents.clear();
        urlComponents.add(GENERAL_URL + REVERSE_SLASH);
        urlComponents.add(user.getUser_id().toString());
        urlComponents.add(TAGS_ENDPOINT);
        urlComponents.add(FILTER_FOR_TAGS);
        urlComponents.add(FIRST_PAGE);
        String finalUrl = String.join("", urlComponents);
        ApiTagsResponseDto apiTagsResponseDto = httpClient.get(finalUrl, ApiTagsResponseDto.class);
        return filterByTags(apiTagsResponseDto);
    }

    private boolean filterByTags(ApiTagsResponseDto apiTagsResponseDto) {
        Optional<String> current = apiTagsResponseDto.getItems()
                .stream().map(ApiTagDto::getName)
                .filter(t -> t.contains(JAVA)
                        || t.contains(DOT_NET)
                        || t.contains(DOCKER)
                        || t.contains(C_SHARP))
                .findFirst();
        int i = 2;
        while (apiTagsResponseDto.isHasMore()) {
            if (current.isPresent()) {
                break;
            }
            String pageNumber = PAGE_WITHOUT_NUMBER.substring(1) + i;
            urlComponents.set(3, pageNumber);
            String subUrlNext = String.join("", urlComponents);
            apiTagsResponseDto = httpClient.get(subUrlNext, ApiTagsResponseDto.class);
            current = apiTagsResponseDto.getItems()
                    .stream().map(ApiTagDto::getName)
                    .filter(t -> t.contains(JAVA)
                            || t.contains(DOT_NET)
                            || t.contains(DOCKER)
                            || t.contains(C_SHARP))
                    .findFirst();
        }
        return current.isPresent();
    }
}
