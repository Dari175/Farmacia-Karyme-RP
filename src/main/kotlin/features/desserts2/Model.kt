package features.desserts2

import kotliquery.Row

data class Dessert(
    var id: String? = null,
    var name: String = "",
    var description: String = "",
    var icon: String = "",
    var color: String = "",
    val calories: Int = 0,
    val fat: Double = 0.0,
    val carbs: Int = 0,
    val protein: Double = 0.0,
    val sodium: Int = 0,
    val calcium: String = "0%",
    val iron: String = "0%"
)


fun rowToDessert(row: Row) = Dessert(
    id = row.string("ID"),
    name = row.string("DNAME"),
    description = row.string("DESCRIPTION"),
    icon = row.string("ICON"),
    color = row.string("COLOR"),
    calories = row.int("CALORIES"),
    fat = row.double("FAT"),
    carbs = row.int("CARBS"),
    protein = row.double("PROTEIN"),
    sodium = row.int("SODIUM"),
    calcium = row.string("CALCIUM"),
    iron = row.string("IRON")
)
