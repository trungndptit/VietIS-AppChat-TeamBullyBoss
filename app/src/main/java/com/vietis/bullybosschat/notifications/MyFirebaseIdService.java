package com.vietis.bullybosschat.notifications;


import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

public class MyFirebaseIdService extends FirebaseMessagingService {

    /*
    là 1 lớp kế thừa từ FirebaseMessagingService (Thay thế cho FirebaseInstanceIdService đã bị khai tử năm 2018.
    Service này xử lý việc tạo, tái tạo và cập nhật lại việc đăng ký token.
    Service này là bắt buộc.
     */

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        // truy xuất token hiện tại, hàm này trả về null nếu token ko có sự thay đổi
        String refreshToken = FirebaseInstanceId.getInstance().getToken();
        if (firebaseUser != null){
            updateToken(refreshToken);
        }
    }

    private void updateToken (String newToken){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference mData = FirebaseDatabase.getInstance().getReference();
        Token token = new Token(newToken);
        mData.child("Tokens").child(firebaseUser.getUid()).setValue(token);
    }


}
