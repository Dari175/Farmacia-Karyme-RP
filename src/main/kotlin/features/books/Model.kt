package mx.com.lennux.features.books

import kotliquery.Row

data class Book(
    var id: String? = null,
    var title: String,
    var author: String,
    var genre: String,
    var year: String,
    var pages: Int,
)

fun rowToBook(row: Row) = Book(
    id = row.string("ID"),
    title = row.string("TITLE"),
    author = row.string("AUTHOR"),
    genre = row.string("GENRE"),
    year = row.string("BYEAR"),
    pages = row.int("BPAGES")
)