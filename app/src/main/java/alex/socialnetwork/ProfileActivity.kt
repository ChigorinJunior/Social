package alex.socialnetwork

import alex.socialnetwork.Common.Profile
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.makeramen.roundedimageview.RoundedImageView
import com.squareup.picasso.Picasso
import android.content.Intent
import android.support.design.widget.BottomNavigationView
import android.view.MenuItem


class ProfileActivity : AppCompatActivity() {

    private lateinit var avatar : RoundedImageView
    private lateinit var link : EditText
    private lateinit var name : EditText
    private lateinit var email : EditText
    private lateinit var phone : EditText

    private val currentEmail = FirebaseAuth.getInstance().currentUser!!.email.toString()
    private val database : FirebaseDatabase = FirebaseDatabase.getInstance()
    private val myRef : DatabaseReference = database.reference.child("users").child(currentEmail.filter { i -> i != '.' })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        avatar = findViewById(R.id.avatar_profile)
        link = findViewById(R.id.link_profile)
        name = findViewById(R.id.name_profile)
        email = findViewById(R.id.email_profile)
        phone = findViewById(R.id.phone_profile)

        val bottomNavigationView = findViewById<View>(R.id.bottomNavView_Bar) as BottomNavigationView
        val menu = bottomNavigationView.menu
        val menuItem = menu.getItem(1)
        menuItem.isChecked = true

        bottomNavigationView.setOnNavigationItemSelectedListener(object : BottomNavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when (item.itemId) {
                    R.id.friends_menu -> {
                        startFriends()
                    }

                    R.id.map_menu -> {
                        startMap()
                    }
                }

                return false
            }
        })

        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val profile = dataSnapshot.getValue<Profile>(Profile::class.java)
                name.setText(profile!!.name)
                email.setText(profile.email)
                if (profile.phone != null) phone.setText(profile.phone)
                if (profile.link == null)
                    avatar.setImageResource(R.drawable.default_avatar)
                else
                    Picasso.get().load(profile.link).into(avatar)
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }

    fun save(view : View)
    {
        if (name.text.toString().isNotEmpty() && checkEmail() && checkPhone())
        {
            val profile = Profile()
            profile.name = name.text.toString()
            profile.email = email.text.toString()
            profile.phone = phone.text.toString()

            if (!link.text.toString().isEmpty())
            {
                profile.link = link.text.toString()
                Picasso.get().load(link.text.toString()).into(avatar)
            }

            database.reference.child("users/" + email.text.toString().filter { i -> i != '.' }).setValue(profile)
            Toast.makeText(this, "Данные сохранены", Toast.LENGTH_LONG).show()
        }
        else Toast.makeText(this, "Данные заполнены некорректно", Toast.LENGTH_LONG).show()
    }

    private fun checkEmail() = email.text.toString().any{ i -> i == '@'} && email.text.toString().any{ i -> i == '.'}

    private fun checkPhone() = phone.text.toString().all{ i -> i in '0'..'9' } && !phone.text.toString().isEmpty()

    private fun startFriends() {
        startActivity(Intent(this, FriendsActivity::class.java))
    }

    private fun startMap(){
        startActivity(Intent(this, MapsActivity::class.java))
    }
}
