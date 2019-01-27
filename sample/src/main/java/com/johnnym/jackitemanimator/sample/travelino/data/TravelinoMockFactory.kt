package com.johnnym.jackitemanimator.sample.travelino.data

import com.johnnym.jackitemanimator.sample.travelino.domain.TravelinoItem
import java.lang.IllegalStateException

class TravelinoMockFactory {

    companion object {

        private const val UNSPLASH_BASE_URL = "https://source.unsplash.com/"

        fun createTravelinoItemList(instanceNumber: Int): List<TravelinoItem> {
            @Suppress("UnnecessaryVariable")
            val list = when (instanceNumber) {
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
                else -> throw IllegalStateException("$instanceNumber should be between 0 (included) and 1 (included)")
            }

            return list
        }

        fun createImageUrl(photoId: String) = UNSPLASH_BASE_URL + photoId

        private fun TravelinoItem.createCopy(
                price: Int = this.price,
                infoMessage: String = ""
        ) = this.copy(
                price = price,
                infoMessage = infoMessage
        )

        val Zagreb = TravelinoItem(
                "id_00",
                "Zagreb",
                70,
                92,
                createImageUrl("ZINC3joF-JQ"),
                "")

        val Paris = TravelinoItem(
                "id_01",
                "Paris",
                52,
                74,
                createImageUrl("Q0-fOL2nqZc"),
                "")

        val NewYork = TravelinoItem(
                "id_02",
                "New York",
                60,
                95,
                createImageUrl("UExx0KnnkjY"),
                "")

        val London = TravelinoItem(
                "id_03",
                "London",
                30,
                35,
                createImageUrl("tZDtyUrYrFU"),
                "")

        val Sidney = TravelinoItem(
                "id_04",
                "Sidney",
                102,
                134,
                createImageUrl("DLbCETd599Y"),
                "")

        val Berlin = TravelinoItem(
                "id_05",
                "Berlin",
                48,
                64,
                createImageUrl("fv0yV5-Pbjc"),
                "")

        val Rome = TravelinoItem(
                "id_06",
                "Rome",
                42,
                48,
                createImageUrl("0Bs3et8FYyg"),
                "")

        val Cuba = TravelinoItem(
                "id_07",
                "Cuba",
                99,
                113,
                createImageUrl("RqMIFcDLeos"),
                "")

        val Hawaii = TravelinoItem(
                "id_08",
                "Hawaii",
                120,
                150,
                createImageUrl("prSogOoFmkw"),
                "")

        val Maldives = TravelinoItem(
                "id_09",
                "Maldives",
                114,
                145,
                createImageUrl("qtbV_8P_Ksk"),
                "")
    }
}
