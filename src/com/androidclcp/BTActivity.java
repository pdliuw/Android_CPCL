package com.androidclcp;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BTActivity extends Activity {

    @BindView(R.id.recy_history)
    RecyclerView recyHistory;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.activity_bt)
    RelativeLayout activityBt;
    private Context mContext;
    private ListView list_bt;
    public BluetoothAdapter myBluetoothAdapter;
    private Intent intent;
    private BaseQuickAdapter<PrintBT, BaseViewHolder> baseQuickAdapter;
    private List<PrintBT> list;
    private int tag;
    private Bluetooth bluetooth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bt);
        ButterKnife.bind(this);
        mContext = getApplicationContext();
        initData();
    }

    private void initData() {
        intent = getIntent();
        tag = intent.getIntExtra("TAG", RESULT_CANCELED);
        ListBluetoothDevice();
    }
    public void ListBluetoothDevice() {
        if ((myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()) == null) {
            Toast.makeText(this, "没有找到蓝牙适配器", Toast.LENGTH_LONG).show();
            return;
        }

        if (!myBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 2);
        }
        list = new ArrayList<PrintBT>();
        baseQuickAdapter = new BaseQuickAdapter<PrintBT, BaseViewHolder>(android.R.layout.simple_list_item_2, list) {

            @Override
            protected void convert(BaseViewHolder helper, PrintBT item) {
                helper.setText(android.R.id.text1, item.getBTname());
                helper.setText(android.R.id.text2, item.getBTmac());
            }
        };
        recyHistory.setLayoutManager(new LinearLayoutManager(mContext));
        recyHistory.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
        recyHistory.setAdapter(baseQuickAdapter);
        bluetooth = Bluetooth.getBluetooth(this);
        initBT();
        baseQuickAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent();
                intent.putExtra("SelectedBDAddress", list.get(position).getBTmac());
                setResult(tag, intent);
                finish();
            }
        });
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initBT();
                if (swipeRefresh.isRefreshing())
                    swipeRefresh.setRefreshing(false);
            }
        });
    }
    private void initBT() {
        Log.d("TAG","initBT:");
        list.clear();
        baseQuickAdapter.notifyDataSetChanged();
        bluetooth.doDiscovery();
        bluetooth.getData(new Bluetooth.toData() {
            @Override
            public void succeed(String BTname, String BTmac) {
                for (PrintBT printBT : list) {
                    if (BTmac.equals(printBT.getBTmac())) {
                        return;
                    }
                }
                //XiangYinBao_X3,ATOL1
                Log.d("TAG","BTname:"+BTname);
                PrintBT printBT = new PrintBT();
                printBT.setBTname(BTname);
                printBT.setBTmac(BTmac);
                list.add(printBT);
                baseQuickAdapter.notifyDataSetChanged();
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        bluetooth.disReceiver();
    }
}
