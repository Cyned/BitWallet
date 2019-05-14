import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class LoginModel (
    @SerializedName("username") @Expose val username: String,
    @SerializedName("password") @Expose val password: String,
    @SerializedName("token") @Expose val token: String,
    @SerializedName("status") @Expose val status: String
)
