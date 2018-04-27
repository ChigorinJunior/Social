package alex.socialnetwork.Repositories

import alex.socialnetwork.Common.Friend
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import java.util.*

class FriendsRepository {
    private val database = FirebaseDatabase.getInstance()
    private val myRef = database.getReference("users")

    fun loadFriends(friendsLoadListener: FriendsLoadListener) {
        val query: Query = myRef.orderByChild("name")

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                friendsLoadListener.onFriendsLoaded(toFriendList(dataSnapshot))
            }

            override fun onCancelled(databaseError: DatabaseError) {
                friendsLoadListener.onError(databaseError.toException())
            }
        })
    }

    interface FriendsLoadListener {
        fun onFriendsLoaded(friends: List<Friend>)
        fun onError(error: Throwable)
    }

    private fun toFriendList(dataSnapshot: DataSnapshot): List<Friend> {
        val friends = ArrayList<Friend>()

        for (snapshot in dataSnapshot.children) {
            if (snapshot.getValue<Friend>(Friend::class.java) != null)
                friends.add(snapshot.getValue<Friend>(Friend::class.java)!!)
        }

        return friends
    }

    fun add(name: String, email: String) {
        val friend = Friend()
        friend.name = name
        friend.email = email

        myRef.child(email.filter { i -> i != '.' }).setValue(friend)
    }
}