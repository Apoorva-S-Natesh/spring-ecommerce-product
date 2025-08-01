package study

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table(name = "line")
class Line(
    @Column(name = "name", nullable = false)
    var name: String,

    @OneToMany(mappedBy = "line") // (1) // Here Line is the owner //join column doesn't know the owner so mapper by is better
    var stations: MutableList<Station> = mutableListOf(), // (2)
    //foreign key always resides on the many side

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
) {
    fun addStation(station: Station) {
        stations.add(station)
        station.line = this
    }
}

//in one to many, the validation can be moved to object as station has lines. the validation can then  be in object and not service.
// this improves the business
