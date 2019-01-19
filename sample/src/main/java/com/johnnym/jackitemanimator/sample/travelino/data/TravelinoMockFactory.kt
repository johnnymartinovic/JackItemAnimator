package com.johnnym.jackitemanimator.sample.travelino.data

import com.johnnym.jackitemanimator.sample.travelino.domain.TravelinoList
import com.johnnym.jackitemanimator.sample.travelino.domain.TravelinoListItem
import java.lang.IllegalStateException

class TravelinoMockFactory {

    companion object {

        private const val INSTANCE_MOD_VALUE = 2

        private const val UNSPLASH_BASE_URL = "https://source.unsplash.com/"

        fun createTravelinoList(currentInstanceNumber: Int): TravelinoList {
            val currentInstanceNumberMod = currentInstanceNumber % INSTANCE_MOD_VALUE

            @Suppress("UnnecessaryVariable")
            val travelinoList = TravelinoList(when (currentInstanceNumberMod) {
                0 -> listOf(
                        Zagreb.createCopy(infoMessage = "Hurry up, Zagreb is great!"),
                        Paris.createCopy(),
                        NewYork.createCopy(),
                        London.createCopy(),
                        Sidney.createCopy(),
                        Berlin.createCopy(),
                        Rome.createCopy(),
                        Cuba.createCopy(),
                        Hawaii.createCopy(),
                        Maldives.createCopy())
                1 -> listOf(
                        Paris.createCopy(price = 37),
                        Hawaii.createCopy(),
                        Zagreb.createCopy(price = 83),
                        London.createCopy(),
                        Sidney.createCopy(),
                        Berlin.createCopy(),
                        Rome.createCopy(),
                        Cuba.createCopy(),
                        Maldives.createCopy())
                else -> throw IllegalStateException("$currentInstanceNumberMod should be between 0 (included) and $INSTANCE_MOD_VALUE")
            })

            return travelinoList
        }

        private fun createImageUrl(photoId: String) = UNSPLASH_BASE_URL + photoId

        private fun TravelinoListItem.createCopy(
                price: Int = this.price,
                infoMessage: String? = null
        ) = this.copy(
                price = price,
                infoMessage = infoMessage
        )

        private val Zagreb = TravelinoListItem(
                "id_00",
                "Zagreb",
                70,
                92,
                createImageUrl("ZINC3joF-JQ"),
                null)

        private val Paris = TravelinoListItem(
                "id_01",
                "Paris",
                52,
                74,
                createImageUrl("Q0-fOL2nqZc"),
                null)

        private val NewYork = TravelinoListItem(
                "id_02",
                "New York",
                60,
                95,
                createImageUrl("UExx0KnnkjY"),
                null)

        private val London = TravelinoListItem(
                "id_03",
                "London",
                30,
                35,
                createImageUrl("tZDtyUrYrFU"),
                null)

        private val Sidney = TravelinoListItem(
                "id_04",
                "Sidney",
                102,
                134,
                createImageUrl("DLbCETd599Y"),
                null)

        private val Berlin = TravelinoListItem(
                "id_05",
                "Berlin",
                48,
                64,
                createImageUrl("fv0yV5-Pbjc"),
                null)

        private val Rome = TravelinoListItem(
                "id_06",
                "Rome",
                42,
                48,
                createImageUrl("0Bs3et8FYyg"),
                null)

        private val Cuba = TravelinoListItem(
                "id_07",
                "Cuba",
                99,
                113,
                createImageUrl("RqMIFcDLeos"),
                null)

        private val Hawaii = TravelinoListItem(
                "id_08",
                "Hawaii",
                120,
                150,
                createImageUrl("prSogOoFmkw"),
                null)

        private val Maldives = TravelinoListItem(
                "id_09",
                "Maldives",
                114,
                145,
                createImageUrl("qtbV_8P_Ksk"),
                null)
    }
}
