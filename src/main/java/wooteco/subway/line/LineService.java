package wooteco.subway.line;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import wooteco.subway.exception.line.LineDuplicateException;
import wooteco.subway.exception.line.LineNotExistException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LineService {

    private final LineDao lineDao;

    public LineService(LineDao lineDao) {
        this.lineDao = lineDao;
    }

    public LineResponse create(String color, String name) {
        try {
            return new LineResponse(lineDao.insert(color, name));
        } catch (DataIntegrityViolationException e) {
            throw new LineDuplicateException();
        }
    }

    public List<LineResponse> showLines() {
        List<Line> lines = lineDao.findAll();
        return lines.stream()
                .map(LineResponse::new)
                .collect(Collectors.toList());
    }

    public LineResponse showLine(Long id) {
        Line line = lineDao.findById(id)
                .orElseThrow(LineNotExistException::new);
        return new LineResponse(line);
    }

    public void updateById(Long id, String color, String name) {
        lineDao.update(id, color, name);
    }

    public void deleteById(Long id) {
        lineDao.delete(id);
    }
}
