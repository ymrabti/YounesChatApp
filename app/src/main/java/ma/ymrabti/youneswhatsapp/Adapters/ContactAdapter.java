package ma.ymrabti.youneswhatsapp.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
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
        User user = listContacts.get(position);
        holder.nomContact.setText(user.getUsername());
        if (user.getImageURL().equals("default")){
            holder.imageContact.setImageResource(R.drawable.avatar_mini);
        }
        else {
            Glide.with(mContext).load(user.getImageURL()).into(holder.imageContact);
        }
    }

    @Override
    public int getItemCount() {
        return listContacts.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nomContact;
        public CircleImageView imageContact;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nomContact = itemView.findViewById(R.id.contact_item_username);
            imageContact = itemView.findViewById(R.id.profile_pdp);
        }
    }
}
