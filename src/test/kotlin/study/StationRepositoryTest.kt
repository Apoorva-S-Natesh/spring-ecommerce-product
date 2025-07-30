package study

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.jdbc.core.JdbcTemplate

@DataJpaTest
class StationRepositoryTest {

    @Autowired
    private lateinit var stations: StationRepository
    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    @Test
    fun save() {
        val expected = Station(name = "pankow", "Germany")
        val actual = stations.save(expected)
        assertThat(actual.id).isNotZero()
        assertThat(actual.name).isEqualTo(expected.name)
    }

    @Test
    fun findByName() {
        val expected = "pankow"
        stations.save(Station(name = expected, "germany"))
        val actual =
            stations.findByName(expected) // OR directly get the name:  val actual = stations.findByName(expected).get().name
        assertThat(actual.id).isNotZero()
        assertThat(actual.name).isEqualTo(expected)
    }

    @Test
    fun findByCountry() {
        val expected = "Germany"
        stations.save(Station(name = "Pankow", country = "Germany"))
        val actual =
            stations.findByCountry(expected) // OR directly get the name:  val actual = stations.findByName(expected).get().name
        assertThat(actual.id).isNotZero()
        assertThat(actual.country).isEqualTo(expected)
    }

    @Test
    fun identity() {
        val station1 = stations.save(Station(name = "pankow", country = "Germany"))
        val station2 = stations.findById(station1.id).get() // val station2 = stations.findById(station1.id!!).get()
        assertThat(station1 === station2).isTrue()
        /*
        insert
    into
        station
        (country, name, id)
    values
        (?, ?, default)
        * */
    }

    @Test
    fun test5() { //Persistence Context
        jdbcTemplate.execute("""insert into station(name, country, id) values("pankow", "germany", 3L""".trimIndent())

        val actual = stations.findByName("pankow")
        val actual2 = stations.findById(3L)
        assertThat(actual).isSameAs(actual2)
    }
    /*
    You're inserting data using jdbcTemplate, which bypasses the persistence context. So:

The persistence context is not aware of the inserted entity.
When you call stations.findByName, it will query the DB and load the entity into the context.
Then stations.findById will likely hit the DB again (unless Spring Data JPA caches it internally, which is not guaranteed).
So actual and actual2 will be equal in value but not necessarily the same object.
✅ How to Ensure They Are the Same?
To make sure both are managed by the same persistence context:

Wrap the test in a @Transactional annotation.
Avoid using jdbcTemplate for inserts—use JPA instead.

@Transactional
@Test
fun test5() {
    val station = Station(id = 3L, name = "pankow", country = "germany")
    stations.save(station)

    val actual = stations.findByName("pankow")
    val actual2 = stations.findById(3L).get()

    assertThat(actual).isSameAs(actual2) // Should pass
}

    * */

    @Test
    fun test6() {
        val save = stations.save(Station("pankow", "germany", 3L))
        assertThat(save.id).isEqualTo(3L)
    }//this test fails, also  has two select, with @Transactional it will be one select.
    //Transactional helps with delayed write or write behind. so we need transactional. Without transactional we dont use the cache.
    //make this class as @JpaTest and then this tests behaviour is different. Check what the Transactional Annotation does and the insert or select of sql in log

    @Test
    fun update() {
        val station1 = stations.save(Station("pankow", "Germany"))
        station1.changeName("oranienburger")
//        stations.flush()
//        val station2 = stations.findByName("oranienburger")
//        assertThat(station2).isNotNull()
        station1.changeName("pankow")
//        stations.flush()
        //check the sql insert, select - if flush is used and flush is not used. then can understand persistence context, transactional
    }
    /*
    finByName always hits the database, always has to be flushed
    can call clear on persisitence context
    * */

}
