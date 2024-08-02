package sv.edu.ues.crud_retrofit_node_js

class Request(private val webService: WebService) {
    suspend fun getUsers():ListaDeUsuariosResponse{ return webService.getUsers()}
}