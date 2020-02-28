package com.veganafro.fragment

//import androidx.compose.Composable
//import androidx.compose.unaryPlus
import androidx.fragment.app.Fragment
//import androidx.ui.core.Text
//import androidx.ui.core.dp
//import androidx.ui.foundation.shape.corner.RoundedCornerShape
//import androidx.ui.foundation.DrawImage
//import androidx.ui.layout.ExpandedWidth
//import androidx.ui.layout.ExpandedHeight
//import androidx.ui.layout.Container
//import androidx.ui.layout.Spacing
//import androidx.ui.layout.Column
//import androidx.ui.layout.Height
//import androidx.ui.layout.Row
//import androidx.ui.material.MaterialTheme
//import androidx.ui.material.surface.Card
//import androidx.ui.material.withOpacity
//import androidx.ui.res.imageResource
//import androidx.ui.text.style.TextOverflow
//import androidx.ui.tooling.preview.Preview
import javax.inject.Inject

class NytArticleDetailsFragment @Inject constructor(
) : Fragment(R.layout.nyt_article_details_view) {
}


//@Composable
//fun NytTrendingCard() {
//    val image = +imageResource(R.drawable.header)
//
//    Container(modifier = Spacing(16.dp)) {
//        Card(shape = RoundedCornerShape(4.dp), modifier = Height(64.dp) wraps ExpandedWidth) {
//            Row {
//                Container(width = 64.dp, modifier = ExpandedHeight) {
//                    DrawImage(image)
//                }
//                Container(width = 200.dp, modifier = ExpandedHeight) {
//                    Column {
//                        Text(
//                            "Davenport, California",
//                            maxLines = 2,
//                            overflow = TextOverflow.Fade,
//                            style = (+MaterialTheme.typography()).body2
//                                .withOpacity(0.87f)
//                        )
//                        Text(
//                            "December 2018",
//                            style = (+MaterialTheme.typography()).body2
//                                .withOpacity(0.6f)
//                        )
//                    }
//                }
//            }
//        }
//    }
//}
//
//@Preview
//@Composable
//fun NewsStoryPreview() {
//    MaterialTheme {
//        NytTrendingCard()
//    }
//}
