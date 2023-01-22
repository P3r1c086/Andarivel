package com.pedroaguilar.andarivel.servicios;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.pedroaguilar.andarivel.R;
import com.pedroaguilar.andarivel.modelo.TipoNotificacion;
import com.pedroaguilar.andarivel.presentacion.ui.login.LoginActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;

public class MiServiciodeFirebaseMessaging extends FirebaseMessagingService {

    public static final String TAG = "MiServicioDeFirebase";

    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private final ServicioFirebaseDatabase database = new ServicioFirebaseDatabase();

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            if (remoteMessage.getData().get("TipoNotificacion") != null &&
                    remoteMessage.getData().get("TipoNotificacion").equals(TipoNotificacion.RECORDATORIO.name())) {
                //Consulta a los datos del usuario para ver si tiene un nodo fichaje correspondiente al día actual
                database.getFichajes(task -> {
                    if (task.isSuccessful()) {
                        Boolean notificar = true;
                        //Accedemos al mapa fichajes del nodo del usuario
                        Map<String, Object> fichajes = (Map<String, Object>) task.getResult().getValue();
                        if (fichajes != null) {
                            for (Map.Entry<String, Object> entry : fichajes.entrySet()) {
                                SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE dd 'de' MMMM 'de' YYYY", Locale.getDefault());
                                String now = dateFormat.format(Calendar.getInstance().getTime().getTime()).trim();
                                Object fechaFichaje = ((Map<String, Object>) entry.getValue()).get("fecha");
                                Object usuario = ((Map<String, Object>) entry.getValue()).get("usuario");
                                Object horaSalida = ((Map<String, Object>) entry.getValue()).get("horaSalida");
                                if (fechaFichaje != null && fechaFichaje.toString().equals(now) &&
                                        usuario != null && usuario.equals(firebaseAuth.getUid())) {
                                    notificar = false;
                                    break;
                                } else if (fechaFichaje != null && fechaFichaje.toString().equals(now) &&
                                        usuario != null && usuario.equals(firebaseAuth.getUid()) &&
                                        horaSalida != null) {
                                    notificar = false;
                                    break;
                                }
                            }
                        }
                        if (notificar){
                            sendNotification(
                                    remoteMessage.getData().get("Title"),
                                    remoteMessage.getData().get("message")
                            );
                        }
                    }
                });
            }
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    @Override
    public void onNewToken(@NonNull String token) {
        Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
        Log.d(TAG, "sendRegistrationTokenToServer(" + token + ")");
    }

    private void sendNotification(String title, String messageBody) {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0 /* Request code */, intent,
                PendingIntent.FLAG_IMMUTABLE
        );

        //Construimos la notificacion con su sonido, titulo, message en el body...
        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
