package gr.android.fakestoreapi.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import gr.android.fakestoreapi.R

@Composable
fun TopBarModal(
    leftIcon: Int = R.drawable.ic_left_arrow,
    middleIcon: Int = R.drawable.ic_toolbar,
    rightIcon: Int = R.drawable.ic_profile,
    leftIconVisibility: Boolean = true,
    middleIconVisibility: Boolean = true,
    rightIconVisibility: Boolean = true
) {
    Column {
        Spacer(modifier = Modifier.fillMaxWidth().height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            if (leftIconVisibility) {
                Image(
                    painter = painterResource(id = leftIcon),
                    contentDescription = "Left Image",
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Center Image
            if (middleIconVisibility) {
                Image(
                    painter = painterResource(id = middleIcon),
                    contentDescription = "Center Logo",
                    modifier = Modifier.height(30.dp).width(110.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Right Image
            if (rightIconVisibility) {
                Image(
                    painter = painterResource(id = rightIcon),
                    contentDescription = "Right Image",
                    modifier = Modifier.size(40.dp)
                        .clip(CircleShape)
                )
            }
        }
    }
}

@Preview
@Composable
private fun TopBarModalPreview() {
    TopBarModal(
        leftIconVisibility = true,
        middleIconVisibility = true,
        rightIconVisibility = true
    )
}