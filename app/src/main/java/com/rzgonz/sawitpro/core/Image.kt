package com.rzgonz.sawitpro.core

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

/**
 * Created by rzgonz on 17/02/23.
 *
 */

fun Modifier.image32(): Modifier {
    return this
        .width(32.dp)
        .height(32.dp)
        .clip(
            RoundedCornerShape(10.dp)
        )
}

fun Modifier.image48(): Modifier {
    return this
        .width(48.dp)
        .height(48.dp)
        .clip(
            RoundedCornerShape(10.dp)
        )
}

fun Modifier.image64(): Modifier {
    return this
        .width(64.dp)
        .height(64.dp)
        .clip(
            RoundedCornerShape(10.dp)
        )

}