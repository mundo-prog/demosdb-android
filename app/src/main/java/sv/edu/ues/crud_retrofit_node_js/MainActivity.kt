package sv.edu.ues.crud_retrofit_node_js

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import sv.edu.ues.crud_retrofit_node_js.databinding.ActivityMainBinding
import timber.log.Timber

class MainActivity : AppCompatActivity(),UsuarioAdapter.OnItemClicked {

    lateinit var binding: ActivityMainBinding

    //Adaptador del recyclerView
    lateinit var adatador: UsuarioAdapter

    var listaUsuarios:ArrayList<Usuario>  = arrayListOf<Usuario>()


    var usuario = Usuario(-1, "","")

    var isEditando = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //yo      binding = ActivityMainBinding.inflate(layoutInflater)
        //yo setContentView(binding.root)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater) //agrego
        setContentView(binding.root) //agrego
        binding.rvUsuarios.layoutManager = LinearLayoutManager(this)
        setupRecyclerView()
      obtenerUsuarios()

        binding.btnAddUpdate.setOnClickListener {
            var isValido = validarCampos()
            if (isValido) {
                if (!isEditando) {
                    //agregarUsuario()
                } else {
                    //actualizarUsuario()
                }
            } else {
                Toast.makeText(this, "Se deben llenar los campos", Toast.LENGTH_LONG).show()
            }
        }

    }

    fun setupRecyclerView() {
        adatador = UsuarioAdapter(this, listaUsuarios)
        adatador.setOnClick(this@MainActivity)
        binding.rvUsuarios.adapter = adatador

    }

    fun validarCampos(): Boolean {
        return !(binding.etNombre.text.isNullOrEmpty() || binding.etEmail.text.isNullOrEmpty())
    }

    override fun onResume() {
        super.onResume()
        var usuarios: ListaDeUsuariosResponse ? = null
        lifecycleScope.launch(Dispatchers.IO) {
            Timber.d("Aqui esta el timber")
            val usuariosResult = runCatching { async { App.request.getUsers() }.await() }
            usuariosResult
                .onSuccess { listaUsuarios ->
                    Log.d("CustomMessage",listaUsuarios.prettify())
                }
                .onFailure { error ->
                    Timber.e(error)
                }
        }
    }
    fun obtenerUsuarios() {
        CoroutineScope(Dispatchers.IO).launch {
            val call = RetrofitClient.webService.obtenerUsuarios()
            runOnUiThread {
                if (call.isSuccessful) {
                    listaUsuarios = call.body()!!.listaUsuarios
                    if (!listaUsuarios.isNullOrEmpty())
                        {setupRecyclerView()}

                } else {
                    Toast.makeText(this@MainActivity, "ERROR CONSULTAR TODOS", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

//    fun agregarUsuario() {
//
//        this.usuario.id = -1
//        this.usuario.nombre = binding.etNombre.text.toString()
//        this.usuario.email = binding.etEmail.text.toString()
//
//        CoroutineScope(Dispatchers.IO).launch {
//            val call = RetrofitClient.webService.agregarUsuario(usuario)
//            runOnUiThread {
//                if (call.isSuccessful) {
//                    Toast.makeText(this@MainActivity, call.body().toString(), Toast.LENGTH_LONG).show()
//                    obtenerUsuarios()
//                    limpiarCampos()
//                    limpiarObjeto()
//
//                } else {
//                    Toast.makeText(this@MainActivity, "ERROR ADD", Toast.LENGTH_LONG).show()
//                }
//            }
//        }
//    }

//    fun actualizarUsuario() {
//
//        this.usuario.nombre = binding.etNombre.text.toString()
//        this.usuario.email = binding.etEmail.text.toString()
//
//        CoroutineScope(Dispatchers.IO).launch {
//            val call = RetrofitClient.webService.actualizarUsuario(usuario.id, usuario)
//            runOnUiThread {
//                if (call.isSuccessful) {
//                    Toast.makeText(this@MainActivity, call.body().toString(), Toast.LENGTH_LONG).show()
//                    obtenerUsuarios()
//                    limpiarCampos()
//                    limpiarObjeto()
//
//                    binding.btnAddUpdate.setText("Agregar Usuario")
//                    binding.btnAddUpdate.backgroundTintList = resources.getColorStateList(R.color.green)
//                    isEditando = false
//                }
//            }
//        }
//    }

    fun limpiarCampos() {
        binding.etNombre.setText("")
        binding.etEmail.setText("")
    }

//    fun limpiarObjeto() {
//        this.usuario.id = -1
//        this.usuario.nombre = ""
//        this.usuario.email = ""
//    }

    override fun editarUsuario(usuario: Usuario) {
        binding.etNombre.setText(usuario.nombre)
        binding.etEmail.setText(usuario.email)
        binding.btnAddUpdate.setText("Actualizar Usuario")
        binding.btnAddUpdate.backgroundTintList = resources.getColorStateList(R.color.purple_500)
        this.usuario = usuario
        isEditando = true
    }

    override fun borrarUsuario(idUsuario: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val call = RetrofitClient.webService.borrarUsuario(idUsuario)
            runOnUiThread {
                if (call.isSuccessful) {
                    Toast.makeText(this@MainActivity, call.body().toString(), Toast.LENGTH_LONG).show()
                    obtenerUsuarios()
                }
            }
        }
    }
}