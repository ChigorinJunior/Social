package alex.socialnetwork.Adapter

import alex.socialnetwork.Common.Friend
import alex.socialnetwork.R
import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.friend_item.view.*

class FriendViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)
{
    var avatar = itemView.avatar
    var name = itemView.name
    var message = itemView.message
}

class FriendAdapter(private var activity: Activity, var items: List<Friend>) : RecyclerView.Adapter<FriendViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        val view = LayoutInflater.from(activity).inflate(R.layout.friend_item, parent, false)

        return FriendViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        val friendModel = items[position]

        holder.message.text = "Chat"
        holder.name.text = friendModel.name

        if (friendModel.avatar != null) Picasso.get().load(friendModel.avatar).into(holder.avatar)
        else holder.avatar.setImageResource(R.drawable.default_avatar)
    }

    fun updateData(coins : List<Friend>)
    {
        this.items = coins
        notifyDataSetChanged()
    }
}
