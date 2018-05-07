package alex.socialnetwork.Repositories

import alex.socialnetwork.Common.Author
import alex.socialnetwork.Common.Message
import alex.socialnetwork.friendsEmail
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*

class MessagesRepository {
    private val database = FirebaseDatabase.getInstance()
    private val path = "messages/" + definePath()
    private val myRef = database.getReference(path)

    fun loadMessages(messageLoadListener: MessageLoadListener) {
        val query : Query = myRef.orderByChild("createdAt/time")
        query.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot?, s: String?) {
                if (dataSnapshot != null)
                    messageLoadListener.onMessagesReceived(toMessageList(dataSnapshot)[0])
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot?, s: String?) {

            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot?) {

            }

            override fun onChildMoved(dataSnapshot: DataSnapshot?, s: String?) {

            }

            override fun onCancelled(databaseError: DatabaseError?) {

            }
        })
    }

    interface MessageLoadListener {
        fun onMessagesReceived(messages: Message)
        fun onError(error: Throwable)
    }

    private fun definePath() : String
    {
        // TODO: что-то слишком много принудительных кастов через !!
        // TODO: лучше добавить проверок, чтобы не упало
        val currentUser = FirebaseAuth.getInstance().currentUser!!.email!!.filter { i -> i != '.' }
        // TODO: а зачем ещё раз фильтровать?
        currentUser.filter { i -> i != '.' }

        return if (currentUser > friendsEmail) currentUser + friendsEmail else friendsEmail + currentUser
    }

    private fun toMessageList(dataSnapshot: DataSnapshot): List<Message> {
        val messages = ArrayList<Message>()

        val message = dataSnapshot.getValue(Message::class.java)
        if (message != null)
            messages.add(message)

        return messages
    }

    fun addMessage(text: String, email: String, uid: String) {
        val id = UUID.randomUUID().toString()

        myRef.child(id).setValue(Message(id, text, Calendar.getInstance().time, Author(uid, email, "")))
    }
}