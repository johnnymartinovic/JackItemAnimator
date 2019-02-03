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
                        Paris.copy(),
                        NewYork.copy(),
                        Zagreb.copy(infoMessage = "Hurry up, Zagreb is great!"),
                        London.copy(),
                        Sidney.copy(),
                        Berlin.copy())
                1 -> listOf(
                        Zagreb.copy(infoMessage = "Now Zagreb is even cheaper!", price = 42),
                        Paris.copy(),
                        Havana.copy(),
                        NewYork.copy(infoMessage = "New York is pretty good!"),
                        Sidney.copy(price = 120),
                        Berlin.copy())
                else -> throw IllegalStateException("$instanceNumber should be between 0 (included) and 1 (included)")
            }

            return list
        }

        private fun createUnsplashImageUrl(photoId: String) = UNSPLASH_BASE_URL + photoId

        private val Paris = TravelinoItem(
                "id_00",
                "Paris's Best Kept Secrets",
                52,
                74,
                createUnsplashImageUrl("Q0-fOL2nqZc"),
                "")

        private val NewYork = TravelinoItem(
                "id_01",
                "Best New York Panoramas",
                60,
                95,
                createUnsplashImageUrl("UExx0KnnkjY"),
                "")

        private val Zagreb = TravelinoItem(
                "id_02",
                "Explore Zagreb with a Local",
                70,
                92,
                createUnsplashImageUrl("ZINC3joF-JQ"),
                "")

        private val London = TravelinoItem(
                "id_03",
                "Unseen London by Bicycle",
                30,
                35,
                createUnsplashImageUrl("tZDtyUrYrFU"),
                "")

        private val Sidney = TravelinoItem(
                "id_04",
                "Visit Sidney Opera House",
                102,
                134,
                createUnsplashImageUrl("DLbCETd599Y"),
                "")

        private val Berlin = TravelinoItem(
                "id_05",
                "World War II Tour in Berlin",
                48,
                64,
                createUnsplashImageUrl("fv0yV5-Pbjc"),
                "")

        private val Rome = TravelinoItem(
                "id_06",
                "Tour around Rome in Fiat 500",
                42,
                48,
                createUnsplashImageUrl("0Bs3et8FYyg"),
                "")

        private val Havana = TravelinoItem(
                "id_07",
                "Learn Salsa in a Havana!",
                99,
                113,
                createUnsplashImageUrl("RqMIFcDLeos"),
                "")
    }
}
