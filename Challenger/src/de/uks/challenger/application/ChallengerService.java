package de.uks.challenger.application;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import de.uks.challenger.R;
import de.uks.challenger.model.Challenger;
import de.uks.challenger.model.User;
import de.uks.challenger.ui.MainActivity;

/**
 * Services which notifies the user, that he has to do a workout.
 * 
 * @author philipp
 * 
 */
public class ChallengerService extends Service {

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
//		Timer timer = new Timer();
//		timer.schedule(new TimerTask() {
//			
//			@Override
//			public void run() {
//				User user = Challenger.getInstance().getUser();
//				if(user == null){
//					//user not set yet
//					return;
//				}
//				
//				//get setted time from user
//				Date temp = user.getWorkoutTime();
//				Calendar workoutTime = new GregorianCalendar();
//				workoutTime.setTime(temp);
//				
//				
//				//get current time
//				temp = new Date();
//				Calendar currentTime = new GregorianCalendar();
//				currentTime.setTime(temp);
//				
//				//check if time is in range
//				int currentHour = currentTime.get(GregorianCalendar.HOUR);
//				int currentMin = currentTime.get(GregorianCalendar.MINUTE);
//				int currentSec = currentTime.get(GregorianCalendar.SECOND);
//				int 
//				int workoutMin = workoutTime.get(GregorianCalendar.MINUTE);
//				int workoutSec = workoutTime.get(GregorianCalendar.SECOND);
//				
//				boolean minMatches = currentMin == workoutMin &&  (currentMin <= (workoutMin));
//				
//				
//				Intent notificationIntent = new Intent(ChallengerService.this, MainActivity.class);
//				PendingIntent contentIntent = PendingIntent.getActivity(ChallengerService.this, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
//
//				NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//				Resources res = getResources();
//				Notification.Builder builder = new Notification.Builder(ChallengerService.this);
//
//				builder.setContentIntent(contentIntent).setSmallIcon(R.drawable.ic_launcher).setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.ic_launcher)).setTicker("your_ticker").setWhen(System.currentTimeMillis()).setAutoCancel(true)
//						.setContentTitle("your_notif_title").setContentText("your_notif_text");
//				Notification n = builder.build();
//
//				int YOUR_NOTIF_ID = 2303;
//				nm.notify(YOUR_NOTIF_ID, n);
//
//				
//			}
//		}, 0, 5000);
//			
		
		
		return Service.START_NOT_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		// not needed
		return null;
	}

}
