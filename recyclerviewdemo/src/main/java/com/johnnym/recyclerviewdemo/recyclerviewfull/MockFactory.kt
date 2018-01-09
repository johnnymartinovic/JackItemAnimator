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
                                "http://www.monologuedb.com/wp-content/uploads/2011/06/Samuel-L.-Jackson-Jules-Winnfield-Pulp-Fiction.png",
                                "Jules Winnfield",
                                3.587f,
                                getRandomDistanceValue())))
    }
}