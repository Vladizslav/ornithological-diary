package com.example.ornithologicaldiary.data

import com.example.ornithologicaldiary.data.model.Observation
import com.example.ornithologicaldiary.data.model.Species

object SampleData {
    val observations = listOf(
        Observation(
            species = "Большая синица",
            place = "Центральный парк",
            date = "12 мая, 08:40",
            note = "Пела на ветке у пруда"
        ),
        Observation(
            species = "Сизый голубь",
            place = "Двор на ул. Ленина",
            date = "12 мая, 09:15",
            note = "Стая из шести особей"
        ),
        Observation(
            species = "Обыкновенный скворец",
            place = "Ботанический сад",
            date = "11 мая, 18:20",
            note = "Сидел на скворечнике"
        )
    )

    val species = listOf(
        Species("Большая синица", "Parus major"),
        Species("Сизый голубь", "Columba livia"),
        Species("Обыкновенный скворец", "Sturnus vulgaris"),
        Species("Серая ворона", "Corvus cornix")
    )
}
