package com.example.patitassanas

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/** Modelo de una cita. id = 0 significa "aún no guardada". */
data class Cita(
    val id: Int = 0,
    val nombre: String = "",
    val telefono: String = "",
    val fecha: String = "",
    val hora: String = "",
    val raza: String = "",
    val tamano: String = ""
)

/** SQLite: crea veterinaria.db y la tabla citas la primera vez que se usa. */
class CitaDatabase(context: Context) :
    SQLiteOpenHelper(context, "veterinaria.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE citas (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                telefono TEXT NOT NULL,
                fecha TEXT NOT NULL,
                hora TEXT NOT NULL,
                raza TEXT NOT NULL,
                tamano TEXT NOT NULL
            )
            """.trimIndent()
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Migraciones futuras aquí. No se elimina la tabla para no perder citas.
    }

    private fun valores(c: Cita) = ContentValues().apply {
        put("nombre", c.nombre)
        put("telefono", c.telefono)
        put("fecha", c.fecha)
        put("hora", c.hora)
        put("raza", c.raza)
        put("tamano", c.tamano)
    }

    fun insertar(cita: Cita): Long = writableDatabase.insert("citas", null, valores(cita))

    fun actualizar(cita: Cita): Int =
        writableDatabase.update("citas", valores(cita), "id = ?", arrayOf(cita.id.toString()))

    fun eliminar(id: Int): Int =
        writableDatabase.delete("citas", "id = ?", arrayOf(id.toString()))

    fun consultarTodas(): List<Cita> {
        val lista = mutableListOf<Cita>()
        readableDatabase.rawQuery(
            "SELECT id, nombre, telefono, fecha, hora, raza, tamano FROM citas ORDER BY id DESC",
            null
        ).use { cursor ->
            while (cursor.moveToNext()) lista.add(leer(cursor))
        }
        return lista
    }

    fun consultarPorId(id: Int): Cita? {
        readableDatabase.rawQuery(
            "SELECT id, nombre, telefono, fecha, hora, raza, tamano FROM citas WHERE id = ?",
            arrayOf(id.toString())
        ).use { cursor ->
            if (cursor.moveToFirst()) return leer(cursor)
        }
        return null
    }

    private fun leer(c: android.database.Cursor) = Cita(
        id = c.getInt(c.getColumnIndexOrThrow("id")),
        nombre = c.getString(c.getColumnIndexOrThrow("nombre")),
        telefono = c.getString(c.getColumnIndexOrThrow("telefono")),
        fecha = c.getString(c.getColumnIndexOrThrow("fecha")),
        hora = c.getString(c.getColumnIndexOrThrow("hora")),
        raza = c.getString(c.getColumnIndexOrThrow("raza")),
        tamano = c.getString(c.getColumnIndexOrThrow("tamano"))
    )
}