package study

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity // (1)
@Table(name = "station") // (2)
class Station(
    @Column(name = "name", nullable = false) // (3)
    var name: String,

    @Column(name = "country", nullable = false) // (3)
    var country: String,

    @ManyToOne (cascade = [CascadeType.PERSIST])// (1) //Owner id the one which has foreign key
    @JoinColumn(name = "line_id") // (2)
    var line: Line? = null,

    @Id // (4)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // (5)
    val id: Long = 0L, // 0 is better tha null, instead of val id: Long?, this is safer
) // (6)
//If the jpa plugin was not used then this class has to be made open class and we need to add a constructor, proxy class can also be added (but thats advanced)
{
    fun changeName(name:String){
        this.name = name
    }

    fun updateLine(line:Line) {
        this.line = line
        line.stations.add(this)
    }
}
