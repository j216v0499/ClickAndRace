package com.dearos.clickandrace.auth.domain.appRepository

import com.dearos.clickandrace.model.dto.LocationDTO
import com.dearos.clickandrace.model.data.LocationData
import com.dearos.clickandrace.model.data.LocationTypeResponse
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns

/**
 * Repositorio encargado de gestionar las operaciones CRUD relacionadas con las ubicaciones (Locations)
 * en la base de datos Supabase.
 *
 * Esta clase actúa como una capa de abstracción que encapsula la lógica de acceso a datos
 * para mantener limpio el código de las vistas y controladores.
 *
 * @param supabaseClient Cliente de Supabase configurado para acceder a la base de datos.
 */
class LocationRepository(private val supabaseClient: SupabaseClient) {

    /**
     * Inserta una nueva ubicación en la tabla "locations".
     *
     * @param location Objeto [LocationDTO] con los datos a insertar.
     */
    suspend fun addLocation(location: LocationDTO) {
        supabaseClient.from("locations").insert(location)
    }

    /**
     * Actualiza una ubicación existente en la tabla "locations" por ID.
     *
     * @param id ID único de la ubicación a actualizar.
     * @param updatedLocation Nuevos datos como [LocationDTO].
     */
    suspend fun updateLocation(id: String, updatedLocation: LocationDTO) {
        supabaseClient.from("locations")
            .update(updatedLocation) {
                filter {
                    eq("id", id)
                }
            }
    }

    /**
     * Elimina una ubicación de la tabla "locations" por ID.
     *
     * @param id ID único de la ubicación a eliminar.
     */
    suspend fun deleteLocation(id: String) {
        supabaseClient.from("locations")
            .delete {
                filter {
                    eq("id", id)
                }
            }
    }

    /**
     * Recupera todas las ubicaciones desde la tabla "locations".
     *
     * @return Lista de objetos [LocationData].
     */
    suspend fun getLocations(): List<LocationData> {
        return supabaseClient.postgrest["locations"]
            .select()
            .decodeList<LocationData>()
    }

    /**
     * Obtiene una lista de tipos de ubicación únicos a partir de los registros disponibles.
     *
     * @return Lista de tipos únicos como [String].
     */
    suspend fun getUniqueTypes(): List<String> {
        return try {
            val response = supabaseClient.from("locations")
                .select(columns = Columns.list("type"))
                .decodeList<LocationTypeResponse>()

            // Filtra valores nulos o vacíos, y los devuelve únicos
            response.mapNotNull { it.type?.trim()?.takeIf { t -> t.isNotEmpty() } }.distinct()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}
