package features.desserts

import kotliquery.Row

data class Dessert(
    var id: String? = null,
    var name: String = "",
    val calories: Int = 0,
    val fat: Double = 0.0,
    val carbs: Int = 0,
    val protein: Double = 0.0
)

fun rowToDessert(row: Row) = Dessert(
    id = row.string("ID"),
    name = row.string("NAME"),
    calories = row.int("CALORIES"),
    fat = row.double("FAT"),
    carbs = row.int("CARBS"),
    protein = row.double("PROTEIN")
)