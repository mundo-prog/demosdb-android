package sv.edu.ues.crud_retrofit_node_js

import kotlinx.serialization.Serializable

@Serializable
data class ListaDeUsuariosResponse(
    val listaUsuarios:List<Usuario>
)
