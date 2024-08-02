package sv.edu.ues.crud_retrofit_node_js

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Usuario(
    val idUsuario: Int,
    val nombre: String,
    val email: String
)