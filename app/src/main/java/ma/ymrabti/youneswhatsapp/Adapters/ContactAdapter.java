package ma.ymrabti.youneswhatsapp.Adapters;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ma.ymrabti.youneswhatsapp.MessageActivity;
import ma.ymrabti.youneswhatsapp.Model.User;
import ma.ymrabti.youneswhatsapp.R;


public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {
    private List<User> listContacts;
    private Context mContext;

    public ContactAdapter(Context context, List<User> listContacts)
    {
        this.listContacts = listContacts;this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.element_contacts,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final User user = listContacts.get(position);
        holder.nomContact.setText(user.getUsername());
        if (user.getImageURL().equals("default")){
            holder.imageContact.setImageResource(R.drawable.avatar_mini);
        }
        else {
            Glide.with(mContext).load(user.getImageURL()).into(holder.imageContact);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MessageActivity.class);
                intent.putExtra("userid",user.getId());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listContacts.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nomContact;
        CircleImageView imageContact;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            nomContact = itemView.findViewById(R.id.contact_item_username);
            imageContact = itemView.findViewById(R.id.profile_pdp);
        }
    }
}
