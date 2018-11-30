package com.johnnym.recyclerviewdemo.recyclerviewfull

import com.johnnym.recyclerviewdemo.recyclerviewfull.domain.TaxiList
import com.johnnym.recyclerviewdemo.recyclerviewfull.domain.TaxiListItem
import com.johnnym.recyclerviewdemo.recyclerviewfull.domain.TaxiStatus
import java.lang.IllegalStateException

class TaxiListMockFactory {

    companion object {

        private const val INSTANCE_MOD_VALUE = 2

        fun createTaxiList(currentInstanceNumber: Int): TaxiList {
            val currentInstanceNumberMod = currentInstanceNumber % INSTANCE_MOD_VALUE

            @Suppress("UnnecessaryVariable")
            val taxiList = when (currentInstanceNumberMod) {
                0 -> TaxiList(listOf(
                        JackTorrance.createCopy(TaxiStatus.AVAILABLE, 5f),
                        TravisBickle.createCopy(TaxiStatus.OCCUPIED, 3f)
                ))
                1 -> TaxiList(listOf(
                ))
                2 -> TaxiList(listOf(
                ))
                3 -> TaxiList(listOf(
                ))
                4 -> TaxiList(listOf(
                ))
                else -> throw IllegalStateException("$currentInstanceNumberMod should be between 0 (included) and $INSTANCE_MOD_VALUE")
            }

            return taxiList
        }

        private fun TaxiListItem.createCopy(
                taxiStatus: TaxiStatus,
                distance: Float
        ) = this.copy(
                taxiStatus = taxiStatus,
                distance = distance
        )

        private val JackTorrance = TaxiListItem(
                "id_00",
                TaxiStatus.AVAILABLE,
                "https://i.pinimg.com/736x/54/a4/fc/54a4fc5e045b6cf0f4c52a4c58b508ed--jack-nicholson-october-.jpg",
                "Jack Torrance",
                3.545f,
                1f)

        private val TravisBickle = TaxiListItem(
                "id_01",
                TaxiStatus.AVAILABLE,
                "https://www.taschen.com/media/images/640/default_pr_schapiro_taxi_driver_travis_bickle_1011041545_id_394863.jpg",
                "Travis Bickle",
                5.000f,
                1f)

        private val TonyMontana = TaxiListItem(
                "id_02",
                TaxiStatus.AVAILABLE,
                "https://upload.wikimedia.org/wikipedia/tr/1/19/Scarfaceinthefall.jpg",
                "Tony Montana",
                4.231f,
                1f)

        private val KeyserSoze = TaxiListItem(
                "id_03",
                TaxiStatus.AVAILABLE,
                "https://pbs.twimg.com/profile_images/610719306847002624/MmvsAf-U.jpg",
                "Keyser Söze",
                4.544f,
                1f)

        private val ManwithNoName = TaxiListItem(
                "id_04",
                TaxiStatus.AVAILABLE,
                "https://images-na.ssl-images-amazon.com/images/M/MV5BMTU4OTY2MTE3OF5BMl5BanBnXkFtZTcwMzc0Mzg4Mw@@._V1_.jpg",
                "Man with No Name",
                2.433f,
                1f)

        private val PatrickBateman = TaxiListItem(
                "id_05",
                TaxiStatus.AVAILABLE,
                "https://atwistedsenseoftumour.files.wordpress.com/2014/03/patrick-bateman.jpg",
                "Patrick Bateman",
                4.654f,
                1f)

        private val MichaelCorleone = TaxiListItem(
                "id_06",
                TaxiStatus.AVAILABLE,
                "https://upload.wikimedia.org/wikipedia/en/thumb/d/df/Michaelcoreleone.jpg/220px-Michaelcoreleone.jpg",
                "Michael Corleone",
                3.2534f,
                1f)

        private val JohnMcClane = TaxiListItem(
                "id_07",
                TaxiStatus.AVAILABLE,
                "https://static.giantbomb.com/uploads/original/1/14103/323221-1_1_.jpg",
                "John McClane",
                4.999f,
                1f)

        private val AntonChigurh = TaxiListItem(
                "id_08",
                TaxiStatus.AVAILABLE,
                "https://pbs.twimg.com/profile_images/2403528821/Picture_28_400x400.png",
                "Anton Chigurh",
                0.6565f,
                1f)

        private val BuzzLightyear = TaxiListItem(
                "id_09",
                TaxiStatus.AVAILABLE,
                "http://www.behindthevoiceactors.com/_img/chars/buzz-lightyear-toy-story-1.37.jpg",
                "Buzz Lightyear",
                1.43f,
                1f)

        private val HAL9000 = TaxiListItem(
                "id_10",
                TaxiStatus.AVAILABLE,
                "https://upload.wikimedia.org/wikipedia/commons/thumb/f/f6/HAL9000.svg/1200px-HAL9000.svg.png",
                "HAL 9000",
                2.43f,
                1f)

        private val JulesWinnfield = TaxiListItem(
                "id_11",
                TaxiStatus.AVAILABLE,
                "http://www.monologuedb.com/wp-content/uploads/2011/06/Samuel-L.-Jackson-Jules-Winnfield-Pulp-Fiction.png",
                "Jules Winnfield",
                3.587f,
                1f)

        private val Legolas = TaxiListItem(
                "id_12",
                TaxiStatus.AVAILABLE,
                "https://vignette.wikia.nocookie.net/lotrfanon/images/c/c0/Legolas.jpg",
                "Legolas",
                4.433f,
                1f)

        private val JudahBenHur = TaxiListItem(
                "id_13",
                TaxiStatus.AVAILABLE,
                "https://upload.wikimedia.org/wikipedia/commons/9/90/Charlton_Heston_in_Ben_Hur_trailer.jpg",
                "Judah Ben-Hur",
                4.997f,
                1f)

        private val IndianaJones = TaxiListItem(
                "id_14",
                TaxiStatus.AVAILABLE,
                "http://s3.amazonaws.com/digitaltrends-uploads-prod/2015/06/indiana-jones.jpg",
                "Indiana Jones",
                2.429f,
                1f)

        private val TheTerminator = TaxiListItem(
                "id_15",
                TaxiStatus.AVAILABLE,
                "http://cdn1us.denofgeek.com/sites/denofgeekus/files/styles/main_wide/public/2015/12/terminator-2-arnold-schwarzenegger.jpg",
                "The Terminator",
                3.587f,
                1f)

        private val TheDude = TaxiListItem(
                "id_16",
                TaxiStatus.AVAILABLE,
                "https://vignette.wikia.nocookie.net/thebiglebowski/images/7/7e/The_Dude.jpeg",
                "The Dude",
                1.000f,
                1f)

        private val KevinMcCallister = TaxiListItem(
                "id_17",
                TaxiStatus.AVAILABLE,
                "https://vignette.wikia.nocookie.net/homealone/images/8/80/Oh_no.jpg",
                "Kevin McCallister",
                1.232f,
                1f)
    }
}
