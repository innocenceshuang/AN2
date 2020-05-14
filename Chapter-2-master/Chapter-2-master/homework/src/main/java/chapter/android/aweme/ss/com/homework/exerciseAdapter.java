package chapter.android.aweme.ss.com.homework;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.List;

import chapter.android.aweme.ss.com.homework.model.Message;
import chapter.android.aweme.ss.com.homework.model.PullParser;
import chapter.android.aweme.ss.com.homework.widget.CircleImageView;


public class exerciseAdapter extends RecyclerView.Adapter<exerciseAdapter.myViewHolder>{
    private int mNumberItems;
    private List<Message> messageList;
    private int count;

    private final ListItemClickListener mOnClickListener;

//    private static int viewHolderCount;

    public exerciseAdapter(List<Message> messages,ListItemClickListener listener) {
//        mNumberItems = numListItems;
        mOnClickListener = listener;
//        viewHolderCount = 0;
        count = 0;
        messageList=messages;
        mNumberItems=messages.size();
    }


    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.im_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        myViewHolder viewHolder = new myViewHolder(view);
        //
        viewHolder.tv_time.setText(messageList.get(count).getTime());
        viewHolder.tv_description.setText(messageList.get(count).getDescription());
        viewHolder.tv_title.setText(messageList.get(count).getTitle());

        if(messageList.get(count).isOfficial())
        {
            viewHolder.robot_notice.setVisibility(View.VISIBLE);
        }
        else
            viewHolder.robot_notice.setVisibility(View.INVISIBLE);

        if(messageList.get(count).getIcon().equals("TYPE_ROBOT"))
            viewHolder.iv_avatar.setImageResource(R.drawable.session_robot);
        else if(messageList.get(count).getIcon().equals("TYPE_GAME"))
            viewHolder.iv_avatar.setImageResource(R.drawable.icon_micro_game_comment);
        else if(messageList.get(count).getIcon().equals("TYPE_SYSTEM"))
            viewHolder.iv_avatar.setImageResource(R.drawable.session_system_notice);
        else if(messageList.get(count).getIcon().equals("TYPE_STRANGER"))
            viewHolder.iv_avatar.setImageResource(R.drawable.session_stranger);
        else if(messageList.get(count).getIcon().equals("TYPE_USER"))
            viewHolder.iv_avatar.setImageResource(R.drawable.icon_girl);
        count++;
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder myViewHolder, int position) {
        myViewHolder.bind(position);
    }



    @Override
    public int getItemCount() {
        return mNumberItems;
    }

    public class myViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final CircleImageView iv_avatar;
        private final ImageView robot_notice;
        private final TextView tv_title;
        private final TextView tv_description;
        private final TextView tv_time;

        public int itemPosition;

        public myViewHolder(@NonNull View itemView){
            super(itemView);
            iv_avatar=itemView.findViewById(R.id.iv_avatar);
            robot_notice=itemView.findViewById(R.id.robot_notice);
            tv_title=itemView.findViewById(R.id.tv_title);
            tv_description=itemView.findViewById(R.id.tv_description);
            tv_time=itemView.findViewById(R.id.tv_time);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
//            int clickedPosition = getAdapterPosition();
//            if (mOnClickListener != null) {
                mOnClickListener.onListItemClick(itemPosition);
//            }
        }

        public void bind(int position) {
            itemPosition=position;
        }
    }

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }
}
