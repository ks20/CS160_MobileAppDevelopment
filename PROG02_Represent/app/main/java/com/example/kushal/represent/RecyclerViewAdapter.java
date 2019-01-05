package com.example.kushal.represent;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Kushal on 10/3/18.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private ArrayList<String> mImageNames = new ArrayList<>();
    private ArrayList<Bitmap> mImages = new ArrayList<>();
    private ArrayList<String> mEmails = new ArrayList<>();
    private ArrayList<String> mParties = new ArrayList<>();
    private ArrayList<String> mUrls = new ArrayList<>();
    private HashMap<String, String> mNameAndIds = new HashMap<>();

    private Context mContext;

    public RecyclerViewAdapter(Context context, ArrayList<String> imageNames, ArrayList<Bitmap> images, ArrayList<String> emails, ArrayList<String> parties, ArrayList<String> urls, HashMap<String, String> nameAndIds) {
        mImageNames = imageNames;
        mImages = images;
        mEmails = emails;
        mParties = parties;
        mUrls = urls;
        mContext = context;
        mNameAndIds = nameAndIds;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final HashMap<String, ArrayList<Object>> ttt = new HashMap<String, ArrayList<Object>>();
        ArrayList<Object> ppp = new ArrayList<>();
        ppp.add(mImages.get(position));
        ppp.add(mParties.get(position));
        ppp.add(mEmails.get(position));
        ppp.add(mUrls.get(position));
        ttt.put(mImageNames.get(position), ppp);

        holder.repName.setText("Representative " + mImageNames.get(position));
        holder.repImage.setImageBitmap(mImages.get(position));
        holder.repParty.setText(mParties.get(position));

        String codify = " <a href=" + mEmails.get(position) + " >Email</a>";
        holder.repEmail.setMovementMethod(LinkMovementMethod.getInstance());
        holder.repEmail.setText(Html.fromHtml(codify, Html.FROM_HTML_MODE_LEGACY));

        String webCodify = " <a href=" + mUrls.get(position) + " >Website</a>";
        holder.repWeb.setMovementMethod(LinkMovementMethod.getInstance());
        holder.repWeb.setText(Html.fromHtml(webCodify, Html.FROM_HTML_MODE_LEGACY));

        if (mParties.get(position).equals("Democrat")) {
            holder.repParty.setTextColor(Color.parseColor("#0169E3"));
            holder.repWeb.setLinkTextColor(Color.parseColor("#0169E3"));
            holder.repEmail.setLinkTextColor(Color.parseColor("#0169E3"));
        }
        else if (mParties.get(position).equals("Republican")) {
            holder.repParty.setTextColor(Color.parseColor("#B6151B"));
            holder.repWeb.setLinkTextColor(Color.parseColor("#B6151B"));
            holder.repEmail.setLinkTextColor(Color.parseColor("#B6151B"));
        }

        holder.repDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MoreDetailsActivity.class);
                intent.putExtra("data", (Serializable)ttt);
                intent.putExtra("ids", (Serializable)mNameAndIds);
                mContext.startActivity(intent);
            }
        });

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, mImageNames.get(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mImageNames.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView repName, repParty, repEmail, repWeb;
        ImageView repImage;
        RelativeLayout parentLayout;
        View separator;
        Button repDetails;

        public ViewHolder(View itemView) {
            super(itemView);
            repName = itemView.findViewById(R.id.repName);
            repImage = itemView.findViewById(R.id.repImage);
            repParty = itemView.findViewById(R.id.repParty);
            repEmail = itemView.findViewById(R.id.repEmail);
            repWeb = itemView.findViewById(R.id.repWeb);
            repDetails = itemView.findViewById(R.id.moreDetails);
            separator = itemView.findViewById(R.id.line);

            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }


}
