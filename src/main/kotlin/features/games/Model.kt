package mx.com.lennux.features.games

import kotliquery.Row

data class Game(
    var id: String?,
    var img: String,
    var title: String,
    var subtitle: String,
    var advanced: Boolean,
    var duration: Int,
)

fun rowToGame(row: Row) = Game(
    id = row.string("ID"),
    img = row.string("IMG"),
    title = row.string("TITLE"),
    subtitle = row.string("SUBTITLE"),
    advanced = row.boolean("ADVANCED"),
    duration = row.int("DURATION")
)