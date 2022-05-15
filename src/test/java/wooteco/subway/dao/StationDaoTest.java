package wooteco.subway.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import wooteco.subway.domain.Station;

@JdbcTest
public class StationDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;

    private StationDao stationDao;

    @BeforeEach
    void setUp() {
        stationDao = new StationDao(jdbcTemplate, dataSource);
    }

    @Test
    @DisplayName("지하철역을 등록한다.")
    void saveTest() {
        Station station = stationDao.save(new Station("선릉역"));
        assertThat(station.getName()).isEqualTo("선릉역");
    }

    @Test
    @DisplayName("등록된 지하철역을 전체 조회한다.")
    void findAllTest() {
        Station expected1 = stationDao.save(new Station("선릉역"));
        Station expected2 = stationDao.save(new Station("역삼역"));
        Station expected3 = stationDao.save(new Station("강남역"));

        List<Long> actual = stationDao.findAll()
            .stream()
            .map(Station::getId)
            .collect(Collectors.toList());

        assertAll(
            () -> assertThat(actual).hasSize(3),
            () -> assertThat(actual).containsAll(List.of(expected1.getId(), expected2.getId(), expected3.getId()))
        );
    }

    @Test
    @DisplayName("ID로 특정 지하철역을 조회한다.")
    void findByIdTest() {
        Station expected = stationDao.save(new Station("짱구역"));
        Station actual = stationDao.findById(expected.getId()).get();

        assertAll(
            () -> assertThat(actual.getId()).isEqualTo(expected.getId()),
            () -> assertThat(actual.getName()).isEqualTo(expected.getName())
        );
    }

    @Test
    @DisplayName("ID로 특정 지하철역을 삭제한다.")
    void deleteTest() {
        final Station station = stationDao.save(new Station("강남역"));
        stationDao.deleteById(station.getId());

        assertThat(stationDao.findById(station.getId())).isEmpty();
    }
}
