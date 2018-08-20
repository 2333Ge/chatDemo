package x.x.com.oneandroidtest1;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xx.utils.MyApplication;
import com.xx.utils.TimeHelper;

import java.sql.Time;
import java.util.List;

public class RvChatAdapter extends RecyclerView.Adapter<RvChatAdapter.ViewHolder> {

    private List<MessageBean> listMessage;

    private Context context;
    RvChatAdapter(List<MessageBean> listMessage, Context context){
        this.listMessage = listMessage;
        this.context = context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(),R.layout.item_chat,null);
        ViewHolder viewHolder  = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MessageBean message = listMessage.get(position);
        boolean fromOthers = message.getFromOthers();

        String content = message.getContent();
        String messageType = message.getMessageType();
        if( !messageType.equals(MessageBean.TEXT) ){//其他类型时有其他操作，此处暂不做处理
            Log.e("messageType",messageType);
            return;
        }
        if (fromOthers){
            holder.ll_left.setVisibility(View.VISIBLE);
            holder.ll_right.setVisibility(View.GONE);
            holder.tv_ChatContentLeft.setText(content);
            if(position == 0){
                holder.tv_timeLeft.setVisibility(View.VISIBLE);
                holder.tv_timeLeft.setText(TimeHelper.longToLocalTime(listMessage.get(position).getTime()));
            }else{
                Long timeLast = listMessage.get(position - 1).getTime();
                Long timeThis = listMessage.get(position).getTime();
                if(TimeHelper.isBelow2Minutes(timeLast,timeThis)){
                    holder.tv_timeLeft.setVisibility(View.GONE);
                }else{
                    holder.tv_timeLeft.setVisibility(View.VISIBLE);
                    holder.tv_timeLeft.setText(TimeHelper.longToLocalTime(timeThis));
                }
            }

        }else{//自己发送过去的
            holder.ll_left.setVisibility(View.GONE);
            holder.ll_right.setVisibility(View.VISIBLE);
            holder.tv_ChatContentRight.setText(content);
            boolean isSendSuccessful = message.isSendSuccessful();
            if( !isSendSuccessful){
                holder.iv_sendFail.setVisibility(View.VISIBLE);
                holder.iv_sendFail.setOnClickListener(new SendFailClickListener(position));//
            }else{
                holder.iv_sendFail.setVisibility(View.GONE);
            }
            if(position == 0){
                holder.tv_timeRight.setVisibility(View.VISIBLE);
                holder.tv_timeRight.setText(TimeHelper.longToLocalTime(listMessage.get(position).getTime()));
            }else{
                Long timeLast = listMessage.get(position - 1).getTime();
                Long timeThis = listMessage.get(position).getTime();
                if(TimeHelper.isBelow2Minutes(timeLast,timeThis)){
                    holder.tv_timeRight.setVisibility(View.GONE);
                }else{
                    holder.tv_timeRight.setVisibility(View.VISIBLE);
                    holder.tv_timeRight.setText(TimeHelper.longToLocalTime(timeThis));
                }

            }
        }

    }

    @Override
    public int getItemCount() {
        return listMessage.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout ll_left,ll_right;
        TextView tv_timeLeft,tv_ChatContentLeft;
        TextView tv_timeRight,tv_ChatContentRight;
        ImageView iv_sendFail;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_timeLeft = itemView.findViewById(R.id.tv_chatTimeLeft);
            tv_ChatContentLeft = itemView.findViewById(R.id.tv_chatLeft);
            tv_timeRight = itemView.findViewById(R.id.tv_chatTimeRight);
            tv_ChatContentRight = itemView.findViewById(R.id.tv_chatRight);
            ll_left = itemView.findViewById(R.id.ll_chatLeft);
            ll_right = itemView.findViewById(R.id.ll_chatRight);
            iv_sendFail = itemView.findViewById(R.id.iv_sendFail);
        }
    }
    class SendFailClickListener  implements View.OnClickListener {
        int position;
        SendFailClickListener(int position){
            this.position = position;
        }
        @Override
        public void onClick(View view) {
            sendAgainDialog(position);
        }
    }
    private void sendAgainDialog(final int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("发送失败");
        builder.setMessage("是否重新发送？");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                sendAgainListener.sendAgain(listMessage.get(position));
                listMessage.remove(position);//相应数据改变界面的变化交给activity
                notifyDataSetChanged();
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    SendAgainListener sendAgainListener ;

    /**
     * 对话框重新发送接口
     */
    public interface SendAgainListener{
        void sendAgain(MessageBean messageBean);
    }
    public void setOnSendAgainListener(SendAgainListener sendAgainListener){
        this.sendAgainListener = sendAgainListener;
    }

}
