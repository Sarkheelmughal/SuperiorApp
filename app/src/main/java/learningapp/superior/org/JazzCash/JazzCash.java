package learningapp.superior.org.JazzCash;

public class JazzCash {

    private String phoneNumber;
    private double amount;
    private String cinc;
    private int status;
    private JazzCashReponse jazzCashReponse;


    public String getPhoneNumber() {
        return phoneNumber;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setCinc(String cinc) {
        this.cinc = cinc;
    }

    public String getCinc() {
        return cinc;
    }

    public static class JazzCashReponse {

        public JazzCashReponse() {
        }

        private String pp_Amount;

        private String pp_AuthCode;

        private String pp_BillReference;

        private String pp_Language;

        private String pp_MerchantID;

        private String pp_ResponseCode;

        private String pp_ResponseMessage;

        private String pp_RetreivalReferenceNo;

        private String pp_SubMerchantID;

        private String pp_TxnCurrency;

        private String pp_TxnDateTime;

        private String pp_TxnRefNo;

        private String pp_MobileNumber;

        private String pp_CNIC;

        private String pp_DiscountedAmount;

        private String ppmpf_1;

        private String ppmpf_2;

        private String ppmpf_3;

        private String ppmpf_4;

        private String ppmpf_5;

        private String pp_SecureHash;

        public void setPp_Amount(String pp_Amount) {
            this.pp_Amount = pp_Amount;
        }

        public String getPp_Amount() {
            return this.pp_Amount;
        }

        public void setPp_AuthCode(String pp_AuthCode) {
            this.pp_AuthCode = pp_AuthCode;
        }

        public String getPp_AuthCode() {
            return this.pp_AuthCode;
        }

        public void setPp_BillReference(String pp_BillReference) {
            this.pp_BillReference = pp_BillReference;
        }

        public String getPp_BillReference() {
            return this.pp_BillReference;
        }

        public void setPp_Language(String pp_Language) {
            this.pp_Language = pp_Language;
        }

        public String getPp_Language() {
            return this.pp_Language;
        }

        public void setPp_MerchantID(String pp_MerchantID) {
            this.pp_MerchantID = pp_MerchantID;
        }

        public String getPp_MerchantID() {
            return this.pp_MerchantID;
        }

        public void setPp_ResponseCode(String pp_ResponseCode) {
            this.pp_ResponseCode = pp_ResponseCode;
        }

        public String getPp_ResponseCode() {
            return this.pp_ResponseCode;
        }

        public void setPp_ResponseMessage(String pp_ResponseMessage) {
            this.pp_ResponseMessage = pp_ResponseMessage;
        }

        public String getPp_ResponseMessage() {
            return this.pp_ResponseMessage;
        }

        public void setPp_RetreivalReferenceNo(String pp_RetreivalReferenceNo) {
            this.pp_RetreivalReferenceNo = pp_RetreivalReferenceNo;
        }

        public String getPp_RetreivalReferenceNo() {
            return this.pp_RetreivalReferenceNo;
        }

        public void setPp_SubMerchantID(String pp_SubMerchantID) {
            this.pp_SubMerchantID = pp_SubMerchantID;
        }

        public String getPp_SubMerchantID() {
            return this.pp_SubMerchantID;
        }

        public void setPp_TxnCurrency(String pp_TxnCurrency) {
            this.pp_TxnCurrency = pp_TxnCurrency;
        }

        public String getPp_TxnCurrency() {
            return this.pp_TxnCurrency;
        }

        public void setPp_TxnDateTime(String pp_TxnDateTime) {
            this.pp_TxnDateTime = pp_TxnDateTime;
        }

        public String getPp_TxnDateTime() {
            return this.pp_TxnDateTime;
        }

        public void setPp_TxnRefNo(String pp_TxnRefNo) {
            this.pp_TxnRefNo = pp_TxnRefNo;
        }

        public String getPp_TxnRefNo() {
            return this.pp_TxnRefNo;
        }

        public void setPp_MobileNumber(String pp_MobileNumber) {
            this.pp_MobileNumber = pp_MobileNumber;
        }

        public String getPp_MobileNumber() {
            return this.pp_MobileNumber;
        }

        public void setPp_CNIC(String pp_CNIC) {
            this.pp_CNIC = pp_CNIC;
        }

        public String getPp_CNIC() {
            return this.pp_CNIC;
        }

        public void setPp_DiscountedAmount(String pp_DiscountedAmount) {
            this.pp_DiscountedAmount = pp_DiscountedAmount;
        }

        public String getPp_DiscountedAmount() {
            return this.pp_DiscountedAmount;
        }

        public void setPpmpf_1(String ppmpf_1) {
            this.ppmpf_1 = ppmpf_1;
        }

        public String getPpmpf_1() {
            return this.ppmpf_1;
        }

        public void setPpmpf_2(String ppmpf_2) {
            this.ppmpf_2 = ppmpf_2;
        }

        public String getPpmpf_2() {
            return this.ppmpf_2;
        }

        public void setPpmpf_3(String ppmpf_3) {
            this.ppmpf_3 = ppmpf_3;
        }

        public String getPpmpf_3() {
            return this.ppmpf_3;
        }

        public void setPpmpf_4(String ppmpf_4) {
            this.ppmpf_4 = ppmpf_4;
        }

        public String getPpmpf_4() {
            return this.ppmpf_4;
        }

        public void setPpmpf_5(String ppmpf_5) {
            this.ppmpf_5 = ppmpf_5;
        }

        public String getPpmpf_5() {
            return this.ppmpf_5;
        }

        public void setPp_SecureHash(String pp_SecureHash) {
            this.pp_SecureHash = pp_SecureHash;
        }

        public String getPp_SecureHash() {
            return this.pp_SecureHash;
        }
    }
    public static  int SUCCESS=1;
    public static int NETWORK_ERROR=2;
    public static int LOW_BALANCE=3;
    public static int AUTH_ERROR=4;
}