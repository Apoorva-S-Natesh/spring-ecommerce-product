package study

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
class LineRepositoryTest {

    @Autowired
    private lateinit var stations: StationRepository
    @Autowired
    private lateinit var lines: LineRepository

    @Test
    fun findById() {
        val station = stations.save(Station("Pankow", "Germany"))
        val line = lines.save(Line("line1"))
        line.addStation(station)
        stations.flush()
    }
}
