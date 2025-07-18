package mx.com.lennux.features.mice

import kotliquery.Row

data class Mouse (
    var id: String?,
    var name: String,
    val color: String,
    val dpi: String,
    val buttons: Int,
    val weight: String,
    val wireless: Boolean,
    val price: Double,
    val description: String,
    val src: String,
    )

fun rowToMouse(row: Row) = Mouse(
    id = row.string("ID"),
    name = row.string("NAME"),
    color = row.string("COLOR"),
    dpi = row.string("DPI"),
    buttons = row.int("BUTTONS"),
    weight = row.string("WEIGHT"),
    wireless = row.boolean("WIRELESS"),
    price = row.double("PRICE"),
    description = row.string("DESCRIPTION"),
    src = row.string("SRC")
)