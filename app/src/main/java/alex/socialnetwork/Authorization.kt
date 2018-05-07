package alex.socialnetwork

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class Authorization : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authorization)

        auth = FirebaseAuth.getInstance()
    }

    fun goToRegistration(view : View)
    {
        startActivity(Intent(this, Registration::class.java))
        finish()
    }

    // TODO: параметр view не нужен
    fun enter(view : View) {
        val mail = findViewById<EditText>(R.id.email)
        val pass = findViewById<EditText>(R.id.password)

        // TODO: для констант нужны осмысленные имена типа EMAIL_MIN_LENGTH
        if (mail.text.length > 2 && pass.text.length > 5) {
            login(mail.text.toString(), pass.text.toString())
        } else {
            // TODO: строку лучше в ресурсы
            Toast.makeText(this, "Проверьте правильность набора данных и попробуйте снова", Toast.LENGTH_LONG).show()
        }
    }

    private fun login(email : String, password : String)
    {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (!task.isSuccessful) {
                        Toast.makeText(this, "Что-то пошло не так", Toast.LENGTH_LONG).show()
                    } else {
                        startActivity(Intent(this, ProfileActivity::class.java))
                        finish()
                    }
                }
    }
}
