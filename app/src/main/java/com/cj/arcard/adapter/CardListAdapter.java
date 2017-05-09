package com.cj.arcard.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cj.arcard.R;
import com.cj.arcard.bean.CardInfo;
import com.cj.arcard.ui.ar.ARActivity;
import com.cj.arcard.ui.home.MsgEditActivity;
import com.cj.arcard.utils.LogUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/3/20.
 */

public class CardListAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<CardInfo> mCardArrayList;

    public CardListAdapter(Context context, ArrayList<CardInfo> cardArrayList) {
        mContext = context;
        mCardArrayList = cardArrayList;
    }

    @Override
    public int getCount() {
        return mCardArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {

        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_card_list, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        final CardInfo cardInfo = mCardArrayList.get(position);
        viewHolder.cardName.setText(cardInfo.getCardName());
        viewHolder.cardDescribe.setText(cardInfo.getCardDescribe());
        Glide.with(mContext).load(cardInfo.getCardPictureUrl()).into(viewHolder.cardImg);
        viewHolder.arMessageEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MsgEditActivity.class);
                try {
                    String videoUrl = cardInfo.getCardVideoUrl();
                    LogUtil.d(videoUrl);
                    if (videoUrl!=null){
                        intent.putExtra("videoUrl",videoUrl);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String cardId = cardInfo.getCardId();
                intent.putExtra("cardId",cardId);
                String receiverName = cardInfo.getCardReceiverName();
                intent.putExtra("receiverName",receiverName);
                mContext.startActivity(intent);

            }
        });

        viewHolder.arScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, ARActivity.class));
            }
        });
        return view;
    }


    static class ViewHolder {
        @BindView(R.id.card_name)
        TextView cardName;
        @BindView(R.id.card_img)
        ImageView cardImg;
        @BindView(R.id.card_describe)
        TextView cardDescribe;
        @BindView(R.id.ar_message_edit)
        Button arMessageEdit;
        @BindView(R.id.ar_scan)
        Button arScan;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
