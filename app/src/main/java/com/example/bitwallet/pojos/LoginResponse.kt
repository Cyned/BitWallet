import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*


data class LoginModel (
    @SerializedName("username") @Expose val username: String,
    @SerializedName("password") @Expose val password: String,
    @SerializedName("token") @Expose val token: String,
    @SerializedName("status") @Expose val status: String,
    @SerializedName("message") @Expose val message: String
)


data class BalanceModel (
    @SerializedName("x-access-token") @Expose val token: String,
    @SerializedName("balance") @Expose val balance: Float,
    @SerializedName("status") @Expose val status: String,
    @SerializedName("auth") @Expose val auth: String,
    @SerializedName("message") @Expose val message: String
)

data class ExchangeModel (
    @SerializedName("x-access-token") @Expose val token: String,
    @SerializedName("price") @Expose val price: Float,
    @SerializedName("change24h") @Expose val change24h: Float,
    @SerializedName("status") @Expose val status: String,
    @SerializedName("pair") @Expose val pair: String,
    @SerializedName("auth") @Expose val auth: String,
    @SerializedName("message") @Expose val message: String
)

data class GetAddressModel (
    @SerializedName("x-access-token") @Expose val token: String,
    @SerializedName("address") @Expose val address: String,
    @SerializedName("status") @Expose val status: String,
    @SerializedName("auth") @Expose val auth: String,
    @SerializedName("message") @Expose val message: String
)


data class SendModel (
    @SerializedName("x-access-token") @Expose val token: String,
    @SerializedName("address") @Expose val address: String,
    @SerializedName("amount") @Expose val amount: Float,
    @SerializedName("comment") @Expose val comment: String,
    @SerializedName("auth") @Expose val auth: String,
    @SerializedName("message") @Expose val message: String,
    @SerializedName("txid") @Expose val txid: String,
    @SerializedName("status") @Expose val status: String
)

data class TransactionsModel (
    @SerializedName("x-access-token") @Expose val token: String,
    @SerializedName("txs") @Expose val txs: List<Map<String, out String>>,
    @SerializedName("status") @Expose val status: String,
    @SerializedName("auth") @Expose val auth: String,
    @SerializedName("message") @Expose val message: String
 )
