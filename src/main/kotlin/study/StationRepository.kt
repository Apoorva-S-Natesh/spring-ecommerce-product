package study

import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface StationRepository : JpaRepository<Station, Long>{
    fun findByName(name: String): Station // or Station? or just Station or Optional<Station>
    fun findByCountry(country: String): Station
}
