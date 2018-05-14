package layout;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.mathi.expense.DatabaseHelper;
import com.example.mathi.expense.R;

import java.util.Calendar;

import static java.security.AccessController.getContext;

/**
 * Implementation of App Widget functionality.
 */
public class NewAppWidget extends AppWidgetProvider {

    static DatabaseHelper databaseHelper;
    static int mnth,a,b,d;
    static String mnth1;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        databaseHelper = new DatabaseHelper(context);
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);
        mnth=month+1;
        if (month < 10) {
            mnth1="0"+mnth;
        }
        else{
            mnth1=""+mnth;
        }
        Cursor c = databaseHelper.getTotalexpense(mnth1);
        if( c != null && c.moveToFirst()){
            a=c.getInt(0);
        }
        c = databaseHelper.getTotalincome(mnth1);
        if( c != null && c.moveToFirst()){
            b=c.getInt(0);
        }
        d=b-a;

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
        views.setTextViewText(R.id.appwidget_income, String.valueOf(b)+"+");
        views.setTextViewText(R.id.appwidget_expense, String.valueOf(a)+"-");
        views.setTextViewText(R.id.appwidget_total,String.valueOf(d)+"=");

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);


        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created


    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

