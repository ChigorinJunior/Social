package alex.socialnetwork

import alex.socialnetwork.Adapter.FriendAdapter
import alex.socialnetwork.Common.Friend
import alex.socialnetwork.Repositories.FriendsRepository
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.GestureDetector
import android.view.MotionEvent
import kotlinx.android.synthetic.main.activity_friends.*

lateinit var friendsEmail : String

class FriendsActivity : AppCompatActivity() {

    internal var items : MutableList<Friend> = ArrayList()
    internal lateinit var adapter : FriendAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friends)

        friends_list.layoutManager = LinearLayoutManager(this)

        val friendsRepository = FriendsRepository()

        friendsRepository.loadFriends(object : FriendsRepository.FriendsLoadListener {
            override fun onFriendsLoaded(friends: List<Friend>) {
                for (f in friends)
                { // TODO: скобки по договоренности пишутся на той же строке
                    items.add(f)
                }
                adapter = FriendAdapter(this@FriendsActivity, items)
                friends_list.adapter = adapter
                adapter.updateData(items)
            }

            override fun onError(error: Throwable) {
                // TODO: ошибку обработать
            }
        })

        // TODO: есть плагин, позволяющий в случае Котлина не писать findViewById
        val recyclerView = findViewById<RecyclerView>(R.id.friends_list)
        recyclerView.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {

            internal var gestureDetector = GestureDetector(this@FriendsActivity, object : GestureDetector.SimpleOnGestureListener() {

                override fun onSingleTapUp(motionEvent: MotionEvent): Boolean {

                    return true
                }

            })

            // TODO: проще навесить ClickListener внутри вью в адаптере
            override fun onInterceptTouchEvent(Recyclerview: RecyclerView, motionEvent: MotionEvent): Boolean {

                val childView = Recyclerview.findChildViewUnder(motionEvent.x, motionEvent.y)

                if (childView != null && gestureDetector.onTouchEvent(motionEvent)) {

                    val recyclerViewItemPosition = Recyclerview.getChildAdapterPosition(childView)
                    friendsEmail = items[recyclerViewItemPosition].email!!.filter { i -> i != '.' }
                    startChat()
                }

                return false
            }

            override fun onTouchEvent(Recyclerview: RecyclerView, motionEvent: MotionEvent) {

            }

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {

            }
        })
    }

    private fun startChat()
    {
        startActivity(Intent(this, MessengerActivity::class.java))
    }
}
