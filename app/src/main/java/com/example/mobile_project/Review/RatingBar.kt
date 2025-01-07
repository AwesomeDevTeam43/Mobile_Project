import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.mobile_project.R
import com.example.mobile_project.ui.theme.Yellow01

@Composable
fun RatingBar(
    rating: Float,
    onRatingChanged: (Float) -> Unit,
    modifier: Modifier = Modifier,
    maxRating: Int = 5,
    starSize: Int = 24,
    starColor: Color = Yellow01
) {
    val filledStars = remember(rating) { rating.toInt() }
    val unfilledStars = remember(rating) { maxRating - filledStars }

    Row(modifier = modifier, horizontalArrangement = Arrangement.Center) {
        for (i in 1..filledStars) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.baseline_star_24),
                contentDescription = null,
                tint = starColor,
                modifier = Modifier
                    .size(starSize.dp)
                    .clickable { onRatingChanged(i.toFloat()) }
            )
        }
        for (i in 1..unfilledStars) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.baseline_star_border_24),
                contentDescription = null,
                tint = starColor,
                modifier = Modifier
                    .size(starSize.dp)
                    .clickable { onRatingChanged((filledStars + i).toFloat()) }
            )
        }
    }
}