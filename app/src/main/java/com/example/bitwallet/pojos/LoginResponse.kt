import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class LoginModel (
    @SerializedName("username") @Expose val username: String,
    @SerializedName("password") @Expose val password: String,
    @SerializedName("token") @Expose val token: String,
    @SerializedName("status") @Expose val status: String,
    @SerializedName("message") @Expose val message: String
)


data class BalanceModel (
    @SerializedName("x-access-token") @Expose val token: String,
    @SerializedName("balance") @Expose val balance: String,
    @SerializedName("status") @Expose val status: String,
    @SerializedName("auth") @Expose val auth: String,
    @SerializedName("message") @Expose val message: String
)

data class ExchangeModel (
    @SerializedName("x-access-token") @Expose val token: String,
    @SerializedName("price") @Expose val price: String,
    @SerializedName("change24h") @Expose val change24h: String,
    @SerializedName("status") @Expose val status: String,
    @SerializedName("auth") @Expose val auth: String,
    @SerializedName("message") @Expose val message: String
)
