package alex.socialnetwork.Common;

import com.google.firebase.database.PropertyName;
import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.commons.models.IUser;

import java.util.Date;

public class Message implements IMessage {

    private String id;

    private String text;

    private Date createdAt;

    @PropertyName("user")
    private Author user;

    public Message() { }

    public Message(String id, String text, Date createdAt, Author author){
        this.id = id;
        this.text = text;
        this.createdAt = createdAt;
        this.user = author;
    }

    @Override
    public String getId(){
        return id;
    }

    @Override
    public String getText(){
        return text;
    }

    public Date getCreatedAt(){
        return createdAt;
    }

    @Override
    public IUser getUser(){
        return user;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setUser(Author user) {
        this.user = user;
    }
}
