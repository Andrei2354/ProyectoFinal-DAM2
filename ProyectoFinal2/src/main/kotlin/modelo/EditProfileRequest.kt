package modelo

data class EditProfileRequest(
    val id: Int,
    val nombre: String,
    val email: String,
    val direccion: String,
    val telefono: String
)
