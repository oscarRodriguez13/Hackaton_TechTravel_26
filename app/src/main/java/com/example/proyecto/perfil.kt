package com.example.proyecto

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.proyecto.databinding.ActivityPerfilBinding
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.util.logging.Logger


class perfil : androidx.appcompat.app.AppCompatActivity() {
    private lateinit var binding: ActivityPerfilBinding

    private val logger = Logger.getLogger(
        MainActivity::class.java.name
    )

    private val CAMERA_PERMISSION_ID = 101
    private val GALLERY_PERMISSION_ID = 102
    var cameraPerm = Manifest.permission.CAMERA

    var imageView: ImageView? = null
    var currentPhotoPath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPerfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        logger.info("Se va a solicitar el permiso")
        requestPermission(this, cameraPerm, "Permiso para utiliza la camara", CAMERA_PERMISSION_ID)
        initView()

        binding.username.text = username
        binding.email.text = email

        // Set up click listener for logout button
        binding.logoutButton.setOnClickListener {
            // TODO: Add your own logic for logging out the user
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }
    }

    // TODO: Add your own logic for retrieving user data and displaying it in the UI
    val username = "John Doe"
    val email = "johndoe@example.com"

    fun requestPermission(
        context: Activity?,
        permission: String,
        justification: String?,
        id: Int
    ) {
        // Se verifica si no hay permisos
        if (ContextCompat.checkSelfPermission(
                context!!,
                permission
            ) == PackageManager.PERMISSION_DENIED
        ) {
            // ¿Deberiamos mostrar una explicación?
            if (ActivityCompat.shouldShowRequestPermissionRationale(context!!, cameraPerm)) {
                Toast.makeText(context, justification, Toast.LENGTH_SHORT).show()
            }
            // Solicitar el permiso
            ActivityCompat.requestPermissions(context!!, arrayOf(permission), id)
        }
    }

    fun initView() {
        if (ContextCompat.checkSelfPermission(
                this,
                cameraPerm
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            logger.warning("Failed to getting the permission")
        } else {
            logger.info("Success getting the permission")
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_ID) {
            initView()
        }
    }

    fun startCamera(view: View?) {
        if (ContextCompat.checkSelfPermission(
                this,
                cameraPerm
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            openCamera()
        } else {
            logger.warning("Ocurrio un fallo en el permiso solicitado")
        }
    }

    fun openCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Asegurarse de que hay una actividad de camara para manejar el intent
        if (takePictureIntent != null) {
            //Crear el archivo donde debería ir la foto
            var photoFile: File? = null
            try {
                photoFile = createImageFile()
            } catch (ex: IOException) {
                logger.warning(ex.message)
            }
            //Continua solo el archivo ha sido exitosamente creado
            if (photoFile != null) {
                val photoURI =
                    FileProvider.getUriForFile(this, "com.example.android.fileprovider", photoFile)
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(takePictureIntent, CAMERA_PERMISSION_ID)
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File? {
        //Crear un nombre dde archivo de imagen
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile("IMG", ".jpg", storageDir)

        // Guardar un archivo: Ruta para usar con ACTION_VIEW intents
        currentPhotoPath = image.absolutePath
        logger.info("Ruta: $currentPhotoPath")
        return image
    }

    fun startGallery(view: View?) {
        val pickGalleryImage = Intent(Intent.ACTION_PICK)
        pickGalleryImage.type = "image/*"
        startActivityForResult(pickGalleryImage, GALLERY_PERMISSION_ID)
    }

    override fun onActivityResult(requestCode: Int, rresultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, rresultCode, data)
        if (rresultCode == RESULT_OK) {
            when (requestCode) {
                CAMERA_PERMISSION_ID -> {
                    imageView!!.setImageURI(Uri.parse(currentPhotoPath))
                    logger.info("Imagen capturada correctamente.")
                }
                GALLERY_PERMISSION_ID -> {
                    val imageUri = data!!.data
                    imageView!!.setImageURI(imageUri)
                    logger.info("Imagen cargadada correctamente")
                }
            }
        }
    }

}
