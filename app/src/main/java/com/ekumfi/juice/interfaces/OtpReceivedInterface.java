package com.ekumfi.juice.interfaces;

/**
 * Created by Andy on 2/23/2020.
 */

public interface OtpReceivedInterface {
    void onOtpReceived(String otp);
    void onOtpTimeout();
}
