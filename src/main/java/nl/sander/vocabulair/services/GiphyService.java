package nl.sander.vocabulair.services;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
@RequiredArgsConstructor
public class GiphyService {

    @Value("${giphy.api.key}")
    private String apiKey;
    @Value("${giphy.theme}}")
    private String theme;
    private final RestTemplateBuilder builder;

    public String getGiphyUrl() {
        RestTemplate template = builder.build();
        String url = "https://api.giphy.com/v1/gifs/random?api_key={apiKey}&tag={tag}&rating=g";
        Response response = template.getForObject(url, Response.class, apiKey, theme);
        String id = response.getData().getId();
        return String.format("https://i.giphy.com/%s.gif", id);
    }

    @Data
    static class Response {
        GifObject data;
    }

    @Data
    static class GifObject {
        Images images;
        String id;
        String image_original_url;
        String image_mp4_url;
        String fixed_height_downsampled_url;
    }

    @Data
    static class Images {
        Original original;
        FixedHeightStill fixed_height_still;
    }

    @Data
    static class Original {
        String url;
    }

    @Data
    static class FixedHeightStill {
        String url;
    }

}
