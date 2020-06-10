package com.androidclcp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import cpcl.PrinterHelper;


public class Activity_Status  extends Activity
{	
	private Context thisCon;
	private TextView txtStatus=null;
	private int iStatusMode=1;
	private String sStatus="";
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);	   
		this.requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_status);	
		thisCon=this.getApplicationContext();
		
		txtStatus=(TextView)this.findViewById(R.id.txtStatus);
//		iStatusMode=this.getIntent().getIntExtra("StatusMode", 1);
		Refresh();
	}
	
	public void onClickRefresh(View view) 
	{
    	if (!checkClick.isClickEvent()) return;
    	
    	try
    	{
    		Refresh();
    	}
    	catch(Exception e)
    	{
    		
    	}
    }	
	private void Refresh(){
		txtStatus.setText("");
		try {
			int getstatus = PrinterHelper.getstatus();
			switch (getstatus) {
			case 0:
				txtStatus.setText(getString(R.string.activity_Statues_ready));
				break;
			case 2:
				txtStatus.setText(getString(R.string.activity_Statues_nopage));
				break;
			case 6:
				txtStatus.setText(getString(R.string.activity_Statues_open));
				break;
			default:
				txtStatus.setText(getString(R.string.activity_Statues_error));
				break;
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.e("TAG", "Activity_Status->REfresh"+e.getMessage().toString());
		}
	}
	/*private void Refresh()
	{
		try
		{
			int iReturn=-1;
			byte[] statusData;
			if(iStatusMode == 1)	//only transmit status
			{
				statusData=new byte[1];
				iReturn=HPRTPrinterHelper.GetTransmitStatus(1, statusData);
				if(iReturn>0)
				{
					if(statusData[0]==0)
						sStatus=thisCon.getString(R.string.activity_status_transmit_status_no_paper);
					else if(statusData[0]==1)
						sStatus=thisCon.getString(R.string.activity_status_transmit_status_has_paper);
				}
				else
				{
					txtStatus.setText("1"+thisCon.getText(R.string.activity_status_error));
					return;
				}
			}
			if(iStatusMode == 32)	//mpt-ii mode status
			{
				statusData=new byte[1];
				iReturn=HPRTPrinterHelper.GetTransmitState(32, statusData);
				if(iReturn>0)
				{
					if(statusData[0]==0)
						sStatus=thisCon.getString(R.string.activity_status_transmit_status_no_paper);
					else if(statusData[0]==1)
						sStatus=thisCon.getString(R.string.activity_status_transmit_status_has_paper);
				}
			}
			if((iStatusMode & 2) > 0)	//have real time status,needn't transmit status
			{
				//get paper status
				statusData=new byte[1];
				iReturn=HPRTPrinterHelper.GetRealTimeStatus((byte)HPRTPrinterHelper.PRINTER_REAL_TIME_STATUS_ITEM_PAPER, statusData);
				if(iReturn<=0)
				{
					txtStatus.setText("2"+thisCon.getText(R.string.activity_status_error));
					return;
				}
				if((statusData[0] & 8)>0)
					sStatus=thisCon.getString(R.string.activity_status_real_time_status_paper_near_end);
				else
					sStatus=thisCon.getString(R.string.activity_status_real_time_status_paper_adquate);
				if((statusData[0] & 64)>0)
					sStatus+="\n" + thisCon.getString(R.string.activity_status_transmit_status_no_paper);
				else
					sStatus+="\n" + thisCon.getString(R.string.activity_status_transmit_status_has_paper);
				
				//get on/off line status
				statusData=new byte[1];
				iReturn=HPRTPrinterHelper.GetRealTimeStatus((byte)HPRTPrinterHelper.PRINTER_REAL_TIME_STATUS_ITEM_PRINTER, statusData);
				if(iReturn<=0)
				{
					txtStatus.setText("3"+thisCon.getText(R.string.activity_status_error));
					return;
				}
				if((statusData[0] & 8)>0)
				{
					//get off line status
					statusData=new byte[1];
					iReturn=HPRTPrinterHelper.GetRealTimeStatus((byte)HPRTPrinterHelper.PRINTER_REAL_TIME_STATUS_ITEM_ONOFFLINE, statusData);
					if(iReturn<=0)
					{
						txtStatus.setText("4"+thisCon.getText(R.string.activity_status_error));
						return;
					}
					if((statusData[0] & 4)>0)
						sStatus+="\n" + thisCon.getString(R.string.activity_status_real_time_status_cover_is_open);
					if((statusData[0] & 8)>0)
						sStatus+="\n" + thisCon.getString(R.string.activity_status_real_time_status_pressed_feed_button);
					if((statusData[0] & 32)>0)
						sStatus+="\n" + thisCon.getString(R.string.activity_status_real_time_status_stopped_no_paper);
					if((statusData[0] & 64)>0)
						sStatus+="\n" + thisCon.getString(R.string.activity_status_real_time_status_has_error);					
				}
				
				//get error status
				statusData=new byte[1];
				iReturn=HPRTPrinterHelper.GetRealTimeStatus((byte)HPRTPrinterHelper.PRINTER_REAL_TIME_STATUS_ITEM_ERROR, statusData);
				if(iReturn<0)
				{
					txtStatus.setText("5"+thisCon.getText(R.string.activity_status_error));
					return;
				}
				if((statusData[0] & 8)>0)
					sStatus+="\n" + thisCon.getString(R.string.activity_status_real_time_status_cutter_error);				
				if((statusData[0] & 32)>0)
					sStatus+="\n" + thisCon.getString(R.string.activity_status_real_time_status_unrecoverable_error);
				if((statusData[0] & 64)>0)
					sStatus+="\n" + thisCon.getString(R.string.activity_status_real_time_status_auto_recoverable_error);
			}
			if(sStatus.equals(""))
				txtStatus.setText("6"+thisCon.getText(R.string.activity_status_error));
			else
				txtStatus.setText(sStatus);
		}*/
		/*catch (Exception e) 
		{			
			Log.d("HPRTSDKSample", (new StringBuilder("Activity_Status --> Refresh ")).append(e.getMessage()).toString());
		}*/
//	}
}
