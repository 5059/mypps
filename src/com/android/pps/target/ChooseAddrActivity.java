package com.android.pps.target;

import com.android.pps.R;
import android.app.Activity;  
import android.content.Context;  
import android.os.Bundle;  
import android.view.LayoutInflater;  
import android.view.View;  
import android.view.ViewGroup;  
import android.widget.BaseAdapter;  
import android.widget.ImageView;  
import android.widget.ListView;  
import android.widget.TextView;  
  
public class ChooseAddrActivity extends Activity {  
    String[] titles = { "China", "UK", "USA", "Japan", "Canada", "NanChong", "XiChong" };  
    int[] resIds = { R.drawable.ic_launcher, R.drawable.ic_launcher, R.drawable.ic_launcher, R.drawable.ic_launcher, R.drawable.ic_launcher,  
            R.drawable.ic_launcher, R.drawable.ic_launcher };  
  
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.choose_addr);  
        ListView listView = (ListView) findViewById(R.id.list);  
        listView.setAdapter(new ListViewAdapter(titles, resIds));  
    }  
  
    public class ListViewAdapter extends BaseAdapter {  
        View[] itemViews;  
  
        public ListViewAdapter(String[] itemTitles, int[] itemImageRes) {  
            itemViews = new View[itemTitles.length];  
  
            for (int i = 0; i < itemViews.length; i++) {  
                itemViews[i] = makeItemView(itemTitles[i], itemImageRes[i]);  
            }  
        }  
  
        public int getCount() {  
            return itemViews.length;  
        }  
  
        public View getItem(int position) {  
            return itemViews[position];  
        }  
  
        public long getItemId(int position) {  
            return position;  
        }  
  
        private View makeItemView(String strTitle, int resId) {  
            LayoutInflater inflater = (LayoutInflater) ChooseAddrActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
  
            // 使用View的对象itemView与R.layout.item关联  
            View itemView = inflater.inflate(R.layout.addr_item, null);  
  
            // 通过findViewById()方法实例R.layout.item内各组件  
            TextView title = (TextView) itemView.findViewById(R.id.title);  
            title.setText(strTitle);  
            ImageView image = (ImageView) itemView.findViewById(R.id.img);  
            image.setImageResource(resId);  
  
            return itemView;  
        }  
  
        public View getView(int position, View convertView, ViewGroup parent) {  
            if (convertView == null)  
                return itemViews[position];  
            return convertView;  
        }  
    }  
}  
