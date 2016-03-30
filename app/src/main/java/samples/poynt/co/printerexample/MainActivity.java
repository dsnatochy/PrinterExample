package samples.poynt.co.printerexample;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import co.poynt.os.model.PrintedReceipt;
import co.poynt.os.model.PrintedReceiptLine;
import co.poynt.os.services.v1.IPoyntReceiptPrintingService;
import co.poynt.os.services.v1.IPoyntReceiptPrintingServiceListener;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";
    private Button printerBtn;
    private IPoyntReceiptPrintingService printingService;
    private ServiceConnection printingServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            printingService = IPoyntReceiptPrintingService.Stub.asInterface(iBinder);
            Log.d(TAG, "Printing service connected");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            printingService = null;
        }
    };
    private IPoyntReceiptPrintingServiceListener printListener = new IPoyntReceiptPrintingServiceListener.Stub() {
        @Override
        public void printQueued() throws RemoteException {
            Log.d(TAG, "printQueued");
        }

        @Override
        public void printFailed() throws RemoteException {
            Log.d(TAG, "PrintFailed");
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        printerBtn = (Button)findViewById(R.id.printBtn);

        printerBtn.setOnClickListener(new View.OnClickListener(){

            private PrintedReceiptLine newLine(String s){
                PrintedReceiptLine line = new PrintedReceiptLine();
                line.setText(s);
                return line;
            }
            @Override
            public void onClick(View view) {
                PrintedReceipt printedReceipt = new PrintedReceipt();

                // BODY
                List <PrintedReceiptLine> body = new ArrayList<PrintedReceiptLine>();

                body.add(newLine(" Check-in REWARD  "));
                body.add(newLine(""));
                body.add(newLine("FREE Reg. 1/2 Order"));
                body.add(newLine("Nachos or CHEESE"));
                body.add(newLine("Quesadilla with min."));
                body.add(newLine("$ 15 bill."));
                body.add(newLine(".................."));
                body.add(newLine("John Doe"));
                body.add(newLine("BD: May-5, AN: Aug-4"));
                body.add(newLine("john.doe@gmail.com"));
                body.add(newLine("Visit #23"));
                body.add(newLine("Member since: 15 June 2013"));
                body.add(newLine(".................."));
                body.add(newLine("Apr-5-2013 3:25 PM"));
                body.add(newLine("Casa Orozco, Dublin, CA"));
                body.add(newLine(".................."));
                body.add(newLine("Coupon#: 1234-5678"));
                body.add(newLine(" Check-in REWARD  "));
                body.add(newLine(""));
                body.add(newLine("FREE Reg. 1/2 Order"));
                body.add(newLine("Nachos or CHEESE"));
                body.add(newLine("Quesadilla with min."));
                body.add(newLine("$ 15 bill."));
                body.add(newLine(".................."));
                body.add(newLine("John Doe"));
                body.add(newLine("BD: May-5, AN: Aug-4"));
                body.add(newLine("john.doe@gmail.com"));
                body.add(newLine("Visit #23"));
                body.add(newLine("Member since: 15 June 2013"));
                body.add(newLine(".................."));
                body.add(newLine("Apr-5-2013 3:25 PM"));
                body.add(newLine("Casa Orozco, Dublin, CA"));
                body.add(newLine(".................."));
                body.add(newLine("Coupon#: 1234-5678"));
                body.add(newLine("  Powered by Poynt"));
                printedReceipt.setBody(body);

                String printJobId = UUID.randomUUID().toString();
                try {
                    printingService.printReceipt(printJobId,printedReceipt, printListener);

                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    protected void onResume(){
        super.onResume();
        bindService(new Intent(IPoyntReceiptPrintingService.class.getName()), printingServiceConnection, Context.BIND_AUTO_CREATE);
    }
    protected void onPause(){
        super.onPause();
        unbindService(printingServiceConnection);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return false;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
