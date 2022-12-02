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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.pedroaguilar.andarivel.R;
import com.pedroaguilar.andarivel.modelo.TipoNotificacion;
import com.pedroaguilar.andarivel.presentacion.ui.login.LoginActivity;

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
            if (remoteMessage.getData().get("TipoNotificacion") != null && remoteMessage.getData().get("TipoNotificacion").equals(TipoNotificacion.RECORDATORIO.name())) {
                //Todo Consulta a los datos del usuario para ver si tiene un nodo fichaje correspondiente al día actual
                database.getFichajesUser(firebaseAuth.getUid(), task -> {
                    if (task.isSuccessful()) {
                        //Accedemos al mapa fichajes del nodo del usuario
                        Map<String, Object> fichajes = (Map<String, Object>) ((Map<String, Object>) task.getResult().getValue()).get("Fichajes");
                        if (fichajes != null) {
                            //Nos guardamos las referencias de los fichajes del usuario
                            for (String key : fichajes.keySet()) {
                                fichajes.put(key, null);
                            }
                            //todo: ahora que tengo una lista de fichajes del usuario, hay que comprobar si el fichaje pertenece al día actual
                            Log.i("fichajes", fichajes.toString());
                        } else {
                            Toast.makeText(this, "Este usuario no tiene fichajes", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                //Todo Si no tiene el nodo; creamos la notificacion, si sí lo tiene; nada
                sendNotification(
                        remoteMessage.getData().get("Title"),
                        remoteMessage.getData().get("message")
                );
            } else {
                sendNotification(
                        remoteMessage.getData().get("Title"),
                        remoteMessage.getData().get("message")
                );
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
                PendingIntent.FLAG_ONE_SHOT
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
