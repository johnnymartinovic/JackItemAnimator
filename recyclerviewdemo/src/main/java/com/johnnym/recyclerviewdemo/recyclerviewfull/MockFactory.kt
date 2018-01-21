package com.johnnym.recyclerviewdemo.recyclerviewfull

import com.johnnym.recyclerviewdemo.recyclerviewfull.domain.TaxiList
import com.johnnym.recyclerviewdemo.recyclerviewfull.domain.TaxiListItem
import com.johnnym.recyclerviewdemo.recyclerviewfull.domain.TaxiStatus
import java.util.*

class TaxiListMockFactory {

    companion object {

        private val RANDOM_INSTANCE = Random()

        private fun getRandomTaxiStatusValue() = TaxiStatus.values()[RANDOM_INSTANCE.nextInt(TaxiStatus.values().size)]

        private fun getRandomDistanceValue() = RANDOM_INSTANCE.nextFloat() * 10

        fun createTaxiList(): TaxiList =
                TaxiList(mutableListOf(
                        TaxiListItem(
                                "id_00",
                                getRandomTaxiStatusValue(),
                                "https://i.pinimg.com/736x/54/a4/fc/54a4fc5e045b6cf0f4c52a4c58b508ed--jack-nicholson-october-.jpg",
                                "Jack Torrance",
                                3.545f,
                                getRandomDistanceValue()),
                        TaxiListItem(
                                "id_01",
                                getRandomTaxiStatusValue(),
                                "https://www.taschen.com/media/images/640/default_pr_schapiro_taxi_driver_travis_bickle_1011041545_id_394863.jpg",
                                "Travis Bickle",
                                5.000f,
                                getRandomDistanceValue()),
                        TaxiListItem(
                                "id_02",
                                getRandomTaxiStatusValue(),
                                "https://upload.wikimedia.org/wikipedia/tr/1/19/Scarfaceinthefall.jpg",
                                "Tony Montana",
                                4.231f,
                                getRandomDistanceValue()),
                        TaxiListItem(
                                "id_03",
                                getRandomTaxiStatusValue(),
                                "https://pbs.twimg.com/profile_images/610719306847002624/MmvsAf-U.jpg",
                                "Keyser Söze",
                                4.544f,
                                getRandomDistanceValue()),
                        TaxiListItem(
                                "id_04",
                                getRandomTaxiStatusValue(),
                                "https://images-na.ssl-images-amazon.com/images/M/MV5BMTU4OTY2MTE3OF5BMl5BanBnXkFtZTcwMzc0Mzg4Mw@@._V1_.jpg",
                                "Man with No Name",
                                2.433f,
                                getRandomDistanceValue()),
                        TaxiListItem(
                                "id_05",
                                getRandomTaxiStatusValue(),
                                "https://atwistedsenseoftumour.files.wordpress.com/2014/03/patrick-bateman.jpg",
                                "Patrick Bateman",
                                4.654f,
                                getRandomDistanceValue()),
                        TaxiListItem(
                                "id_06",
                                getRandomTaxiStatusValue(),
                                "https://upload.wikimedia.org/wikipedia/en/thumb/d/df/Michaelcoreleone.jpg/220px-Michaelcoreleone.jpg",
                                "Michael Corleone",
                                3.2534f,
                                getRandomDistanceValue()),
                        TaxiListItem(
                                "id_07",
                                getRandomTaxiStatusValue(),
                                "https://static.giantbomb.com/uploads/original/1/14103/323221-1_1_.jpg",
                                "John McClane",
                                4.999f,
                                getRandomDistanceValue()),
                        TaxiListItem(
                                "id_08",
                                getRandomTaxiStatusValue(),
                                "https://pbs.twimg.com/profile_images/2403528821/Picture_28_400x400.png",
                                "Anton Chigurh",
                                0.6565f,
                                getRandomDistanceValue()),
                        TaxiListItem(
                                "id_09",
                                getRandomTaxiStatusValue(),
                                "http://www.behindthevoiceactors.com/_img/chars/buzz-lightyear-toy-story-1.37.jpg",
                                "Buzz Lightyear",
                                1.43f,
                                getRandomDistanceValue()),
                        TaxiListItem(
                                "id_10",
                                getRandomTaxiStatusValue(),
                                "https://upload.wikimedia.org/wikipedia/commons/thumb/f/f6/HAL9000.svg/1200px-HAL9000.svg.png",
                                "HAL 9000",
                                2.43f,
                                getRandomDistanceValue()),
                        TaxiListItem(
                                "id_11",
                                getRandomTaxiStatusValue(),
                                "http://www.monologuedb.com/wp-content/uploads/2011/06/Samuel-L.-Jackson-Jules-Winnfield-Pulp-Fiction.png",
                                "Jules Winnfield",
                                3.587f,
                                getRandomDistanceValue()),
                        TaxiListItem(
                                "id_12",
                                getRandomTaxiStatusValue(),
                                "https://vignette.wikia.nocookie.net/lotrfanon/images/c/c0/Legolas.jpg",
                                "Legolas",
                                4.433f,
                                getRandomDistanceValue()),
                        TaxiListItem(
                                "id_13",
                                getRandomTaxiStatusValue(),
                                "https://upload.wikimedia.org/wikipedia/commons/9/90/Charlton_Heston_in_Ben_Hur_trailer.jpg",
                                "Judah Ben-Hur",
                                4.997f,
                                getRandomDistanceValue()),
                        TaxiListItem(
                                "id_14",
                                getRandomTaxiStatusValue(),
                                "http://s3.amazonaws.com/digitaltrends-uploads-prod/2015/06/indiana-jones.jpg",
                                "Indiana Jones",
                                2.429f,
                                getRandomDistanceValue()),
                        TaxiListItem(
                                "id_15",
                                getRandomTaxiStatusValue(),
                                "http://cdn1us.denofgeek.com/sites/denofgeekus/files/styles/main_wide/public/2015/12/terminator-2-arnold-schwarzenegger.jpg",
                                "The Terminator",
                                3.587f,
                                getRandomDistanceValue()),
                        TaxiListItem(
                                "id_16",
                                getRandomTaxiStatusValue(),
                                "https://vignette.wikia.nocookie.net/thebiglebowski/images/7/7e/The_Dude.jpeg",
                                "The Dude",
                                1.000f,
                                getRandomDistanceValue()),
                        TaxiListItem(
                                "id_17",
                                getRandomTaxiStatusValue(),
                                "https://vignette.wikia.nocookie.net/homealone/images/8/80/Oh_no.jpg",
                                "Kevin McCallister",
                                1.232f,
                                getRandomDistanceValue())))
    }
}