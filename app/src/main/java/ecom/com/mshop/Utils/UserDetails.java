package ecom.com.mshop.Utils;

/**
 * Created by Pandey on 17-11-2016.
 *
 *
 */
import com.google.gson.annotations.SerializedName;

import java.math.BigInteger;

public class UserDetails {

    private boolean status;
    private String message;
    private UserData userData;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public class UserData {
        private String userName;
        private String memberID;
        private String mobileNumber;
        private String emailID;

        public String getEmailID() {
            return emailID;
        }

        public void setEmailID(String emailID) {
            this.emailID = emailID;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getMemberID() {
            return memberID;
        }

        public void setMemberID(String memberID) {
            this.memberID = memberID;
        }

        public String getMobileNumber() {
            return mobileNumber;
        }

        public void setMobileNumber(String mobileNumber) {
            this.mobileNumber = mobileNumber;
        }
    }
}
