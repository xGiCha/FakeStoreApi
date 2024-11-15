package gr.android.fakestoreapi.ui.navigation

@JvmInline
value class Route(val value: String)

sealed class Screen(
    val route: Route
) {

    fun Route.withArgs(vararg args: String): String {
        return listOf(this.value, *args).joinToString(separator = "/")
    }

    fun Route.withArgsFormat(vararg args: String): String {
        return buildString {
            append(this@withArgsFormat.value)
            if (args.isNotEmpty()) {
                append(args.joinToString(separator = "/", prefix = "/") { "{$it}" })
            }
        }
    }
    data object SplashScreen : Screen(Route("splashScreen"))
    data object HomeScreen : Screen(Route("homeScreen"))
    data object LoginScreen : Screen(Route("loginScreen"))
    data object ProductDetailsScreen : Screen(Route("productDetailsScreen")) {
        internal const val ARGUMENT_PRODUCT_ID = "productId"

        fun createRoute(productId: String): String =
            this.route.withArgs(productId)
    }
}