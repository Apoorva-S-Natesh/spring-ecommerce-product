package study

import jakarta.persistence.*

@Entity // (1)
@Table(name = "station") // (2)
class Station(
    @Column(name = "name", nullable = false) // (3)
    var name: String,

    @Column(name = "country", nullable = false) // (3)
    var country: String,
    
    @Id // (4)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // (5)
    val id: Long = 0L, // 0 is better tha null, instead of val id: Long?, this is safer
) // (6)
//If the jpa plugin was not used then this class has to be made open class and we need to add a constructor, proxy class can also be added (but thats advanced)
{
    fun changeName(name:String){
        this.name = name
    }
}
