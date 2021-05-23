package wooteco.subway.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.dto.SectionRequest;

@DisplayName("지하철 구간 관련 기능")
@Sql("classpath:stations.sql")
public class SectionAcceptanceTest extends AcceptanceTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @DisplayName("구간 생성 - 성공")
    @Test
    public void createSection() throws Exception {
        //given
        SectionRequest sectionRequest = new SectionRequest(1L, 3L, 10);

        //when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
            .body(OBJECT_MAPPER.writeValueAsString(sectionRequest))
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/lines/1/sections")
            .then().log().all()
            .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }
}