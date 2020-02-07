package ma.ymrabti.youneswhatsapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ma.ymrabti.youneswhatsapp.Model.Chatt;
import ma.ymrabti.youneswhatsapp.R;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    private List<Chatt> chatts;
    private Context mContext;
    private String imageURL;
    private FirebaseUser firebaseUser;

    public MessageAdapter(List<Chatt> listContacts, Context mContext, String imageURL) {
        this.chatts = listContacts;
        this.mContext = mContext;
        this.imageURL = imageURL;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT){
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right,parent,false);
            return new MessageAdapter.ViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left,parent,false);
            return new MessageAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {
        Chatt chatt= chatts.get(position);
        holder.show_message.setText(chatt.getMessage());
        if (imageURL.equals("default")){
            holder.imageContact.setImageResource(R.drawable.avatar_mini);
        }
        else {
            Glide.with(mContext).load(imageURL).into(holder.imageContact);
        }
    }

    @Override
    public int getItemCount() {
        return chatts.size();
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (chatts.get(position).getSender().equals(firebaseUser.getUid())){
            return MSG_TYPE_RIGHT;
        }
        else {
            return MSG_TYPE_LEFT;
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView show_message;
        CircleImageView imageContact;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            show_message = itemView.findViewById(R.id.show_message);
            imageContact = itemView.findViewById(R.id.profile_image_conversation);
        }
    }
}
