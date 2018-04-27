package alex.socialnetwork

import alex.socialnetwork.Common.Message
import alex.socialnetwork.Repositories.MessagesRepository
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.stfalcon.chatkit.messages.MessageInput
import com.stfalcon.chatkit.messages.MessagesList
import com.stfalcon.chatkit.messages.MessagesListAdapter

class MessengerActivity : AppCompatActivity() {

    private lateinit var adapter: MessagesListAdapter<Message>
    private lateinit var repository: MessagesRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messenger)

        val messagesList = findViewById<MessagesList>(R.id.messagesList)
        val messageInput = findViewById<MessageInput>(R.id.input)

        val user = FirebaseAuth.getInstance().currentUser
        adapter = MessagesListAdapter(user!!.uid, null)
        messagesList.setAdapter(adapter)

        repository = MessagesRepository()

        repository.loadMessages(object : MessagesRepository.MessageLoadListener {
            override fun onMessagesReceived(messages: Message) {
                adapter.addToStart(messages, true)
            }

            override fun onError(error: Throwable) {

            }
        })

        messageInput.setInputListener(MessageInput.InputListener { input ->
            val currentUser = FirebaseAuth.getInstance().currentUser
            if (currentUser != null && currentUser.email != null)
            {
                repository.addMessage(input.toString(), currentUser.email!!, currentUser.uid)
            }
            true
        })
    }
}

