package sv.edu.ues.crud_retrofit_node_js

import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

inline fun <reified T> T.prettify():String where T:Any{
    val json= Json { prettyPrint = true }
    return try {
        json.encodeToString(serializer(),this)
    }
    catch(e:SerializationException) {
        "ERROR SERIALIZING OBJECT: ${e.message}"
    }
}
