package com.android.pps.target;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.pps.R;
import com.android.pps.db.SqliteUnitl;
import com.android.pps.util.Address;
import com.android.pps.util.SwipeDismissListView;
import com.android.pps.util.SwipeDismissListView.OnDismissCallback;
  
public class ChooseAddrActivity extends Activity {  
    List<Address> addresses = new ArrayList<Address>();  
    
    int resIds = R.drawable.ic_launcher;  
    
    SwipeDismissListView listView;
    ListViewAdapter adapter;
    
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.choose_addr);  
        setTitle("选择地址");
        listView = (SwipeDismissListView) findViewById(R.id.list);
        addresses = SqliteUnitl.getAllAddress(this);
        adapter = new ListViewAdapter(addresses, resIds);
        listView.setAdapter(adapter); 
        listView.setOnDismissCallback(new MyDismissCallback()); 
        listView.setOnItemLongClickListener(new MyItemLongClickListener());
        listView.setOnItemClickListener(new MyItemClickListener());
    }  
    
    private class MyDismissCallback implements OnDismissCallback{

        @Override  
        public void onDismiss(int position) {  
			//从数据库中删除
			SqliteUnitl.deleteAddress(ChooseAddrActivity.this, ChooseAddrActivity.this.addresses.get(position).get_id());
			//删除item
			ChooseAddrActivity.this.addresses.remove(position);
			ChooseAddrActivity.this.listView.setAdapter(new ListViewAdapter(ChooseAddrActivity.this.addresses, ChooseAddrActivity.this.resIds));
        }
    }
    
    private class MyItemLongClickListener implements OnItemLongClickListener{ 
    	
		@Override
		public boolean onItemLongClick(AdapterView<?> parent, final View view,
				final int position, long id) {
//            Toast.makeText(ChooseAddrActivity.this, "(长按)您选择的是" + ChooseAddrActivity.this.addresses.get(position).getAddress(), Toast.LENGTH_SHORT).show(); 
            Builder builder = new AlertDialog.Builder(ChooseAddrActivity.this);
            builder.setTitle("选项");
			builder.setItems(new String[] { "删除", "取消" },
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							if (which == 0) {
								//删除item
								ChooseAddrActivity.this.listView.performDismiss(view, position);
							}
						}
					});
			builder.show();
            return true; 
		}
    }
    
    private class MyItemClickListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
            /*
             * 点击列表项时触发onItemClick方法，四个参数含义分别为
             * arg0：发生单击事件的AdapterView
             * arg1：AdapterView中被点击的View 
             * position：当前点击的行在adapter的下标
             * id：当前点击的行的id
             */
//            Toast.makeText(ChooseAddrActivity.this,
//                            "(单击)您选择的是" + ChooseAddrActivity.this.addresses.get(position).getAddress(),
//                            Toast.LENGTH_SHORT).show(); 
            //传送选择结果
			Intent resultIntent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putSerializable("addressObj", ChooseAddrActivity.this.addresses.get(position));
			resultIntent.putExtras(bundle);
			ChooseAddrActivity.this.setResult(RESULT_OK, resultIntent);
			ChooseAddrActivity.this.finish();
		}
    }
    
    public class ListViewAdapter extends BaseAdapter {  
        View[] itemViews;  
  
        public ListViewAdapter(List<Address> addresses, int itemImageRes) {  
            itemViews = new View[addresses.size()];  
            for (int i = 0; i < itemViews.length; i++) {  
                itemViews[i] = makeItemView(addresses.get(i), itemImageRes);  
            }
        }  
  
        @Override
        public int getCount() {  
            return itemViews.length;  
        }  
  
        public View getItem(int position) {  
            return itemViews[position];  
        }  
  
        public long getItemId(int position) {  
            return position;  
        }  
  
        private View makeItemView(Address address, int resId) {  
            LayoutInflater inflater = (LayoutInflater) ChooseAddrActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
  
            // 使用View的对象itemView与R.layout.item关联  
            View itemView = inflater.inflate(R.layout.addr_item, null);  
  
            // 通过findViewById()方法实例R.layout.item内各组件  
            TextView title = (TextView) itemView.findViewById(R.id.title);  
            TextView title1 = (TextView) itemView.findViewById(R.id.title1);
            title.setText(address.getAddress());  
            title1.setText(address.getSaveTime());
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
