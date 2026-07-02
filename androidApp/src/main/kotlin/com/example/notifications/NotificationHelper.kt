package com.example.notifications
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.signtranslate.R

class NotificationHelper(private val context: Context) {

    companion object {
        private const val CHANNEL_ID = "signtranslate_channel"

        // Cada tipo de notificación necesita un ID distinto,
        // si no, una sobrescribe a la otra en la barra de estado.
        private const val ID_SESSION_EXPIRED = 1001
        private const val ID_TRANSLATION_READY = 1002
        private const val ID_CONNECTION_ERROR = 1003
        private const val ID_REGISTER_SUCCESS = 1004
    }

    init {
        createNotificationChannel()
    }

    // Los canales de notificación son obligatorios desde Android 8 (API 26).
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "SignTranslate",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notificaciones de sesión, traducción y conexión"
            }
            val manager = context.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    private fun showNotification(id: Int, title: String, message: String) {
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        // Verificación obligatoria del permiso antes de mostrar la notificación
        if (androidx.core.content.ContextCompat.checkSelfPermission(
                context, android.Manifest.permission.POST_NOTIFICATIONS
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
            NotificationManagerCompat.from(context).notify(id, notification)
        }
    }

    // 1) Se dispara cuando el AUTH_TOKEN guardado en DataStore ya no existe o venció.
    fun notifySessionExpired() {
        showNotification(
            ID_SESSION_EXPIRED,
            "Sesión expirada",
            "Tu sesión terminó. Vuelve a iniciar sesión para seguir traduciendo."
        )
    }

    // 2) Se dispara cuando el clasificador termina de procesar una secuencia de señas.
    fun notifyTranslationReady() {
        showNotification(
            ID_TRANSLATION_READY,
            "Traducción lista",
            "Tu traducción se completó correctamente."
        )
    }

    // 3) Se dispara cuando falla la comunicación con el backend.
    fun notifyConnectionError() {
        showNotification(
            ID_CONNECTION_ERROR,
            "Error de conexión",
            "No se pudo conectar al servidor. Verifica tu conexión a internet."
        )
    }

    // 4) Se dispara cuando el backend confirma que el registro fue exitoso.
    fun notifyRegisterSuccess() {
        showNotification(
            ID_REGISTER_SUCCESS,
            "Registro exitoso",
            "Tu cuenta fue creada correctamente. ¡Ya puedes iniciar sesión!"
        )
    }
}