package alex.socialnetwork

import alex.socialnetwork.Repositories.FriendsRepository
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class Registration : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        auth = FirebaseAuth.getInstance()
    }

    fun registration(view : View)
    {
        val mail = findViewById<EditText>(R.id.email_registration)
        val pass = findViewById<EditText>(R.id.password_registration)
        val confirm = findViewById<EditText>(R.id.password_confirm)
        val name = findViewById<EditText>(R.id.name_registration)


        when {
            !(mail.text.toString().any{ i -> i == '@'} && mail.text.toString().any{ i -> i == '.'}) -> Toast.makeText(this, "Некорректная почта", Toast.LENGTH_LONG).show()
            pass.text.toString().length < 6 -> Toast.makeText(this, "Пароль должен содержать более пяти знаков", Toast.LENGTH_LONG).show()
            pass.text.toString() != confirm.text.toString() -> Toast.makeText(this, "Пароли не совпадают", Toast.LENGTH_LONG).show()
            name.text.toString().length < 3 -> Toast.makeText(this, "Имя должно быть длиннее 3 знаков", Toast.LENGTH_LONG).show()
            else -> signUp(mail.text.toString(), pass.text.toString(), confirm.text.toString())
        }
    }

    private fun signUp(email : String, password : String, confirm : String)
    {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (!task.isSuccessful) {
                        Toast.makeText(this, "Что-то пошло не так", Toast.LENGTH_LONG).show()
                    } else {
                        addUser(email)
                        Toast.makeText(this, "Регистрация прошла успешно!", Toast.LENGTH_LONG).show()
                        startActivity(Intent(this, Authorization::class.java))
                        finish()
                    }
                }
    }

    private fun addUser(email : String){
        val name = findViewById<EditText>(R.id.name_registration)
        val repository = FriendsRepository()
        repository.add(name.text.toString(), email)
    }

    fun back(view : View)
    {
        startActivity(Intent(this, Authorization::class.java))
        finish()
    }
}
