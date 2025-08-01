package study

import org.springframework.data.jpa.repository.JpaRepository

interface LineRepository: JpaRepository<Line, Long> {
}
