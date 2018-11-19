package cn.gdcp.sqlitelistview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by acer on 2018/11/19.
 */

public class StuAdater extends BaseAdapter {
    private ArrayList<Student> studentArrayList;
    private Context context;
    private IOnListener listener;

    public StuAdater(ArrayList<Student> studentArrayList, Context context, IOnListener listener) {
        this.studentArrayList = studentArrayList;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return studentArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return studentArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        final Student student=studentArrayList.get(i);
        View view;
        if(convertView==null){
            view=LayoutInflater.from(context).inflate(R.layout.student,null);
        }else{
            view=convertView;
        }
        TextView tv_sno= (TextView) view.findViewById(R.id.txt_stuno);
        TextView tv_name= (TextView) view.findViewById(R.id.txt_name);
        TextView tv_age= (TextView) view.findViewById(R.id.txt_age);
        ImageView iv_imag= (ImageView) view.findViewById(R.id.iv_del);
        tv_sno.setText(student.getStuno());
        tv_name.setText(student.getName());
        tv_age.setText(String.valueOf(student.getAge()));
        iv_imag.setImageResource(R.drawable.del);
        //实现点击删除图标删除学生
        iv_imag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.delete(student);
            }
        });
        return view;
    }
}
