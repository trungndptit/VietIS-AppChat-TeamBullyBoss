package com.vietis.bullybosschat.notifications;

import com.vietis.bullybosschat.notifications.Response;
import com.vietis.bullybosschat.notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAn-68-g0:APA91bEzcVMhdoPAb0zbBYqQ_wEQZ4r0OwE58lX7ZyE3HptGaR28YSRZUIc_kJ5CM6Lr3Pix8_gT7KaSoCGtYT3OTwn0Jpik_wQ_rSQmM6fcBnhB3lWvdX4gusUkq8EMc7c1j5NxDgkw"
            }
    )


    @POST("fcm/send")
    Call<Response> sendNotification(@Body Sender body);
}
