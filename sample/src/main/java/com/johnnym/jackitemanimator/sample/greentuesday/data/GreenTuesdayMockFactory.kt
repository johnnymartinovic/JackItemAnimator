package com.johnnym.jackitemanimator.sample.greentuesday.data

import com.johnnym.jackitemanimator.sample.greentuesday.domain.GreenTuesdayList
import com.johnnym.jackitemanimator.sample.greentuesday.domain.GreenTuesdayListItem
import java.lang.IllegalStateException

class GreenTuesdayMockFactory {

    companion object {

        private const val INSTANCE_MOD_VALUE = 2

        fun createGreenTuesdayList(currentInstanceNumber: Int): GreenTuesdayList {
            val currentInstanceNumberMod = currentInstanceNumber % INSTANCE_MOD_VALUE

            @Suppress("UnnecessaryVariable")
            val greenTuesdayList = GreenTuesdayList(when (currentInstanceNumberMod) {
                0 -> listOf(
                        Beans.createCopy(infoMessage = "Hurry up, these beans are great!"),
                        Cookies.createCopy(),
                        Milk.createCopy(),
                        Steak.createCopy(),
                        Yogurt.createCopy(),
                        Mojito.createCopy(infoMessage = "This mojito is a bomb!"),
                        Chicken.createCopy(),
                        Vodka.createCopy())
                1 -> listOf(
                        Milk.createCopy(),
                        Steak.createCopy(discountPrice = 1.23f, infoMessage = "This steak has never been cheaper!"),
                        Beans.createCopy(),
                        Cookies.createCopy(),
                        Chicken.createCopy(discountPrice = 7.48f),
                        Yogurt.createCopy(),
                        Mojito.createCopy(discountPrice = 6.01f),
                        Vodka.createCopy())
                else -> throw IllegalStateException("$currentInstanceNumberMod should be between 0 (included) and $INSTANCE_MOD_VALUE")
            })

            return greenTuesdayList
        }

        private fun GreenTuesdayListItem.createCopy(
                discountPrice: Float = this.discountPrice,
                infoMessage: String? = null
        ) = this.copy(
                discountPrice = discountPrice,
                infoMessage = infoMessage
        )

        private val Beans = GreenTuesdayListItem(
                "id_00",
                "Soylent Green Beans",
                8.20f,
                5.69f,
                "https://i.pinimg.com/736x/54/a4/fc/54a4fc5e045b6cf0f4c52a4c58b508ed--jack-nicholson-october-.jpg",
                null)

        private val Cookies = GreenTuesdayListItem(
                "id_01",
                "Greench home made cookies",
                8.20f,
                7.20f,
                "https://www.taschen.com/media/images/640/default_pr_schapiro_taxi_driver_travis_bickle_1011041545_id_394863.jpg",
                null)

        private val Milk = GreenTuesdayListItem(
                "id_02",
                "Soylent Milk - Missing human edition",
                16.84f,
                12.34f,
                "https://upload.wikimedia.org/wikipedia/tr/1/19/Scarfaceinthefall.jpg",
                null)

        private val Steak = GreenTuesdayListItem(
                "id_03",
                "Steak green soyless",
                17.39f,
                15.25f,
                "https://pbs.twimg.com/profile_images/610719306847002624/MmvsAf-U.jpg",
                null)

        private val Yogurt = GreenTuesdayListItem(
                "id_04",
                "Soyless yogurt",
                4.22f,
                2.70f,
                "https://images-na.ssl-images-amazon.com/images/M/MV5BMTU4OTY2MTE3OF5BMl5BanBnXkFtZTcwMzc0Mzg4Mw@@._V1_.jpg",
                null)

        private val Mojito = GreenTuesdayListItem(
                "id_05",
                "Classic mojito",
                6.53f,
                5.55f,
                "https://atwistedsenseoftumour.files.wordpress.com/2014/03/patrick-bateman.jpg",
                null)

        private val Chicken = GreenTuesdayListItem(
                "id_06",
                "Pure chicken",
                20.43f,
                13.23f,
                "https://upload.wikimedia.org/wikipedia/en/thumb/d/df/Michaelcoreleone.jpg/220px-Michaelcoreleone.jpg",
                null)

        private val Vodka = GreenTuesdayListItem(
                "id_07",
                "Soyless vodka",
                32.32f,
                22.34f,
                "https://static.giantbomb.com/uploads/original/1/14103/323221-1_1_.jpg",
                null)
    }
}
