package wooteco.subway.dao.line;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import wooteco.subway.domain.Line;

@Repository
public class LineDaoH2 implements LineDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final RowMapper<Line> rowMapper;

    public LineDaoH2(final JdbcTemplate jdbcTemplate, final DataSource source) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(source)
            .withTableName("LINE")
            .usingGeneratedKeyColumns("id");
        this.rowMapper = (rs, rowNum) -> {
            final Long foundId = rs.getLong("id");
            final String name = rs.getString("name");
            final String color = rs.getString("color");
            return new Line(foundId, name, color);
        };
    }

    @Override
    public Line create(final Line line) {
        Map<String, String> params = new HashMap<>();
        params.put("name", line.getName());
        params.put("color", line.getColor());

        long key = jdbcInsert.executeAndReturnKey(params).longValue();

        return new Line(key, line.getName(), line.getColor());
    }

    @Override
    public Line show(final Long id) {
        String sql = "SELECT * FROM LINE WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    @Override
    public List<Line> showAll() {
        String sql = "SELECT * FROM LINE";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public int update(final long id, final Line line) {
        String sql = "UPDATE LINE SET name = ?, color = ? WHERE id = ?";
        return jdbcTemplate.update(sql, line.getName(), line.getColor(), id);
    }

    @Override
    public int delete(final long id) {
        String sql = "DELETE FROM LINE WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}